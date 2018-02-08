package org.firstinspires.ftc.teamcode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name = "Red Recovery", group = "Autonomous")
public class RedRecovery extends LinearOpMode {
    private AutoDrive drive;
    private JewelArm JewelArm;
    private ForkLift ForkLift;
    private BeehiveVuforia vuforia;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Systems Systems;
    private static final double MOVE_TOWARDS_CRYPTOBOX_DISTANCE_RED_RECOVERY = 34;

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
        JewelArm.findJewel(Color.RED);
        pictograph = vuforia.getMark();
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            drive.forward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_RED_RECOVERY + drive.CRYPTOBOX_COLUMNS_OFFSET_RECOVERY);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            drive.forward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_RED_RECOVERY);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.forward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_RED_RECOVERY - drive.CRYPTOBOX_COLUMNS_OFFSET_RECOVERY);
        }
        drive.rightGyro(drive.SPIN_TO_CRYPTOBOX_SPEED, -90);
        ForkLift.moveMotor(-1,250);
        drive.forward(drive.DRIVE_INTO_CRYPTOBOX_SPEED, 5);
        Systems.pushInBlock();
        drive.backward(drive.BACK_AWAY_FROM_BLOCK_SPEED, 6);
        drive.leftGyro(0,0,-drive.SPIN_TO_CENTER_SPEED, 90);
        ForkLift.openClaw();
        ForkLift.moveUntilDown();
        sleep(1000);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}