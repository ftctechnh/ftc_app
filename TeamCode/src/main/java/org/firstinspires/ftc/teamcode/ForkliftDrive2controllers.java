package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ForkliftDrivewith2remotes", group = "linear OpMode")
public class ForkliftDrive2controllers extends OpMode {
    private ForkLift ForkLift;
    private DriveMecanum drive;

    @Override
    public void init() {
        drive = new DriveMecanum(
                hardwareMap.dcMotor.get("m1"), //FrontLeft
                hardwareMap.dcMotor.get("m2"), //FrontRight
                hardwareMap.dcMotor.get("m3"), //RearLeft
                hardwareMap.dcMotor.get("m4"), //RearRight
                1.0, telemetry);
        ForkLift = new ForkLift(
                hardwareMap.servo.get("s1"), //rightClaw
                hardwareMap.servo.get("s2"), //leftClaw
                hardwareMap.dcMotor.get("m5"), //updown
                hardwareMap.touchSensor.get("b0"), //top button
                hardwareMap.touchSensor.get("b1"),
                telemetry); //bottom button
        ForkLift.init();
    }

    @Override
    public void loop() {
        drive.driveLeftRight(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if (gamepad2.a) {
            ForkLift.closeClaw();

        }
        if (gamepad2.b) {
            ForkLift.openClaw();
        }
        ForkLift.moveUpDown(gamepad2.right_trigger - gamepad2.left_trigger);
    }
}
