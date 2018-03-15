package org.firstinspires.ftc.teamcode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name = "Red Far", group = "Autonomous")
public class RedFar extends LinearOpMode {
    private Robot robot;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;

    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET"); telemetry.update();
        robot = new Robot(this);
        robot.mapRobot();
        robot.calibrateGyro();
        telemetry.addLine("NOW YOU CAN PRESS PLAY"); telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        robot.forkLift.autoInit();
        robot.jewelArm.findJewel(Color.RED);
        pictograph = robot.phone.getMark();
        robot.drive.forward(robot.drive.DRIVE_OFF_BALANCE_BOARD_SPEED,  robot.drive.DRIVE_TO_CYRPTOBOX_DISTANCE_FAR);
        robot.forkLift.moveMotor(-1,250);
        boolean isDistanceSane = 12 < robot.getDistance() && robot.getDistance() < 19;
        if (pictograph == RelicRecoveryVuMark.LEFT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            if(isDistanceSane){
                robot.driveLeftUntilDistance(robot.drive.STRAFING_PAST_CRYPTOBOX_SPEED, robot.drive.DISTANCE_TO_FAR_COLUMN);
            } else {
                robot.drive.strafeLeft(robot.drive.STRAFING_PAST_CRYPTOBOX_SPEED, robot.drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + robot.drive.CRYPTOBOX_COLUMNS_OFFSET_FAR + 3);
            }
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            if (isDistanceSane) {
                robot.driveLeftUntilDistance(robot.drive.STRAFING_PAST_CRYPTOBOX_SPEED, robot.drive.DISTANCE_TO_CENTER_COLUMN);
            } else {
                robot.drive.strafeLeft(robot.drive.STRAFING_PAST_CRYPTOBOX_SPEED, robot.drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION + 2.5);
            }
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT) {
            if (isDistanceSane) {
                robot.driveLeftUntilDistance(robot.drive.STRAFING_PAST_CRYPTOBOX_SPEED, robot.drive.DISTANCE_TO_CLOSE_COLUMN);
            } else {
                robot.drive.strafeLeft(robot.drive.STRAFING_PAST_CRYPTOBOX_SPEED, robot.drive.DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION - robot.drive.CRYPTOBOX_COLUMNS_OFFSET_FAR + 3);
            }
        }
        robot.drive.forward(robot.drive.DRIVE_INTO_CRYPTOBOX_SPEED, 6);
        robot.pushInBlock();
        robot.drive.backward(robot.drive.BACK_AWAY_FROM_BLOCK_SPEED,7);
        robot.leftGyro(robot.drive.SPIN_TO_CENTER_SPEED, 120);
        robot.forkLift.openClaw();
        robot.forkLift.moveUntilDown();
        sleep(1000);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}