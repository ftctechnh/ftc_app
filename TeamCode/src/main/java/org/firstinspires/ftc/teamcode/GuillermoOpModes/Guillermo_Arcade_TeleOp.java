package org.firstinspires.ftc.teamcode.GuillermoOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
        //Hold left bumper for ultra-turbo mode
        if(gamepad1.left_bumper)
            robot.drivePower = 1f;
        else
            robot.drivePower = 0.3f;

        //Moves the servos when the right trigger or bumpers are pressed
        if(gamepad1.right_trigger > 0)
            robot.grabGlyph();
        else if(gamepad1.left_trigger > 0)
            robot.releaseGlyph();
        else if(gamepad1.right_bumper)
            robot.slightlyReleaseGlyph();

        //Drives robot based on joysticks in an arcade drive fashion
        robot.drive((- gamepad1.left_stick_y + gamepad1.left_stick_x) * robot.drivePower, (- gamepad1.left_stick_y - gamepad1.left_stick_x) * robot.drivePower);

        //Telemetry
        telemetry.addData("Joystick X Axis:", gamepad1.left_stick_x);
        telemetry.addData("Joystick Y Axis:", gamepad1.left_stick_y);
        telemetry.addData("Drive Power:", robot.drivePower);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData("Left Servo Position:", robot.lub.getPosition());
        telemetry.addData("Right Servo Position:", robot.rub.getPosition());
        telemetry.addData("Elapsed Time:", robot.time);
    }
}
