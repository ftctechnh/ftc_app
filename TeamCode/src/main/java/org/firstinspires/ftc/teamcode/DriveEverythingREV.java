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
        RelicClaw = new RelicClaw(hardwareMap.servo.get("s1"), hardwareMap.servo.get("s2"), hardwareMap.dcMotor.get("m5"), telemetry);
        RelicClaw.init();
        drive = new DriveMecanum(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), 1.0, telemetry);
        jewelArm = new JewelArm(hardwareMap.servo.get("s4"), hardwareMap.colorSensor.get("cs1"), telemetry);
        jewelArm.up();
    }
    @Override
    public void loop() {
        if (gamepad1.right_bumper || gamepad1.left_bumper) {
            drive.driveLeftRight(gamepad1.left_stick_x / 4, gamepad1.right_stick_x / 4, gamepad1.left_stick_y / 4, gamepad1.right_stick_y / 4);
        } else {
            drive.driveLeftRight(gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_y);
        }
        if (gamepad1.x) {
            ForkLift.closeClaw();
            ForkLift.moveMotor(1, 750);
            drive.driveTranslateRotate(0, -0.75,0, 750);
            ForkLift.openClaw();
            ForkLift.moveMotor(-1, 500);
            ForkLift.closeClaw();
            sleep(250);
            ForkLift.moveMotor(1, 250);
        }
        if (gamepad1.dpad_up) {drive.driveTranslateRotate(0, -0.2, 0);}

        if (gamepad1.dpad_left) {drive.driveTranslateRotate(-0.2, 0, 0);}

        if (gamepad1.dpad_down) {drive.driveTranslateRotate(0, 0.2, 0);}

        if (gamepad1.dpad_right) {drive.driveTranslateRotate(0.2, 0, 0);}

        if (gamepad1.a) {ForkLift.closeClaw();}

        if (gamepad1.b) {ForkLift.openClaw();}

        if (gamepad2.a) {RelicClaw.closeClaw();}

        if (gamepad2.b) {RelicClaw.openClaw();}

        if (gamepad2.x) {RelicClaw.down();}

        if (gamepad2.y) {RelicClaw.up();}

        RelicClaw.moveMotor(gamepad2.right_trigger - gamepad2.left_trigger);
    }
    private void sleep(long time) {try {Thread.sleep(time);} catch (InterruptedException e) {}}
}