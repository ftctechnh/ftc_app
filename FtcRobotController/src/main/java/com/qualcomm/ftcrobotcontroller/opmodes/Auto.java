package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.bamboo.Gyro;
import com.qualcomm.ftcrobotcontroller.bamboo.Light;
import com.qualcomm.ftcrobotcontroller.bamboo.Motor;
import com.qualcomm.ftcrobotcontroller.bamboo.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * Created by alex on 11/24/15.
 */
public class Auto extends LinearOpMode {

    // ok so what do I need to have here.. a move, and a turn?

    public final double FAST_SPEED = 0.5;
    public final double SLOW_SPEED = 0.25;
    public final double SIG_WEIGHT = 15;
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

    Light lightRight;
    Light lightLeft;
    Gyro gyro;
    int wait;

    Motor right, left, rotLeft, rotRight, extLeft, extRight;

    boolean isRed = false;

    public Auto() {
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // set some consants...



        lightRight = new Light("lightRight", hardwareMap);
        lightLeft = new Light("lightLeft", hardwareMap);
        gyro = new Gyro("gyro", hardwareMap);

        right = new Motor("right", hardwareMap);
        left = new Motor("left", hardwareMap, true);

        rotRight = new Motor("rightrot", hardwareMap, true);
        rotLeft = new Motor("leftrot", hardwareMap);
        extRight = new Motor("rightext", hardwareMap);
        extLeft = new Motor("leftext", hardwareMap, true);

        lightRight.enable();
        lightLeft.enable();

        telemetry.addData("wait", FtcRobotControllerActivity.waittime);

        waitForStart();

            if (FtcRobotControllerActivity.waittime != 0) {
                long st = System.currentTimeMillis();
                while ((System.currentTimeMillis() - st) / 1000 < FtcRobotControllerActivity.waittime && opModeIsActive()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            telemetry.addData("made", "it");

            double white = 1.0;
            double myColor = 0.8;
            double otherColor = 0.6;

            // to midfield stop at diagonal divide
            int dir = forwardUntil(myColor, 2000, 4000);

           /* // make the 90 turn based upon which light gets triggered
            if (dir == LIGHT_RIGHT) turnDeg(false, 90);
            else if (dir == LIGHT_LEFT) turnDeg(true, 90);
            else turnDeg(true, 900);

            // move until we find the score box
            dir = forwardUntil(myColor, 1500, 2500);

            // align ourselves with the score box
            if (dir == LIGHT_RIGHT) turnTill(true, myColor);
            else if (dir == LIGHT_LEFT) turnTill(false, myColor);

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
            climber.execute();*/

        }

    public int forwardUntil(double color, int guess, int giveup)
    {
        return forwardUntil(color, guess, giveup, false);
    }

    public int forwardUntil(double color, int guess, int giveup, boolean negate)
    {

        DecimalFormat df = new DecimalFormat("###.##");

        telemetry.addData("fu", "starting");

        double r = FAST_SPEED;
        double l = FAST_SPEED;

        double gypos = 0;
        double drift = 0;
        long timefirst = System.currentTimeMillis();
        long timelast = timefirst;

        while(timelast - timefirst < giveup && opModeIsActive())
        {
            long timediff = timelast;
            timelast = System.currentTimeMillis();
            timediff = timelast - timediff;
            gypos += gyro.dps() * (timediff);
            double avgchange = (right.turnDiff()+left.turnDiff())/2;
            drift += gypos * avgchange;

            l = sigmoid(drift/SIG_WEIGHT);
            r = 1 - l;

            if(negate) {
                double temp = -r;
                r = -l;
                l = temp;
            }

            if(timelast-timefirst > guess)
            {
                set(r/2, l/2);
                telemetry.addData("fu", df.format(r/2)+", "+df.format(l/2)+"/ guessing"+ (timelast - timefirst));
            }
            else {
                set(r, l);
                telemetry.addData("fu", df.format(r)+", "+df.format(l)+"/ moving"+ (timelast - timefirst));
            }

            if(lightRight.get() < color+VARIENCE && lightRight.get() > color-VARIENCE)
            {
                zero();
                return LIGHT_RIGHT;
            }
            else if(lightLeft.get() < color+VARIENCE && lightLeft.get() > color-VARIENCE)
            {
                zero();
                return LIGHT_LEFT;
            }

            telemetry.addData("gy", df.format(gyro.rotation()) + ", " + df.format(gyro.dps()) + ", " + df.format(gypos) + ", " + df.format(drift));
            telemetry.addData("ls", "(" + (int) (lightLeft.get() * 100) + ", " + (int) (lightRight.get() * 100) + ")");


        }

        zero();
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
        zero();
    }

    public void turnTill(boolean right, double color)
    {
        if(right) set(0, FAST_SPEED);
        else set(FAST_SPEED, 0);

        if(right) {
            while(lightLeft.get() < color+VARIENCE && lightLeft.get() > color-VARIENCE) {}
        }
        else {
            while(lightRight.get() < color+VARIENCE && lightRight.get() > color-VARIENCE) {}
        }

        zero();
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

    public void zero()
    {
        set(0.0, 0.0);
    }

}