package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by phanau on 12/14/15.
 */

// a library of classes that support autonomous opmode programming
public class AutoLib {

    // the base class from which everything else derives.
    // each action in an autonomous sequence is a Step of some kind.
    // a Step may be simple (like run a Motor) or a composite of several Steps which
    // are either run sequentially or in parallel (see Sequences below).
    static public abstract class Step {

        int mLoopCount;     // keeps count of how many times loop() has been called on this Step

        protected Step() {
            mLoopCount = 0;
        }


        // returns true iff called from the first call to loop() on this Step
        boolean firstLoopCall() {
            boolean flc = (mLoopCount == 1);    // assume this is called AFTER super.loop()
            mLoopCount++;
            return flc;
        }

        // run the next time-slice of the Step; return true when the Step is completed
        boolean loop() {
            mLoopCount++;       // increment the loop counter
            return false;
        }

    }

    // ------------------ some implementations of Sequence constructs -------------------------

    // base class for Sequences that perform multiple Steps, either sequentially or concurrently
    static public abstract class Sequence extends Step {
        protected ArrayList<Step> mSteps;  // expandable array containing the Steps in the Sequence

        protected Sequence() {
            mSteps = new ArrayList<Step>(10);   // create the array with an initial capacity of 10
        }

        // add a Step to the Sequence
        public Step add(Step step) {
            mSteps.add(step);
            return this;        // allows daisy-chaining of calls
        }

        public Step preAdd(Step step){
            mSteps.add(0, step);
            return this;
        }

        // run the next time-slice of the Sequence; return true when the Sequence is completed.
        public boolean loop() {
            super.loop();
            return false;
        }

    }

    // a Sequence that performs its constituent Steps sequentially
    static public class LinearSequence extends Sequence {
        int mIndex;     // index of currently active Step

        public LinearSequence() {
            mIndex = 0;     // start at the beginning
        }

        // run the current Step of the Sequence until it completes, then the next Step and
        // the next, etc., etc. until the last Step completes, at which point the Sequence
        // returns complete.
        public boolean loop() {
            super.loop();
            if (mIndex < mSteps.size()) {       // if this Sequence is not completed ...
                if (mSteps.get(mIndex).loop())  // if this Step is complete, move to the next Step
                    mIndex++;
            }
            return (mIndex >= mSteps.size());   // return true when last Step completes
        }

    }


    // a Sequence that performs its constituent Steps concurrently
    static public class ConcurrentSequence extends Sequence {

        public ConcurrentSequence() {
        }

        // run all the Steps in the Sequence "concurrently" -- i.e. run the loop() function of
        // each of the Steps each time loop() is called. When ALL the Steps report that they
        // are done, then this Sequence is done.
        public boolean loop() {
            super.loop();
            boolean bDone = true;
            for (Step s : mSteps)
                bDone &= s.loop();      // "done" means ALL Steps are done
            return bDone;
        }

    }

    static public class DebugLinearSequence extends Sequence {
        OpMode mMode;
        int mIndex;
        boolean mDone = false;

        public DebugLinearSequence() {
            mIndex = 0;
            mMode = null;
        }

        public DebugLinearSequence(OpMode mode){
            mIndex = 0;
            mMode = mode;
        }

        public boolean loop(){
            super.loop();

            if(!mDone && mIndex < mSteps.size()) mDone = mSteps.get(mIndex).loop();

            if(mMode != null){
                mMode.telemetry.addData("Step #", mIndex);
                mMode.telemetry.addData("Step Done", mDone);
            }

            return mIndex >= mSteps.size();
        }

        public void incStep(){
            mDone = false;
            mIndex++;
        }
    }


    // ------------------ some implementations of primitive Steps ----------------------------

    // a simple Step that just logs its existence for a given number of loop() calls
    // really just for testing sequencer stuff, not for actual use on a robot.
    static public class LogCountStep extends Step {
        OpMode mOpMode;     // needed so we can log output
        String mName;       // name of the output field
        int mCount;         // current loop count of this Step

        public LogCountStep(OpMode opMode, String name, int loopCount) {
            mOpMode = opMode;
            mName = name;
            mCount = loopCount;
        }

        public boolean loop() {
            super.loop();

            // log some info about this Step
            if (mCount > 0) {
                mOpMode.telemetry.addData(mName, "count = " + mCount);
                mCount--;

                // wait a bit so we can see the displayed info ...
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            } else
                mOpMode.telemetry.addData(mName, "done");


            // return true when count is exhausted
            return (mCount <= 0);
        }

    }

    // a simple Step that just logs its existence for a given length of time
    static public class LogTimeStep extends Step {
        OpMode mOpMode;     // needed so we can log output
        String mName;       // name of the output field
        Timer mTimer;       // Timer for this Step

        public LogTimeStep(OpMode opMode, String name, double seconds) {
            mOpMode = opMode;
            mName = name;
            mTimer = new Timer(seconds);
        }

