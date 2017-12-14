package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;


/**
 * Lift for the Relic Recovery robot
 */
@SuppressWarnings("WeakerAccess")
public class Lift extends RobotComponent
{
    public DcMotor _liftMotor;


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


    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);

        _liftMotor = mapper.mapMotor("liftMotor" , DcMotorSimple.Direction.REVERSE);
        _liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
    public void run(final double POWER_VALUE)
    {
        _liftMotor.setPower(POWER_VALUE);
    }
}
