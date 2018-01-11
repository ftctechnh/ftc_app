package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 10/20/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "MecanumDriveExpo", group = "linear OpMode")
@Disabled
public class MecanumDriveExpo extends OpMode {
    private DriveMecanum drive;
    private double x;
    private double y;
    private double z;
    private double speed = 1;
    private double expo = 1.75;
    @Override
    public void init() {
        drive = new DriveMecanum(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), 1.0, telemetry);
    }
    @Override
    public void loop() {
        if (gamepad1.left_stick_y > 0) {
            y = Math.pow(gamepad1.left_stick_y, expo);
        }
        else if (gamepad1.left_stick_y < 0){
            y = -Math.pow(Math.abs(gamepad1.left_stick_y), expo);
        }
        else {
            y = 0;
        }

        if (gamepad1.left_stick_x > 0) {
            x = Math.pow(gamepad1.left_stick_x, expo);
        }
        else if (gamepad1.left_stick_x < 0){
            x = -Math.pow(Math.abs(gamepad1.left_stick_x), expo);
        }
        else {
            x = 0;
        }

        if (gamepad1.right_stick_x > 0) {
            z = Math.pow(gamepad1.right_stick_x, expo);
        }
        else if (gamepad1.right_stick_x < 0){
            z = -Math.pow(Math.abs(gamepad1.right_stick_x), expo);
        }
        else {
            z = 0;
        }
        drive.driveTranslateRotate(x, y, z);
    }
}
