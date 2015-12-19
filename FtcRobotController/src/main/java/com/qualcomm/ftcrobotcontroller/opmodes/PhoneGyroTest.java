package com.qualcomm.ftcrobotcontroller.opmodes;

import android.provider.ContactsContract;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by subash on 11/27/2015.
 */
public class PhoneGyroTest extends OpMode {
    PhoneGyrometer gyro;
    // DcMotor right;


    public void init() {
        gyro = new PhoneGyrometer(hardwareMap);
        // right = hardwareMap.dcMotor.get("rightMotor");
    }

    public void loop() {
        telemetry.addData("Azimuth = ", gyro.getAzimuth());
        telemetry.addData("Pitch = ", gyro.getPitch());
        telemetry.addData("Roll = ", gyro.getRoll());

        //right.setPower(1.0f);
    }

    public void stop() {
        gyro.onDestroy();
    }

}
