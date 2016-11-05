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

@Autonomous(name = "LineSensors", group = "")
public class LineSensors extends OpMode {
    ModernRoboticsI2cColorSensor cs;
    Servo buttonPusher;
    ModernRoboticsUsbDeviceInterfaceModule interfaceModule;
    double cs1;
    double cs2;
    double cs3;

    public void init() {
        cs = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "CS");
        buttonPusher = hardwareMap.servo.get("buttonPusher");
        interfaceModule = hardwareMap.get(ModernRoboticsUsbDeviceInterfaceModule.class, "CDI");

    }

    public void loop() {
        telemetry.update();
        telemetry.addData("vex1:", cs1);
        telemetry.addData("vex2:", cs2);
        telemetry.addData("vex3:", cs3);
        telemetry.addData("red:", cs.red());
        telemetry.addData("blue:", cs.blue());

        cs1 = interfaceModule.getAnalogInputVoltage(0);
        cs2 = interfaceModule.getAnalogInputVoltage(1);
        cs3 = interfaceModule.getAnalogInputVoltage(2);

    }
}

