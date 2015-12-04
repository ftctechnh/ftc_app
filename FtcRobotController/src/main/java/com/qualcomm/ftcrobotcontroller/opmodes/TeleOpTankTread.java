/**
 * Created by edengh on 10/6/15.
 */
//includes ramp pusher

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.ndhsb.ftc7593.tbc;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */

/**
 *
 *
class TankThread implements Runnable {
    private Thread t;
    private String Drive;

    TankThread( String ArcadeDrive){
        Drive = ArcadeDrive;
        System.out;
    }
}
 */

public class TeleOpTankTread extends OpMode {

    // amount to change the tape servo position.
    double mtapeDelta = 0.1;

    float servoInput = 0.5f;
    float bservoSpeed = 0.5f;

    public ElapsedTime mRuntime = new ElapsedTime();   // Time into round. // MPH

    double eventStart = 0.0;

    /**
     * Constructor
     */
    public TeleOpTankTread() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        tbc.hardwareMap = hardwareMap;
        tbc.initHardwareMap();

        mRuntime.reset();           // Zero game clock
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        float drivespeed = -gamepad1.left_stick_y;
        float driveturn = gamepad1.right_stick_x;
        float hook = gamepad2.left_stick_y;
        float pusher = gamepad2.right_stick_y;

        float right = drivespeed - driveturn;
        float left = drivespeed + driveturn;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);
        hook = Range.clip(hook, -1, 1);
        pusher = Range.clip(pusher, -1,1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);
        hook = (float)scaleInput(hook);
        pusher = (float)scaleInput(pusher);

        // write the values to the motors

        tbc.setMotorRRightPower(right);
        tbc.setMotorRLeftPower(left);
        tbc.setMotorFRightPower(right);
        tbc.setMotorFLeftPower(left );

        tbc.setMotorHookPower(hook);

        if(gamepad1.x) {
            tbc.buttonServoSpeed = 0.0f;
        }
        if(gamepad1.y) {
            tbc.buttonServoSpeed = 1.0f;
        }
        if(gamepad1.x != true && gamepad1.y != true) {
            tbc.buttonServoSpeed = 0.5f;
        }
        tbc.setButtonServoSpeed(tbc.buttonServoSpeed);

        if(gamepad2.a) {
            tbc.climberPosition = tbc.CLIMBER_MAX_RANGE;
        }
        if(gamepad2.b) {
            tbc.climberPosition = tbc.CLIMBER_MIN_RANGE;
        }
        tbc.climberPosition = Range.clip(tbc.climberPosition, tbc.CLIMBER_MIN_RANGE, tbc.CLIMBER_MAX_RANGE);
        tbc.setClimberPosition(tbc.climberPosition);

        if(gamepad2.right_bumper) {
            tbc.snowplowPosition = tbc.SNOWPLOW_MAX_RANGE;
        }
        if(gamepad2.left_bumper) {
            tbc.snowplowPosition = tbc.SNOWPLOW_MIN_RANGE;
        }
        tbc.snowplowPosition = Range.clip(tbc.snowplowPosition, tbc.SNOWPLOW_MIN_RANGE, tbc.SNOWPLOW_MAX_RANGE);
        tbc.setSnowplowPosition(tbc.snowplowPosition);

        if(gamepad2.x) {
            tbc.sliderPosition = tbc.SLIDER_MAX_RANGE;
        }
        if(gamepad2.y) {
            tbc.sliderPosition = tbc.SLIDER_MIN_RANGE;
        }
        tbc.sliderPosition = Range.clip(tbc.sliderPosition, tbc.SLIDER_MIN_RANGE, tbc.SLIDER_MAX_RANGE);
        tbc.setSliderPosition(tbc.snowplowPosition);

        // MPH
        Double mtapeNewPos = tbc.mtapePosition;
        if(gamepad1.right_bumper) {
            if ((mRuntime.time() - eventStart) > 0.1) {
                mtapeNewPos = tbc.mtapePosition + mtapeDelta;
                eventStart = mRuntime.time();
            }
        }
        if(gamepad1.left_bumper) {
            if ((mRuntime.time() - eventStart) > 0.1) {
                mtapeNewPos = tbc.mtapePosition - mtapeDelta;
                eventStart = mRuntime.time();
            }
        }
        tbc.mtapePosition = Range.clip(mtapeNewPos, tbc.MTAPE_MIN_RANGE, tbc.MTAPE_MAX_RANGE);
        tbc.setMtapePosition(tbc.mtapePosition);

        if ((!gamepad1.right_bumper) && (!gamepad1.left_bumper)) {
            eventStart = mRuntime.time();
        }

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        // telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        // telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("servo in",  "servo in: " + String.format("%.2f", servoInput));
        telemetry.addData("pusher servo",  "pusher servo: " + String.format("%.2f", pusher));
        telemetry.addData("button servo", "button servo: " + String.format("%.2f", tbc.buttonServoSpeed));
        telemetry.addData("climber", "climber:  " + String.format("%.2f", tbc.climberPosition));
        telemetry.addData("slider", "slider: " + String.format("%.2f", tbc.sliderPosition));
        telemetry.addData("mtape", "mtape: " + String.format("%.2f", tbc.mtapePosition));

        // if LinearOpMode
        // waitOneFullHardwareCycle();
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

        tbc.buttonServoSpeed = 0.5f;
        tbc.setButtonServoSpeed(tbc.buttonServoSpeed);

        tbc.destroyHardwareMap();
    }

    /*
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
