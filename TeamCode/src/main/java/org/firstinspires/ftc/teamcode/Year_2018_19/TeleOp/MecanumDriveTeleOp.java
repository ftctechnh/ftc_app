package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.TileRunnerRobotHardware;

@TeleOp(name="MecanumDriveTeleOp", group="TeleOpMode")
@Disabled

public class MecanumDriveTeleOp extends OpMode
{
    //Creates robot hardware.
    TileRunnerRobotHardware robot = new TileRunnerRobotHardware();

    double power = 0.5;

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
        if (gamepad1.left_bumper) //If left bumper pressed
        {
            //Power low
            power = 0.5;
        }
        else if (gamepad1.right_bumper) //If right bumper pressed
        {
            //Power high
            power = 1;
        }

        if (gamepad1.left_stick_y <= -0.5) //If left stick forward
        {
            //Move forward
            robot.topLeftMotor.setPower(power);
            robot.topRightMotor.setPower(power);
            robot.bottomLeftMotor.setPower(power);
            robot.bottomRightMotor.setPower(power);
        }
        else if (gamepad1.left_stick_y >= 0.5) //If left stick backward
        {
            //Move backward
            robot.topLeftMotor.setPower(-power);
            robot.topRightMotor.setPower(-power);
            robot.bottomLeftMotor.setPower(-power);
            robot.bottomRightMotor.setPower(-power);
        }
        else
        {
            //Stop
        }

        if (gamepad1.left_stick_x <= -0.5) //If left stick left
        {
            //Strafe left
            robot.topLeftMotor.setPower(power);
            robot.topRightMotor.setPower(-power);
            robot.bottomLeftMotor.setPower(-power);
            robot.bottomRightMotor.setPower(power);
        }
        else if (gamepad1.left_stick_x >= 0.5) //If left stick right
        {
            //Strafe right
            robot.topLeftMotor.setPower(-power);
            robot.topRightMotor.setPower(power);
            robot.bottomLeftMotor.setPower(power);
            robot.bottomRightMotor.setPower(-power);
        }
        else
        {
            //Stop
        }

        if(gamepad1.right_stick_x <= -0.5) //If right stick left
        {
            //Rotate left
            robot.topLeftMotor.setPower(-power);
            robot.topRightMotor.setPower(power);
            robot.bottomLeftMotor.setPower(-power);
            robot.bottomRightMotor.setPower(power);
        }
        else if (gamepad1.right_stick_x >= 0.5) //If right stick right
        {
            //Rotate right
            robot.topLeftMotor.setPower(power);
            robot.topRightMotor.setPower(-power);
            robot.bottomLeftMotor.setPower(power);
            robot.bottomRightMotor.setPower(-power);
        }
        else
        {
            //Stop
        }
    }

    //Robot ends.
    public void stop()
    {
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }
}