package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Knock off gem", group="Autonomous")
@Disabled
public class NullbotGemOnlyAutonomous extends LinearOpMode {

    public NullbotHardware robot   = new NullbotHardware();

    final int DISTANCE_TO_DRIVE = 400;
    final int PIXYCAM_DATA_POINTS = 50;
    final int MS_TO_GATHER_PIXYCAM_DATA = 3000;
    final double CERTAINTY_THRESHOLD = 10;

    int directionMultiplier;

    PixyCam pixyCam;
    PixyCam.Block redBall;
    PixyCam.Block blueBall;

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

        while (!isStarted()) {

            updateBlocks();
            telemetry.addData("Red ball:", redBall.toString());
            telemetry.addData("Blue ball:", blueBall.toString());
            telemetry.update();
        }

        telemetry.log().add("Robot started");
        telemetry.log().add("Reading PixyCam values");

        Alliance rightMostBall = getBallPositions();

        telemetry.addData("Rightmost ball:", rightMostBall);

        if (rightMostBall == Alliance.UNKNOWN) {
            telemetry.log().add("Couldn't determine which ball was where!");
            return;
        }

        telemetry.update();

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

    /**
     * Takes 100 frames and computes which ball is on which side
     *
     * @return    How certain the PixyCam is about its result
     */
    public Alliance getBallPositions() {

        // The variance can only be calculated once we have all data, so we have to
        // store it before it can be processed
        int[][] positions =
                new int[][]{new int[PIXYCAM_DATA_POINTS], new int[PIXYCAM_DATA_POINTS]};

        int index = 0;
        ElapsedTime giveUpTimer = new ElapsedTime(); // We should't idle if we don't see balls

        // Data collection is seperate from computation to make making changes to algorithm easier
        while (opModeIsActive() && index < PIXYCAM_DATA_POINTS
                && giveUpTimer.milliseconds() < MS_TO_GATHER_PIXYCAM_DATA) {

            updateBlocks();

            positions[0][index] = blueBall.averageX();
            positions[1][index] = redBall.averageX();
        }

        int[] numPoints = new int[2];
        double[] means = new double[2];
        double[] variances = new double[2];

        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < PIXYCAM_DATA_POINTS; k++) {
                if (positions[i][k] != 0) {
                    numPoints[i] += 1;
                    means[i] += positions[i][k];

                    double error = positions[i][k] - means[i];
                    variances[i] += Math.pow(error, 2);
                }
            }

            if (numPoints[i] == 0) { // Stop division by zero from occurring
                return Alliance.UNKNOWN; // If we can't see one ball, then we should have zero certainty
            }

            means[i] = means[i] / ((double) numPoints[i]);
            variances[i] = (variances[i] / ((double) numPoints[i])) - Math.pow(means[i], 2);
        }

        // We have now computed the averages and variances of both data sets

        // We don't need to check if the variances are zero, because doubles can be infinity or
        // negative infinity

        double certainty = (means[0] - means[1]) / Math.sqrt(variances[0] + variances[1]);
        telemetry.addData("Certainty:", certainty);
        telemetry.update();
        // Make sense of our number

        if (Math.abs(certainty) > CERTAINTY_THRESHOLD) {
            if (certainty > 0) {
                return Alliance.BLUE;
            } else {
                return Alliance.RED;
            }
        } else {
            return Alliance.UNKNOWN;
        }
    }
}
