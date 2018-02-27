package org.firstinspires.ftc.teamcode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name = "Blue Recovery", group = "Autonomous")
public class BlueRecovery extends LinearOpMode {
    private AutoDrive drive;
    private JewelArm JewelArm;
    private ForkLift ForkLift;
    private BeehiveVuforia vuforia;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Systems Systems;
    private static final double MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY = 36.5;

    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET");
        telemetry.update();
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init(); //Calibrates gyro
        JewelArm = new JewelArm(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        vuforia = new BeehiveVuforia(hardwareMap, telemetry);
        Systems = new Systems(drive, ForkLift, JewelArm, vuforia, hardwareMap, telemetry);
        telemetry.addLine("NOW YOU CAN PRESS PLAY");
        telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ForkLift.autoInit();
        JewelArm.findJewel(Color.BLUE);
        pictograph = vuforia.getMark();
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            drive.backward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY - drive.CRYPTOBOX_COLUMNS_OFFSET_RECOVERY);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            drive.backward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.backward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY + drive.CRYPTOBOX_COLUMNS_OFFSET_RECOVERY);
        }
        ForkLift.moveMotor(-1,250);
        drive.rightGyro(drive.SPIN_TO_CRYPTOBOX_SPEED, -90);
        drive.forward(drive.DRIVE_INTO_CRYPTOBOX_SPEED, 5);
        Systems.pushInBlock();
        drive.backward(drive.BACK_AWAY_FROM_BLOCK_SPEED, 4);
        drive.leftGyro(drive.SPIN_TO_CENTER_SPEED, 90);
        ForkLift.openClaw();
        ForkLift.moveUntilDown();
        sleep(1000);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}