package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

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
public abstract class VoltageBase extends LinearOpMode {
    public abstract void DefineOpMode();

    //Declare Motors
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor liftMotor;

    //Declare Servos
    public Servo mineralArm;

    //variables or any other data you will use later here
    public int inchConstant = 1; //if you are using encoders on your drivewheels, change this to the ratio of ticks to inches.
    public int encoderTicksperRevConstant = 288;
    public int degConstant = 1;  //and this to the ratio between ticks and turn degrees.
    public int thingsInBot = 0; //currently not used for anything.
    public boolean RobotIsGoingForwards = true;
    public int mineralPosition = 0;
    public boolean hanging = true;

    @Override
    public void runOpMode() {
        //put all initializing stuff here.  hardwaremaps, starting settings for motors and servos, etc.
        //Map the Motors
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");

        //Map the Servos
        mineralArm = hardwareMap.get(Servo.class, "mineralArm");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE); //Check 3 motors
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Don't let motors move if they're not supposed to
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //If Using Encoders
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Some of the servos are flipped, too
        mineralArm.setDirection(Servo.Direction.REVERSE); //Check

        DefineOpMode(); //I moved waitforstart inside defineopmode to make autonomous easier.
    }

    //Here is a set of methods for everything the robot needs to do.  These can be used anywhere.

    //Utility Methods

    public void DriveMotors(int speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
    }

    public void ChangeDirection() {
        leftDrive.getMode();
        if (leftDrive.getDirection() == DcMotorSimple.Direction.FORWARD
                && rightDrive.getDirection() == DcMotorSimple.Direction.REVERSE) {
            leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

            RobotIsGoingForwards = false;
        } else {
            leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

            RobotIsGoingForwards = true;
        }
    }

    public void ThingsInBotReset() {
        thingsInBot = 0;
    }

    public void OffTheLander() {

    }


    //Movement Methods

    public void DriveForwardsOrBackwards(int speed) {
        DriveMotors(speed);
    }

    public void TurnLeft(int speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(-speed);
    }

    public void TurnRight(int speed) {
        leftDrive.setPower(-speed);
        rightDrive.setPower(speed);
    }

    public void StopDriving() {
        DriveMotors(0);
    }

    public void DriveForwardsDistance(int speed, int inches) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(inches / inchConstant);
        rightDrive.setTargetPosition(inches / inchConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void DriveBackwards(int speed, int inches) {
        ChangeDirection();

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(inches / inchConstant);
        rightDrive.setTargetPosition(inches / inchConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        if (RobotIsGoingForwards == true) {

        } else {
            ChangeDirection();
        }

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnLeftDegrees(int speed, int degrees) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(-degrees / degConstant);
        rightDrive.setTargetPosition(degrees / degConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);


        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnRightDegrees(int speed, int degrees) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(degrees / degConstant);
        rightDrive.setTargetPosition(-degrees / degConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    //Arm Methods

    public void extendHook() {
        if (gamepad1.b = true) {
            liftMotor.setPower(-0.8);
            //Fix this – Thread.sleep(2000);
            liftMotor.setPower(0);
        }

    }

    public void contractHook() {
        if (gamepad1.a = true) {
            liftMotor.setPower(0.8);
            //Fix this – Thread.sleep(2000);
            liftMotor.setPower(0);
        }
    }

}
/*
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

    };
*/

