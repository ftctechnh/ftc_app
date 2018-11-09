package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class IMU {
    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    public IMU(HardwareMap hwMap){
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.mode = BNO055IMU.SensorMode.IMU;


        imu = hwMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);



        while (!imu.isGyroCalibrated());
    }

    public double getHeading(){
        Orientation angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angle.firstAngle;
    }

    public void resetAngleToZero() {
        imu.initialize(parameters);
        while (!imu.isGyroCalibrated());
    }
}
