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

    private final double FIRST_FORWARD = 16;
    private final double SECOND_FORWARD = 1;
    private final double AMT_ROT = 45;


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
        // set some constants...

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

        // wait for the time which is set in the robot controller application.

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

            double white = 1.0;
            double myColor = 0.8;
            double otherColor = 0.6;

            // to midfield stop at diagonal divide
            forwardUntil(FIRST_FORWARD);

            // now which way do we turn...?
            turnDeg(FtcRobotControllerActivity.isRed, AMT_ROT);

            Timing test = new Timing();
            test.append(right, 0.5, 0, 3000);
            test.execute();


            /*Timing climber = new Timing();
            climber.append(right, 0.5, 0, 1000);
            climber.append(left, 0.5, 0, 1000);
            climber.append(rotRight, -0.5, 800, 1600);
            climber.append(extRight, 0.5, 1100, 1900);
            climber.append(rotRight, -0.1, 1900, 2600);
            climber.execute();*/

        }

    public void forwardUntil(double turns)
    {

        DecimalFormat df = new DecimalFormat("###.##");

        telemetry.addData("fu", "starting");

        double r = FAST_SPEED;
        double l = FAST_SPEED;

        double gypos = 0;
        double drift = 0;
        long timefirst = System.currentTimeMillis();
        long timelast = timefirst;

        double totDist = 0;

        while(totDist < turns && opModeIsActive())
        {
            // so we are trying to calculate the amount off cource we currently are
            // it terms of wheel rotations, to get this we need a few things
            // our current angle in degrees
            // the amount in turns we've moved since last frame.
            // the integral of[ sin (angle) * dist traveled ]
            // should give us that value

            long timediff = timelast;
            timelast = System.currentTimeMillis();
            timediff = timelast - timediff;
            gypos += gyro.dps() * (timediff);
            double avgchange = (right.turnDiff()+left.turnDiff())/2;
            drift += Math.sin(Math.PI*(gypos/180)) * avgchange;
            totDist += Math.cos(Math.PI*(gypos/180)) * avgchange;

            // now we have the drift from course, we correct naturally
            // with the use of a sigmoid function
            // the function balances left and right to correct back to the mean
            // along the line, adjust sig weight as needed.

            l = sigmoid(drift/SIG_WEIGHT);
            r = 1 - l;

            set(r, l);
            telemetry.addData("fu", df.format(r)+", "+df.format(l)+"/ moving"+ (totDist));
            telemetry.addData("gy", df.format(gyro.rotation()) + ", " + df.format(gyro.dps()) + ", " + df.format(gypos) + ", " + df.format(drift));
            //telemetry.addData("ls", "(" + (int) (lightLeft.get() * 100) + ", " + (int) (lightRight.get() * 100) + ")");
        }
        zero();
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

        while(gypos < degrees && opModeIsActive()) {
            long timediff = timelast;
            timelast = System.currentTimeMillis();
            timediff = timelast - timediff;
            gypos += gyro.dps() * (timediff);
        };
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