package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by andrew on 11/17/17.
 */

public class EncoderTurn extends LinearOpMode {

    HardwareRobot robot = new HardwareRobot();

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        waitForStart();

        telemetry.addData( "current pos: ", robot.leftDriveFront.getCurrentPosition());

        int currentPos = robot.leftDriveFront.getCurrentPosition();
        int targetPos = currentPos + (int)(robot.COUNTS_PER_INCH * 3);

        robot.leftDriveFront.setTargetPosition(targetPos);
        robot.leftDriveBack.setTargetPosition(targetPos);
        robot.rightDriveFront.setTargetPosition(targetPos);
        robot.rightDriveBack.setTargetPosition(targetPos);

        telemetry.addData( "target pos: ", robot.leftDriveFront.getTargetPosition());

        robot.setAllLeftDrivePower(1);
        robot.setAllRightDrivePower(1);

        while (Math.abs(robot.leftDriveFront.getCurrentPosition() - targetPos) > 1) {

        }

        telemetry.addData( "current pos: ", robot.leftDriveFront.getCurrentPosition());

        targetPos = currentPos + (int)(robot.COUNTS_PER_INCH * 14.13);

        robot.leftDriveFront.setTargetPosition(-targetPos);
        robot.leftDriveBack.setTargetPosition(-targetPos);
        robot.rightDriveFront.setTargetPosition(targetPos);
        robot.rightDriveBack.setTargetPosition(targetPos);

        telemetry.addData( "target pos: ", robot.leftDriveFront.getTargetPosition());

        robot.setAllLeftDrivePower(-1);
        robot.setAllRightDrivePower(1);

        while ((Math.abs(robot.leftDriveFront.getCurrentPosition() - targetPos) > 1) &&
                (Math.abs(robot.rightDriveFront.getCurrentPosition() - targetPos) > 1)) {

        }

        telemetry.addData( "current pos: ", robot.leftDriveFront.getCurrentPosition());
        telemetry.update();
    }
}
