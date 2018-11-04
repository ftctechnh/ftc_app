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
public abstract class SingleSampling extends AutoUtils {

    public static int trajectoryIndex = 0;

    private ElapsedTime runtime = new ElapsedTime();
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;
    private static final float mmTargetHeight   = (6) * mmPerInch;
    @Override
    public void runOpMode() throws InterruptedException {
        // Set up road runner
        SampleMecanumDriveREV drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        robot.init(false);
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_RETRACTED);
        //initVuforia();

        waitForStart();

        // Will unhook robot and bring it to starting pos
        //unhookFromLander(drive, robot);

        /*int[] probabilities = new int[3];
        ElapsedTime scanTime = new ElapsedTime();

        while(opModeIsActive() && scanTime.milliseconds() < 1000) {
            telemetry.addData("Yellow position", getMiddlePosition(detector.getFoundRect()));
            telemetry.addData("Found?", detector.isFound());

            if (!detector.isFound()) {
                probabilities[2] += 1;
            } else {
                if (getMiddlePosition(detector.getFoundRect()) > 400) {
                    probabilities[0] += 1;
                } else {
                    probabilities[1] += 1;
                }
            }
            telemetry.update();
        }

        int trajectoryIndex;
        if (probabilities[2] > probabilities[1] && probabilities[2] > probabilities[0]) {
            trajectoryIndex = 2;
        } else if (probabilities[0] > probabilities[1] && probabilities[0] > probabilities[2]) {
            trajectoryIndex = 0;
        } else {
            trajectoryIndex = 1;
        }*/

        Trajectory sampleTrajectory;
        if (startingPosition == StartingPosition.CRATER) {
            sampleTrajectory = Paths.CRATER_MINERAL_SELECTOR[trajectoryIndex];
        } else { // Case depot
            sampleTrajectory = Paths.DEPOT_MINERAL_SELECTOR[trajectoryIndex];
        }

        drive.followTrajectory(sampleTrajectory);
        while (opModeIsActive() && drive.isFollowingTrajectory()) {
            drive.update();
        }

        // Now we're in the depot and need to turn to a correct heading before deploying marker
        Trajectory turnTrajectory;
        Trajectory toCraterTrajectory;

        if (parkingLocation == ParkingLocation.OTHER_COLOR_CRATER) {
            turnTrajectory = Paths.DEPOT_TURN_OTHER_CRATER(sampleTrajectory.end());
            toCraterTrajectory = Paths.DEPOT_TO_OTHER_CRATER;
        } else {
            turnTrajectory = Paths.DEPOT_TURN_SAME_CRATER(sampleTrajectory.end());
            toCraterTrajectory = Paths.DEPOT_TO_SAME_CRATER;
        }

        drive.followTrajectory(turnTrajectory);
        while (opModeIsActive() && drive.isFollowingTrajectory()) {
            drive.update();
        }

        // Deploy the team marker
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_DEPLOY);
        robot.sleep(750);
        robot.markerDeployer.setPwmDisable();

        if (parkingLocation != ParkingLocation.DEPOT) {
            drive.followTrajectory(toCraterTrajectory);
            while (opModeIsActive() && drive.isFollowingTrajectory()) {
                drive.update();
            }
        }
    }
}
