/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
//eden
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ndhsb.ftc7593.AutonChoice;
import org.ndhsb.ftc7593.tbc;

/**
 * Example autonomous program.
 * <p>
 * This example program uses elapsed time to determine how to move the robot.
 * The OpMode.java class has some class members that provide time information
 * for the current op mode.
 * The public member variable 'time' is updated before each call to the run() event.
 * The method getRunTime() returns the time that has elapsed since the op mode
 * starting running to when the method was called.
 */

public class EETestAuton extends OpMode {

    final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster

    /*
    final static double HOLD_IR_SIGNAL_STRENGTH = 0.20; // Higher values will cause the robot to follow closer
    final static double LIGHT_THRESHOLD = 0.5;
    */

    private boolean complainLight = false;

    int xVal, yVal, zVal = 0;
    int heading = 0;
    int headingNow = 0;

    int turn = 0;

    //
    // public AutonChoice(double durationI,
    //  double startTimeI, double endTimeI, double lMotorI, double rMotorI
    //  double turnDegreesI) {
    private org.ndhsb.ftc7593.AutonChoice[] autonSteps1 = {
            new AutonChoice(1.1, 0.0,1.1,1.0,1.0, 0.0), //
            new AutonChoice(0.8, 2.0,2.8,0.5,-0.5, 0.0), //
            new AutonChoice(3.0, 3.5,6.5,1.0,1.0, 0.0), //
            new AutonChoice(0.8, 7.0,7.8,0.5,-0.5, 0.0), //
            new AutonChoice(2.0, 8.0,10.0,1.0,1.0, 0.0), //
            new AutonChoice(5.75, 15.0,20.75,0.0,0.0, 0.0) //
    };

    /*
    private org.ndhsb.ftc7593.AutonChoice[] autonSteps1 = {

            new AutonChoice(0.0,1.1,1.0,1.0), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(2.0,2.8,0.5,-0.5), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(3.5,6.5,1.0,1.0), // from 5 and 8.5 s, run the motor at 0.15
            new AutonChoice(7.0,7.8,0.5,-0.5), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(8.0,10.0,1.0,1.0), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(15.0,20.75,0.0,0.0) // between 15 and 20.75 s, point turn left.
            };
    */

    public ElapsedTime mRuntime = new ElapsedTime();   // Time into round. // MPH

    /**
     * Constructor
     */
    public EETestAuton() {

    }

    void compileAuton(org.ndhsb.ftc7593.AutonChoice[] a) {
        double startTime = 0.0;
        double endTime = 0.0;
        for(AutonChoice value : a) {
            if (value.duration == 0.0) {
                value.duration = value.endTime - value.startTime;
            }
            value.startTime = startTime;
            value.endTime = startTime + value.duration;
            startTime = value.endTime;
        }
    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        compileAuton(autonSteps1);

        tbc.hardwareMap = hardwareMap;
        tbc.initHardwareMap();

        tbc.initServoValues();

        // will want to change these for auton
        tbc.setClimberPosition(tbc.climberPosition);
        tbc.setSliderPosition(tbc.sliderPosition);
        tbc.setSnowplowPosition(tbc.snowplowPosition);
        tbc.setMtapePosition(tbc.mtapePosition);
        tbc.setButtonServoSpeed(tbc.buttonServoSpeed);

        if (tbc.sc != null) {
            tbc.sc.pwmEnable(); // enable servo controller PWM outputs
        }
    }

    //--------------------------------------------------------------------------
    // start - MPH plagiarized from State example
    //--------------------------------------------------------------------------
    @Override
    public void start()
    {
        // Start game clock
        mRuntime.reset();           // Zero game clock
    }


    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {
        double reflection = 0.0;
        double left, right = 0.0;

        float hsvValues[] = {0F,0F,0F};

        if (tbc.sensorRGB != null) {
            // read the light sensor
        }

        if (tbc.sensorGyro != null) {
            xVal = tbc.sensorGyro.rawX();
            yVal = tbc.sensorGyro.rawY();
            zVal = tbc.sensorGyro.rawZ();
            heading = tbc.sensorGyro.getHeading();
        }

        left = 0.0; // default speeds are 0.0
        right = 0.0; // default speeds are 0.0
        for(AutonChoice value : autonSteps1)
        {
            double sTime = value.startTime;
            double eTime = value.endTime;
            double time = mRuntime.time();
            if ((sTime <= time) && (time <= eTime)) {
                left = value.lMotor;
                right = value.rMotor;
                turn = (int) value.turnDegrees;
                headingNow = heading;

                break;  // first rule to match wins and we leave the loop!
            }
            //double f = value.startTime;
            //System.out.println(value);
        }

		/*
		 * set the motor power
		 */

        tbc.setMotorRRightPower((float) right);
        tbc.setMotorRLeftPower((float) left);
        tbc.setMotorFRightPower((float) right);
        tbc.setMotorFLeftPower((float) left);

        // getPower()
        //telemetry.addData("1. x", String.format("%03d", xVal));
        //telemetry.addData("2. y", String.format("%03d", yVal));
        //telemetry.addData("3. z", String.format("%03d", zVal));
        telemetry.addData("4. h", String.format("%03d", heading));
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
        telemetry.addData("reflection", "reflection:  " + Double.toString(reflection));
        telemetry.addData("left tgt pwr",  "left  pwr: " + Double.toString(left));
        telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(right));

        //
        // waitForNextHardwareCycle();
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
        // make sure those motors are stopped!
        /*
        tbc.setMotorRRightPower(0.0);
        motorRLeft.setPower(0.0);
        motorFRight.setPower(0.0);
        motorFLeft.setPower(0.0);

        if (false) {
            motorFLeft = null;
            motorFRight = null;
            motorRLeft = null;
            motorRRight = null;
        }
        */
    }

}
