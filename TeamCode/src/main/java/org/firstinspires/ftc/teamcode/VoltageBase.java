package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Benla on 11/9/2018.
 */

/*
I'm filling out the structure of both team's opmodes.

Here's some more programming stuff

Relevant object types:
    - int holds a whole number
    - double and float hold decimal values, but robot hardware doesn't like these as much
    - Strings hold strings of letters/numbers/whatever.  note that these must be in caps (String not string)
        - unlike the others, the data they hold must be in quotes
    - Voids are used for instructions that don't need to output anything.  robotics methods are generally voids.


Useful Java Logic Stuff:

    -If/else statements: used to do something once when a condition is met (Ex. pushing a button in teleop)

        if (condition)
        {
            effect
        }

            -These can be chained to fit as many possible outcomes as needed:

            if (condition 1)
            {
                effect 1;
            } else if (condition 2)
            {
                effect 2;
            } else
            {
                effect if none of the other conditions are met;
            }

            -You can also specify multiple conditions

            if (condition1 && condition2)
            {
                ...
            }


    -While loop: used for doing something as long as a condition is met (Ex. having teleop controls as long as the game is running)

        while (condition)
        {
            effect;
        }


    -In java, = changes a value to what you specify...
            int myNumber = 2.

        == checks equality.
            if (myNumber == 2)
            {
                ...
            }


Also general tip, whenever you open a new set of brackets, indent everything inside it on level further than the outside.
    This makes it harder to lose track.
 */

@Disabled
public abstract class VoltageBase extends LinearOpMode
{
    public abstract void DefineOpMode();

    public DcMotor leftDrive;
    public DcMotor rightDrive;

    public int inchConstant = 1; //if you are using encoders on your drivewheels, change this to the ratio of ticks to inches.
    public int degConstant = 1;  //and this to the ratio between ticks and turn degrees.

    public int thingsInBot = 0; //currently not used for anything.

    public boolean RobotIsGoingForwards = true;

    //put any other data you will use later here.

    @Override
    public void runOpMode()
    {
        //put all initializing stuff here.  hardwaremaps, starting settings for motors and servos, etc.

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");

        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        DefineOpMode(); //I moved waitforstart inside defineopmode to make autonomous easier.
    }

    public void DriveMotors (int speed)
    {
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
    }

    public void ChangeDirection ()
    {
        leftDrive.getMode();
        if (leftDrive.getDirection() == DcMotorSimple.Direction.FORWARD
                && rightDrive.getDirection() == DcMotorSimple.Direction.REVERSE)
        {
            leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

            RobotIsGoingForwards = false;
        } else
        {
            leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

            RobotIsGoingForwards = true;
        }
    }

    public void ThingsInBotReset ()
    {
        thingsInBot = 0;
    }

    public void OffTheLander ()
    {

    }


    //Movement Methods

    public void DriveForwardsOrBackwards (int speed)
    {
        DriveMotors(speed);
    }

    public void TurnLeft (int speed)
    {
        leftDrive.setPower(-speed);
        rightDrive.setPower(speed);
    }

    public void TurnRight (int speed)
    {
        leftDrive.setPower(speed);
        rightDrive.setPower(-speed);
    }

    public void StopDriving ()
    {
        DriveMotors(0);
    }

    public void DriveForwardsDistance (int speed, int inches)
    {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(inches/inchConstant);
        rightDrive.setTargetPosition(inches/inchConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy())
        {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void DriveBackwards (int speed, int inches)
    {
        ChangeDirection();

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(inches/inchConstant);
        rightDrive.setTargetPosition(inches/inchConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy())
        {

        }

        StopDriving();

        if (RobotIsGoingForwards == true)
        {

        } else
        {
            ChangeDirection();
        }

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnLeftDegrees (int speed, int degrees)
    {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(-degrees/degConstant);
        rightDrive.setTargetPosition(degrees/degConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);


        while (leftDrive.isBusy() && rightDrive.isBusy())
        {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnRightDegrees(int speed, int degrees)
    {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(degrees/degConstant);
        rightDrive.setTargetPosition(-degrees/degConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy())
        {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    //Arm Methods

    public void RaiseArm ()
    {

    }

    public void LowerArm ()
    {

    }


    public void DumpAndReset ()
    {
        //dump
        thingsInBot = 0;
    }

    public void OpenClaw ()
    {

    }

    public void CloseClaw ()
    {

    }

}
