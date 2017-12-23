package org.firstinspires.ftc.teamcode.opmodes.outdated;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libraries.SquirrelyLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.FunctionCall;
import org.firstinspires.ftc.teamcode.libraries.interfaces.SetPower;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.LineFollowLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.firstinspires.ftc.teamcode.libraries.PushyLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.DisplacementSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.FinishSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.opencv.core.Mat;

import java.util.ArrayList;


//I'm just going to pretend I am proud of this

@Autonomous(name="Line Drive", group ="Line Follow")
@Disabled
public class LineDrive extends OpenCVLib {
    AutoLib.Sequence mSequence;             // the root of the sequence tree
    AutoLib.Sequence mShoot;
    AutoLib.Sequence mDrive;
    AutoLib.Sequence mPushy;
    AutoLib.Sequence mCharge;
    boolean bDone;                          // true when the programmed sequence is done
    BotHardwareOld robot;                      // robot hardware object
    SensorLib.PID mPid;
    SensorLib.PID mgPid;                     // PID controllers for the sequence
    SensorLib.PID mdPid;
    SensorLib.PID muPid;
    SensorLib.PID mGPid;
    // parameters of the PID controller for this sequence's first part
    float Kp = 0.05f;        // degree heading proportional term correction per degree of deviation
    //float Ki = 0.02f;         // ... integrator term
    float Ki = 0.00f;
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    // parameters of the PID controller for the heading of the line following
    float Kp2 = 0.35f;
    float Ki2 = 0.000f;
    float Kd2 = 0;
    float Ki2Cutoff = 0.0f;

    // parameters of the PID controller for the displacement of the line following
    float Kp3 = 5.00f;
    float Ki3 = 0.0f;
    float Kd3 = 0;
    float Ki3Cutoff = 0.00f;

    // parameters of the PID controller for the ultrasonic sensor driving
    float Kp4 = 0.025f;
    float Ki4 = 0.00f;
    float Kd4 = 0;
    float Ki4Cutoff = 0.00f;

    //parameters for gyro PID, but cranked up
    float Kp5 = 0.1f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.05f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 3.0f;    // maximum angle error for which we update integrator

    final float ultraDistS1 = 15;
    final float ultraDistS2 = 11;

    //constants for pushy pushy
    final double pushPos = 1.0;
    final double pushyTime = 0.5;
    boolean red = false;
    final int colorThresh = 200;
    final float driveTime = 0.10f;
    final int driveLoopCount = 1;
    final float pushyPower = 0.2f;

    final int countPerRotation = 280;
    
    OpMode modePointer = this;
    boolean mJustShoot = false;

    AutoLib.Timer mWait = new AutoLib.Timer(20);

    public LineDrive(OpMode mode, boolean redColor, boolean justShoot){
        if(mode != null) modePointer = mode;
        red = redColor;
        mJustShoot = justShoot;
    }

