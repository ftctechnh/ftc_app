package org.firstinspires.ftc.teamcode.opmodes.demo;
/*
import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.libraries.LineFollowLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by Robotics on 10/28/2016.
 *

@Autonomous(name = "Line Follow Algorithm + Graph Demo", group = "Test")
@Disabled
public class OpenCVGraphFun extends OpenCVLib {

    private int[] yValStore = new int[3];

    private RelativeLayout layout;
    private LineChart chart;

    private LinkedList<Entry> dataAngle = new LinkedList<Entry>();
    private LinkedList<Entry> dataDisp = new LinkedList<Entry>();

    @Override
    public void init(){
        layout = (RelativeLayout)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.CheapCamera);

        Runnable doGraphSetup = new Runnable() {
            @Override
            public void run() {
                chart = new LineChart(hardwareMap.appContext);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layout.addView(chart, params);

                chart.setAutoScaleMinMaxEnabled(false);
                AxisBase leftAxis = chart.getAxisLeft();
                leftAxis.setAxisMinimum(-180);
                leftAxis.setAxisMaximum(180);
                AxisBase rightAxis = chart.getAxisRight();
                rightAxis.setAxisMinimum(-1);
                rightAxis.setAxisMaximum(1);
                chart.setTouchEnabled(true);
            }
        };

        layout.getHandler().post(doGraphSetup);

        initOpenCV();
        startCamera();

        //catch a frame as soon as one becomes availible
        Mat frame = getCameraFrame();

        //init scanline Y values
        yValStore[0] = frame.rows() / 4;
        yValStore[1] = frame.rows() / 2;
        yValStore[2] = (frame.rows() * 3) / 4;

        //log all the data
        telemetry.addData("Top Y Value", yValStore[0]);
        telemetry.addData("Middle Y Value", yValStore[1]);
        telemetry.addData("Bottom Y Value", yValStore[2]);

        telemetry.addData("Frame Width", frame.width());
        telemetry.addData("Frame Height", frame.height());
    }

    @Override
    public void start(){
        //wait ten seconds
    }

    @Override
    public void loop(){
        //get frame
        //catch a frame
        Mat frame = getCameraFrame();

        final double angle = LineFollowLib.getAngle(frame, yValStore[0], yValStore[2]);
        final double displace = LineFollowLib.getDisplacment(frame, yValStore[1]);

        //log data
        telemetry.addData("Angle", angle);
        telemetry.addData("Displacement", displace);

        //graph that shizzle
        dataAngle.add(new Entry((float)getRuntime(), (float)angle));
        dataDisp.add(new Entry((float)getRuntime(), (float)displace));

        //cutoff lists so no overflow
        if(dataAngle.size() > 100) dataAngle.removeFirst();
        if(dataDisp.size() > 100) dataDisp.removeFirst();

        //create data sets
        LineDataSet angleFormat = new LineDataSet(new ArrayList<>(dataAngle), "Angle");
        LineDataSet dispFormat = new LineDataSet(new ArrayList<>(dataDisp), "Dispalcement");

        angleFormat.setColor(Color.RED);
        angleFormat.setCircleColor(Color.RED);
        angleFormat.setAxisDependency(YAxis.AxisDependency.LEFT);
        dispFormat.setColor(Color.BLACK);
        dispFormat.setCircleColor(Color.BLACK);
        dispFormat.setAxisDependency(YAxis.AxisDependency.RIGHT);

        //put them in a super data set
        final LineData allData = new LineData(angleFormat, dispFormat);

        Runnable postData = new Runnable() {
            @Override
            public void run() {
                chart.setData(allData);
                chart.invalidate();
            }
        };

        layout.getHandler().post(postData);
    }

    @Override
    public void stop(){
        stopCamera();

        Runnable hideChart = new Runnable() {
            @Override
            public void run() {
                layout.removeView(chart);
            }
        };

        layout.getHandler().post(hideChart);
    }
}
*/