package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.bamboo.Motor;
import com.qualcomm.ftcrobotcontroller.bamboo.Root;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by chsrobotics on 12/7/2015.
 */
public class UnitTests extends OpMode {

    public LightSensor lRight;
    public LightSensor lLeft;
    public GyroSensor gyro;

    public UnitTests()
    {

    }

    double gypos = 0;
    double drift = 0;
    long timefirst = System.currentTimeMillis();
    long timelast = timefirst;

    public Motor m;
    public Motor b;

    @Override
    public void init()
    {
        lRight = hardwareMap.lightSensor.get("lightRight");
        lLeft = hardwareMap.lightSensor.get("lightLeft");
        gyro = hardwareMap.gyroSensor.get("gyro");

        m = new Motor("right", hardwareMap);
        b = new Motor("left", hardwareMap);

        lRight.enableLed(true);
        lLeft.enableLed(true);

        //m.set(1);
        //b.set(-1);
    }

    @Override
    public void loop()
    {
        long timediff = timelast;
        timelast = System.currentTimeMillis();
        timediff = timelast - timediff;
        if(gyro.getRotation() != 587 && gyro.getRotation() != 588)
            gypos += ((gyro.getRotation()-588)/(1000)) * (timediff);
        //int avgchange = (right.encoderDiff()+left.encoderDiff())/2;
        long avgchange = timediff;
        drift += gypos * avgchange;

        telemetry.addData("ls", "(" + (int) (lRight.getLightDetected() * 100) + ", " + (int) (lLeft.getLightDetected() * 100) + ")");
        telemetry.addData("gy", gyro.getRotation() + ", " + gypos);
        telemetry.addData("bro", m._motor.getCurrentPosition()+", "+b._motor.getCurrentPosition());
    }
}
