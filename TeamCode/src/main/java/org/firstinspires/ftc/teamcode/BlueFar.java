package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/30/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name = "Blue Far", group = "Autonomous")
public class BlueFar extends LinearOpMode {
    private AutoDrive drive;
    private JewelArm JewelArm;
    private ForkLift ForkLift;
    private Phone phone;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Systems Systems;
    private final double DISTANCE_OFFSET = 3;

    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET");
        telemetry.update();
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init(); //Calibrates gyro
        JewelArm = new JewelArm(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        phone = new Phone(hardwareMap, telemetry);
        Systems = new Systems(drive, ForkLift, JewelArm, phone, hardwareMap, telemetry);
        telemetry.addLine("NOW YOU CAN PRESS PLAY");
        telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ForkLift.autoInit();
        JewelArm.findJewel(Color.BLUE);
        pictograph = phone.getMark();
        drive.backward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, drive.DRIVE_TO_CYRPTOBOX_DISTANCE_FAR + 2);
        boolean isDistanceSane = 12 < drive.getDistance() && drive.getDistance() < 19;
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            if (isDistanceSane) {
                drive.driveLeftUntilDistance(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DISTANCE_TO_CLOSE_COLUMN + DISTANCE_OFFSET);
            } else {
                drive.strafeLeft(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION - drive.CRYPTOBOX_COLUMNS_OFFSET_FAR + 2);
            }
        } else if (pictograph == RelicRecoveryVuMark.CENTER) {
            if (isDistanceSane) {
                drive.driveLeftUntilDistance(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DISTANCE_TO_CENTER_COLUMN + DISTANCE_OFFSET);
            } else {
                drive.strafeLeft(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION);
            }
        } else if (pictograph == RelicRecoveryVuMark.RIGHT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            if (isDistanceSane) {
                drive.driveLeftUntilDistance(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DISTANCE_TO_FAR_COLUMN + DISTANCE_OFFSET);
            } else {
                drive.strafeLeft(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + drive.CRYPTOBOX_COLUMNS_OFFSET_FAR - 3);
            }
        }
        drive.rightGyro(drive.SPIN_TO_CRYPTOBOX_SPEED, -180);
        ForkLift.moveMotor(-1, 250);
        drive.forward(drive.DRIVE_INTO_CRYPTOBOX_SPEED, 3);
        Systems.pushInBlock();
        drive.backward(drive.BACK_AWAY_FROM_BLOCK_SPEED, 6);
        //drive.leftGyro(-drive.SPIN_TO_CENTER_SPEED, 30);
        ForkLift.openClaw();
        ForkLift.moveUntilDown();
        sleep(1000);
    }
}