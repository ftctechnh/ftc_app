package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.SetPower;

import java.util.ArrayList;

/**
 * Created by Noah on 3/17/2017.
 */

//some autolib steps (read: code dump) to measure direction of velocity and use it to correct crappy meccanum wheels
    //never quite got it working due to the impossibility of measuring linear velocity

public class MeccanumVelocityLib {
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
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public SquirrleyAngledGuideStep(OpMode mode, float heading, HeadingSensor gyro, HeadingSensor vel, SensorLib.PID gPid, SensorLib.PID vPid,
                                        ArrayList<SetPower> motorsteps, float power)
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
            SquirrelyLib.MotorPowers mp = SquirrelyLib.GetSquirrelyWheelMotorPowers(mHeading + vCorrection);

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

            ArrayList<SetPower> steps = new ArrayList<SetPower>();
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

    private class NavXVelocity implements HeadingSensor {
        public float getHeading(){
            //TODO: figure out how to get integrated velocity from navx
            return 0;
        }
    }

    static public class SquirleyNavXStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mDirection;                           // compass direction to drive robot at (relative to robot itself) (-180 .. +180 degrees
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private HeadingSensor mVel;
        private SensorLib.PID gPid;                         // proportional–integral–derivative controller (PID controller)
        private SensorLib.PID vPid;
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<SetPower> mMotorSteps;    // the motor steps we're guiding - assumed order is right ... left ...

        public SquirleyNavXStep(OpMode mode, float direction, HeadingSensor gyro, HeadingSensor velocity, SensorLib.PID gpid, SensorLib.PID vpid,
                                ArrayList<SetPower>motorSteps, float power){
            mOpMode = mode;
            mDirection = direction;
            mGyro = gyro;
            mVel = velocity;
            gPid = gpid;
            vPid = vpid;
            mMotorSteps = motorSteps;
            mPower = power;
        }

        public boolean loop(){
            super.loop();
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            }

            final float gError = SensorLib.Utils.wrapAngle(mGyro.getHeading());   // deviation from desired heading
            // deviations to left are positive, to right are negative

            final float vError = SensorLib.Utils.wrapAngle(mVel.getHeading() - mDirection);    // deviation from desired direction

            // compute delta time since last call -- used for integration time of PID step
            final double time = mOpMode.getRuntime();
            final double dt = time - mPrevTime;
            mPrevTime = time;

            // feed error through PID to get motor power correction value
            final float gCorrection = -gPid.loop(gError, (float) dt);
            final float vCorrection = -vPid.loop(vError, (float) dt);

            //calculate motor powers for ideal fancy wheels
            SquirrelyLib.MotorPowers mp = SquirrelyLib.GetSquirrelyWheelMotorPowers(mDirection);

            //calculate correction for each motors
            final float leftPower = gCorrection;
            final float rightPower = -gCorrection;
            final float frontPower = -vCorrection;
            final float backPower = vCorrection;

            final float[] motorPowers = {frontPower + rightPower + (float)mp.Front(),
                    backPower + rightPower + (float)mp.Back(),
                    frontPower + leftPower + (float)mp.Front(),
                    backPower + leftPower + (float)mp.Back()};

            final float scale = AutoLib.scaleMotorFactor(motorPowers);

            //set the powers
            mMotorSteps.get(0).setPower(motorPowers[0] * scale * mPower);
            mMotorSteps.get(1).setPower(motorPowers[1] * scale * mPower);
            mMotorSteps.get(2).setPower(motorPowers[2] * scale * mPower);
            mMotorSteps.get(3).setPower(motorPowers[3] * scale * mPower);

            //log some data
            if(mOpMode != null){
                mOpMode.telemetry.addData("Heading", mGyro.getHeading());
                mOpMode.telemetry.addData("Direction", mVel.getHeading());
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }
    }
}
