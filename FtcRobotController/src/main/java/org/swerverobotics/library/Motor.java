package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;

import java.util.*;

//==================================================================================================

public class Motor
    {
    public static IMotor Create(DcMotor motor)
        {
        return new MotorOnMotor(motor);
        }

    public static IMotor Create(com.qualcomm.robotcore.hardware.Servo servo)
        {
        return new MotorOnServo(servo);
        }
    }

//==================================================================================================

class MotorServoBase implements IDisplayNameable
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected String  displayName = "";

    //----------------------------------------------------------------------------------------------
    // IDisplayNameable
    //----------------------------------------------------------------------------------------------

    public String displayName()
        {
        return this.displayName;
        }
    public void setDisplayName(String displayName)
        {
        this.displayName = displayName;
        }

    /**
     * Set the display name of the object as it is found in the indicated hardware mapping
     */
    protected <T> void setDisplayName(HardwareMap.DeviceMapping<T> mapping, Object o)
        {
        for (Map.Entry<String, T> entry : mapping.entrySet())
            {
            if (entry.getValue() == o)
                {
                this.setDisplayName(entry.getKey());
                return;
                }
            }
        }
    }

//==================================================================================================

/**
 * An implementation of IMotor on top of a DC motor connected
 * to a HiTechnic or a ModernRobotics motor controller.
 */
class MotorOnMotor extends MotorServoBase implements IMotor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    DcMotor           thunkingMotor;
    DcMotorController thunkingController;
    IMotorEncoder     encoder;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    MotorOnMotor(DcMotor motor)
        {
        // Do a check so we can work with ANY DcMotor, just for completeness.
        // Avoids double thunking, which won't work.
        if (motor.getController() instanceof ThunkingMotorController)
            {
            this.thunkingController = motor.getController();
            }
        else
            {
            this.thunkingController = new ThunkingMotorController(motor.getController());
            }
        this.thunkingMotor  = new DcMotor(this.thunkingController, motor.getPortNumber());
        this.encoder        = new MotorEncoder(this.thunkingController, this.thunkingMotor.getPortNumber(), 1440);

        // Set a name for us if we can get one from the hardware
        this.setDisplayName(SynchronousOpMode.getThreadThunker().hardwareMap.dcMotor, motor);
        }

    //----------------------------------------------------------------------------------------------
    // IMotor
    //----------------------------------------------------------------------------------------------

    public boolean reflected()
        {
        return this.thunkingMotor.getDirection() == DcMotor.Direction.REVERSE;
        }

    public void setReflected(boolean reflected)
        {
        this.thunkingMotor.setDirection(reflected ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        }

    public double power()
        {
        return this.thunkingMotor.getPower();
        }

    public void setPower(double power)
        {
        this.thunkingMotor.setPower(power);
        }

    public IMotorEncoder encoder()
        {
        return this.encoder;
        }
    }

//==================================================================================================

class MotorEncoder implements IMotorEncoder
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    DcMotorController thunkingController;
    int               portNumber;
    int               hwPositionsPerRevolution;
    double            hwBias = 0;
    double            positionsPerRevolution = 1.0;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    MotorEncoder(DcMotorController thunkingController, int portNumber, int hwPositionsPerRevolution)
        {
        this.thunkingController       = thunkingController;
        this.portNumber               = portNumber;
        this.hwPositionsPerRevolution = hwPositionsPerRevolution;
        }

    //----------------------------------------------------------------------------------------------
    // IMotorEncoder
    //----------------------------------------------------------------------------------------------

    public double position()
        {
        double hw = this.thunkingController.getMotorCurrentPosition(this.portNumber) - this.hwBias;
        return hw / hwPositionsPerRevolution * positionsPerRevolution;
        }
    public void setPosition(double position)
        {
        double hw = position / positionsPerRevolution * hwPositionsPerRevolution;
        this.hwBias = thunkingController.getMotorCurrentPosition(this.portNumber) - hw;
        }

    public double positionsPerRevolution()
        {
        return this.positionsPerRevolution;
        }
    public void setPositionsPerRevolution(double positionsPerRevolution)
        {
        // The invariant here is that the current position does not change
        // as we adjust the positions per revolution
        double position = this.position();
        this.positionsPerRevolution = positionsPerRevolution;
        this.setPosition(position);
        }
    }

//==================================================================================================

/**
 * An implementation of IMotor on top of a continuous rotation servo connected
 * to a HiTechnic or a ModernRobotics servo controller.
 */
class MotorOnServo extends MotorServoBase implements IMotor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    com.qualcomm.robotcore.hardware.Servo servo;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    MotorOnServo(com.qualcomm.robotcore.hardware.Servo servo)
        {
        this.servo = servo;
        }

    //----------------------------------------------------------------------------------------------
    // IMotor
    //----------------------------------------------------------------------------------------------

    public boolean reflected()
        {
        // Not yet implemented
        return false;
        }
    public void setReflected(boolean reflected)
        {
        // Not yet implemented
        }

    public double power()
        {
        // Not yet implemented
        return 0;
        }
    public void setPower(double power)
        {
        // Not yet implemented
        }

    public IMotorEncoder encoder()
        {
        // A motor on a servo can't have an associated encoder as the hardware
        // doesn't support that
        return null;
        }
    }

