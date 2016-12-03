package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class Sensors
{
    //The touch sensor devices
    DeviceInterfaceModule dim = null;
    DigitalChannel touchSensor = null;

    OpticalDistanceSensor opSensor1 = null;
    OpticalDistanceSensor opSensor2 = null;

    ColorSensor colorSensorLeft = null;
    ColorSensor colorSensorRight = null;
    ColorSensor colorSensorBottom = null;

    ModernRoboticsI2cRangeSensor rangeSensorFront = null;
    ModernRoboticsI2cRangeSensor rangeSensorBack = null;

    public Sensors(HardwareMap hardwareMap)
    {
        dim = hardwareMap.deviceInterfaceModule.get("dim");
        opSensor1 = hardwareMap.opticalDistanceSensor.get("op_sense1");
        opSensor2 = hardwareMap.opticalDistanceSensor.get("op_sense2");
//        touchSensor = hardwareMap.touchSensor.get("sensor_touch");
//        colorSensorLeft = hardwareMap.colorSensor.get("sensor_color_left");
//        colorSensorRight = hardwareMap.colorSensor.get("sensor_color_right");
//        colorSensorBottom = hardwareMap.colorSensor.get("sensor_color_bottom");
        rangeSensorFront = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "");
        rangeSensorBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "");
//        colorSensorBottom = hardwareMap.
    }

    public OpticalDistanceSensor getOp(int index)
    {
        if(index == 1)
            return opSensor1;
        if(index == 2)
            return opSensor2;
        return null;
    }

}
