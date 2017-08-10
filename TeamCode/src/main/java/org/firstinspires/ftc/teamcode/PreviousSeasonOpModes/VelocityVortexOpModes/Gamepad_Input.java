package org.firstinspires.ftc.teamcode.PreviousSeasonOpModes.VelocityVortexOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name = "Gamepad Test", group = "Sensor")
public class Gamepad_Input extends OpMode {

    @Override public void init() {}

    @Override public void loop() {

        telemetry.addData("A:",gamepad1.a);
        telemetry.addData("B:",gamepad1.b);
        telemetry.addData("X:",gamepad1.x);
        telemetry.addData("Y:",gamepad1.y);

        telemetry.addData("Start:",gamepad1.start);
        telemetry.addData("Back:",gamepad1.back);


        telemetry.addData("Left Trigger:",gamepad1.left_trigger);
        telemetry.addData("Left Bumper:",gamepad1.left_bumper);

        telemetry.addData("Right Trigger:",gamepad1.right_trigger);
        telemetry.addData("Right Bumper:",gamepad1.right_bumper);

        telemetry.addData("Left Joystick x-axis:",gamepad1.left_stick_x);
        telemetry.addData("Left Joystick y-axis:",gamepad1.left_stick_y);

        telemetry.addData("Right Joystick x-axis:",gamepad1.right_stick_x);
        telemetry.addData("Right Joystick y-axis:",gamepad1.right_stick_y);
        
        telemetry.addData("A:",gamepad2.a);
        telemetry.addData("B:",gamepad2.b);
        telemetry.addData("X:",gamepad2.x);
        telemetry.addData("Y:",gamepad2.y);

        telemetry.addData("Start:",gamepad2.start);
        telemetry.addData("Back:",gamepad2.back);


        telemetry.addData("Left Trigger:",gamepad2.left_trigger);
        telemetry.addData("Left Bumper:",gamepad2.left_bumper);

        telemetry.addData("Right Trigger:",gamepad2.right_trigger);
        telemetry.addData("Right Bumper:",gamepad2.right_bumper);

        telemetry.addData("Left Joystick x-axis:",gamepad2.left_stick_x);
        telemetry.addData("Left Joystick y-axis:",gamepad2.left_stick_y);

        telemetry.addData("Right Joystick x-axis:",gamepad2.right_stick_x);
        telemetry.addData("Right Joystick y-axis:",gamepad2.right_stick_y);
    }
}
