package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.EndGoal;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.StartingPosition;
import org.firstinspires.ftc.teamcode.Utilities.RoadRunner.AssetsTrajectoryLoader;

import java.io.IOException;
@Config
public abstract class SuperiorSampling extends AutoUtils {

    public EndGoal goal;
    public static int position;

    public void switchAppendagePositions() {
        robot.winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.winch.setPower(1);
        robot.winch.setTargetPosition(-1500);

        robot.intake.collect();

        robot.sleep(2500);

        robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.winch.setPower(0);
        refoldMechanisms();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Set up road runner
        SampleMecanumDriveREV drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        robot.calibrate(true);
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_RETRACTED);
        initVuforia();
        //setWinchHoldPosition();

        // Display telemetry feedback
        telemetry.log().add("Running sophisticated sampling op-mode");
        telemetry.log().add("Starting from: [[" + startingPosition.name() + "]]");
        telemetry.log().add("Final actions: [[" + goal.name() + "]]");
        telemetry.update();

        GoldPosition goldLoc = waitAndWatchMinerals();
        if (isStopRequested()) return;

        if (position == 0) {
            goldLoc = GoldPosition.LEFT;
        } else if (position == 1) {
            goldLoc = GoldPosition.CENTER;
        } else if (position == 2) {
            goldLoc = GoldPosition.RIGHT;
        }

        // Use appropriate method for dehooking
        /*if (startingPosition == StartingPosition.DEPOT) {
            unhookFromLander(drive, robot, DetachMethod.STRAFE);
        } else {
            unhookFromLander(drive, robot, DetachMethod.TURN);
        }*/

        robot.cameraPositioner.flipDown();

        if (startingPosition == StartingPosition.DEPOT) {
            //switchAppendagePositions();
            followPath(drive, Paths.DEPO_SAME_SELECTOR[goldLoc.index]);
        } else {
            try {
                followPath(drive, AssetsTrajectoryLoader.load("Crater" + goldLoc.fileName + "Sel"));
                followPath(drive, AssetsTrajectoryLoader.load("CraterBackup"));
                //switchAppendagePositions();
                turnToPos(Math.PI/4);
                followPath(drive, AssetsTrajectoryLoader.load("Crater" + goldLoc.fileName + "Dir"));
                if (goldLoc == GoldPosition.LEFT) {
                    turnToPos(3 * Math.PI / 2);
                } else if (goldLoc == GoldPosition.CENTER) {
                    turnToPos(5 * Math.PI / 4);
                } else {
                    turnToPos(0);
                }
                followPath(drive, AssetsTrajectoryLoader.load("CraterDouble" + goldLoc.fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Initialize with it facing the depo along whichever wall it should strafe

        // Strafe to wall

        /*if (goal == EndGoal.RED_CRATER) {
            followPath(drive, Paths.STRAFE_LEFT);
        } else {
            followPath(drive, Paths.STRAFE_RIGHT);
        }
        followPath(drive, Paths.FORWARD_RIGHT);*/
    }
}
