package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Research_and_Development.ObjectOriented.Robot.Components.Subcomponents;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;


public class REV_Gyro {

    public BNO055IMU gyro;
    public Orientation angles;


    public REV_Gyro(){
        this.gyro = gyro;
    }

    public void initialize(){
        setParameters();
        composeTelemetry();
    }

    private void setParameters(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        gyro.initialize(parameters);
        composeTelemetry();
    }

    private void composeTelemetry() {
        angles   = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        formatAngle(angles.angleUnit, angles.firstAngle);
        formatAngle(angles.angleUnit, angles.secondAngle);
        formatAngle(angles.angleUnit, angles.thirdAngle);
    }

    private String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    private String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public double getHeading() {

        double heading = angles.firstAngle;

        return heading;
    }
}
