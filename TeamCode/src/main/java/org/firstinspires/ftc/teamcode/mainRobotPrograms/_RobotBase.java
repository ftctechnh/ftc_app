package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BaseFunctions;

import java.util.ArrayList;

public abstract class _RobotBase extends BaseFunctions
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected ArrayList <DcMotor> leftDriveMotors = new ArrayList <>(), rightDriveMotors = new ArrayList<>();
    //Other motors
    protected DcMotor harvester, flywheels, lift;
    protected Servo leftButtonPusher, rightButtonPusher;
    protected Servo capBallHolder;
    protected final double CBH_CLOSED = 0.0, CBH_OPEN = 1.0;

    // Called on initialization (once)
    protected void initializeHardware() throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        rightDriveMotors.add(initialize(DcMotor.class, "frontRight"));
        rightDriveMotors.add(initialize(DcMotor.class, "backRight"));
        for(DcMotor motor : rightDriveMotors)
            motor.setDirection(DcMotor.Direction.REVERSE);

        leftDriveMotors.add(initialize(DcMotor.class, "frontLeft"));
        leftDriveMotors.add(initialize(DcMotor.class, "backLeft"));

        /*************************** OTHER MOTORS AND SERVOS ***************************/
        harvester = initialize(DcMotor.class, "harvester");
        flywheels = initialize(DcMotor.class, "flywheels");
        flywheels.setDirection(DcMotor.Direction.REVERSE);

        lift = initialize(DcMotor.class, "lift");
        leftButtonPusher = initialize(Servo.class, "leftButtonPusher");
        leftButtonPusher.setPosition(0.5);
        rightButtonPusher = initialize(Servo.class, "rightButtonPusher");
        rightButtonPusher.setPosition(0.5);

        capBallHolder = initialize(Servo.class, "clamp");
        capBallHolder.setPosition(CBH_CLOSED);
    }

    protected void setRightPower(double power)
    {
        for (DcMotor motor : rightDriveMotors)
            motor.setPower(power);
    }

    protected void setLeftPower(double power)
    {
        for (DcMotor motor : leftDriveMotors)
            motor.setPower(power);
    }
}