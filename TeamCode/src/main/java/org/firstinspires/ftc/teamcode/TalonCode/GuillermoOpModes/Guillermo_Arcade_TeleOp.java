package org.firstinspires.ftc.teamcode.TalonCode.GuillermoOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Guillermo Arcade Tele-Op", group = "guillermo")
public class Guillermo_Arcade_TeleOp extends OpMode {

    Guillermo_Hardware robot;

    @Override
    public void init (){
        //Sets up the robot class for this program
        robot = new Guillermo_Hardware(hardwareMap, telemetry);
    }

    @Override
    public void loop (){
        //Hold right bumper to speed up and left to slow down (on first gamepad)
        if(gamepad1.right_trigger > 0)
            robot.currentDrivePower = robot.DRIVE_POWER + (1 - robot.DRIVE_POWER) * gamepad1.right_trigger;
        else if(gamepad1.left_trigger > 0)
            robot.currentDrivePower = robot.DRIVE_POWER - (robot.DRIVE_POWER - .1f) * gamepad1.left_trigger;
        else
            robot.currentDrivePower = robot.DRIVE_POWER;

        //Moves the servos when triggers or the right bumper are pressed on second gamepad
        if(gamepad2.right_trigger > 0)
            robot.grabGlyph();
        else if(gamepad2.left_trigger > 0)
            robot.releaseGlyph();
        else if(gamepad2.left_bumper)
            robot.slightlyReleaseGlyph();

        //Raises or lowers the lift when dpad on second gamepad is pressed
        if(gamepad2.dpad_up)
            robot.lift.setPower(1);
        else if(gamepad2.dpad_down)
            robot.lift.setPower(-1);
        else
            robot.lift.setPower(0);

        //Drives robot based on joysticks in an arcade drive fashion
        robot.drive((- gamepad1.left_stick_y + gamepad1.left_stick_x) * robot.currentDrivePower, (- gamepad1.left_stick_y - gamepad1.left_stick_x) * robot.currentDrivePower);

        //Telemetry
        telemetry.addData("Joystick X Axis:", gamepad1.left_stick_x);
        telemetry.addData("Joystick Y Axis:", gamepad1.left_stick_y);
        telemetry.addData("Drive Power:", robot.currentDrivePower);
        telemetry.addData("Left Servo Position:", robot.lub.getPosition());
        telemetry.addData("Right Servo Position:", robot.rub.getPosition());
        telemetry.addData("Elapsed Time:", robot.time);
    }
}
