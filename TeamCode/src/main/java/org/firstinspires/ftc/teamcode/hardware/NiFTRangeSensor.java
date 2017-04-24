/**
 * This code encapsulates the range sensor and enables the user to guarantee that the value returned is valid and not just a sensor blip (often misfires and gets 255 when the dist is less)
 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class NiFTRangeSensor
{
    public final ModernRoboticsI2cRangeSensor sensor;
    public NiFTRangeSensor (String servoName, int i2cAddress)
    {
        sensor = NiFTInitializer.initialize (ModernRoboticsI2cRangeSensor.class, servoName);

        sensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));
    }

    public boolean returningValidOutput()
    {
        return !(sensor.getDistance (DistanceUnit.CM) < 1.0);
    }

    //Since sometimes the range sensor will return 255 at longer distances, this code obtains a more realistic value.
    public double validDistCM(double defaultVal)
    {
        return validDistCM (defaultVal, 0);
    }
    public double validDistCM(double defaultVal, long maxTimePermitted)
    {
        double ultrasonicDist = sensor.cmUltrasonic ();

        if (ultrasonicDist >= 255 || ultrasonicDist <= 0)
        {
            if (maxTimePermitted > 0)
            {
                long startTime = System.currentTimeMillis ();
                while (System.currentTimeMillis () - startTime <= maxTimePermitted)
                {
                    ultrasonicDist = sensor.cmUltrasonic ();
                    if (!(ultrasonicDist >= 255 || ultrasonicDist <= 0))
                        break;
                }
            }
            else
                return defaultVal;
        }

        return ultrasonicDist;
    }
}
