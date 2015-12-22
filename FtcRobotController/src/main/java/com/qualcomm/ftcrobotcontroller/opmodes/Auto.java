package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.bamboo.Motor;
import com.qualcomm.ftcrobotcontroller.bamboo.Timing;
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
    public final double VARIENCE = 0.1;

    public final int LIGHT_RIGHT = 0;
    public final int LIGHT_LEFT = 1;
    public final int LIGHT_UNKNOWN = -1;
    public final boolean ONE_WHEEL = true;


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
        
    }

    @Override
    public void init() {
        // set some consants...

        lightRight = hardwareMap.lightSensor.get("lightRight");
        lightLeft = hardwareMap.lightSensor.get("lightLeft");
        gyro = hardwareMap.gyroSensor.get("gyro");

        right = new Motor("right", hardwareMap);
        left = new Motor("left", hardwareMap, true);

        rotRight = new Motor("rightrot", hardwareMap);
        rotLeft = new Motor("leftrot", hardwareMap);
        extRight = new Motor("rightext", hardwareMap);
        extLeft = new Motor("leftext", hardwareMap);

        lightRight.enableLed(true);
        lightLeft.enableLed(true);


    }

    @Override
    public void loop()
    {
        double white = 1.0;
        double myColor = 0.8;
        double otherColor = 0.6;


        // to midfield stop at diagonal divide
        int dir = forwardUntil(myColor, 2000, 3000);

        // make the 90 turn based upon which light gets triggered
        if(dir == LIGHT_RIGHT) turnDeg(false, 90);
        else if(dir == LIGHT_LEFT) turnDeg(true, 90);
        else turnDeg(true, 900);

        // move until we find the score box
        dir = forwardUntil(myColor, 1500, 2500);

        // align ourselves with the score box
        if(dir == LIGHT_RIGHT) turnTill(true, myColor);
        else if(dir == LIGHT_LEFT) turnTill(false, myColor);

        // turn ourselfves around
        turnDeg(true, 90, ONE_WHEEL);
        forwardUntil(white, 400, 800);
        turnDeg(true, 90);

        Timing climber = new Timing();
        climber.append(right, 0.5, 0, 1000);
        climber.append(left, 0.5, 0, 1000);
        climber.append(rotRight, -0.5, 800, 1600);
        climber.append(extRight, 0.5, 1100, 1900);
        climber.append(rotRight, -0.1, 1900, 2600);
        climber.execute();


        while(true) {}
    }

    public int forwardUntil(double color, int guess, int giveup)
    {
        double r = FAST_SPEED;
        double l = FAST_SPEED;

        double gypos = 0;
        double drift = 0;
        long timefirst = System.currentTimeMillis();
        long timelast = timefirst;

        while(timelast - timefirst < giveup)
        {
            long timediff = timelast;
            timelast = System.currentTimeMillis();
            timediff = timelast - timediff;

            gypos += gyro.getRotation()*(timediff);
            //int avgchange = (right.encoderDiff()+left.encoderDiff())/2;
            long avgchange = timediff;
            drift += gypos*avgchange;

            // how do we correct our route
            r = sigmoid(SIG_WEIGHT*drift);
            l = 1 - r;

            if(timelast-timefirst > guess) set(r/2, l/2);
            else set(r, l);

            if(lightRight.getLightDetected() < color+VARIENCE && lightRight.getLightDetected() > color-VARIENCE)
            {
                stop();
                return LIGHT_RIGHT;
            }
            else if(lightLeft.getLightDetected() < color+VARIENCE && lightLeft.getLightDetected() > color-VARIENCE)
            {
                stop();
                return LIGHT_LEFT;
            }
        }

        stop();
        return -1;
    }

    public void turnDeg(boolean right, double degrees)
    {
        turnDeg(right, degrees, false);
    }

    public void turnDeg(boolean right, double degrees, boolean onewheel)
    {
        double gypos = 0;
        long timefirst = System.currentTimeMillis();
        long timelast = timefirst;

        if(onewheel)
        {
            if(!right) set(FAST_SPEED, 0);
            else set(0, FAST_SPEED);
        }
        else
        {
            if(right) set(-FAST_SPEED, FAST_SPEED);
            else set(FAST_SPEED, -FAST_SPEED);
        }

        while(gypos < degrees) {};
        stop();
    }

    public void turnTill(boolean right, double color)
    {
        if(right) set(0, FAST_SPEED);
        else set(FAST_SPEED, 0);



        if(right) {
            while(lightLeft.getLightDetected() < color+VARIENCE && lightLeft.getLightDetected() > color-VARIENCE) {}
        }
        else {
            while(lightRight.getLightDetected() < color+VARIENCE && lightRight.getLightDetected() > color-VARIENCE) {}
        }

        stop();
    }

    public double sigmoid(double inp)
    {
        return 1/(1+Math.pow(Math.E, -inp));
    }


    public void set(double r, double l)
    {
        right.set(r);
        left.set(l);
    }

    public void stop()
    {
        right.set(0);
        left.set(0);
    }

}