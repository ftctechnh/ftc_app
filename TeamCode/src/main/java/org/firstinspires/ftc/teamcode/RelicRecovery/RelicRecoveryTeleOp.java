package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveTrain.TankDrive;
import org.firstinspires.ftc.teamcode.DriveTrain.TurnDrive;
import org.firstinspires.ftc.teamcode.Utilities.SetRobot;

/**
 * Created by Shane on 13-11-2017.
 */
@TeleOp(name = "Relic Recovery",group = "TeleOp")
public class RelicRecoveryTeleOp extends RelicRecoveryHardware {

    private int padCofig = 0;
    private boolean ifHold;
    private boolean RelicHold;
    private boolean slowDrive = false;

    @Override
    public void init() {
        super.init();
        driveMode = "Tank Drive Init";
        driveMode();
        slowDrive();
        ifHold();
        ifHold = false;
    }

    @Override
    public void loop() {
        super.loop();
        driveMode();
        slowDrive();
        ifHold();
        configTele();
        padControls();
        setMotorPower();
    }   //end loop

    private void padControls() {
        if(gamepad1.right_bumper) {
            ballPusherPosition = BALL_PUSHER_DOWN;
        } else if (gamepad1.left_bumper){
            ballPusherPosition = BALL_PUSHER_UP;
        }
        if (gamepad1.dpad_right) {
            slowDrive = true;
        } else if (gamepad1.dpad_left) {
            slowDrive = false;
        }
        if (gamepad1.x) {
            padCofig = 0;
        }
        if (gamepad1.y) {
            padCofig = 1;
        }
        double drivePower[] = new double[2];
        if (padCofig == 0) {
            TankDrive tank = new TankDrive();
            drivePower = tank.drive(gamepad1);
            driveMode = "Tank Drive";
        }
        if (padCofig == 1) {
            TurnDrive turn = new TurnDrive();
            drivePower = turn.drive(gamepad1);
            driveMode = "Turn Drive";
        }
        rightPower = drivePower[0];
        leftPower = drivePower[1];
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
        if (slowDrive) {
            rightPower /= 2;
            leftPower /= 2;
        }
        armPosition = gamepad2.right_trigger;
        if(gamepad2.right_stick_button) {
            poopPosition = POOP_OPEN;
        } else {
            poopPosition = POOP_CLOSED;
        }
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
        // ------- Continuous Rotation Servos -------
        setRobot.position(crHand,crHandPosition,"hand crservo");
        setRobot.position(crRelicGrabber,relicPosition, "relic grabber crservo");
        setRobot.position(crArmLift,armLifterSPosition,"arm lift crservo");
        setRobot.position(crArm,armPower,"arm crservo");
    }

    private void configTele() {
        telemetry.addData("gamepad Configuration", padCofig);
    }
    private void driveMode() {
        telemetry.addData("Drive Mode", driveMode);
    }
    private void slowDrive() {
        telemetry.addData("If Slow Drive", slowDrive);
    }
    private void ifHold() {
        telemetry.addData("If Hold", ifHold);
    }
}

