package org.firstinspires.ftc.teamcode.TestCode.BNO055Test;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Core.RobotBase;
import org.firstinspires.ftc.teamcode.Core.Sensors.REVIMU;


public class Base extends RobotBase
{
    REVIMU imu;


    @Override
    public void init(HardwareMap HW)
    {
        hardware = HW;

        imu = new REVIMU(this);

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();

        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = true;
        params.loggingTag = "IMU";
        params.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.mapIMU("imu" , params);
    }
}
