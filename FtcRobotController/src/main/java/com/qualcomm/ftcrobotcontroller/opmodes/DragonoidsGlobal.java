package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DragonoidsGlobal {
    // Drive motors
    public static DcMotor rightOne, rightTwo, leftOne, leftTwo;
    // Aux motors
    public static DcMotor knocker, conveyor;
    // Servos
    public static Servo gate;

    public static void init(HardwareMap hardwareMap) {
        rightOne = hardwareMap.dcMotor.get("rightOneDrive");
        rightTwo = hardwareMap.dcMotor.get("rightTwoDrive");
        leftOne = hardwareMap.dcMotor.get("leftOneDrive");
        leftTwo = hardwareMap.dcMotor.get("leftTwoDrive");

        rightOne.setDirection(DcMotor.Direction.REVERSE);
        leftOne.setDirection(DcMotor.Direction.REVERSE);

        conveyor = hardwareMap.dcMotor.get("conveyor");
        knocker = hardwareMap.dcMotor.get("knocker");

        //gate = hardwareMap.servo.get("gate");
    }

    public static void setDrivePower(double rightPower, double leftPower) {
        rightOne.setPower(rightPower);
        rightTwo.setPower(rightPower);
        leftOne.setPower(leftPower);
        leftTwo.setPower(leftPower);
    }
    public static void stopMotors() {
        setDrivePower(0, 0);
    }
    public static void stopAll() {
        // Stop all motors
        stopMotors();
        conveyor.setPower(0);
        knocker.setPower(0);
    }
}