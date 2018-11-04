package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;

@Config
@Autonomous(name="Depot - Other - Simple single sample")
public class SimpleSingleSamplingOther extends AutoUtils {

    public static int overrideTrajectoryIndex = -1;
    public static int unhookFromLander = 1;

    double TURN_MAX_SPEED = 0.6;
    double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45;

    private ElapsedTime runtime = new ElapsedTime();
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;
    private static final float mmTargetHeight   = (6) * mmPerInch;
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
        followPath(drive, Paths.UNHOOK);

        // Deploy the team marker
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_DEPLOY);
        robot.sleep(750);

        followPath(drive, Paths.DEPOT_TO_OTHER_CRATER);
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
