package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.directcurrent.season.relicrecovery.drivetrain.DriveToDistance;
import org.directcurrent.season.relicrecovery.drivetrain.TurnTo;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;


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

    public ActivateForTime activateForTime;


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

        leftWheelMotor = mapper.mapMotor("lInMotor" , DcMotorSimple.Direction.REVERSE);
        rightWheelMotor = mapper.mapMotor("rInMotor" , DcMotorSimple.Direction.REVERSE);
        conveyorMotor = mapper.mapMotor("convMotor" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * Sets Glyph Grabber dependencies and performs any additional initializations required
     *
     * Call this in your robot base.init(), after all other components have been initialized.
     *
     * If you don't, you'll get NullPointerException. How about we don't? Place this where it
     * belongs
     */
    public void setDependencies()
    {
        activateForTime = new ActivateForTime(this);
    }


    /**
     * Sets the state of the glyph grabber
     *
     * @param STATE GrabState to set the glyph grabber to
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


    /**
     * Stops the Glyph Grabber
     */
    @Override
    public void stop()
    {
        setState(State.STOP);
        activateForTime.stop();
    }
}
