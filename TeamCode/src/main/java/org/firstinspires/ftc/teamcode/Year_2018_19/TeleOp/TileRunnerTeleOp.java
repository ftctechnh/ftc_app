package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.TileRunnerRobotHardware;

@TeleOp(name="TileRunnerTeleOp", group="TeleOpMode")

public class TileRunnerTeleOp extends OpMode
{
    //Creates robot hardware.
    TileRunnerRobotHardware robot = new TileRunnerRobotHardware();

    //Robot initiates hardware and components.
    public void init()
    {
        robot.init(hardwareMap);
        telemetry.addData("Status", "Robot has initiated!");
        telemetry.update();
    }

    //Robot starts.
    public void start()
    {
        telemetry.addData("Status", "Robot has started!");
        telemetry.update();
    }

    //Called repeatedly after robot starts.
    public void loop()
    {
        robot.topLeftMotor.setPower(-gamepad1.left_stick_y); //Controls top left motor
        robot.bottomLeftMotor.setPower(-gamepad1.left_stick_y); //Controls bottom left motor
        robot.topRightMotor.setPower(-gamepad1.right_stick_y); //Controls top right motor
        robot.bottomRightMotor.setPower(-gamepad1.right_stick_y); //Controls bottom right motor
    }

    //Robot ends.
    public void stop()
    {
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }

    public void FlagWave() throws InterruptedException
    {
        Thread.sleep(1000);
        robot.flagServo.setPosition(0.6);
        Thread.sleep(1000);
        robot.flagServo.setPosition(0.3);
    }
}