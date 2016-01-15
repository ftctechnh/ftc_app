package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;

public class DragonoidsAuto extends DragonoidsOpMode {
    @Override
    public void init() {
        super.init();
    }

    public int getRightEncoderValue() {
        int total = 0;
        total += driveMotors.get("rightOne").getCurrentPosition();
        total += driveMotors.get("rightTwo").getCurrentPosition();
        return total / 2;
    }
    public int getLeftEncoderValue() {
        int total = 0;
        total += driveMotors.get("leftOne").getCurrentPosition();
        total += driveMotors.get("leftTwo").getCurrentPosition();
        return total / 2;
    }

    public void turn() {

    }

    @Override
    public void loop() {
        // Choose flow based on alliance color

        // Drive forward a bit

        // Use the phone's IMU to make a precise 45 degree turn

        // Drive forward to the beacon zone

        // Turn 45 degrees again

        // Drive forward to color detection distance

        // Detect color of the beacon

        // Drive forward or extend arm to push the correct button

        // Deposit climbers in the bucket behind the beacon

        // Reverse out of the beacon area (or turn 180 degrees and then drive forward)

        // Turn -45 degrees

        // Drive forward as far as possible up the mountain

        // Use the "churro grabbers" to gain more traction and hoist the robot up the
        // remaining portion of the mountain after the normal wheels begin to slip

        super.loop();
    }
}