package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.bamboo.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by alex on 11/24/15.
 */
public class Auto extends OpMode {

    // ok so what do I need to have here.. a move, and a turn?

    public final double FAST_SPEED = 0.5;
    public final double SLOW_SPEED = 0.25;
    public final double SIG_WEIGHT = 10;


    // the path

    // begin with robot backward against the wall and with 90 towards the basket
    // more straight until it hits the color in question with one sensor.
    // turn a 90
    // more straight until we hit the color on the other side
    // align turn until we are lined up
    // turn to align with white center line
    // use the robot as a fulcrum to calculate our position???
    //

    LightSensor lightRight;
    LightSensor lightLeft;
    GyroSensor gyro;

    Motor right;
    Motor left;
    Motor rotRight;
    Motor rotLeft;
    Motor extRight;
    Motor extLeft;

    boolean isRed = false;

    public Auto() {
        lightRight = hardwareMap.lightSensor.get("lightRight");
        lightLeft = hardwareMap.lightSensor.get("lightLeft");
        gyro = hardwareMap.gyroSensor.get("gyro");

        right = new Motor("right", hardwareMap);
        left = new Motor("left", hardwareMap, true);

        rotRight = new Motor("rotRight", hardwareMap);
        rotLeft = new Motor("rotLeft", hardwareMap);
        extRight = new Motor("extRight", hardwareMap);
        extLeft = new Motor("extLeft", hardwareMap);
    }

    @Override
    public void init() {
        // set some consants...
        forwardUntil(123, 10000);

    }

    @Override
    public void loop()
    {

    }

    public void forwardUntil(int color, int guess)
    {
        double r = FAST_SPEED;
        double l = FAST_SPEED;

        double gypos = 0;
        double drift = 0;
        long timefirst = System.currentTimeMillis();
        long timelast = timefirst;

        while(true)
        {
            long timediff = timelast;
            timelast = System.currentTimeMillis();
            timediff = timelast - timediff;
            if(timelast - timefirst < guess) break;

            gypos += gyro.getRotation()*(timediff);
            //int avgchange = (right.encoderDiff()+left.encoderDiff())/2;
            long avgchange = timediff;
            drift += gypos*avgchange;

            // how do we correct our route
            r = sigmoid(SIG_WEIGHT*drift);
            l = 1 - r;

            right.set(r);
            left.set(l);
        }


    }

    public double sigmoid(double inp)
    {
        return 1/(1+Math.pow(Math.E, -inp));
    }


    public void forward(float speed)
    {
        right.set(speed);
        left.set(speed);
    }

    public void turn(int steps)
    {

    }

}
