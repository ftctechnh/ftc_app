package org.firstinspires.ftc.teamcode.TalonCode.DaquanOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Disabled
@TeleOp(name = "Daquan Robot-Centric Tafon", group = "Daquan")
public class Daquan_Talon_TeleOp_robotCentric extends OpMode
{
    Daquan_Hardware robot;

    @Override   //Initializes the robot class and sets up its hardware map
    public void init()
    {
        robot = new Daquan_Hardware(hardwareMap, telemetry, false);
    }

    @Override
    public void loop () {

        //Ultra-Turbo Mode when both bumpers are held
        if(gamepad1.right_bumper && gamepad1.left_bumper)
            robot.currentDrivePower = 1f;
        else
            robot.currentDrivePower = robot.DRIVE_POWER;

        //Arcade drive with right joystick, turn with triggers(clockwise-right, counterclockwise-left)
        //Format: +/- Turning +/- Forward/Backward +/- Strafing
        robot.drive(
                robot.currentDrivePower * (gamepad1.right_trigger - gamepad1.left_trigger) + robot.currentDrivePower * (- gamepad1.right_stick_y) + robot.currentDrivePower * (gamepad1.right_stick_x),
                robot.currentDrivePower * (- gamepad1.right_trigger + gamepad1.left_trigger) + robot.currentDrivePower * (- gamepad1.right_stick_y) + robot.currentDrivePower * (- gamepad1.right_stick_x),
                robot.currentDrivePower * (gamepad1.right_trigger - gamepad1.left_trigger) + robot.currentDrivePower * (- gamepad1.right_stick_y) + robot.currentDrivePower * (- gamepad1.right_stick_x),
                robot.currentDrivePower * (- gamepad1.right_trigger + gamepad1.left_trigger) + robot.currentDrivePower * (- gamepad1.right_stick_y) + robot.currentDrivePower * (gamepad1.right_stick_x)
        );
        
        //Adding telemetry to debug if needed
        telemetry.addData("Drive Speed:", robot.currentDrivePower);
        telemetry.addData("Right Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Right Joystick Y Axis:", gamepad1.right_stick_y);
        telemetry.addData("Left Trigger:", gamepad1.left_trigger);
        telemetry.addData("Right Trigger:", gamepad1.right_trigger);
        telemetry.addData("Dpad Up:", gamepad2.left_trigger);
        telemetry.addData("Dpad Down:", gamepad2.right_trigger);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
    }
}
