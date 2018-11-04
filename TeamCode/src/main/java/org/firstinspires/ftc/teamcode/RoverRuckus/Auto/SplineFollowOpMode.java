package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.Utilities.Control.HoldingPIDMotor;

@Autonomous
public class SplineFollowOpMode extends LinearOpMode {
    SparkyTheRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDriveBase drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        robot.init(false);
        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(0.5);
        robot.winch.setTargetPosition(0);

        waitForStart();

        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setPower(-0.5);
        while (opModeIsActive()) {
            robot.updateReadings();
            if (robot.imu.getGravity().zAccel >= 9.7) {
                break;
            }
        }
        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(0);
        drive.followTrajectory(Paths.UNHOOK);
        while (opModeIsActive() && drive.isFollowingTrajectory()) {
            drive.update();
        }

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
        sleep(1500);

        drive.followTrajectory(Paths.UNDO_UNHOOK);
        while (opModeIsActive() && drive.isFollowingTrajectory()) {
            drive.update();
        }
    }
}