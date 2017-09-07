package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import android.util.Log;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;


/**
 * <p>
 *      Class to manage the gyro sensor.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
final class SensorGyro
{
    private Robot _robot;                   // Robot we're working with
    private ModernRoboticsI2cGyro _gyro;    // A Modern Robotics gyro

    private int offset = 0;                 // Offset used for fast calibration


    /**
     * Creates a new gyroscope
     *
     * @param myRobot The robot we're working with
     */
    SensorGyro(Robot myRobot)
    {
        _robot = myRobot;
    }


    /**
     * Maps the gyroscope given a name and a heading mode
     *
     * @param NAME Name of the gyro as appears in the configuration file
     * @param mode Heading mode (cartesian or cardinal) to set the gyro to
     */
    void mapGyro(final String NAME , ModernRoboticsI2cGyro.HeadingMode mode)
    {
        HardwareAux mapHelper = new HardwareAux(_robot);    // Helps with mapping

        _gyro = mapHelper.mapMRGyro(NAME , mode);
    }


    /**
     * Returns the gyro heading, converted for our needs. 0 degrees is right, 90 is up, etc if the
     * gyro is mapped with cartesian. Otherwise, the unmodified gyro heading is returned. If the
     * method cannot get a heading from the gyro, -1 is returned.
     *
     * @return Returns gyro heading, may be adjusted
     */
    int heading()
    {
        final int ERROR = -1;           // Number that denotes failure
        int heading;                    // Stores the heading from the gyro

        try
        {
            // Do something with the offset here
            if(_gyro.getHeadingMode() == ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN)
                heading = UtilBasic.trimAngle(_gyro.getHeading() + 90 - offset);

            else
                heading = _gyro.getHeading();
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot get gyro heading, check your mapping");
            heading = ERROR;
        }

        return heading;
    }


    /**
     * Attempts to fully calibrates the gyro in a certain amount of time
     *
     * @return True if attempt to calibrate successful, false otherwise
     */
    boolean calibrate()
    {
        final int TIMEOUT = 7_000;          // Maximum amount of time to wait before attempt fails

        boolean success = true;             // Whether calibration was successful or not

        long startTime = System.currentTimeMillis();

        try
        {
            _gyro.calibrate();

            while(_gyro.isCalibrating())
            {
                if(System.currentTimeMillis() > startTime + TIMEOUT)
                {
                    success = false;
                    break;
                }
            }
        }
        catch(Exception e)
        {
            success = false;
        }

        return success;
    }


    /**
     * Quickly calibrate the gyro, just adjusts the offset
     *
     * @return True if attempt successful, false otherwise
     */
    boolean fastCalibrate()
    {
        boolean success = true;     // Tells if attempt to calibrate is successful or not

        try
        {
            offset = _gyro.getHeading();
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot calibrate gyro, check your mapping");
            success = false;
        }

        return success;
    }
}