package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Sensors {
    //The touch sensor devices
    DeviceInterfaceModule dim = null;
    TouchSensor touchSensor = null;

    //Probably wont use these...
    OpticalDistanceSensor opSensor1 = null;
    OpticalDistanceSensor opSensor2 = null;

    ColorSensor colorSensorLeft = null;
    ColorSensor colorSensorRight = null;
    ColorSensor colorSensorBottom = null;

    ModernRoboticsI2cRangeSensor rangeSensorFront = null;
    ModernRoboticsI2cRangeSensor rangeSensorBack = null;

    final static private int COLOR_MAX = 255;
    final static private int RED_COLOR = 255;
    final static private int BLUE_COLOR = 255;
    final static private int WHITE_COLOR = 255;

    public Sensors(HardwareMap hardwareMap) {
        dim = hardwareMap.deviceInterfaceModule.get("dim");
        touchSensor = hardwareMap.touchSensor.get("sensor_touch");

        opSensor1 = hardwareMap.opticalDistanceSensor.get("sensor_optical1");
        opSensor2 = hardwareMap.opticalDistanceSensor.get("sensor_optical2");

        colorSensorLeft = hardwareMap.colorSensor.get("sensor_color_left");
        colorSensorRight = hardwareMap.colorSensor.get("sensor_color_right");
        colorSensorBottom = hardwareMap.colorSensor.get("sensor_color_bottom");

        rangeSensorFront = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range_back");
        rangeSensorBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range_front");
    }

    boolean isLeftRed() {
        if (colorSensorLeft.red() >= RED_COLOR)
            return true;
        else
            return false;
    }

    boolean isRightRed() {
        if (colorSensorRight.red() >= RED_COLOR)
            return true;
        else
            return false;
    }

    boolean isLeftBlue() {
        if (colorSensorLeft.blue() >= BLUE_COLOR)
            return true;
        else
            return false;
    }

    boolean isRightBlue() {
        if (colorSensorRight.blue() >= BLUE_COLOR)
            return true;
        else
            return false;
    }

    boolean isBottomWhite()
    {
        if(colorSensorBottom.red() >= COLOR_MAX & colorSensorBottom.blue() >= COLOR_MAX & colorSensorBottom.green() >= COLOR_MAX)
            return true;
        else
            return false;
//        if (colorSensorBottom.alpha() >=WHITE_COLOR) {
//            return true;
//        } else {
//            return false;
//        }
    }

    double getFrontDistance()
    {
        return rangeSensorFront.getDistance(DistanceUnit.INCH);
    }

    double getBackDistance()
    {
        return rangeSensorBack.getDistance(DistanceUnit.INCH);
    }

    public OpticalDistanceSensor getOp(int index) {
        if (index == 1)
            return opSensor1;
        if (index == 2)
            return opSensor2;
        return null;
    }
}