        public boolean loop() {
            super.loop();

            // start the Timer on our first call
            if (firstLoopCall())
                mTimer.start();

            // log some info about this Step every nth call (to not slow things down too much)
            if (!mTimer.done() && mLoopCount % 100 == 0)        // appears to cycle here at about 3ms/loop
                mOpMode.telemetry.addData(mName, "time = " + mTimer.remaining());
            if (mTimer.done())
                mOpMode.telemetry.addData(mName, "done");

            // return true when time is exhausted
            return (mTimer.done());
        }

    }

    // interface for setting the current power of either kind of MotorStep
    interface SetPower {
        public void setPower(double power);
    }

    // a Step that runs a DcMotor at a given power, for a given time
    static public class TimedMotorStep extends Step implements SetPower {
        Timer mTimer;
        DcMotor mMotor;
        double mPower;
        boolean mStop;          // stop motor when count is reached

        public TimedMotorStep(DcMotor motor, double power, double seconds, boolean stop) {
            mMotor = motor;
            mPower = power;
            mTimer = new Timer(seconds);
            mStop = stop;
        }

        // for dynamic adjustment of power during the Step
        public void setPower(double power) {
            mPower = power;
        }

        public boolean loop() {
            super.loop();

            // start the Timer and start the motor on our first call
            if (firstLoopCall()) {
                mTimer.start();
                mMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                mMotor.setPower(mPower);
            }

            // run the motor at the requested power until the Timer runs out
            boolean done = mTimer.done();
            if (done && mStop)
                mMotor.setPower(0);
            else
                mMotor.setPower(mPower);        // update power in case it changed

            return done;
        }

    }

    // a Step that runs a DcMotor at a given power, for a given encoder count
    static public class EncoderMotorStep extends Step implements SetPower {
        DcMotor mMotor;    // motor to control
        double mPower;          // power level to use
        int mEncoderCount;      // target encoder count
        int mState;             // internal state machine state
        boolean mStop;          // stop motor when count is reached

        public EncoderMotorStep(DcMotor motor, double power, int count, boolean stop) {
            mMotor = motor;
            mPower = power;
            mEncoderCount = count;
            mState = 0;
            mStop = stop;
        }

        // for dynamic adjustment of power during the Step
        public void setPower(double power) {
            mPower = power;
        }

        public boolean loop() {
            super.loop();

            boolean done = false;

            // we need a little state machine to make the encoders happy
            if (firstLoopCall()) {
                // set up the motor on our first call
                DcMotorSimple.Direction reverasl = mMotor.getDirection();
                mMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                mMotor.setDirection(reverasl);
                mMotor.setTargetPosition(mMotor.getCurrentPosition() + mEncoderCount);
                mMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                mMotor.setPower(mPower);
                mState++;
            }

            // the rest of the time, just update power and check to see if we're done
            done = !mMotor.isBusy();
            if (done && mStop)
                mMotor.setPower(0);     // optionally stop motor when target reached
            else
                mMotor.setPower(mPower);        // update power in case it changed

            return done;
        }

    }

    // a Step that runs a DcMotor at a given power, for a given time
    static public class DriveUntilStopMotorStep extends AutoLib.Step implements AutoLib.SetPower {
        FinishSensor mSensor;
        DcMotor mMotor;
        double mPower;
        boolean mStop;          // stop motor when count is reached

        public DriveUntilStopMotorStep(DcMotor motor, double power, FinishSensor sensor, boolean stop) {
            mMotor = motor;
            mPower = power;
            mSensor = sensor;
            mStop = stop;
        }

        // for dynamic adjustment of power during the Step
        public void setPower(double power) {
            mPower = power;
        }

        public boolean loop() {
            super.loop();

            // start the Timer and start the motor on our first call
            if (firstLoopCall()) {
                mMotor.setPower(mPower);
            }

            // run the motor at the requested power until the Timer runs out
            boolean done = mSensor.checkStop();
            if (done && mStop)
                mMotor.setPower(0);
            else
                mMotor.setPower(mPower);        // update power in case it changed

            return done;
        }
    }

    static public class SquirrleyGuideStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mDirection;                             // compass heading to steer for (-180 .. +180 degrees
        private float mHeading;
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private SensorLib.PID mPid;                         // proportional–integral–derivative controller (PID controller)
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<AutoLib.SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public SquirrleyGuideStep(OpMode mode, float direction, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                  ArrayList<AutoLib.SetPower> motorsteps, float power) {
            mOpMode = mode;
            mDirection = direction;
            mHeading = heading;
            mGyro = gyro;
            mPid = pid;
            mMotorSteps = motorsteps;
            mPower = power;
        }

        public boolean loop() {
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            }

            final float heading = mGyro.getHeading();     // get latest reading from direction sensor
            // convention is positive angles CCW, wrapping from 359-0

            final float error = SensorLib.Utils.wrapAngle(heading - mHeading);   // deviation from desired heading
            // deviations to left are positive, to right are negative

            // compute delta time since last call -- used for integration time of PID step
            final double time = mOpMode.getRuntime();
            final double dt = time - mPrevTime;
            mPrevTime = time;

            // feed error through PID to get motor power correction value
            final float correction = -mPid.loop(error, (float) dt);

            //calculate motor powers for fancy wheels
            AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(mDirection);

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

