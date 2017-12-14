package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilBasic;


/**
 * Drivetrain for the Relic Recovery Robot
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class Drivetrain extends RobotComponent
{
    // Power multipliers dependant upon state
    private final double _FORWARD_MULTIPLIER = 1;
    private final double _REVERSE_MULTIPLIER = -1;
    private final double _FAST_MULTIPLIER = 1;
    private final double _SLOW_MULTIPLIER = .5;
    private final double _STOP_MULTIPLIER = 0;

    // Motors
    public DcMotor leftMotor;
    public DcMotor rightMotor;

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

        leftMotor = mapper.mapMotor("leftDrive" , DcMotorSimple.Direction.REVERSE);
        rightMotor = mapper.mapMotor("rightDrive" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * @return Returns the current state of the drivetrain
     */
    public State state()
    {
        return _state;
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
                break;

            case FORWARD_SLOW:
                _powerMultiplier = _FORWARD_MULTIPLIER * _SLOW_MULTIPLIER;
                break;

            case REVERSE_FAST:
                _powerMultiplier = _REVERSE_MULTIPLIER * _FAST_MULTIPLIER;
                break;

            case REVERSE_SLOW:
                _powerMultiplier = _REVERSE_MULTIPLIER * _SLOW_MULTIPLIER;

            case STOP:
                _powerMultiplier = _STOP_MULTIPLIER;
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


        leftMotor.setPower(drivePower + rotatePower);
        rightMotor.setPower(drivePower - rotatePower);
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
}
