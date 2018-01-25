package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Toggle;

import static org.directcurrent.season.relicrecovery.ToggleTelMetKt.outputLift;


/**
 * Lift for the Relic Recovery robot
 */
@SuppressWarnings("WeakerAccess")
public class Lift extends RobotComponent
{
    private DcMotor _liftMotor;

    private Toggle _powered = new Toggle();


    /**
     * Position to lift to
     */
    @SuppressWarnings("unused")
    public enum Position
    {
        LOW(0) ,
        MID_LOW(1500) ,
        MID_HIGH(3000) ,
        HIGH(4500);

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

        _liftMotor = mapper.mapMotor("liftMotor" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * @param POSITION Sets the position of the lift motor
     */
    @SuppressWarnings("unused")
    public void toPos(final Position POSITION)
    {
        _liftMotor.setTargetPosition(POSITION.encoderCount());
    }


    /**
     * Runs the lift given a specific power
     *
     * @param POWER_VALUE Power value to set to the lift
     */
    public void run(double POWER_VALUE)
    {
        _liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        _liftMotor.setPower(POWER_VALUE);


        if(outputLift)
        {
            _outputTelMet();
        }
    }


    /**
     * Resets the lift encoder count- be *very* careful when using this
     */
    public void resetEncoder()
    {
        _liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftMotor.setTargetPosition(0);
        _liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    /**
     * Outputs telemetry to current OpMode
     */
    private void _outputTelMet()
    {
        base.telMet().write("--- Lift ---");
        base.telMet().tagWrite("Power" , _liftMotor.getPower());
        base.telMet().tagWrite("Position" , _liftMotor.getCurrentPosition());
        base.telMet().tagWrite("Target" , _liftMotor.getTargetPosition());
        base.telMet().newLine();
        base.telMet().newLine();
    }


    /**
     * Stops the lift
     */
    @Override
    public void stop()
    {
        _liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftMotor.setPower(0);
    }
}
