package org.firstinspires.ftc.teamcode.opmodes.demo;
/*(
import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9960;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
*/

/**
 * Created by Noah on 10/16/2017.
 * Opmode demonstrating the APDS distance sensor with a graph
 */
/*

@Autonomous(name = "APDS", group = "Line Follow")
@Disabled
public class APDSGrapher extends OpMode{
    APDS9960 dist;

    private RelativeLayout layout;
    private LineChart chart;

    final LinkedList<Entry> data = new LinkedList<>();
    final ReentrantReadWriteLock dataLock = new ReentrantReadWriteLock();

    byte thing;

    @Override
    public void init() {
        final APDS9960.Config config = new APDS9960.Config();
        config.setPulse(APDS9960.Config.PulseLength.PULSE_16US, (byte)8, APDS9960.Config.LEDStrength.STREN_100MA, APDS9960.Config.LEDBoost.BOOST_1X, APDS9960.Config.DistGain.GAIN_4X);
        dist = new APDS9960(config, hardwareMap.get(I2cDeviceSynch.class, "bluedist"));
        thing = dist.initDevice();

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
        telemetry.addData("ID", thing & 0xFF);

        final double run = getRuntime();
        //dataLock.readLock().lock();
        final LineDataSet lineData = new LineDataSet(data, "Dist (mm)");
        final LineData realLineData = new LineData(lineData);
        //dataLock.readLock().unlock();

        lineData.setColor(Color.GREEN);
        //add data
        dataLock.writeLock().lock();
        if(data.size() >= 50) data.removeFirst();
        data.add(new Entry((float)run, (float)dist.getDist()));
        dataLock.writeLock().unlock();

        Runnable postData = new Runnable() {
            @Override
            public void run() {
                dataLock.readLock().lock();
                chart.setData(realLineData);
                chart.invalidate();
                dataLock.readLock().unlock();
            }
        };

        layout.getHandler().post(postData);
    }
}
*/