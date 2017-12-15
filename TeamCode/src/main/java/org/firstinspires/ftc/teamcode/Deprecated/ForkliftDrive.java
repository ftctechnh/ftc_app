package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ForkliftDrive", group = "linear OpMode")
@Disabled
public class ForkliftDrive extends OpMode {
    private ForkLift ForkLift;
    private DriveMecanum drive;
    @Override
    public void init() {
        drive = new DriveMecanum(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), 1.0, telemetry);
        // Right and Left claws are from back to front point of view
        ForkLift = new ForkLift(hardwareMap.servo.get("s1"), hardwareMap.servo.get("s2"), hardwareMap.dcMotor.get("m5"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
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
