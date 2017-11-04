package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.ConstrainedPIDMotor.Direction.BACKWARD;
import static org.firstinspires.ftc.teamcode.ConstrainedPIDMotor.Direction.COAST;
import static org.firstinspires.ftc.teamcode.ConstrainedPIDMotor.Direction.FORWARD;
import static org.firstinspires.ftc.teamcode.ConstrainedPIDMotor.Direction.HOLD;
import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;
import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;


//@TeleOp(name="Main Tele-Op", group="_Competition")
public class MainTeleOp extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();

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

    boolean wasLeftBumperPressed;
    boolean wasRightBumperPressed;
    boolean wasRightTriggerPressed;
    boolean wasLeftTriggerPressed;
    boolean flashMode;
    boolean nonrelativeDriveModeEnabled;
    boolean scale;

    ElapsedTime timeTillHeadingLock;

    ConstrainedPIDMotor lift;
    ConstrainedPIDMotor zType;

    ElapsedTime timeSinceDriveModeToggle;
    ElapsedTime timeSinceSlowModeToggle;

    ElapsedTime totalElapsedTime;

    ElapsedTime timeAPressed;
    boolean wasBackPressed;
    boolean slowMode;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, true, gamepad2);

        lift = new ConstrainedPIDMotor(robot.lift, 100, 0.6, 0.6, 0, -2500);
        zType = new ConstrainedPIDMotor(robot.zType, 100, 0.4, 0.3, 0, 12288);

        waitForStart();

        initialHeading = robot.getGyroHeading();
        desiredHeading = initialHeading;

        wasLeftBumperPressed = false;
        wasRightBumperPressed = false;
        flashMode = false;
        timeTillHeadingLock = new ElapsedTime();

        timeSinceDriveModeToggle = new ElapsedTime();
        timeSinceSlowModeToggle = new ElapsedTime();
        totalElapsedTime = new ElapsedTime();
        timeAPressed = new ElapsedTime();
        wasRightTriggerPressed = false;
        wasLeftTriggerPressed = false;
        wasBackPressed = false;
        slowMode = false;


        while (opModeIsActive()) {

            // Toggle control mode
            if (gamepad1.right_trigger > triggerThreshold && !wasRightTriggerPressed &&
                    timeSinceDriveModeToggle.milliseconds() > 100) {
                nonrelativeDriveModeEnabled = !nonrelativeDriveModeEnabled;
                timeSinceDriveModeToggle.reset();
            }

            wasRightTriggerPressed = gamepad1.right_trigger > triggerThreshold;

            scale = true;
            // Calculate speed reduction
            desiredMax = 1;

            // Toggle slow mode
            if (gamepad1.left_trigger > triggerThreshold || robot.zType.getCurrentPosition() > 3000) {// Left trigger activates slow mode
                desiredMax = minSlowModePower + ((1 - minSlowModePower) * (1 - gamepad1.left_trigger));
                robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else {
                robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }


            if (!robot.isTestChassis) {

                if (!gamepad1.a) {
                    timeAPressed.reset();
                }
                if (gamepad1.back && !wasBackPressed) {
                    timeAPressed.reset();
                }

                setOverride((gamepad1.a && !gamepad1.start));

                if (gamepad1.start && !gamepad1.back && !gamepad1.a) { // Back coasts all motors
                    lift.setDirection(COAST);
                    zType.setDirection(COAST);
                } else {
                    // Adjust lift
                    if (gamepad1.dpad_up || gamepad2.dpad_up) {
                        lift.setDirection(FORWARD);
                    } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
                        lift.setDirection(BACKWARD);
                    } else {
                        lift.setDirection(HOLD);
                    }

                    if (gamepad1.dpad_right || gamepad2.dpad_right) {
                        zType.setDirection(FORWARD);
                    } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
                        zType.setDirection(BACKWARD);
                    } else {
                        zType.setDirection(HOLD);
                    }

                    if (gamepad1.b) {
                        robot.openBlockClaw();
                    } else if (gamepad1.x) {
                        robot.closeBlockClaw();
                    }

                    if (gamepad1.left_bumper && !wasLeftBumperPressed) {
                        robot.toggleRelicClaw();
                    }
                    if (gamepad1.right_bumper) {
                        if (gamepad1.back && gamepad1.a && timeAPressed.milliseconds() > 500) {
                            robot.relicFipperPosition -= 1;
                            robot.updateFlipperPos();

                        } else if (!gamepad1.back && gamepad1.a && timeAPressed.milliseconds() > 500) { // Explicitly stated for clarity
                            robot.relicFipperPosition += 1;
                            robot.updateFlipperPos();

                        } else if (!wasRightBumperPressed) {
                            robot.toggleRelicClawFlipper();
                        }
                    }
                    wasLeftBumperPressed = gamepad1.left_bumper;
                    wasRightBumperPressed = gamepad1.right_bumper;

                    // Taunt code
                    if (!gamepad1.y) {
                        robot.raiseWhipSnake();
                    } else {
                        robot.lowerWhipSnake();
                    }

                    if (gamepad1.start && gamepad1.back) {

                        robot.gyroError = robot.getGyroHeadingRaw();
                    }

                    if (gamepad1.start && gamepad1.a) {
                        lift.encoderOffset = robot.lift.getCurrentPosition();
                    }

                    if (Math.abs(gamepad2.left_stick_x) > triggerThreshold) {
                        robot.relicFipperPosition += gamepad1.left_stick_x * 0.01;
                        robot.relicClawFlipper.setPosition(robot.relicFipperPosition);
                    }

                    wasBackPressed = gamepad1.back;
                }
            }

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            // Auto turning code
            heading = robot.getGyroHeading();


            if (turnRelevant || timeTillHeadingLock.milliseconds() < headingLockMS){
                desiredHeading = heading;
                turnSpeed = 0;

                if (turnRelevant) {
                    turnSpeed = gamepad1.right_stick_x;
                    if (gamepad1.left_trigger > triggerThreshold) {
                        turnSpeed /= 10.0;
                    }
                    timeTillHeadingLock.reset();
                }
            } else {
                // Auto turning
                if (true) {
                    difference = getAngleDifference(desiredHeading, heading);
                    turnSpeed = difference / (Math.PI / turnVolatility);
                    turnSpeed = clamp(turnSpeed);
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

            // Turning around a point bypasses all previous settings
            if (gamepad1.left_trigger < triggerThreshold) { // If there's nothing that needs precision running
                robot.adjustMotorEncoders(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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

            robot.setMotorSpeeds(unscaledMotorPowers);

            telemetry.addLine()
                    .addData("Current lift position", robot.lift.getCurrentPosition());
            telemetry.addLine()
                    .addData("Locked position", lift.lockPos);
            telemetry.addLine()
                    .addData("Current tick", totalElapsedTime.milliseconds());
            telemetry.update(); // Send telemetry data to driver station

            // Run above code at 1Khz
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

        double[] unscaledPowers = new double[4];
        unscaledPowers[0] = Math.sin(robotAngle + Math.PI/4);
        unscaledPowers[1] = Math.cos(robotAngle + Math.PI/4);
        unscaledPowers[2] = unscaledPowers[1];
        unscaledPowers[3] = unscaledPowers[0];
        return unscaledPowers;
    }

    public void setOverride (boolean b) {
        lift.override = b;
        zType.override = b;
    }
}
