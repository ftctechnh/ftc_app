package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 10/20/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MecanumTranslateRotate", group = "linear OpMode")
@Disabled
public class MecanumTranslateRotate extends OpMode {
    private DriveMecanum drive;
    @Override
    public void init() {
        drive = new DriveMecanum(hardwareMap, telemetry);
    }
    @Override
    public void loop() {
        drive.driveTranslateRotate(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        if (gamepad1.right_bumper) {
            drive.swingRight();
        }
        else if (gamepad1.left_bumper) {
            drive.swingLeft();
        }
        

    }
}
