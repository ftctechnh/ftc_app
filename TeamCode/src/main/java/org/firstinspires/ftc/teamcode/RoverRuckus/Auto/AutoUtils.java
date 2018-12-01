package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.StartingPosition;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;
import org.firstinspires.ftc.teamcode.Vision.VuforiaCVUtil;
import org.opencv.core.Rect;

public abstract class AutoUtils extends VuforiaCVUtil {
    public StartingPosition startingPosition;
    public SparkyTheRobot robot;
    public HoldingPIDMotor hangMotor;

    public static double MARKER_DEPLOYER_DEPLOY = 0;
    public static double MARKER_DEPLOYER_RETRACTED = 0.85;
    public static double HANG_HOLD_POWER = -0.1;

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

        if (method == DetachMethod.STRAFE) {
            followPath(drive, Paths.UNHOOK);
        } else if (method == DetachMethod.TURN) {
            // Flip around, making sure we turn the correct direction
            turnToPos(robot.normAngle(Math.PI + robot.getGyroHeading()), -1);
        }

        // Now, lower the hang arm
        robot.intake.collect();

        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(1);
        robot.winch.setTargetPosition(-1500);

        if (method == DetachMethod.STRAFE) {
            // Sleep a little bit to let things move
            // a little
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

    abstract class FollowPathLambda {
        private boolean isTerminated;
        public boolean runAtTermination;
        public SparkyTheRobot robot;

        public FollowPathLambda(SparkyTheRobot robot) {
            this.robot = robot;
        }

        public abstract void run();
        public void terminate() {
            isTerminated = true;
        }
        public boolean isTerminated() {
            return isTerminated;
        }
    }

    class NullLambda extends FollowPathLambda {
        public NullLambda(SparkyTheRobot robot) {
            super(robot);
        }
        @Override
        public void run() {
            terminate();
        }
        @Override
        public void terminate() {
            super.terminate();
        }
    }

    public void followPath(SampleMecanumDriveREV drive, Trajectory trajectory) {
        followPath(drive, trajectory, new NullLambda(robot));
    }

    public void followPath(SampleMecanumDriveREV drive, Trajectory trajectory, FollowPathLambda lambda) {
        drive.followTrajectory(trajectory);
        while (!isStopRequested() && drive.isFollowingTrajectory()) {
            drive.update();
            if (!lambda.isTerminated) {
                lambda.run();
            }
        }
        if (!lambda.isTerminated && lambda.runAtTermination) {
            lambda.terminate();
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

    /* This is really complicated and probably doesn't provide a
    whole lot of benefit. You should really think about whether
    this is worth the additional complexity
     */

    public static int MIN_ANALYZE_TIME_MS = 1000;
    /*public GoldPosition waitAndWatchMinerals() {
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

            telemetry.addData("Vision", visionResults.peek().toString());
            telemetry.update();

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
    }*/

    public static double increment = 0.0005;
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