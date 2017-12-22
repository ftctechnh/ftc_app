package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.concurrent.TimeUnit;

/**
 * Created by Peter on 12/21/2017.
 */

public class Presentation extends OpMode {

    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;

    public void init() {
        double speed = 0.5;

        drive = new DriveMecanum(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), 1.0, telemetry);
        ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
        ForkLift.init();



        //Translations:
        drive.driveTranslateRotate(0, speed, 0);
        sleep(100);
        drive.driveTranslateRotate(0, -speed, 0);
        sleep(100);
        drive.driveTranslateRotate(0, 0, 0);
        sleep(1000);

        //Forklift:
        ForkLift.openClaw();
        sleep(1000);
        ForkLift.closeClaw();
        sleep(1000);

        ForkLift.moveUpDown(speed);
        sleep(500);
        ForkLift.moveUpDown(-speed);
        sleep(500);
        ForkLift.moveUpDown(0);
        sleep(1000);
    }
    public void loop() {

    }
    private void sleep(long ms) {
        try {Thread.sleep(ms);} catch (InterruptedException e) {}
    }
}
