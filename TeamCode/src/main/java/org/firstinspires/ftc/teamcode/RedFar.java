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
        JewelArm.findJewel(Color.RED);
        pictograph = vuforia.getMark();
        drive.forward(drive.DRIVE_OFF_BALANCE_BOARD_SPEED,  drive.DRIVE_TO_CYRPTOBOX_DISTANCE_FAR);
        if (pictograph == RelicRecoveryVuMark.LEFT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            drive.strafeLeft(drive.STRAFING_PAST_CRYPTOBOX_SPEED,drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + drive.CRYPTOBOX_COLUMNS_OFFSET_FAR + 3);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            drive.strafeLeft(drive.STRAFING_PAST_CRYPTOBOX_SPEED, drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + 4);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT) {
            drive.strafeLeft(drive.STRAFING_PAST_CRYPTOBOX_SPEED,drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION - drive.CRYPTOBOX_COLUMNS_OFFSET_FAR + 3);
        }
        ForkLift.moveMotor(-1,250);
        Systems.pushInBlock();
        drive.backward(drive.BACK_AWAY_FROM_BLOCK_SPEED,4);
        drive.leftGyro(0,0,-drive.SPIN_TO_CENTER_SPEED, 120);
        ForkLift.openClaw();
        ForkLift.moveUntilDown();
        sleep(1000);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}