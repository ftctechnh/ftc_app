package org.firstinspires.ftc.teamcode.Core.Sensors;


import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.Core.HardwareMapper;
import org.firstinspires.ftc.teamcode.Core.RobotBase;

public class REVIMU
{
    private RobotBase _robot = null;

    private BNO055IMU _imu = null;

    private Orientation _orien = null;
    private Acceleration _accel = null;


    /**
     * Creates a new gyroscope
     *
     * @param myRobot The robot we're working with
     */
    public REVIMU(RobotBase myRobot)
    {
        _robot = myRobot;
    }



    /**
     * Maps the Bosch BNO055 IMU given a name and parameters
     *
     * @param NAME Name of the IMU as appears in the configuration file
     * @param PARAMETERS Fully initialized Parameters of the IMU
     */
    public void mapIMU(final String NAME , BNO055IMU.Parameters PARAMETERS)
    {
        HardwareMapper mapHelper = new HardwareMapper(_robot);  // Helps with mapping

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
        _accel = _imu.getGravity();
    }


    public double xAngle()
    {
        return _orien.firstAngle;
    }


    public double yAngle()
    {
        return _orien.secondAngle;
    }


    public double zAngle()
    {
        return _orien.thirdAngle;
    }
}
