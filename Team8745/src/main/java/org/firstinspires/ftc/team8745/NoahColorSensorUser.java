package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Noah on 10/29/2016.
 */

@TeleOp(name="ColorTest")
@Disabled
public class NoahColorSensorUser extends OpMode {
    DcMotor left;
    DcMotor right;
    Servo front;
    Servo back;
    TouchSensor touchSensor1;
    LightSensor lightSensor1;
    ColorSensor cory;
    Servo beaconBumper;
    public void init() {
        left = hardwareMap.dcMotor.get("motor-left");
        right = hardwareMap.dcMotor.get("motor-right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        front = hardwareMap.servo.get("servo-front");
        back = hardwareMap.servo.get("servo-back");
        touchSensor1 = hardwareMap.touchSensor.get("sensor-Touch Sensor");
        lightSensor1 = hardwareMap.opticalDistanceSensor.get("sensor-ODS");
        cory = hardwareMap.colorSensor.get("sensor-Color Sensor");
        beaconBumper = hardwareMap.servo.get("servo-beacon");
    }

    @Override
    public void loop() {
        float leftDC = gamepad1.left_stick_y;
        float rightDC = gamepad1.right_stick_y;
        float rightInput = gamepad1.right_trigger;
        float leftInput = gamepad1.left_trigger;


        front.setPosition(rightInput);

        //if(leftDCy > 0
        back.setPosition(lightSensor1.getLightDetected());
        if(touchSensor1.isPressed()) {
            left.setPower(0);
            right.setPower(0);}

        else {left.setPower(leftDC);
            left.setPower(leftDC);
            right.setPower(rightDC);}
        beaconBumper.setPosition(leftInput);
        telemetry.addData("beaconBumper",beaconBumper.getPosition());
        telemetry.addData("blue",cory.blue());
        telemetry.addData("red",cory.red());
        telemetry.addData("alpha",cory.alpha());
        telemetry.addData("green",cory.green());
        telemetry.update();


    }
}


