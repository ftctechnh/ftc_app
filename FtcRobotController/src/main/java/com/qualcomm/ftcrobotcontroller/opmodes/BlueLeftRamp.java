package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

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

public class BlueLeftRamp extends OpMode {

    final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move faster

    /*
    final static double HOLD_IR_SIGNAL_STRENGTH = 0.20; // Higher values will cause the robot to follow closer
    final static double LIGHT_THRESHOLD = 0.5;
    */

    private boolean complainLight = false;

    int xVal, yVal, zVal = 0;
    int heading = 0;
    int headingNow = 0;
    int targetHeading = 0;

    int turn = 0;
    final static int TOLERANCE = 5; // error tolerance in degrees

    //
    // public AutonChoice(double durationI,
    //  double startTimeI, double endTimeI, double lMotorI, double rMotorI
    //  double turnDegreesI) {
    private org.ndhsb.ftc7593.AutonChoice[] autonSteps1 = {
            new AutonChoice(1.1, 0.0, 1.1, 1.0, 1.0, 0.0), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(0.8, 2.0, 2.8, 0.5, -0.5, 0.0), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(3.0, 3.5, 6.5, 1.0, 1.0, 0.0), // from 5 and 8.5 s, run the motor at 0.15
            new AutonChoice(0.8, 7.0, 7.8, 0.5, -0.5, 0.0), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(2.0, 8.0,10.0,1.0,1.0, 0.0), // from 0 to 1 s, run the motor at 0.15
            new AutonChoice(5.75, 15.0,20.75,0.0,0.0, 0.0) // between 15 and 20.75 s, point turn left.
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
    public BlueLeftRamp() {

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
        tbc.setSliderLPosition(tbc.sliderLPosition);
        tbc.setSliderRPosition(tbc.sliderRPosition);
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
            Color.RGBToHSV(tbc.sensorRGB.red() * 8, tbc.sensorRGB.green() * 8, tbc.sensorRGB.blue() * 8, hsvValues);

            // read the light sensor
        }

       /* if (tbc.sensorGyro != null) {
            xVal = tbc.sensorGyro.rawX();
            yVal = tbc.sensorGyro.rawY();
            zVal = tbc.sensorGyro.rawZ();
            heading = tbc.sensorGyro.getHeading();
        }
        */

        left = 0.0; // default speeds are 0.0
        right = 0.0; // default speeds are 0.0
        turn = 0; // default is straight
        for(AutonChoice value : autonSteps1)
        {
            double sTime = value.startTime;
            double eTime = value.endTime;
            double time = mRuntime.time();
            if ((sTime <= time) && (time <= eTime)) {
                left = value.lMotor;
                right = value.rMotor;
                turn = (int) value.turnDegrees;
                headingNow = heading; // ranges between 0 and 359
                targetHeading = (headingNow + turn + 360) % 360; // compute target heading; make sure it's positive.
                break;  // first rule to match wins and we leave the loop!
            }
            //double f = value.startTime;
            //System.out.println(value);
        }

        // Eden points out that this number is between 0 and 359 not between -180 and + 180
        // example case: heading: 350; turn: 45; target heading: 35 - so we should turn right 45
        // example case: heading: 300; turn: 45; target heading: 345 - so we should turn right 45
        // example case: heading: 310; turn: -45; target heading: 275 - so we should turn left 45
        // example case: heading: 10; turn: -45; target heading: 325 - so we should turn left 45
        //
        if (turn != 0) {

            int turnAmount = (targetHeading - heading);
            // turnAmount -325
            // turnAmount 45
            // turnAmount -45
            // turnAmount 315

            if (Math.abs(turnAmount) > TOLERANCE) {

                int sign = 1; // turn one direction
                if (turnAmount < 0) sign = -1;

                //float turnSpeed = ((((float) turnAmount / 180.0f) * 0.8f) * 0.5f)  + 0.2f); // scale turn rate
                float turnSpeed = (((float) turnAmount/ 400.0f) + 0.2f);

                left = (float) sign * turnSpeed;    // might need to flip this for turn direction.
                right = -left;

                telemetry.addData("turn: heading: ",  String.format("%d", heading));
                telemetry.addData("turn: targetHeading: ",  String.format("%d", targetHeading));
                telemetry.addData("turn: turnAmount: ",  String.format("%d", turnAmount));
                telemetry.addData("turn: left",  String.format("%.2f", left));
                telemetry.addData("turn: right", String.format("%.2f", right));
                System.out.println("turn: left" + String.format("%.2f", left));

            } else {
                turn = 0; // stop the turn
            }
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
