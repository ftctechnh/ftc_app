package org.firstinspires.ftc.teamcode.utils;

import android.util.Log;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by 292486 on 10/26/2016.
 */
//To be composed in a Hardware classtype, only heading and rotation should be accessible
public class Gyro {

    private GyroSensor gyro;
    private ElapsedTime timer;
    private double lastTime, deltaTime;
    private double lastRotation;
    private double averageRotation, minRotation, maxRotation;
    private HardwareMap map;
    public double heading;  //In degrees

    public Gyro(HardwareMap map){
        this(map, "gyro");
    }

    public Gyro(HardwareMap map, String name){
        this.map = map;
        gyro = map.gyroSensor.get(name);

        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    //Robot must be still. Test if we need it
    public void calibrate(){
        double rotation = 0;
        averageRotation = 0;
        minRotation = Integer.MAX_VALUE;
        maxRotation = Integer.MIN_VALUE;
        for(int i = 0; i < 1000; i++){
            rotation = gyro.getRotationFraction();
            averageRotation += rotation;
            if(rotation < minRotation)
            {
                minRotation = rotation;
            }
            if(rotation > maxRotation)
            {
                maxRotation = rotation;
            }

            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {
                Log.e(e.getMessage(), "");
            }
        }
        averageRotation /= 1000;

        if(minRotation < -2.0) minRotation = -2.0;  //Cap it off
        if(maxRotation > 2.0) maxRotation = 2.0;
    }

    public void resetHeading(){
        heading = 0;
    }

    public double trapezoidalIntegrate(){
        return (1.0F/2.0F) * (deltaTime) * ((rotationOffset() + lastRotation)*360.0);
    }

    public void updateHeading(){
        deltaTime = timer.time() - lastTime;
        if(gyro.getRotationFraction() < minRotation || gyro.getRotationFraction() > maxRotation) {
            heading += trapezoidalIntegrate();   //Trapezoidal integration
        }

        lastTime = timer.time();
        lastRotation = rotationOffset();
    }

    public double getTime()
    {
        return timer.time();
    }

    public double rotationOffset()
    {
        return gyro.getRotationFraction() - averageRotation;
    }

    public double getAverage()
    {
        return averageRotation;
    }
}
