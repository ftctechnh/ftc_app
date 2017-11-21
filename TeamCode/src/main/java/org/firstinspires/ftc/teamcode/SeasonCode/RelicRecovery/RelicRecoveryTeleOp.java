package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.DriveTrain.LeftDrive;
import org.firstinspires.ftc.teamcode.Components.DriveTrain.TankDrive;
import org.firstinspires.ftc.teamcode.Components.DriveTrain.TurnDrive;
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
    private boolean leftStick = true;
    private boolean rightStick = true;
    private boolean x = true;
    private boolean defaultDrive = true;
    // ----------------------- Public Methods -----------------------
    // ------------------ Init ------------------
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
    // ------------------ Loop ------------------
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
    private void padControls() {
        gamepad1Controls();
        gamepad2Controls();
        armPower = gamepad2.right_trigger - gamepad2.left_trigger;
        liftPower = gamepad1.right_trigger - gamepad1.left_trigger;
        liftPower -= gamepad2.right_stick_y;
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            ifHold = true;
        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
            ifHold = false;
            crHandPosition = OPEN_CR_HAND;
        }
        if (ifHold) {
            crHandPosition = CLOSED_CR_HAND;
        } else {
            if (gamepad1.dpad_down || gamepad2.dpad_down) {
                crHandPosition = OPEN_CR_HAND;
            } else {
                crHandPosition = STOPPED_CR_HAND;
            }

        }
        if (gamepad2.dpad_right) {
            RelicHold = true;
            oneHandPosition = OPEN_ONE_HAND;
        } else if (gamepad2.dpad_left) {
            RelicHold = false;
            relicPosition = OPEN_RELIC;
            oneHandPosition = CLOSED_ONE_HAND;
        }
        if (RelicHold) {
            relicPosition = CLOSED_RELIC;
        } else {
            if (gamepad2.dpad_left) {
                relicPosition = OPEN_RELIC;
            } else {
                relicPosition = STOPPED_RELIC;
            }
        }

        if (gamepad2.right_bumper) {
            armLifterPwr = UP_ARM_LIFTER;
        } else if (gamepad2.left_bumper) {
            armLifterPwr = DOWN_ARM_LIFTER;
        } else {
            armLifterPwr = STOPPED_ARM_LIFTER;
        }
        if (gamepad2.right_bumper) {
            armLifterSPosition = UP_ARM_LIFTER_S;
        } else if (gamepad2.left_bumper) {
            armLifterSPosition = DOWN_ARM_LIFTER_S;
        } else {
            armLifterSPosition = STOPPED_ARM_LIFTER_S;
        }
        if (gamepad2.x) {
            armPosition = ARM_OUT;
        }
        if (gamepad2.y) {
            armPosition = ARM_IN;
        }
        armPosition = gamepad2.right_trigger;
    }

    private void gamepad1Controls() {
        if (gamepad1.left_stick_button) {
            if (leftStick) {
                defaultDrive = !defaultDrive;
                leftStick = false;
            }
        } else {
            leftStick = true;
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
                TurnDrive turn = new TurnDrive();
                drivePower = turn.drive(gamepad1);
                driveMode = "Turn Drive";
            }
            if (driveConfig == 1) {
                TankDrive tank = new TankDrive();
                drivePower = tank.drive(gamepad1);
                driveMode = "Tank Drive";
            }
        } else {

        }
        rightPower = drivePower[0];
        leftPower = drivePower[1];
        if (gamepad1.right_stick_button) {
            if (rightStick) {
                slowDrive = !slowDrive;
                rightStick = false;
            }
        } else {
            rightStick = true;
        }
        if (slowDrive) {
            rightPower /= 2;
            leftPower /= 2;
        }
        // -------------------- Jewels --------------------
        if (gamepad1.x) {
            if (x) {
                if (ballPusherPosition == BALL_PUSHER_UP) {
                    ballPusherPosition = BALL_PUSHER_DOWN;
                } else {
                    ballPusherPosition = BALL_PUSHER_UP;
                }
                x = false;
            } else {
                x = true;
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

    private void gamepad2Controls() {

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

