package com.walnutHillsEagles.WalnutLibrary;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Yan Vologzhanin on 1/17/2016.
 */
public class ContinuousServo implements Drivable {
    //hardware
    private Servo servo;

    //Parameters
    private double trueCenter;
    private double trueMax;
    private double trueMin;

    public ContinuousServo(Servo myServo, double center, double max, double min){
        servo = myServo;
        trueCenter = center;
        trueMax = max;
        trueMin = min;
    }
    public void operate(){

    }
    public void stop(){

    }
}
