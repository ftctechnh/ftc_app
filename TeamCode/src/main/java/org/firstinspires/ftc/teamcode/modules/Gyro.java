package org.firstinspires.ftc.teamcode.modules;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by billcipher1344 on 10/18/2016.
 */
public final class Gyro {

    public static void rotate(double degrees, BNO055IMU imu, DcMotor[] leftMotors, DcMotor[] rightMotors){

        for(DcMotor motor : leftMotors){
            motor.setPower(1.00);
        }
        for(DcMotor motor : rightMotors){
            motor.setPower(-1.00);
        }

        Orientation orientation = imu.getAngularOrientation();

        double initialHeading = orientation.firstAngle;
        double targetHeading = initialHeading + degrees;
    }
}
