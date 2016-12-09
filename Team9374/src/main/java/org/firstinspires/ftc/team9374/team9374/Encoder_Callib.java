package org.firstinspires.ftc.team9374.team9374;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team9374.team9374.Hardware9374;

/**
 * Created by darwin on 12/4/16.
 */
@TeleOp(name = "encoder calib")
public class Encoder_Callib extends OpMode {

    Hardware9374 robot = new Hardware9374();

    public void init() {
        robot.init(hardwareMap);

    }
    public void loop(){
        double lStickY = gamepad1.left_stick_y;
        double rSticky = gamepad1.right_stick_y;

        robot.left_b.setPower(lStickY);
        robot.left_f.setPower(lStickY);

        robot.right_f.setPower(rSticky);
        robot.right_b.setPower(rSticky);

        if (gamepad1.a) {
            robot.resetEncoders();
        }

        telemetry.addData("Left Back position",robot.left_b.getCurrentPosition());
        telemetry.addData("Left Front position",robot.left_f.getCurrentPosition());
        telemetry.addData("Right Back position",robot.right_b.getCurrentPosition());
        telemetry.addData("Right Front position",robot.right_f.getCurrentPosition());

        telemetry.addData("Color sensor value (R)", robot.CSensor.red());
        telemetry.addData("Color sensor value (B)", robot.CSensor.blue());
    }

}
