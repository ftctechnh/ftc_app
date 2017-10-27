package org.firstinspires.ftc.teamcode.DaquanOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
@Disabled
@TeleOp(name = "Daquan Field-Centric Tafon", group = "Daquan")
public class Daquan_Field_Centric extends OpMode {

    Daquan_Hardware robot;
    public static final double ANGLE_FROM_DRIVER = Math.PI / 2;

    @Override
    public void init (){
        robot = new Daquan_Hardware(hardwareMap, telemetry, true);
    }

    @Override
    public void loop (){

        robot.updateGyro();

        double inputAngle = Math.atan2(- gamepad1.right_stick_y, gamepad1.right_stick_x);

        double inputPower = Math.sqrt(gamepad1.right_stick_x * gamepad1.right_stick_x + gamepad1.right_stick_y * gamepad1.right_stick_y);

        if(inputPower > 1)
            inputPower = 1;

        double moveAngle = inputAngle + (ANGLE_FROM_DRIVER - robot.heading);

            robot.drive(
                    (Math.sin(moveAngle) + Math.cos(moveAngle)) * inputPower / 2 + gamepad1.right_trigger - gamepad1.left_trigger,
                    (Math.sin(moveAngle) - Math.cos(moveAngle)) * inputPower / 2 - gamepad1.right_trigger + gamepad1.left_trigger,
                    (Math.sin(moveAngle) - Math.cos(moveAngle)) * inputPower / 2 + gamepad1.right_trigger - gamepad1.left_trigger,
                    (Math.sin(moveAngle) + Math.cos(moveAngle)) * inputPower / 2 - gamepad1.right_trigger + gamepad1.left_trigger
            );

        telemetry.addData("Left Trigger", gamepad1.left_trigger);
        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("Joystick Direction", Math.toDegrees(inputAngle));
        telemetry.addData("Joystick Magnitude", inputPower);
        telemetry.addData("Gyro Heading", robot.heading);
    }
}