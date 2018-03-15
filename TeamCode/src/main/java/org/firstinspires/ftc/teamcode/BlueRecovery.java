package org.firstinspires.ftc.teamcode;

/**
 * Created by BeehiveRobotics-3648 on 11/28/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name = "Blue Recovery", group = "Autonomous")
public class BlueRecovery extends LinearOpMode {
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Robot robot;
    private static final double MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY = 36.5;

    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET"); telemetry.update();
        robot = new Robot(this);
        robot.mapRobot();
        robot.calibrateGyro();
        telemetry.addLine("NOW YOU CAN PRESS PLAY"); telemetry.update();
        waitForStart();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        robot.forkLift.autoInit();
        robot.jewelArm.findJewel(Color.BLUE);
        pictograph = robot.phone.getMark();
        if (pictograph == RelicRecoveryVuMark.LEFT) {
            robot.drive.backward(robot.drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY - robot.drive.CRYPTOBOX_COLUMNS_OFFSET_RECOVERY);
        }
        else if (pictograph == RelicRecoveryVuMark.CENTER) {
            robot.drive.backward(robot.drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY);
        }
        else if (pictograph == RelicRecoveryVuMark.RIGHT || pictograph == RelicRecoveryVuMark.UNKNOWN) {
            robot.drive.backward(robot.drive.DRIVE_OFF_BALANCE_BOARD_SPEED, MOVE_TOWARDS_CRYPTOBOX_DISTANCE_BLUE_RECOVERY + robot.drive.CRYPTOBOX_COLUMNS_OFFSET_RECOVERY);
        }
        robot.forkLift.moveMotor(-1,250);
        robot.rightGyro(robot.drive.SPIN_TO_CRYPTOBOX_SPEED, -90);
        robot.drive.forward(robot.drive.DRIVE_INTO_CRYPTOBOX_SPEED, 5);
        robot.pushInBlock();
        robot.drive.backward(robot.drive.BACK_AWAY_FROM_BLOCK_SPEED, 4);
        robot.leftGyro(robot.drive.SPIN_TO_CENTER_SPEED, 90);
        robot.forkLift.openClaw();
        robot.forkLift.moveUntilDown();
        sleep(1000);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}