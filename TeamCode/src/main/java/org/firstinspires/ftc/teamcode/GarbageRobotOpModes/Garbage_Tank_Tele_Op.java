package org.firstinspires.ftc.teamcode.GarbageRobotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Garbage Tank Tele-Op", group = "garbage")
public class Garbage_Tank_Tele_Op extends OpMode {

    Garbage_Hardware_Map robot;

    @Override
    public void init (){

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
        robot.drive(- gamepad1.left_stick_y * robot.dp, - gamepad1.right_stick_y * robot.dp);

        if(gamepad1.a)
        {
            robot.fleckerino.setPower(robot.shootPower);
        }
        else
        {
            robot.fleckerino.setPower(0);
        }
        //Telemetry
        telemetry.addData("Left Joystick:", gamepad1.left_stick_y);
        telemetry.addData("Right Joystick:", gamepad1.right_stick_y);
        telemetry.addData("Drive Power:", robot.dp);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
    }
}
