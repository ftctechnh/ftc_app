package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by 292486 on 12/1/2016.
 */

@TeleOp(name = "controllor test")
public class XBoxControllerTest extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("Sticks: ", "L%f %f | R%f %f", gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_y, gamepad1.right_stick_x);
        telemetry.addData("Buttons: ", "A%b B%b X%b Y%b", gamepad1.a, gamepad1.b, gamepad1.x, gamepad1.y);
        telemetry.addData("Triggers: ", "L%f | R%f", gamepad1.left_trigger, gamepad1.right_trigger);
        telemetry.addData("Bumpers: ", "L%b | R%b", gamepad1.left_bumper, gamepad1.right_bumper);
        telemetry.update();
    }
}
