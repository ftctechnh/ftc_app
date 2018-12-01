package org.firstinspires.ftc.teamcode.DriveSystems.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.MecanumHardware;
import org.firstinspires.ftc.teamcode.Utilities.Control.RampingController;

import static org.firstinspires.ftc.teamcode.Hardware.BaseHardware.clamp;
import static org.firstinspires.ftc.teamcode.Hardware.BaseHardware.getAngleDifference;

public class TeleOpMecanum extends LinearOpMode {

    MecanumHardware robot;

    RampingController rampController;

    final double turnVolatility = 2; // Higher number makes turning more jerklike, but faster

    final double moveMotorThreshold = 0;
    final double triggerThreshold = 0.10;
    final double minSlowModePower = 0.45;
    final int headingLockMS = 1000;

    double initialHeading;
    double desiredHeading;
    double difference;

    double turnSpeed;
    double heading;

    boolean wasGP1LeftBumperPressed;
    boolean wasGP1LeftTriggerPressed;
    boolean wasGP1APressed;
    boolean wasGP1BPressed;
    boolean wasGP1YPressed;

    boolean wasGP1RightBumperPressed;
    boolean wasGP1RightTriggerPressed;

    public boolean nonrelativeDriveModeEnabled;
    boolean slowMode;
    public int accelTime;

    boolean intakeUp;
    boolean flipperOut;
    boolean spinningIntake;
    boolean reversedIntake;

    ElapsedTime timeTillHeadingLock;
    ElapsedTime totalElapsedTime;

    @Override
    public void runOpMode() {
        telemetry.log().add("Got here");
        telemetry.update();

        robot = new MecanumHardware(this);
        robot.calibrate();

        waitForStart();
        rampController = new RampingController(robot.motorArr, accelTime);

        initialHeading = robot.getGyroHeading() + Math.PI;
        desiredHeading = initialHeading;

        wasGP1LeftBumperPressed = false;
        wasGP1LeftTriggerPressed = false;
        wasGP1APressed = false;
        wasGP1BPressed = false;
        wasGP1YPressed = false;
        slowMode = false;

        wasGP1RightBumperPressed = false;
        wasGP1RightTriggerPressed = false;

        intakeUp = false;
        flipperOut = false;
        spinningIntake = false;
        reversedIntake = false;

        timeTillHeadingLock = new ElapsedTime();

        totalElapsedTime = new ElapsedTime();

        while (opModeIsActive()) {
            long startTime = System.currentTimeMillis();

            robot.updateReadings();

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            // Auto turning code
            heading = robot.getGyroHeading();

            if (turnRelevant || timeTillHeadingLock.milliseconds() < headingLockMS){
                desiredHeading = heading;
                turnSpeed = 0;

                if (turnRelevant) {
                    turnSpeed = -gamepad1.right_stick_x;
                    if (gamepad1.left_trigger > triggerThreshold) {
                        turnSpeed *= 0.32;
                    }
                    timeTillHeadingLock.reset();
                }
            } else {
                // Auto turning
                if (gamepad2.right_trigger > triggerThreshold) {
                    difference = getAngleDifference(initialHeading, heading);
                    turnSpeed = difference / (Math.PI / turnVolatility);
                    turnSpeed = Math.copySign(turnSpeed, -(initialHeading - heading));

                    turnSpeed = clamp(turnSpeed);
                    if (Math.abs(turnSpeed) < 0.15) {
                        turnSpeed = Math.copySign(0.05, turnSpeed);
                    }
                } else {
                    turnSpeed = 0;
                }
            }

            double[] unscaledMotorPowers = getDesiredDirection();
            // Add turning and scaling to powers

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] += turnSpeed;
                } else {
                    unscaledMotorPowers[i] -= turnSpeed;
                }
            }

            // Now, let's scale those powers
            if (slowMode) {
                double greatest = 0;
                for (double d : unscaledMotorPowers) {
                    greatest = Math.max(greatest, Math.abs(d));
                }
                greatest /= 0.3;
                for (int i = 0; i < unscaledMotorPowers.length; i++) {
                    unscaledMotorPowers[i] = chop(unscaledMotorPowers[i] / greatest);
                }
            }

            rampController.setMotorPowers(unscaledMotorPowers);

            if (gamepad1.right_bumper && !wasGP1RightBumperPressed) {
                wasGP1RightBumperPressed = true;
                slowMode = !slowMode;
                if (slowMode) {
                    robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else {
                    robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
            }

            if (!gamepad1.right_bumper) {wasGP1RightBumperPressed = false;}
            if (!gamepad1.left_bumper) {wasGP1LeftBumperPressed = false;}
            if (!(gamepad1.left_trigger > triggerThreshold)) {wasGP1LeftTriggerPressed = false;}
            if (!gamepad1.a) {wasGP1APressed = false;}
            if (!gamepad1.b) {wasGP1BPressed = false;}
            if (!gamepad1.y) {wasGP1YPressed = false;}

            robot.updateLogs();
            long endTime = System.currentTimeMillis();
            telemetry.addData("Loop time", endTime - startTime);
            telemetry.update();
        }
        rampController.quit();
        robot.stop();

    }
    private double chop(double d) { // Cutoff all signals being sent to the motor below a threshold
        if (Math.abs(d) < moveMotorThreshold) {
            return 0;
        } else {
            return d;
        }
    }

    public double getLeftStickDist(Gamepad g) {
        return Math.sqrt(g.left_stick_x*g.left_stick_x + g.left_stick_y*g.left_stick_y);
    }

    public double[] getDesiredDirection() { //Reads controller, checks other variables, etcetera. Returns desired angle in relation to
        double controllerAngle;
        if (getLeftStickDist(gamepad1) > triggerThreshold) {
            controllerAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) + Math.PI/2;
            controllerAngle = robot.normAngle(controllerAngle); //normalize angle returns value between pi and 2pi
        } else return new double[]{0, 0, 0, 0};


        double robotAngle; //desired angle in relation to self

        if (nonrelativeDriveModeEnabled) { // Right trigger activates nonrelative drive
            robotAngle = robot.normAngle(controllerAngle + heading);
        } else { // Default is relative drive
            robotAngle = controllerAngle;
        }


        double headingInterval = Math.PI / 4;
        robotAngle = Math.round(robotAngle / headingInterval) * headingInterval;




        return robot.getDrivePowersFromAngle(robotAngle);
    }

    private int getClosestTargetIndex(int[] targets, int current) {
        int bestDistance = Integer.MAX_VALUE;
        int best = 0;
        for (int i = 0; i < targets.length; i++) {
            int trialDist = Math.abs(current - targets[i]);
            if (trialDist < bestDistance) {
                bestDistance = trialDist;
                best = i;
            }
        }

        return best;
    }
}
