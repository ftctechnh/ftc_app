package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.StartingPosition;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;
import org.firstinspires.ftc.teamcode.Vision.VuforiaCVUtil;
import org.opencv.core.Rect;

@Config
public abstract class AutoUtils extends VuforiaCVUtil {
    public StartingPosition startingPosition;
    public SparkyTheRobot robot;

    public static double MARKER_DEPLOYER_DEPLOY = 0;
    public static double MARKER_DEPLOYER_RETRACTED = 0.85;
    public static double HANG_HOLD_POWER = -0.15;

    double TURN_MAX_SPEED = 0.6;
    double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45;

    public void setWinchHoldPosition() {
        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(1);
        robot.winch.setTargetPosition(0);
    }

    public enum DetachMethod {
        STRAFE,
        TURN
    }

    public void unhookFromLander(SampleMecanumDriveREV drive, SparkyTheRobot robot, DetachMethod method) {

        // Lower robot in two phases
        // In the first phase, we will lower the robot "manually"
        // In the second phase, we will just freefall to be faster

        robot.updateReadings();
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(1);
        while (opModeIsActive()) {
            robot.updateReadings();
            if (robot.primaryIMU.getGravity().zAccel >= 9.6) {
                break;
            }
        }
        robot.winch.setMotorEnable();
        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.winch.setPower(0);

        robot.updateReadings();
        if (method == DetachMethod.STRAFE) {
            followPath(drive, Paths.UNHOOK);
        } else if (method == DetachMethod.TURN) {
            // Flip around, making sure we turn the correct direction
            turnToPos(robot.normAngle(Math.PI + robot.getGyroHeading()), -1);
        }

        // Now, lower the hang arm

        if (method == DetachMethod.STRAFE) {
            // Sleep a little bit to let things move
            // a little
            robot.intake.collect();

            robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.winch.setPower(1);
            robot.winch.setTargetPosition(-1500);

            sleep(500);
            followPath(drive, Paths.UNDO_UNHOOK);
        }
    }

    public void refoldMechanisms() {
        robot.winch.setMotorDisable();
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
            if (forcedDir != 0 && Math.abs(difference) >= Math.PI / 2) {
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

    public GoldPosition waitAndWatchMinerals() {
        // This sometimes might count the same frame twice
        // but we're OK with that - we'll just run this for
        // a set time, not a set frame count
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(HANG_HOLD_POWER);
        GoldPosition result = GoldPosition.CENTER;

        // Positions:

        // Left: 517
        // Center: 297
        // Right: 80

        while (!isStarted() && !isStopRequested()) {
            int middleLine = getMiddlePosition(detector.getFoundRect());

            if (middleLine < 200) {
                result = GoldPosition.RIGHT;
            } else if (middleLine < 400) {
                result = GoldPosition.CENTER;
            } else {
                result = GoldPosition.LEFT;
            }

            telemetry.addData("Vision line", middleLine);
            telemetry.addData("Vision", result.toString());
            telemetry.update();
        }
        return result;
    }
}