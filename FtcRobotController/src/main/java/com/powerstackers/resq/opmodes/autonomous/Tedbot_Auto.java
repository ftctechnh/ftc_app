package com.powerstackers.resq.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.swerverobotics.library.ClassFactory;

/**
 * Created by Derek on 12/23/2015.
 */
public class Tedbot_Auto extends LinearOpMode {

    final static double servoBeacon_MIN_RANGE  = 0.00;
    final static double servoBeacon_MAX_RANGE  = 1.00;

    /** position of servo <Value of Variable>
     *
     */
    double servoBeaconPosition;

    //Color Values
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;

    DeviceInterfaceModule cdim;
    ColorSensor colorSensor;
    ColorSensor colorFSensor;
    TouchSensor touchSensor;
    DcMotor motorBRight;
    DcMotor motorBLeft;
    Servo servoBeacon;

    @Override
    public void runOpMode() throws InterruptedException {

        /**
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */

        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        /**
         * Sensors
         */
        colorSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
        colorSensor.enableLed(true);
        colorFSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorFSensor"));
        colorFSensor.enableLed(true);
        touchSensor = hardwareMap.touchSensor.get("touchSensor");

        /**
         * Motors
         */
        motorBRight = hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);
        /**
         * Servos
         */
        servoBeacon = hardwareMap.servo.get("servoBeacon");

        // wait for the start button to be pressed
        waitForStart();



        /** ColorSensor Controls
         *
         */
        if (colorSensor.blue() > colorSensor.red()) {
            servoBeaconPosition = 0.20;

        } else if (colorSensor.red() > colorSensor.blue()) {
            servoBeaconPosition = 0.80;
        } else {
            servoBeaconPosition = 0.50;
        }


    }
}
