package org.firstinspires.ftc.teamcode.TalonCode.TeleOpTemplates.HolonomicOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
- Name: Holonomic Robot-Centric Tele-Op
- Creator[s]: Talon
- Date Created: 5/27/17
- Objective: To drive our holonomic robot around for our Physics Class demonstration
- Controls: The right joystick controls translational movement, while the triggers control rotation.
          Also, a corner of the dpad can be held down to pivot around that wheel, and holding both 
          bumpers sends the robot into ultra-turbo mode, which enhances its speed.
- Sensor Usage: None
- Key Algorithms: Lines 67-72 contain the concise algorithm for finding motor powers, which combines
                  all of the gamepad input to calculate the various powers in just 4 short lines.
- Uniqueness: This program was designed to be simple and reliable for demonstrations. The main unique
              part was adding in a pivoting capability that, while not all too useful, was cool to show off.
- Possible Improvements: Through some testing we have found this program to be pretty foolproof just
                         like we wanted it, so no adjustments need to be made.
 */

@Disabled
@TeleOp(name = "Holonomic Robot-Centric Tele-Op Talon", group = "holonomic")
public class Holonomic_RobotCentric_TeleOp extends OpMode {

    Holonomic_Hardware robot;

    //Array for pivoting(fl = 0, fr = 1, bl = 2, br = 3)
    float[] pivoting = new float[4];

    @Override
    public void init (){
        robot = new Holonomic_Hardware(hardwareMap, telemetry, false);
    }

    @Override
    public void loop (){
        //Reseting pivoting
        pivoting[0] = 1;
        pivoting[1] = 1;
        pivoting[2] = 1;
        pivoting[3] = 1;

        //Use tophat corners for pivoting
        if(gamepad1.dpad_up && gamepad1.dpad_left)
            pivoting[0] = 0;
        else if(gamepad1.dpad_up && gamepad1.dpad_right)
            pivoting[1] = 0;
        else if(gamepad1.dpad_down && gamepad1.dpad_left)
            pivoting[2] = 0;
        else if(gamepad1.dpad_down && gamepad1.dpad_right)
            pivoting[3] = 0;

        //Hold both bumpers for ultra-turbo mode
        if(gamepad1.right_bumper && gamepad1.left_bumper)
            robot.dp = 1f;
        else
            robot.dp = 0.2f;

        //Arcade drive with right joystick, turn with triggers(clockwise-right, counterclockwise-left)
        robot.drive(
                pivoting[0] * (gamepad1.right_trigger + gamepad1.left_trigger * -1) + -gamepad1.right_stick_y + gamepad1.right_stick_x,
                pivoting[1] * (gamepad1.right_trigger * -1 + gamepad1.left_trigger) + -gamepad1.right_stick_y + gamepad1.right_stick_x * -1,
                pivoting[2] * (gamepad1.right_trigger + gamepad1.left_trigger * -1) + -gamepad1.right_stick_y + gamepad1.right_stick_x * -1,
                pivoting[3] * (gamepad1.right_trigger * -1 + gamepad1.left_trigger) + -gamepad1.right_stick_y + gamepad1.right_stick_x
            );

        //Telemetry
        telemetry.addData("Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Joystick Y Axis:", gamepad1.right_stick_y);
        telemetry.addData("Left Trigger:", gamepad1.left_trigger);
        telemetry.addData("Right Trigger:", gamepad1.right_trigger);
        telemetry.addData("Pivoting:", gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_right || gamepad1.dpad_left);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
    }
}