    @Override
    public void init() {
        //init hardware objects
        final boolean debug = false;
        robot = new BotHardwareOld();
        robot.init(modePointer, debug, true);
        robot.setMaxSpeedAll(2500);

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

        mShoot.add(new AutoLib.MoveByEncoderStep(robot.getMotorArray(), 0.4f, 850, true));
        mShoot.add(new AutoLib.LogTimeStep(modePointer, "YAY", 0.5));
        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0f,  1500, true, modePointer));
        mShoot.add(new AutoLib.TimedServoStep(robot.ballServo, 0.6f, 0.8, false));
        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0f, 1500, true, modePointer));

        mSequence.add(mShoot);

        mDrive = new AutoLib.LinearSequence();

        mDrive.add(new AutoLib.MoveByEncoderStep(robot.getMotorArray(), 0.2f, 400, true));
        mDrive.add(new AutoLib.LogTimeStep(modePointer, "YAY!", 0.1));

        int heading;
        float turnPower;

        if(red){
            heading = -90;
            turnPower = 0.3f;
        }
        else {
            heading = 90;
            turnPower = 0.3f;
        }

        //mDrive.add(new AutoLib.GyroTurnStep(modePointer, heading, robot.getNavXHeadingSensor(), robot.getMotorArray(), turnPower, 3.0f, true));
        mDrive.add(new AutoLib.LogTimeStep(modePointer, "Yay!", 0.1));

        mDrive.add(new AutoLib.RunCodeStep(new FunctionCall() {
            public void run() {
                robot.initMotors(thing, debug, true);
                robot.setMaxSpeedAll(2800);
            }
        }));

        //mDrive.add(new AutoLib.SquirrleyAzimuthTimedDriveStep(modePointer, 0, heading, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.4f, 1.0f, false));
        mDrive.add(new SquirrelyLib.SquirrleyAzimuthFinDriveStep(modePointer, 0, heading, robot.getNavXHeadingSensor(), mPid, robot.getMotorArray(), 0.5f, new UltraSensors(robot.distSensorLeft, 70, 2.5), false));
        //if(red) mDrive.add(new UltraSquirrleyAzimuthTimedDriveStep(modePointer, heading, heading, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(modePointer, robot.distSensorLeft, 50), mPid, muPid, robot.getMotorArray(), 0.7f, 1.0f, false));
        mDrive.add(new UltraSquirrleyAzimuthFinDriveStep(modePointer, heading, heading, robot.navXHeading, new UltraCorrectedDisplacement(modePointer, robot.distSensorLeft, 16, robot.distSensorRight), mPid, muPid, robot.getMotorArray(), 0.4f, new LineSensors(this, frame, 0.0f), true));

        mSequence.add(mDrive);

        mPushy = new AutoLib.LinearSequence();

        //two-stage line follow

        mPushy.add(new AutoLib.RunCodeStep(new FunctionCall() {
            public void run() {
                robot.initMotors(thing, debug, true);
                //robot.setMaxSpeedAll(2500);
            }
        }));


        //mPushy.add(new AutoLib.GyroTurnStep(modePointer, 0, new LineSensors(this, frame), robot.getMotorArray(), 0.1f, 0.05f, true, true));
        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensorLeft, ultraDistS1, 4.0, robot.distSensorRight), mgPid, mdPid, robot.getMotorArray(), 0.1f, false));
        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensorLeft, ultraDistS2, 4.0, robot.distSensorRight), mgPid, mdPid, robot.getMotorArray(), 0.05f, true));

        /*
        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.navX.zeroYaw();
            }
        }));
        */

        //pushy pushy
        mPushy.add(new PushyLib.pushypushy(modePointer, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, pushyTime, red, colorThresh, pushyPower, driveTime, driveLoopCount));

        mPushy.add(new UltraSquirrleyAzimuthFinDriveStep(modePointer, heading, heading, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(modePointer, robot.distSensorLeft, 25, robot.distSensorRight), mPid, muPid, robot.getMotorArray(), 0.4f, new LineSensors(this, frame, 3000.0f), true));


        mPushy.add(new AutoLib.RunCodeStep(new FunctionCall() {
            public void run() {
                robot.initMotors(thing, debug, true);
                //robot.setMaxSpeedAll(2500);
            }
        }));

        //mPushy.add(new AutoLib.GyroTurnStep(modePointer, 0, new LineSensors(this, frame), robot.getMotorArray(), 0.1f, 0.5f, true, true));
        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensorLeft, ultraDistS1+1, 4.0, robot.distSensorRight), mgPid, mdPid, robot.getMotorArray(), 0.1f, false));
        mPushy.add(new LineFollowLib.LineDriveStep(modePointer, 0, new LineSensors(this, frame), new LineSensors(this, frame), new UltraSensors(robot.distSensorLeft, ultraDistS2+1, 4.0, robot.distSensorRight), mgPid, mdPid, robot.getMotorArray(), 0.05f, true));

        /*
        mPushy.add(new AutoLib.RunCodeStep(new AutoLib.FunctionCall() {
            public void run() {
                robot.navX.zeroYaw();
            }
        }));
        */

        //pushy pushy
        mPushy.add(new PushyLib.pushypushy(modePointer, robot.getMotorArray(), robot.leftSensor, robot.rightSensor, robot.leftServo, robot.rightServo,
                pushPos, pushyTime, red, colorThresh, pushyPower, driveTime, driveLoopCount));

        mSequence.add(mPushy);

        mCharge = new AutoLib.LinearSequence();

        mCharge.add(new AutoLib.RunCodeStep(new FunctionCall() {
            public void run() {
                robot.initMotors(thing, debug, true);
                robot.setMaxSpeedAll(2800);
            }
        }));

        mCharge.add(new UltraSquirrleyAzimuthFinDriveStep(modePointer, -heading, heading, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(modePointer, robot.distSensorLeft, 30), mPid, muPid, robot.getMotorArray(), 1.0f, new TimerFinish(0.5), false));
        mCharge.add(new UltraSquirrleyAzimuthFinDriveStep(modePointer, -heading, heading, robot.getNavXHeadingSensor(), new UltraCorrectedDisplacement(modePointer, robot.distSensorLeft, 130), mPid, muPid, robot.getMotorArray(), 1.0f, new TimerFinish(2.25), true));

        mSequence.add(mCharge);
        // start out not-done
        bDone = false;
    }

    @Override
    public void init_loop(){
        modePointer.telemetry.addData("NavX Connected", robot.navX.isConnected());
        modePointer.telemetry.addData("NavX Ready", robot.startNavX());
    }

    @Override
    public void start(){
        robot.navX.zeroYaw();
        if(mJustShoot) mWait.start();
    }

    @Override
    public void loop() {
        try{
           if(mJustShoot){
               // until we're done, keep looping through the current Step(s)
               if (!bDone && mWait.done())
                   bDone = mShoot.loop();       // returns true when we're done
               else
                   modePointer.telemetry.addData("sequence finished", "");
           }
            else {
               // until we're done, keep looping through the current Step(s)
               if (!bDone)
                   bDone = mSequence.loop();       // returns true when we're done
               else
                   modePointer.telemetry.addData("sequence finished", "");
           }
        }
        catch (Exception e){
            modePointer.telemetry.addData("Exception: ", e.getMessage());
            telemetry.update();
            robot.waitForTick(2000);
            //init();
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

        final float dispOffset = 0.1f;

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
            return (float) linePos + dispOffset;
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
        UltrasonicSensor mSensor2;
        float mDist;
        float mHeading;
        double mSeconds;
        AutoLib.Timer mTime = null;

        UltraSensors(UltrasonicSensor sensor, float dist, double time) {
            mSensor = sensor;
            mDist = dist;
            mSeconds = time;
        }

        UltraSensors(UltrasonicSensor sensor, float dist, double time, UltrasonicSensor sensor2) {
            mSensor2 = sensor2;
            mSensor = sensor;
            mDist = dist;
            mSeconds = time;
        }

        public boolean checkStop() {
            if(mTime == null) {
                mTime = new AutoLib.Timer(mSeconds);
                mTime.start();
            }

            //get ultrasonic distance
            double dist;
            if(mSensor2 == null) dist = mSensor.getUltrasonicLevel();
            else dist = (mSensor.getUltrasonicLevel() + mSensor2.getUltrasonicLevel()) / 2.0;

            //cutoff ridiculous values
            if (mSensor.getUltrasonicLevel() > 200 || mSensor.getUltrasonicLevel() < 5 || mSensor2.getUltrasonicLevel() > 200 || mSensor2.getUltrasonicLevel() < 5) return false;
                //now check if the robot is in range
            else return (dist < mDist) || mTime.done();
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

    public static class UltraCorrectedDisplacement{
        private final float Kp = 0.3f;
        private float mDist;
        private UltrasonicSensor mSensor;
        private UltrasonicSensor mSensor2;
        private OpMode mMode;

        UltraCorrectedDisplacement(OpMode mode, UltrasonicSensor sensor, float dist) {
            mMode = mode;
            mSensor = sensor;
            mDist = dist;
        }

        UltraCorrectedDisplacement(OpMode mode, UltrasonicSensor sensor, float dist, UltrasonicSensor sensor2) {
            mSensor2 = sensor2;
            mMode = mode;
            mSensor = sensor;
            mDist = dist;
        }

        public float getDisp(float angleError){
            float dist;
            if(mSensor2 == null) dist = (float)mSensor.getUltrasonicLevel();
            else dist = (float)((mSensor.getUltrasonicLevel() + mSensor2.getUltrasonicLevel()) / 2.0);

            mMode.telemetry.addData("Ultra", dist);

            if(dist > 200) dist = 200;
            else if (dist < 5) dist = 5;

            //dist += angleError * Kp;

            return dist - mDist;
        }

    }

    static public class TimerFinish implements FinishSensor {
        private AutoLib.Timer mTimer;

        TimerFinish(double seconds){
            mTimer = new AutoLib.Timer(seconds);
        }

        public boolean checkStop(){
            if(!mTimer.isStarted()) mTimer.start();
            return mTimer.done();
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
        private ArrayList<SetPower> mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public UltraSquirrleyGuideStep(OpMode mode, float direction, float heading, HeadingSensor gyro, UltraCorrectedDisplacement ultra, SensorLib.PID gpid,
                                       SensorLib.PID dpid, ArrayList<SetPower> motorsteps, float power) {
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
            super.loop();
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
            SquirrelyLib.MotorPowers mp = SquirrelyLib.GetSquirrelyWheelMotorPowers(mDirection);

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
                //mOpMode.telemetry.addData("front power ", mp.Front());
                //mOpMode.telemetry.addData("back power ", mp.Back());
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

            ArrayList<SetPower> steps = new ArrayList<SetPower>();
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

            ArrayList<SetPower> steps = new ArrayList<SetPower>();
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