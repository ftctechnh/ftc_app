package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 10/20/2017.
 */
@Autonomous(name = "Test Ramp", group = "test")
public class AutonomousTest extends LinearOpMode
{
    private TankBase robot;
    public void runOpMode()
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        telemetry.addData("Yaw ", robot.getYaw());
        telemetry.update();
        telemetry.update();
        robot.pivot_IMU(45f,.25);
        robot.driveStraight_In(60f);
        while(!gamepad1.a)
        {
            telemetry.addData("Roboangle= ", robot.getYaw());
            telemetry.update();
        }

        //robot.driveStraight_In(36, .5);
        /*while(!gamepad1.a)
        {
            telemetry.addData("Yaw ", robot.getYaw());
            telemetry.update();
        }/
        robot.pivot_IMU(-90, .25);
  /*      while(!gamepad1.a)
        {
            telemetry.addData("Yaw ", robot.getYaw());
            telemetry.update();
        }/
        robot.driveStraight_In(3);
        sleep(100);
        robot.driveStraight_In(2, .2);
        */
    }
}
