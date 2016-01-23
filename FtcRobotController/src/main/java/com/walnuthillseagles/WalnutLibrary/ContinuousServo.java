package com.walnuthillseagles.walnutlibrary;

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
    //ranges
    private double upperRange;
    private double lowerRange;

    public ContinuousServo(Servo myServo, double center, double max, double min){
        servo = myServo;
        trueCenter = center;
        trueMax = max;
        trueMin = min;
        //Initilize Ranges
        
    }
    public void operate(){

    }
    public void stop(){

    }

    private double scaleInput(){
        return 0;
    }
}
