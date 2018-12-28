package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;

public class IMU {

    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    ElapsedTime GyroTimeOut;

    public IMU(HardwareMap hwMap) {
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.mode = BNO055IMU.SensorMode.IMU;

        imu = hwMap.get(BNO055IMU.class, "imu");

        AbstractOpMode.telemetry.addData("IMU initializing: " + imu.toString());

        imu.initialize(parameters);

        GyroTimeOut = new ElapsedTime();
        GyroTimeOut.reset();

        while (!imu.isGyroCalibrated() && GyroTimeOut.milliseconds() <= 1000 && AbstractOpMode.isOpModeActive()) {
            AbstractOpMode.telemetry.addDataPhone(DoubleTelemetry.LogMode.INFO, imu.getCalibrationStatus().toString());
            AbstractOpMode.telemetry.update();
        }

        AbstractOpMode.telemetry.addData("IMU initialized");
    }

    public double getHeading() {
        Orientation angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angle.firstAngle;
    }

    public void resetAngleToZero() {
        imu.initialize(parameters);

        while (!imu.isGyroCalibrated() && GyroTimeOut.milliseconds() <= 1000) ;
    }

    public boolean isGyroCalibrated() {
        return imu.isGyroCalibrated();
    }
}