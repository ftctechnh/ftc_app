package org.firstinspires.ftc.teamcode.Shane;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/**
 * Created by team on 7/19/2017.
 */

@TeleOp(name = "Shane's TeleOp", group = "TeleOp")
public class MyTestCodeTeleOp extends MyTestCodeTelemetry {

    private int padCofig = 0;
    private boolean ifHold;
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
        liftPower = gamepad2.right_trigger/2 - gamepad2.left_trigger/2;
        liftPower += gamepad1.right_trigger - gamepad1.left_trigger;
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
        if (gamepad2.left_bumper) {
            armPower = 1;
        } else if (gamepad2.right_bumper) {
            armPower = -1;
        } else {
            armPower = 0;
        }
        if (gamepad2.x) {
            rightHandPosition = OPEN_RIGHT_HAND;
            leftHandPosition = OPEN_LEFT_HAND;
        }
        if (gamepad2.y) {
            rightHandPosition = CLOSED_RIGHT_HAND;
            leftHandPosition = CLOSED_LEFT_HAND;
        }
        if (gamepad2.dpad_up) {
            oneHandPosition = CLOSED_ONE_HAND;
        } else if (gamepad2.dpad_down) {
            oneHandPosition = OPEN_ONE_HAND;
        }
        if (slowDrive) {
            rightPower /= 2;
            leftPower /= 2;
        }
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
    protected void myTelemetry() {
        //motors
        telemetry.addData("Right Motor Power", rightPower);
        telemetry.addData("Left Motor Power", leftPower);
        telemetry.addData("Lift Motor Power", liftPower);
        telemetry.addData("Arm Motor Power", armPower);
        //servos
        telemetry.addData("Ball Pusher Position", ballPusherPosition);
        telemetry.addData("Right Hand Position", rightHandPosition);
        telemetry.addData("Left Hand Position", leftHandPosition);
        telemetry.addData("One Hand Position", oneHandPosition);
        telemetry.addData("CR Hand Position", crHandPosition);
    }
}
