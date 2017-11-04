package org.firstinspires.ftc.teamcode.TestCode.BNO055Test;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;


public class Base extends RobotBase
{
    REVIMU imu;


    @Override
    public void init(HardwareMap HW , OpMode OPMODE)
    {
        super.init(HW , OPMODE);

        imu = new REVIMU(this);

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();

        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = true;
        params.loggingTag = "IMU";
        params.calibrationDataFile = "IMUCalibration.json";

        imu.mapIMU("imu" , params);
    }
}
