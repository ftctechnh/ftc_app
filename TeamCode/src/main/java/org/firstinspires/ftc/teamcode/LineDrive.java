package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.opencv.core.Mat;

import java.util.ArrayList;


/**
 * simple example of using a Step that uses gyro input to drive along a given course for a given time
 * Created by phanau on 1/3/16 as SensorDriveTestOp, modified 1/22/16 to use Gyro, renamed GyroDriveTestOp 2/16/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Line Drive", group ="Line Follow")
//@Disabled
public class LineDrive extends OpenCVLib {
    AutoLib.Sequence mSequence;             // the root of the sequence tree
    AutoLib.Sequence mShoot;
    AutoLib.Sequence mDrive;
    AutoLib.Sequence mPushy;
    boolean bDone;                          // true when the programmed sequence is done
    BotHardware robot;                      // robot hardware object
    SensorLib.PID mPid;
    SensorLib.PID mgPid;                     // PID controllers for the sequence
    SensorLib.PID mdPid;
    SensorLib.PID muPid;
    // parameters of the PID controller for this sequence's first part
    float Kp = 0.05f;        // degree heading proportional term correction per degree of deviation
    float Ki = 0.02f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    // parameters of the PID controller for the heading of the line following
    float Kp2 = 0.500f;
    float Ki2 = 0.000f;
    float Kd2 = 0;
    float Ki2Cutoff = 0.0f;

    // parameters of the PID controller for the displacement of the line following
    float Kp3 = 2.50f;
    float Ki3 = 0.0f;
    float Kd3 = 0;
    float Ki3Cutoff = 0.00f;

    // parameters of the PID controller for the ultrasonic sensor driving
    float Kp4 = 0.125f;
    float Ki4 = 0.0f;
    float Kd4 = 0;
    float Ki4Cutoff = 0.00f;

    final float ultraDistS1 = 20;
    final float ultraDistS2 = 13;

    //constants for pushy pushy
    final double pushPos = 1.0;
    final double time = 1.0;
    final boolean red = false;
    final int colorThresh = 200;
    final float driveTime = 0.025f;
    final int driveLoopCount = 1;
    final float pushyPower = 0.2f;

    final int countPerRotation = 280;

    @Override
    public void init() {
        //init hardware objects
        final boolean debug = false;
        robot = new BotHardware();
        robot.init(this, debug);

        // create a PID controller for the sequence
        mPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);
        mgPid = new SensorLib.PID(Kp2, Ki2, Kd2, Ki2Cutoff);    // make the object that implements PID control algorithm
        mdPid = new SensorLib.PID(Kp3, Ki3, Kd3, Ki3Cutoff);
        muPid = new SensorLib.PID(Kp4, Ki4, Kd4, Ki4Cutoff);

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        final float power = 0.5f;
        initOpenCV();
        startCamera();

        //start up opencv

        //catch a frame
        Mat frame = getCameraFrame();

        final OpMode thing = this;

        mSequence = new AutoLib.LinearSequence();

        mShoot = new AutoLib.LinearSequence();

        mShoot.add(new AutoLib.TimedMotorStep(robot.launcherMotor, 1.0, 1.0, true));
        mShoot.add(new AutoLib.TimedMotorStep(robot.lifterMotor, 1.0, 1.0, true));
        mShoot.add(new AutoLib.TimedMotorStep(robot.launcherMotor, 1.0, 1.0, true));

        mSequence.add(mShoot);

        mDrive = new AutoLib.LinearSequence();

        mDrive.add(new SquirrleyAzimuthTimedDriveStep(this, 90, 0, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 2.0f, false));
        mDrive.add(new SquirrleyAzimuthTimedDriveStep(this, 0, 0, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 1.5f, false));
        mDrive.add(new SquirrleyAzimuthTimedDriveStep(this, 90, 0, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 0.5f, false));
        mDrive.add(new SquirrleyAzimuthFinDriveStep(this, 0, 0, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.3f, new UltraSensors(30), true));
        mDrive.add(new SquirrleyAzimuthFinDriveStep(this, 90, 0, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.3f, new LineSensors(frame), true));

        mSequence.add(mDrive);

        // create the root Sequence for this autonomous OpMode
        mPushy = new AutoLib.LinearSequence();

        //mSequence.add(new DriveUntilLine(robot.getMotorArray(), power + 0.1, true));
        //two-stage line follow

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.init(thing, debug);
            }
        }));

        mPushy.add(new LineDriveStep(this, 0, new LineSensors(frame), new LineSensors(frame), new UltraSensors(ultraDistS1), mgPid, mdPid, robot.getMotorArray(), 0.1f, false));
        mPushy.add(new LineDriveStep(this, 0, new LineSensors(frame), new LineSensors(frame), new UltraSensors(ultraDistS2), mgPid, mdPid, robot.getMotorArray(), 0.05f, true));

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.navX.zeroYaw();
            }
        }));

        //pushy pushy
        mPushy.add(new PushyLib.pushypushy(this, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, time, red, colorThresh, pushyPower, driveTime, driveLoopCount));

        mPushy.add(new UltraSquirrleyAzimuthFinDriveStep(this, 90, 0, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(22), mPid, muPid, robot.getMotorArray(), 0.4f, new LineSensors(frame, 3000.0f), true));

        //mPushy.add(new SquirrleyAzimuthTimedDriveStep(this, 120.0f, 0.0f, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 0.45f, false));
        //mPushy.add(new SquirrleyAzimuthTimedDriveStep(this, 90.0f, 0.0f, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 0.5f, false));
        //mPushy.add(new SquirrleyAzimuthFinDriveStep(this, 60.0f, 0.0f, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, new LineSensors(frame), true));

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.init(thing, debug);
            }
        }));

        mPushy.add(new LineDriveStep(this, 0, new LineSensors(frame), new LineSensors(frame), new UltraSensors(ultraDistS1), mgPid, mdPid, robot.getMotorArray(), 0.1f, false));
        mPushy.add(new LineDriveStep(this, 0, new LineSensors(frame), new LineSensors(frame), new UltraSensors(ultraDistS2), mgPid, mdPid, robot.getMotorArray(), 0.05f, true));
        //pushy pushy
        mPushy.add(new PushyLib.pushypushy(this, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, time, red, colorThresh, pushyPower, driveTime, driveLoopCount));

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.navX.zeroYaw();
            }
        }));

        mPushy.add(new SquirrleyAzimuthTimedDriveStep(this, 180.0f, 0.0f, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 0.5f, true));

        mSequence.add(mPushy);

        robot.startNavX();

        // start out not-done
        bDone = false;
    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("sequence finished", "");
    }

    @Override
    public void stop() {
        super.stop();
        stopCamera();
    }

    private class LineSensors implements HeadingSensor, DisplacementSensor, FinishSensor {

        int lineCount;
        private double lastLinePos;
        private double lastLineHeading;
        private int yValStore[] = new int[3];
        float mWait;
        ElapsedTime mTime;

        LineSensors(Mat calib){
            yValStore[0] = calib.rows() / 4;
            yValStore[1] = calib.rows() / 2;
            yValStore[2] = (calib.rows() * 3) / 4;
            reset();
        }

        LineSensors(Mat calib, float wait){
            yValStore[0] = calib.rows() / 4;
            yValStore[1] = calib.rows() / 2;
            yValStore[2] = (calib.rows() * 3) / 4;
            reset();
            mWait = wait;
        }

        public void reset(){
            lineCount = 0;
            lastLinePos = 0;
            lastLineHeading = 0;
            mWait = 0;
        }

        //heading sensor code for line following
        public float getHeading() {
            Mat frame = getCameraFrame();
            //get line displacement
            double linePos = -LineFollowLib.getAngle(frame, yValStore[0], yValStore[2]);
            //if it's an error, turn to where line was last
            if (linePos == LineFollowLib.ERROR_TOO_NOISY) {
                if (lastLineHeading == 0) return 0.0f;
                else if (lastLineHeading > 0) return 50.0f;
                else return -50.0f;
            } else lastLineHeading = linePos;

            return (float) linePos;
        }

        public float getDisp() {
            Mat frame = getCameraFrame();
            double linePos = -LineFollowLib.getDisplacment(frame, yValStore[1]);
            //if it's an error, turn to where line was last
            if (linePos == LineFollowLib.ERROR_TOO_NOISY) {
                if(lastLinePos == 0) return 0.0f;
                else if (lastLinePos > 0) return 0.8f;
                else return -0.8f;
            } else lastLinePos = linePos;
            return (float) linePos;
        }

        public boolean checkStop() {
            if(mWait == 0){
                if (LineFollowLib.getDisplacment(getCameraFrame(), yValStore[2]) != LineFollowLib.ERROR_TOO_NOISY){
                    //lineCount++;
                    return true;
                }
                //else lineCount = 0;
                //return lineCount >= 3;
                return false;
            }
            else {
                if(mTime == null) mTime = new ElapsedTime();
                else if(mTime.milliseconds() > mWait) mWait = 0;
                return false;
            }
        }
    }

    private class UltraSensors implements FinishSensor, DisplacementSensor {
        float mDist;
        float mHeading;

        UltraSensors(float dist) {
            mDist = dist;
        }

        public boolean checkStop() {
            //get ultrasonic distance
            double dist = robot.distSensor.getUltrasonicLevel();

            telemetry.addData("Ultra", dist);
            //cutoff ridiculous values
            if (dist > 200 || dist < 5) return false;
                //now check if the robot is in range
            else return dist < mDist;
        }

        public float getDisp(){
            //get ultrasonic level
            float dist = (float)robot.distSensor.getUltrasonicLevel();

            telemetry.addData("Ultra", dist);

            //cutoff ridiculous values
            if (dist > 200) return 100.0f - mDist;
            if (dist < 5) return 5.0f - mDist;
            return dist - mDist;
        }
    }

    private class UltraCorrectedDisplacement{
        private final float Kp = 1.0f/3.0f;
        private float mDist;

        UltraCorrectedDisplacement(float dist) {
            mDist = dist;
        }

        public float getDisp(float angleError){
            float dist = (float)robot.distSensor.getUltrasonicLevel();

            telemetry.addData("Ultra", dist);

            if(dist > 200) dist = 200;
            else if (dist < 5) dist = 5;

            dist += angleError * Kp;

            return dist - mDist;
        }

    }

    // a Step that provides gyro-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps in order right ..., left ...
    // this step tries to keep the robot on the given course by adjusting the left vs. right motors to change the robot's heading.
    static public class LineGuideStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private DisplacementSensor mDisp;
        private SensorLib.PID mgPid;                         // proportional–integral–derivative controller (PID controller)
        private SensorLib.PID mdPid;
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<AutoLib.SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public LineGuideStep(OpMode mode, float heading, HeadingSensor gyro, DisplacementSensor disp, SensorLib.PID gPid, SensorLib.PID dPid,
                             ArrayList<AutoLib.SetPower> motorsteps, float power) {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mDisp = disp;
            mgPid = gPid;
            mdPid = dPid;
            mMotorSteps = motorsteps;
            mPower = power;

            gPid.reset();
            dPid.reset();
        }

        public boolean loop() {
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            }

            float heading = mGyro.getHeading();     // get latest reading from direction sensor
            // convention is positive angles CCW, wrapping from 359-0

            float error = SensorLib.Utils.wrapAngle(heading - mHeading);   // deviation from desired heading
            // deviations to left are positive, to right are negative

            // compute delta time since last call -- used for integration time of PID step
            double time = mOpMode.getRuntime();
            double dt = time - mPrevTime;
            mPrevTime = time;

            // feed error through PID to get motor power correction value
            float gCorrection = -mgPid.loop(error, (float) dt);

            //feed other error through PID to get more values
            float dCorrection = -mdPid.loop(mDisp.getDisp(), (float) dt);

            // compute new right/left motor powers
            float frontRight = Range.clip(1 + gCorrection + dCorrection, -1, 1) * mPower;
            float backRight = Range.clip(1 + gCorrection - dCorrection, -1, 1) * mPower;
            float frontLeft = Range.clip(1 - gCorrection + dCorrection, -1, 1) * mPower;
            float backLeft = Range.clip(1 - gCorrection - dCorrection, -1, 1) * mPower;

            // set the motor powers -- handle both time-based and encoder-based motor Steps
            // assumed order is right motors followed by an equal number of left motors
            mMotorSteps.get(0).setPower(frontRight);
            mMotorSteps.get(1).setPower(backRight);
            mMotorSteps.get(2).setPower(frontLeft);
            mMotorSteps.get(3).setPower(backLeft);

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("left power ", frontLeft);
                mOpMode.telemetry.addData("right power ", frontRight);
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }
    }

    // a Step that uses Vuforia input to drive a normal wheel robot to a given position on the field.
    // uses a VuforiaGuideStep to adjust power to the 4 motors, assuming order fr, br, fl, bl.
    static public class LineDriveStep extends AutoLib.ConcurrentSequence {

        public LineDriveStep(OpMode mode, float heading, HeadingSensor gyro, DisplacementSensor disp, FinishSensor fin, SensorLib.PID gPid, SensorLib.PID dPid,
                             DcMotor[] motors, float power, boolean stop) {
            // add a concurrent Step to control each motor
            ArrayList<AutoLib.SetPower> steps = new ArrayList<AutoLib.SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    DriveUntilStopMotorStep step = new DriveUntilStopMotorStep(em, power, fin, stop);  // always set requested power and return "done"
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on camera input
            this.preAdd(new LineGuideStep(mode, heading, gyro, disp, gPid, dPid, steps, power));
        }

        // the base class loop function does all we need --
        // since the motors always return done, the composite step will return "done" when
        // the GuideStep says it's done, i.e. we've reached the target location.
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

    // a Step that uses gyro input to drive along a given course for a given distance given by motor encoders.
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
                    DriveUntilStopMotorStep step = new DriveUntilStopMotorStep(em, 0, fin, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new SquirrleyGuideStep(mode, direction, heading, gyro, pid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

    static public class UltraSquirrleyGuideStep extends AutoLib.Step {
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private float mDirection;                             // compass heading to steer for (-180 .. +180 degrees
        private float mHeading;
        private OpMode mOpMode;                             // needed so we can log output (may be null)
        private HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private UltraCorrectedDisplacement mUltra;
        private SensorLib.PID gPid;                         // proportional–integral–derivative controller (PID controller)
        private SensorLib.PID dPid;
        private double mPrevTime;                           // time of previous loop() call
        private ArrayList<AutoLib.SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public UltraSquirrleyGuideStep(OpMode mode, float direction, float heading, HeadingSensor gyro, UltraCorrectedDisplacement ultra, SensorLib.PID gpid,
                                       SensorLib.PID dpid, ArrayList<AutoLib.SetPower> motorsteps, float power) {
            mOpMode = mode;
            mDirection = direction;
            mHeading = heading;
            mGyro = gyro;
            mUltra = ultra;
            gPid = gpid;
            dPid = dpid;
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
            final float gCorrection = -gPid.loop(error, (float) dt);

            final float dCorrection = -dPid.loop(mUltra.getDisp(error), (float) dt);

            //calculate motor powers for fancy wheels
            AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(mDirection);

            final float leftPower = gCorrection;
            final float rightPower = -gCorrection;
            final float frontPower = -dCorrection;
            final float backPower = -dCorrection;

            //set the powers
            mMotorSteps.get(0).setPower(Range.clip(frontPower + rightPower + mp.Front(), -1, 1) * mPower);
            mMotorSteps.get(1).setPower(Range.clip(backPower + rightPower + mp.Back(), -1, 1) * mPower);
            mMotorSteps.get(2).setPower(Range.clip(frontPower + leftPower + mp.Front(), -1, 1) * mPower);
            mMotorSteps.get(3).setPower(Range.clip(backPower + leftPower + mp.Back(), -1, 1) * mPower);

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

    static public class UltraSquirrleyAzimuthTimedDriveStep extends AutoLib.ConcurrentSequence {

        public UltraSquirrleyAzimuthTimedDriveStep(OpMode mode, float direction, float heading, HeadingSensor gyro, UltraCorrectedDisplacement ultra, SensorLib.PID gpid, SensorLib.PID dpid,
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
            this.preAdd(new UltraSquirrleyGuideStep(mode, direction, heading, gyro, ultra, gpid, dpid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }

    static public class UltraSquirrleyAzimuthFinDriveStep extends AutoLib.ConcurrentSequence {

        public UltraSquirrleyAzimuthFinDriveStep(OpMode mode, float direction, float heading, HeadingSensor gyro, UltraCorrectedDisplacement ultra, SensorLib.PID gpid, SensorLib.PID dpid,
                                                   DcMotor motors[], float power, FinishSensor fin, boolean stop) {
            // add a concurrent Step to control each motor

            ArrayList<AutoLib.SetPower> steps = new ArrayList<AutoLib.SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    DriveUntilStopMotorStep step = new DriveUntilStopMotorStep(em, 0, fin, stop);
                    this.add(step);
                    steps.add(step);
                }

            // add a concurrent Step to control the motor steps based on gyro input
            this.preAdd(new UltraSquirrleyGuideStep(mode, direction, heading, gyro, ultra, gpid, dpid, steps, power));
        }

        // the base class loop function does all we need -- it will return "done" when
        // all the motors are done.
    }
}