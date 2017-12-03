package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Joseph Liang on 12/3/2017.
 */

@TeleOp (name = "30minTeleOp", group = "Pushbot")
//@Disabled
public class ThirtyMinTeleOp extends OpMode{

    GlyphArm gilgearmesh = new GlyphArm();
    RelicDrive robot       = new RelicDrive();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double right;
        double left;
        double armPower;

        left = gamepad1.left_stick_y;
        right = gamepad1.right_stick_y;

        if (gamepad1.left_trigger > 0) {
            left = gamepad1.left_stick_y;
            right = gamepad1.right_stick_y;
        } else if (gamepad1.left_bumper = true) {
            left = gamepad1.left_stick_y/2.5;
            right = gamepad1.right_stick_y/2.5;
        } else if (gamepad1.right_bumper = true) {
            left = gamepad1.left_stick_y/4;
            right = gamepad1.right_stick_y/4;
        } else if (gamepad1.left_trigger > 0) {
            left = gamepad1.left_stick_y/6;
            right = gamepad1.right_stick_y/6;
        } else {
            left = gamepad1.left_stick_y/2;
            right = gamepad1.right_stick_y/2;}
        robot.controlDrive(left, right);

        armPower = gamepad2.left_stick_y;

        gilgearmesh.armPower(-armPower);

        if (gamepad2.right_bumper) {
            gilgearmesh.clawPos(1);
        } else if (gamepad2.left_bumper) {
            gilgearmesh.clawPos(0);
        }

        telemetry.addData("Arm Pos",gilgearmesh.getArmPosition());
    }
}
