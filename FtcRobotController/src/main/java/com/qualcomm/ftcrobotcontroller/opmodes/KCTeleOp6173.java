package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class KCTeleOp6173 extends OpMode {


    DcMotor driveRight;
    DcMotor driveLeft;
    DcMotor driveLeft2;
    DcMotor driveRight2;
    int i = 0;
    int x = 0;

    final static double LEFT_ARM_MIN = 0;
    final static double LEFT_ARM_MAX = .8;
    final static double RIGHT_ARM_MIN = 0;
    final static double RIGHT_ARM_MAX = .8;
    final static double ARM_DELAY = 0.250; //ms to wait for next arm press

    // position of the arm servo.
    double leftArmPosition;
    double rightArmPosition;

    double leftArmWaitUntil = 0;
    double rightArmWaitUntil = 0;

    // amount to change the arm servo position.
    double armDelta = 0.1;

    /*
    float currentLeftPower;
    float currentRightPower;
    float powerStep;
    long startTime;
    long currentTime;
    boolean lastGamePad1Y;
    Steerer currentSteerer;
    */

    Servo servoRight;
    Servo servoLeft;


    /**
     * Constructor
     */
    public KCTeleOp6173() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {


		/*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        driveLeft = hardwareMap.dcMotor.get("left_front");
        driveLeft2 = hardwareMap.dcMotor.get("left_back");
        driveLeft.setDirection(DcMotor.Direction.REVERSE);
        driveLeft2.setDirection(DcMotor.Direction.REVERSE);

        driveRight = hardwareMap.dcMotor.get("right_front");
        driveRight2 = hardwareMap.dcMotor.get("right_front");
        driveRight.setDirection(DcMotor.Direction.FORWARD);
        driveRight2.setDirection(DcMotor.Direction.FORWARD);

        servoLeft = hardwareMap.servo.get("servo_left");
        servoLeft.setDirection(Servo.Direction.REVERSE);
        servoRight = hardwareMap.servo.get("servo_right");
        servoLeft.setDirection(Servo.Direction.FORWARD);

        resetArmPositions();
    }

    private void resetArmPositions() {
        leftArmPosition = LEFT_ARM_MIN; //(LEFT_ARM_MAX-LEFT_ARM_MIN)/2;
        rightArmPosition = RIGHT_ARM_MAX; //(RIGHT_ARM_MAX-RIGHT_ARM_MIN)/2;
        servoLeft.setPosition(leftArmPosition);
        servoRight.setPosition(rightArmPosition);
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {


        double leftThrottle = gamepad1.left_stick_y;
        double rightThrottle = gamepad1.right_stick_y;


        // clip the right/left values so that the values never exceed +/- 1
        leftThrottle = Range.clip(leftThrottle, -1, 1);
        rightThrottle = Range.clip(rightThrottle, -1, 1);

        //scale the throttle so it's easier to control at low speeds
        //this is UNTESTED on the actual robot as of 12/4/2015, so just comment out if it's acting up
        leftThrottle = scaleInput(leftThrottle);
        rightThrottle = scaleInput(rightThrottle);

        driveRight.setPower(rightThrottle);
        driveRight2.setPower(rightThrottle);
        driveLeft.setPower(leftThrottle);
        driveLeft2.setPower(leftThrottle);

        boolean moveLeftArm = false;
        boolean moveRightArm = false;

        if (getRuntime() >= leftArmWaitUntil) { //pause before changing position again
            if (gamepad1.left_bumper) {
                leftArmPosition -= armDelta;
                moveLeftArm = true;
            }

            if (gamepad1.left_trigger > 0.25) {
                leftArmPosition += armDelta;
                moveLeftArm = true;
            }

            if (moveLeftArm) {
                leftArmPosition = Range.clip(leftArmPosition, LEFT_ARM_MIN, LEFT_ARM_MAX);
                servoLeft.setPosition(leftArmPosition);
                leftArmWaitUntil = getRuntime() + ARM_DELAY;
            }
        }

        if (getRuntime() >= rightArmWaitUntil) {
            if (gamepad1.right_bumper) {
                rightArmPosition += armDelta;
                moveRightArm = true;
            }

            if (gamepad1.right_trigger > 0.25) {
                rightArmPosition -= armDelta;
                moveRightArm = true;
            }

            if (moveRightArm) {
                rightArmPosition = Range.clip(rightArmPosition, RIGHT_ARM_MIN, RIGHT_ARM_MAX);
                servoRight.setPosition(rightArmPosition);
                rightArmWaitUntil = getRuntime() + ARM_DELAY;
            }
        }

/*
         * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        //telemetry.addData("Text", "*** Robot Data***");
        //telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        //telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left pwr", "left  pwr: " + String.format("%.2f", leftThrottle));
        telemetry.addData("right pwr", "right pwr: " + String.format("%.2f", rightThrottle));
        telemetry.addData("left arm", leftArmPosition);
        telemetry.addData("right arm", rightArmPosition);
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
        resetArmPositions();
    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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

