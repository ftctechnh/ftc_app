package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Hardware.QuadWheelHardware;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.EndGoal;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.StartingPosition;

@Autonomous
@Config
public class JustPathsSampling extends AutoUtils {

    public static int startX = 36;
    public static int startY = 36;
    public static double startA = Math.PI * 0.25;

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
        robot = new SparkyTheRobot(this);
        robot.calibrate(false);
        robot.setFrontDir(QuadWheelHardware.FrontDir.HOOK);
        waitForStart();

        if (reverseSel != 0) {
            robot.setFrontDir(QuadWheelHardware.FrontDir.HOOK);
        } else {
            robot.setFrontDir(QuadWheelHardware.FrontDir.CAMERA);
        }

        Trajectory sel = new TrajectoryBuilder(new Pose2d(startX, startY, startA), DriveConstants.BASE_CONSTRAINTS)
                .beginComposite()
                .splineTo(new Pose2d(midX, midY, midA))
                .closeComposite()
                .build();

        followPath(drive, sel);
    }
}
