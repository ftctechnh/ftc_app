package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

/**
 * An implementation of DcMotorController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain.
 */
class ThunkingMotorController implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    DcMotorController target;           // can only talk to him on the loop thread

    DeviceMode controllerMode = null;  // the last mode we know the controller to be in

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkingMotorController(DcMotorController target)
        {
        this.target = target;
        }

    static public ThunkingMotorController Create(DcMotorController target)
        {
        return target instanceof ThunkingMotorController ? (ThunkingMotorController)target : new ThunkingMotorController(target);
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private void enterReadOperation()
        {
        this.switchToMode(DeviceMode.READ_ONLY);
        }
    private void enterWriteOperation()
        {
        this.switchToMode(DeviceMode.WRITE_ONLY);
        }

    /**
     * Switch the controller to either read-only or write-only device mode.
     *
     * We assume that the only spontaneous transitions are
     *      SWITCHING_TO_READ_MODE -> READ_MODE
     * and
     *      SWITCHING_TO_WRITE_MODE -> WRITE_MODE.
     * All other transitions only happen because we request them.
     */
    private synchronized void switchToMode(DeviceMode newMode)
        {
        // Note: remember that in general the user code may choose to spawn worker threads.
        // Thus, we may have multiple, concurrent threads simultaneously trying to switch modes.
        // We deal with that by using synchronized methods, allowing only one client in at
        // a time; this gives us a sequential sequence of modes we need the controller to be in.

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
        while (this.controllerMode == DeviceMode.SWITCHING_TO_READ_MODE ||
                this.controllerMode == DeviceMode.SWITCHING_TO_WRITE_MODE)
            {
            // As getMotorControllerDeviceMode() will thunk, we need not do a yield()
            this.controllerMode = this.getMotorControllerDeviceMode();
            }

        // If he's read-write, then that's just dandy
        if (this.controllerMode == DeviceMode.READ_WRITE)
            return;

        // If he's not what we want, then ask him to switch him to what we want and
        // spin until he gets there.
        if (this.controllerMode != newMode)
            {
            this.setMotorControllerDeviceMode(newMode);
            do
                {
                // Again, a yield() is not necessary in this loop
                this.controllerMode = this.getMotorControllerDeviceMode();
                }
            while(this.controllerMode != newMode);
            }
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController interface
    //----------------------------------------------------------------------------------------------

    @Override public synchronized String getDeviceName()
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<String>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized int getVersion()
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<Integer>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized void close()
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.close();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public synchronized void setMotorControllerDeviceMode(DcMotorController.DeviceMode mode)
    // setMotorControllerDeviceMode is neither a 'read' nor a 'write' operation; it's internal
        {
        class Thunk extends NonwaitingThunk
            {
            DcMotorController.DeviceMode mode;
            @Override public void actionOnLoopThread()
                {
                target.setMotorControllerDeviceMode(mode);
                }
            }
        Thunk thunk = new Thunk();
        thunk.mode = mode;
        thunk.dispatch();

        // Required: right now we have no idea what mode the controller is in (we know what
        // we *asked* him to do, yes). Thus, our cached knowledge of his state is unknown.
        this.controllerMode = null;
        }

    @Override public synchronized DcMotorController.DeviceMode getMotorControllerDeviceMode()
    // getMotorControllerDeviceMode is neither a 'read' nor a 'write' operation; it's internal
        {
        class Thunk extends ResultableThunk<DeviceMode>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorControllerDeviceMode();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();

        // Optimization: we may as well update our knowledge about the controller's state
        this.controllerMode = thunk.result;

        return thunk.result;
        }

    @Override public synchronized void setMotorChannelMode(int channel, DcMotorController.RunMode mode)
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            int channel;
            DcMotorController.RunMode mode;
            @Override public void actionOnLoopThread()
                {
                target.setMotorChannelMode(channel, mode);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.mode = mode;
        thunk.dispatch();
        }

    @Override public synchronized DcMotorController.RunMode getMotorChannelMode(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<RunMode>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorChannelMode(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized void setMotorPower(int channel, double power)
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            int channel;
            double power;
            @Override public void actionOnLoopThread()
                {
                target.setMotorPower(channel, power);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.power = power;
        thunk.dispatch();
        }

    @Override public synchronized double getMotorPower(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<Double>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorPower(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    /**
     * Set the power level of the indicated motor to 'float'ing. Aka 'coast'ing.
     */
    @Override public synchronized void setMotorPowerFloat(int channel)
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                target.setMotorPowerFloat(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        }

    /**
     * Is the indicated motor currently floating / coasting?
     */
    @Override public synchronized boolean getMotorPowerFloat(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<Boolean>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorPowerFloat(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized void setMotorTargetPosition(int channel, int position)
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            int channel;
            int position;
            @Override public void actionOnLoopThread()
                {
                target.setMotorTargetPosition(channel, position);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.position = position;
        thunk.dispatch();
        }

    @Override public synchronized int getMotorTargetPosition(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<Integer>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorTargetPosition(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized int getMotorCurrentPosition(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<Integer>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorCurrentPosition(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized void setGearRatio(int channel, double ratio)
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            int channel;
            double ratio;
            @Override public void actionOnLoopThread()
                {
                target.setGearRatio(channel, ratio);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.ratio = ratio;
        thunk.dispatch();
        }

    @Override public synchronized double getGearRatio(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<Double>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getGearRatio(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public synchronized void setDifferentialControlLoopCoefficients(int channel, DifferentialControlLoopCoefficients pid)
        {
        this.enterWriteOperation();
        class Thunk extends NonwaitingThunk
            {
            int channel;
            DifferentialControlLoopCoefficients pid;
            @Override public void actionOnLoopThread()
                {
                target.setDifferentialControlLoopCoefficients(channel, pid);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.pid = pid;
        thunk.dispatch();
        }

    @Override public synchronized DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int channel)
        {
        this.enterReadOperation();
        class Thunk extends ResultableThunk<DifferentialControlLoopCoefficients>
            {
            int channel;
            @Override public void actionOnLoopThread()
                {
                this.result = target.getDifferentialControlLoopCoefficients(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }
    }