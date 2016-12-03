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
    ModernRoboticsI2cRangeSensor rangeSensorFront = null;
    ModernRoboticsI2cRangeSensor rangeSensorBack = null;

    double distanceApart;


    public RangePair(HardwareMap hardwareMap, double cmBetweenSensors)
    {
        rangeSensorFront = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "");
        rangeSensorBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "");
        distanceApart = cmBetweenSensors;
    }

    public double meanDistance()
    {
        return (rangeSensorBack.getDistance(DistanceUnit.CM)+rangeSensorFront.getDistance(DistanceUnit.CM))/2;
    }

    public double angleToWall()
    {
        double frontDistance = rangeSensorFront.getDistance(DistanceUnit.CM);
        double backDistance = rangeSensorBack.getDistance(DistanceUnit.CM);
        double difference = frontDistance - backDistance;

        return Math.atan(difference/distanceApart);
    }

}
