package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.IOException;

import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;
import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;


//@TeleOp(name="Main Tele-Op", group="_Competition")
public class MainTeleOp extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
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
    double desiredMax;
    double heading;

    boolean wasGP2LeftBumperPressed;
    boolean wasGP2RightBumperPressed;

    boolean nonrelativeDriveModeEnabled;
    int accelTime;

    boolean scale;

    ElapsedTime timeTillHeadingLock;

    ElapsedTime totalElapsedTime;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        waitForStart();
        rampController = new RampingController(robot.motorArr, accelTime);

        initialHeading = robot.getGyroHeading() + Math.PI;
        desiredHeading = initialHeading;

        wasGP2LeftBumperPressed = false;
        wasGP2RightBumperPressed = false;

        timeTillHeadingLock = new ElapsedTime();

        totalElapsedTime = new ElapsedTime();

        while (opModeIsActive()) {
            robot.updateReadings();

            scale = true;
            // Calculate speed reduction
            desiredMax = 1;

            // Toggle slow mode
            if (gamepad1.left_trigger > triggerThreshold) {// Left trigger activates slow mode
                desiredMax = minSlowModePower + ((1 - minSlowModePower) * (1 - gamepad1.left_trigger));
                robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else {
                robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }

            wasGP2LeftBumperPressed = gamepad2.left_bumper;
            wasGP2RightBumperPressed = gamepad2.right_bumper;

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            // Auto turning code
            heading = robot.getGyroHeading();

            if (turnRelevant || timeTillHeadingLock.milliseconds() < headingLockMS){
                desiredHeading = heading;
                turnSpeed = 0;

                if (turnRelevant) {
                    turnSpeed = gamepad1.right_stick_x;
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

            if (scale) {
                // Now, let's scale those powers
                double greatest = 0;
                for (double d : unscaledMotorPowers) {
                    greatest = Math.max(greatest, Math.abs(d));
                }
                greatest /= desiredMax;
                for (int i = 0; i < unscaledMotorPowers.length; i++) {
                    unscaledMotorPowers[i] = chop(unscaledMotorPowers[i] / greatest);
                }
            }

            rampController.setMotorPowers(unscaledMotorPowers);

            // Run above code at 1Khz
            //robot.writeLogTick();
            //robot.waitForTick(1000 / robot.hz);
        }
        rampController.quit();
        try {
            robot.closeLog();
        } catch (IOException e) {}

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

    public double[] getDesiredDirection() {
        double controllerAngle;
        if (getLeftStickDist(gamepad1) > triggerThreshold) {
            controllerAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) + Math.PI/2;
            controllerAngle = robot.normAngle(controllerAngle);
        } else {
            // If we're not moving, don't scale the values
            scale = false;
            return new double[]{0, 0, 0, 0};
        }

        double robotAngle;

        if (nonrelativeDriveModeEnabled) { // Right trigger activates nonrelative drive
            robotAngle = robot.normAngle(controllerAngle + heading);
        } else { // Default is relative drive
            robotAngle = controllerAngle;
        }

        return robot.getDrivePowersFromAngle(robotAngle);
    }
}
