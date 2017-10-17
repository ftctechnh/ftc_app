package org.firstinspires.ftc.teamcode.opmodes.demo;
import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.libraries.APDS9960;
import java.util.LinkedList;

/**
 * Created by Noah on 10/16/2017.
 * Opmode demonstrating the APDS distance sensor with a graph
 */

public class APDSGrapher extends OpMode{
    APDS9960 dist;

    private RelativeLayout layout;
    private LineChart chart;

    final LinkedList<Entry> data = new LinkedList<>();

    @Override
    public void init() {
        dist = new APDS9960(new APDS9960.Config(), hardwareMap.get(I2cDeviceSynch.class, "dist"));
        dist.initDevice();

        //graph stuff
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
    }

    @Override
    public void start() {
        dist.startDevice();
    }

    @Override
    public void loop() {
        telemetry.addData("dist", dist.getDist());

        //add data
        if(data.size() >= 100) data.removeFirst();
        data.add(new Entry((float)getRuntime(), (float)dist.getDist()));

        final LineDataSet lineData = new LineDataSet(data, "Dist (mm)");
        lineData.setColor(Color.GREEN);
        final LineData realLineData = new LineData(lineData);

        Runnable postData = new Runnable() {
            @Override
            public void run() {
                chart.setData(realLineData);
                chart.invalidate();
            }
        };

        layout.getHandler().post(postData);
    }
}
