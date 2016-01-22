package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class DragonoidsAuto extends LinearOpMode implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private int sensorType = Sensor.TYPE_GYROSCOPE;
    private static final float nanoSecondsToSeconds = 1.0f / 1000000000.0f;
    private float lastGyroTimestamp = 0;
    private float heading = 0; // In radians
    private float headingDegrees = 0; // In degrees (use in autonomous flow)
    // Autonomous constants
    private final double drivePower = 0.5;
    private final double turnPower = 0.3;
    private final int step1Distance = 500;
    private final int step2Distance = 2000;
    private final int step3Distance = 500;

    public void initialize() {
        DragonoidsGlobal.init(hardwareMap);
        // Set up the rotation vector sensor
        this.sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        this.gyroSensor = sensorManager.getDefaultSensor(this.sensorType);
        if (this.gyroSensor != null) {
            this.sensorManager.registerListener(this, this.gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else {
            telemetry.addData("Error", "Gyroscope sensor not found");
        }
    }
    // For gyro sensor data
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != this.sensorType) return;

        if (lastGyroTimestamp != 0) {
            final float dT = (event.timestamp - lastGyroTimestamp) * nanoSecondsToSeconds;
            heading += dT * event.values[1];
            headingDegrees = (float) Math.toDegrees(heading);
        }
        lastGyroTimestamp = event.timestamp;

        telemetry.addData("Heading", headingDegrees);
        telemetry.addData("Right1 encoder", DragonoidsGlobal.rightOne.getCurrentPosition());
        telemetry.addData("Right2 encoder", DragonoidsGlobal.rightTwo.getCurrentPosition());
        telemetry.addData("Left1  encoder", DragonoidsGlobal.leftOne.getCurrentPosition());
        telemetry.addData("Left2  encoder", DragonoidsGlobal.leftTwo.getCurrentPosition());
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() != this.sensorType) return;

        String description;
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                description = "Unreliable";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                description = "Low accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                description = "Medium accuracy";
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                description = "High accuracy";
                break;
            default:
                description = "None?";
        }
        telemetry.addData("Gyro accuracy changed", String.format("%s (%d)", description, accuracy));
    }

    public int getRightEncoderValue() {
        int total = 0;
        total += DragonoidsGlobal.rightOne.getCurrentPosition();
        total += DragonoidsGlobal.rightTwo.getCurrentPosition();
        return total / 2;
    }
    public int getLeftEncoderValue() {
        int total = 0;
        total += DragonoidsGlobal.leftOne.getCurrentPosition();
        total += DragonoidsGlobal.leftTwo.getCurrentPosition();
        return total / 2;
    }

    protected enum Direction {
        Right, Left
    }

    public void turn(Direction direction, float degrees) throws InterruptedException {
        float startingRotation = this.headingDegrees;
        float targetRotation;

        if (direction == Direction.Left) {
            targetRotation = startingRotation - degrees;
            while (this.headingDegrees > targetRotation) {
                DragonoidsGlobal.setDrivePower(-turnPower, turnPower);
                waitOneFullHardwareCycle();
            }
        }

        if (direction == Direction.Right) {
            targetRotation = startingRotation + degrees;
            while (this.headingDegrees < targetRotation) {
                DragonoidsGlobal.setDrivePower(turnPower, -turnPower);
                waitOneFullHardwareCycle();
            }
        }
        DragonoidsGlobal.stopMotors();
    }
    public void drive(int distance) {
        while ((this.getLeftEncoderValue() + this.getRightEncoderValue()) / 2 < distance) {
            DragonoidsGlobal.setDrivePower(drivePower, drivePower);
        }
        DragonoidsGlobal.stopMotors();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();
        waitForStart();
        // Choose flow based on alliance color (we're assuming red)

        // Drive forward a bit
        this.drive(step1Distance);
        // Use the phone's IMU to make a precise 45 degree turn
        this.turn(Direction.Left, 45);
        // Drive forward to the beacon zone
        this.drive(step2Distance);
        // Turn 45 degrees again
        this.turn(Direction.Left, 45);
        // Drive forward to color detection distance
        this.drive(step3Distance);
        // Detect color of the beacon

        // Drive forward or extend arm to push the correct button

        // Deposit climbers in the bucket behind the beacon

        // Reverse out of the beacon area (or turn 180 degrees and then drive forward)

        // Turn -45 degrees

        // Drive forward as far as possible up the mountain

        // Use the "churro grabbers" to gain more traction and hoist the robot up the
        // remaining portion of the mountain after the normal wheels begin to slip

        DragonoidsGlobal.stopAll();
    }
}