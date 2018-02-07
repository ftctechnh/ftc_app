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
        JewelArm.findJewel(Color.BLUE);
        pictograph = vuforia.getMark();
        drive.backward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, drive.DRIVE_TO_CYRPTOBOX_DISTANCE_FAR + 2);
        drive.rightGyro(drive.SPIN_TO_CRYPTOBOX_SPEED, -180);
        ForkLift.moveMotor(-1,250);
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            drive.strafeRight(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION - drive.CRYPTOBOX_COLUMNS_OFFSET_FAR + 2);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            drive.strafeRight(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.strafeRight(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + drive.CRYPTOBOX_COLUMNS_OFFSET_FAR - 3);
        }
        Systems.pushInBlock();
        drive.backward(drive.BACK_AWAY_FROM_BLOCK_SPEED, 4);
        drive.leftGyro(0,0,-drive.SPIN_TO_CENTER_SPEED, 30);
        ForkLift.openClaw();
        ForkLift.moveUntilDown();
    }
}