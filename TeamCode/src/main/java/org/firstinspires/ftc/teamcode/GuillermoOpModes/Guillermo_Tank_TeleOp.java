package org.firstinspires.ftc.teamcode.GuillermoOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Guillermo Tank Tele-Op", group = "guillermo")
public class Guillermo_Tank_TeleOp extends OpMode {

    Guillermo_Hardware robot;

    @Override
    public void init (){
        //sets up our robot class
        robot = new Guillermo_Hardware(hardwareMap, telemetry);
    }

    @Override
    public void loop (){
        //Hold both bumpers for ultra-turbo mode
        if(gamepad1.right_bumper && gamepad1.left_bumper)
            robot.drivePower = 1f;
        else
            robot.drivePower = 0.3f;

        //Drives robot based on joysicks in a tank drive fashion
        robot.drive(- gamepad1.left_stick_y * robot.drivePower, - gamepad1.right_stick_y * robot.drivePower);

        //Telemetry
        telemetry.addData("Left Joystick:", gamepad1.left_stick_y);
        telemetry.addData("Right Joystick:", gamepad1.right_stick_y);
        telemetry.addData("Drive Power:", robot.drivePower);
        telemetry.addData("Ultra Turbo Mode Activated:", gamepad1.right_bumper && gamepad1.left_bumper);
        telemetry.addData("Time:", robot.time);
    }
}