    // a Step that uses gyro input to drive in a given direction with the robot pointing in a given heading
    // uses a SquirleyGuideStep to adjust power to 2 or 4 motors.
    // assumes a robot with up to 4 drive motors in assumed order right motors, left motors
    static public class SquirrleyAzimuthTimedDriveStep extends AutoLib.ConcurrentSequence {

        public SquirrleyAzimuthTimedDriveStep(OpMode mode, float direction, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                              DcMotor motors[], float power, float time, boolean stop) {
            // add a concurrent Step to control each motor

            ArrayList<AutoLib.SetPower> steps = new ArrayList<AutoLib.SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.TimedMotorStep step = new AutoLib.TimedMotorStep(em, 0, time, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new SquirrleyGuideStep(mode, direction, heading, gyro, pid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

    static public class SquirrleyAzimuthFinDriveStep extends AutoLib.ConcurrentSequence {

        public SquirrleyAzimuthFinDriveStep(OpMode mode, float direction, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                            DcMotor motors[], float power, FinishSensor fin, boolean stop) {
            // add a concurrent Step to control each motor

            ArrayList<AutoLib.SetPower> steps = new ArrayList<AutoLib.SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.DriveUntilStopMotorStep step = new AutoLib.DriveUntilStopMotorStep(em, 0, fin, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new SquirrleyGuideStep(mode, direction, heading, gyro, pid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

    // a Step that provides Vuforia-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps assuming order fr, br, fl, bl driving Squirrely Wheels
    // (i.e. wheels that can move the robot in any direction without yawing the robot itself).
    // this step tries to keep the robot on course to a given location on the field.
    static public class VuforiaSquirrelyGuideStep extends Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...
        private VectorF mTargetPosition;                    // target position on field
        private LocationSensor mLocSensor;                  // sensor to use for field location information (e.g. Vuforia)
        private HeadingSensor mYawSensor;                   // sensor to use for robot orientation on field (may be same as LocationSensor)
        private float mError;                               // how close do we have to be to declare "done"

        public VuforiaSquirrelyGuideStep(OpMode mode, VectorF targetPosition, LocationSensor locSensor, HeadingSensor yawSensor,
                                         ArrayList<SetPower> motorSteps, float power, float error)
        {
            mOpMode = mode;
            mTargetPosition = targetPosition;
            mLocSensor = locSensor;
            mYawSensor = yawSensor;
            mMotorSteps = motorSteps;
            mPower = power;
            mError = error;
        }

        public boolean loop()
        {
            // compute absolute direction vector to target position on field
            VectorF position = mLocSensor.getLocation();
            if (position == null) {
                float searchPower = -0.1f;
                mMotorSteps.get(0).setPower(searchPower); //right
                mMotorSteps.get(1).setPower(searchPower);
                mMotorSteps.get(2).setPower(-searchPower); //left
                mMotorSteps.get(3).setPower(-searchPower);
                return false;       // not done
            }
            VectorF dirToTarget = mTargetPosition.subtracted(position);

            // compute absolute field heading to target: zero aligned with Y axis, positive CCW, degrees 0/359
            double headingToTarget = Math.atan2(-dirToTarget.get(0), dirToTarget.get(1));
            headingToTarget *= 180.0/Math.PI;       // to degrees

            // get current orientation of the robot on the field
            double robotYaw = mYawSensor.getHeading();

            // compute relative heading robot should move along
            double robotHeading = headingToTarget - robotYaw;

            // compute motor powers needed to go in that direction
            MotorPowers mp = GetSquirrelyWheelMotorPowers(robotHeading);
            double frontPower = Range.clip(mp.Front(), -mPower, mPower);
            double backPower = Range.clip(mp.Back(), -mPower, mPower);

            // reduce motor powers when we're very close to the target position
            //final double slowDist = 254.0;   // start slowing down when we're this close
            double distToTarget = dirToTarget.magnitude();
            //if (distToTarget < slowDist) {
            //    frontPower *= distToTarget/slowDist;
            //    backPower  *= distToTarget/slowDist;
            //}

            // output debug telemetry
            mOpMode.telemetry.addData("VSGS:", "abs heading: %4.1f  distance: %4.1f", headingToTarget, distToTarget);

            // update motors
            // assumed order is fr, br, fl, bl
            mMotorSteps.get(0).setPower(frontPower);
            mMotorSteps.get(1).setPower(backPower);
            mMotorSteps.get(2).setPower(frontPower);
            mMotorSteps.get(3).setPower(backPower);

            // are we there yet?
            boolean bDone = distToTarget < mError;     // within an inch of target position?

            if(bDone){
                double searchPower = 0.0;
                mMotorSteps.get(0).setPower(searchPower); //right
                mMotorSteps.get(1).setPower(searchPower);
                mMotorSteps.get(2).setPower(searchPower); //left
                mMotorSteps.get(3).setPower(searchPower);
            }

            return bDone;
        }
    }

    // a Step that uses Vuforia input to drive a SquirrelyWheel robot to a given position on the field.
    // uses a VuforiaSquirrelyGuideStep to adjust power to the 4 motors, assuming order fr, br, fl, bl.
    static public class VuforiaSquirrelyDriveStep extends ConcurrentSequence {

        public VuforiaSquirrelyDriveStep(OpMode mode, VectorF targetPosition, LocationSensor locSensor, HeadingSensor yawSensor,
                                         DcMotor motors[], float power, float error)
        {
            // add a concurrent Step to control each motor
            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    TimedMotorStep step = new TimedMotorStep(em, power, 0, false);  // always set requested power and return "done"
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on Vuforia input
            this.preAdd(new VuforiaSquirrelyGuideStep(mode, targetPosition, locSensor, yawSensor, steps, power, error));

        }

        // the base class loop function does all we need --
        // since the motors always return done, the composite step will return "done" when
        // the GuideStep says it's done, i.e. we've reached the target location.

    }

    // a Step that provides Vuforia-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps assuming order fr, br, fl, bl driving normal wheels
    // this step tries to keep the robot on course to a given location on the field.
    static public class VuforiaGuideStep extends Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...
        private VectorF mTargetPosition;                    // target position on field
        private LocationSensor mLocSensor;                  // sensor to use for field location information (e.g. Vuforia)
        private HeadingSensor mYawSensor;                   // sensor to use for robot orientation on field (may be same as LocationSensor)
        private float mError;                               // how close do we have to be to declare "done"

        public VuforiaGuideStep(OpMode mode, VectorF targetPosition, LocationSensor locSensor, HeadingSensor yawSensor,
                                         ArrayList<SetPower> motorSteps, float power, float error)
        {
            mOpMode = mode;
            mTargetPosition = targetPosition;
            mLocSensor = locSensor;
            mYawSensor = yawSensor;
            mMotorSteps = motorSteps;
            mPower = power;
            mError = error;
        }

        public boolean loop()
        {
            // compute absolute direction vector to target position on field
            VectorF position = mLocSensor.getLocation();
            if (position == null){
                mMotorSteps.get(0).setPower(0.2); //fr
                mMotorSteps.get(1).setPower(0.2); //br
                mMotorSteps.get(2).setPower(-0.2); //fl
                mMotorSteps.get(3).setPower(-0.2); //bl
                return false;       // not done
            }

            VectorF dirToTarget = mTargetPosition.subtracted(position);

            // compute absolute field heading to target: zero aligned with Y axis, positive CCW, degrees 0/359
            double headingToTarget = Math.atan2(-dirToTarget.get(0), dirToTarget.get(1));
            headingToTarget *= 180.0/Math.PI;       // to degrees

            // get current orientation of the robot on the field
            double robotYaw = mYawSensor.getHeading();

            // compute relative heading robot should move along
            double robotHeading = headingToTarget - robotYaw;

            // try to keep the robot facing toward the corner between the sides of the fields with Vuforia targets --
            // if headingToTarget is away from that corner of the field, run backwards to the target; otherwise forwards.
                                        // TBD ...

            // compute motor powers needed to go in that direction
            double leftPower = 0;       // TBD ....
            double rightPower = 0;      // TBD ....

            // reduce motor powers when we're very close to the target position
            final double slowDist = 254.0;   // start slowing down when we're this close (10")
            double distToTarget = dirToTarget.magnitude();
            if (distToTarget < slowDist) {
                leftPower *= distToTarget/slowDist;
                rightPower  *= distToTarget/slowDist;
            }

            // output debug telemetry
            mOpMode.telemetry.addData("VGS:", "abs heading: %4.1f  distance: %4.1f", headingToTarget, distToTarget);

            // update motors
            // assumed order is fr, br, fl, bl
            mMotorSteps.get(0).setPower(rightPower);
            mMotorSteps.get(1).setPower(rightPower);
            mMotorSteps.get(2).setPower(leftPower);
            mMotorSteps.get(3).setPower(leftPower);

            // are we there yet?
            boolean bDone = distToTarget < mError;     // within an inch of target position?
            return bDone;
        }
    }

    // a Step that uses Vuforia input to drive a normal wheel robot to a given position on the field.
    // uses a VuforiaGuideStep to adjust power to the 4 motors, assuming order fr, br, fl, bl.
    static public class VuforiaDriveStep extends ConcurrentSequence {

        public VuforiaDriveStep(OpMode mode, VectorF targetPosition, LocationSensor locSensor, HeadingSensor yawSensor,
                                         DcMotor motors[], float power, float error)
        {
            // add a concurrent Step to control each motor
            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    TimedMotorStep step = new TimedMotorStep(em, power, 0, false);  // always set requested power and return "done"
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on Vuforia input
            this.preAdd(new VuforiaSquirrelyGuideStep(mode, targetPosition, locSensor, yawSensor, steps, power, error));

        }

        // the base class loop function does all we need --
        // since the motors always return done, the composite step will return "done" when
        // the GuideStep says it's done, i.e. we've reached the target location.

    }

    // a Step that provides gyro-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps in order right ..., left ...
    // this step tries to turn the to a given angle by adjusting the left vs. right motors to change the robot's heading.
    static public class GyroTurnStep extends Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
        private float mError;
        private boolean mStop;
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private DcMotor[] mMotors;            // the motor steps we're guiding - assumed order is right ... left ...

        public GyroTurnStep(OpMode mode, float heading, HeadingSensor gyro,
                             DcMotor[] motors, float power, float error, boolean stop)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mMotors = motors;
            mPower = power;
            mError = error;
            mStop = stop;
        }

        public boolean loop()
        {
            //check if heading exists
            float heading;
            try{
                heading = mGyro.getHeading();     // get latest reading from direction sensor
                // convention is positive angles CCW, wrapping from 359-0
            }
            //and if not run search code
            catch (NullPointerException e){
                mMotors[0].setPower(0.2); //fr
                mMotors[1].setPower(0.2); //br
                mMotors[2].setPower(-0.2); //fl
                mMotors[3].setPower(-0.2); //bl

                //log some data
                if(mOpMode != null)
                    mOpMode.telemetry.addData("heading ", "Null");

                return false;       // not done
            }

            float dist = SensorLib.Utils.wrapAngle(heading-mHeading);   // deviation from desired heading
            // deviations to left are positive, to right are negative

            //check if turning has finshed
            if(Math.abs(dist) < mError){
                if(mStop) {
                    for(DcMotor em : mMotors)
                        em.setPower(0.0);
                }
                return true;
            }

            float power = Range.clip(dist / 45.0f, -mPower, mPower);

            // compute new right/left motor powers
            float rightPower = power;
            float leftPower = -power;

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("left power ", leftPower);
                mOpMode.telemetry.addData("right power ", rightPower);
            }

            // set the motor powers -- handle both time-based and encoder-based motor Steps
            // assumed order is right motors followed by an equal number of left motors
            int i = 0;
            for (DcMotor ms : mMotors) {
                ms.setPower((i++ < mMotors.length/2) ? rightPower : leftPower);
            }

            return false;
        }
    }

    // a Step that provides gyro-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps in order right ..., left ...
    // this step tries to keep the robot on the given course by adjusting the left vs. right motors to change the robot's heading.
    static public class GyroGuideStep extends Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private SensorLib.PID mPid;                         // proportional–integral–derivative controller (PID controller)
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public GyroGuideStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                             ArrayList<SetPower> motorsteps, float power)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mPid = pid;
            mMotorSteps = motorsteps;
            mPower = power;
        }

