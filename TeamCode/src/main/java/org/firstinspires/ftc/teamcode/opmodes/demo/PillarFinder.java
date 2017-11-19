package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
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

/**
 * Created by Noah on 11/13/2017.
 * Testing file for cryptobox pillar detection using camera
 */

@Autonomous(name="Posterior", group="test")
public class PillarFinder extends VuforiaBallLib {
    private static final double SAT_MIN = 0.5;
    private static final int LUM_THRESH = 50;
    private static final int PEAK_WIDTH_MIN = 6;
    private static final int PEAK_HEIGHT_MIN = 350;
    private int[] data;
    private BotHardware bot = new BotHardware(this);

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    @Override
    public void init() {
        initVuforia(true);
        bot.init();
        mSeq.add(new PilliarTurnStep(bot.getMotorRay(), 0.2f, true, 15, false));
    }

    @Override
    public void start() {
        //hmmm
    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
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

        //cached values
        private int[] ray;
        private double histScale;
        private double rowScale;
        private double colScale;

        public boolean loop() {
            ArrayList<Integer> peaks = new ArrayList<>();
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
                telemetry.addData("Peak", data[55]);
                int runStart = -1;
                for (int i = 0; i < data.length; i++) {
                    if (runStart == -1) {
                        if (data[i] > PEAK_HEIGHT_MIN) runStart = i;
                    } else if (data[i] <= PEAK_HEIGHT_MIN) {
                        if (i - runStart >= PEAK_WIDTH_MIN)
                            peaks.add(runStart + (i - runStart) / 2);
                        runStart = -1;
                    }
                }
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
            } catch (InterruptedException e) {
                //lol yeah naw
            }

            //if there's no or one peak, turn
            if(peaks.size() <= 1) {
                if(turnLeft) turnLeft();
                else turnRight();
            }
            //if there's more than two peaks, take the ones farthest from where we're turning
            else{
                int peakCenter;
                if(turnLeft) peakCenter = (peaks.get(0) + peaks.get(1)) / 2;
                else peakCenter = (peaks.get(peaks.size() - 2) + peaks.get(peaks.size() - 1)) / 2;
                if(Math.abs(peakCenter - 128) < error) {
                    stopMotors();
                    return true;
                }
                else if(peakCenter > 128) turnRight();
                else turnLeft();
            }
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