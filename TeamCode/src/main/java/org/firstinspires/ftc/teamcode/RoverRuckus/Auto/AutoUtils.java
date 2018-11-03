package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
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

    public void setupRobotHang() {
        hangMotor = new HoldingPIDMotor(robot.winch, 1);
        telemetry.log().add("Ensure hook is at a 90 degree angle before proceeding");
        telemetry.log().add("Use GP1 right and left triggers to hang the robot");

        while (!isStarted()) {
            telemetry.addData("Middle x position", getMiddlePosition(detector.getFoundRect()));
            telemetry.update();
            if (gamepad1.right_trigger > 0.1 || gamepad1.left_trigger > 0.1) {
                hangMotor.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            } else {
                hangMotor.setPower(0);
            }
        }
    }

    public void unhookFromLander(SampleMecanumDriveREV drive) {
        robot.winch.setPower(0.5);
        robot.winch.setTargetPosition(0);
        sleep(2000);
        followPath(drive, Paths.UNHOOK);
        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setPower(1);
        sleep(750);
        robot.winch.setPower(0);
        followPath(drive, Paths.UNDO_UNHOOK);
    }

    public int getMiddlePosition(Rect boundingBox) {
        return boundingBox.x + (boundingBox.width / 2);
    }

    public void followPath(SampleMecanumDriveREV drive, Trajectory trajectory) {
        drive.followTrajectory(trajectory);
        while (!isStopRequested() && drive.isFollowingTrajectory()) {
            drive.update();
        }
    }
}
