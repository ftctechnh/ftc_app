package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Hardware.QuadWheelHardware;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.Utilities.RoadRunner.AssetsTrajectoryLoader;

import java.io.IOException;

@Autonomous
@Disabled
public class JustPathsSampling extends AutoUtils {

    public static int startX = 38;
    public static int startY = 14;
    public static double startA = Math.PI * 0.75;

    public static int midX = -20;
    public static int midY = 63;
    public static double midA = 0;

    //public static int endX = -63;
    //public static int endY = 63;
    //public static double endA = 0;

    public static int reverseSel = 0;
    //public static int reversePlace = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        // Set up road runner
        SampleMecanumDriveREV drive = new SampleMecanumDriveREV(hardwareMap);
        //robot = new SparkyTheRobot(this);
        //robot.calibrate(false);

        try {
            followPath(drive, AssetsTrajectoryLoader.load("CraterCenterSel"));
            waitForStart();
            followPath(drive, AssetsTrajectoryLoader.load("CraterCenterDir"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
