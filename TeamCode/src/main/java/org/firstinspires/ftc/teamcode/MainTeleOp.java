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
    boolean wasRightTriggerPressed;
    boolean wasLeftTriggerPressed;
    boolean wasGP2APressed;
    boolean flashMode;
    boolean nonrelativeDriveModeEnabled;
    boolean scale;

    ElapsedTime timeTillHeadingLock;

    ConstrainedPIDMotor lift;
    ConstrainedPIDMotor zType;

    ElapsedTime timeSinceDriveModeToggle;
    //ElapsedTime timeSinceSlowModeToggle;

    ElapsedTime totalElapsedTime;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        lift = new ConstrainedPIDMotor(robot.lift, 100, 0.6, 0.6, 0, -2500, telemetry);
        zType = new ConstrainedPIDMotor(robot.zType, 100, 0.4, 0.4, 0, 12288, telemetry);

        waitForStart();

        initialHeading = robot.getGyroHeading();
        desiredHeading = initialHeading;

        wasGP2LeftBumperPressed = false;
        wasGP2RightBumperPressed = false;

        flashMode = false;
        timeTillHeadingLock = new ElapsedTime();
        timeSinceDriveModeToggle = new ElapsedTime();

        //timeSinceSlowModeToggle = new ElapsedTime();
        totalElapsedTime = new ElapsedTime();
        wasRightTriggerPressed = false;
        wasLeftTriggerPressed = false;
        wasGP2APressed = false;

        while (opModeIsActive()) {
            robot.updateReadings();
            // Toggle control mode
            if (gamepad1.right_trigger > triggerThreshold && !wasRightTriggerPressed &&
                    timeSinceDriveModeToggle.milliseconds() > 100) {
                nonrelativeDriveModeEnabled = !nonrelativeDriveModeEnabled;
                timeSinceDriveModeToggle.reset();
            }

            zType.forwardRunSpeed = (1 - gamepad2.left_trigger) * 0.35 + 0.05;
            zType.backwardRunSpeed = zType.forwardRunSpeed;

            wasRightTriggerPressed = gamepad1.right_trigger > triggerThreshold;

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

            if (true) {

                setOverride((gamepad1.a || gamepad2.b) && !gamepad1.start && !gamepad2.start);

                if (gamepad1.start && !gamepad1.back && !gamepad1.a) { // Start coasts all motors
                    lift.setDirection(ConstrainedPIDMotor.Direction.COAST);
                    zType.setDirection(ConstrainedPIDMotor.Direction.COAST);
                } else {
                    // Adjust lift
                    if (gamepad1.dpad_up || gamepad2.dpad_up) {
                        lift.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
                    } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
                        lift.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
                    } else {
                        lift.setDirection(ConstrainedPIDMotor.Direction.HOLD);
                    }

                    if (gamepad1.dpad_right || gamepad2.dpad_right) {
                        zType.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
                    } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
                        zType.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
                    } else if (gamepad2.a){
                        zType.setTargetToSeek(2500);
                    } else if (gamepad2.x){
                        zType.setTargetToSeek(0);
                    } else if (!zType.seekingPosition){
                        zType.setDirection(ConstrainedPIDMotor.Direction.HOLD, false);
                    }

                    if (gamepad1.b) {
                        robot.openBlockClaw();
                    } else if (gamepad1.x) {
                        robot.closeBlockClaw();
                    }

                    if (gamepad2.left_bumper && !wasGP2LeftBumperPressed) {
                        robot.toggleRelicClaw();

                    }

                    if (gamepad1.right_bumper || gamepad2.right_bumper) {
                        if (gamepad2.back && gamepad2.a) {
                            robot.relicFipperPosition -= 0.02;
                            robot.updateFlipperPos();

                        } else if (!gamepad2.back && gamepad2.a) { // Explicitly stated for clarity
                            robot.relicFipperPosition += 0.02;
                            robot.updateFlipperPos();

                        } else if (gamepad2.right_bumper && !wasGP2RightBumperPressed) {
                            robot.toggleRelicClawFlipper();
                        }
                    }
                    wasGP2LeftBumperPressed = gamepad2.left_bumper;
                    wasGP2RightBumperPressed = gamepad2.right_bumper;
                    wasGP2APressed = gamepad2.a;

                    // Taunt code
                    if (!gamepad1.y && !gamepad2.y) {
                        if (zType.seekingPosition) {
                            if (zType.targetPos == 0) {
                                robot.almostRaiseWhipSnake();
                            } else {
                                robot.raiseWhipSnake();
                            }
                        } else {
                            robot.raiseWhipSnake();
                        }
                    } else {
                        robot.almostRaiseWhipSnake();
                    }

                    // Intake speed control
                    if (Math.abs(gamepad2.left_stick_x) > triggerThreshold) {
                        robot.setIntakeSpeed(gamepad2.left_stick_x);
                    } else {
                        robot.setIntakeSpeed(0);
                    }

                    if ((gamepad1.start && gamepad1.a) || (gamepad2.start && gamepad2.a)) {
                        lift.encoderOffset = robot.lift.getCurrentPosition();
                    }

                    if (Math.abs(gamepad2.left_stick_x) > triggerThreshold) {
                        robot.relicFipperPosition += gamepad1.left_stick_x * 0.01;
                        robot.relicClawFlipper.setPosition(robot.relicFipperPosition);
                    }
                }
            }

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            // Auto turning code
            heading = robot.getGyroHeading();


            if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                turnSpeed = 0.35;
            } else if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                turnSpeed = -0.35;
            } else if (turnRelevant || timeTillHeadingLock.milliseconds() < headingLockMS){
                desiredHeading = heading;
                turnSpeed = 0;

                if (turnRelevant) {
                    turnSpeed = gamepad1.right_stick_x;
                    if (gamepad1.left_trigger > triggerThreshold) {
                        turnSpeed *= 0.2;
                    }
                    timeTillHeadingLock.reset();
                }
            } else {
                // Auto turning
                if (false) {
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
                    .addData("Current tick", totalElapsedTime.milliseconds());
            telemetry.addLine().addData("Seeking?", zType.seekingPosition);
            telemetry.update(); // Send telemetry data to driver station

            // Run above code at 1Khz
            //robot.writeLogTick();
            //robot.waitForTick(1000 / robot.hz);
        }
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

    public void setOverride (boolean b) {
        lift.override = b;
        zType.override = b;
    }
}
