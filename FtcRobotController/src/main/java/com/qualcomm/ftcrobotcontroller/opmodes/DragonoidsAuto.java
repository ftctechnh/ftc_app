package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class DragonoidsAuto extends LinearOpMode implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    // Switch to TYPE_GAME_ROTATION_VECTOR if magnetic field disturbances cause issues with inertial navigation
    private int sensorType = Sensor.TYPE_ROTATION_VECTOR;
    private float yaw;
    private float pitch;
    private float roll;
    // Autonomous constants
    private final double turnPower = 0.3;

    public void initialize() {
        DragonoidsGlobal.init(hardwareMap);
        // Set up the rotation vector sensor
        this.sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        this.rotationSensor = sensorManager.getDefaultSensor(this.sensorType);
        if (this.rotationSensor != null) {
            this.sensorManager.registerListener(this, this.rotationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else {
            telemetry.addData("Error", "Rotation vector sensor not found");
        }
    }
    // For rotation vector sensor data
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != this.sensorType) return;

        float[] rotationMatrix1 = new float[9];
        float[] rotationMatrix2 = new float[9];
        // Documentation for these SensorManager.* methods can be found at https://developer.android.com/reference/android/hardware/SensorManager.html
        SensorManager.getRotationMatrixFromVector(rotationMatrix1, event.values);
        SensorManager.remapCoordinateSystem(rotationMatrix1, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix2);
        float[] orientation = new float[3];
        SensorManager.getOrientation(rotationMatrix2, orientation);
        // Convert the orientation from radians to degrees
        this.yaw = (float) Math.toDegrees(orientation[0]);
        this.pitch = (float) Math.toDegrees(orientation[1]);
        this.roll = (float) Math.toDegrees(orientation[2]);
        telemetry.addData("Yaw", this.yaw);
        telemetry.addData("Pitch", this.pitch);
        telemetry.addData("Roll", this.roll);
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
        telemetry.addData("Rotation accuracy changed", String.format("%s (%d)", description, accuracy));
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
        float startingRotation = this.yaw;
        float targetRotation;

        if (direction == Direction.Left) {
            targetRotation = startingRotation - degrees;
            if (targetRotation <= -180) {
                targetRotation += 360;
            }
            while (this.yaw > targetRotation) {
                DragonoidsGlobal.setDrivePower(-turnPower, turnPower);
                waitOneFullHardwareCycle();
            }
        }

        if (direction == Direction.Right) {
            targetRotation = startingRotation + degrees;
            if (targetRotation >= 180) {
                targetRotation -= 360;
            }
            while (this.yaw < targetRotation) {
                DragonoidsGlobal.setDrivePower(turnPower, -turnPower);
                waitOneFullHardwareCycle();
            }
        }
        DragonoidsGlobal.stopMotors();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.initialize();
        waitForStart();

        while (opModeIsActive()) {
            // Choose flow based on alliance color

            // Drive forward a bit

            // Use the phone's IMU to make a precise 45 degree turn

            // Drive forward to the beacon zone

            // Turn 45 degrees again

            // Drive forward to color detection distance

            // Detect color of the beacon

            // Drive forward or extend arm to push the correct button

            // Deposit climbers in the bucket behind the beacon

            // Reverse out of the beacon area (or turn 180 degrees and then drive forward)

            // Turn -45 degrees

            // Drive forward as far as possible up the mountain

            // Use the "churro grabbers" to gain more traction and hoist the robot up the
            // remaining portion of the mountain after the normal wheels begin to slip
        }
        DragonoidsGlobal.stopAll();
    }
}