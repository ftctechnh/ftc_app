package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.UserConfigurationType;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.libraries.interfaces.FinishSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.FunctionCall;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.LocationSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.SetPower;

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
        protected boolean firstLoopCall() {
            boolean flc = (mLoopCount == 1);    // assume this is called AFTER super.loop()
            mLoopCount++;
            return flc;
        }

        // run the next time-slice of the Step; return true when the Step is completed
        public boolean loop() {
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
                //mMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        double lastEncoder;
        OpMode mMode;

        public EncoderMotorStep(DcMotor motor, double power, int count, boolean stop) {
            mMode = null;
            mMotor = motor;
            mPower = power;
            mEncoderCount = count;
            mState = 0;
            mStop = stop;
        }

        public EncoderMotorStep(DcMotor motor, double power, int count, boolean stop, OpMode mode) {
            mMode = mode;
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
                mMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                lastEncoder = mMotor.getCurrentPosition();
                mMotor.setPower(mPower);
                mState++;
            }

            // the rest of the time, just update power and check to see if we're done
            done = Math.abs(mMotor.getCurrentPosition()-lastEncoder) >= mEncoderCount;
            if (done && mStop)
                mMotor.setPower(0);     // optionally stop motor when target reached
            else
                mMotor.setPower(mPower);        // update power in case it changed

            if(mMode != null){
                mMode.telemetry.addData("Motor Position", mMotor.getCurrentPosition());
                mMode.telemetry.addData("Amount Left", Math.abs(mMotor.getCurrentPosition()-lastEncoder));
            }

            if(done){
                //mMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            return done;
        }

    }

    // a Step that runs a DcMotor at a given power, for a given time
    static public class DriveUntilStopMotorStep extends AutoLib.Step implements SetPower {
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
            super.loop();
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
            this.preAdd(new SquirrelyLib.VuforiaSquirrelyGuideStep(mode, targetPosition, locSensor, yawSensor, steps, power, error));

        }

        // the base class loop function does all we need --
        // since the motors always return done, the composite step will return "done" when
        // the GuideStep says it's done, i.e. we've reached the target location.

    }

    // a Step that provides gyro-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps in order right ..., left ...
    // this step tries to turn the to a given angle by adjusting the left vs. right motors to change the robot's heading.
    static public class GyroTurnStep extends Step {
        private float mPowerMin;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mPowerMax;
        private float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
        private float mError;
        private boolean mStop;
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private DcMotor[] mMotors;            // the motor steps we're guiding - assumed order is right ... left ...
        private boolean mReversed;
        private final int mRightCount;
        private final SensorLib.PID errorPid;
        private double lastTime = 0;
        private int rightCount = 0;
        private boolean increment;
        private float startHeading;

        public GyroTurnStep(OpMode mode, float heading, HeadingSensor gyro,
                            DcMotor[] motors, float powerMin, float powerMax, SensorLib.PID errorPID, float error, int count, boolean stop, boolean reversed, boolean increment)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mMotors = motors;
            mPowerMin = powerMin;
            mPowerMax = powerMax;
            mError = error;
            mStop = stop;
            mRightCount = count;
            mReversed = reversed;
            this.errorPid = errorPID;
            this.increment = increment;
        }

        public GyroTurnStep(OpMode mode, float heading, HeadingSensor gyro,
                            DcMotor[] motors, float powerMin, float powerMax, SensorLib.PID errorPID, float error, int count, boolean stop)
        {
            this(mode, heading, gyro, motors,powerMin, powerMax, errorPID, error, count, stop, false, false);
        }

        public boolean loop()
        {
            super.loop();
            //check if heading exists
            float heading;
            try{
                heading = mGyro.getHeading();     // get latest reading from direction sensor
                // convention is positive angles CCW, wrapping from 359-0
                if(firstLoopCall()) startHeading = heading;
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

            float error;
            if(!increment) error = SensorLib.Utils.wrapAngle(heading-mHeading);   // deviation from desired heading
            else error = SensorLib.Utils.wrapAngle((heading - startHeading) - mHeading);
            // deviations to left are positive, to right are negative

            //check if turning has finshed
            if(Math.abs(error) < mError){
                if(mStop)
                    for(DcMotor em : mMotors)
                        em.setPower(0.0);
                if(++rightCount > mRightCount) return true;
            }
            else rightCount = 0;

            if(lastTime == 0) lastTime = mOpMode.getRuntime();
            double time = mOpMode.getRuntime();
            float pError = errorPid.loop(error, (float)(time - lastTime));
            lastTime = time;
            mOpMode.telemetry.addData("power error", pError);
            //cut out a middle range, but handle positive and negative
            float power;
            if(pError >= 0) power = Range.clip(mPowerMin + pError, mPowerMin, mPowerMax);
            else power = Range.clip(pError - mPowerMin, -mPowerMax, -mPowerMin);

            // compute new right/left motor powers
            float rightPower;
            float leftPower;
            if(!mReversed){
                rightPower = power;
                leftPower = -power;
            }
            else {
                rightPower = -power;
                leftPower = power;
            }

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
        private float powerMin;
        private float powerMax;

        public GyroGuideStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                             ArrayList<SetPower> motorsteps, float power)
        {
            this(mode, heading, gyro, pid, motorsteps, power, -1, 1);
        }

        public GyroGuideStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                             ArrayList<SetPower> motorsteps, float power, float powerMin, float powerMax)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mPid = pid;
            mMotorSteps = motorsteps;
            mPower = power;
            this.powerMin = powerMin;
            this.powerMax = powerMax;
        }

        public boolean loop()
        {
            super.loop();
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

            mOpMode.telemetry.addData("Correction", correction);

            // compute new right/left motor powers
            float rightPower = Range.clip(mPower + correction, this.powerMin, this.powerMax);
            float leftPower = Range.clip(mPower - correction, this.powerMin, this.powerMax);

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
            super.loop();
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
                                     DcMotor motors[], float power, float time, boolean stop, float powerMin, float powerMax)
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
            this.preAdd(new GyroGuideStep(mode, heading, gyro, pid, steps, power, powerMin, powerMax));

        }

        public AzimuthTimedDriveStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                     DcMotor motors[], float power, float time, boolean stop)
        {
            this(mode, heading, gyro, pid, motors, power, time, stop, -1, 1);
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.

    }

    // a Step that uses gyro input to drive along a given course for a given distance given by motor encoders.
    // uses a GyroGuideStep to adjust power to 2 or 4 motors.
    // assumes a robot with up to 4 drive motors in assumed order right motors, left motors
    static public class AzimuthCountedDriveStep extends ConcurrentSequence {

        public AzimuthCountedDriveStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                       DcMotor motors[], float power, int count, boolean stop, float powerMin, float powerMax)
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
            this.preAdd(new GyroGuideStep(mode, heading, gyro, pid, steps, power, powerMin, powerMax));

        }

        public AzimuthCountedDriveStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                                       DcMotor motors[], float power, int count, boolean stop)
        {
            this(mode, heading, gyro, pid, motors, power, count, stop, -1, 1);
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

        public TurnByTimeStep(DcMotor[] ray, double rightPower, double leftPower, double seconds, boolean stop)
        {
            if (ray[0] != null)
                this.add(new TimedMotorStep(ray[0], rightPower, seconds, stop));
            if (ray[1] != null)
                this.add(new TimedMotorStep(ray[1], rightPower, seconds, stop));
            if (ray[2] != null)
                this.add(new TimedMotorStep(ray[2], leftPower, seconds, stop));
            if (ray[3] != null)
                this.add(new TimedMotorStep(ray[3], leftPower, seconds, stop));
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

    public static class RunUntilStopStep extends Step {
        private Step[] test;
        public RunUntilStopStep(Step... testers) {
            this.test = testers;
        }

        public boolean loop() {
            boolean thing = false;
            for(Step t : test) thing |= t.loop();
            return thing;
        }

    }

    public static class RunCodeStep extends Step{
        FunctionCall funcPoint;

        public RunCodeStep(FunctionCall call){
            funcPoint = call;
        }

        public boolean loop(){
            super.loop();
            funcPoint.run();
            return true;
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

        public boolean isStarted(){
            return mStartTime != 0L;
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

        //uhhhhh no
        public void setMotorType(MotorConfigurationType type) {}

        public MotorConfigurationType getMotorType() { return null; }
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

    //function which takes four motor powers (presumably different ones) and returns a scale factor
    //such that Largest power * scale factor = 1.0
    //useful for when multiple PID machines are causing motors to stall
    static public float scaleMotorFactor(float[] ray){
        float maxVal = 0;
        for(int i = 0; i < ray.length; i++){
            if(Math.abs(ray[i]) > maxVal) maxVal = Math.abs(ray[i]);
        }
        if(maxVal == 0) return 1.0f;
        return 1.0f / maxVal;
    }
}



