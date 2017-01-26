package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by Noah on 11/24/2016.
 */

public final class LineFollowLib {
    public static final int ERROR_TOO_NOISY = -255;

    //get the angle of the phone relative to robot through line following
    public static double getAngle(Mat src, int topY, int bottomY){
        int[] linePos = new int[2];

        linePos[0] = scanlineAvg(src, topY);
        linePos[1] = scanlineAvg(src, bottomY);

        if(linePos[0] == ERROR_TOO_NOISY || linePos[1] == ERROR_TOO_NOISY) return ERROR_TOO_NOISY;

        final int yDist = linePos[1] - linePos[0];
        final int xDist = bottomY - topY;

        double headingToTarget = Math.atan2(yDist, xDist);
        headingToTarget = Math.toDegrees(headingToTarget); //to degrees

        return headingToTarget;
    }

    //gets the line relative to the camera view on the horizontal axis, from 1 to -1
    public static double getDisplacment(Mat src, int middleY){
        int linePos;

        linePos = scanlineAvg(src, middleY);

        if(linePos == ERROR_TOO_NOISY) return ERROR_TOO_NOISY;

        //convert to value from -1 to 1
        return  (double)(linePos - (src.cols() / 2))  / (double)(src.cols() / 2);
    }

    public static int[] scanlineAvg(Mat src, int[] scanlineYs) {
        //here we gooooooooo!

        byte[][]ray = new byte[scanlineYs.length][src.cols()];

        //apply adaptive threshold to image
        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        //copy scan lines into arrays
        for (int i = 0; i < scanlineYs.length; i++) {
            src.row(scanlineYs[i]).get(0, 0, ray[i]);
        }

        //calculate average position for each
        int[] ret = new int[scanlineYs.length];
        for (int i = 0; i < scanlineYs.length; i++) {
            final int avg = findAvgOverZero(ray[i]);
            if(avg == -400) ret[i] = ERROR_TOO_NOISY;
            else ret[i] = avg;
        }

        return ret;
    }

    public static int scanlineAvg(Mat src, int scanlineY){
        byte[] ray = new byte[src.cols()];

        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        src.row(scanlineY).get(0, 0, ray);

        final int avg = findAvgOverZero(ray);
        if(avg == -400) return ERROR_TOO_NOISY;
        else return avg;
    }

    public static int[] minMax(Mat src, int scanlineY){
        byte[] ray = new byte[src.cols()];

        src.row(scanlineY).get(0, 0, ray);

        int min = 0;
        int max = 0;
        for(int i = 0; i < ray.length; i++){
            if(min > ray[i]) min = ray[i];
            else if(max < ray[i]) max = ray[i];
        }

        return new int[] {min, max};
    }

    //takes a n array of bytes, and returns (min + max)/2 for a threshold
    private static int threshFind(int[] ray){
        int min = 0;
        int max = 0;
        for(int i = 0; i < ray.length; i++){
            if(min > ray[i]) min = ray[i];
            else if(max < ray[i]) max = ray[i];
        }

        return (min + max) / 2;
    }

    //find the average position of all numbers > 0 in an array
    private static int findAvgOverZero(byte[] ray){
        int totalPos = 0;
        int totalNum = 0;
        for(int i = 0; i < ray.length; i++){
            if(ray[i] != 0){
                totalPos += i;
                totalNum++;
            }
        }

        //and return it in a value between -1 and 1, or and error if there is too much noise
        if(totalNum > ray.length / 4 || totalNum < ray.length / 16) return -400;
        else if(totalNum == 0) return 0;
        else return totalPos/totalNum;
    }

    public static int scanlineAvgDebug(Mat src, int scanlineY, OpMode mode){
        byte[] ray = new byte[src.cols()];

        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        src.row(scanlineY).get(0, 0, ray);

        final int avg = findAvgOverZeroDebug(ray, mode);
        if(avg == -400) return ERROR_TOO_NOISY;
        else return avg;
    }

    //find the average position of all numbers > 0 in an array
    private static int findAvgOverZeroDebug(byte[] ray, OpMode mode){
        int totalPos = 0;
        int totalNum = 0;
        for(int i = 0; i < ray.length; i++){
            if(ray[i] != 0){
                totalPos += i;
                totalNum++;
            }
        }

        mode.telemetry.addData("Total Num Pixel", totalNum);

        //and return it in a value between -1 and 1, or and error if there is too much noise
        if(totalNum > ray.length / 4 || totalNum < ray.length / 16) return -400;
        else if(totalNum == 0) return 0;
        else return totalPos/totalNum;
    }

    // a Step that provides heading + displacement guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
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
            super.loop();
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

    static public class LineDriveStep extends AutoLib.ConcurrentSequence {

        public LineDriveStep(OpMode mode, float heading, HeadingSensor gyro, DisplacementSensor disp, FinishSensor fin, SensorLib.PID gPid, SensorLib.PID dPid,
                             DcMotor[] motors, float power, boolean stop) {
            // add a concurrent Step to control each motor
            ArrayList<AutoLib.SetPower> steps = new ArrayList<AutoLib.SetPower>();
            for (DcMotor em : motors)
                if (em != null) {
                    AutoLib.DriveUntilStopMotorStep step = new AutoLib.DriveUntilStopMotorStep(em, power, fin, stop);  // always set requested power and return "done"
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
}