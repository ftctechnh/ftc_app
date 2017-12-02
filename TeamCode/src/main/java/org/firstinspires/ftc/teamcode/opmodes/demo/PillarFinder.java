package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.SetPower;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Noah on 11/13/2017.
 * Testing file for cryptobox pillar detection using camera
 */

@Autonomous(name="Posterior", group="test")
public class PillarFinder extends VuforiaBallLib {
    private static final double SAT_MIN = 0.5;
    private static final int LUM_THRESH = 50;
    private static final int PEAK_WIDTH_MIN = 4;
    private static final int PEAK_HEIGHT_MIN = 110;
    private static final int PEAK_FIND_WINDOW = 40;
    private static final int PEAK_FRAME_COUNT = 3;
    protected boolean red = true;
    private int[] data;
    private BotHardware bot = new BotHardware(this);

    private AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();

    @Override
    public void init() {
        initVuforia(true);
        bot.init();

        mSeq.add(
            new AutoLib.RunUntilStopStep(
                new AutoLib.AzimuthTimedDriveStep(
                        this, 0, bot.getHeadingSensor(), new SensorLib.PID(1.0f, 0, 0, 0), bot.getMotorVelocityShimArray(), 180.0f, 500.0f, true
                ),
                new PeakFindStep(
                        PEAK_FIND_WINDOW, PEAK_FRAME_COUNT
                )
            )
        );
        mSeq.add(new PeakHoneStep(bot.getMotorVelocityShimArray(), false, 180.0f, 3, this));
    }

    @Override
    public void start() {
        //hmmm
    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
    }

    private class DebugHeading implements HeadingSensor {
        public float getHeading() {
            return 0;
        }
    }

    private class PeakFindStep extends AutoLib.Step {
        private int peakFrameCount = 0;
        private int count;
        private int centerWindow;
        PeakFindStep(int centerWindow, int count) {
            this.count = count;
            this.centerWindow = centerWindow;
        }

        public boolean loop() {
            final int middle = 255 / 2;
            //check peak window for peaks
            ArrayList<Integer> peaks = getPeaks();
            int i = 0;
            for(; i < peaks.size(); i++) {
                if(peaks.get(i) < middle + centerWindow && peaks.get(i) > middle - centerWindow) {
                    peakFrameCount++;
                    break;
                }
            }
            if(i == peaks.size()) peakFrameCount = 0;

            return peakFrameCount > this.count;
        }
   }

   private class PeakHoneStep extends  AutoLib.Step {
        private final int error;
        private final float power;
        private final DcMotor[] motorRay;
        private final boolean reversed;
        private final OpMode mode;

        private int lastPeakPos = -1;
        private int lostPeaksCount = 0;
        private int foundPeakCount = 0;

        private static final int PEAK_LOST_BREAK = 10;
        private static final int PEAK_DIST_THRESH = 15;
        private static final int PEAK_FOUND_COUNT = 10;

        PeakHoneStep(DcMotor[] motors, boolean revesed, float power, int error, OpMode mode) {
            this.motorRay = motors;
            this.power = power;
            this.error = error;
            this.reversed = revesed;
            this.mode = mode;
        }

        public boolean loop() {
            //if first run, get the peak we want to center on.
            //in this case, the one farthest left in the frame
            ArrayList<Integer> peaks = getPeaks();
            //if we have no peaks, ty again next time
            if(peaks.size() <= 0) return ++lostPeaksCount >= PEAK_LOST_BREAK;
            //else if the distance of the peaks is greater than reasonable, break
            if(lastPeakPos != -1 && Math.abs(peaks.get(peaks.size() - 1) - lastPeakPos) >= PEAK_DIST_THRESH) return ++lostPeaksCount >= PEAK_LOST_BREAK;
            //if the peak is within stopping margin, stop
            telemetry.addData("Peak dist", Math.abs(peaks.get(peaks.size() - 1) - 127));
            if(Math.abs(peaks.get(peaks.size() - 1) - 127) <= error) {
                setMotors(0);
                return ++foundPeakCount >= 10;
            }
            //save the last peak
            lastPeakPos = peaks.get(peaks.size() - 1);
            //reset lost peaks counter
            lostPeaksCount = 0;
            foundPeakCount = 0;
            //adjust motors
            if(peaks.get(0) > 127) setMotors(reversed ? -power : power);
            else setMotors(reversed ? power : -power);
            return false;
        }

        private void setMotors(float power) {
            for(DcMotor motor : motorRay) motor.setPower(power);
        }
   }

    //cached values
    private int[] ray;
    private double histScale;
    private double rowScale;
    private double colScale;

