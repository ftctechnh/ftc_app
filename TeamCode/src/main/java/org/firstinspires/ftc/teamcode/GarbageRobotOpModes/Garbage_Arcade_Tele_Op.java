package org.firstinspires.ftc.teamcode.GarbageRobotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Garbage Arcade Tele-Op", group = "garbage")
public class Garbage_Arcade_Tele_Op extends OpMode {

    Garbage_Hardware_Map robot;

    @Override
    public void init (){
        //Sets up the robot with this program
        robot = new Garbage_Hardware_Map(hardwareMap, telemetry);
    }

    @Override
    public void loop (){
        //Hold both bumpers for ultra-turbo mode
        if(gamepad1.right_bumper && gamepad1.left_bumper)
            robot.dp = 1f;
        else
            robot.dp = 0.3f;

        //Drives robot based on joysicks in a tank drive fashion
        robot.drive((- gamepad1.right_stick_y + gamepad1.right_stick_x) * robot.dp, (- gamepad1.right_stick_y - gamepad1.right_stick_x) * robot.dp);

        //Telemetry
        telemetry.addData("Joystick X Axis:", gamepad1.right_stick_x);
        telemetry.addData("Joystick Y Axis:", gamepad1.right_stick_y);
        telemetry.addData("Drive Power:", robot.dp);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.update();
    }
}
