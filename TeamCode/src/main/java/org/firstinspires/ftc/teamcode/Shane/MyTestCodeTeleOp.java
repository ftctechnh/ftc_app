package org.firstinspires.ftc.teamcode.Shane;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by team on 7/19/2017.
 */

@TeleOp(name = "Stable TeleOp", group = "TeleOp")
public class MyTestCodeTeleOp extends MyTestCodeTelemetry {

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
        super.myTelemetry();
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
            rightHandPosition = OPEN_RIGHT_HAND;
            leftHandPosition = OPEN_LEFT_HAND;
            //oneHandPosition = OPEN_ONE_HAND;
            armPosition = ARM_OUT;
        }
        if (gamepad2.y) {
            rightHandPosition = CLOSED_RIGHT_HAND;
            leftHandPosition = CLOSED_LEFT_HAND;
            //oneHandPosition = CLOSED_ONE_HAND;
            armPosition = ARM_IN;
        }
        /*if (gamepad2.dpad_up) {
            oneHandPosition = CLOSED_ONE_HAND;
        } else if (gamepad2.dpad_down) {
            oneHandPosition = OPEN_ONE_HAND;
        }*/
        if (slowDrive) {
            rightPower /= 2;
            leftPower /= 2;
        }
        armPosition = gamepad2.right_trigger;
    }

    private void setMotorPower() {
        //motors
        try {
            rightMotor.setPower(rightPower);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "right motor");
        }
        try {
            leftMotor.setPower(leftPower);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "left motor");
        }
        try {
            liftMotor.setPower(liftPower);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "lift motor");
        }
        try {
            armMotor.setPower(armPower);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "arm motor");
        }
        try {
            armLifter.setPower(armLifterPwr);
        } catch (Exception OpModeException) {
            telemetry.addData("Cant run (not mapped)", "arm lifter");
        }
        //servos
        try {
            ballPusher.setPosition(ballPusherPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "ball pusher");
        }
        try {
            rightHand.setPosition(rightHandPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "right hand");
        }
        try {
            leftHand.setPosition(leftHandPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "left hand");
        }
        try {
            oneHand.setPosition(oneHandPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "one hand");
        }
        try {
            crHand.setPower(crHandPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "cr hand");
        }
        try {
            relic.setPower(relicPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Cant run (not mapped)", "relic grabber");

        }
        try {
            armLifterS.setPower(armLifterSPosition);
        }catch (Exception opModeException){
            telemetry.addData("Cant run (not mapped)", "Servo lift arm");
        }
        try {
            armServo.setPower(armPower);
        } catch (Exception opModeException) {
            telemetry.addData("Can't run (not mapped)", "Servo arm motor");
        }
        try {
            arm.setPosition(armPosition);
        } catch (Exception opModeException) {
            telemetry.addData("Can't run (not mapped)", "Servo arm");
        }
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
