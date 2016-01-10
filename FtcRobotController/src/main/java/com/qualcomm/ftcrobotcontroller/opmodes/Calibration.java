package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.io.BufferedReader;
import java.io.FileOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jerry on 10/10/2015.
 */


public class Calibration extends OpMode {

    OpticalDistanceSensor opticalDistanceSensor;

    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;

    double redValue;
    double blueValue;
    double whiteValue;
    double matValue;


    //linear slide tilt motor

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void init() {

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("light");
        frontLeftMotor = hardwareMap.dcMotor.get("frontL");
        frontRightMotor = hardwareMap.dcMotor.get("frontR");
        backLeftMotor = hardwareMap.dcMotor.get("backL");
        backRightMotor = hardwareMap.dcMotor.get("backR");
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    public void loop() {
        float throttle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;
        float secondThrottle = gamepad2.left_stick_y;
        float secondRightThrottle = gamepad2.right_stick_y;

        if (Math.abs(throttle) < 0.01 && Math.abs(rightThrottle) < 0.01) {
            frontRightMotor.setPower(secondRightThrottle);
            backRightMotor.setPower(secondRightThrottle);
            frontLeftMotor.setPower(secondThrottle);
            backLeftMotor.setPower(secondThrottle);
        } else {
            frontRightMotor.setPower(rightThrottle);
            backRightMotor.setPower(rightThrottle);
            frontLeftMotor.setPower(throttle);
            backLeftMotor.setPower(throttle);
        }
        telemetry.addData("blueValue", blueValue);
        telemetry.addData("redValue", redValue);
        telemetry.addData("whiteValue", whiteValue);
        telemetry.addData("matValue", matValue);

        if (gamepad1.x) { //Blue
            blueValue = opticalDistanceSensor.getLightDetected();
        }
        if (gamepad1.b) { //Red
            redValue = opticalDistanceSensor.getLightDetected();
        }
        if (gamepad1.y) { //White
            whiteValue = opticalDistanceSensor.getLightDetected();
        }
        if (gamepad1.a) { //Black
            matValue = opticalDistanceSensor.getLightDetected();
        }
    }

    public void stop(){
        if (redValue >  0 || blueValue > 0 || whiteValue > 0 || matValue > 0) {
            try {
                File file = new File("/sdcard/FIRST/calibration.txt");
                FileOutputStream fileoutput = new FileOutputStream(file);
                PrintStream ps = new PrintStream(fileoutput);
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
                ps.println(date);
                ps.println(redValue);
                ps.println(blueValue);
                ps.println(whiteValue);
                ps.println(matValue);

                ps.close();
                fileoutput.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
