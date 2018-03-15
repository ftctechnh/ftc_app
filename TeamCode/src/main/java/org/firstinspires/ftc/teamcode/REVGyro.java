package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/14/2017.
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class REVGyro {
    private HardwareMap hardwareMap;
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;
    private boolean isInitialized = false;
    public double heading;
    public REVGyro (OpMode opMode) {
        this.hardwareMap = opMode.hardwareMap;
        this.parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        this.imu = hardwareMap.get(BNO055IMU.class, "imu");
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