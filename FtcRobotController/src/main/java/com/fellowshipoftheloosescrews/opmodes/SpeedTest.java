package com.fellowshipoftheloosescrews.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Thomas on 6/24/2015.
 *
 * Measures how fast the framework calls the loop() method.
 */
public class SpeedTest extends OpMode {

    //time that start() is called
    private long startTime;

    //number of iterations of loop()
    private long numOfCycles = 0;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        startTime = System.nanoTime();
    }

    @Override
    public void loop() {
        numOfCycles++;

        //finds the amount of time between now and start()
        long currentTime = System.nanoTime() - startTime;

        //calculate the number of cycles per second
        double cyclesPerSecond = (double)numOfCycles / (double)currentTime * 1000000000.0d;

        //add the data to telemetry
        telemetry.addData("Text", "**** Speed Test ****");
        telemetry.addData("Number of cycles", numOfCycles);
        telemetry.addData("Time", currentTime / 1000000000.0d);
        telemetry.addData("Cycles per second", cyclesPerSecond);


    }

    @Override
    public void stop() {

    }
}
