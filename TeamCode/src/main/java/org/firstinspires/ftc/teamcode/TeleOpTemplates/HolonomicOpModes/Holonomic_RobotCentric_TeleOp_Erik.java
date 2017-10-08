package org.firstinspires.ftc.teamcode.TeleOpTemplates.HolonomicOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.TeleOpTemplates.HolonomicOpModes.Holonomic_Hardware;

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
@TeleOp(name = "Mecanum Erik Style Controls", group = "Daquan")
public class Holonomic_RobotCentric_TeleOp_Erik extends OpMode
{
    Holonomic_Hardware robot;

    @Override
    public void init()
    {
        robot = new Holonomic_Hardware(hardwareMap, telemetry, false);
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
                -gamepad1.left_stick_y+gamepad1.left_stick_x-gamepad1.right_stick_x,
                -gamepad1.left_stick_y-gamepad1.left_stick_x+gamepad1.right_stick_x,
                -gamepad1.left_stick_y-gamepad1.left_stick_x-gamepad1.right_stick_x,
                -gamepad1.left_stick_y+gamepad1.left_stick_x+gamepad1.right_stick_x
        );


        telemetry.addData(" Left Joystick X Axis:", gamepad1.left_stick_x);
        telemetry.addData(" Left Joystick Y Axis:", gamepad1.left_stick_y);
        telemetry.addData(" Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Left Trigger:", gamepad1.left_trigger);
        telemetry.addData("Right Trigger:", gamepad1.right_trigger);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
    }
}
