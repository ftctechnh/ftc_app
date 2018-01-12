package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 10/19/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "DriveEverythingTranslateRotate", group = "linear OpMode")
public class DriveEverythingTranslateRotate extends OpMode {
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;

    @Override
    public void init() {
        ForkLift = new ForkLift(hardwareMap, telemetry);
        ForkLift.init();
        RelicClaw = new RelicClaw(hardwareMap, telemetry);
        RelicClaw.init();
        drive = new DriveMecanum(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        //Drive
        if (gamepad1.right_bumper || gamepad1.left_bumper) {
            drive.driveTranslateRotate(gamepad1.left_stick_x/4, gamepad1.left_stick_y/4, gamepad1.right_stick_x/4);

        } else {
            drive.driveTranslateRotate(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        }
        //ForkLift
        if (gamepad1.a) {
            ForkLift.closeClaw();
        }
        if (gamepad1.b) {
            ForkLift.openClaw();
        }
        ForkLift.moveMotor(gamepad1.right_trigger - gamepad1.left_trigger);
        //Relic arm
        if (gamepad2.a) {
            RelicClaw.closeClaw();
        }
        if (gamepad2.b) {
            RelicClaw.openClaw();
        }
        if (gamepad2.x) {
            RelicClaw.down();
        }
        if (gamepad2.y) {
            RelicClaw.up();
        }
        RelicClaw.moveMotor(-gamepad2.left_stick_y);
    }
}