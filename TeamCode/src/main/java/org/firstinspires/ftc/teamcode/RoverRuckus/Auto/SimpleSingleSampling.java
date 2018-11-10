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
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.Vision.VuforiaCVUtil;

@Config
@Autonomous(name="Depot - Same - Simple single sample")
public class SimpleSingleSampling extends AutoUtils {

    public DepotEndGoal goal;

    public static int overrideTrajectoryIndex = -1;
    public static int unhookFromLander = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        // Set up road runner
        SampleMecanumDriveREV drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        robot.init(true);
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_RETRACTED);
        initVuforia();

        if (unhookFromLander != 0) {
            unhookFromLander(drive, robot);
        } else {
            // Will unhook robot and bring it to starting pos
            waitForStart();
        }

        followPath(drive, Paths.BACKUP);

        int trajectoryIndex = -1;

        if (overrideTrajectoryIndex == -1) {
            if (getMiddlePosition(detector.getFoundRect()) < 50) {
                trajectoryIndex = 2;
            } else if (getMiddlePosition(detector.getFoundRect()) < 400) {
                trajectoryIndex = 1;
            } else {
                trajectoryIndex = 0;
            }
        } else {
            trajectoryIndex = overrideTrajectoryIndex;
        }

        followPath(drive, Paths.FORWARD);
        followPath(drive, Paths.DEPO_SAME_SELECTOR[trajectoryIndex]);

        // Initialize with it facing where you want it to go
        turnToPos(0);

        // Strafe to wall
        if (goal == DepotEndGoal.BLUE_DOUBLE_SAMPLE) {
            followPath(drive, Paths.FORWARD_RIGHT);
        } else if (goal == DepotEndGoal.BLUE_CRATER) {
            followPath(drive, Paths.UNDO_UNHOOK);
        } else { // goal == RED_CRATER
            followPath(drive, Paths.UNHOOK);
        }

        // Deploy the team marker
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_DEPLOY);

        if (goal == DepotEndGoal.BLUE_CRATER) {
            followPath(drive, Paths.DEPOT_TO_SAME_CRATER);
        } else if (goal == DepotEndGoal.RED_CRATER) {
            followPath(drive, Paths.DEPOT_TO_OTHER_CRATER);
        } else {
            // The double sample!
            followPath(drive, Paths.DEPO_TO_CRATER_SELECTOR[trajectoryIndex]);
        }
    }
}
