package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by subash on 11/17/2015.
 */
public class CompBotTeleop extends OpMode
{
    PhoneGyrometer gyro;
    CompBot compBot = new CompBot(hardwareMap);


    @Override
    public void stop()
    {
        gyro.onDestroy();
        compBot.kill();
    }

    @Override
    public void init()
    {
        gyro = new PhoneGyrometer(hardwareMap);
        gamepad1.setJoystickDeadzone(0.01f);
        gamepad2.setJoystickDeadzone(0.01f);
    }

    @Override
    public void loop()
    {
        compBot.getRightMotor().setPower(gamepad1.left_stick_y);
        compBot.getLeftMotor().setPower(gamepad1.right_stick_y);
        compBot.getGrabberMotor().setPower(gamepad2.left_stick_y);
        compBot.getWinchMotor().setPower(gamepad2.right_stick_y);
        compBot.getDispenserServo().setPosition(0.5f);
        compBot.getDispenserFlipperServo().setPosition(1.0f);
        if(gamepad2.right_stick_button)
        {
            compBot.getDiverterServo().setPosition(1.0f);
        }
        else if(gamepad2.left_stick_button)
        {
            compBot.getDiverterServo().setPosition(0.0f);
        }
        else
        {
            compBot.getDiverterServo().setPosition(0.5f);
        }

        if(gamepad2.dpad_up)
        {
            compBot.getTapeMeasureServo().setPosition(1.0f);
        }
        else if(gamepad2.dpad_down)
        {
            compBot.getTapeMeasureServo().setPosition(0.0f);
        }
        else
        {
            compBot.getTapeMeasureServo().setPosition(0.5f);
        }

        if(gamepad1.a)
        {
            compBot.getFrontRightMotor().setPower(-0.2f);
            compBot.getFrontLeftMotor().setPower(-0.2f);
            compBot.getBackLeftMotor().setPower(0.2f);
            compBot.getBackRightMotor().setPower(0.2f);
        }
        else
        {

            if(gamepad1.right_bumper)
            {
                compBot.getFrontRightMotor().setPower(1.0f);
                compBot.getFrontLeftMotor().setPower(1.0f);
            }
            else if(gamepad1.right_trigger > 0.05f)
            {
                compBot.getFrontRightMotor().setPower(-1.0f);
                compBot.getFrontLeftMotor().setPower(-1.0f);
            }
            else
            {
                compBot.getFrontRightMotor().setPower(0.0f);
                compBot.getFrontLeftMotor().setPower(0.0f);
            }
            if(gamepad1.left_bumper)
            {
                compBot.getBackLeftMotor().setPower(1.0f);
                compBot.getBackRightMotor().setPower(1.0f);
            }
            else if(gamepad1.left_trigger > 0.05f)
            {
                compBot.getBackLeftMotor().setPower(-1.0f);
                compBot.getBackRightMotor().setPower(-1.0f);
            }
            else
            {
                compBot.getBackLeftMotor().setPower(0.0f);
                compBot.getBackRightMotor().setPower(0.0f);
            }
        }
        telemetry.addData("Azimuth = ", gyro.getAzimuth());
        telemetry.addData("Pitch = ", gyro.getPitch());
        telemetry.addData("Roll = ", gyro.getRoll());
    }




}
