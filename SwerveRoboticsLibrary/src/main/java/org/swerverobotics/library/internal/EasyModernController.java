package org.swerverobotics.library.internal;

import android.util.Log;
import com.qualcomm.hardware.modernrobotics.*;
import com.qualcomm.modernrobotics.*;
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Base class common to both easy modern servo and motor controllers. Handles glue about
 * talking to a ModernRobotics USB device, notably arming and disarming help.
 */
public abstract class EasyModernController extends ModernRoboticsUsbDevice implements IOpModeStateTransitionEvents
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String LOGGING_TAG = SynchronousOpMode.LOGGING_TAG;

    protected OpMode                        context;
    protected String                        targetName;
    protected HardwareMap.DeviceMapping     targetDeviceMapping;
    protected boolean                       readWriteRunnableIsRunning;

    enum WRITE_STATUS { IDLE, DIRTY, READ };

    protected WRITE_STATUS                  writeStatus;
    protected final AtomicLong              readCompletionCount = new AtomicLong();
    // Locking hierarchy is in the order listed
    protected final Object                  concurrentClientLock = new Object();
    protected final Object                  callbackLock         = new Object();


    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public EasyModernController(OpMode context, ModernRoboticsUsbDevice target, ModernRoboticsUsbDevice.CreateReadWriteRunnable createReadWriteRunnable) throws RobotCoreException, InterruptedException
        {
        super(target.getSerialNumber(), SwerveThreadContext.getEventLoopManager(), target.getOpenRobotUsbDevice(), createReadWriteRunnable);

        // Initialize the rest of our state
        this.context          = context;
        this.writeStatus      = WRITE_STATUS.IDLE;
        this.readWriteRunnableIsRunning = false;
        }

    //----------------------------------------------------------------------------------------------
    // Reading and writing
    //
    // The key thing here is that we will block reads to wait until any pending writes have
    // completed before issuing, as that guarantees that they will see the effect of the writes.
    // Second, we block *writes* on any pending writes so that we know they've issued.
    //----------------------------------------------------------------------------------------------

    @Override public void write(int address, byte[] data)
        {
        synchronized (this.concurrentClientLock)
            {
            synchronized (this.callbackLock)
                {
                // If there's another write ahead of us, then wait till it
                // has been sent to the USB module before we do our write (which
                // might, for example, set the same registers he was writing to a
                // now different value. Think 'motor mode' as an example).
                //
                // TODO: consider write coalescing as we do in the legacy motor controller
                while (this.writeStatus == WRITE_STATUS.DIRTY)
                    {
                    if (!waitForCallback())
                        return;         // interrupted or readWriteRunnable is dead, no point in writing
                    }

                // Write the data to the buffer and put off reads and writes until it gets out
                this.writeStatus = WRITE_STATUS.DIRTY;
                super.write(address, data);
                }
            }
        }

    @Override public byte[] read(int address, int size)
        {
        synchronized (this.concurrentClientLock)
            {
            synchronized (this.callbackLock)
                {
                // Make sure that any read we issue happens *after* any writes
                // that have been send and then *after* we complete a read cycle
                // following thereafter.
                while (this.writeStatus != WRITE_STATUS.IDLE)
                    {
                    if (!waitForCallback())
                        break;          // interrupted or readWriteRunnable is dead, just read stale data (?)
                    }
                return super.read(address, size);
                }
            }
        }

    @Override public void writeComplete() throws InterruptedException
        {
        // Any previously issued writes are now in the hands of the USB module
        synchronized (this.callbackLock)
            {
            super.writeComplete();
            // Make sure we really read after we write before read()s can continue
            if (this.writeStatus == WRITE_STATUS.DIRTY)
                this.writeStatus = WRITE_STATUS.READ;
            this.callbackLock.notifyAll();
            }
        }

    @Override public void readComplete() throws InterruptedException
        {
        synchronized (this.callbackLock)
            {
            super.readComplete();
            if (this.writeStatus==WRITE_STATUS.READ)
                this.writeStatus = WRITE_STATUS.IDLE;
            readCompletionCount.incrementAndGet();
            this.callbackLock.notifyAll();
            }
        }

    void waitForNextReadComplete()
    // TODO: it's unclear why the clients who currently use this really need to do so.
    // But it's working for now, so we'll leave it and possibly tune later.
        {
        synchronized (this.concurrentClientLock)
            {
            synchronized (this.callbackLock)
                {
                long cur = this.readCompletionCount.get();
                long target = cur + 1;
                while (this.readCompletionCount.get() < target)
                    {
                    if (!waitForCallback())
                        return;     // interrupted or readWriteRunnable is dead, deem us to have completed
                    }
                }
            }
        }

    @Override public void startupComplete()
        {
        this.readWriteRunnableIsRunning = true;
        }

    @Override public void shutdownComplete()
        {
        this.readWriteRunnableIsRunning = false;
        this.callbackLock.notifyAll();  // wake up any waiter
        }

    boolean waitForCallback()
        {
        boolean interrupted = false;
        if (this.readWriteRunnableIsRunning)
            {
            try {
                callbackLock.wait();
                }
            catch (InterruptedException e)
                {
                interrupted = true;
                Util.handleCapturedInterrupt(e);
                }
            }
        return !interrupted && this.readWriteRunnableIsRunning;
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    public abstract String getDeviceName();

    //----------------------------------------------------------------------------------------------
    // IOpModeStateTransitionEvents
    //----------------------------------------------------------------------------------------------

    public abstract void stopHardware();

    @Override synchronized public boolean onUserOpModeStop()
        {
        Log.d(LOGGING_TAG, String.format("EasyModern: auto-stopping %s...", this.getSerialNumber().toString()));
        if (this.isArmed())
            {
            this.stopHardware();  // mirror StopRobotOpMode
            try {
                this.disarm();
                }
            catch (Exception e)
                {
                Util.handleCapturedException(e);
                }
            }
        Log.d(LOGGING_TAG, "EasyModern: ... done");
        return true;    // unregister us
        }

    @Override synchronized public boolean onRobotShutdown()
        {
        Log.d(LOGGING_TAG, String.format("EasyModern: auto-closing %s...", this.getSerialNumber().toString()));

        // We actually shouldn't be here by now, having received a onUserOpModeStop()
        // after which we should have been unregistered. But we close down anyway.
        this.close();

        Log.d(LOGGING_TAG, "EasyModern: ... done");
        return true;    // unregister us
        }
    }
