package org.firstinspires.ftc.teamcode.PreviousSeasonOpModes.VelocityVortexOpModes;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class StateHardwareMap {
    //Setting up empty hardware map
    HardwareMap hwMap =  null;

    //Declaing motor variables
    public DcMotor fleft = null; //Front left drive motor
    public DcMotor fright = null; //Front right drive motor
    public DcMotor bleft = null; //Back left drive motor
    public DcMotor bright = null; //Back right drive motor
    public DcMotor intake = null; //Intake motor
    public DcMotor flicker = null; //Flicker motor

    //Declaring servo variables
    public Servo leftBeacon = null; //Hits left beacon button
    public Servo rightBeacon = null; //Hits right beacon button

    //Declaring empty sensor variables here to make them global
    public TouchSensor touch = null;
    public ColorSensor color = null;
    public LightSensor leftLight = null;
    public LightSensor rightLight = null;
    public UltrasonicSensor ultrasonic = null;
    public BNO055IMU imu = null;

    public float heading;
    public float lastHeading;
    float rawGyro;
    float gyroAdd = 0;

    //Change wheelDiameter depending on the diameter of the drive wheels
    public static final double WHEEL_DIAMETER = 4;//inches

    //Set driveSpeed depending on speed preference
    public static final double DRIVE_SPEED = 0.5f;

    //colorThreshold is 1/2 * (line color + floor color)
    public static final double COLOR_THRESHOLD = .26f;

    //Constructor
    public StateHardwareMap(){}

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        //Setting up data for gyro sensors
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        updateGyro();

        //Assigning values to the previously declared motor, beacon, and sensor variables
        fleft = hwMap.dcMotor.get("fright");
        fright = hwMap.dcMotor.get("fleft");
        bleft = hwMap.dcMotor.get("bright");
        bright = hwMap.dcMotor.get("bleft");
        flicker = hwMap.dcMotor.get("flicker");
        intake = hwMap.dcMotor.get("intake");

        //Reversing right motors so that they both go the same way
        fleft.setDirection(DcMotor.Direction.REVERSE);
        bleft.setDirection(DcMotor.Direction.REVERSE);

        //Reversing Flicker motor so that positive powers make it shoot
        flicker.setDirection(DcMotor.Direction.REVERSE);

        //Setting up servo variables(these are continuous rotation servos but are setup as regular ones to simplify it)
        leftBeacon = hwMap.servo.get("rightBeacon");
        rightBeacon = hwMap.servo.get("leftBeacon");

        //Setting up sensors
        touch = hwMap.touchSensor.get("touch");
        color = hwMap.colorSensor.get("color");
        leftLight = hwMap.lightSensor.get("rightLight");
        rightLight = hwMap.lightSensor.get("leftLight");
        ultrasonic = hwMap.ultrasonicSensor.get("ultrasonic");
        imu = hwMap.get(BNO055IMU.class, "imu"); //Gyro sensor

        //Turning on sensor light to enhance accuracy of readings
        color.enableLed(false);
        leftLight.enableLed(true);
        rightLight.enableLed(true);
    }

    //Updates the gyro sensor and formats the angle so that it is easier to use
    public void updateGyro(){
        //Gets the raw value of the gyro sensor
        rawGyro = -imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX).firstAngle;

        //Detects if the gyro sensor goes from 0-360 or 360-0 and adjusts gyroAdd to compensate
        if (lastHeading < 60 && rawGyro > 300) {
            gyroAdd = gyroAdd + 360;
        } else if (lastHeading > 300 && rawGyro < 60) {
            gyroAdd = gyroAdd - 360;
        }

        //Puts formatted angle in heading variable and sets the current value as last value for the next cycle
        heading = gyroAdd - rawGyro;
        lastHeading = rawGyro;
    }
}