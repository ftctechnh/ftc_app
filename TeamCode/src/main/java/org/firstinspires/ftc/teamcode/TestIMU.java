package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 8/27/2017.
 */
//@Disabled
@Autonomous(name = "Original Autopark in Auto", group = "concept")
public class TestIMU extends LinearOpMode
{
   // private NewRobotFinal robot;
    @Override
    public void runOpMode() throws InterruptedException
    {
     /*   robot = new NewRobotFinal(hardwareMap);
        robot.initVuforia(hardwareMap);
        waitForStart();
        while(opModeIsActive())
        {
            //double angle = robot.selfBalStraight();
            double y = robot.gravity.yAccel;
            double z = robot.gravity.zAccel;
            double x = robot.gravity.xAccel;
            double angle = Math.atan(y/z) * 180/Math.PI;

            if(angle> 5)
            {
                robot.getDriveLeftOne().setPower(.5);
                robot.getDriveRightOne().setPower(-.5);
            }
            else if (angle < -5)
            {
                robot.getDriveLeftOne().setPower(-.5);
                robot.getDriveRightOne().setPower(.5);
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
        }
        robot.stopAllMotors();
        */
    }
}