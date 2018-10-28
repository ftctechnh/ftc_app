package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.followers.MecanumPIDVAFollower;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.RoadRunnerMecanumInterface;
import org.firstinspires.ftc.teamcode.Vision.VuforiaCVUtil;

@Autonomous(name="Single Sampling")
@Config
public class SingleSampling extends VuforiaCVUtil {

    private ElapsedTime runtime = new ElapsedTime();
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;
    private static final float mmTargetHeight   = (6) * mmPerInch;

    public static PIDCoefficients TRANSLATIONAL_COEFFS = new PIDCoefficients(0, 0, 0);
    public static PIDCoefficients HEADING_COEFFS = new PIDCoefficients(0, 0, 0);
    private static double K_V = 0.014;
    private static double K_A = 0;
    private static double K_STATIC = 0.07096;
    private static double MAX_VELO = 40;
    private static double MAX_ACCEL = 20;
    static DriveConstraints baseConstraints = new DriveConstraints(MAX_VELO, MAX_ACCEL, Math.PI / 2, Math.PI/2);

    @Override
    public void runOpMode() throws InterruptedException {
        // Enable the config of parameters
        TelemetryPacket k = new TelemetryPacket();
        FtcDashboard dashboard = FtcDashboard.getInstance();

        // Set up road runner
        RoadRunnerMecanumInterface drive = new RoadRunnerMecanumInterface(hardwareMap);
        MecanumConstraints constraints = new MecanumConstraints(baseConstraints, drive.getTrackWidth(), drive.getWheelBase());
        MecanumPIDVAFollower follower = new MecanumPIDVAFollower(
                drive,
                TRANSLATIONAL_COEFFS,
                HEADING_COEFFS,
                K_V,
                K_A,
                K_STATIC);

        initVuforia();

        while (!isStarted()) {
            telemetry.addData("X position", detector.getFoundRect().x);
            telemetry.update();
        }
        Trajectory trajectory = null;
        do {
            if (detector.getFoundRect().x > 350) {
                trajectory = Paths.DEPOT_RIGHT;
            } else if (detector.getFoundRect().x > 150) {
                trajectory = Paths.DEPOT_CENTER;
            } else if (detector.getFoundRect().x > 1) {
                trajectory = Paths.DEPOT_LEFT;
            }
        } while (trajectory == null);

        // Figure out what where the gold is
        follower.followTrajectory(trajectory);


        while (opModeIsActive() && follower.isFollowing()) {
            Pose2d currentPose = drive.getPoseEstimate();
            follower.update(currentPose);
            drive.updatePoseEstimate();
            telemetry.addData("X position", detector.getFoundRect().x);
            telemetry.update();
        }
    }
}
