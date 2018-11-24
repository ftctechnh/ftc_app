package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

//This class defines all the specific hardware for our robot.

public class MasterHardwareClass {
    /* Public OpMode members. */
    public DcMotor myMotor = null;
    public Servo myServo = null;
    public DigitalChannel myTouchSensor = null;
    public ColorSensor myColorSensor = null;
    public BNO055IMU imu;

    /* Define hardwaremap */
    HardwareMap hardwareMap = null;

    /* Constructor */
    public MasterHardwareClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {

        // Save reference to Hardware map
        hardwareMap = ahwMap;

        // Get color sensor from xml/configuration
        myMotor = hardwareMap.dcMotor.get("MM");
        myServo = hardwareMap.servo.get("MS");
        myTouchSensor = hardwareMap.get(DigitalChannel.class, "MTS");
        myColorSensor = hardwareMap.colorSensor.get("MCS");

        /* Set the digital channel to input. */
        myTouchSensor.setMode(DigitalChannel.Mode.INPUT);

        // Set hardward to it's default position
        myMotor.setPower(0);
        myServo.setPosition(.5);


    /* Set parameters for the gyro (imu)*/
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
    }
}
