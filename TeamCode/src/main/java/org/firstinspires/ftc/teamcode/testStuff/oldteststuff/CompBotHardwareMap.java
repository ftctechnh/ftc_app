package org.firstinspires.ftc.teamcode.testStuff.oldteststuff;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CompBotHardwareMap
{
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;

    public Servo markerServo = null;

    public ColorSensor colorSensorLeft = null;
    public ColorSensor colorSensorRight = null;

    private DcMotor.RunMode initialMode = null;

    HardwareMap hwMap = null;

    public CompBotHardwareMap (DcMotor.RunMode enteredMode)
    {
        initialMode = enteredMode;
    }

    public void init (HardwareMap ahwMap)
    {

        hwMap = ahwMap;

        //motor initialization
        leftFrontDrive = hwMap.dcMotor.get("left_front_drive");
        leftBackDrive = hwMap.dcMotor.get("left_back_drive");
        rightFrontDrive = hwMap.dcMotor.get("right_front_drive");
        rightBackDrive = hwMap.dcMotor.get("right_back_drive");

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setMode(initialMode);
        leftBackDrive.setMode(initialMode);
        rightFrontDrive.setMode(initialMode);
        rightBackDrive.setMode(initialMode);

        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);

        //servo initialization
        markerServo = hwMap.servo.get("marker_servo");

        markerServo.setPosition(0);
        //color sensor initialization

        colorSensorLeft = hwMap.colorSensor.get("color_sensor_left");
        colorSensorRight = hwMap.colorSensor.get("color_sensor_right");

        colorSensorLeft.enableLed(false);
        colorSensorRight.enableLed(false);
    }
}
