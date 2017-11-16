package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilBasic;

import org.directcurrent.season.relicrecovery.ToggleTelMetKt;


/**
 * Drivetrain for the Relic Recovery Robot
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class Drivetrain extends RobotComponent
{
    // Power multipliers dependant upon state
    private final double _FORWARD_MULTIPLIER = 1;
    private final double _REVERSE_MULTIPLIER = -1;
    private final double _FAST_MULTIPLIER = .95;        // Different motors have different maxes
    private final double _SLOW_MULTIPLIER = .5;
    private final double _STOP_MULTIPLIER = 0;

    // Motors
    private DcMotor _leftMotor;
    private DcMotor _rightMotor;

    // Drivetrain is stopped right now
    private double _powerMultiplier = _STOP_MULTIPLIER; // Current power multiplier
    private State _state = State.STOP;                  // State of the robot


    /**
     * Regulates the states of the drivetrain
     */
    public enum State
    {
        FORWARD_FAST ,
        FORWARD_SLOW ,
        REVERSE_FAST ,
        REVERSE_SLOW ,
        STOP
    }


    /**
     * Initializes the drivetrain.
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);

        _leftMotor = mapper.mapMotor("leftDrive" , DcMotorSimple.Direction.REVERSE);
        _rightMotor = mapper.mapMotor("rightDrive" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * @return Returns the current state of the drivetrain
     */
    public State state()
    {
        return _state;
    }


    /**
     * @return Returns the current encoder count of the left motor
     */
    public long leftEncoderCount()
    {
        return _leftMotor.getCurrentPosition();
    }


    /**
     * @return Returns the current encoder count of the right motor
     */
    public long rightEncoderCount()
    {
        return _rightMotor.getCurrentPosition();
    }


    /**
     * State machine- sets the state of the drivetrain
     *
     * @param STATE State to set to the drivetrain
     */
    public void setState(final State STATE)
    {
        switch(STATE)
        {
            case FORWARD_FAST:
                _powerMultiplier = _FORWARD_MULTIPLIER * _FAST_MULTIPLIER;
                _encoderOff();
                break;

            case FORWARD_SLOW:
                _powerMultiplier = _FORWARD_MULTIPLIER * _SLOW_MULTIPLIER;
                _encoderOff();
                break;

            case REVERSE_FAST:
                _powerMultiplier = _REVERSE_MULTIPLIER * _FAST_MULTIPLIER;
                _encoderOff();
                break;

            case REVERSE_SLOW:
                _powerMultiplier = _REVERSE_MULTIPLIER * _SLOW_MULTIPLIER;
                _encoderOff();
                break;

            case STOP:
                _powerMultiplier = _STOP_MULTIPLIER;
                _encoderStopReset();
        }

        _state = STATE;
    }


    /**
     * Runs the drivetrain- operation is dependant upon current drivetrain state, powers passed in,
     * and whether to scale input or not. Scaling is applied after state multipliers are applied.
     *
     * @param drivePower Power for forward/backward movement, between -1 and 1
     * @param rotatePower Power for rotational movement, between -1 and 1
     * @param scale Whether to scale input or not
     */
    public void run(double drivePower , double rotatePower , boolean scale)
    {
        // Scale first- that way the multipliers don't reduce the power to like .1 or something
        if(scale)
        {
            drivePower = UtilBasic.scaleValue(drivePower);
            rotatePower = UtilBasic.scaleValue(rotatePower);
        }

        drivePower *= _powerMultiplier;
        rotatePower *= _powerMultiplier;

        // We don't want to reverse rotation- that stays constant
        if(_state == State.REVERSE_FAST || _state == State.REVERSE_SLOW)
        {
            rotatePower *= -1;
        }


        _leftMotor.setPower(drivePower + rotatePower);
        _rightMotor.setPower(drivePower - rotatePower);


        if(ToggleTelMetKt.outputDrivetrain)
        {
            _outputTelMet();
        }
    }


    /**
     * Flips the direction of the drivetrain (forward/reverse)
     */
    public void flipDirection()
    {
        switch(_state)
        {
            case FORWARD_FAST:
                setState(State.REVERSE_FAST);
                break;

            case FORWARD_SLOW:
                setState(State.REVERSE_SLOW);
                break;

            case REVERSE_FAST:
                setState(State.FORWARD_FAST);
                break;

            case REVERSE_SLOW:
                setState(State.FORWARD_SLOW);
                break;
        }
    }


    /**
     * Flips the speed of the drivetrain (fast/slow)
     */
    public void flipSpeed()
    {
        switch(_state)
        {
            case FORWARD_FAST:
                setState(State.FORWARD_SLOW);
                break;

            case FORWARD_SLOW:
                setState(State.FORWARD_FAST);
                break;

            case REVERSE_FAST:
                setState(State.REVERSE_SLOW);
                break;

            case REVERSE_SLOW:
                setState(State.REVERSE_FAST);
                break;
        }
    }


    /**
     * Sets the encoder mode to RUN_USING_ENCODER
     *
     * Turns on the PID conroller and will hold positions
     */
    private void _encoderOn()
    {
        _leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        _rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    /**
     * Sets the encoder mode to RUN_WITHOUT_ENCODER
     *
     * This means that positions can still be set, but positions won't be held
     */
    private void _encoderOff()
    {
        _leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    /**
     * Sets the encoder mode to STOP_AND_RESET_ENCODER
     *
     * Stops all encoder shenanigans and resets counts to 0
     */
    private void _encoderStopReset()
    {
        _leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    /**
     * Outputs telemetry to current OpMode
     */
    private void _outputTelMet()
    {
        base.telMet().write("--- Drivetrain ---");
        base.telMet().tagWrite("Left Power: " , _leftMotor.getPower());
        base.telMet().tagWrite("Right Power: " , _rightMotor.getPower());
        base.telMet().newLine();
        base.telMet().tagWrite("Left Encoder: " , _leftMotor.getCurrentPosition());
        base.telMet().tagWrite("Right Encoder: " , _rightMotor.getCurrentPosition());
        base.telMet().newLine();
        base.telMet().tagWrite("Left Target: " , _leftMotor.getTargetPosition());
        base.telMet().tagWrite("Right Target: " , _rightMotor.getTargetPosition());
        base.telMet().write("------------------");
        base.telMet().newLine();
        base.telMet().newLine();
    }
}
