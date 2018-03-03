package org.firstinspires.ftc.teamcode.References;

/*
 * Howdy! This class defines all the hardware, constants, and variables
 * that are called in the methodList!
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

public class relicClass {

    /* Define hardward */
    DcMotor verticalArmMotor;
    DcMotor trayMotor;
    DcMotor P1Motor = null;
    DcMotor P2Motor = null;
    Servo gemServo;
    ColorSensor colorSensor;
    DigitalChannel topTouch;
    DigitalChannel bottomTouch;
    double xPosUp = 0;
    double xPosDown = .55;
    int trayOut = -390;
    int trayIn = 10;
}
