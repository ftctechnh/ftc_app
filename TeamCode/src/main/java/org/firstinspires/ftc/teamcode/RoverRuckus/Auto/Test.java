package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.path.heading.ConstantInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.DriveConstants;
import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.AutoUtils;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.ParkingLocation;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.Paths;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.SplineFollowOpMode;
import org.firstinspires.ftc.teamcode.RoverRuckus.Auto.StartingPosition;
import org.firstinspires.ftc.teamcode.Utilities.Lambda;

@Autonomous(name="NavTest")
public abstract class Test extends ExtendedAutoUtils {
    public SparkyTheRobot robot;
    private ElapsedTime runtime = new ElapsedTime();
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;
    private static final float mmTargetHeight   = (6) * mmPerInch;

    static boolean startdepot;

    @Override
    public void runOpMode() throws InterruptedException {
        // Set up road runner
        SampleMecanumDriveREV drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        robot.init(true);
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_RETRACTED);

        int mineralposition = 0;
        double startHeading = 0;

        waitForStart();

        //look for mineral

        unhookFromLander(drive, robot);

        Lambda lowerarm = new Lambda() {
            public void run() {

            }
        };


        Vector2D[] mineralAndDeposit = new Vector2D[] {
                (startdepot) ? Locations.depotParticles(mineralposition) : Locations.depotParticles(mineralposition),
                Locations.markerDeposit()
        };
        goToPosition(drive, lowerarm, mineralAndDeposit);


    }
}
