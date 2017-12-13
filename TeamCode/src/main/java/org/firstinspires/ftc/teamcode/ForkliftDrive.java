package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ForkliftDrive", group = "linear OpMode")
public class ForkliftDrive extends OpMode {
    private ForkLift ForkLift;
    private DriveMecanum drive;
    @Override
    public void init() {
        drive = new DriveMecanum(
                hardwareMap.dcMotor.get("m1"), //FrontLeft
                hardwareMap.dcMotor.get("m2"), //FrontRight
                hardwareMap.dcMotor.get("m3"), //RearLeft
                hardwareMap.dcMotor.get("m4"), //RearRight
                1.0, //top speed as a decimal
                telemetry); //for debugging
        // Right and Left claws are from back to front point of view
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
        if (gamepad1.a) {
            ForkLift.closeClaw();

        }
        if (gamepad1.b) {
            ForkLift.openClaw();
        }
        ForkLift.moveUpDown(gamepad1.right_trigger - gamepad1.left_trigger);
    }
}
