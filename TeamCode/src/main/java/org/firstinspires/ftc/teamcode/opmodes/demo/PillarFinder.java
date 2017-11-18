package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
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

@Autonomous(name="Posterior")
public class PillarFinder extends VuforiaBallLib {
    private static final double SAT_MIN = 0.5;
    private static final int LUM_THRESH = 50;
    private static final int PEAK_WIDTH_MIN = 6;
    private static final int PEAK_HEIGHT_MIN = 350;
    private int[] data;

    @Override
    public void init() {
        initVuforia(true);
    }

    @Override
    public void start() {
        //hmmm
    }

    private int[] ray;
    private double scale;

    @Override
    public void loop() {
        //I should put this somewhere else, but might as well throw it here
        try {
            Mat frame = getFrame();
            //first algorithm: follow tape line
            //step 0: reduce image by downsizing it with a gaussian pyramid
            Imgproc.pyrDown(frame, frame);
            //intermission: instanciate list
            data = new int[255];
            if(scale == 0) scale = 255.0/(double)frame.cols();
            //step 1: separate pixels into six color groups: r, g, b, black, white, grey
            //upsize aray to 32bit signed
            frame.convertTo(frame, CvType.CV_32S);
            //convert to java array
            if(ray == null) ray = new int[(int)(frame.total() * frame.elemSize())];
            frame.get(0, 0, ray);
            double[] thing = frame.get(frame.rows() / 2, frame.cols() / 2);
            telemetry.addData("Center", "r: " + thing[0] + " g: " + thing[1] + " b: " + thing[2]);
            //and posterize!
            for(int i = 0; i < ray.length; i += 3) {
                //get rgb
                final int r = ray[i];
                final int g = ray[i + 1];
                final int b = ray[i + 2];
                //calc luminance and saturation
                final int lum = Math.max(r, Math.max(g, b));
                double sat = 0;
                if(lum != 0) sat = (double)(lum - Math.min(r, Math.min(g, b))) / (double)lum;
                //threshold values for white or grey pixels
                if(lum <= LUM_THRESH) {
                    ray[i] = 0;
                    ray[i + 1] = 0;
                    ray[i + 2] = 0;
                }
                else if(sat <= SAT_MIN) {
                    final int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                    ray[i] = gray;
                    ray[i + 1] = gray;
                    ray[i + 2] = gray;
                }
                //else it must be a solid color, so group those
                else{
                    //red
                    if(lum == r) {
                        ray[i] = 255;
                        ray[i + 1] = 0;
                        ray[i + 2] = 0;
                        //increment histogram!
                        data[(int)(((i / 3) % frame.cols()) * scale)]++;
                    }
                    //green
                    else if(lum == g) {
                        ray[i] = 0;
                        ray[i + 1] = 255;
                        ray[i + 2] = 0;
                    }
                    //blue
                    else if(lum == b) {
                        ray[i] = 0;
                        ray[i + 1] = 0;
                        ray[i + 2] = 255;
                    }
                }
            }
            //reinsert mat
            frame.put(0, 0, ray);
            //draw histogram
            final double scalar = (double)frame.cols() / (double)data.length;
            final double dataScale = 255.0 / (double)(frame.rows());
            final Scalar color = new Scalar(255, 0, 0);
            for(int i = 0; i < data.length; i++) Imgproc.rectangle(frame, new Point(scalar * i, frame.rows() - 1), new Point(scalar * i + scalar, frame.rows() - 1 - data[i] * dataScale), color);
            Imgproc.drawMarker(frame, new Point(frame.cols() / 2, frame.rows() / 2), color);
            //count pillars
            //generate peaks
            telemetry.addData("Peak", data[55]);
            int runStart = -1;
            ArrayList<Integer> peaks = new ArrayList<>();
            for(int i = 0; i < data.length; i++) {
                if(runStart == -1) { if(data[i] > PEAK_HEIGHT_MIN) runStart = i; }
                else if(data[i] <= PEAK_HEIGHT_MIN) {
                    if(i - runStart >= PEAK_WIDTH_MIN) peaks.add(runStart + (i - runStart) / 2);
                    runStart = -1;
                }
            }
            telemetry.addData("Peaks", peaks.toString());
            for(Integer i : peaks) telemetry.addData("Peak " + i, data[i]);
            //mark peaks with column
            Scalar green = new Scalar(0, 255, 0);
            for (Integer i : peaks) Imgproc.rectangle(frame, new Point(i * scalar, 0), new Point(i * scalar + scalar, frame.rows()), green);
            //convert back to 8 bit
            frame.convertTo(frame, CvType.CV_8U);
            //step 2: pyrUp!
            Imgproc.pyrUp(frame, frame);
            //display
            drawFrame(frame);
        }
        catch (InterruptedException e) {
            //lol yeah naw
        }
    }

    private class PilliarTurnStep extends AutoLib.Step {
        private DcMotorEx[] motors;

        PilliarTurnStep(DcMotorEx[] motors) {
            this.motors = motors;
        }

        public boolean loop() {
            //I should put this somewhere else, but might as well throw it here
            try {
                Mat frame = getFrame();
                //first algorithm: follow tape line
                //step 0: reduce image by downsizing it with a gaussian pyramid
                Imgproc.pyrDown(frame, frame);
                //intermission: instanciate list
                data = new int[255];
                if (scale == 0) scale = 255.0 / (double) frame.cols();
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
                            data[(int) (((i / 3) % frame.cols()) * scale)]++;
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
                        }
                    }
                }
                //reinsert mat
                frame.put(0, 0, ray);
                //draw histogram
                final double scalar = (double) frame.cols() / (double) data.length;
                final double dataScale = 255.0 / (double) (frame.rows());
                final Scalar color = new Scalar(255, 0, 0);
                for (int i = 0; i < data.length; i++)
                    Imgproc.rectangle(frame, new Point(scalar * i, frame.rows() - 1), new Point(scalar * i + scalar, frame.rows() - 1 - data[i] * dataScale), color);
                Imgproc.drawMarker(frame, new Point(frame.cols() / 2, frame.rows() / 2), color);
                //count pillars
                //generate peaks
                telemetry.addData("Peak", data[55]);
                int runStart = -1;
                ArrayList<Integer> peaks = new ArrayList<>();
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
                    Imgproc.rectangle(frame, new Point(i * scalar, 0), new Point(i * scalar + scalar, frame.rows()), green);
                //convert back to 8 bit
                frame.convertTo(frame, CvType.CV_8U);
                //step 2: pyrUp!
                Imgproc.pyrUp(frame, frame);
                //display
                drawFrame(frame);
            } catch (InterruptedException e) {
                //lol yeah naw
            }
        }
    }
}