package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by Noah on 11/13/2017.
 * Testing file for cryptobox pillar detection using camera
 */

@Autonomous(name="Posterior")
public class PillarFinder extends VuforiaBallLib {
    private static final int LUM_MIN = 20;
    private static final int LUM_MAX = 235;
    private static final int SAT_MIN = 20;
    private static final int HUE_RANGE = 5;

    @Override
    public void init() {
        initVuforia(true);
    }

    @Override
    public void start() {
        //hmmm
    }

    @Override
    public void loop() {
        //I should put this somewhere else, but might as well throw it here
        try {
            Mat frame = getFrame();
            //first algorithm: follow tape line
            //step 0: reduce image by downsizing it with a gaussian pyramid
            Imgproc.pyrDown(frame, frame);
            //step 1: separate pixels into six color groups: r, g, b, black, white, grey
            //convert image to HSV
            Mat hsv = new Mat();
            Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);
            //split
            ArrayList<Mat> list = new ArrayList<>(3);
            Core.split(hsv, list);
            Mat hue = list.get(0);
            Mat sat = list.get(1);
            Mat val = list.get(2);
            list.clear();
            //and posterize!
            for(int row = 0; row < sat.rows(); row++) for(int col = 0; col < sat.cols(); col++) {
                final int lum = (int)val.get(row, col)[0];
                //threshold value for black pixels
                if(lum <= LUM_MIN) frame.put(row, col, new byte[] {(byte)0, (byte)0, (byte)0});
                //threshold values for white or grey pixels
                else if(sat.get(row, col)[0] <= SAT_MIN) {
                    //if lum is greater than max, it's white
                    if(lum >= LUM_MAX) frame.put(row, col, new byte[] {(byte)255, (byte)255, (byte)255});
                    //else it's grey
                    else frame.put(row, col, new byte[] {(byte)128, (byte)128, (byte)128});
                }
                //else it must be a solid color, so group those
                else{
                    final int tempHue = (int)hue.get(row, col)[0] * 2;
                    //if it's categorized as red, mark it red
                    //also wrap the angle
                    if(tempHue + HUE_RANGE >= 180 || tempHue - HUE_RANGE <= 0) frame.put(row, col, new byte[] {(byte)255, (byte)0, (byte)0});
                    //also mark blue
                    else if(Math.abs(tempHue - 240) <= HUE_RANGE) frame.put(row, col, new byte[] {(byte)0, (byte)0, (byte)255});
                }
            }
            //release mats
            hue.release();
            sat.release();
            val.release();
            //step 2: pyrUp!
            Imgproc.pyrUp(frame, frame);
            //display
            drawFrame(frame);
        }
        catch (InterruptedException e) {
            //lol yeah naw
        }
    }
}