package org.firstinspires.ftc.teamcode.mechanism.impl;

/**
 * Created by ftc6347 on 9/1/17.
 */

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;

/**
 * A mechanism that wraps around and abstracts from an BNO055IMU.
 *
 * @see BNO055IMU
 */
public class BNO055IMUWrapper implements IMechanism {

    private BNO055IMU imu;

    /**
     * The calibration file name this class uses.
     * JSON calibration data should be written to this file.
     */
    public static final String CALIBRATION_DATA_FILE = "BNO055IMUCalibration.json";

    /**
     * Construct a new {@link BNO055IMUWrapper} with a reference to the robot.
     *
     * @param robot the robot using this IMU wrapper object
     */
    public BNO055IMUWrapper(Robot robot) {
        HardwareMap hardwareMap = robot.getCurrentOpMode().hardwareMap;
        this.imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = CALIBRATION_DATA_FILE;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
    }

    /**
     * Start the polling of sensor data and start integration.
     * Essentially, enable the sensor for further use.
     */
    public void startIntegration() {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    /**
     * Stop the polling of sensor data and stop integrating; disable the sensor.
     */
    public void stopIntegration() {
        imu.stopAccelerationIntegration();
    }

    private Orientation getAngles() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    /**
     * Get the IMU heading (Z axis) in degrees.
     * The value returned in degrees is within a range of +180 and -180.
     *
     * @return the Z axis of the IMU in degrees
     */
    public double getHeading() {
        Orientation angles = getAngles();
        return AngleUnit.normalizeDegrees(
                AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
    }

    /**
     * Return the {@code BNO055IMU.CalibrationStatus} of the IMU.
     *
     * @see BNO055IMU.CalibrationStatus
     * @return the calibration status of the imu
     */
    public BNO055IMU.CalibrationStatus getCalibrationStatus() {
        return imu.getCalibrationStatus();
    }

    /**
     * Return the {@code BNO055IMU.SystemStatus} of the IMU.
     *
     * @see BNO055IMU.SystemStatus
     * @return the system status of the imu
     */
    public BNO055IMU.SystemStatus getSystemStatus() {
        return imu.getSystemStatus();
    }

    /**
     * Return the internal IMU object that is wrapped around.
     *
     * @return the BNO055IMU instance
     */
    public BNO055IMU getInstance(){
        return this.imu;
    }
}
