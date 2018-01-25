package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.RelicGrabber;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;


/**
 * Relic Grabber for our Relic Recovery robot
 */
public class RelicGrabber extends RobotComponent
{
    private Servo _grabber;
    private DcMotor _twister;


    /**
     * Holds the states of the grabber servo
     */
    public enum GrabState
    {
        IN,
        OUT,
        STOP
    }


    /**
     * Holds the states of the rotating servo
     */
    public enum RotateState
    {
        UP,
        DOWN,
        STILL
    }


    /**
     * Initializes the Relic Grabber and hardware maps it
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    @Override
    public void init(final RobotBase BASE)
    {
        super.init(BASE);

        _grabber = mapper.mapServo("grabServo", Servo.Direction.REVERSE);
        _twister = mapper.mapMotor("flipMotor", CRServo.Direction.FORWARD);
    }


    /**
     * Sets the state of the grabbing servo
     *
     * @param STATE State to set the servo to
     */
    public void setGrabState(final GrabState STATE)
    {
        switch (STATE)
        {
            case IN:
                _grabber.setPosition(0.1);
                break;

            case OUT:
                _grabber.setPosition(.25);
                break;

        }
    }


    public void runRotator(double value)
    {
        _twister.setPower(value * .3);
    }


    /**
     * Sets the state of the rotating servo
     *
     * @param STATE2 State to set the servo to
     */
    public void setRotateState(final RotateState STATE2)
    {
        switch(STATE2)
        {
            case UP:
            _twister.setPower(1);

            break;

            case DOWN:
            _twister.setPower(-1);

            break;

            case STILL:
            _twister.setPower(0.00);

        }
    }


    /**
     * Stops the relic grabber
     */
    @Override
    public void stop()
    {
        setGrabState(GrabState.STOP);
        setRotateState(RotateState.STILL);
    }
}