        public boolean loop()
        {
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
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

            // compute new right/left motor powers
            float rightPower = Range.clip(mPower + correction, -1, 1);
            float leftPower = Range.clip(mPower - correction, -1, 1);

            // set the motor powers -- handle both time-based and encoder-based motor Steps
            // assumed order is right motors followed by an equal number of left motors
            int i = 0;
            for (SetPower ms : mMotorSteps) {
                ms.setPower((i++ < mMotorSteps.size()/2) ? rightPower : leftPower);
            }

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("left power ", leftPower);
                mOpMode.telemetry.addData("right power ", rightPower);
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }
    }

    //a step which runs a servo for a given amount of time
    static public class TimedServoStep extends Step{
        Servo mServo;
        double mPosition;
        Timer mTimer;
        boolean mRewind;
        private double lastPosition;
        OpMode mMode;

        public TimedServoStep(Servo servo, double position, double time, boolean rewind){
            mServo = servo;
            mPosition = position;
            mTimer = new Timer(time);
            mRewind = rewind;
        }

        public boolean loop(){
            if(firstLoopCall()){
                lastPosition = mServo.getPosition();
                mServo.setPosition(mPosition);
                mTimer.start();
            }

            if(mTimer.done()){
                //RobotLog.vv("AutoLib", "Timer: " + mTimer.elapsed());
                if(mRewind){
                    mRewind = false;
                    mServo.setPosition(lastPosition);
                    mTimer.start();
                    //RobotLog.vv("AutoLib", "MRewind Called!");
                }
                else return true;
            }

            return false;
        }
    }

