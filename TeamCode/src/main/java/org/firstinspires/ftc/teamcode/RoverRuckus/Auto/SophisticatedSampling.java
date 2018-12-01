package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.DriveSystems.Mecanum.RoadRunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.EndGoal;
import org.firstinspires.ftc.teamcode.RoverRuckus.Deployers.Auto.StartingPosition;

public abstract class SophisticatedSampling extends AutoUtils {

    public EndGoal goal;

    class CheckWinchLambda extends FollowPathLambda {
        public CheckWinchLambda(SparkyTheRobot robot) { super(robot); runAtTermination = true;}

        @Override
        public void run() {
            if (!robot.hangSwitch.getState()) {
                terminate();
            }
        }

        @Override
        public void terminate() {
            robot.winch.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.winch.setPower(0);
            robot.winch.setMotorDisable();
            robot.intake.goToMin();
            super.terminate();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Set up road runner
        SampleMecanumDriveREV drive = new SampleMecanumDriveREV(hardwareMap);
        robot = new SparkyTheRobot(this);
        robot.calibrate(false);
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_RETRACTED);
        initVuforia();
        setWinchHoldPosition();

        // Display telemetry feedback
        telemetry.log().add("Running sophisticated sampling op-mode");
        telemetry.log().add("Starting from: [[" + startingPosition.name() + "]]");
        telemetry.log().add("Final actions: [[" + goal.name() + "]]");
        telemetry.update();

        GoldPosition goldLoc = waitAndWatchMinerals();

        // Use appropriate method for dehooking
        if (startingPosition == StartingPosition.DEPOT) {
            unhookFromLander(drive, robot, DetachMethod.STRAFE);
        } else {
            unhookFromLander(drive, robot, DetachMethod.TURN);
        }

        if (startingPosition == StartingPosition.DEPOT) {
            // Wait until we either reach the correct position or trigger the microswitch
            while (robot.hangSwitch.getState() &&
                    Math.abs(robot.winch.getCurrentPosition() - robot.winch.getTargetPosition()) > 20
                    && !isStopRequested()) {
                // While not pressed
            }
            robot.winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.winch.setPower(0);

            robot.sleep(2000); // Wait a little longer for lifter arm to come down
            // Eventually, this should be removed
            refoldMechanisms();
        }

        if (startingPosition == StartingPosition.DEPOT) {
            followPath(drive, Paths.DEPO_SAME_SELECTOR[goldLoc.index]);
        } else {
            followPath(drive, Paths.CRATER_SAME_SELECTOR[goldLoc.index], new CheckWinchLambda(robot));
        }

        // Initialize with it facing the depo along whichever wall it should strafe
        turnToPos(0);

        // Strafe to wall
        if (goal == EndGoal.BLUE_DOUBLE_SAMPLE) {
            followPath(drive, Paths.FORWARD_RIGHT);
        } else if (goal == EndGoal.BLUE_CRATER) {
            followPath(drive, Paths.UNHOOK);
        } else { // goal == RED_CRATER
            followPath(drive, Paths.UNDO_UNHOOK);
        }

        // Deploy the team marker
        robot.markerDeployer.setPosition(MARKER_DEPLOYER_DEPLOY);

        if (goal == EndGoal.BLUE_CRATER) {
            followPath(drive, Paths.DEPOT_TO_SAME_CRATER);
        } else if (goal == EndGoal.RED_CRATER) {
            followPath(drive, Paths.DEPOT_TO_OTHER_CRATER);
        } else {
            // The double sample!
            followPath(drive, Paths.DEPO_TO_CRATER_SELECTOR[goldLoc.index]);
        }
    }
}
