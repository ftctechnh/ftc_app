package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.util.Range;

import org.opencv.core.Mat;

import java.util.ArrayList;


/**
 * simple example of using a Step that uses gyro input to drive along a given course for a given time
 * Created by phanau on 1/3/16 as SensorDriveTestOp, modified 1/22/16 to use Gyro, renamed GyroDriveTestOp 2/16/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Line Drive", group ="Line Follow")
//@Disabled
public class LineDrive extends OpenCVLib implements HeadingSensor, DisplacementSensor, FinishSensor {

    private int yValStore[] = new int[3];
    private double lastLinePos = 0;

    private double lastDisp = 0;

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    BotHardware robot;                      // robot hardware object
    SensorLib.PID mgPid;                     // PID controller for the sequence
    SensorLib.PID mdPid;

    // parameters of the PID controller for this sequence
    float Kp = 0.010f;        // motor power proportional term correction per degree of deviation
    float Ki = 0.000f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    float Kp2 = 0.25f;
    float Ki2 = 0.0f;
    float Kd2 = 0;
    float Ki2Cutoff = 0.01f;

    @Override
public void init() {
        //init hardware objects
        final boolean debug = false;
        robot = new BotHardware();
        robot.init(this, debug);

        // create a PID controller for the sequence
        mgPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);    // make the object that implements PID control algorithm
        mdPid = new SensorLib.PID(Kp2, Ki2, Kd2, Ki2Cutoff);

        // create an autonomous sequence with the steps to drive
        // several legs of a polygonal course ---
        final float power = 0.1f;
        final float time = 30.0f;

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        mSequence.add(new DriveUntilLine(robot.getMotorArray(), power, this, true));
        mSequence.add(new LineGuideStep(this, 0, this, this, this, mgPid, mdPid, robot.getMotorArray(), power));

        // start out not-done
        bDone = false;

        //start up opencv
        initOpenCV();
        startCamera();

        //catch a frame
        Mat frame = getCameraFrame();

        //init scanline Y values
        yValStore[0] = frame.rows() / 4;
        yValStore[1] = frame.rows() / 2;
        yValStore[2] = (frame.rows() * 3) / 4;

        telemetry.addData("Top Y Value", yValStore[0]);
        telemetry.addData("Middle Y Value", yValStore[1]);
        telemetry.addData("Bottom Y Value", yValStore[2]);

        telemetry.addData("Frame Width", frame.width());
        telemetry.addData("Frame Height", frame.height());
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

    //heading sensor code for line following
    public float getHeading(){
        Mat frame = getCameraFrame();
        //get line displacement
        double linePos = -LineFollowLib.getAngle(frame, yValStore[0], yValStore[2]);
        //if it's an error, turn to where line was last
        if(linePos == LineFollowLib.ERROR_TOO_NOISY){
            if(lastLinePos > 0) return 180.0f;
            else return -180.0f;
        }

        return (float)linePos;
    }

    public float getDisp(){
        Mat frame = getCameraFrame();
        double linePos = -LineFollowLib.getDisplacment(frame, yValStore[1]);
        //if it's an error, turn to where line was last
        if(linePos == LineFollowLib.ERROR_TOO_NOISY){
            if(lastLinePos > 0) return 1.0f;
            else return -1.0f;
        }
        return (float)linePos;
    }

    public boolean checkStop(){
        return false;
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
        private FinishSensor mStop;
        private SensorLib.PID mgPid;                         // proportional–integral–derivative controller (PID controller)
        private SensorLib.PID mdPid;
        private double mPrevTime;                           // time of previous loop() call
        private DcMotor[] mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...

        public LineGuideStep(OpMode mode, float heading, HeadingSensor gyro, DisplacementSensor disp, FinishSensor stop, SensorLib.PID gPid, SensorLib.PID dPid,
                             DcMotor[] motorsteps, float power)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mDisp = disp;
            mStop = stop;
            mgPid = gPid;
            mdPid = dPid;
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
            float gCorrection = -mgPid.loop(error, (float)dt);

            //feed other error through PID to get more values
            float dCorrection = -mdPid.loop(mDisp.getDisp(), (float)dt);

            // compute new right/left motor powers
            float frontRight = Range.clip(mPower + gCorrection + dCorrection, -1, 1);
            float backRight = Range.clip(mPower + gCorrection - dCorrection, -1, 1);
            float frontLeft = Range.clip(mPower - gCorrection + dCorrection, -1, 1);
            float backLeft = Range.clip(mPower - gCorrection - dCorrection, -1, 1);

            // set the motor powers -- handle both time-based and encoder-based motor Steps
            // assumed order is right motors followed by an equal number of left motors
            mMotorSteps[0].setPower(frontRight);
            mMotorSteps[1].setPower(backRight);
            mMotorSteps[2].setPower(frontLeft);
            mMotorSteps[3].setPower(backLeft);

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("left power ", frontLeft);
                mOpMode.telemetry.addData("right power ", frontRight);
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return mStop.checkStop();
        }
    }

    public class DriveUntilLine extends AutoLib.ConcurrentSequence implements FinishSensor {

        DisplacementSensor mDisp;
        int lineCount = 0;

        public DriveUntilLine(DcMotor motors[], double power, DisplacementSensor disp, boolean stop)
        {
            mDisp = disp;

            for (DcMotor em : motors)
                if (em != null)
                    this.add(new DriveUntilStopMotorStep(em, power, this, stop));
        }

        public boolean checkStop(){
            if(mDisp.getDisp() != LineFollowLib.ERROR_TOO_NOISY) lineCount++;
            else lineCount = 0;

            return lineCount > 5;
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
}