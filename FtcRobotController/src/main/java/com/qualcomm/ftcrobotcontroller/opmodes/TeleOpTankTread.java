package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
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
    double mtapeDelta = 0.001;
    double climberDelta = 0.005;
    double snowplowDelta = 0.005;
    double reflection;

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

        tbc.initServoValues();

        tbc.setClimberPosition(tbc.climberPosition);
        tbc.setSliderLPosition(tbc.sliderLPosition);
        tbc.setSliderRPosition(tbc.sliderRPosition);
        tbc.setSnowplowPosition(tbc.snowplowPosition);
        tbc.setMtapePosition(tbc.mtapePosition);
        tbc.setButtonServoSpeed(tbc.buttonServoSpeed);
        //tbc.setMotorRRightPower(0.0f);
        //tbc.setMotorRLeftPower(0.0f);
        //tbc.setMotorFLeftPower(0.0f);
       // tbc.setMotorFRightPower(0.0f);
        tbc.setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
        //tbc.setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);


        if (tbc.sc != null) {
            tbc.sc.pwmEnable(); // enable servo controller PWM outputs
        }

        mRuntime.reset();           // Zero game clock
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        tbc.setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        reflection = tbc.light1.getLightDetected();
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
        float hook = -gamepad2.right_stick_y;
        float intake = gamepad2.left_stick_y;

        float right = drivespeed - driveturn;
        float left = drivespeed + driveturn;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);
        hook = Range.clip(hook, -1, 1);
        intake = Range.clip(intake, -1,1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);
        hook = (float)scaleInput(hook);
        intake = (float)scaleInput(intake);

        // write the values to the motors

        tbc.setMotorRRightPower(-right);
        tbc.setMotorRLeftPower(-left);
        tbc.setMotorFRightPower(-right);
        tbc.setMotorFLeftPower(-left );

        tbc.setMotorHookPower(hook);
        tbc.setMotorPusherPower(intake);

        if(gamepad2.x) {
            tbc.buttonServoSpeed = 0.0f;
        }
        if(gamepad2.y) {
            tbc.buttonServoSpeed = 1.0f;
        }
        if(gamepad2.x != true && gamepad2.y != true) {
            tbc.buttonServoSpeed = 0.5f;
        }
        tbc.setButtonServoSpeed(tbc.buttonServoSpeed);

        /*if(gamepad2.a) {
            tbc.climberPosition = tbc.CLIMBER_MAX_RANGE;
        }
        if(gamepad2.b) {
            tbc.climberPosition = tbc.CLIMBER_MIN_RANGE;
        }
        tbc.climberPosition = Range.clip(tbc.climberPosition, tbc.CLIMBER_MIN_RANGE, tbc.CLIMBER_MAX_RANGE);
        tbc.setClimberPosition(tbc.climberPosition);
        */

        Double climberNewPos = tbc.climberPosition;
        if(gamepad2.a) {
            climberNewPos = tbc.climberPosition + climberDelta;
        }
        if(gamepad2.b) {
            climberNewPos = tbc.climberPosition - climberDelta;
        }
        tbc.climberPosition = Range.clip(climberNewPos, tbc.CLIMBER_MIN_RANGE, tbc.CLIMBER_MAX_RANGE);
        tbc.setClimberPosition(tbc.climberPosition);

        Double snowplowNewPos = tbc.snowplowPosition;
        if(gamepad1.right_bumper) {
            snowplowNewPos = tbc.snowplowPosition + snowplowDelta;
        }
        if(gamepad1.left_bumper) {
            snowplowNewPos = tbc.snowplowPosition - snowplowDelta;
        }
        if(gamepad1.dpad_down) {
            snowplowNewPos = tbc.SNOWPLOW_MIN_RANGE;
        }
        tbc.snowplowPosition = Range.clip(snowplowNewPos, tbc.SNOWPLOW_MIN_RANGE, tbc.SNOWPLOW_MAX_RANGE);
        tbc.setSnowplowPosition(tbc.snowplowPosition);

        if(gamepad1.x) {
            tbc.sliderLPosition = tbc.SLIDERL_MAX_RANGE;
        }
        if(gamepad1.y) {
            tbc.sliderLPosition = tbc.SLIDERL_MIN_RANGE;
        }
        tbc.sliderLPosition = Range.clip(tbc.sliderLPosition, tbc.SLIDERL_MIN_RANGE, tbc.SLIDERL_MAX_RANGE);
        tbc.setSliderLPosition(tbc.sliderLPosition);

        if(gamepad1.a) {
            tbc.sliderRPosition = tbc.SLIDERR_MAX_RANGE;
        }
        if(gamepad1.b) {
            tbc.sliderRPosition = tbc.SLIDERR_MIN_RANGE;
        }
        tbc.sliderRPosition = Range.clip(tbc.sliderRPosition, tbc.SLIDERR_MIN_RANGE, tbc.SLIDERR_MAX_RANGE);
        tbc.setSliderRPosition(tbc.sliderRPosition);

        Double mtapeNewPos = tbc.mtapePosition;
        if(gamepad2.dpad_up) {
            mtapeNewPos = tbc.mtapePosition + mtapeDelta;
        }
        if(gamepad2.dpad_down) {
            mtapeNewPos = tbc.mtapePosition - mtapeDelta;
        }
        if(gamepad2.dpad_left) {
            tbc.mtapePosition = tbc.MTAPE_MIN_RANGE;
        }
        if(gamepad2.dpad_right) {
            tbc.mtapePosition = tbc.MTAPE_MAX_RANGE;
        }

        tbc.mtapePosition = Range.clip(mtapeNewPos, tbc.MTAPE_MIN_RANGE, tbc.MTAPE_MAX_RANGE);
        tbc.setMtapePosition(tbc.mtapePosition);

        if ((!gamepad2.right_bumper) && (!gamepad2.left_bumper)) {
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
        telemetry.addData("light", "light: " + String.format("%d", reflection));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("servo in",  "servo in: " + String.format("%.2f", servoInput));
        telemetry.addData("intake",  "intake: " + String.format("%.2f", intake));
        telemetry.addData("button servo", "button servo: " + String.format("%.2f", tbc.buttonServoSpeed));
        telemetry.addData("climber", "climber:  " + String.format("%.2f", tbc.climberPosition));
        telemetry.addData("sliderL", "sliderL: " + String.format("%.2f", tbc.sliderLPosition));
        telemetry.addData("sliderR", "sliderR:" + String.format("%.2f", tbc.sliderRPosition));
        telemetry.addData("mtape", "mtape: " + String.format("%.2f", tbc.mtapePosition));
        telemetry.addData("lf encoder", "lf encoder:" + String.format("%d", tbc.motorFLeft.getCurrentPosition()));
        telemetry.addData("rf encoder", "rf encoder:" + String.format("%d", tbc.motorFRight.getCurrentPosition()));
        telemetry.addData("lr encoder", "lr encoder:" + String.format("%d", tbc.motorRLeft.getCurrentPosition()));
        telemetry.addData("rr encoder", "rr encoder:" + String.format("%d", tbc.motorRRight.getCurrentPosition()));
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
