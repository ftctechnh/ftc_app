package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Peter on 12/21/2017.
 */
@Autonomous(name = "Presentation Program", group = "Test")
@Disabled
public class Presentation extends LinearOpMode {
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;

    public void runOpMode() {
        waitForStart();
        double speed = 0.5;

        drive = new DriveMecanum(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), 1.0, telemetry);
        ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
        ForkLift.init();

        //Translations:
        drive.driveTranslateRotate(speed, 0, 0);
        sleep(100);
        drive.driveTranslateRotate(-speed, 0, 0);
        sleep(100);
        drive.driveTranslateRotate(0, 0, 0);
        sleep(1000);

        //Forklift:
        ForkLift.openClaw();
        sleep(1000);
        ForkLift.closeClaw();
        sleep(1000);
        ForkLift.moveMotor(speed);
        sleep(500);
        ForkLift.moveMotor(-speed);
        sleep(500);
        ForkLift.moveMotor(0);
        sleep(1000);
    }
}
