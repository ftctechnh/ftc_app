package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 8/27/2017.
 */
@Disabled
@Autonomous(name = "IMU DRIVE TESTS", group = "concept")
public class TestIMU extends LinearOpMode
{
    private TankBase robot;
    @Override
    public void runOpMode() throws InterruptedException
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        while(opModeIsActive())
        {

            robot.updateIMUValues();
            //double angle = robot.selfBalStraight();
            double y = robot.gravity.yAccel;
            double z = robot.gravity.zAccel;
            double angle = Math.atan(y/z) * 180/Math.PI;

            if(angle> 5){
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
                robot.stopAllMotors();
            }

            telemetry.addData("Angle ",angle);
            telemetry.addData("y", y);
            telemetry.addData("z", z);
            telemetry.update();
        }
    }
}