    private ArrayList<Integer> getPeaks() {
        //I should put this somewhere else, but might as well throw it here
        try {
            Mat frame = getFrame();
            //first algorithm: follow tape line
            //step 0: reduce image by downsizing it with a gaussian pyramid
            Imgproc.pyrDown(frame, frame);
            //twice
            Imgproc.pyrDown(frame, frame);
            //intermission: instanciate list
            data = new int[255];
            if (histScale == 0) {
                histScale = (double)data.length / (double)frame.cols();
                rowScale = (double)data.length / (double)frame.rows();
                colScale = (double) frame.cols() / (double) data.length;
            }
            //step 1: separate pixels into six color groups: r, g, b, black, white, grey
            //upsize aray to 32bit signed
            frame.convertTo(frame, CvType.CV_32S);
            //convert to java array
            if (ray == null) ray = new int[(int) (frame.total() * frame.elemSize())];
            frame.get(0, 0, ray);
            double[] thing = frame.get(frame.rows() / 2, frame.cols() / 2);
            telemetry.addData("Center", "r: " + thing[0] + " g: " + thing[1] + " b: " + thing[2]);
            //and posterize!
            for (int i = 0; i < ray.length; i += 3) {
                //get rgb
                final int r = ray[i];
                final int g = ray[i + 1];
                final int b = ray[i + 2];
                //calc luminance and saturation
                final int lum = Math.max(r, Math.max(g, b));
                double sat = 0;
                if (lum != 0) sat = (double) (lum - Math.min(r, Math.min(g, b))) / (double) lum;
                //threshold values for white or grey pixels
                if (lum <= LUM_THRESH) {
                    ray[i] = 0;
                    ray[i + 1] = 0;
                    ray[i + 2] = 0;
                } else if (sat <= SAT_MIN) {
                    final int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                    ray[i] = gray;
                    ray[i + 1] = gray;
                    ray[i + 2] = gray;
                }
                //else it must be a solid color, so group those
                else {
                    //red
                    if (lum == r) {
                        ray[i] = 255;
                        ray[i + 1] = 0;
                        ray[i + 2] = 0;
                        //increment histogram!
                        if(red) data[(int) (((i / 3) % frame.cols()) * histScale)]++;
                    }
                    //green
                    else if (lum == g) {
                        ray[i] = 0;
                        ray[i + 1] = 255;
                        ray[i + 2] = 0;
                    }
                    //blue
                    else if (lum == b) {
                        ray[i] = 0;
                        ray[i + 1] = 0;
                        ray[i + 2] = 255;
                        if(!red) data[(int) (((i / 3) % frame.cols()) * histScale)]++;
                    }
                }
            }
            //reinsert mat
            frame.put(0, 0, ray);
            //draw histogram
            final Scalar color;
            if(red) color = new Scalar(255, 0, 0);
            else color = new Scalar(0, 0, 255);
            for (int i = 0; i < data.length; i++)
                Imgproc.rectangle(frame, new Point(colScale * i, frame.rows() - 1), new Point(colScale * i + colScale, frame.rows() - 1 - data[i] * rowScale), color);
            Imgproc.drawMarker(frame, new Point(frame.cols() / 2, frame.rows() / 2), color);
            //count pillars
            //generate peaks
            ArrayList<Integer> peaks;
            int tempThresh = PEAK_HEIGHT_MIN;
            do {
                peaks = new ArrayList<>();
                int runStart = -1;
                for (int i = 0; i < data.length; i++) {
                    if (runStart == -1) {
                        if (data[i] > tempThresh) runStart = i;
                    } else if (data[i] <= tempThresh) {
                        if (i - runStart >= PEAK_WIDTH_MIN)
                            peaks.add(runStart + (i - runStart) / 2);
                        runStart = -1;
                    }
                }
                tempThresh += 5;
            } while (peaks.size() > 4);

            telemetry.addData("Thresh", tempThresh - 5);
            telemetry.addData("Peaks", peaks.toString());
            for (Integer i : peaks) telemetry.addData("Peak " + i, data[i]);
            //mark peaks with column
            Scalar green = new Scalar(0, 255, 0);
            for (Integer i : peaks)
                Imgproc.rectangle(frame, new Point(i * colScale, 0), new Point(i * colScale + colScale, frame.rows()), green);
            //convert back to 8 bit
            frame.convertTo(frame, CvType.CV_8U);
            //display
            drawFrame(frame);
            return peaks;
        } catch (InterruptedException e) {
            //lol yeah naw
            return new ArrayList<>();
        }
    }

    private class PilliarTurnStep extends AutoLib.Step {
        private DcMotorEx[] motors;
        private float power;
        private boolean turnLeft;
        private int error;
        private boolean red;

        PilliarTurnStep(DcMotorEx[] motors, float power, boolean turnLeft, int error, boolean red) {
            this.motors = motors;
            this.power = power;
            this.turnLeft = turnLeft;
            this.error = error;
            this.red = red;
        }

        public boolean loop() {


            return false;
        }


        private void turnLeft() {
            motors[0].setVelocity(-180, AngleUnit.DEGREES);
            motors[1].setPower(-power);
            motors[2].setVelocity(180, AngleUnit.DEGREES);
            motors[3].setPower(power);
        }
        private void turnRight() {
            motors[0].setVelocity(180, AngleUnit.DEGREES);
            motors[1].setPower(power);
            motors[2].setVelocity(-180, AngleUnit.DEGREES);
            motors[3].setPower(-power);
        }
        private void stopMotors() { for(DcMotorEx motor : motors ) motor.setPower(0); }
    }
}