package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Metheds.DriveTrainMethods.TankDriveMethods;
import org.firstinspires.ftc.teamcode.Metheds.DriveTrainMethods.TurnDriveMethods;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by Shane on 13-11-2017.
 */
@TeleOp(name = "Relic Recovery TeleOp",group = "TeleOp")
public class RelicRecoveryTeleOp extends RelicRecoveryHardware {
    // --------------------- Private Variables ----------------------
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
        super.init();
        driveMode = "Turn Drive Init";
        ifDriveOverride();
        driveMode();
        slowDrive();
        ifHold();
        ifHold = false;
    }
    // ----------------------- Loop -----------------------
    @Override
    public void loop() {
        super.loop();
        ifDriveOverride();
        driveMode();
        slowDrive();
        ifHold();
        padControls();
        setMotorPower();
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
            crHandPosition = OPEN_CR_HAND;
        }
        if (ifHold) {
            crHandPosition = CLOSED_CR_HAND;
        } else {
            if (gamepad1.dpad_left || gamepad2.dpad_left) {
                crHandPosition = OPEN_CR_HAND;
            } else {
                crHandPosition = STOPPED_CR_HAND;
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
            liftPower = 0;
        } else { // for gamepad 1 controls override
            // ---- relic override ----
            if (gamepad1.y) {
                if (y1) {
                    if (oneHandPosition == OPEN_RELIC) {
                        oneHandPosition = CLOSED_RELIC;
                    } else {
                        oneHandPosition = OPEN_RELIC;
                    }
                    y1 = false;
                } else {
                    y1 = true;
                }
            }
            // ---- lift override -----
            if (gamepad1.dpad_up) {
                liftPower = 1;
            } else if (gamepad1.dpad_down) {
                liftPower = -1;
            } else {
                liftPower = 0;
            }
            // ----- arm override -----
            if (gamepad1.right_bumper) {
                armPosition += .02;
            } else if (gamepad1.left_bumper) {
                armPosition -=.02;
            }
            armLifterPwr = gamepad1.right_trigger - gamepad1.left_trigger;
        }
        rightPower = drivePower[0];
        leftPower = drivePower[1];
        if (gamepad1.right_stick_button) {
            if (rightStick1) {
                slowDrive = !slowDrive;
                rightStick1 = false;
            }
        } else {
            rightStick1 = true;
        }
        if (slowDrive) {
            rightPower /= 2;
            leftPower /= 2;
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
                if (ballPusherPosition == BALL_PUSHER_UP) {
                    ballPusherPosition = BALL_PUSHER_DOWN;
                } else {
                    ballPusherPosition = BALL_PUSHER_UP;
                }
            }
        }

        if (gamepad1.b) {
            ballRotatorPosition = BALL_ROTATE_RIGHT;
        } else if (gamepad1.a) {
            ballRotatorPosition = BALL_ROTATE_LEFT;
        } else {
            ballRotatorPosition = BALL_ROTATE_CENTER;
        }
    }
    // ---------------------- Pad 2 -----------------------
    private void gamepad2Controls() {
        // ---------------- Lift ----------------
        liftPower -= gamepad2.right_stick_y;
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
                if (oneHandPosition == OPEN_ONE_HAND) {
                    oneHandPosition = CLOSED_ONE_HAND;
                } else {
                    oneHandPosition = OPEN_ONE_HAND;
                }
            }
        }
        // ---------------- Arm -----------------
        if (gamepad2.right_bumper) {
            armPosition += .0008;
        } else if (gamepad2.left_bumper) {
            armPosition -=.0008;
            if (armPosition < 0)
                armPosition = 0;
        }
        armLifterPwr = gamepad2.right_trigger - gamepad2.left_trigger;
    }

    private void setMotorPower() {
        SetRobot setRobot = new SetRobot(telemetry);
        // -------------- DcMotors --------------
        setRobot.power(mRight,rightPower,"right motor");
        setRobot.power(mLeft,leftPower,"left motor");
        setRobot.power(mLift,liftPower,"lift motor");
        setRobot.power(mArm,armPower,"arm motor");
        setRobot.power(mArmLift,armLifterPwr,"arm lift motor");
        // ---------- Standard Servos -----------
        setRobot.position(ssBallPusher,ballPusherPosition,"ball pusher servo");
        setRobot.position(ssArm,armPosition,"arm servo");
        setRobot.position(ssRelicGrabber,oneHandPosition,"relic grabber servo");
        setRobot.position(ssPoop,poopPosition,"poop");
        setRobot.position(ssBallRotator, ballRotatorPosition,"ball rototor sero");
        // ----- Continuous Rotation Servos -----
        setRobot.position(crHand,crHandPosition,"hand crservo");
        setRobot.position(crRelicGrabber,relicPosition, "relic grabber crservo");
        setRobot.position(crArmLift,armLifterSPosition,"arm lift crservo");
        setRobot.position(crArm,armPower,"arm crservo");
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

