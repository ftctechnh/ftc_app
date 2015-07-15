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

    DcMotorController target;   // can only talk to him on the loop thread

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
    // DcMotorController interface
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
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

    @Override public int getVersion()
        {
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

    @Override public void close()
        {
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

    @Override public void setMotorControllerDeviceMode(DcMotorController.DeviceMode mode)
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

    @Override public DcMotorController.RunMode getMotorChannelMode(int channel)
        {
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

    @Override public void setMotorPower(int channel, double power)
        {
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

    @Override public double getMotorPower(int channel)
        {
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

    /**
     * Set the power level of the indicated motor to 'float'ing. Aka 'coast'ing.
     */
    @Override public void setMotorPowerFloat(int channel)
        {
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

    /**
     * Is the indicated motor currently floating / coasting?
     */
    @Override public boolean getMotorPowerFloat(int channel)
        {
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

    @Override public void setMotorTargetPosition(int channel, int position)
        {
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

    @Override public int getMotorTargetPosition(int channel)
        {
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

    @Override public int getMotorCurrentPosition(int channel)
        {
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

    @Override public void setGearRatio(int channel, double ratio)
        {
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

    @Override public double getGearRatio(int channel)
        {
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

    @Override public void setDifferentialControlLoopCoefficients(int channel, DifferentialControlLoopCoefficients pid)
        {
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

    @Override public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int channel)
        {
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

    }