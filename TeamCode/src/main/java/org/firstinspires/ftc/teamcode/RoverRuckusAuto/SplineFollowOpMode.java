package org.firstinspires.ftc.teamcode.RoverRuckusAuto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.followers.MecanumPIDVAFollower;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.TrajectoryLoader;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.RoadRunnerMecanumInterface;

@Autonomous(name="Mecanum - Spline Demo", group="AutoDemo")
public class SplineFollowOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        /*RoadRunnerMecanumInterface drive = new RoadRunnerMecanumInterface(hardwareMap);
        // change these constraints to something reasonable for your drive
        DriveConstraints baseConstraints = new DriveConstraints(20.0,
                30.0, Math.PI / 2, Math.PI / 2);

        MecanumConstraints constraints = new MecanumConstraints(baseConstraints, drive.getTrackWidth(), drive.getWheelBase());

        Trajectory trajectory = TrajectoryLoader.load();
        // TODO: tune kV, kA, and kStatic in the following follower
        // then tune the PID coefficients after you verify the open loop response is roughly correct
        MecanumPIDVAFollower follower = new MecanumPIDVAFollower(
                drive,
                new PIDCoefficients(0, 0, 0),
                new PIDCoefficients(0, 0, 0),
                0.019791,
                0.00062,
                0.01263);

        waitForStart();

        follower.followTrajectory(trajectory);
        while (opModeIsActive() && follower.isFollowing()) {
            Pose2d currentPose = drive.getPoseEstimate();

            follower.update(currentPose);
            drive.updatePoseEstimate();
        }*/
    }
}
