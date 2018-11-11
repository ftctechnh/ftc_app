package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Sensors {
    HardwareMap hardwareMap;

    //Servo dServoX;
    //Servo dServoZ;

    public TouchSensor touchTop;
    public TouchSensor touchBottom;

    DistanceSensor dFixed;
    DistanceSensor dMobile;

    public Sensors(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;
        //dServoX = hardwareMap.get(Servo.class, "dServoX");
        //dServoZ = hardwareMap.get(Servo.class, "dServoZ");
        dFixed = hardwareMap.get(DistanceSensor.class, "dFixed");
        dMobile = hardwareMap.get(DistanceSensor.class, "dMobileZ");
        touchTop = hardwareMap.get(TouchSensor.class, "touchTop");
        touchBottom = hardwareMap.get(TouchSensor.class, "touchBottom");
    }

    public void rotateMobileX(double rightOfCenter) //in degrees for clarity
    {
        //angle    should go from -90   to 90
        //position should go from min to max
        double position = rightOfCenter / 90 ;

        //dServoX.setPosition(position);
    }

//    public double getMobileAngle()
//    {
//        double position = dServoX.getPosition();
//        return position  * Math.PI;
//    }

}
