package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/12/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "DriveEverythingFar", group = "linear OpMode")
public class DriveEverythingFar extends OpMode {
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;
    private JewelArm jewelArm;
    private Systems Systems;

    @Override
    public void init() {
        jewelArm = new JewelArm(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        RelicClaw = new RelicClaw(hardwareMap, telemetry);
        drive = new DriveMecanum(hardwareMap, telemetry);
        Systems = new Systems(drive, ForkLift, RelicClaw);
    }

    @Override
    public void loop() {
        jewelArm.up();
        //drive
        if (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.right_stick_x) + Math.abs(gamepad1.right_stick_y) > Math.abs(gamepad2.left_stick_x) + Math.abs(gamepad2.left_stick_y) + Math.abs(gamepad2.right_stick_x) + Math.abs(gamepad2.right_stick_y)) {
            if (gamepad1.right_bumper || gamepad1.left_bumper) {
                drive.driveLeftRight(gamepad1.left_stick_x * drive.BUMPER_SLOW_SPEED, gamepad1.right_stick_x * drive.BUMPER_SLOW_SPEED, gamepad1.left_stick_y * drive.BUMPER_SLOW_SPEED, gamepad1.right_stick_y * drive.BUMPER_SLOW_SPEED);
            } else {
                drive.driveLeftRight(gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_y);
            }
        } else if (Math.abs(gamepad2.left_stick_x) + Math.abs(gamepad2.left_stick_y) + Math.abs(gamepad2.right_stick_x) + Math.abs(gamepad2.right_stick_y) > Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.right_stick_x) + Math.abs(gamepad1.right_stick_y)) {
            if (gamepad2.right_bumper || gamepad2.left_bumper) {
              drive.driveLeftRight(gamepad2.left_stick_x * drive.BUMPER_SLOW_SPEED, gamepad2.right_stick_x * drive.BUMPER_SLOW_SPEED, gamepad2.left_stick_y * drive.BUMPER_SLOW_SPEED, gamepad2.right_stick_y * drive.BUMPER_SLOW_SPEED);
            } else {
              drive.driveLeftRight(gamepad2.left_stick_x, gamepad2.right_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_y);

            }
        } else {
            if (gamepad1.dpad_up) {
                drive.forward(drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad1.dpad_left) {
                drive.strafeLeft(-drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad1.dpad_down) {
                drive.backward(drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad1.dpad_right) {
                drive.strafeRight(drive.D_PAD_SLOW_SPEED);
            }
            else if(gamepad2.dpad_left) {
                drive.strafeLeft(drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad2.dpad_right) {
                drive.strafeRight(drive.D_PAD_SLOW_SPEED);
            }
            else {
                drive.stop();
            }
        }
        //ForkLift
        if (gamepad1.a) {
            ForkLift.closeClaw();
        }

        if (gamepad1.b) {
            ForkLift.openClaw();
        }
        ForkLift.moveMotor(gamepad1.right_trigger - gamepad1.left_trigger);

        //RelicClaw
        if (gamepad2.a) {
            RelicClaw.closeClaw();
        }
        if (gamepad2.b) {
            RelicClaw.openClaw();
        }
        if (gamepad2.dpad_up) {
            RelicClaw.up();
        }
        if (gamepad2.dpad_down) {
            RelicClaw.down();
        }
        if (gamepad2.x) {
            RelicClaw.pickup();
        }
        if (gamepad2.y) {
            RelicClaw.driving();
        }
        RelicClaw.moveMotor(gamepad2.left_trigger - gamepad2.right_trigger);
    }
}
