package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;

@Config
@Autonomous(name="Sparky - Test Vision", group="Diagnostics")
public class VisionSampling extends AutoUtils {

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

        while (!isStopRequested()) {
            int trajectoryIndex = -1;

            if (trajectoryIndex == -1) {
                if (getMiddlePosition(detector.getFoundRect()) < 50) {
                    trajectoryIndex = 2;
                } else if (getMiddlePosition(detector.getFoundRect()) < 400) {
                    trajectoryIndex = 1;
                } else {
                    trajectoryIndex = 0;
                }
            }

            telemetry.addData("Trajectory index", trajectoryIndex);
            telemetry.update();
        }
    }
}
