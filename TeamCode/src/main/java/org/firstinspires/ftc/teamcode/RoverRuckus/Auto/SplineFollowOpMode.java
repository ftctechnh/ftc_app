package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;


/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous
public class SplineFollowOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDriveBase drive = new SampleMecanumDriveREV(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        /*drive.followTrajectory(Paths.DEPOT_LEFT);
        while (!isStopRequested() && drive.isFollowingTrajectory()) {
            drive.update();
        }

        drive.followTrajectory(Paths.DEPOT_TO_BLUE_CRATER(Paths.DEPOT_LEFT.end()));
        while (!isStopRequested() && drive.isFollowingTrajectory()) {
            drive.update();
        }*/

        drive.followTrajectory(
                new TrajectoryBuilder(new Pose2d(0, 0, 0), DriveConstants.BASE_CONSTRAINTS)
                .splineTo(new Pose2d(0, 5, 0), new ConstantInterpolator(0))
                .splineTo(new Pose2d(0, 0, 0), new ConstantInterpolator(0))
                .build()
        );
        while (!isStopRequested() && drive.isFollowingTrajectory()) {
            drive.update();
        }
    }
}