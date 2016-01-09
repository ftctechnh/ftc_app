package com.powerstackers.resq.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * @author Derek Helm
 */
public class BlueAlianceOP extends OpMode {

    /*
     * TETRIX VALUES.
     */
    final static double servoBeacon_MIN_RANGE  = 0.00;
    final static double servoBeacon_MAX_RANGE  = 1.00;

    /*
     * position of servo <Value of Variable>
     */
    double servoBeaconPosition;

    /*
     * Color Values
     */
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;

    DeviceInterfaceModule cdim;
    ColorSensor colorSensor;
    //    TouchSensor touchSensor;
    Servo servoBeacon;
    DcMotor motorFRight;
    DcMotor motorFLeft;
    DcMotor motorBRight;
    DcMotor motorBLeft;

    public BlueAlianceOP() {

    }

    @Override
    public void init() {


        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */

        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        //Sensors
        colorSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
        colorSensor.enableLed(true);
//        touchSensor = hardwareMap.touchSensor.get("touchSensor");
        /*
         * Servos
         */
        servoBeacon = hardwareMap.servo.get("servoBeacon");
        servoBeaconPosition = 0.50;

        /*
         * Motors
         */
        motorFRight = hardwareMap.dcMotor.get("motorFRight");
        motorFLeft = hardwareMap.dcMotor.get("motorFLeft");
        motorFRight.setDirection(DcMotor.Direction.REVERSE);
        motorBRight = hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);


    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#
     */
    @Override
    public void loop() {

        //ColorSensor Controls
        if (colorSensor.red() > colorSensor.blue()) {
            servoBeaconPosition = 0.20;

        } else if (colorSensor.blue() > colorSensor.red()) {
            servoBeaconPosition = 0.80;
        } else {
            servoBeaconPosition = 0.50;
        }




        /*
         * clip the position values so that they never exceed their allowed range.
         */
        servoBeaconPosition = Range.clip(servoBeaconPosition, servoBeacon_MIN_RANGE, servoBeacon_MAX_RANGE);

        /*
         * write position values to the servos
         */
        servoBeacon.setPosition(servoBeaconPosition);

        /*
         * Send telemetry data back to driver station. N
         motorFRight.setPower(right);
         motorFLeft.setPower(left);ote that if we are using
         * a legacy NXT-compatible motor controller, then the getPower() method
         * will return a null value. The legacy NXT-compatible motor controllers
         * are currently write only.
         */
        telemetry.addData("Text", "*** Robot Data***");

        /*
         * Color Telemetry
         */
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());

        /*
         * servo Telemetry
         */
        telemetry.addData("servoBeacon", "position: " + String.valueOf(servoBeaconPosition));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

}
