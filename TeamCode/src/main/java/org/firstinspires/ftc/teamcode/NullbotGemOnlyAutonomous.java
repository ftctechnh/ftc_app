package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.io.IOException;

import static org.firstinspires.ftc.teamcode.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Alliance.RED;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Knock off gem", group="Autonomous")
@Disabled
public class NullbotGemOnlyAutonomous extends LinearOpMode {

    public NullbotHardware robot   = new NullbotHardware();

    final int DISTANCE_TO_DRIVE = 400;
    int directionMultiplier;

    PixyCam pixyCam;
    PixyCam.Block redBall;
    PixyCam.Block blueBall;

    Alliance rightMostBall; // Which alliance the rightmost ball belongs to, from robot POV

    @Override
    public void runOpMode() {

        if (!robot.initialized) {
            robot.init(hardwareMap, this, gamepad1, gamepad2);
        }

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        pixyCam = robot.leftPixyCam;

        telemetry.clearAll();

        telemetry.log().add("Gem only autonomous mode");
        telemetry.log().add("Be prepared for robot to MOVE");
        telemetry.log().add("Robot's current alliance is " + robot.color);
        telemetry.log().add("--------------------------");

        robot.openLogFile();
        while (!isStarted()) {

            updateBlocks();
            telemetry.addData("Red ball:", redBall.toString());
            telemetry.addData("Blue ball:", blueBall.toString());
            telemetry.update();
            robot.writePixyCamTick(blueBall, redBall);
        }
        try {
            robot.flushLogs();
            robot.closeLog();
        } catch (IOException e) {telemetry.log().add("Writing logs failed");}

        telemetry.log().add("Robot started");

        updateBlocks();

        // Higher x-values are on the right

        if (redBall.averageX() > blueBall.averageX()) {
            rightMostBall = RED;
        } else {
            rightMostBall = BLUE;
        }

        telemetry.addData("Rightmost ball:", rightMostBall);

        robot.lowerLeftWhipSnake();

        robot.waitForTick(500);


        int desiredDistance = DISTANCE_TO_DRIVE;

        if (rightMostBall == robot.color) {
            desiredDistance *= -1;
        }
        desiredDistance *= directionMultiplier;


        robot.closeBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.5);
        robot.lift.setTargetPosition(-500);
        robot.sleep(2000);

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.2);
        }

        robot.frontLeft.setTargetPosition(desiredDistance);
        robot.backLeft.setTargetPosition(desiredDistance);
        robot.frontRight.setTargetPosition(-desiredDistance);
        robot.backRight.setTargetPosition(-desiredDistance);

        robot.sleep(1000);
        robot.raiseWhipSnake();
        robot.sleep(500);

        for (DcMotor m : robot.motorArr) {
            m.setTargetPosition(0);
        }
        robot.sleep(1000);

        robot.lift.setTargetPosition(0);
        robot.sleep(2000);
    }

    public void updateBlocks() {
        redBall = pixyCam.GetBiggestBlock(1);
        blueBall = pixyCam.GetBiggestBlock(2);
    }
}
