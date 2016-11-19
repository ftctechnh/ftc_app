package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by FTC Team 4799-4800 on 10/13/2016.
 */

@Autonomous(name = "4800AutoRed", group = "")
public class Red4800 extends OpMode {
    //DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    //DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    ModernRoboticsI2cColorSensor cs;
    Servo buttonPusher;
    ModernRoboticsUsbDeviceInterfaceModule interfaceModule;
    double cs1;
    double cs2;
    double cs3;
    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        cs = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "CS");
        buttonPusher = hardwareMap.servo.get("buttonPusher");
        interfaceModule = hardwareMap.get(ModernRoboticsUsbDeviceInterfaceModule.class, "CDI");

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void loop() {
        motorBackLeft.setTargetPosition(10000);
        motorBackRight.setTargetPosition(-10000);
        motorFrontRight.setTargetPosition(-10000);
        motorFrontLeft.setTargetPosition(10000);
        telemetry.update();
        telemetry.addData("vex1:", cs1);
        telemetry.addData("vex2:", cs2);
        telemetry.addData("vex3:", cs3);
        telemetry.addData("red:", cs.red());
        telemetry.addData("blue:", cs.blue());
        telemetry.addData("isBusy():", motorBackLeft.isBusy());

        cs1 = interfaceModule.getAnalogInputVoltage(0);
        cs2 = interfaceModule.getAnalogInputVoltage(1);
        cs3 = interfaceModule.getAnalogInputVoltage(2);

        if (motorBackLeft.isBusy()) {
            motorBackLeft.setPower(.5);
            motorBackRight.setPower(.5);
            motorFrontLeft.setPower(.69*.5);
            motorFrontRight.setPower(.69*.5);
            if (cs1<3) {
                motorBackLeft.setPower(.2);
                motorBackRight.setPower(.5);
                motorFrontLeft.setPower(.69*.2);
                motorFrontRight.setPower(.69*.5);
            }
            else if (cs3<3){
                motorBackLeft.setPower(.5);
                motorBackRight.setPower(.2);
                motorFrontLeft.setPower(.69*.5);
                motorFrontRight.setPower(.69*.2);
            }
        }
        else{
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }

        if (cs.red()>20 || cs.blue()>20){
            if (cs.red()>20)
                buttonPusher.setPosition(-1);
            else
                buttonPusher.setPosition(.5);
            motorBackLeft.setPower(.5);
            motorBackRight.setPower(.5);
            motorFrontLeft.setPower(.69*.5);
            motorFrontRight.setPower(.69*.5);
        }
    }
}

