package org.firstinspires.ftc.teamcode.opmodes.profiling;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Noah on 4/25/2018.
 * Designed for a ratbot using neverest 3.7s
 * drives forward as fast as possible without slipping the wheels
 * Direction of accel:
 *
 * *----*
 * |  ^ |
 * |  Y | X >
 * |    |
 * *----*
 */

@Autonomous(name = "Fast As Possible")
public class FastAsPossible extends OpMode implements SensorEventListener {
    private static final double WHEEL_CIRCUMFRENCE_M = DistanceUnit.INCH.toCm(2.875) / 100 * Math.PI * 2;

    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;
    SensorManager man;

    List<float[]> lastVal = new LinkedList<>();

    public void init() {
        leftMotor = hardwareMap.get(DcMotorEx.class, "l");
        rightMotor = hardwareMap.get(DcMotorEx.class, "r");

        man = (SensorManager)hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        final Sensor accel = man.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        man.registerListener(this, accel, 0);

        telemetry.addLine().addData("Accel", new Func<String>() {
            @Override public String value() {
                final float[] ray = new float[3];
                for(float[] val : lastVal) {
                    ray[0] += val[0];
                    ray[1] += val[1];
                    ray[2] += val[2];
                }
                ray[0] /= (float)lastVal.size();
                ray[1] /= (float)lastVal.size();
                ray[2] /= (float)lastVal.size();
                lastVal.clear();
                return Arrays.toString(ray);
            }
        });
    }

    public void init_loop() {

    }

    public void start() {

    }

    public void loop() {

    }

    public void stop() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        man.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(lastVal == null) lastVal.add(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
