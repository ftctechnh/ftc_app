package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */
@Autonomous(name = "RAutonomousRC", group = "Autonomous")
public class RedRecoveryClasses extends LinearOpMode {
    AutoDrive drive;
    JewelArm jewelArm;
    ForkLift ForkLift;
    RelicClaw RelicClaw;
    BeehiveVuforia vuforia = new BeehiveVuforia();
    RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    String color;

    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init(); //Calibrates gyro
        jewelArm = new JewelArm(hardwareMap.servo.get("s4"), hardwareMap.colorSensor.get("cs1"), telemetry);
        ForkLift = new ForkLift(hardwareMap.servo.get("s5"), hardwareMap.servo.get("s6"), hardwareMap.dcMotor.get("m6"), hardwareMap.digitalChannel.get("b0"), hardwareMap.digitalChannel.get("b1"), telemetry);
        RelicClaw = new RelicClaw(hardwareMap.servo.get("s1"), hardwareMap.servo.get("s2"), hardwareMap.dcMotor.get("m5"), telemetry);
        telemetry.addLine("ready to start");
        telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ForkLift.autoInit();
        jewelArm.up();
        ForkLift.closeClaw();
        sleep(200);
        ForkLift.moveUpDown(1, 300);
        pictograph = vuforia.getMark(hardwareMap, telemetry);
        color = jewelArm.findJewel();
        if (color.equals("Red")) { //if the arm sees red
            drive.driveTranslateRotate(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED, drive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            jewelArm.up();
            drive.driveTranslateRotate(0, 0, -drive.SPIN_ON_BALANCE_BOARD_SPEED, drive.SPIN_ON_BALANCE_BOARD_DISTANCE);
        } else if (color.equals("Blue")) { //if the arm sees blue
            drive.driveTranslateRotate(0, 0, -drive.SPIN_ON_BALANCE_BOARD_SPEED, drive.SPIN_ON_BALANCE_BOARD_DISTANCE);
            jewelArm.up();
            drive.driveTranslateRotate(0, 0, drive.SPIN_ON_BALANCE_BOARD_SPEED, drive.SPIN_ON_BALANCE_BOARD_DISTANCE);
        } else {
        }
        sleep(500);
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            drive.driveTranslateRotate(0, drive.DRIVE_OFF_BALANCE_BAORD_SPEED, 0, 40);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.driveTranslateRotate(0, drive.DRIVE_OFF_BALANCE_BAORD_SPEED, 0, 32);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT) {
            drive.driveTranslateRotate(0, drive.DRIVE_OFF_BALANCE_BAORD_SPEED, 0, 24);

        }
        drive.rightGyro(0, 0, drive.TURN_TO_CRYPTOBOX_SPEED, -90);
        drive.driveTranslateRotate(0, drive.DRIVE_INTO_CRYPTOBOX_SPEED, 0, 5);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}