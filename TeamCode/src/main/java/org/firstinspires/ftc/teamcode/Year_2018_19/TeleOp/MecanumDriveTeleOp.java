package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.MecanumDriveRobot;

@TeleOp(name="MecanumDriveTeleOp", group="TeleOpMode")
//@Disabled

public class MecanumDriveTeleOp extends OpMode
{
    //Creates robot hardware.
    private MecanumDriveRobot robot = new MecanumDriveRobot();

    //Robot initiates hardware and components.
    public void init()
    {
        robot.init(hardwareMap);
        robot.playR2D2Sound(this.hardwareMap.appContext);
        telemetry.addData("Status", "Robot has initiated!");
        telemetry.update();
    }

    //Robot starts.
    public void start()
    {
        robot.playBB8Sound(this.hardwareMap.appContext);
        telemetry.addData("Status", "Robot has started!");
        telemetry.update();
    }

    //Called repeatedly after robot starts.
    public void loop()
    {
        if (gamepad1.left_bumper) //If left bumper pressed
        {
            //Power low
            robot.drivePower = 0.5;
        }
        else if (gamepad1.right_bumper) //If right bumper pressed
        {
            //Power high
            robot.drivePower = 1;
        }

        if (gamepad1.left_stick_y <= -0.5) //If left stick forward
        {
            //Move forward
            robot.frontLeftDrive.setPower(robot.drivePower);
            robot.frontRightDrive.setPower(robot.drivePower);
            robot.backLeftDrive.setPower(robot.drivePower);
            robot.backRightDrive.setPower(robot.drivePower);
        }
        else if (gamepad1.left_stick_y >= 0.5) //If left stick backward
        {
            //Move backward
            robot.frontLeftDrive.setPower(-robot.drivePower);
            robot.frontRightDrive.setPower(-robot.drivePower);
            robot.backLeftDrive.setPower(-robot.drivePower);
            robot.backRightDrive.setPower(-robot.drivePower);
        }

        if (gamepad1.left_stick_x <= -0.5) //If left stick left
        {
            //Strafe left
            robot.frontLeftDrive.setPower(robot.drivePower);
            robot.frontRightDrive.setPower(-robot.drivePower);
            robot.backLeftDrive.setPower(-robot.drivePower);
            robot.backRightDrive.setPower(robot.drivePower);
        }
        else if (gamepad1.left_stick_x >= 0.5) //If left stick right
        {
            //Strafe right
            robot.frontLeftDrive.setPower(-robot.drivePower);
            robot.frontRightDrive.setPower(robot.drivePower);
            robot.backLeftDrive.setPower(robot.drivePower);
            robot.backRightDrive.setPower(-robot.drivePower);
        }

        if(gamepad1.right_stick_x <= -0.5) //If right stick left
        {
            //Rotate left
            robot.frontLeftDrive.setPower(-robot.drivePower);
            robot.frontRightDrive.setPower(robot.drivePower);
            robot.backLeftDrive.setPower(-robot.drivePower);
            robot.backRightDrive.setPower(robot.drivePower);
        }
        else if (gamepad1.right_stick_x >= 0.5) //If right stick right
        {
            //Rotate right
            robot.frontLeftDrive.setPower(robot.drivePower);
            robot.frontRightDrive.setPower(-robot.drivePower);
            robot.backLeftDrive.setPower(robot.drivePower);
            robot.backRightDrive.setPower(-robot.drivePower);
        }

        if (this.gamepad1.x) {
            robot.playMusic(this.hardwareMap.appContext);
        }
        else if (gamepad1.y) {
            robot.stopMusic();
        }
    }

    //Robot ends.
    public void stop()
    {
        robot.safetyStop();
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }
}