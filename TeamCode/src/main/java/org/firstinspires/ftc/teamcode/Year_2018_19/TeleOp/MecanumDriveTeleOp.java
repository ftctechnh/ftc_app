package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.RobotHardware;

@TeleOp(name="MecanumDriveTeleOp", group="TeleOpMode")

public class MecanumDriveTeleOp extends OpMode
{
    //Creates robot hardware.
    RobotHardware myRobot = new RobotHardware();

    double power = 0.5;

    //Robot initiates hardware and components.
    public void init()
    {
        myRobot.init(hardwareMap);
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
        if (gamepad1.left_bumper) //If left bumper.
        {
            //Power low
            power = 0.5;
        }
        else if (gamepad1.right_bumper) //If right bumper.
        {
            //Power high
            power = 1;
        }

        if (gamepad1.left_stick_y <= -0.5) //If left stick forward
        {
            //Move forward
            myRobot.topLeftMotor.setPower(power);
            myRobot.topRightMotor.setPower(power);
            myRobot.bottomLeftMotor.setPower(power);
            myRobot.bottomRightMotor.setPower(power);
        }
        else if (gamepad1.left_stick_y >= 0.5) //If left stick backward
        {
            //Move backward
            myRobot.topLeftMotor.setPower(-power);
            myRobot.topRightMotor.setPower(-power);
            myRobot.bottomLeftMotor.setPower(-power);
            myRobot.bottomRightMotor.setPower(-power);
        }
        else
        {
            //Stop
        }

        if (gamepad1.left_stick_x <= -0.5) //If left stick left
        {
            //Strafe left
            myRobot.topLeftMotor.setPower(power);
            myRobot.topRightMotor.setPower(-power);
            myRobot.bottomLeftMotor.setPower(-power);
            myRobot.bottomRightMotor.setPower(power);
        }
        else if (gamepad1.left_stick_x >= 0.5) //If left stick right
        {
            //Strafe right
            myRobot.topLeftMotor.setPower(-power);
            myRobot.topRightMotor.setPower(power);
            myRobot.bottomLeftMotor.setPower(power);
            myRobot.bottomRightMotor.setPower(-power);
        }
        else
        {
            //Stop
        }

        if(gamepad1.right_stick_x <= -0.5) //If right stick left
        {
            //Rotate left
            myRobot.topLeftMotor.setPower(-power);
            myRobot.topRightMotor.setPower(power);
            myRobot.bottomLeftMotor.setPower(-power);
            myRobot.bottomRightMotor.setPower(power);
        }
        else if (gamepad1.right_stick_x >= 0.5) //If right stick right
        {
            //Rotate right
            myRobot.topLeftMotor.setPower(power);
            myRobot.topRightMotor.setPower(-power);
            myRobot.bottomLeftMotor.setPower(power);
            myRobot.bottomRightMotor.setPower(-power);
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