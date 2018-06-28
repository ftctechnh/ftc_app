package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.TileRunnerRobotHardware;

@TeleOp(name="TileRunnerTele", group="TeleOpMode")

public class TileRunnerTeleOpNEW extends LinearOpMode
{
    TileRunnerRobotHardware robot = new TileRunnerRobotHardware();

    public void runOpMode() throws InterruptedException
    {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Robot has initiated!");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Robot has started!");
        telemetry.update();

        while (opModeIsActive())
        {
            robot.topLeftMotor.setPower(-gamepad1.left_stick_y); //Controls top left motor
            robot.bottomLeftMotor.setPower(-gamepad1.left_stick_y); //Controls bottom left motor
            robot.topRightMotor.setPower(-gamepad1.right_stick_y); //Controls top right motor
            robot.bottomRightMotor.setPower(-gamepad1.right_stick_y); //Controls bottom right motor
            FlagWave();
            idle();
        }
    }

    public void FlagWave() throws InterruptedException
    {
        Thread.sleep(1000);
        robot.flagServo.setPosition(0.6);
        Thread.sleep(1000);
        robot.flagServo.setPosition(0.3);
    }
}
