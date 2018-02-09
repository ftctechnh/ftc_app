package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/14/2017.
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class REVGyro {
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;
    private boolean isInitialized = false;
    public double heading;
    public REVGyro (BNO055IMU imu) {
        this.parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        this.imu = imu;
    }
    private void init() {isInitialized = imu.initialize(parameters);}
    public void calibrate() {
        ElapsedTime time = new ElapsedTime();
        time.reset();
        while(!isInitialized) {
            if(time.seconds()>10) {
                break;
            }
            init();
        }
        heading = getHeading();
    }

    public double getHeading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }
}