package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.GlyphGrabber;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;


/**
 * Glyph Grabber of the Relic Recovery Robot
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class GlyphGrabber extends RobotComponent
{
    private final double WHEEL_SPEED = .5;
    private final double CONVEYOR_SPEED = .5;
    private final double STOP_SPEED = 0;

    public DcMotor leftWheelMotor;
    public DcMotor rightWheelMotor;
    public DcMotor conveyorMotor;


    /**
     * States of the Glyph Grabber
     */
    public enum State
    {
        INPUT ,
        OUTPUT ,
        STOP
    }


    /**
     * Initializes the Glyph Grabber
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);

        leftWheelMotor = mapper.mapMotor("lInMotor" , DcMotorSimple.Direction.FORWARD);
        rightWheelMotor = mapper.mapMotor("rInMotor" , DcMotorSimple.Direction.REVERSE);
        conveyorMotor = mapper.mapMotor("convMotor" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * Sets the state of the glyph grabber
     *
     * @param STATE State to set the glyph grabber to
     */
    public void setState(final State STATE)
    {
        switch(STATE)
        {
            case INPUT:
                leftWheelMotor.setPower(WHEEL_SPEED);
                rightWheelMotor.setPower(WHEEL_SPEED);
                conveyorMotor.setPower(CONVEYOR_SPEED);
                break;

            case OUTPUT:
                leftWheelMotor.setPower(-WHEEL_SPEED);
                rightWheelMotor.setPower(-WHEEL_SPEED);
                conveyorMotor.setPower(-CONVEYOR_SPEED);
                break;

            case STOP:
                leftWheelMotor.setPower(STOP_SPEED);
                rightWheelMotor.setPower(STOP_SPEED);
                conveyorMotor.setPower(STOP_SPEED);
                break;
        }
    }
}
