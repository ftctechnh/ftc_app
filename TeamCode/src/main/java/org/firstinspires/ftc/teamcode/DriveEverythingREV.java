package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaden on 112/12/2017.
 */
@TeleOp(name = "REVDriveEverything", group = "linear OpMode")
public class DriveEverythingREV extends OpMode {
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;
    private JewelArm jewelArm;

    @Override
    public void init() {
        ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
        RelicClaw = new RelicClaw(hardwareMap.servo.get("s1"), hardwareMap.servo.get("s2"), hardwareMap.dcMotor.get("m5"), hardwareMap.digitalChannel.get("b2"), hardwareMap.digitalChannel.get("b3"), telemetry);
        RelicClaw.init();
        drive = new DriveMecanum(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), 1.0, telemetry);
        jewelArm = new JewelArm(hardwareMap.servo.get("s4"), hardwareMap.colorSensor.get("cs1"), telemetry);
        jewelArm.up();
    }

    @Override
    public void loop() {
        //drive
        if (!gamepad1.atRest()) {
            if (gamepad1.right_bumper || gamepad1.left_bumper) {
                drive.driveLeftRight(gamepad1.left_stick_x / 4, gamepad1.right_stick_x / 4, gamepad1.left_stick_y / 4, gamepad1.right_stick_y / 4);
            } else {
                drive.driveLeftRight(gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_y);
            }
        } else if (!gamepad2.atRest()) {
            if (gamepad2.right_bumper || gamepad2.left_bumper) {
              drive.driveLeftRight(gamepad2.left_stick_x / 4, gamepad2.right_stick_x / 4, gamepad2.left_stick_y / 4, gamepad2.right_stick_y / 4);
            } else {
              drive.driveLeftRight(gamepad2.left_stick_x, gamepad2.right_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_y);

            }
        } else {
            if (gamepad1.dpad_up) {
                drive.driveTranslateRotate(0, -0.2, 0);
            }
            else if (gamepad1.dpad_left) {
                drive.driveTranslateRotate(-0.2, 0, 0);
            }
            else if (gamepad1.dpad_down) {
                drive.driveTranslateRotate(0, 0.2, 0);
            }
            else if (gamepad1.dpad_right) {
                drive.driveTranslateRotate(0.2, 0, 0);
            } else {
                drive.stop();
            }
        }
        if (gamepad1.x) {
            ForkLift.closeClaw();
            ForkLift.moveMotor(1, 750);
            drive.driveTranslateRotate(0, -0.75, 0, 750);
            ForkLift.openClaw();
            ForkLift.moveMotor(-1, 500);
            ForkLift.closeClaw();
            sleep(250);
            ForkLift.moveMotor(1, 250);
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
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }
}