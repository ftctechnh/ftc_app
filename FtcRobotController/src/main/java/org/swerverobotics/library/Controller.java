package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

//==================================================================================================

/**
 * An implementation of DcMotorController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain.
 */
class ThunkingMotorController implements DcMotorController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    DcMotorController targetController;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    ThunkingMotorController(DcMotorController targetController)
        {
        this.targetController = targetController;
        }

    //----------------------------------------------------------------------------------------------
    // DcMotorController interface
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        class Thunk extends ResultableAction<String>
            {
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getDeviceName();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public int getVersion()
        {
        class Thunk extends ResultableAction<Integer>
            {
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getVersion();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void close()
        {
        class Thunk extends WaitableAction
            {
            @Override public void evaluateOnLoopThread()
                {
                targetController.close();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void setMotorControllerDeviceMode(DcMotorController.DeviceMode mode)
        {
        class Thunk extends WaitableAction
            {
            DcMotorController.DeviceMode mode;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setMotorControllerDeviceMode(mode);
                }
            }
        Thunk thunk = new Thunk();
        thunk.mode = mode;
        thunk.dispatch();
        }

    @Override public DcMotorController.DeviceMode getMotorControllerDeviceMode()
        {
        class Thunk extends ResultableAction<DcMotorController.DeviceMode>
            {
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getMotorControllerDeviceMode();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setMotorChannelMode(int channel, DcMotorController.RunMode mode)
        {
        class Thunk extends WaitableAction
            {
            int channel;
            DcMotorController.RunMode mode;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setMotorChannelMode(channel, mode);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.mode = mode;
        thunk.dispatch();
        }

    @Override public DcMotorController.RunMode getMotorChannelMode(int channel)
        {
        class Thunk extends ResultableAction<DcMotorController.RunMode>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getMotorChannelMode(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setMotorPower(int channel, double power)
        {
        class Thunk extends WaitableAction
            {
            int channel;
            double power;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setMotorPower(channel, power);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.power = power;
        thunk.dispatch();
        }

    @Override public double getMotorPower(int channel)
        {
        class Thunk extends ResultableAction<Double>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getMotorPower(channel);
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
        class Thunk extends WaitableAction
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setMotorPowerFloat(channel);
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
        class Thunk extends ResultableAction<Boolean>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getMotorPowerFloat(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setMotorTargetPosition(int channel, int position)
        {
        class Thunk extends WaitableAction
            {
            int channel;
            int position;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setMotorTargetPosition(channel, position);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.position = position;
        thunk.dispatch();
        }

    @Override public int getMotorTargetPosition(int channel)
        {
        class Thunk extends ResultableAction<Integer>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getMotorTargetPosition(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public int getMotorCurrentPosition(int channel)
        {
        class Thunk extends ResultableAction<Integer>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getMotorCurrentPosition(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setGearRatio(int channel, double ratio)
        {
        class Thunk extends WaitableAction
            {
            int channel;
            double ratio;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setGearRatio(channel, ratio);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.ratio = ratio;
        thunk.dispatch();
        }

    @Override public double getGearRatio(int channel)
        {
        class Thunk extends ResultableAction<Double>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getGearRatio(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setDifferentialControlLoopCoefficients(int channel, DifferentialControlLoopCoefficients pid)
        {
        class Thunk extends WaitableAction
            {
            int channel;
            DifferentialControlLoopCoefficients pid;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setDifferentialControlLoopCoefficients(channel, pid);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.pid = pid;
        thunk.dispatch();
        }

    @Override public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int channel)
        {
        class Thunk extends ResultableAction<DifferentialControlLoopCoefficients>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getDifferentialControlLoopCoefficients(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }

    }

//==================================================================================================

/**
 * An implementation of ServoController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain.
 */
class ThunkingServoController implements ServoController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    ServoController targetController;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    ThunkingServoController(ServoController targetController)
        {
        this.targetController = targetController;
        }

    //----------------------------------------------------------------------------------------------
    // ServoController interface
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        class Thunk extends ResultableAction<String>
            {
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getDeviceName();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public int getVersion()
        {
        class Thunk extends ResultableAction<Integer>
            {
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getVersion();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void close()
        {
        class Thunk extends WaitableAction
            {
            @Override public void evaluateOnLoopThread()
                {
                targetController.close();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void pwmEnable()
        {
        class Thunk extends WaitableAction
            {
            @Override public void evaluateOnLoopThread()
                {
                targetController.pwmEnable();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void pwmDisable()
        {
        class Thunk extends WaitableAction
            {
            @Override public void evaluateOnLoopThread()
                {
                targetController.pwmDisable();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public ServoController.PwmStatus getPwmStatus()
        {
        class Thunk extends ResultableAction<ServoController.PwmStatus>
            {
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getPwmStatus();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setServoPosition(int channel, double position)
        {
        class Thunk extends WaitableAction
            {
            int channel;
            double position;
            @Override public void evaluateOnLoopThread()
                {
                targetController.setServoPosition(channel, position);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.position = position;
        thunk.dispatch();
        }

    @Override public double getServoPosition(int channel)
        {
        class Thunk extends ResultableAction<Double>
            {
            int channel;
            @Override public void evaluateOnLoopThread()
                {
                this.result = targetController.getServoPosition(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.channel = channel;
        thunk.dispatch();
        return thunk.result;
        }
    }