package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;


@TeleOp(name="Nullbot: Teleop Nonrelative", group="Nullbot")
public class NullbotTeleopNonrelative extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();

    final double turnVolatility = 2; // Higher number makes turning more jerklike, but faster

    final double moveMotorThreshold = 0;
    final double triggerThreshold = 0.10;
    final double minSlowModePower = 0.15;
    final int headingLockMS = 250;
    double initialHeading;
    double desiredHeading;
    double difference;
    double turnSpeed;
    double desiredMax;
    double heading;

    boolean leftGemHitterDown;

    boolean wasAButtonPressed;
    boolean wasLeftBumperPressed;
    boolean wasRightBumperPressed;
    boolean scale;

    ElapsedTime timeTillHeadingLock;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, true);

        waitForStart();

        initialHeading = robot.getGyroHeading();
        desiredHeading = initialHeading;

        wasLeftBumperPressed = false;
        wasRightBumperPressed = false;
        wasAButtonPressed = false;
        leftGemHitterDown = false;
        timeTillHeadingLock = new ElapsedTime();

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            scale = true;
            // Calculate speed reduction
            desiredMax = 1;

            if (gamepad1.left_trigger > triggerThreshold) {// Left trigger activates slow mode
                desiredMax = minSlowModePower + ((1 - minSlowModePower) * (1 - gamepad1.left_trigger));
            }

            adjustDesiredHeading();
            setServoPositions();

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            // Auto turning code
            heading = robot.getGyroHeading();
            difference = getAngleDifference(desiredHeading, heading);


            if (turnRelevant || timeTillHeadingLock.milliseconds() < headingLockMS){
                turnSpeed = gamepad1.right_stick_x;

                if (turnRelevant) {
                    timeTillHeadingLock.reset();
                }
            } else {
                turnSpeed = difference / (Math.PI / turnVolatility);
                turnSpeed = clamp(turnSpeed);
            }

            double[] unscaledMotorPowers = getDesiredDirection();
            // Add turning and scaling to powers

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                unscaledMotorPowers[i] *= desiredMax;
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
                for (int i = 0; i < unscaledMotorPowers.length; i++) {
                    unscaledMotorPowers[i] = chop(unscaledMotorPowers[i] / greatest);
                }
            }

            robot.setMotorSpeeds(unscaledMotorPowers);

            telemetry.addLine()
                    .addData("TurnSpeed", turnSpeed);
            telemetry.addLine()
                    .addData("Raw gyro direction", robot.getGyroHeadingRaw());
            telemetry.addLine()
                    .addData("Raw compass direction", robot.getCompassHeading());
            telemetry.addLine()
                    .addData("Gyro error", robot.gyroError);
            telemetry.addLine()
                    .addData("EAG direction", robot.getGyroHeading());
            telemetry.addLine()
                    .addData("Initial compass heading", robot.initialCompassHeading);
            telemetry.update(); // Send telemetry data to driver station

            // Run above code at 25hz
            robot.writeLogTick(gamepad1);
            robot.waitForTick(1000 / robot.hz);
        }

    }
    private double chop(double d) { // Cutoff all signals being sent to the motor below a threshold
        if (Math.abs(d) < moveMotorThreshold) {
            return 0;
        } else {
            return d;
        }
    }
    private void adjustDesiredHeading() {
        // Turn 45 degrees based on right and left buttons
        if (!wasLeftBumperPressed && gamepad1.left_bumper) {desiredHeading += Math.PI/4;}
        if (!wasRightBumperPressed && gamepad1.right_bumper) {desiredHeading -= Math.PI/4;}

        // Normalize desired heading between 0 and tau radians
        desiredHeading = robot.normAngle(desiredHeading);

        // Store bumper positions for next run through loop
        wasLeftBumperPressed = gamepad1.left_bumper;
        wasRightBumperPressed = gamepad1.right_bumper;
    }
    private void setServoPositions() {
        if (!wasAButtonPressed && gamepad1.a) {
            leftGemHitterDown = !leftGemHitterDown;
        }

        if (leftGemHitterDown) {
            robot.leftGemHitter.setPosition(0.0);
        } else {
            robot.leftGemHitter.setPosition(0.5);
        }
    }
    public double getAngleDifference(double d1, double d2) {
        double diff = d2 - d1;
        if (d1 > Math.PI) {d1 -= 2 * Math.PI;}
        if (d2 > Math.PI) {d2 -= 2 * Math.PI;}

        double diff2 = d2 - d1;

        if (Math.abs(diff) < Math.abs(diff2)) {
            return diff;
        } else {
            return diff2;
        }
    }

    public double getLeftStickDist(Gamepad g) {
        return Math.sqrt(g.left_stick_x*g.left_stick_x + g.left_stick_y*g.left_stick_y);
    }

    public double[] getDesiredDirection() {
        double controllerAngle;
        if (gamepad1.dpad_right) {
            if (gamepad1.dpad_up) {
                controllerAngle = Math.PI/4;
            } else if (gamepad1.dpad_down) {
                controllerAngle = 3*Math.PI/4;
            } else {
                controllerAngle = Math.PI/2;
            }
        } else if (gamepad1.dpad_left) {
            if (gamepad1.dpad_up) {
                controllerAngle = 7*Math.PI/4;
            } else if (gamepad1.dpad_down) {
                controllerAngle = 5*Math.PI/4;
            } else {
                controllerAngle = 3*Math.PI/2;
            }
        } else if (gamepad1.dpad_up) {
            controllerAngle = 0.0;
        } else if (gamepad1.dpad_down) {
            controllerAngle = Math.PI;
        } else if (getLeftStickDist(gamepad1) > triggerThreshold) {
            controllerAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) + Math.PI/2;
        } else {
            // If we're not moving, don't scale the values
            scale = false;
            return new double[]{0, 0, 0, 0};
        }

        double robotAngle;

        if (gamepad1.right_trigger > triggerThreshold) {// Right trigger activates relative drive
            robotAngle = controllerAngle;
        } else { // Default is nonrelative drive
            // When calculating the robot's angle, we don't have to take into account the initial heading,
            // because it is always 0 (we'll always calibrate the gyro)
            robotAngle = robot.normAngle(controllerAngle + heading);
        }

        double[] unscaledPowers = new double[4];
        unscaledPowers[0] = Math.sin(robotAngle + Math.PI/4);
        unscaledPowers[1] = Math.cos(robotAngle + Math.PI/4);
        unscaledPowers[2] = unscaledPowers[1];
        unscaledPowers[3] = unscaledPowers[0];
        return unscaledPowers;
    }
}
