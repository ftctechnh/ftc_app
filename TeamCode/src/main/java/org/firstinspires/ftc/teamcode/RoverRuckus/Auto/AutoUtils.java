package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.StartingPosition;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;
import org.firstinspires.ftc.teamcode.Vision.VuforiaCVUtil;
import org.opencv.core.Rect;

//class contains navigation code - controlling robot, moving from position to position, unhooking hook
//note: again, depends on sparky specs -- needs hang motor.
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AutoUtils extends VuforiaCVUtil {
    public StartingPosition startingPosition;
    public SparkyTheRobot robot;
    public HoldingPIDMotor hangMotor;

    public static double MARKER_DEPLOYER_DEPLOY = 0;
    public static double MARKER_DEPLOYER_RETRACTED = 0.85;

    double TURN_MAX_SPEED = 0.6;
    double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45;

    public void setWinchHoldPosition() {
        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(0.5);
        robot.winch.setTargetPosition(0);
    }

    public enum DetachMethod {
        STRAFE,
        TURN
    }

    public void unhookFromLander(SampleMecanumDriveREV drive, SparkyTheRobot robot, DetachMethod method) {

        // Lower robot
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(-1);
        while (opModeIsActive()) {
            robot.updateReadings();
            if (robot.imu.getGravity().zAccel >= 9.7) {
                break;
            }
        }
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(0);

        if (method == DetachMethod.STRAFE) {
            followPath(drive, Paths.UNHOOK);
        } else if (method == DetachMethod.TURN) {
            // Make sure we turn correct direction
            turnToPos(Math.PI, -1);
        }

        // Now, lower the hang arm
        robot.linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.linearSlide.setPower(0.4);
        robot.linearSlide.setTargetPosition(500);
        robot.intake.setPos(0.8);

        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(1);
        robot.winch.setTargetPosition(1500);

        // Sleep a little bit to let things move
        // a little
        sleep(500);

        if (method == DetachMethod.STRAFE) {
            followPath(drive, Paths.UNDO_UNHOOK);
        }
    }

    public void refoldMechanisms() {
        robot.winch.setMotorDisable();
        robot.linearSlide.setTargetPosition(0);
        robot.intake.goToMin();
    }

    public static int getMiddlePosition(Rect boundingBox) {
        return boundingBox.x + (boundingBox.width / 2);
    }

    public void followPath(SampleMecanumDriveREV drive, Trajectory trajectory) {
        drive.followTrajectory(trajectory);
        while (!isStopRequested() && drive.isFollowingTrajectory()) {
            drive.update();
        }
    }

    public void turnToPos(double pos) {
        turnToPos(pos, 0);
    }

    public void turnToPos(double pos, int forcedDir) {
        double difference = Double.MAX_VALUE;
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && opModeIsActive()) {
            robot.updateReadings();

            difference = robot.getSignedAngleDifference(robot.normAngle(pos), robot.getGyroHeading());

            double turnSpeed = Math.max(-TURN_MAX_SPEED, Math.min(TURN_MAX_SPEED, difference));
            turnSpeed = Math.copySign(Math.max(0.05, Math.abs(turnSpeed)), turnSpeed);

            // Optionally force turn direction
            // But allow for minor changes in direction the other way
            if (forcedDir != 0 && difference >= Math.PI / 2) {
                turnSpeed = Math.copySign(turnSpeed, forcedDir);
            }

            double[] unscaledMotorPowers = new double[4];

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] = -turnSpeed;
                } else {
                    unscaledMotorPowers[i] = turnSpeed;
                }
            }
            telemetry.update();

            robot.setMotorSpeeds(unscaledMotorPowers);
        }
        stopMoving();
    }

    public void stopMoving() {
        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }
    }

    /* This is really complicated and probably doesn't provide a
    whole lot of benefit. You should really think about whether
    this is worth the additional complexity
     */

    public static int MIN_ANALYZE_TIME_MS = 1000;
    public GoldPosition waitAndWatchMinerals() {
        // This sometimes might count the same frame twice
        // but we're OK with that - we'll just run this for
        // a set time, not a set frame count
        Queue<GoldPosition> visionResults = new PriorityQueue<>();
        ElapsedTime timer = new ElapsedTime();

        while ((!isStarted() || timer.milliseconds() < MIN_ANALYZE_TIME_MS) && !isStopRequested()) {
            if (getMiddlePosition(detector.getFoundRect()) < 50) {
                visionResults.add(GoldPosition.RIGHT);
            } else if (getMiddlePosition(detector.getFoundRect()) < 400) {
                visionResults.add(GoldPosition.CENTER);
            } else {
                visionResults.add(GoldPosition.LEFT);
            }

            if (timer.milliseconds() > MIN_ANALYZE_TIME_MS) {
                // We need to start overwriting old data
                visionResults.remove();
            }
        }

        // Now, find the mode of our data
        Map<GoldPosition, Integer> histogram = new HashMap<>();
        histogram.put(GoldPosition.LEFT, 0);
        histogram.put(GoldPosition.CENTER, 0);
        histogram.put(GoldPosition.RIGHT, 0);

        while (!visionResults.isEmpty()) {
            GoldPosition result = visionResults.remove();
            histogram.put(result, histogram.get(result) + 1);
        }

        // Default to center (cause it's fastest)
        if (histogram.get(GoldPosition.LEFT) > histogram.get(GoldPosition.CENTER) &&
                histogram.get(GoldPosition.LEFT) > histogram.get(GoldPosition.RIGHT)) {
            return GoldPosition.LEFT;
        } else if (histogram.get(GoldPosition.RIGHT) > histogram.get(GoldPosition.CENTER)) {
            return GoldPosition.RIGHT;
        } else {
            return GoldPosition.CENTER;
        }
    }
}