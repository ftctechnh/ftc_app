package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class RangePair
{
    Sensors sensors = null;

    private double distanceApart;


    public RangePair(HardwareMap hardwareMap, double inBetweenSensors, Sensors sensors)
    {
        distanceApart = inBetweenSensors;
    }

    public double meanDistance()
    {
        return (sensors.rangeSensorBack.getDistance(DistanceUnit.INCH)+sensors.rangeSensorFront.getDistance(DistanceUnit.INCH))/2;
    }

    public double minDistanceAway()
    {
        return Math.min(sensors.rangeSensorBack.getDistance(DistanceUnit.INCH),sensors.rangeSensorFront.getDistance(DistanceUnit.INCH));
    }

    public double angleToWall()
    {
        double frontDistance = sensors.rangeSensorFront.getDistance(DistanceUnit.INCH);
        double backDistance = sensors.rangeSensorBack.getDistance(DistanceUnit.INCH);
        //gives positive values when angled towards wall
        double difference = backDistance - frontDistance;

        return Math.atan(difference/distanceApart);
    }

}
