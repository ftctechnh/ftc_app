package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class TestAutonHardware
{
    /* Public OpMode members. */
    public DcMotor  MotorFrontRight;
    public DcMotor MotorFrontLeft;
    public DcMotor MotorRearRight;
    public DcMotor MotorRearLeft;
    public ColorSensor color = null;
    public Servo servo1 = null;
    public BNO055IMU imu;
   // public ModernRoboticsI2cRangeSensor MRrange = null;

    public double heading = 0;



    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public TestAutonHardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        MotorFrontRight   = hwMap.dcMotor.get("motor_front_right");
        MotorFrontLeft = hwMap.dcMotor.get("motor_front_left");
        MotorRearRight = hwMap.dcMotor.get("motor_rear_right");
        MotorRearLeft = hwMap.dcMotor.get("motor_rear_left");

        color = hwMap.get(ColorSensor.class, "MR_color");

        imu = hwMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);

        // Set all motors to zero power
        MotorFrontRight.setPower(0);
        MotorFrontLeft.setPower(0);
        MotorRearRight.setPower(0);
        MotorRearLeft.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
//        MotorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        MotorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        MotorRearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        MotorRearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /* Android in-built gyro register */

        SensorEventListener orientationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                heading = event.values[0];
                /*azimuth_angle = event.values[0];
                pitch_angle = event.values[1];
                roll_angle = event.values[2];*/
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // do not use this method
            }
        };

        SensorManager sensorManager;
        sensorManager =
                (SensorManager) ahwMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(orientationListener , sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);


    }



    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}

