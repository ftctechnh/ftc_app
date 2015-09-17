package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An implementation of DcMotorController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain. The implementation automatically
 * takes care of read and write device mode switching.
 */
public class ThunkedDCMotorController implements DcMotorController, IThunkedReadWriteListener, IThunkWrapper<DcMotorController>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private DcMotorController target;          // can only talk to him on the loop thread

    @Override public DcMotorController getWrappedTarget() { return this.target; }

    private DeviceMode        controllerMode;  // the last mode we know the controller to be in
    
    private int controllerWriteThunkKey = Thunk.getNewActionKey();
    private int controllerReadThunkKey  = Thunk.getNewActionKey();
    
    // Channel indices are one-based
    private final int[] channelWriteThunkKeys = new int[] { Thunk.nullActionKey, Thunk.getNewActionKey(), Thunk.getNewActionKey() };
    private int getChannelWriteKey(int channel) 
        { 
        return this.channelWriteThunkKeys[channel]; 
        }
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedDCMotorController(DcMotorController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        this.controllerMode = null;
        }

    static public ThunkedDCMotorController create(DcMotorController target)
        {
        return target instanceof ThunkedDCMotorController ? (ThunkedDCMotorController)target : new ThunkedDCMotorController(target);
        }

    //----------------------------------------------------------------------------------------------
    // IThunkedReadWriteListener interface
    //----------------------------------------------------------------------------------------------

    @Override public void enterReadOperation() throws InterruptedException
        {
        this.switchToMode(DeviceMode.READ_ONLY);
        }
    @Override public void enterWriteOperation() throws InterruptedException
        {
        this.switchToMode(DeviceMode.WRITE_ONLY);
        }
    @Override public int getListenerReadThunkKey()
        {
        return this.controllerWriteThunkKey;
        }
    @Override public int getListenerWriteThunkKey()
        {
        return this.controllerReadThunkKey;
        }

    /**
     * Switch the controller to either read-only or write-only device mode.
     *
     * We assume that the only spontaneous transitions are
     *      SWITCHING_TO_READ_MODE -> READ_MODE
     * and
     *      SWITCHING_TO_WRITE_MODE -> WRITE_MODE.
     * All other transitions only happen because we (or someone else) requests them.
     */
    private synchronized void switchToMode(DeviceMode newMode) throws InterruptedException
        {
        // Note: remember that in general the user code may choose to spawn worker threads.
        // Thus, we may have multiple, concurrent threads simultaneously trying to switch modes.
        // We deal with that by using synchronized public methods, allowing only one client in at
        // a time; this gives us a sequential sequence of modes we need the controller to be in.
        // This method, too, is synchronized, but that's paranoia.

        // If we don't currently know his mode, we need to ask the controller where we stand
        if (null == this.controllerMode)
            {
            this.controllerMode = this.getMotorControllerDeviceMode();
            }

        // If the controller is being stupid in returning a non-actual mode (the mock one was) 
        // then to heck with trying to keep him happy
        if (null == this.controllerMode)
            return;

        // We might have caught this guy mid transition. Wait until he settles down
        if (this.controllerMode == DeviceMode.SWITCHING_TO_READ_MODE ||
            this.controllerMode == DeviceMode.SWITCHING_TO_WRITE_MODE)
            {
            for (;;)
                {
                this.controllerMode = this.getMotorControllerDeviceMode();
                //
                if (this.controllerMode == DeviceMode.SWITCHING_TO_READ_MODE ||
                    this.controllerMode == DeviceMode.SWITCHING_TO_WRITE_MODE)
                    {
                    SynchronousOpMode.synchronousThreadIdle();
                    }
                else
                    break;
                }
            }

        // If he's read-write, then that's just dandy
        if (this.controllerMode == DeviceMode.READ_WRITE)
            return;

        // If he's not what we want, then ask him to switch him to what we want and
        // spin until he gets there.
        if (this.controllerMode != newMode)
            {
            // We need to complete any existing thunks (we could be more refined, but that suffices)
            // as, to quote Johnathan Berling:
            //
            // http://ftcforum.usfirst.org/showthread.php?4352-Legacy-Motor-controller-write-to-read-mode-amount-of-time/page3
            /*
            When the loop call finishes, all commands are sent simultaneously to the device. So, it 
            simultaneously gets put into read mode and told to change the channel mode. In this case 
            it can't comply with the command to switch the channel mode since the port is in read mode.

                Code:
                motorLeft.setTargetPosition(firstTarget);
                motorRight.setTargetPosition(-firstTarget);
                
                motorLeft.setPower(1.0);
                motorRight.setPower(1.0);
                
                wheelController.setMotorControllerDeviceMode(DcMot orController.DeviceMode.READ_ONLY);
                
            In this case, all of the lines above the READ_ONLY line won't take effect until the 
            device is placed back into write mode.
            */
            // What this says is that if you're switching to a new mode then there better not
            // be any existing commands still in the queue for that device. In effect, mode switches 
            // should (conservatively) happen at the TOP of a loop() call so that they are compatible 
            // with anything else that is issued to that controller in that call.
            // 
            // We accomplish this by waiting until any incompatible operations were executed on 
            // previous loop() calls. *New* incompatible operations are prevented from starting
            // by the fact that this controller object uses synchronized methods.
            int oppositeKey = newMode==DeviceMode.READ_ONLY ? this.controllerReadThunkKey : this.controllerWriteThunkKey;
            SynchronousOpMode.synchronousThreadWaitForLoopCycleEmptyOfActionKey(oppositeKey);

            // Tell him to switch
            this.setMotorControllerDeviceMode(newMode);
            
            // Wait until he gets there
            do
                {
                SynchronousOpMode.synchronousThreadIdle();
                this.controllerMode = this.getMotorControllerDeviceMode();
                }
            while (this.controllerMode != newMode);
            }
        }

    private void waitForWritesToChannelToDrain(int channel)
        {
        try {
            SynchronousOpMode.synchronousThreadWaitForLoopCycleEmptyOfActionKey(this.getChannelWriteKey(channel));        
            }
        catch (InterruptedException e)
            {
            Util.handleCapturedInterrupt();
            }
        }
    
    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void close()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doUntrackedWriteOperation();
        }

    @Override public synchronized int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doUntrackedReadOperation();
        }

    @Override public synchronized String getConnectionInfo()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doUntrackedReadOperation();
        }

    @Override public synchronized String getDeviceName()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doUntrackedReadOperation();
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController interface
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void setMotorControllerDeviceMode(final DeviceMode mode)
    // setMotorControllerDeviceMode is neither a 'read' nor a 'write' operation; it's internal, 
    // so we don't call doReadOperation() or doWriteOperation().
        {
        // Check arguments to rule out the transient modes which aren't supposed to be
        // set manually.
        if (!(mode==DeviceMode.READ_ONLY || mode==DeviceMode.WRITE_ONLY || mode==DeviceMode.READ_WRITE))
            throw new IllegalArgumentException("invalid DeviceMode");
        
        // Get a thunk
        ThunkForWriting thunk = (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMotorControllerDeviceMode(mode);
                }
            });
        
        // Classify the thunk as to the kind of operation that's happening on this 
        // motor controller instance
        switch (mode)
            {
        case READ_ONLY:
            thunk.addActionKey(this.controllerWriteThunkKey);
            break;
        case WRITE_ONLY:
            thunk.addActionKey(this.controllerReadThunkKey);
            break;
        case READ_WRITE:
            thunk.addActionKey(this.controllerWriteThunkKey);
            thunk.addActionKey(this.controllerReadThunkKey);
            break;
            }
        
        // Dispatch the thing!        
        thunk.doUntrackedWriteOperation();

        // Required: right now we have no idea what mode the controller is in (we know what
        // we *asked* him to do, yes). Thus, our cached knowledge of his state is unknown.
        this.controllerMode = null;
        }

    @Override public synchronized DeviceMode getMotorControllerDeviceMode()
    // getMotorControllerDeviceMode is neither a 'read' nor a 'write' operation; it's internal
        {
        DeviceMode result;
        try
            {
            result = (new ThunkForReading<DeviceMode>()
                {
                @Override protected void actionOnLoopThread()
                    {
                    this.result = target.getMotorControllerDeviceMode();
                    }
                }).doUntrackedReadOperation();
            }
        catch (RuntimeException e)
            {
            this.controllerMode = null;         // paranoia
            throw e;
            }

        // Optimization: we may as well update our knowledge about the controller's state
        this.controllerMode = result;

        return result;
        }

    @Override public synchronized void setMotorChannelMode(final int channel, final DcMotorController.RunMode mode)
        {
        (new ThunkForWriting(this.getChannelWriteKey(channel))
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMotorChannelMode(channel, mode);
                }
            }).doWriteOperation(this);
        }

    @Override public synchronized DcMotorController.RunMode getMotorChannelMode(final int channel)
        {
        return (new ThunkForReading<RunMode>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMotorChannelMode(channel);
                }
            }).doReadOperation(this);
        }

    @Override public synchronized void setMotorPower(final int channel, final double power)
        {
        (new ThunkForWriting(this.getChannelWriteKey(channel))
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMotorPower(channel, power);
                }
            }).doWriteOperation(this);
        }

    @Override public synchronized double getMotorPower(final int channel)
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMotorPower(channel);
                }
            }).doReadOperation(this);
        }

    @Override public synchronized boolean isBusy(final int channel)
        {
        this.waitForWritesToChannelToDrain(channel);
        
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.isBusy(channel);
                }
            }).doReadOperation(this);
        }

    @Override public synchronized void setMotorPowerFloat(final int channel)
        {
        (new ThunkForWriting(this.getChannelWriteKey(channel))
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMotorPowerFloat(channel);
                }
            }).doWriteOperation(this);
        }

    @Override public synchronized boolean getMotorPowerFloat(final int channel)
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMotorPowerFloat(channel);
                }
            }).doReadOperation(this);
        }

    @Override public synchronized void setMotorTargetPosition(final int channel, final int position)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMotorTargetPosition(channel, position);
                }
            }).doWriteOperation(this);
        }

    @Override public synchronized int getMotorTargetPosition(final int channel)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMotorTargetPosition(channel);
                }
            }).doReadOperation(this);
        }

    @Override public synchronized int getMotorCurrentPosition(final int channel)
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMotorCurrentPosition(channel);
                }
            }).doReadOperation(this);
        }
    }