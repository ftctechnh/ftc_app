package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/*
This is also an old class that needs to be updated...
 */

public class OldSensors {
    HardwareMap hardwareMap;


    //Probably wont use these...
    //OpticalDistanceSensor opSensorBottom = null;
    DistanceSensor opSensorFront;

    ColorSensor colorSensorBottom;
    ColorSensor colorSensorLeft;
    ColorSensor colorSensorRight;

    //Distance between the range sensors
    private double distanceApart = 9; //inches
    //Direction
    private boolean forwards;

    ModernRoboticsI2cRangeSensor rangeSensorFront;
    ModernRoboticsI2cRangeSensor rangeSensorBack;

    final static private int COLOR_MAX = 255;
    final static private int RED_COLOR = 255;
    final static private int BLUE_COLOR = 255;
    final static private int WHITE_COLOR = 240;

    public OldSensors(HardwareMap hardwareMap) {
        //opSensorBottom = hardwareMap.opticalDistanceSensor.get("optical_ball");
//        opSensorFront = hardwareMap.opticalDistanceSensor.get("optical_ball");
//        touchSensor = hardwareMap.touchSensor.get("sensor_touch");
        colorSensorLeft = hardwareMap.colorSensor.get("sensor_color_left");
        colorSensorRight = hardwareMap.colorSensor.get("sensor_color_right");
        colorSensorBottom = hardwareMap.colorSensor.get("sensor_color_bottom");
//        rangeSensorFront = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "");
//        rangeSensorBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "");
//        colorSensorBottom = hardwareMap.
    }
//
//    public OpticalDistanceSensor getOp(int index){
//        touchSensor = hardwareMap.touchSensor.get("sensor_touch");
//
//        opSensor1 = hardwareMap.opticalDistanceSensor.get("sensor_optical1");
//        opSensor2 = hardwareMap.opticalDistanceSensor.get("sensor_optical2");
//
//        colorSensorLeft = hardwareMap.colorSensor.get("sensor_color_left");
//        colorSensorRight = hardwareMap.colorSensor.get("sensor_color_right");
//        colorSensorBottom = hardwareMap.colorSensor.get("sensor_color_bottom");
//
//        rangeSensorFront = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range_front");
//        rangeSensorBack = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range_back");
//    }

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
        if(colorSensorBottom != null && colorSensorBottom.red() >= COLOR_MAX & colorSensorBottom.blue() >= COLOR_MAX & colorSensorBottom.green() >= COLOR_MAX)
            return true;

        else if (colorSensorBottom.alpha() >= WHITE_COLOR) {
           return true;
        } else {
            return false;
        }
    }

    /*
    boolean opIsBottomWhite()
    {
        return opSensorBottom.getLightDetected() > 200;
    }
    */

    double getFrontDistance()
    {
        return rangeSensorFront.getDistance(DistanceUnit.INCH);
    }

    double getBackDistance()
    {
        return rangeSensorBack.getDistance(DistanceUnit.INCH);
    }

    public DistanceSensor getOp(int index) {
        if (index == 1)
            return null;
        if (index == 2)
            return opSensorFront;
        return null;
    }

    public double minDistanceAway()
    {
        return Math.min(rangeSensorBack.getDistance(DistanceUnit.INCH), rangeSensorFront.getDistance(DistanceUnit.INCH));
    }

    public double angleToWall()
    {
        double frontDistance = rangeSensorFront.getDistance(DistanceUnit.INCH);
        double backDistance = rangeSensorBack.getDistance(DistanceUnit.INCH);
        //gives positive values when angled towards wall
        double difference = backDistance - frontDistance;

        if(forwards){
            return Math.atan(difference/distanceApart);
        }
        else{
            return -1 * Math.atan(difference/distanceApart);
        }

    }

    public void invertDirection()
    {
        forwards = !forwards;
    }
}
