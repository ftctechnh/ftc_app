package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//This class defines all the specific hardware for a the BACONbot robot.

public class MasterHardwareClassRIGHTNOW {

    /* Public OpMode members. */
    public ColorSensor sensorColorRight;
    public Servo gemServo;
    public BNO055IMU imu;

    /* local OpMode members. */
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;

    /* Give place holder values for the motors and the grabber servo */
    double FrontLeftPower = 0;
    double FrontRightPower = 0;
    double BackRightPower = 0;
    double BackLeftPower = 0;

    /* Define values used in commanding the claw */
    static double clawClose = .5;
    static double clawOpen = -.5;
    static double clawStill = 0;
    static double xPosUp = 1;
    static double xPosDown = .5;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public MasterHardwareClassRIGHTNOW() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Hardware

        // Define and Initialize Hardware
        frontLeftMotor = hwMap.dcMotor.get("FL");
        frontRightMotor = hwMap.dcMotor.get("FR");
        backLeftMotor = hwMap.dcMotor.get("BL");
        backRightMotor = hwMap.dcMotor.get("BR");
        imu = hwMap.get(BNO055IMU.class, "imu");
        gemServo = hwMap.servo.get("gemservo");
        sensorColorRight = hwMap.colorSensor.get("colorsensor");

        // Set all hardware to default position
        // Set all hardware to default position
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        gemServo.setPosition(xPosUp);


        // Set proper encoder state for all motor
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        gemServo = hwMap.get(Servo.class, "gemservo");

    }
}