    // a Step that uses gyro input to drive along a given course for a given distance given by motor encoders.
    // uses a GyroGuideStep to adjust power to 2 or 4 motors.
    // assumes a robot with up to 4 drive motors in assumed order right motors, left motors
    static public class AzimuthTimedDriveStep extends ConcurrentSequence {

        public AzimuthTimedDriveStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                     DcMotor motors[], float power, float time, boolean stop)
        {
            // add a concurrent Step to control each motor
            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    TimedMotorStep step = new TimedMotorStep(em, power, time, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new GyroGuideStep(mode, heading, gyro, pid, steps, power));

        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.

    }

    // a Step that uses gyro input to drive along a given course for a given distance given by motor encoders.
    // uses a GyroGuideStep to adjust power to 2 or 4 motors.
    // assumes a robot with up to 4 drive motors in assumed order right motors, left motors
    static public class AzimuthCountedDriveStep extends ConcurrentSequence {

        public AzimuthCountedDriveStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                       DcMotor motors[], float power, int count, boolean stop)
        {
            // add a concurrent Step to control each motor
            ArrayList<SetPower> steps = new ArrayList<SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    EncoderMotorStep step = new EncoderMotorStep(em, power, count, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new GyroGuideStep(mode, heading, gyro, pid, steps, power));

        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.

    }


