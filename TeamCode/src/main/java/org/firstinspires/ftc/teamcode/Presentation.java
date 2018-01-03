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
        drive.driveTranslateRotate(speed, 0, 0, (long) speed*400);
        sleep((long) speed*2000);
        drive.driveTranslateRotate(-speed, 0, 0, (long) speed*400);
        sleep((long) speed*2000);

        //Forklift:
        ForkLift.openClaw();
        sleep((long) speed*2000);
        ForkLift.closeClaw();
        sleep((long) speed*2000);

        ForkLift.moveUntilUp(speed);
        sleep((long) speed*2000);
        ForkLift.moveUntilDown(speed);
    }
}
