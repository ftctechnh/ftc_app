package org.firstinspires.ftc.teamcode.ErikCode.TeleOpTemplates.TankOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Tank Tank Tele-Op", group = "Tank")
public class Tank_Tank_TeleOp extends OpMode {

    Tank_Hardware robot;

    @Override
    public void init (){
        //sets up our robot class
        robot = new Tank_Hardware(hardwareMap, telemetry);
        ;
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

       if(gamepad1.a)
       {
           robot.time.reset();
       }

        //Drives robot based on first gamepad joysticks in a tank drive fashion
        robot.drive(- gamepad1.left_stick_y * robot.currentDrivePower, - gamepad1.right_stick_y * robot.currentDrivePower);

        //Telemetry
        telemetry.addData("Left Joystick:", gamepad1.left_stick_y);
        telemetry.addData("Right Joystick:", gamepad1.right_stick_y);
        telemetry.addData("Drive Power:", robot.currentDrivePower);

        telemetry.addData("Elapsed Time:", robot.time.seconds());
    }
}
