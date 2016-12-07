package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Drive Backward", group = "Utility Group")
public class DriveBackward extends _AutonomousBase
{
    //Custom initialization
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        for (DcMotor lMotor : leftDriveMotors)
            lMotor.setPower(.3);
        for (DcMotor rMotor : rightDriveMotors)
            rMotor.setPower(-.3);
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        //Set the motor powers.
        for (DcMotor lMotor : leftDriveMotors)
            lMotor.setPower(-.6);
        for (DcMotor rMotor : rightDriveMotors)
            rMotor.setPower(-.6);
    }
}