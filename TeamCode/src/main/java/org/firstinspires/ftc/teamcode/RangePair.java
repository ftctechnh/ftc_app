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

    double distanceApart;


    public RangePair(HardwareMap hardwareMap, double cmBetweenSensors, Sensors sensors)
    {
        distanceApart = cmBetweenSensors;
    }

    public double meanDistance()
    {
        return (sensors.rangeSensorBack.getDistance(DistanceUnit.CM)+sensors.rangeSensorFront.getDistance(DistanceUnit.CM))/2;
    }

    public double angleToWall()
    {
        double frontDistance = sensors.rangeSensorFront.getDistance(DistanceUnit.CM);
        double backDistance = sensors.rangeSensorBack.getDistance(DistanceUnit.CM);
        double difference = frontDistance - backDistance;

        return Math.atan(difference/distanceApart);
    }

}
