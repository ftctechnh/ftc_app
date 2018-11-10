package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.Utilities.Audio.SoundEffectManager;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;
import org.firstinspires.ftc.teamcode.Vision.VuforiaCVUtil;
import org.opencv.core.Rect;

public abstract class AutoUtils extends VuforiaCVUtil {
    public ParkingLocation parkingLocation;
    public StartingPosition startingPosition;
    public SparkyTheRobot robot;
    public HoldingPIDMotor hangMotor;

    public static double MARKER_DEPLOYER_DEPLOY = 0;
    public static double MARKER_DEPLOYER_RETRACTED = 0.85;

    double TURN_MAX_SPEED = 0.6;
    double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45;

    public void unhookFromLander(SampleMecanumDriveREV drive, SparkyTheRobot robot) {
        robot.init(false);
        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(0.5);
        robot.winch.setTargetPosition(0);

        waitForStart();

        robot.soundEffects.playEffect("begin");

        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setPower(-1);
        while (opModeIsActive()) {
            robot.updateReadings();
            if (robot.imu.getGravity().zAccel >= 9.7) {
                break;
            }
        }
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(0);
        robot.soundEffects.playEffect("move");
        followPath(drive, Paths.FORWARD_A_LITTLE);
        followPath(drive, Paths.UNHOOK);

        // Now, lower the hang arm
        robot.linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.linearSlide.setPower(0.4);
        robot.linearSlide.setTargetPosition(500);
        robot.intake.deposit();

        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(1);
        robot.winch.setTargetPosition(1500);

        int encoder = robot.winch.getCurrentPosition();
        while (opModeIsActive() && Math.abs(encoder - 1500) > 20) {
            encoder = robot.winch.getCurrentPosition();
            telemetry.addData("Current position", encoder);
            telemetry.update();
        }

        robot.winch.setMotorDisable();
        robot.linearSlide.setTargetPosition(0);
        robot.intake.goToMin();
        sleep(500);

        followPath(drive, Paths.UNDO_UNHOOK);
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
        double difference = Double.MAX_VALUE;
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && opModeIsActive()) {
            robot.updateReadings();

            difference = robot.getSignedAngleDifference(robot.normAngle(pos), robot.getGyroHeading());
            double turnSpeed = Math.max(-TURN_MAX_SPEED, Math.min(TURN_MAX_SPEED, difference));

            turnSpeed = Math.copySign(Math.max(0.05, Math.abs(turnSpeed)), turnSpeed);

            telemetry.addData("Turn rate: ", turnSpeed);
            telemetry.update();

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
}
