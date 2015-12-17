package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by Carlos on 12/10/2015.
 */
public class AccelerometerTest extends LinearOpMode {

    AccelerometerReader accelerometer;

    @Override
    public void runOpMode() throws InterruptedException
    {


        waitForStart();

        while(opModeIsActive())
        {
            telemetry.addData("1. x", String.format("%03d", accelerometer.x));
            telemetry.addData("2. y", String.format("%03d", accelerometer.y));
            telemetry.addData("3. z", String.format("%03d", accelerometer.z));

        }
    }
}


class AccelerometerReader extends Activity implements SensorEventListener{
    SensorManager sensorManager;
    Sensor accelerometer;
    float x = 0, y = 0, z = 0;

    public void onCreate(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}