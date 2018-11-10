package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;

@Config
@Autonomous(name="Crater - Simple single sample")
public class CraterSimpleSingleSampling extends AutoUtils {

    public static int overrideTrajectoryIndex = -1;
    public static int unhookFromLander = 1;

    double TURN_MAX_SPEED = 0.6;
    double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45;

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
        followPath(drive, Paths.CRATER_SAME_SELECTOR[trajectoryIndex]);
    }
}