    // some convenience utility classes for common operations

    // a Sequence that moves an up-to-four-motor robot in a straight line with given power for given time
    static public class MoveByTimeStep extends ConcurrentSequence {

        public MoveByTimeStep(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, double power, double seconds, boolean stop)
        {
            if (fr != null)
                this.add(new TimedMotorStep(fr, power, seconds, stop));
            if (br != null)
                this.add(new TimedMotorStep(br, power, seconds, stop));
            if (fl != null)
                this.add(new TimedMotorStep(fl, power, seconds, stop));
            if (bl != null)
                this.add(new TimedMotorStep(bl, power, seconds, stop));
        }

        public MoveByTimeStep(DcMotor motors[], double power, double seconds, boolean stop)
        {
            for (DcMotor em : motors)
                if (em != null)
                    this.add(new TimedMotorStep(em, power, seconds, stop));
        }

    }


    // a Sequence that turns an up-to-four-motor robot by applying the given right and left powers for given time
    static public class TurnByTimeStep extends ConcurrentSequence {

        public TurnByTimeStep(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, double rightPower, double leftPower, double seconds, boolean stop)
        {
            if (fr != null)
                this.add(new TimedMotorStep(fr, rightPower, seconds, stop));
            if (br != null)
                this.add(new TimedMotorStep(br, rightPower, seconds, stop));
            if (fl != null)
                this.add(new TimedMotorStep(fl, leftPower, seconds, stop));
            if (bl != null)
                this.add(new TimedMotorStep(bl, leftPower, seconds, stop));
        }

    }


    // a Sequence that moves an up-to-four-motor robot in a straight line with given power for given encoder count
    static public class MoveByEncoderStep extends ConcurrentSequence {

        public MoveByEncoderStep(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, double power, int count, boolean stop)
        {
            if (fr != null)
                this.add(new EncoderMotorStep(fr, power, count, stop));
            if (br != null)
                this.add(new EncoderMotorStep(br, power, count, stop));
            if (fl != null)
                this.add(new EncoderMotorStep(fl, power, count, stop));
            if (bl != null)
                this.add(new EncoderMotorStep(bl, power, count, stop));
        }

        public MoveByEncoderStep(DcMotor motors[], double power, int count, boolean stop)
        {
            for (DcMotor em : motors)
                if (em != null)
                    this.add(new EncoderMotorStep(em, power, count, stop));
        }

    }


    // a Sequence that turns an up-to-four-motor robot by applying the given right and left powers for given right and left encoder counts
    static public class TurnByEncoderStep extends ConcurrentSequence {

        public TurnByEncoderStep(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, double rightPower, double leftPower, int rightCount, int leftCount, boolean stop)
        {
            if (fr != null)
                this.add(new EncoderMotorStep(fr, rightPower, rightCount, stop));
            if (br != null)
                this.add(new EncoderMotorStep(br, rightPower, rightCount, stop));
            if (fl != null)
                this.add(new EncoderMotorStep(fl, leftPower, leftCount, stop));
            if (bl != null)
                this.add(new EncoderMotorStep(bl, leftPower, leftCount, stop));
        }

    }

    //simple interface to run step based on function pointer

    public interface FunctionCall {
        void run();
    }

    public static class RunCodeStep extends Step{
        FunctionCall funcPoint;

        RunCodeStep(FunctionCall call){
            funcPoint = call;
        }

        public boolean loop(){
            funcPoint.run();
            return true;
        }
    }

    // linear interpolation
    private static double lerp (double x, double x0, double x1, double y0, double y1)
    {
        return ((x-x0)/(x1-x0))*(y1-y0) + y0;
    }

    // some utilities to support "Squirrely Wheels" that move the robot sideways
    // when front and back wheels turn in opposite directions

    // example of a class used to return multiple values from a function call
    public static class MotorPowers {
        public double mFront;
        public double mBack;
        public MotorPowers(double front, double back) {
            mFront = front;
            mBack = back;
        }
        public double Front() { return mFront; }
        public double Back() { return mBack; }
    }

    // this function computes the relative front/back power settings needed to move along a given
    // heading, relative to the current orientation of the robot.
    public static MotorPowers GetSquirrelyWheelMotorPowers(
            double heading    // in degrees, zero = straight ahead, positive CCW, range +-180
    )
    {
        // wrap heading around to acceptable range
        heading = SensorLib.Utils.wrapAngle(heading);

        // compute front and back wheel relative speeds needed to go in desired direction
        double front = 0.0f;
        double back = 0.0f;
        if (heading < 0) {
            if (heading > -90) {
                front = 1.0;
                back = lerp(heading, 0, -90, 1, -1);
            } else {
                front = lerp(heading, -90, -180, 1, -1);
                back = -1.0;
            }
        }
        else {
            if (heading < 90) {
                front = lerp(heading, 0, 90, 1, -1);
                back = 1.0;
            }
            else {
                front = -1.0;
                back = lerp(heading, 90, 180, 1, -1);
            }
        }

        // return results
        return new MotorPowers(front, back);
    }

