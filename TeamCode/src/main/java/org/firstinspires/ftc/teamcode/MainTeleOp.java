package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

    final int[] liftPresetPositions = new int[]{0, -650, -1280};

    double initialHeading;
    double desiredHeading;
    double difference;

    double turnSpeed;
    double desiredMax;
    double heading;

    boolean wasGP2LeftBumperPressed;
    boolean wasGP2RightBumperPressed;

    boolean wasLiftPosUpPressed;
    boolean wasLiftPosDownPressed;

    boolean nonrelativeDriveModeEnabled;

    boolean scale;

    ElapsedTime timeTillHeadingLock;

    ConstrainedPIDMotor lift;
    ConstrainedPIDMotor zType;

    ElapsedTime totalElapsedTime;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        lift = new ConstrainedPIDMotor(robot.lift, 100, 1.0, 0.5, 0, -2500, telemetry, false);
        zType = new ConstrainedPIDMotor(robot.zType, 100, 0.4, 0.4, 0, 12288, telemetry, true);

        waitForStart();
        robot.raiseWhipSnake();

        initialHeading = robot.getGyroHeading() + Math.PI;
        desiredHeading = initialHeading;

        wasGP2LeftBumperPressed = false;
        wasGP2RightBumperPressed = false;

        wasLiftPosUpPressed = false;
        wasLiftPosDownPressed = false;

        timeTillHeadingLock = new ElapsedTime();

        //timeSinceSlowModeToggle = new ElapsedTime();
        totalElapsedTime = new ElapsedTime();

        while (opModeIsActive()) {
            robot.updateReadings();

            zType.forwardRunSpeed = (1 - gamepad2.left_trigger) * 0.20 + 0.20;
            zType.backwardRunSpeed = zType.forwardRunSpeed;

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

            setOverride((gamepad1.a || gamepad2.b) && !gamepad1.start && !gamepad2.start);

            if (gamepad1.start && !gamepad1.back && !gamepad1.a) { // Start coasts all motors
                lift.setDirection(ConstrainedPIDMotor.Direction.COAST);
                zType.setDirection(ConstrainedPIDMotor.Direction.COAST);
            } else {
                // Adjust lift
                if (gamepad2.dpad_up) {
                    lift.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
                } else if (gamepad2.dpad_down) {
                    lift.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
                } else if (gamepad1.right_bumper && !wasLiftPosUpPressed) {
                    //lift.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
                    setLiftSeekPosition(1);
                    wasLiftPosUpPressed = true;
                    telemetry.log().add("Going up!");
                } else if (gamepad1.right_trigger > triggerThreshold && !wasLiftPosDownPressed) {
                    //lift.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
                    wasLiftPosDownPressed = true;
                    setLiftSeekPosition(-1);
                    telemetry.log().add("Going down!");
                } else {
                    lift.setDirection(ConstrainedPIDMotor.Direction.HOLD, false);
                }

                if (gamepad1.dpad_right || gamepad2.dpad_right) {
                    zType.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
                } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
                    zType.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
                } else if (gamepad2.a) {
                    zType.lockPos = 2500;
                } else if (gamepad2.x) {
                    zType.lockPos = 0;
                } else {
                    zType.setDirection(ConstrainedPIDMotor.Direction.HOLD, false);
                }
            }

            if (gamepad1.b || gamepad2.b) {
                robot.openBlockClaw();
            } else if (gamepad1.x || gamepad2.x) {
                robot.closeBlockClaw();
            }

            if (gamepad2.left_bumper && !wasGP2LeftBumperPressed) {
                robot.toggleRelicClaw();

            }

            if (gamepad2.right_bumper) {
                if (gamepad2.back && gamepad2.a) {
                    robot.relicFipperPosition -= 0.02;
                    robot.updateFlipperPos();

                } else if (!gamepad2.back && gamepad2.a) { // Explicitly stated for clarity
                    robot.relicFipperPosition += 0.02;
                    robot.updateFlipperPos();

                } else if (!wasGP2RightBumperPressed) {
                    robot.toggleRelicClawFlipper();
                }
            }

            wasGP2LeftBumperPressed = gamepad2.left_bumper;
            wasGP2RightBumperPressed = gamepad2.right_bumper;

            // Taunt code
            if (!gamepad1.y && !gamepad2.y) {
                int currentPosition = robot.zType.getCurrentPosition();
                if (currentPosition < 500) {
                    if (currentPosition < 100) {
                        robot.raiseWhipSnake();
                    } else {
                        robot.almostRaiseWhipSnake();
                    }
                } else {
                    robot.raiseWhipSnake();
                }
            } else {
                robot.almostRaiseWhipSnake();
            }

            // Intake speed control
            if (Math.abs(gamepad2.right_stick_x) > triggerThreshold) {
                robot.setIntakeSpeed(gamepad2.right_stick_x);
                telemetry.addData("Spinning intake", true);

            } else {
                robot.setIntakeSpeed(0);
                telemetry.addData("Spinning intake", false);
            }
            telemetry.addData("Zarm lockpos", zType.lockPos);

            if (Math.abs(gamepad2.left_stick_y) > 0.5) {
                if (Math.signum(gamepad2.left_stick_y) > 0) {
                    if (robot.intakeTarget == NullbotHardware.IntakeTarget.LOWERED) {
                        robot.closeBlockClaw();
                    }
                    robot.raiseIntake();
                } else {
                    if (robot.intakeTarget == NullbotHardware.IntakeTarget.RAISED) {
                        robot.closeBlockClaw();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                robot.openBlockClaw();
                            }
                        }, 300);
                    }
                    robot.lowerIntake();
                }
            }

            if ((gamepad1.start && gamepad1.a) || (gamepad2.start && gamepad2.a)) {
                lift.encoderOffset = robot.lift.getCurrentPosition();
            }

            if (Math.abs(gamepad2.left_stick_x) > triggerThreshold) {
                robot.relicFipperPosition += gamepad1.left_stick_x * 0.01;
                robot.relicClawFlipper.setPosition(robot.relicFipperPosition);
            }

            telemetry.addData("Current lift position", robot.lift.getCurrentPosition());

            if (!gamepad1.right_bumper) {
                wasLiftPosUpPressed = false;
            }
            if (!(gamepad1.right_trigger > triggerThreshold)) {
                wasLiftPosDownPressed = false;
            }

            boolean turnRelevant = Math.abs(gamepad1.right_stick_x) > 0.25;

            // Auto turning code
            heading = robot.getGyroHeading();

            /*if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                turnSpeed = 0.35;
            } else if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                turnSpeed = -0.35;
            } else */if (turnRelevant || timeTillHeadingLock.milliseconds() < headingLockMS){
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

            robot.setMotorSpeeds(unscaledMotorPowers);

            telemetry.addLine()
                    .addData("Current tick", totalElapsedTime.milliseconds());
            telemetry.addLine().addData("Ztype seeking?", zType.seekingPosition);
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

    private void setLiftSeekPosition(int increment) {
        telemetry.log().add("Adding an increment of " + Integer.toString(increment));
        int currentPosition = -lift.encoderOffset;

        // Fix this
        if (robot.lift.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
            // If we're in override mode, use the current position
            currentPosition += robot.lift.getCurrentPosition();
        } else if (lift.lockPos == robot.lift.getTargetPosition()){
            // If we're locked to a position
            currentPosition += lift.lockPos;
        } else {
            // If we're normally moving up or down
            currentPosition += robot.lift.getCurrentPosition();
        }

        telemetry.log().add("Current position is " + Integer.toString(currentPosition));

        int currentIndex = getClosestTargetIndex(liftPresetPositions, currentPosition);

        telemetry.log().add("Current index is " + Integer.toString(currentIndex));

        int desiredIndex = currentIndex + increment;
        if (desiredIndex >= 0 && desiredIndex < liftPresetPositions.length) {
            telemetry.log().add("Seeking desired position " + Integer.toString(liftPresetPositions[desiredIndex]));
            lift.lockPos = liftPresetPositions[desiredIndex] + lift.encoderOffset;
        } else {
            telemetry.log().add("Seeking current position " + Integer.toString(liftPresetPositions[currentIndex]));
            lift.lockPos = liftPresetPositions[currentIndex] + lift.encoderOffset;
        }
        lift.timer = new ElapsedTime(1000);
        telemetry.update();
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
