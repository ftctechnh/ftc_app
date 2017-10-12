package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 8/27/2017.
 */
@Autonomous(name = "Test Stuff", group = "concept")
public class TestLinearOp extends LinearOpMode
{
    private TankBase robot;
    @Override
    public void runOpMode() throws InterruptedException
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        /*
        robot.driveStraight_In(24);
        //telemetry.addData("R Encoders", robot.getDriveRightOne().)
        while(gamepad1.a == false)
        {

        }
        robot.driveStraight_In(48);
        while(gamepad1.a == false)
        {

        }
      //  robot.driveStraight_In(3);
      */
        robot.spin_Left(-90);
        while(!gamepad1.a)
        {
            telemetry.addData("L Enc", robot.getLeftEncoderPos());
            telemetry.update();
        }
        robot.spin_Left(-90);
        while(!gamepad1.a)
        {

        }
        robot.spin_Right(90);
        while(!gamepad1.a)
        {

        }
        robot.spin_Right(-90);
        while(!gamepad1.a)
        {

        }
        robot.pivot(90);
        while(!gamepad1.a)
        {

        }
        robot.pivot(-90);
        while(!gamepad1.a)
        {

        }
    }
}
