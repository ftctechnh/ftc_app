package org.swerverobotics.library.internal;

import android.util.Log;
import com.qualcomm.hardware.*;
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
    protected EventLoopManager              eventLoopManager;
    protected boolean                       isArmed;
    protected String                        targetName;
    protected HardwareMap.DeviceMapping     targetDeviceMapping;
    protected final RobotUsbDevice          robotUsbDevice;

    enum WRITE_STATUS { IDLE, DIRTY, READ };

    protected WRITE_STATUS                  writeStatus;
    protected final AtomicLong              readCompletionCount = new AtomicLong();
    // Locking hierarchy is in the order listed
    protected final Object                  concurrentClientLock = new Object();
    protected final Object                  callbackLock         = new Object();


    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public EasyModernController(OpMode context, ModernRoboticsUsbDevice target, ReadWriteRunnableHandy readWriteRunnable) throws RobotCoreException, InterruptedException
        {
        // We *have to* give a live ReadWriteRunnable to our parent constructor, so we grudgingly do so
        super(target.getSerialNumber(), SwerveThreadContext.getEventLoopManager(), readWriteRunnable);

        // Wait until the thing actually gets going. We need it to get to the point where the
        // 'this.running=true' has at least been set so that the 'this.running=false' we are
        // about to do in the close() in next line down will sequence correctly against it.
        readWriteRunnable.waitUntilRunGetsGoing();

        // Then shut it down right away, because we want to start disarmed until we fully configure
        closeModernRoboticsUsbDevice(this);

        // Initialize the rest of our state
        this.context          = context;
        this.eventLoopManager = SwerveThreadContext.getEventLoopManager();
        this.isArmed          = false;
        this.writeStatus      = WRITE_STATUS.IDLE;

        ReadWriteRunnableStandard readWriteRunnableStandard = MemberUtil.getReadWriteRunnableModernRoboticsUsbDevice(target);
        ReadWriteRunnableUsbHandler handler                 = MemberUtil.getHandlerOfReadWriteRunnableStandard(readWriteRunnableStandard);
        this.robotUsbDevice                                 = MemberUtil.getRobotUsbDeviceOfReadWriteRunnableUsbHandler(handler);
        }

    //----------------------------------------------------------------------------------------------
    // Arming and disarming
    //----------------------------------------------------------------------------------------------

    public boolean isArmed()
        {
        return this.isArmed;
        }

    public abstract void arm();

    public abstract void disarm();

    static void closeModernRoboticsUsbDevice(ModernRoboticsUsbDevice usbDevice)
    // Close down the usbDevice in a robust and reliable way
        {
        // Get access to the state
        ExecutorService service = MemberUtil.getExecutorServiceModernRoboticsUsbDevice(usbDevice);
        ReadWriteRunnableStandard readWriteRunnableStandard = MemberUtil.getReadWriteRunnableModernRoboticsUsbDevice(usbDevice);

        // Stop accepting new work
        service.shutdown();

        // Set a dummy handler so that we don't end up closing the actual FT_device.
        // Note that this overwrites a 'final' member variable, so there's a slight
        // risk of running into optimization problems, but we live with that.
        RobotUsbDevice robotUsbDevice = new DummyRobotUsbDevice();
        ReadWriteRunnableUsbHandler dummyHandler = new ReadWriteRunnableUsbHandler(robotUsbDevice);
        MemberUtil.setHandlerOfReadWriteRunnableStandard(readWriteRunnableStandard, dummyHandler);

        // Set 'running' to false; this fixes a race condition
        MemberUtil.setRunningReadWriteRunnableStandard(readWriteRunnableStandard, false);

        // Ok: actually carry out the close
        readWriteRunnableStandard.close();

        // Wait until the thread terminates
        Util.awaitTermination(service);
        }

    void installReadWriteRunnable(ModernRoboticsUsbDevice usbDevice, int cbMonitor, int ibStart)
        {
        try
            {
            ExecutorService service = Executors.newSingleThreadScheduledExecutor();
            ReadWriteRunnableStandard rwRunnable = new ReadWriteRunnableHandy(usbDevice.getSerialNumber(), this.robotUsbDevice, cbMonitor, ibStart, false);
            //
            MemberUtil.setExecutorServiceModernRoboticsUsbDevice(usbDevice, service);
            MemberUtil.setReadWriteRunnableModernRoboticsUsbDevice(usbDevice, rwRunnable);
            service.execute(rwRunnable);
            rwRunnable.blockUntilReady();
            rwRunnable.setCallback(usbDevice);
            this.eventLoopManager.registerSyncdDevice(rwRunnable);
            }
        catch (Exception e)
            {
            Util.handleCapturedException(e);
            }
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
                    wait(this.callbackLock);
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
                    wait(this.callbackLock);
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
                    wait(this.callbackLock);
                    }
                }
            }
        }

    void wait(Object o)
        {
        try {
            o.wait();
            }
        catch (InterruptedException e)
            {
            Util.handleCapturedInterrupt(e);
            }
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    public abstract String getDeviceName();

    //----------------------------------------------------------------------------------------------
    // Shims
    //----------------------------------------------------------------------------------------------

    /**
     * This class implements a dummy RobotUsbDevice that will apparently successfully do reads and
     * writes but doesn't actually do anything.
     */
    static class DummyRobotUsbDevice implements RobotUsbDevice
        {
        byte cbExpected = 0;
        @Override public void close()  {}
        @Override public void setBaudRate(int i) throws RobotCoreException {}
        @Override public void setDataCharacteristics(byte b, byte b1, byte b2) throws RobotCoreException  {}
        @Override public void setLatencyTimer(int i) throws RobotCoreException {}
        @Override public void purge(Channel channel) throws RobotCoreException {}
        @Override public int read(byte[] bytes) throws RobotCoreException { return this.read(bytes, bytes.length, 0/*bogus*/); }
        @Override public void write(byte[] bytes) throws RobotCoreException
            {
            // Write commands have zero-sized responses, read commands indicate their expected size
            byte bCommand = bytes[2];
            this.cbExpected = bCommand==0 ? 0/*write*/ : bytes[4] /*read*/;
            }
        @Override public int read(byte[] bytes, int cbReadExpected, int timeout) throws RobotCoreException
            {
            // Need to set the 'sync' bytes correctly, and set the sizes
            bytes[0]    = (byte)0x33;
            bytes[1]    = (byte)0xCC;
            bytes[4]    = (byte)cbExpected;
            return cbReadExpected;
            }
        }

    /**
     * A handler that let's you know when it's had methods called on it
     */
    static class InterlockingReadWriteRunnableUsbHandler extends ReadWriteRunnableUsbHandler
        {
        private ManualResetEvent methodCalled = new ManualResetEvent(false);

        public InterlockingReadWriteRunnableUsbHandler(RobotUsbDevice device)
            {
            super(device);
            }

        @Override public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException
            {
            methodCalled.set();
            super.purge(channel);
            }

        @Override public void read(int address, byte[] buffer) throws RobotCoreException, InterruptedException
            {
            methodCalled.set();
            super.read(address, buffer);
            }

        @Override public void write(int address, byte[] buffer) throws RobotCoreException, InterruptedException
            {
            methodCalled.set();
            super.write(address, buffer);
            }

        void awaitMethodCall() throws InterruptedException
            {
            this.methodCalled.waitOne();
            }
        }

    /**
     * This class is a ReadWriteRunnableStandard but one that doesn't report any errors
     * due to connection failures in its blockUntilReady(). And you can interlock with its
     * startup. And it sets it's thread name to be something recognizable. All very handy :-).
     */
    static class ReadWriteRunnableHandy extends ReadWriteRunnableStandard
        {
        public ReadWriteRunnableHandy(SerialNumber serialNumber, RobotUsbDevice device, int monitorLength, int startAddress, boolean debug)
            {
            super(serialNumber, device, monitorLength, startAddress, debug);
            InterlockingReadWriteRunnableUsbHandler handler = new InterlockingReadWriteRunnableUsbHandler(device);
            MemberUtil.setHandlerOfReadWriteRunnableStandard(this, handler);
            }

        void waitUntilRunGetsGoing() throws InterruptedException
        // We ideally want to wait until after 'running' is set and the log is written
            {
            ((InterlockingReadWriteRunnableUsbHandler)this.usbHandler).awaitMethodCall();
            }

        @Override public void run()
            {
            Thread.currentThread().setName("ReadWriteRunnableHandy.run");
            try {
                super.run();
                }
            catch (Exception e)
                {
                Log.d(LOGGING_TAG, String.format("ignoring exception thrown in rwrunhandy.run: %s", Util.getStackTrace(e)));
                }
            }

        @Override public void blockUntilReady() throws RobotCoreException, InterruptedException
            {
            // Do nothing. In particular, don't report any errors
            }
        }

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
            this.disarm();
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
