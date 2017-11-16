package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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

    private RelativeLayout layout;
    private GraphView chart;
    private int[] data;
    final private LineGraphSeries<DataPoint> lineData = new LineGraphSeries<>();

    @Override
    public void init() {
        initVuforia(true);

        //graph stuff
        layout = (RelativeLayout)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.CheapCamera);
        Runnable doGraphSetup = new Runnable() {
            @Override
            public void run() {
                chart = new GraphView(hardwareMap.appContext);
                chart.addSeries(lineData);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layout.addView(chart, params);
            }
        };
        layout.getHandler().post(doGraphSetup);
    }

    @Override
    public void start() {
        //hmmm
    }

    private int[] ray;
    private double scale;
    private DataPoint[] linePoints;

    @Override
    public void loop() {
        //I should put this somewhere else, but might as well throw it here
        try {
            Mat frame = getFrame();
            //first algorithm: follow tape line
            //step 0: reduce image by downsizing it with a gaussian pyramid
            Imgproc.pyrDown(frame, frame);
            //intermission: instanciate list
            if(data == null) {
                data = new int[255];
                scale = 1.0/(double)frame.cols() * 255.0;
                linePoints = new DataPoint[255];
            }
            //step 1: separate pixels into six color groups: r, g, b, black, white, grey
            //upsize aray to 32bit signed
            frame.convertTo(frame, CvType.CV_32S);
            //convert to java array
            if(ray == null) ray = new int[(int)(frame.total() * frame.elemSize())];
            frame.get(0, 0, ray);
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
                if(sat <= SAT_MIN) {
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
                        //increment histogram!
                        data[(int)((i % frame.cols()) * scale)]++;
                    }
                }
            }
            //reinsert mat
            frame.put(0, 0, ray);
            //convert back to 8 bit
            frame.convertTo(frame, CvType.CV_8U);
            //step 2: pyrUp!
            Imgproc.pyrUp(frame, frame);
            //display
            drawFrame(frame);
            //draw histogram graph
            synchronized (linePoints) {
                for(int i = 0; i < data.length; i++) linePoints[i] = new DataPoint(i, data[i]);
            }

            Runnable postData = new Runnable() {
                @Override
                public void run() {
                    synchronized (linePoints) {
                        lineData.resetData(linePoints);
                    }
                    chart.invalidate();
                }
            };

            layout.getHandler().post(postData);
        }
        catch (InterruptedException e) {
            //lol yeah naw
        }
    }
}