    // a Step that drives 4 "squirrely wheels" to move the robot in a given direction
    // relative to where it's facing, for a given time.
    static public class MoveSquirrelyByTimeStep extends ConcurrentSequence {

        public MoveSquirrelyByTimeStep(
                DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl,     // motors
                double heading,    // in degrees, zero = straight ahead, positive CCW, range +-180
                double power,      // overall power scaling factor
                double seconds,    // time in seconds
                boolean stop)
        {
            _MoveSquirrelyByTimeStep(fr, br, fl, bl, heading, power, seconds, stop);
        }

        public MoveSquirrelyByTimeStep(
                DcMotor motors[],  // motors -- assumed order is fr, br, fl, bl
                double heading,    // in degrees, zero = straight ahead, positive CCW, range +-180
                double power,      // overall power scaling factor
                double seconds,    // time in seconds
                boolean stop)
        {
            _MoveSquirrelyByTimeStep(motors[0], motors[1], motors[2], motors[3], heading, power, seconds, stop);
        }


        // internal function that actually does the constructor stuff for the two different ctors
        protected void _MoveSquirrelyByTimeStep(
                DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl,     // motors
                double heading,    // in degrees, zero = straight ahead, positive CCW, range +-180
                double power,      // overall power scaling factor
                double seconds,    // time in seconds
                boolean stop)
        {
            // compute relative front and back motor powers needed to move on the desired heading
            MotorPowers mp = GetSquirrelyWheelMotorPowers(heading);

            // create TimedMotorSteps to control the 4 motors
            if (fr != null)
                this.add(new TimedMotorStep(fr, mp.Front()*power, seconds, stop));
            if (br != null)
                this.add(new TimedMotorStep(br, mp.Back()*power, seconds, stop));
            if (fl != null)
                this.add(new TimedMotorStep(fl, mp.Front()*power, seconds, stop));
            if (bl != null)
                this.add(new TimedMotorStep(bl, mp.Back()*power, seconds, stop));
        }
    }


    // timer
    static public class Timer {
        long mStartTime;
        double mSeconds;

        public Timer(double seconds) {
            mStartTime = 0L;        // creation time is NOT start time
            mSeconds = seconds;
        }

        public void start() {
            mStartTime = System.nanoTime();
        }

        // return elapsed time in seconds since timer was created or restarted
        public double elapsed() {
            double ret = (double) (System.nanoTime() - mStartTime) / (double) TimeUnit.SECONDS.toNanos(1L);
            if(ret > 1000) return 0.0;
            else return ret;
        }

        public double remaining() {
            return mSeconds - elapsed();
        }

        public boolean done() {
            return (remaining() <= 0);
        }
    }

    // test hardware classes -- useful for testing when no hardware is available.
    // these are primarily intended for use in testing autonomous mode code, but could
    // also be useful for testing tele-operator modes.

    // a dummy DcMotor that just logs commands we send to it --
    // useful for testing Motor code when you don't have real hardware handy
    static public class TestMotor implements DcMotor {
        OpMode mOpMode;     // needed for logging data
        String mName;       // string id of this motor
        double mPower;      // current power setting
        DcMotor.RunMode mMode;
        int mTargetPosition;
        int mCurrentPosition;
        boolean mPowerFloat;
        Direction mDirection;
        ZeroPowerBehavior mZeroPowerBehavior;
        int mMaxSpeed;

        public TestMotor(String name, OpMode opMode) {
            super();     // init base class (real DcMotor) with dummy data
            mOpMode = opMode;
            mName = name;
            mPower = 0.0;
            mMaxSpeed = 0;
            mMode = DcMotor.RunMode.RUN_WITHOUT_ENCODERS;
            mTargetPosition = 0;
            mCurrentPosition = 0;
            mPowerFloat = false;
            mDirection = Direction.FORWARD;
            mZeroPowerBehavior = ZeroPowerBehavior.FLOAT;
        }

        @Override       // override all the functions of the real DcMotor class that touch hardware
        public void setPower(double power) {
            mPower = power;
            mOpMode.telemetry.addData(mName, " power: " + String.valueOf(mPower));
        }

        public double getPower() {
            return mPower;
        }

        public void close() {
            mOpMode.telemetry.addData(mName, " close();");
        }

        public boolean isBusy() {
            return false;
        }

        public void setPowerFloat() {
            mPowerFloat = true;
            mOpMode.telemetry.addData(mName, " setPowerFloat();");
        }

        public boolean getPowerFloat() {
            return mPowerFloat;
        }

        public void setMaxSpeed(int encoderTicksPerSecond)
        {
            mMaxSpeed = encoderTicksPerSecond;
            mOpMode.telemetry.addData(mName, "maxSpeed: " + String.valueOf(encoderTicksPerSecond));
        }

        public int getMaxSpeed() { return mMaxSpeed; }

        public void setTargetPosition(int position) {
            mTargetPosition = position;
            mOpMode.telemetry.addData(mName, "target: " + String.valueOf(position));
        }

        public int getTargetPosition() {
            return mTargetPosition;
        }

