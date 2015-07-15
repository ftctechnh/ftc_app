package org.swerverobotics.library;

import com.qualcomm.modernrobotics.ModernRoboticsNxtDcMotorController;
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
    private void exitReadOperation()
        {
        }
    private void enterWriteOperation()
        {
        this.switchToMode(DeviceMode.WRITE_ONLY);
        }
    private void exitWriteOperation()
        {
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
    private void switchToMode(DeviceMode newMode)
        {
        // On the first time through, we need to know where we stand
        if (null == this.controllerMode)
            {
            this.controllerMode = this.getMotorControllerDeviceMode();
            }

        // If the controller is being stupid (the mock one was) then to heck with
        // trying to keep him happy with all this mode stuff
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

        // If he's not what we want, then switch him to what we want and
        // spin until he gets there.
        if (this.controllerMode != newMode)
            {
            this.setMotorControllerDeviceMode(newMode);
            do
                {
                this.controllerMode = this.getMotorControllerDeviceMode();
                }
            while(this.controllerMode != newMode);
            }
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController interface
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<String>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public int getVersion()
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<Integer>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public void close()
        {
        try
            {
            this.enterWriteOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
                {
                @Override public void actionOnLoopThread()
                    {
                    target.close();
                    }
                }
            Thunk thunk = new Thunk();
            thunk.dispatch();
            }
        finally
            {
            this.exitWriteOperation();
            }
        }

    @Override public void setMotorControllerDeviceMode(DcMotorController.DeviceMode mode)
    // setMotorControllerDeviceMode is neither a 'read' nor a 'write' operation; it's internal
        {
        class Thunk extends SynchronousOpMode.WaitableAction
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
        }

    @Override public DcMotorController.DeviceMode getMotorControllerDeviceMode()
    // getMotorControllerDeviceMode is neither a 'read' nor a 'write' operation; it's internal
        {
        class Thunk extends SynchronousOpMode.ResultableAction<DcMotorController.DeviceMode>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getMotorControllerDeviceMode();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setMotorChannelMode(int channel, DcMotorController.RunMode mode)
        {
        try
            {
            this.enterWriteOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
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
        finally
            {
            this.exitWriteOperation();
            }
        }

    @Override public DcMotorController.RunMode getMotorChannelMode(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<DcMotorController.RunMode>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public void setMotorPower(int channel, double power)
        {
        try
            {
            this.exitReadOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public double getMotorPower(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<Double>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    /**
     * Set the power level of the indicated motor to 'float'ing. Aka 'coast'ing.
     */
    @Override public void setMotorPowerFloat(int channel)
        {
        try
            {
            this.enterWriteOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
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
        finally
            {
            this.exitWriteOperation();
            }
        }

    /**
     * Is the indicated motor currently floating / coasting?
     */
    @Override public boolean getMotorPowerFloat(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<Boolean>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public void setMotorTargetPosition(int channel, int position)
        {
        try
            {
            this.enterWriteOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
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
        finally
            {
            this.exitWriteOperation();
            }
        }

    @Override public int getMotorTargetPosition(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<Integer>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public int getMotorCurrentPosition(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<Integer>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public void setGearRatio(int channel, double ratio)
        {
        try
            {
            this.enterWriteOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
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
        finally
            {
            this.exitWriteOperation();
            }
        }

    @Override public double getGearRatio(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<Double>
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
        finally
            {
            this.exitReadOperation();
            }
        }

    @Override public void setDifferentialControlLoopCoefficients(int channel, DifferentialControlLoopCoefficients pid)
        {
        try
            {
            this.enterWriteOperation();
            class Thunk extends SynchronousOpMode.WaitableAction
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
        finally
            {
            this.exitWriteOperation();
            }
        }

    @Override public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int channel)
        {
        try
            {
            this.enterReadOperation();
            class Thunk extends SynchronousOpMode.ResultableAction<DifferentialControlLoopCoefficients>
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
        finally
            {
            this.exitReadOperation();
            }
        }
    }