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

    public ParkingLocation parkingLocation;
    public StartingPosition startingPosition;
    public SparkyTheRobot robot;

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
        initVuforia();

        // Will run through hang dialog until op mode starts
        setupRobotHang();

        // Will unhook robot and return it to starting pos
        unhookFromLander(drive);

        int trajectoryIndex = -1;
        do {
            if (detector.getFoundRect().x > 350) {
                trajectoryIndex = 2;
            } else if (detector.getFoundRect().x > 150) {
                trajectoryIndex = 1;
            } else if (detector.getFoundRect().x > 1) {
                trajectoryIndex = 0;
            }
        } while (trajectoryIndex == -1);

        // Force a certain trajectory
        //trajectoryIndex = 0;

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

        // Now, we need to unfold our mechanism
        robot.linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.linearSlide.setPower(0.4);
        robot.leftFlipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftFlipper.setPower(0.2);
        robot.rightFlipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFlipper.setPower(0.2);
        robot.linearSlide.setTargetPosition(500);
        robot.leftFlipper.setTargetPosition(500);
        robot.rightFlipper.setTargetPosition(500);
        robot.intake.collect();

        robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.winch.setPower(0.2);

        // Now, move hook down and stop on current spike
        while (opModeIsActive()) {
            try {
                LynxGetADCResponse resp = new LynxGetADCCommand(robot.rightHub, LynxGetADCCommand.Channel.MOTOR1_CURRENT, LynxGetADCCommand.Mode.ENGINEERING).sendReceive();
                int currentMA = resp.getValue();
                telemetry.addData("Current (mA)", currentMA);
                telemetry.update();
                if (currentMA > 1000) {
                    break;
                }
            } catch (LynxNackException e) {
                e.printStackTrace();
                break;
            }
        }
        robot.sleep(5000);
        robot.linearSlide.setTargetPosition(0);
        robot.leftFlipper.setTargetPosition(0);
        robot.rightFlipper.setTargetPosition(0);
        robot.sleep(1000);
        robot.intake.goToMin();

    }
}
