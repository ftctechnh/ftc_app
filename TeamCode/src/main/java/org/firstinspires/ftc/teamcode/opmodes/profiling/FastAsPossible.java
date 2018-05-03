package org.firstinspires.ftc.teamcode.opmodes.profiling;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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

@TeleOp(name = "Fast as Possible")
public class FastAsPossible extends OpMode implements SensorEventListener {
    private static final double WHEEL_CIRCUMFRENCE_M = DistanceUnit.INCH.toCm(2.875) / 100.0 * Math.PI;
    private static final double Ka = 0.00001;

    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;
    private SensorManager man;

    private float[] lastVal;

    private double lastLeftPos;
    private double lastRightPos;
    private double lastLeftVel;
    private double lastRightVel;
    private double lastRuntime;

    public void init() {
        leftMotor = hardwareMap.get(DcMotorEx.class, "l");
        rightMotor = hardwareMap.get(DcMotorEx.class, "r");

        man = (SensorManager)hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        final Sensor accel = man.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        man.registerListener(this, accel, 1000);

        /*
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
        */
    }

    public void init_loop() {

    }

    public void start() {

    }

    public void loop() {
        final double leftPos = leftMotor.getCurrentPosition();
        final double rightPos = rightMotor.getCurrentPosition();
        final double runtime = getRuntime();
        final double phoneAccel = getAverageAccel();
        final double delta = runtime - lastRuntime;
        final double leftVel = (leftPos - lastLeftPos) / delta;
        final double rightVel = (rightPos - lastRightPos) / delta;
        final double leftAccel = (leftVel - lastLeftVel) / delta * WHEEL_CIRCUMFRENCE_M;
        final double rightAccel = (rightVel - lastRightVel) / delta * WHEEL_CIRCUMFRENCE_M;
        //apply kA term to joystick
        if(gamepad1.left_stick_y > 0) leftMotor.setPower(-gamepad1.left_stick_y - Ka * (phoneAccel - leftAccel));
        if(gamepad1.right_stick_y > 0) rightMotor.setPower(-gamepad1.right_stick_y - Ka * (phoneAccel - rightAccel));
        //set
        lastRuntime = runtime;
        lastLeftPos = leftPos;
        lastRightPos = rightPos;
        lastLeftVel = leftVel;
        lastRightVel = rightVel;
        //logs
        telemetry.addData("Pos Left", leftPos);
        telemetry.addData("Pos Right", rightPos);
        telemetry.addData("Vel Left", leftVel);
        telemetry.addData("Vel Right", rightVel);
        telemetry.addData("Phone Accel", phoneAccel);
        telemetry.addData("Left Accel", leftAccel);
        telemetry.addData("Right Accel", rightAccel);
    }

    public void stop() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        man.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        lastVal = sensorEvent.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private double getAverageAccel() {
        //return lastVal[2] * 9.80665;
        return lastVal[2];
    }
}
