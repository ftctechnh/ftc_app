package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Kaden on 12/30/2017.
 */
@Autonomous(name = "Blue Far", group = "Autonomous")
public class BlueFar extends LinearOpMode {
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
        Systems = new Systems(drive, ForkLift, JewelArm, vuforia);
        telemetry.addLine("NOW YOU CAN PRESS PLAY");
        telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ForkLift.autoInit();
        JewelArm.up();
        ForkLift.closeClaw();
        sleep(200);
        ForkLift.moveMotor(1, 400);
        Systems.findJewel(Color.BLUE);
        pictograph = Systems.getMark();
        sleep(500);
        drive.driveTranslateRotate(0,-drive.DRIVE_OFF_BALANCE_BOARD_SPEED, 0, drive.DRIVE_TO_CYRPTOBOX_DISTANCE_FAR + 2);
        drive.rightGyro(0,0, drive.SPIN_TO_CRYPTOBOX_SPEED, -178);
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            drive.driveTranslateRotate(drive.STRAFING_PAST_CRYPTOBOX_SPEED, 0, 0, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION - drive.CYRPTOBOX_COLUMNS_OFFSET);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            drive.driveTranslateRotate(drive.STRAFING_PAST_CRYPTOBOX_SPEED, 0, 0, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.driveTranslateRotate(drive.STRAFING_PAST_CRYPTOBOX_SPEED, 0, 0, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + drive.CYRPTOBOX_COLUMNS_OFFSET);
        }
        drive.driveTranslateRotate(0, drive.DRIVE_INTO_CRYPTOBOX_SPEED, 0, 3);
        Systems.pushInBlock();
        drive.driveTranslateRotate(0,drive.BACK_AWAY_FROM_BLOCK_SPEED, 0, 2);
        drive.leftGyro(0,0,-drive.SPIN_TO_CENTER_SPEED, 30);
        ForkLift.openClaw();
        ForkLift.moveUntilDown(0.75);
    }
}