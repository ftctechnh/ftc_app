package com.qualcomm.ftcrobotcontroller.opmodes;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.util.Range;

/**
 * Created by Robotics on 10/28/2015.
 */

public class TestTeleOP extends OpMode {
    DcMotor frontLeftMotor; //motor declarations, actual motor names will be later on
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;

    private static final String frontLeft =  "front_left";  //motor name defines
    private static final String frontRight = "front_right";
    private static final String backLeft = "back_left";
    private static final String backRight = "back_right";
    private static final String armName = "arm";
    private static final String clawName = "claw";

    //private static final double armRest = 1.0;
    //private static final double clawRest = 0.7;
    //private static final double armStage1 = 0.8;
    //private static final double armStage2 = 0.3;
    //private static final double clawStage1 = 0.2;
    private static final double frontMotorMultiple = 0.75;
    private static final double backMotorMultiple = 1.0;

    Servo armBase;
    Servo armPrimary1;
    Servo armPrimary2;
    Servo armSecondary1;
    //Servo armSecondary2;
    //Servo armHatch;

    private static final String baseName = "base";
    private static final String primary1Name = "primary_1";
    private static final String primary2Name = "primary_2";
    private static final String secondary1Name = "secondary_1";
    //private static final String secondary2Name = "secondary_2";
    //private static final String hatchName = "hatch";

    //constructor
    public TestTeleOP() {
        //nope
    }

    //copy pasted from k9TeleOP
    @Override
    public void init() {

        frontLeftMotor = hardwareMap.dcMotor.get(frontLeft);
        frontRightMotor = hardwareMap.dcMotor.get(frontRight);
        backRightMotor = hardwareMap.dcMotor.get(backLeft);
        backLeftMotor = hardwareMap.dcMotor.get(backRight);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        //backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        //frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection((DcMotor.Direction.REVERSE));
        //arm = hardwareMap.servo.get("servo_1");
        //claw = hardwareMap.servo.get("servo_6");

        // robotic arm servos
        armBase = hardwareMap.servo.get(baseName);
        armPrimary1 = hardwareMap.servo.get(primary1Name);
        armPrimary2 = hardwareMap.servo.get(primary2Name);
        armSecondary1 = hardwareMap.servo.get(secondary1Name);
        //armSecondary2 = hardwareMap.servo.get(secondary2Name);
        //armHatch = hardwareMap.servo.get(hatchName);
    }

    @Override
    public void loop() {

		/*
		Gamepad 1 controls the motors via the left stick, and it controls the
		wrist/claw via the a,b, x, y buttons

        throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        1 is full down
        direction: left_stick_x ranges from -1 to 1, where -1 is full left
        and 1 is full right
        */

        /*
        TANK STEERING MOTHERFUCKAH
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;
        */

        float leftThrottle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;

        // write the values to the motors
        backLeftMotor.setPower(leftThrottle * backMotorMultiple);
        frontLeftMotor.setPower(leftThrottle * frontMotorMultiple);
        backRightMotor.setPower(rightThrottle * backMotorMultiple);
        frontRightMotor.setPower(rightThrottle * frontMotorMultiple);
        // update the position of the arm
		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** F***YEAH!!!***");
        //telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        //telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftThrottle));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightThrottle));
        telemetry.addData("Left Trigger", gamepad2.left_bumper);
        telemetry.addData("Right Trigger", gamepad2.right_bumper);

        updateArmServos(gamepad2.left_stick_x, gamepad2.right_stick_x, gamepad2.left_trigger, gamepad2.right_trigger, gamepad2.a);
    }

    @Override
    public void stop() {
        telemetry.addData("Text", "****ROBOT IS STOPPED****");
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

    public void updateArmServos(double joystickLeftX, double joystickRightX, double leftTrigger, double rightTrigger, boolean buttonA) {

        // base control (triggers)
        if(leftTrigger > 0.0) armBase.setPosition(1.0);
        else if(rightTrigger > 0.0) armBase.setPosition(0.0);
        else armBase.setPosition(0.5);

        // primary joint control (left joystick)
        armPrimary1.setPosition(Range.clip(armPrimary1.getPosition() + joystickLeftX / 2.0, 0.0, 1.0));
        armPrimary2.setPosition(Range.clip(armPrimary2.getPosition() - joystickLeftX / 2.0, 0.0, 1.0));

        // secondary joint control (right joystick)
        armSecondary1.setPosition(Range.clip(armSecondary1.getPosition() + joystickRightX, 0.0, 1.0));
        //armSecondary2.setPosition(Range.clip(armSecondary2.getPosition() - joystickRightX, 0.0, 1.0));

        // hatch control (A button)
        //if(buttonA) armHatch.setPosition(1.0);
        //else armHatch.setPosition(0.0);
    }
}
