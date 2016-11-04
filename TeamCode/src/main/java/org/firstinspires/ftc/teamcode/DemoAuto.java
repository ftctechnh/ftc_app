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

@Autonomous(name = "DemoAuto", group = "")
public class DemoAuto extends OpMode {
    //DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    //DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    ModernRoboticsI2cColorSensor cs;
    //Servo buttonPusher;
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
        //buttonPusher = hardwareMap.servo.get("buttonPusher");
        interfaceModule = hardwareMap.get(ModernRoboticsUsbDeviceInterfaceModule.class, "CDI");

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void loop() {
        cs1 = interfaceModule.getAnalogInputVoltage(0);
        cs2 = interfaceModule.getAnalogInputVoltage(1);
        cs3 = interfaceModule.getAnalogInputVoltage(2);

        if (Math.abs(-6000-motorBackLeft.getCurrentPosition())>100) {
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(1);
            motorFrontLeft.setPower(-1);
            motorFrontRight.setPower(1);
            if (cs1<100 && cs2<100) {
                motorBackLeft.setPower(-.5);
                motorBackRight.setPower(1);
                motorFrontLeft.setPower(-.5);
                motorFrontRight.setPower(1);
            }
            else if (cs3<100 && cs2<100){
                motorBackLeft.setPower(-1);
                motorBackRight.setPower(.5);
                motorFrontLeft.setPower(-1);
                motorFrontRight.setPower(.5);
            }
        }
        else{
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }

        if (cs.red()>200 || cs.blue()>200){
            if (cs.red()>200);
        //        buttonPusher.setPosition(-1);
            else;
        //        buttonPusher.setPosition(1);
        }
    }
}

