package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryBeta;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Components.DriveTrain.TankDriveMethods;
import org.firstinspires.ftc.teamcode.Components.DriveTrain.TurnDriveMethods;

import static org.firstinspires.ftc.teamcode.SeasonCode.RelicRecoveryBeta.ServoPositions.*;

/**
 * Created by Shane on 13-11-2017.
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Relic Recovery TeleOp Beta",group = "TeleOp")
public class TeleOp extends OpMode {
    // --------------------- Private Variables ----------------------
    private RobotHardware robot;
    private String driveMode;
    private int driveConfig = 0;
    private boolean ifHold;
    private boolean RelicHold;
    private boolean slowDrive = false;
    private boolean leftStick1 = true;
    private boolean rightStick1 = true;
    private boolean x1 = true;
    private boolean y1 = true;
    private boolean defaultDrive = true;

    private boolean isPad1XPressed;
    private boolean isPad1XReleased;
    private boolean isPad2YPressed;
    private boolean isPad2YReleased;

    // ----------------------- Public Methods -----------------------
    // ----------------------- Init -----------------------
    @Override
    public void init() {
        robot = new RobotHardware(hardwareMap,telemetry);
        robot.init();
        driveMode = "Turn Drive Init";
        ifHold = false;
        tele();
    }
    // ----------------------- Loop -----------------------
    @Override
    public void loop() {
        padControls();
        tele();
        robot.setHardwarePower();
    }
    // ---------------------- Private Methods -----------------------
    // ----------------------- Pads -----------------------
    private void padControls() {
        gamepad1Controls();
        gamepad2Controls();
        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            ifHold = true;
        } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
            ifHold = false;
            robot.crHandPosition = HAND_OPEN;
        }
        if (ifHold) {
            robot.crHandPosition = HAND_CLOSED;
        } else {
            if (gamepad1.dpad_left || gamepad2.dpad_left) {
                robot.crHandPosition = HAND_OPEN;
            } else {
                robot.crHandPosition = HAND_STOPPED;
            }

        }
    }
    // ---------------------- Pad 1 -----------------------
    private void gamepad1Controls() {
        // ------------ Drive Train -------------
        if (gamepad1.left_stick_button) {
            if (leftStick1) {
                defaultDrive = !defaultDrive;
                leftStick1 = false;
            }
        } else {
            leftStick1 = true;
        }
        double drivePower[] = new double[2];
        if (defaultDrive) {
            if (gamepad1.y) {
                driveConfig++;
                if (driveConfig == 2) {
                    driveConfig = 0;
                }
            }
            if (gamepad1.x) {
                driveConfig--;
                if (driveConfig == -1) {
                    driveConfig = 1;
                }
            }
            if (driveConfig == 0) {
                TurnDriveMethods turn = new TurnDriveMethods();
                drivePower = turn.drive(gamepad1);
                driveMode = "Turn Drive";
            }
            if (driveConfig == 1) {
                TankDriveMethods tank = new TankDriveMethods();
                drivePower = tank.drive(gamepad1);
                driveMode = "Tank Drive";
            }
            robot.liftPower = 0;
        } else { // for gamepad 1 controls override
            // ---- relic override ----
            if (gamepad1.y) {
                if (y1) {
                    if (robot.grabberPosition == GRABBER_OPEN) {
                        robot.grabberPosition = GRABBER_CLOSED;
                    } else {
                        robot.grabberPosition = GRABBER_OPEN;
                    }
                    y1 = false;
                } else {
                    y1 = true;
                }
            }
            // ---- lift override -----
            if (gamepad1.dpad_up) {
                robot.liftPower = 1;
            } else if (gamepad1.dpad_down) {
                robot.liftPower = -1;
            } else {
                robot.liftPower = 0;
            }
            // ----- arm override -----
            if (gamepad1.right_bumper) {
                robot.armPosition += .02;
            } else if (gamepad1.left_bumper) {
                robot.armPosition -=.02;
            }
            robot.armLiftPower = gamepad1.right_trigger - gamepad1.left_trigger;
        }
        robot.rightPower = drivePower[0];
        robot.leftPower = drivePower[1];
        if (gamepad1.right_stick_button) {
            if (rightStick1) {
                slowDrive = !slowDrive;
                rightStick1 = false;
            }
        } else {
            rightStick1 = true;
        }
        if (slowDrive) {
            robot.rightPower /= 2;
            robot.leftPower /= 2;
        }
        // --------------- Jewels ---------------
        if (gamepad1.x) {
            isPad1XPressed = true;
            isPad1XReleased = false;
        }
        if (isPad1XPressed) {
            if (!gamepad1.x) {
                isPad1XReleased = true;
                isPad1XPressed = false;
            }
            if (isPad1XReleased) {
                if (robot.ballPusherPosition == BALL_PUSHER_UP) {
                    robot.ballPusherPosition = BALL_PUSHER_DOWN;
                } else {
                    robot.ballPusherPosition = BALL_PUSHER_UP;
                }
            }
        }

        if (gamepad1.b) {
            robot.ballRotatorPosition = BALL_ROTATOR_RIGHT;
        } else if (gamepad1.a) {
            robot.ballRotatorPosition = BALL_ROTATOR_LEFT;
        } else {
            robot.ballRotatorPosition = BALL_ROTATOR_CENTER;
        }
    }
    // ---------------------- Pad 2 -----------------------
    private void gamepad2Controls() {
        // ---------------- Lift ----------------
        robot.liftPower -= gamepad2.right_stick_y;
        // -------------- Grabber ---------------

        if (gamepad2.y) {
            isPad2YPressed = true;
            isPad2YReleased = false;
        }
        if (isPad2YPressed) {
            if (!gamepad2.y) {
                isPad2YReleased = true;
                isPad2YPressed = false;
            }
            if (isPad2YReleased) {
                if (robot.grabberPosition == HAND_OPEN) {
                    robot.grabberPosition = HAND_CLOSED;
                } else {
                    robot.grabberPosition = HAND_OPEN;
                }
            }
        }
        // ---------------- Arm -----------------
        if (gamepad2.right_bumper) {
            robot.armPosition += .0008;
        } else if (gamepad2.left_bumper) {
            robot.armPosition -=.0008;
            if (robot.armPosition < 0)
                robot.armPosition = 0;
        }
        robot.armLiftPower = gamepad2.right_trigger - gamepad2.left_trigger;
    }

    private void tele() {
        ifDriveOverride();
        driveMode();
        slowDrive();
        ifHold();
    }
    private void ifDriveOverride() {
        telemetry.addData("If default drive", defaultDrive);
    }
    private void driveMode() {
        telemetry.addData("Drive Mode", driveMode);
    }
    private void slowDrive() {
        telemetry.addData("If Slow Drive", slowDrive);
    }
    private void ifHold() {
        telemetry.addData("If Relic Held", ifHold);
    }
}

