package org.firstinspires.ftc.teamcode.opmodes.demo;
/*
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noah on 4/2/2017.


@Autonomous(name="Graph Fun", group="Test")
@Disabled
public class GraphDemo extends OpMode{
    int stupid = 0;

    private RelativeLayout layout;
    private LineChart chart;

    @Override
    public void init(){
        telemetry.addData("Graph test!", "Go!");
        telemetry.update();

        layout = (RelativeLayout)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.CheapCamera);

        Runnable doGraphSetup = new Runnable() {
            @Override
            public void run() {
                chart = new LineChart(hardwareMap.appContext);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layout.addView(chart, params);
            }
        };

        layout.getHandler().post(doGraphSetup);



        List<Entry> data = new ArrayList<Entry>();

        for(int i = 0; i < 50; i++){
            data.add(new Entry(i, i % 5));
        }

        LineDataSet datas = new LineDataSet(data, "Rando");
        datas.setColor(Color.GREEN);

        final LineData maDatum = new LineData(datas);

        Runnable postData = new Runnable() {
            @Override
            public void run() {
                chart.setData(maDatum);
                //chart.setAutoScaleMinMaxEnabled(true);
                chart.fitScreen();
                chart.setTouchEnabled(true);
                //chart.enableScroll();
                //chart.setDoubleTapToZoomEnabled(true);
                chart.invalidate();
                //chart.setAutoScaleMinMaxEnabled(true);
                chart.fitScreen();
                chart.setAlpha(1.0f);
            }
        };

        layout.getHandler().post(postData);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){
        Runnable hideChart = new Runnable() {
            @Override
            public void run() {
                chart.setAlpha(0.0f);
                layout.removeView(chart);
            }
        };

        layout.getHandler().post(hideChart);
    }

}
*/