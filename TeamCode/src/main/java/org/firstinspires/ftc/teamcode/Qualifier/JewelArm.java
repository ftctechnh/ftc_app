package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class JewelArm {
    public Servo jewelArmServo;
    public Servo jewelFlickerServo;
    public ColorSensor sensorColor;
    public DistanceSensor sensorDistance;

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;

    public void init(HardwareMap hardwareMap) {
        jewelArmServo = hardwareMap.servo.get("jewel_arm");
        //jewelFlickerServo = hardwareMap.servo.get("jewelflicker");
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color");
        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color");


        jewelArmUp();

        //jewelflickerCenter();

    }

    public void jewelArmUp() {
//        jewelArmServo.setPosition(0.75);
        jewelArmServo.setPosition(0.0);
    }

    public void jewelArmDown() {
        jewelArmServo.setPosition(0.55);
    }

   /* public void jewelflickerBack() {
        jewelFlickerServo.setPosition(0.0);
    }
    public void jewelflickerCenter() {
        jewelFlickerServo.setPosition(0.5);
    }
    public void jewelflickerForward() {
        jewelArmServo.setPosition(1.0);
    }*/


}
