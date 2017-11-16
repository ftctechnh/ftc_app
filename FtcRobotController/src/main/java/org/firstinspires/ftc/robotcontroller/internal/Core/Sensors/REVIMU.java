/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-10-12

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;


import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.directcurrent.core.TelMet;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcontroller.internal.Core.HardwareMapper;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

import java.io.File;


/**
 * Wrapper for the BNO055 that is built into the REV modules- handles easy retrieval of orientation
 * and acceleration
 */
public class REVIMU
{
    private BNO055IMU _imu = null;

    private Orientation _orien = null;
    private Acceleration _accel = null;



    /**
     * Maps the Bosch BNO055 IMU given a name and parameters
     *
     * @param BASE Robot base the IMU belongs to
     * @param NAME Name of the IMU as appears in the configuration file
     * @param PARAMETERS Fully initialized Parameters of the IMU
     */
    public void init(final RobotBase BASE , final String NAME , BNO055IMU.Parameters PARAMETERS)
    {
        HardwareMapper mapHelper = new HardwareMapper(BASE);

        _imu = mapHelper.mapREVIMU(NAME , PARAMETERS);
    }


    /**
     * Starts the logging of measured acceleration
     */
    public void startAccelMeasurement()
    {
        _imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }


    /**
     * Pulls sensor readings
     */
    public void pull()
    {
        _orien = _imu.getAngularOrientation(AxesReference.INTRINSIC , AxesOrder.XYZ , AngleUnit.DEGREES);
        _accel = _imu.getLinearAcceleration();
    }


    /**
     * @return Returns x orientation from the gyro
     */
    public double xAngle()
    {
        return _orien.firstAngle;
    }


    /**
     * @return Returns y orientation from the gyro
     */
    public double yAngle()
    {
        return _orien.secondAngle;
    }


    /**
     * @return Returns z orientation from the gyro
     */
    public double zAngle()
    {
        return _orien.thirdAngle;
    }


    /**
     * @return Returns acceleration in the x direction from the accelerometer
     */
    public double xAccel()
    {
        return _accel.xAccel;
    }


    /**
     * @return Returns acceleration in the y direction from the accelerometer
     */
    public double yAccel()
    {
        return _accel.yAccel;
    }


    /**
     * @return Returns acceleration in the z direction from the accelerometer
     */
    public double zAccel()
    {
        return _accel.zAccel;
    }
}
