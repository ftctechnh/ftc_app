package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DragonoidsGlobal {
    // Drive motors
    public static DcMotor rightOne, rightTwo, leftOne, leftTwo;
    // Aux motors
    public static DcMotor conveyor;
    // Slider motors
    public static DcMotor leftSlider, rightSlider;
    // Servos
    public static Servo gate;

    public static void init(HardwareMap hardwareMap) {
        rightOne = hardwareMap.dcMotor.get("rightOneDrive");
        rightTwo = hardwareMap.dcMotor.get("rightTwoDrive");
        leftOne = hardwareMap.dcMotor.get("leftOneDrive");
        leftTwo = hardwareMap.dcMotor.get("leftTwoDrive");

        rightTwo.setDirection(DcMotor.Direction.REVERSE);
        leftTwo.setDirection(DcMotor.Direction.REVERSE);

        conveyor = hardwareMap.dcMotor.get("conveyor");

        leftSlider = hardwareMap.dcMotor.get("leftSlider");
        rightSlider = hardwareMap.dcMotor.get("rightSlider");

        leftSlider.setDirection(DcMotor.Direction.REVERSE);

        gate = hardwareMap.servo.get("gate");
        resetServos();
    }

    public static void setDrivePower(double rightPower, double leftPower) {
        rightOne.setPower(rightPower);
        rightTwo.setPower(rightPower);
        leftOne.setPower(leftPower);
        leftTwo.setPower(leftPower);
    }

    public static void resetServos(){
        gate.setPosition(0.75);
    }

    public static void stopMotors() {
        setDrivePower(0, 0);
    }
    public static void stopAll() {
        // Stop all motors
        stopMotors();
        conveyor.setPower(0);
        leftSlider.setPower(0);
        rightSlider.setPower(0);
        resetServos();
    }
}