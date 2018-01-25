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


import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.HardwareMapper;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;


/**
 * Wrapper for the BNO055 that is built into the REV modules- handles easy retrieval of orientation
 * and acceleration
 */
public class REVIMU
{
    private BNO055IMU _imu = null;

    private Acceleration _accel = null;

    private double angle1;
    private double angle2;
    private double angle3;

    private double angle1Offset = 0;
    private double angle2Offset = 0;
    private double angle3Offset = 0;


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
     * Writes to the IMU
     *
     * @param REGISTER Register value
     * @param BYTE Value that is written in
     */
    public void write8(final BNO055IMU.Register REGISTER , final int BYTE)
    {
        _imu.write8(REGISTER , BYTE);
    }


    /**
     * Pulls sensor readings- readings are converted from a -180 to 180 scale to 0 to 360 scale
     */
    public void pull()
    {
       angle1 = _imu.getAngularOrientation(AxesReference.INTRINSIC , AxesOrder.XYZ ,
               AngleUnit.DEGREES).firstAngle - angle1Offset;
       angle2 = _imu.getAngularOrientation(AxesReference.INTRINSIC , AxesOrder.XYZ ,
               AngleUnit.DEGREES).secondAngle - angle2Offset;
       angle3 = _imu.getAngularOrientation(AxesReference.INTRINSIC , AxesOrder.XYZ ,
               AngleUnit.DEGREES).thirdAngle - angle3Offset;

       // Do some adjusting- I want a 0 to 360 scale
        if(angle1 < 0)
        {
            angle1 += 360;
            angle1 %= 360;
        }

        if(angle2 < 0)
        {
            angle2 += 360;
            angle2 %= 360;
        }

        if(angle3 < 0)
        {
            angle3 += 360;
            angle3 %= 360;
        }

        _accel = _imu.getLinearAcceleration();
    }


    /**
     * Adjusts offsets such that each angle value reads 0
     */
    public void fastCalibrate()
    {
        calibrateTo(0);
    }


    /**
     * Calibrates the angles to the given offset
     *
     * @param OFFSET Offset to set the angles to
     */
    public void calibrateTo(final int OFFSET)
    {
        angle1Offset = angle1 - OFFSET;
        angle2Offset = angle2 - OFFSET;
        angle3Offset = angle3 - OFFSET;
    }


    /**
     * @return Returns x orientation from the gyro
     */
    public double xAngle()
    {
        return angle1;
    }


    /**
     * @return Returns y orientation from the gyro
     */
    public double yAngle()
    {
        return angle2;
    }


    /**
     * @return Returns z orientation from the gyro
     */
    public double zAngle()
    {
        return angle3;
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
