/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Blue Auto", group="Main")  // @Autonomous(...) is the other common choice
@Disabled
public class AutonomousSecondaryBlue extends OpMode {

    BotHardware robot = new BotHardware();
    /* Declare OpMode members. */

    //some constants to make navigating the field easier
    static final double mmToEncode = 1; //TODO: Find this value
    static final double inchToMm = 25.4;
    static final double footToMm = inchToMm * 12;
    static final double squareToMm = footToMm * 2;

    final float power = 0.2f;

    //constants for pushy pushy
    final double pushPos = 1.0;
    final double time = 2.0;
    final boolean red = false;
    final int colorThresh = 200;
    final float driveTime = 0.1f;
    final int driveLoopCount = 2;

    final boolean debug = false;

    // parameters of the PID controller for this sequence's first part
    final float Kp = 1.0f;        // degree heading proportional term correction per degree of deviation
    final float Ki = 0.2f;         // ... integrator term
    final float Kd = 0;             // ... derivative term
    final float KiCutoff = 1.0f;    // maximum angle error for which we update integrator

    SensorLib.PID mdPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);

    // create the root Sequence for this autonomous OpMode
    AutoLib.Sequence mSequence = new AutoLib.LinearSequence();

    // start out not-done
    boolean bDone = false;

    @Override
    public void init(){
        robot.init(this, debug);

        // drive to the first beacon
        //mSequence.add(new SquirrleyAzimuthTimedDriveStep(this, -90.0f, robot.getNavXHeadingSensor(), mdPid, robot.getMotorArray(), power, 10000, true));
        //mSequence.add(new SquirrleyAzimuthTimedDriveStep(this, 0, robot.getNavXHeadingSensor(), mdPid, robot.getMotorArray(), power, 5000, true));

        //line following here

        mSequence.add(new AutoLib.MoveByTimeStep(robot.getMotorArray(), 0.0, 0, true));

        //pushy pushy
        mSequence.add(new PushyLib.pushypushy(this, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, time, red, colorThresh, power, driveTime, driveLoopCount)); //SO. MANY. VARIABLES.

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start(){
        robot.startNavX();
    }

    @Override
    public void loop(){
        // until we're done, keep looping through the current Step(s)
        while (!bDone) {
            bDone = mSequence.loop();       // returns true when we're done
            telemetry.update();
        }

        telemetry.addData("Sequence finished!", "");
    }

    @Override
    public void stop(){
        robot.navX.close();
    }

    static public class SquirrleyAngledGuideStep extends AutoLib.Step{
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
        private float mStartHeading;
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private HeadingSensor mVel;
        private SensorLib.PID mgPid;                         // proportional–integral–derivative controller (PID controller)
        private SensorLib.PID mvPid;
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<AutoLib.SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public SquirrleyAngledGuideStep(OpMode mode, float heading, HeadingSensor gyro, HeadingSensor vel, SensorLib.PID gPid, SensorLib.PID vPid,
                             ArrayList<AutoLib.SetPower> motorsteps, float power)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mVel = vel;
            mgPid = gPid;
            mvPid = vPid;
            mMotorSteps = motorsteps;
            mPower = power;
        }

        public boolean loop()
        {
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
                mStartHeading = mGyro.getHeading();
            }

            // compute delta time since last call -- used for integration time of PID step
            final double time = mOpMode.getRuntime();
            final double dt = time - mPrevTime;
            mPrevTime = time;

            final float heading = mGyro.getHeading();     // get latest reading from direction sensor
            // convention is positive angles CCW, wrapping from 359-0

            final float error = SensorLib.Utils.wrapAngle(heading - mStartHeading);   // deviation from desired heading
            // deviations to left are positive, to right are negative

            // feed error through PID to get motor power correction value
            final float correction = -mgPid.loop(error, (float)dt);

            final float vHeading = mVel.getHeading();

            final float vError = SensorLib.Utils.wrapAngle(vHeading - mHeading);

            final float vCorrection = -mvPid.loop(vError, (float)dt);

            //calculate motor powers for fancy wheels
            AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(mHeading + vCorrection);

            final float leftPower = correction;
            final float rightPower = -correction;

            //set the powers
            mMotorSteps.get(0).setPower((rightPower + mp.Front()) * mPower);
            mMotorSteps.get(1).setPower((rightPower + mp.Back()) * mPower);
            mMotorSteps.get(2).setPower((leftPower + mp.Front()) * mPower);
            mMotorSteps.get(3).setPower((leftPower + mp.Back()) * mPower);

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("front power ", mp.Front());
                mOpMode.telemetry.addData("back power ", mp.Back());
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }

    }

    // a Step that uses gyro input to drive along a given course for a given distance given by motor encoders.
    // uses a SquirleyGuideStep to adjust power to 2 or 4 motors.
    // assumes a robot with up to 4 drive motors in assumed order right motors, left motors
    static public class SquirrleyAzimuthTimedAngledDriveStep extends AutoLib.ConcurrentSequence {

        public SquirrleyAzimuthTimedAngledDriveStep(OpMode mode, float heading, HeadingSensor gyro, HeadingSensor vel, SensorLib.PID gPid, SensorLib.PID vPid,
                                     DcMotor motors[], float power, float time, boolean stop)
        {
            // add a concurrent Step to control each motor

            ArrayList<AutoLib.SetPower> steps = new ArrayList<AutoLib.SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.TimedMotorStep step = new AutoLib.TimedMotorStep(em, 0, time, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new SquirrleyAngledGuideStep(mode, heading, gyro, vel, gPid, vPid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

}
