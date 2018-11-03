package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.systems.RRVHardwarePushbot;

import java.util.Timer;
import java.util.TimerTask;

import motionProfileGenerator.ftc.tools.PathFollower;
import motionProfileGenerator.ftc.tools.StraightProfileGenerator;
import motionProfileGenerator.ftc.tools.WheelTrajectory;
import motionProfileGenerator.ftc.tools.Config;

/**
 * /**
 * * This file illustrates the concept of driving a motion profiling path using encoders and periods of time
 * * <p>
 * * This class uses encoders to help with more precise movement
 * * <p>
 * * The code assumes that you do have encoders on the wheels,
 * *
 * * <p>
 * * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 * <p>
 * For this class to work the Hardware file constants need to match your robot
 */
@Autonomous(name = "MPDriveForward", group = "MP Bot")
// @Disabled

public class LinearMotionProfileTest extends LinearOpMode {

    /* Declare OpMode members. */
    RRVHardwarePushbot robot = new RRVHardwarePushbot();   // Use a bot's hardware

    //Constants are inside the hardware file

    Timer time = new Timer();

    Config config;
    WheelTrajectory trajectory;
    PathFollower follower;

    /**
     * Leave these variables alone
     */
    boolean isRunning = false;
    int index = 0;
    double targetDistance;

    @Override
    public void runOpMode() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        //Sets both motors to brake mode
        robot.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        config = new Config(robot.dt, robot.max_velocity, robot.max_acceleration);

        //Generate Trajectory by entering how far you want to travels
        generateTrajectory(20);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        if (opModeIsActive()) {
            follower.configureEncoder(robot.rightRear.getCurrentPosition(), robot.leftRear.getCurrentPosition(), robot.ticks_per_revolution, robot.wheel_diameter);
            follower.configurePV(robot.kP_DriveForward, robot.kV_Drive);
            robot.rightRear.setTargetPosition(robot.rightRear.getCurrentPosition() + follower.unitToCounts(targetDistance));
            robot.leftRear.setTargetPosition(robot.leftRear.getCurrentPosition() + follower.unitToCounts(targetDistance));
            robot.rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            time.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (opModeIsActive()) {
                        isRunning = true;
                        int currLeftPos = robot.leftRear.getCurrentPosition();
                        int currRightPos = robot.rightRear.getCurrentPosition();

                        double left = follower.calculateLeftPower(index, currLeftPos);
                        double right = follower.calculateRightPower(index, currRightPos);

                        // Normalize the values so neither exceed +/- 1.0
                        double max = Math.max(Math.abs(left), Math.abs(right));
                        if (max > 1.0) {
                            left /= max;
                            right /= max;
                        }

                        robot.setLeftRight(left, right);
                    } else {
                        isRunning = false;
                        robot.setLeftRight(0, 0);
                        robot.leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        time.cancel();
                    }
                    if (index >= trajectory.getLeftTrajectory().length()) {
                        isRunning = false;
                        robot.setLeftRight(0,0);
                        robot.leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        time.cancel();
                    }

                    index++;
                }
            }, 0, (long) (robot.dt * 1000));
        }

        while (opModeIsActive()) {
            telemetry.addData("Left Power", robot.leftRear.getPower());
            telemetry.addData("Right Power", robot.rightRear.getPower());
            telemetry.addData("Current Segment", index);
            telemetry.update();
        }

        robot.setLeftRight(0,0);
    }

    private void generateTrajectory(double distance) {
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Generating ", "Trajectory");    //
        telemetry.update();
        targetDistance = distance;
        trajectory = StraightProfileGenerator.generateTrajectory(config, robot.wheelbase_width, distance);
        follower = new PathFollower(trajectory);

    }
}