package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Timer;
import java.util.TimerTask;

import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;
import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;

public class MainTeleOp extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
    RampingController rampController;

    final double turnVolatility = 2; // Higher number makes turning more jerklike, but faster

    final double moveMotorThreshold = 0;
    final double triggerThreshold = 0.10;
    final double minSlowModePower = 0.45;
    final int headingLockMS = 1000;

    final int[] liftPresetPositions = new int[]{0, 640, 1280};
    final double[] trayFlipperLeftPositions = new double[] {0.25, 0.65, 0.55};
    final double[] trayFlipperRightPositions = new double[] {0.75, 0.35, 0.45};
    final double[] trayTailPositions = new double[] {0, 0.75};

    double initialHeading;
    double desiredHeading;
    double difference;

    double turnSpeed;
    double desiredMax;
    double heading;

    boolean wasGP1LeftBumperPressed;
    boolean wasGP1LeftTriggerPressed;
    boolean wasGP1APressed;
    boolean wasGP1BPressed;
    boolean wasGP1YPressed;

    boolean wasGP1RightBumperPressed;
    boolean wasGP1RightTriggerPressed;

    boolean nonrelativeDriveModeEnabled;
    boolean slowMode;
    int accelTime;

    boolean intakeUp;
    boolean flipperOut;
    boolean spinningIntake;
    boolean reversedIntake;

    ElapsedTime timeTillHeadingLock;
    ElapsedTime totalElapsedTime;

    ConstrainedPIDMotor lift;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        lift = new ConstrainedPIDMotor(robot.lift, 100, 1.0, 0.5, 0, -2500, telemetry, false);

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
        desiredMax = 1.0;

        timeTillHeadingLock = new ElapsedTime();

        totalElapsedTime = new ElapsedTime();

        while (opModeIsActive()) {
            robot.updateReadings();

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

            // Now intake work
            if (gamepad1.left_bumper && !wasGP1LeftBumperPressed) {
                spinningIntake = !spinningIntake;
                wasGP1LeftBumperPressed = true;
                updateIntakePower();
            }

            // Flipper servos
            if (gamepad1.left_trigger > triggerThreshold && !wasGP1LeftTriggerPressed) {
                wasGP1LeftTriggerPressed = true;
                intakeUp = !intakeUp;
                updateTray();
            }

            if (gamepad1.right_bumper) {
                robot.trayFlipperLeft.setPosition(trayFlipperLeftPositions[0] +
                        (trayFlipperLeftPositions[1] - trayFlipperLeftPositions[0])/2.0);
                robot.trayFlipperRight.setPosition(trayFlipperRightPositions[0] +
                        (trayFlipperRightPositions[1] - trayFlipperRightPositions[0])/2.0);
            }

            // Jostle blocks
            if (gamepad1.a && !wasGP1APressed) {
                robot.trayFlipperLeft.setPosition(robot.trayFlipperLeft.getPosition() - 0.15);
                robot.trayFlipperRight.setPosition(robot.trayFlipperRight.getPosition() + 0.15);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        robot.trayFlipperLeft.setPosition(trayFlipperLeftPositions[1]);
                        robot.trayFlipperRight.setPosition(trayFlipperRightPositions[1]);
                    }
                }, 300);

                wasGP1APressed = true;
            }

            if (gamepad1.y && !wasGP1YPressed) {
                flipperOut = !flipperOut;
                wasGP1YPressed = true;
                int index = flipperOut ? 0 : 1;
                robot.trayTail.setPosition(trayTailPositions[index]);
            }

            if (gamepad1.b && !wasGP1BPressed) {
                wasGP1BPressed = true;
                intakeUp = false;
                robot.trayFlipperLeft.setPosition(trayFlipperLeftPositions[2]);
                robot.trayFlipperRight.setPosition(trayFlipperRightPositions[2]);
            }

            // Lift
            if (gamepad1.dpad_up) {
                lift.setDirection(ConstrainedPIDMotor.Direction.FORWARD);
            } else if (gamepad1.dpad_down) {
                lift.setDirection(ConstrainedPIDMotor.Direction.BACKWARD);
            } /*else if (gamepad1.right_bumper && !wasGP1RightBumperPressed) {
                setLiftSeekPosition(1);
                wasGP1RightBumperPressed = true;
            } else if (gamepad1.right_trigger > triggerThreshold && !wasGP1RightTriggerPressed) {
                wasGP1RightTriggerPressed = true;
                setLiftSeekPosition(-1);
            } */else {
                lift.setDirection(ConstrainedPIDMotor.Direction.HOLD, false);
            }

            if (gamepad1.right_bumper && !wasGP1RightBumperPressed) {
                wasGP1RightBumperPressed = true;
                slowMode = !slowMode;
                if (slowMode) {
                    robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
                } else {
                    robot.setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
            }

            if (gamepad1.right_trigger > triggerThreshold && !wasGP1RightTriggerPressed) {
                reversedIntake = true;
                wasGP1RightTriggerPressed = true;
                updateIntakePower();
            } else if (gamepad1.right_trigger < triggerThreshold && wasGP1RightTriggerPressed) {
                reversedIntake = false;
                wasGP1RightTriggerPressed = false;
                updateIntakePower();
            }

            if (!gamepad1.right_bumper) {wasGP1RightBumperPressed = false;}
            //if (!(gamepad1.right_trigger > triggerThreshold)) {wasGP1RightTriggerPressed = false;}
            if (!gamepad1.left_bumper) {wasGP1LeftBumperPressed = false;}
            if (!(gamepad1.left_trigger > triggerThreshold)) {wasGP1LeftTriggerPressed = false;}
            if (!gamepad1.a) {wasGP1APressed = false;}
            if (!gamepad1.b) {wasGP1BPressed = false;}
            if (!gamepad1.y) {wasGP1YPressed = false;}


        }
        rampController.quit();

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

    private void setLiftSeekPosition(int increment) {
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

        int currentIndex = getClosestTargetIndex(liftPresetPositions, currentPosition);

        int desiredIndex = currentIndex + increment;
        if (desiredIndex >= 0 && desiredIndex < liftPresetPositions.length) {
            lift.lockPos = liftPresetPositions[desiredIndex] + lift.encoderOffset;
        } else {
            lift.lockPos = liftPresetPositions[currentIndex] + lift.encoderOffset;
        }
        lift.timer = new ElapsedTime(1000);
    }
    int invert(int i) {return i == 1 ? 0 : 1; }

    private void updateTray() {
        int index = intakeUp ? 0 : 1;
        robot.trayFlipperLeft.setPosition(trayFlipperLeftPositions[index]);
        robot.trayFlipperRight.setPosition(trayFlipperRightPositions[index]);
    }

    private void updateIntakePower() {
        double speed = spinningIntake ? (reversedIntake ? -1 : 1) : 0;
        robot.intakeLeft.setPower(speed);
        robot.intakeRight.setPower(speed);
    }
}