        public int getCurrentPosition() {
            return mTargetPosition;
        }

        public void setMode(DcMotor.RunMode mode) {
            this.mMode = mode;
            mOpMode.telemetry.addData(mName, "run mode: " + String.valueOf(mode));
        }

        public DcMotor.RunMode getMode() {
            return this.mMode;
        }

        public void setDirection(Direction direction)
        {
            mDirection = direction;
            mOpMode.telemetry.addData(mName, "direction: " + String.valueOf(direction));
        }

        public Direction getDirection() { return mDirection; }

        public String getConnectionInfo() {
            return mName + " port: unknown";
        }

        public DcMotorController getController()
        {
            return null;
        }

        public void resetDeviceConfigurationForOpMode() {
            mOpMode.telemetry.addData(mName, "resetDeviceConfigurationForOpMode: ");
        }

        public int getPortNumber()
        {
            return 0;
        }

        public ZeroPowerBehavior getZeroPowerBehavior()
        {
            return mZeroPowerBehavior;
        }

        public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior)
        {
            mZeroPowerBehavior = zeroPowerBehavior;
            mOpMode.telemetry.addData(mName, "zeroPowerBehavior: " + String.valueOf(zeroPowerBehavior));
        }

        public int getVersion() { return 0; }

        public HardwareDevice.Manufacturer getManufacturer() { return Manufacturer.Unknown; }

        public String getDeviceName() { return "AutoLib_TestMotor: " + mName; }


    }

    // a dummy Servo that just logs commands we send to it --
    // useful for testing Servo code when you don't have real hardware handy
    static public class TestServo implements Servo {
        OpMode mOpMode;     // needed for logging data
        String mName;       // string id of this servo
        double mPosition;   // current target position
        Direction mDirection;
        double mScaleMin;
        double mScaleMax;

        public TestServo(String name, OpMode opMode) {
            super();     // init base class (real DcMotor) with dummy data
            mOpMode = opMode;
            mName = name;
            mPosition = 0.0;
        }

        @Override       // this function overrides the setPower() function of the real DcMotor class
        public void setPosition(double position) {
            mPosition = position;
            mOpMode.telemetry.addData(mName, " position: " + String.valueOf(mPosition));
            mDirection = Direction.FORWARD;
        }

        @Override       // this function overrides the getPower() function of the real DcMotor class
        public double getPosition() {
            return mPosition;
        }

        @Override       // override all other functions of Servo that touch the hardware
        public String getConnectionInfo() {
            return mName + " port: unknown";
        }

        public HardwareDevice.Manufacturer getManufacturer() { return Manufacturer.Unknown; }

        public void resetDeviceConfigurationForOpMode() {
            mOpMode.telemetry.addData(mName, "resetDeviceConfigurationForOpMode: ");
        }

        public Servo.Direction getDirection() { return mDirection; }
        public void setDirection(Servo.Direction direction) {
            mDirection = direction;
            mOpMode.telemetry.addData(mName, "direction: " + String.valueOf(mDirection));
        }

        public void scaleRange(double min, double max)
        {
            mScaleMin = min;
            mScaleMax = max;
        }

        public ServoController getController() { return null; }
        public String getDeviceName() { return "AutoLib_TestServo: " + mName; }
        public int getPortNumber()
        {
            return 0;
        }
        public int getVersion() { return 0; }

        public void close() {}

    }

    // define interface to Factory that creates various kinds of hardware objects
    static public interface HardwareFactory {
        public DcMotor getDcMotor(String name);
        public Servo getServo(String name);
    }

    // this implementation generates test-hardware objects that just log data
    static public class TestHardwareFactory implements HardwareFactory {
        OpMode mOpMode;     // needed for logging data

        public TestHardwareFactory(OpMode opMode) {
            mOpMode = opMode;
        }

        public DcMotor getDcMotor(String name){
            return new TestMotor(name, mOpMode);
        }

        public Servo getServo(String name){
            return new TestServo(name, mOpMode);
        }
    }

    // this implementation gets real hardware objects from the hardwareMap of the given OpMode
    static public class RealHardwareFactory implements HardwareFactory {
        OpMode mOpMode;     // needed for access to hardwareMap

        public RealHardwareFactory(OpMode opMode) {
            mOpMode = opMode;
        }

        public DcMotor getDcMotor(String name){
            DcMotor motor = null;
            try {
                motor = mOpMode.hardwareMap.dcMotor.get(name);
            }
            catch (Exception e) {
                // okay -- just return null (absent) for this motor
            }

            // just to make sure - a previous OpMode may have set it differently ...
            if (motor != null)
                motor.setDirection(DcMotor.Direction.FORWARD);

            return motor;
        }

        public Servo getServo(String name){
            Servo servo = null;
            try {
                servo = mOpMode.hardwareMap.servo.get(name);
            }
            catch (Exception e) {
                // okay - just return null (absent) for this servo
            }

            // just to make sure - a previous OpMode may have set it differently ...
            if (servo != null)
                servo.setDirection(Servo.Direction.FORWARD);
            
            return servo;
        }
    }

}



