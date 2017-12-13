package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Kaden on 10/20/2017.
 */

@TeleOp(name = "MecanumTranslateRotate", group = "linear OpMode")
public class MecanumTranslateRotate extends OpMode {
    private DriveMecanum drive;
    @Override
    public void init() {
        drive = new DriveMecanum(
            hardwareMap.dcMotor.get("m1"), //FrontLeft
            hardwareMap.dcMotor.get("m2"), //FrontRight
            hardwareMap.dcMotor.get("m3"), //RearLeft
            hardwareMap.dcMotor.get("m4"), //RearRight
            1.0, telemetry); //Top speed as a decimal

    }
    @Override
    public void loop() {
        drive.driveTranslateRotate(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if (gamepad1.right_bumper) {
            drive.swingRight();
        }
        else if (gamepad1.left_bumper) {
            drive.swingLeft();
        }
        

    }
}
