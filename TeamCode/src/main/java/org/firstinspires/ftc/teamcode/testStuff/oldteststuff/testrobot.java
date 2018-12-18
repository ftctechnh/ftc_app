package org.firstinspires.ftc.teamcode.testStuff.oldteststuff;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * The class that handles the initialization and calibration of all of the variables required for the testrobot
 */
public class testrobot
{

    //TODO make sure all motors, servos, and sensors are declared and defined

    protected DcMotor frontRightDrive;
    protected DcMotor frontLeftDrive;
    protected DcMotor backRightDrive;
    protected DcMotor backLeftDrive;
    protected DcMotor armLiftMotor;
    protected DcMotor armTiltMotor;

    protected Servo frontServo;

    protected ColorSensor colorSensor;
    protected DigitalChannel magLimitSwitch;


    private Telemetry telemetry;
    private HardwareMap hardwareMap;

    public testrobot(Telemetry telemetry, HardwareMap hardwareMap, LinearOpMode linearOpMode)
    {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
    }

    public testrobot(Telemetry telemetry, HardwareMap hardwareMap) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
    }

    /***
     * Handles the initialization and direction setting of all motors
     */
    public void initMotors()
    {
        frontRightDrive = hardwareMap.dcMotor.get("front_right_drive");
        frontLeftDrive = hardwareMap.dcMotor.get("front_left_drive");
        backRightDrive = hardwareMap.dcMotor.get("back_right_drive");
        backLeftDrive =  hardwareMap.dcMotor.get("back_left_drive");
        armLiftMotor = hardwareMap.dcMotor.get("arm_lift_motor");
        armTiltMotor = hardwareMap.dcMotor.get("arm_tilt_motor");

    }

    /***
     * Handles the initialization and direction setting of all servos
     */
    public void initServos()
    {
        frontServo = hardwareMap.servo.get("front_servo");


    }

    /***
     * Handles the initialization and initial calibration of all sensors
     */
    public void initSensors()
    {
        colorSensor = hardwareMap.colorSensor.get("color_sensor");
        magLimitSwitch = hardwareMap.digitalChannel.get("mag_limit_switch");

    }


    /**
     * Runs initMotors(), initServos(), and initSensors()
     */
    public void initAllHardware()
    {
        initMotors();
        initServos();
        initSensors();
    }
}