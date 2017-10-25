package org.firstinspires.ftc.teamcode.DaquanOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
- Name: Daquan Robot-Centric Tele-Op with Erik's Controls
- Creator[s]: Erik
- Date Created: 10/7/17
- Objective: To drive our mecanum drive robot and us the intake
- Controls: The left joystick controls translational movement, while the right joystick control
            rotation. Also, holding both bumpers sends the robot into ultra-turbo mode, which
            enhances its speed. Using the Triggers, you can move the hurricane intakes, with left
            being intake and right being outtake.
- Sensor Usage: None
- Key Algorithms: TBD
- Uniqueness: This program was designed to drive, and test our intake.  While not too unique, it
              does have interesting code regarding the mecanum wheels.
- Possible Improvements: Creating a version of the program that successfully incorporates field
centric Drive.
 */
@Disabled
@TeleOp(name = "Daquan Robot Centric Erik", group = "Daquan")
public class Daquan_Erik_TeleOp_robotCentric extends OpMode
{
    Daquan_Hardware robot;

    @Override
    public void init()
    {
        robot = new Daquan_Hardware(hardwareMap, telemetry, false);
    }

    @Override
    public void loop ()
    {
        if(gamepad1.right_bumper && gamepad1.left_bumper)
        {
            robot.currentDrivePower = 1f;
        }
        else
        {
            robot.currentDrivePower = .2f;
        }

        robot.drive(
                -gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x,
                -gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x,
                -gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x,
                -gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x
        );

        if(gamepad1.right_trigger != 1)
        {
            robot.rurricane.setPower(.16*gamepad1.right_trigger);
            robot.lurricane.setPower(1*gamepad1.right_trigger);
        }
        else if(gamepad1.left_trigger != 1)
        {
            robot.rurricane.setPower(-.16*gamepad1.left_trigger);
            robot.lurricane.setPower(-1*gamepad1.left_trigger);
        }
        else
        {
            robot.rurricane.setPower(0);
            robot.lurricane.setPower(0);
        }

        telemetry.addData(" Left Joystick X Axis:", gamepad1.left_stick_x);
        telemetry.addData(" Left Joystick Y Axis:", gamepad1.left_stick_y);
        telemetry.addData(" Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Left Trigger:", gamepad1.left_trigger);
        telemetry.addData("Right Trigger:", gamepad1.right_trigger);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
    }
}
