package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Sensors {
    HardwareMap hardwareMap;

    Servo dServo;

    TouchSensor touchTop;
    TouchSensor touchBottom;

    DistanceSensor dFixed;
    DistanceSensor dMobile;

    ColorSensor colorSensorBottom;

    public Sensors(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;
        dServo = hardwareMap.get(Servo.class, "dServo");
        dFixed = hardwareMap.get(DistanceSensor.class, "dFixed");
        dMobile = hardwareMap.get(DistanceSensor.class, "dMobile");
        colorSensorBottom = hardwareMap.get(ColorSensor.class, "colorSensorBottom");
        touchBottom = hardwareMap.get(TouchSensor.class, "touchBottom");
        touchTop = hardwareMap.get(TouchSensor.class, "touchTop");
    }

    public void rotateMobile(double angle) //in degrees for clarity
    {
        //angle    should go from 0   to 180
        //position should go from min to max
        double position = angle / 180 + .5; //TODO: Math from angle to position

        dServo.setPosition(position);
    }

    public double getMobileAngle()
    {
        double position = dServo.getPosition();
        return 0; //TODO: Math from position to angle in radians
    }
}
