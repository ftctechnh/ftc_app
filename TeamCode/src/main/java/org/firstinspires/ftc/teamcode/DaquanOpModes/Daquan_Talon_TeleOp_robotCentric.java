package org.firstinspires.ftc.teamcode.DaquanOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
- Name: Daquan Robot-Centric Tele-Op with Talon's Controls
- Creator[s]: Erik
- Date Created: 10/7/17
- Objective: To drive our mecanum drive robot and us the intake
- Controls: The right joystick controls translational movement, while the triggers control
            rotation. Also, holding both bumpers sends the robot into ultra-turbo mode, which
            enhances its speed. Using the triggers on the second gamepad, you can move the hurricane
            intakes, with left being intake and right being outtake.
- Sensor Usage: None
- Key Algorithms: TBD
- Uniqueness: This program was designed to drive, and test our intake.  While not too unique, it
              does have interesting code regarding the mecanum wheels.
- Possible Improvements: Creating a version of the program that successfully incorporates field
centric Drive.
 */
@TeleOp(name = "Mecanum Talon Style Controls", group = "Daquan")
public class Daquan_Talon_TeleOp_robotCentric extends OpMode
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
            robot.dp = 1f;
        }
        else
        {
            robot.dp = .2f;
        }

        robot.drive(
                -gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_trigger-gamepad1.left_trigger,
                -gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_trigger+gamepad1.left_trigger,
                -gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_trigger-gamepad1.left_trigger,
                -gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_trigger+gamepad1.left_trigger
        );

        if(gamepad2.right_trigger != 1)
        {
            robot.rurricane.setPower(.16*gamepad1.right_trigger);
            robot.lurricane.setPower(1*gamepad1.right_trigger);
        }
        else if(gamepad2.left_trigger != 1)
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
        telemetry.addData("Left Trigger Drive:", gamepad1.left_trigger);
        telemetry.addData("Right Trigger Drive:", gamepad1.right_trigger);
        telemetry.addData("Left Trigger Intake:", gamepad2.left_trigger);
        telemetry.addData("Right Trigger Intake:", gamepad2.right_trigger);

        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
    }
}
