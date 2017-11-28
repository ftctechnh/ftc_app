package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilToggle;

import static org.directcurrent.season.relicrecovery.ToggleTelMetKt.outputLift;


/**
 * Lift for the Relic Recovery robot
 */
@SuppressWarnings("WeakerAccess")
public class Lift extends RobotComponent
{
    public DcMotor _liftMotor;

    private UtilToggle _powered = new UtilToggle();


    /**
     * Position to lift to
     */
    @SuppressWarnings("unused")
    enum Position
    {
        LOW(0) ,
        MID_LOW(200) ,
        MID_HIGH(400) ,
        HIGH(600);

        private int _encoderPos;

        /**
         * Encoder count of given position
         *
         * @param ENCODER_POS Given encoder count of position
         */
        Position(final int ENCODER_POS)
        {
            _encoderPos = ENCODER_POS;
        }


        /**
         * @return Returns the encoder count of the given position
         */
        public int encoderCount()
        {
            return _encoderPos;
        }
    }


    /**
     * Initializes the lift- lift motor is set to RUN_T0_POSITION. This is to keep it staying up
     * when we let go of the controls
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);

        _liftMotor = mapper.mapMotor("liftMotor" , DcMotorSimple.Direction.REVERSE);
        _liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    /**
     * @param POSITION Sets the position of the lift motor
     */
    @SuppressWarnings("unused")
    public void toPos(final Position POSITION)
    {
        _liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        _liftMotor.setTargetPosition(POSITION.encoderCount());
    }


    /**
     * Runs the lift given a specific power
     *
     * @param POWER_VALUE Power value to set to the lift
     */
    public void run(double POWER_VALUE)
    {
        final int CLOSE_ENOUGH = 2;
        final int LOW_BOUND = -20;

        // Set target position the moment the joystick isn't moved. But only once!
        if(_powered.isPressed(POWER_VALUE != 0))
        {
            _liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            _liftMotor.setTargetPosition(_liftMotor.getCurrentPosition());
        }


        if(POWER_VALUE == 0)
        {
            // If we're not quite at the target
            if(Math.abs(_liftMotor.getTargetPosition() - _liftMotor.getCurrentPosition()) >
                    CLOSE_ENOUGH)
            {
                _liftMotor.setPower(1);
            }
            else
            {
                _liftMotor.setPower(0);
            }
        }
        else                        // Manual override always takes priority :)
        {
            if(_liftMotor.getCurrentPosition() <= LOW_BOUND)
            {
                _liftMotor.setPower(0);
            }
            else
            {
                _liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                _liftMotor.setPower(POWER_VALUE);
            }
        }

        if(outputLift)
        {
            _outputTelmet();
        }
    }


    /**
     * Resets encoder counts. Be *very* careful when using this!
     */
    public void resetEncoder()
    {
        _liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    /**
     * Outputs telemetry to current OpMode
     */
    private void _outputTelmet()
    {
        base.telMet().write("--- Lift ---");
        base.telMet().tagWrite("Power" , _liftMotor.getPower());
        base.telMet().newLine();
        base.telMet().tagWrite("Position" , _liftMotor.getCurrentPosition());
        base.telMet().newLine();
        base.telMet().tagWrite("Target" , _liftMotor.getTargetPosition());
        base.telMet().newLine();
        base.telMet().newLine();
    }
}
