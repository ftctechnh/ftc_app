package org.firstinspires.ftc.teamcode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name = "Red Far", group = "Autonomous")
public class RedFar extends LinearOpMode {
    private AutoDrive drive;
    private JewelArm JewelArm;
    private ForkLift ForkLift;
    private BeehiveVuforia vuforia;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Systems Systems;

    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET");
        telemetry.update();
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init(); //Calibrates gyro
        JewelArm = new JewelArm(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        vuforia = new BeehiveVuforia(hardwareMap, telemetry);
        Systems = new Systems(drive, ForkLift, JewelArm, vuforia, telemetry);
        telemetry.addLine("NOW YOU CAN PRESS PLAY");
        telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ForkLift.autoInit();
        JewelArm.up();
        ForkLift.closeClaw();
        sleep(200);
        ForkLift.moveMotor(1, 300);
        Systems.findJewel(Color.RED);
        pictograph = Systems.getMark();
        sleep(500);
        drive.driveTranslateRotate(0, drive.DRIVE_OFF_BALANCE_BOARD_SPEED,  0, drive.DRIVE_TO_CYRPTOBOX_DISTANCE_FAR);
        if (pictograph == RelicRecoveryVuMark.LEFT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.driveTranslateRotate(-drive.STRAFING_PAST_CRYPTOBOX_SPEED, 0, 0, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + drive.CYRPTOBOX_COLUMNS_OFFSET);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            drive.driveTranslateRotate(-drive.STRAFING_PAST_CRYPTOBOX_SPEED, 0, 0, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT) {
            drive.driveTranslateRotate(-drive.STRAFING_PAST_CRYPTOBOX_SPEED, 0, 0, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION - drive.CYRPTOBOX_COLUMNS_OFFSET);
        }
        drive.driveTranslateRotate(0, drive.DRIVE_INTO_CRYPTOBOX_SPEED, 0, 3);
        Systems.pushInBlock();
        drive.driveTranslateRotate(0,drive.BACK_AWAY_FROM_BLOCK_SPEED, 0, 4);
        //drive.leftGyro(0,0,-drive.SPIN_TO_CENTER_SPEED, 120);
        ForkLift.openClaw();
        ForkLift.moveUntilDown(0.75);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}