package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//This class defines all the specific hardware for a the BACONbot robot.

public class TestHardwareClass {
    /* Public OpMode members. */
    ColorSensor sensorColorRight;
    DistanceSensor sensorDistanceRight;
    Servo gemServo;
    ColorSensor sensorColorLeft;
    DistanceSensor sensorDistanceLeft;
    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public TestHardwareClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        // Define and Initialize Hardware
        sensorColorRight = hwMap.get(ColorSensor.class, "colorsensor");
        // get a reference to the distance sensor that shares the same name.
        sensorDistanceRight= hwMap.get(DistanceSensor.class, "colorsensor");
        //Second Color Sensor
        //sensorColorLeft = hwMap.get(ColorSensor.class,"othercolorsensor");
        //Distance Sensor of the Second Color Sensor
        //sensorDistanceLeft = hwMap.get(DistanceSensor.class,"othercolorsensor");
        gemServo = hwMap.get(Servo.class, "gemservo");

    }
}
