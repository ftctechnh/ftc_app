package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class DragonoidsAuto extends LinearOpMode implements SensorEventListener {
    // Gyro sensor
    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private int sensorType = Sensor.TYPE_GYROSCOPE;
    private static final float nanoSecondsToSeconds = 1.0f / 1000000000.0f;
    private float lastGyroTimestamp = 0;
    private float heading = 0; // In radians
    private float headingDegrees = 0; // In degrees (use in autonomous flow)
    private final float secondsToCalibrate = 5;
    private boolean calibrationComplete = false;
    private float firstGyroTimestamp = 0;
    private float totalError = 0;
    private double headingCompensation = 0;
    // Autonomous constants
    private final double drivePower = 0.4;
    private final double driveMinPower = 0.35;
    private final double turnPower = 0.4;
    private final int step1Distance = 500;
    private final int step2Distance = 2000;
    private final int step3Distance = 500;

    public void initialize() {
        DragonoidsGlobal.init(hardwareMap);
        DragonoidsGlobal.stopAll();
        // Set up the gyro sensor
        this.sensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        this.gyroSensor = sensorManager.getDefaultSensor(this.sensorType);
        if (this.gyroSensor != null) {
            this.sensorManager.registerListener(this, this.gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else {
            telemetry.addData("Error", "Gyroscope sensor not found");
        }
    }
    private void outputTelemetry() {
        telemetry.addData("Heading", headingDegrees);
        telemetry.addData("RightOne", DragonoidsGlobal.rightOne.getCurrentPosition());
        telemetry.addData("RightTwo", DragonoidsGlobal.rightTwo.getCurrentPosition());
        telemetry.addData("Left  One", DragonoidsGlobal.leftOne.getCurrentPosition());
        telemetry.addData("Left  Two", DragonoidsGlobal.leftTwo.getCurrentPosition());
    }
    // For gyro sensor data
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != this.sensorType) return;

        final float dT = (event.timestamp - lastGyroTimestamp) * nanoSecondsToSeconds;
        if (lastGyroTimestamp != 0) {
            heading += (dT * event.values[2]);
            headingDegrees = (float) Math.toDegrees(heading);
        }
        lastGyroTimestamp = event.timestamp;

        this.outputTelemetry();
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
        Right, Left, Forward, Backward
    }

    public void turn(Direction direction, float degrees) throws InterruptedException {
        float startingRotation = this.headingDegrees;
        float targetRotation;

        if (direction == Direction.Right) {
            targetRotation = startingRotation - degrees;
            while (this.headingDegrees > targetRotation) {
                DragonoidsGlobal.setDrivePower(turnPower, -turnPower);
                waitOneFullHardwareCycle();
            }
        }

        if (direction == Direction.Left) {
            targetRotation = startingRotation + degrees;
            while (this.headingDegrees < targetRotation) {
                DragonoidsGlobal.setDrivePower(-turnPower, turnPower);
                waitOneFullHardwareCycle();
            }
        }
        DragonoidsGlobal.stopMotors();
    }
    public void drive(Direction direction, int distance) {
        while ((this.getLeftEncoderValue() + this.getRightEncoderValue()) / 2 < distance) {
            if (direction == Direction.Forward) {
                DragonoidsGlobal.setDrivePower(drivePower, drivePower);
            }
            if (direction == Direction.Backward) {
                DragonoidsGlobal.setDrivePower(-drivePower, -drivePower);
            }
        }
        DragonoidsGlobal.stopMotors();
    }
    public void driveTime(Direction direction, long milliseconds) throws InterruptedException {
        if (direction == Direction.Forward) {
            DragonoidsGlobal.setDrivePower(drivePower, drivePower);
        }
        if (direction == Direction.Backward) {
            DragonoidsGlobal.setDrivePower(-drivePower, -drivePower);
        }
        double startTime = getRuntime();
        double runTime = milliseconds / 1000.0;
        while ((getRuntime() - startTime) < runTime) {
            waitOneFullHardwareCycle();
        }
        DragonoidsGlobal.stopMotors();
    }

    protected enum Alliance {
        Red, Blue
    }
    @Override
    public void runOpMode() throws InterruptedException {
        // Change this and upload depending on alliance color
        final Alliance alliance = Alliance.Blue;
        final Direction turnDirection = (alliance == Alliance.Blue) ? Direction.Right : Direction.Left;

        try {
            this.initialize();
            waitForStart();
            // Run the conveyor backwards so that debris doesn't get caught in the robot
            DragonoidsGlobal.conveyor.setPower(-0.25);
            // Choose flow based on alliance color (we're assuming red)

            // Drive forward a bit
            //this.drive(Direction.Forward, step1Distance);
            this.driveTime(Direction.Forward, 1000);
            // Use the phone's IMU to make a precise 45 degree turn
            this.turn(turnDirection, 45);
            // Drive forward to the beacon zone
            //this.drive(Direction.Forward, step2Distance);
            this.driveTime(Direction.Forward, 2500);
            // Turn 45 degrees again
            this.turn(turnDirection, 40);
            // Drive forward to color detection distance
            //this.drive(Direction.Forward, step3Distance);
            double odsStartTime = getRuntime();
            double maxRunTime = 10; // 10 seconds before watchdog timer kicks in and stops the robot
            while (DragonoidsGlobal.opticalDistanceSensor.getLightDetected() < 0.1 && (getRuntime() - odsStartTime) < maxRunTime) {
                DragonoidsGlobal.setDrivePower(driveMinPower, driveMinPower);
                waitOneFullHardwareCycle();
            }
            DragonoidsGlobal.stopMotors();
            // Deposit climbers into bucket
            DragonoidsGlobal.autonomousClimbers.setPosition(0.0);
            sleep(1000);
            DragonoidsGlobal.autonomousClimbers.setPosition(1.0);
            // Detect color of the beacon
            if (DragonoidsGlobal.colorSensor.red() > DragonoidsGlobal.colorSensor.blue()) {
                // Red color detected

            }
            else {
                // Blue color detected

            }
            // Drive forward or extend arm to push the correct button

            // Deposit climbers in the bucket behind the beacon

            // Reverse out of the beacon area (or turn 180 degrees and then drive forward)

            // Turn -45 degrees

            // Drive forward as far as possible up the mountain

            // Use the "churro grabbers" to gain more traction and hoist the robot up the
            // remaining portion of the mountain after the normal wheels begin to slip

            DragonoidsGlobal.stopAll();
        }
        catch (Exception e) {
            DragonoidsGlobal.stopAll();
            throw e;
        }
    }
}