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
    public abstract void DefineOpMode();

    public DcMotor leftDrive;
    public DcMotor rightDrive;
    //declare

    public int thingsInBot = 0;

    @Override
    public void runOpMode()
    {
        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        //hardwaremap

        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        DefineOpMode();
    }


    //distance based things if needed

    public void DriveMotors (double speed)
    {
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
    }

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

    public void StopDrving ()
    {
        DriveMotors(0);
    }


    public void ArmsUp ()
    {

    }

    public void ArmsDown ()
    {

    }

    public void CollectorUpAndOut ()
    {

    }

    public void CollectorBackAndIn ()
    {

    }

    public void CollectAThing ()
    {
        if (thingsInBot < 2)
        {
            //collect
            thingsInBot = thingsInBot+1;
        } else
        {

        }

    }

    public void DumpAndReset ()
    {
        //dump
        thingsInBot = 0;
    }

    public void EmergencyReset ()
    {
        thingsInBot = 0;
    }


    public void ClawOpen ()
    {

    }

    public void ClawClosed ()
    {

    }


    public void OffTheLander ()
    {

    }

}
