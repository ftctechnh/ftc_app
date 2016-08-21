package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by subash on 12/19/2015.
 */
public class CompBotTeleopV2 extends OpMode {
    CompBot compBot;
    float speed = 0.65f;



    @Override
    public void stop() {
        compBot.kill();
    }

    @Override
    public void init() {
        compBot = new CompBot(hardwareMap);
        gamepad1.setJoystickDeadzone(0.01f);
        gamepad2.setJoystickDeadzone(0.01f);
    }

    @Override
    public void loop() {
        compBot.getLeftMotor().setPower(-gamepad1.left_stick_y);
        compBot.getRightMotor().setPower(-gamepad1.right_stick_y);
        if (gamepad2.dpad_right) {
            compBot.getDiverterServo().setPosition(1.0f);
        } else if (gamepad2.dpad_left) {
            compBot.getDiverterServo().setPosition(0.0f);
        } else {
            compBot.getDiverterServo().setPosition(0.5f);
        }

       /* if (gamepad2.dpad_up) {
            compBot.getTapeMeasureServo().setPosition(1.0f);
        } else if (gamepad2.dpad_down) {
            compBot.getTapeMeasureServo().setPosition(0.0f);
        } else {
            compBot.getTapeMeasureServo().setPosition(0.5f);
        }*/

        if (gamepad2.a) {
            compBot.getGrabberMotor().setPower(-1.0f);
        } else if (gamepad2.b) {
            compBot.getGrabberMotor().setPower(1.0f);
        }
        else
        {
            compBot.getGrabberMotor().setPower(0.0f);
        }

        if (gamepad2.x) {
            compBot.getClimberReleaseServoRight().setPosition(1.0f);
            compBot.getClimberReleaseServoLeft().setPosition(0.0f);
        } else if (gamepad2.y)
        {
            compBot.getClimberReleaseServoRight().setPosition(0.4f);
            compBot.getClimberReleaseServoLeft().setPosition(0.6f);
        }

        if (gamepad2.left_trigger > 0.5f) {
            compBot.getWinchMotor().setPower(1.0f);
        } else if (gamepad2.left_bumper) {
            compBot.getWinchMotor().setPower(-1.0f);
        } else {
            compBot.getWinchMotor().setPower(0.0f);
        }

        if(gamepad2.right_trigger > 0.5f)
        {
            compBot.getDispenserServo().setPosition(1.0f);
        }
        else
        {
            compBot.getDispenserServo().setPosition(0.5f);
        }

        compBot.checkAndFlipDispenserServo(gamepad2.right_bumper);



            if (gamepad1.a) {
                telemetry.addData("a button pressed", 0);
                compBot.getFrontRightMotor().setPower(-0.2f);
                compBot.getFrontLeftMotor().setPower(-0.2f);
                compBot.getBackLeftMotor().setPower(-0.2f);
                compBot.getBackRightMotor().setPower(-0.2f);
            }
            else
            {
                    if (gamepad1.right_bumper) {
                        compBot.getFrontRightMotor().setPower(speed);
                        compBot.getFrontLeftMotor().setPower(speed);
                    } else if (gamepad1.right_trigger > 0.05f) {
                        compBot.getFrontRightMotor().setPower(-speed);
                        compBot.getFrontLeftMotor().setPower(-speed);
                    } else {
                        compBot.getFrontRightMotor().setPower(0.0f);
                        compBot.getFrontLeftMotor().setPower(0.0f);
                    }
                    if (gamepad1.left_bumper) {
                        compBot.getBackLeftMotor().setPower(-speed);
                        compBot.getBackRightMotor().setPower(-speed);
                    } else if (gamepad1.left_trigger > 0.05f) {
                        compBot.getBackLeftMotor().setPower(speed);
                        compBot.getBackRightMotor().setPower(speed);
                    } else {
                        compBot.getBackLeftMotor().setPower(0.0f);
                        compBot.getBackRightMotor().setPower(0.0f);
                    }
                }
    }




}
