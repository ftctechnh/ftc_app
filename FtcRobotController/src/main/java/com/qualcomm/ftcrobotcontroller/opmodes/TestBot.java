package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;


/**
 * Created by subash on 10/11/15.
 */
public class TestBot extends OpMode
{
    private static final float DEADZONE = 0.1f;
    DcMotor rightMotor;
    DcMotor leftMotor;
    DcMotor grabber;
    LightSensor beaconColor;

    public void init()
    {
        beaconColor = hardwareMap.lightSensor.get("beaconColor");
        gamepad1.setJoystickDeadzone(DEADZONE);
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
       // rightMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //leftMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        grabber = hardwareMap.dcMotor.get("grabber");
        grabber.setDirection(DcMotor.Direction.FORWARD);
       //grabber.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

    }

    public void loop()
    {
        leftMotor.setPower(gamepad1.left_stick_y);
        rightMotor.setPower(gamepad1.right_stick_y);
        if(gamepad1.right_bumper)
        {
            grabber.setPower(1.0f);
        }
        else if(gamepad1.left_bumper)
        {
            grabber.setPower(-1.0f);
        }
        else
        {
            grabber.setPower(0.0f);
        }
        telemetry.addData("leftMotor", leftMotor.getPower());
        telemetry.addData("rightMotor", rightMotor.getPower());
        telemetry.addData("beaconColor", beaconColor.getLightDetected());

    }

    public void stop()
    {
        rightMotor.setPower(0.0f);
        leftMotor.setPower(0.0f);
        grabber.setPower(0.0f);
    }

}
