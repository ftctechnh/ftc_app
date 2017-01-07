package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
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
@Disabled
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
    SensorLib.PID mGPid;
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
    float Kp4 = 0.05f;
    float Ki4 = 0.00f;
    float Kd4 = 0;
    float Ki4Cutoff = 0.00f;

    //parameters for gyro PID, but cranked up
    float Kp5 = 0.1f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.05f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 3.0f;    // maximum angle error for which we update integrator

    final float ultraDistS1 = 20;
    final float ultraDistS2 = 13;

    //constants for pushy pushy
    final double pushPos = 1.0;
    final double pushyTime = 0.5;
    boolean red = false;
    final int colorThresh = 200;
    final float driveTime = 0.025f;
    final int driveLoopCount = 1;
    final float pushyPower = 0.2f;

    final int countPerRotation = 280;
    
    OpMode modePointer = this;
    
    LineDrive(OpMode mode, boolean redColor){
        if(mode != null) modePointer = mode;
        red = redColor;
    }

    @Override
    public void init() {
        //init hardware objects
        final boolean debug = false;
        robot = new BotHardware();
        robot.init(modePointer, debug);

        // create a PID controller for the sequence
        mPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);
        mgPid = new SensorLib.PID(Kp2, Ki2, Kd2, Ki2Cutoff);    // make the object that implements PID control algorithm
        mdPid = new SensorLib.PID(Kp3, Ki3, Kd3, Ki3Cutoff);
        muPid = new SensorLib.PID(Kp4, Ki4, Kd4, Ki4Cutoff);
        mGPid = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        final float power = 0.5f;
        initOpenCV();
        startCamera();

        //start up opencv

        //catch a frame
        Mat frame = getCameraFrame();

        final OpMode thing = modePointer;

        mSequence = new AutoLib.LinearSequence();

        mShoot = new AutoLib.LinearSequence();

        mShoot.add(new AutoLib.TimedMotorStep(robot.launcherMotor, 1.0, 0.6, true));
        mShoot.add(new AutoLib.TimedServoStep(robot.ballGate, 1.0, 0.2, false));
        mShoot.add(new AutoLib.TimedMotorStep(robot.lifterMotor, 1.0, 1.0, true));
        mShoot.add(new AutoLib.TimedMotorStep(robot.launcherMotor, 1.0, 0.6, true));

        mSequence.add(mShoot);

        mDrive = new AutoLib.LinearSequence();

        float time;

        if(red)
            time = 1.5f;
        else
            time = 2.0f;


        mDrive.add(new AutoLib.SquirrleyAzimuthTimedDriveStep(modePointer, 90, 0, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, time, false));

        int heading;
        int rightAngle;

        if(red){
            mDrive.add(new AutoLib.GyroTurnStep(modePointer, 180, robot.getNavXHeadingSensor(), robot.getMotorArray(), 0.3f, 3.0f, true));
            mDrive.add(new AutoLib.GyroTurnStep(modePointer, 180, robot.getNavXHeadingSensor(), robot.getMotorArray(), -0.2f, 3.0f, true));
            mDrive.add(new AutoLib.LogTimeStep(this, "YAY!", 0.1));

            heading = 180;
            rightAngle = -90;
        }
        else {
            heading = 0;
            rightAngle = 90;
        }

        mDrive.add(new AutoLib.SquirrleyAzimuthTimedDriveStep(modePointer, 0, heading, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, time - 0.5f, false));
        mDrive.add(new AutoLib.SquirrleyAzimuthFinDriveStep(modePointer, 0, heading, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, new UltraSensors(robot.distSensor, 60), true));
        mDrive.add(new UltraSquirrleyAzimuthFinDriveStep(modePointer, rightAngle, heading, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(23), mGPid, muPid, robot.getMotorArray(), 0.4f, new LineSensors(this, frame), true));

        mSequence.add(mDrive);

        mPushy = new AutoLib.LinearSequence();

        //two-stage line follow

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.initMotors(thing, debug);
            }
        }));


        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensor, ultraDistS1), mgPid, mdPid, robot.getMotorArray(), 0.1f, false));
        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensor, ultraDistS2), mgPid, mdPid, robot.getMotorArray(), 0.05f, true));

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.navX.zeroYaw();
            }
        }));

        //pushy pushy
        mPushy.add(new PushyLib.pushypushy(modePointer, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, pushyTime, red, colorThresh, pushyPower, driveTime, driveLoopCount));

        mPushy.add(new UltraSquirrleyAzimuthFinDriveStep(modePointer, rightAngle, 0, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(20), mPid, muPid, robot.getMotorArray(), 0.4f, new LineSensors(this, frame, 3000.0f), true));


        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.initMotors(thing, debug);
            }
        }));


        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensor, ultraDistS1), mgPid, mdPid, robot.getMotorArray(), 0.1f, false));
        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensor, ultraDistS2), mgPid, mdPid, robot.getMotorArray(), 0.05f, true));

        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.navX.zeroYaw();
            }
        }));

        //pushy pushy
        mPushy.add(new PushyLib.pushypushy(modePointer, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, pushyTime, red, colorThresh, pushyPower, driveTime, driveLoopCount));

        mPushy.add(new AutoLib.MoveByTimeStep(robot.getMotorArray(), -0.4, 1.0, true));

        mSequence.add(mPushy);

        // start out not-done
        bDone = false;
    }

    @Override
    public void init_loop(){
        modePointer.telemetry.addData("NavX Null", robot.navX == null);
        modePointer.telemetry.addData("NavX Ready", robot.startNavX());
    }

    @Override
    public void start(){
        robot.navX.zeroYaw();
    }

    @Override
    public void loop() {
        try{
            // until we're done, keep looping through the current Step(s)
            if (!bDone)
                bDone = mSequence.loop();       // returns true when we're done
            else
                modePointer.telemetry.addData("sequence finished", "");
        }
        catch (Exception e){
            telemetry.addData("Exception: ", e.getMessage());
            init();
        }
    }

    @Override
    public void stop() {
        super.stop();
        stopCamera();
    }

    static public class LineSensors implements HeadingSensor, DisplacementSensor, FinishSensor {

        OpenCVLib mMode;
        int lineCount;
        private double lastLinePos;
        private double lastLineHeading;
        private int yValStore[] = new int[3];
        float mWait;
        ElapsedTime mTime;

        LineSensors(OpenCVLib mode, Mat calib){
            mMode = mode;
            yValStore[0] = calib.rows() / 4;
            yValStore[1] = calib.rows() / 2;
            yValStore[2] = (calib.rows() * 3) / 4;
            reset();
        }

        LineSensors(OpenCVLib mode, Mat calib, float wait){
            mMode = mode;
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
            Mat frame = mMode.getCameraFrame();
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
            Mat frame = mMode.getCameraFrame();
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
                if (LineFollowLib.getDisplacment(mMode.getCameraFrame(), yValStore[2]) != LineFollowLib.ERROR_TOO_NOISY){
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

    public static class UltraSensors implements FinishSensor, DisplacementSensor {
        UltrasonicSensor mSensor;
        float mDist;
        float mHeading;

        UltraSensors(UltrasonicSensor sensor, float dist) {
            mSensor = sensor;
            mDist = dist;
        }

        public boolean checkStop() {
            //get ultrasonic distance
            double dist = mSensor.getUltrasonicLevel();

            //cutoff ridiculous values
            if (dist > 200 || dist < 5) return false;
                //now check if the robot is in range
            else return dist < mDist;
        }

        public float getDisp(){
            //get ultrasonic level
            float dist = (float)mSensor.getUltrasonicLevel();

            //cutoff ridiculous values
            if (dist > 200) return 100.0f - mDist;
            if (dist < 5) return 5.0f - mDist;
            return dist - mDist;
        }
    }

    private class UltraCorrectedDisplacement{
        private final float Kp = 0.3f;
        private float mDist;

        UltraCorrectedDisplacement(float dist) {
            mDist = dist;
        }

        public float getDisp(float angleError){
            float dist = (float)robot.distSensor.getUltrasonicLevel();

            modePointer.telemetry.addData("Ultra", dist);

            if(dist > 200) dist = 200;
            else if (dist < 5) dist = 5;

            //dist += angleError * Kp;

            return dist - mDist;
        }

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
                    AutoLib.DriveUntilStopMotorStep step = new AutoLib.DriveUntilStopMotorStep(em, 0, fin, stop);
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