package com.powerstackers.resq.opmodes.misc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * @author Derek Helm
 */
public class Tedbot extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the arm servo approaches 0, the arm position moves up (away from the floor).
     * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
     */
    /* TETRIX VALUES.
     *
     */
    final static double servoBeacon_MIN_RANGE  = 0.00;
    final static double servoBeacon_MAX_RANGE  = 1.00;

    /* position of servo <Value of Variable>
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

    public Tedbot() {

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

        /*
         * Sensors
         */
        colorSensor = ClassFactory.createSwerveColorSensor(this,
                this.hardwareMap.colorSensor.get("colorSensor"));
        colorSensor.enableLed(true);
        colorFSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorFSensor"));
        colorFSensor.enableLed(true);
        touchSensor = hardwareMap.touchSensor.get("touchSensor");

        /*
         * Motors
         */
        motorBRight = hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);
        /*
         * Servos
         */
        servoBeacon = hardwareMap.servo.get("servoBeacon");

    }

    /**
     * This method will be called repeatedly in a loop
     *
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * lift/Brushes via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control the robot more precisely
        // at slower speeds.
        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

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

        // clip the position values so that they never exceed their allowed range.
        servoBeaconPosition = Range.clip(servoBeaconPosition, servoBeacon_MIN_RANGE, servoBeacon_MAX_RANGE);

        // write position values to the servos
        servoBeacon.setPosition(servoBeaconPosition);

        // write the values to the motors
        motorBRight.setPower(right);
        motorBLeft.setPower(left);

		/*
		 * Send telemetry data back to driver station. N
        motorFRight.setPower(right);
        motorFLeft.setPower(left);ote that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

        //Color Telemetry
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("blue", colorSensor.blue());

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }


    /**
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}