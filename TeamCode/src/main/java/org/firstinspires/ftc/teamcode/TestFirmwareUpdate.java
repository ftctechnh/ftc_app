package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Peter on 9/18/2016.
 */

@TeleOp(name = "Firmware Update", group = "Tests")
public class TestFirmwareUpdate extends LinearOpMode
{
    //new System requires that we make a robot object
    private TestFirmwareUpdateBot robot = new TestFirmwareUpdateBot();

    public void runOpMode() throws InterruptedException
    {
        robot.init(hardwareMap);

        waitForStart();

        robot.motorOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (robot.motorOne.getCurrentPosition() <= 1000)
        {
            robot.motorOne.setPower(100);
            robot.motorTwo.setPower(100);

            telemetry.addData("Motor Pos", robot.motorOne.getCurrentPosition());
            telemetry.update();
        }
        robot.motorOne.setPower(0);
        robot.motorTwo.setPower(0);
    }
}
