package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;

/**
 * Created by Noah on 4/7/2018.
 */

// a Step that provides gyro-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
// assumes an even number of concurrent drive motor steps in order right ..., left ...
// this step tries to keep the robot on the given course by adjusting the left vs. right motors to change the robot's heading.
public class GyroCorrectStep extends AutoLib.Step {
    private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
    private final float startPower;
    private final float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
    private final OpMode mOpMode;                             // needed so we can log output (may be null)
    private final HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
    private final SensorLib.PID mPid;                         // proportional–integral–derivative controller (PID controller)
    private double mPrevTime;                           // time of previous loop() call
    private final DcMotor[] mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...
    private final float powerMin;
    private final float powerMax;
    private final float mError;

    public GyroCorrectStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                           DcMotor[] motorsteps, float power)
    {
        this(mode, heading, gyro, pid, motorsteps, power, -1, 1);
    }

    public GyroCorrectStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                           DcMotor[] motorsteps, float power, float powerMin, float powerMax)
    {
        this(mode, heading, gyro, pid, motorsteps, power, powerMin, powerMax, 1.0f);
    }

    public GyroCorrectStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                           DcMotor[] motorsteps, float power, float powerMin, float powerMax, float error) {
        mOpMode = mode;
        mHeading = heading;
        mGyro = gyro;
        mPid = pid;
        mMotorSteps = motorsteps;
        mPower = power;
        startPower = power;
        this.powerMin = powerMin;
        this.powerMax = powerMax;
        this.mError = error;
    }

    public boolean loop()
    {
        super.loop();
        // initialize previous-time on our first call -> dt will be zero on first call
        if (firstLoopCall()) {
            mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            mPid.reset();
        }

        float heading = mGyro.getHeading();     // get latest reading from direction sensor
        // convention is positive angles CCW, wrapping from 359-0

        float error = SensorLib.Utils.wrapAngle(heading-mHeading);   // deviation from desired heading
        // deviations to left are positive, to right are negative

        // compute delta time since last call -- used for integration time of PID step
        double time = mOpMode.getRuntime();
        double dt = time - mPrevTime;
        mPrevTime = time;

        // feed error through PID to get motor power correction value
        float correction = -mPid.loop(error, (float)dt);

        mOpMode.telemetry.addData("Correction", correction);

        // compute new right/left motor powers
        float rightPower;
        float leftPower;
        if(mPower >= 0) {
            rightPower = Range.clip(mPower + correction, this.powerMin, this.powerMax);
            leftPower = Range.clip(mPower - correction, this.powerMin, this.powerMax);
        }
        else {
            rightPower = Range.clip(mPower + correction, -this.powerMax, -this.powerMin);
            leftPower = Range.clip(mPower - correction, -this.powerMax, -this.powerMin);
        }

        // set the motor powers -- handle both time-based and encoder-based motor Steps
        // assumed order is right motors followed by an equal number of left motors
        int i = 0;
        for (DcMotor ms : mMotorSteps) {
            ms.setPower((i++ < mMotorSteps.length/2) ? rightPower : leftPower);
        }

        // log some data
        if (mOpMode != null) {
            mOpMode.telemetry.addData("heading ", heading);
            mOpMode.telemetry.addData("left power ", leftPower);
            mOpMode.telemetry.addData("right power ", rightPower);
        }

        return Math.abs(error) <= mError;
    }

    public float getPower() {
        return this.mPower;
    }

    public float getStartPower() {
        return this.startPower;
    }

    public void setPower(float power) {
        this.mPower = power;
    }

    public DcMotor[] getMotors() {
        return this.mMotorSteps;
    }

    public float getMinPower() {
        return this.powerMin;
    }

    public float getMaxPower() {
        return this.powerMax;
    }

    public float getHeading() {
        return this.mHeading;
    }

    public void reset() {
        this.mPower = startPower;
        super.mLoopCount = 0;
        this.mPid.reset();

    }
}