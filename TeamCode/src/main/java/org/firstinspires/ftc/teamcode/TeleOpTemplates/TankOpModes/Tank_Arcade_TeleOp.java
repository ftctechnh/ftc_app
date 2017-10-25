package org.firstinspires.ftc.teamcode.TeleOpTemplates.TankOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Arcade Tele-Op Talon", group = "Tank")
public class Tank_Arcade_TeleOp extends OpMode {

    Tank_Hardware robot;

    @Override
    public void init (){
        //Sets up the robot class for this program
        robot = new Tank_Hardware(hardwareMap, telemetry);
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

        //Drives robot based on joysticks in an arcade drive fashion
        robot.drive((- gamepad1.left_stick_y + gamepad1.left_stick_x) * robot.currentDrivePower, (- gamepad1.left_stick_y - gamepad1.left_stick_x) * robot.currentDrivePower);

        //Telemetry
        telemetry.addData("Joystick X Axis:", gamepad1.left_stick_x);
        telemetry.addData("Joystick Y Axis:", gamepad1.left_stick_y);
        telemetry.addData("Drive Power:", robot.currentDrivePower);
        telemetry.addData("Elapsed Time:", robot.time);
    }
}
