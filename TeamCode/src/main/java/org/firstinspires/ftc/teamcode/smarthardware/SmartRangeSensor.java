package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SmartRangeSensor
{
    public final ModernRoboticsI2cRangeSensor sensor;

    public SmartRangeSensor(ModernRoboticsI2cRangeSensor rangeSensor, int i2cAddress)
    {
        this.sensor = rangeSensor;

        rangeSensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));
    }

    public boolean returningValidOutput()
    {
        return !(sensor.getDistance (DistanceUnit.CM) < 1.0);
    }
    public double ultrasonicDistCM ()
    {
        return sensor.cmUltrasonic ();
    }
    public double getDistOffFromIdealWallDist()
    {
        return ultrasonicDistCM () - 15;
    }
}
