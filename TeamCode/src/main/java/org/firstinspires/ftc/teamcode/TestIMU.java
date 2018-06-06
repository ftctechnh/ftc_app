package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 8/27/2017.
 */

@Autonomous(name = "Original Autopark in Auto", group = "concept")
public class TestIMU extends LinearOpMode
{
    private ParadeBot robot;
    @Override
    public void runOpMode() throws InterruptedException
    {
        robot = new ParadeBot(hardwareMap);
        waitForStart();
        while(opModeIsActive())
        {
            double y = robot.gravity.yAccel;
            double z = robot.gravity.zAccel;
            double x = robot.gravity.xAccel;
            double angle = Math.atan(y/z) * 180/Math.PI;

            if(angle> 4)
            {
                robot.getDriveLeftOne().setPower(.25);
                robot.getDriveRightOne().setPower(-.25);
            }
            else if (angle < -4)
            {
                robot.getDriveLeftOne().setPower(-.25);
                robot.getDriveRightOne().setPower(.25);
            }
            else
            {
                robot.stopDriveMotors();
            }
            telemetry.addData("Angle atan y/z",angle);
            telemetry.addData("y", y);
            telemetry.addData("z", z);
            telemetry.addData("x", x);
            telemetry.update();
            robot.updateIMUValues();
        }
        robot.stopAllMotors();
    }
}