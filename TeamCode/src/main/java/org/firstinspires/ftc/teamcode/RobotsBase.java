package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Benla on 10/14/2018.
 */

@Disabled
public abstract class RobotsBase extends LinearOpMode
{
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor leftArm;
    public DcMotor rightArm;
    public DcMotor raiseLeft;
    public DcMotor raiseRight;

    public abstract void DefineOpMode();

    public int inchConstant = 1;

    public int degConstant = 1;

    public boolean RobotIsGoingForwards = true;

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        leftArm = hardwareMap.dcMotor.get("leftArm");
        rightArm = hardwareMap.dcMotor.get("rightArm");
        raiseLeft = hardwareMap.dcMotor.get("raiseLeft");
        raiseRight = hardwareMap.dcMotor.get("raiseRight");


        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightArm.setDirection(DcMotorSimple.Direction.REVERSE);
        raiseRight.setDirection(DcMotorSimple.Direction.REVERSE);


        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        DefineOpMode();
    }


    //Here is a set of methods for everything the robot needs to do.  These can be used anywhere.

    //Utility Methods

    public void DriveMotors (double speed)
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


    public void OffTheLander ()
    {
        CollectorUpAndOut();

        DriveForwardsDistance(0.5, 3);

        CollectorBackAndIn();
    }


    //Movement Methods

    public void DriveForwardsOrBackwards (double speed)
    {
        DriveMotors(speed);
    }

    public void TurnLeft (double speed)
    {
        leftDrive.setPower(-speed);
        rightDrive.setPower(speed);
    }

    public void TurnRight (double speed)
    {
        leftDrive.setPower(speed);
        rightDrive.setPower(-speed);
    }

    public void StopDriving ()
    {
        DriveMotors(0);
    }

    public void DriveForwardsDistance (double speed, int inches)
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

    public void DriveBackwards (double speed, int inches)
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

    public void TurnLeftDegrees (double speed, int degrees)
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

    public void TurnRightDegrees(double speed, int degrees)
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

    public void ArmsUp ()
    {
        leftArm.setPower(0.8);
        rightArm.setPower(0.8);
        Thread.sleep(2000);

        while (leftArm.isBusy() && rightArm.isBusy())
        {
            raiseLeft.setPower(-0.1);
            raiseRight.setPower(-0.1);
        }

        leftArm.setPower(0);
        rightArm.setPower(0);
    }

    public void ArmsDown ()
    {
        leftArm.setPower(-0.25);
        rightArm.setPower(-0.25);
        Thread.sleep(2000);

        while (leftArm.isBusy()&& rightArm.isBusy())
        {
            raiseLeft.setPower(0.1);
            raiseRight.setPower(0.1);
        }

        leftArm.setPower(0);
        rightArm.setPower(0);
    }

    public void CollectorUpAndOut ()
    {
        raiseLeft.setPower(0.5);
        raiseRight.setPower(0.5);
        Thread.sleep(1000);
        raiseLeft.setPower(0);
        raiseRight.setPower(0);
    }

    public void CollectorBackAndIn ()
    {
        raiseLeft.setPower(-0.5);
        raiseRight.setPower(-0.5);
        Thread.sleep(1000);
        raiseLeft.setPower(0);
        raiseRight.setPower(0);
    }

    public void DropMarker ()
    {

    }

    public void CollectAThing ()
    {


    }

    public void DumpAndReset ()
    {

    }


}