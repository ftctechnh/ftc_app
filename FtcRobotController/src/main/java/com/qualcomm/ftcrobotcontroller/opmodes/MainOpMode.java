package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by JackV on 9/12/15.
 */
public class MainOpMode extends OpMode{

    final static double ARM_MIN_RANGE  = 0;
    final static double ARM_MAX_RANGE  = 1;

    double armDelta = 0.05;

    // MOTOR VALUES
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor sucker;
    DcMotor linear;

    Servo servo_right, servo_left, servo_lip;

    double rightArmPosition, leftArmPosition, lipServoPosition;


    // CONSTRUCTOR and Electronics Diagram
    public MainOpMode() {
    /*
    Note- There is no constructor

    Explaination of the electronics -
    There are 3 motor controller hubs and 1 servo controller hubs
    Each of these are connected to a central power/controller hub.
    This hub is connected to the phone.

    Each of the 4 motor/servo controllers are connected both by power and USB to the central hub.
    Then, each of the motors are placed into a motor controller near them, and the servos are placed into the singular servo controller.

    Offical Electronics Layout Diagram/Documentation:

    C.P.D.M. or CPDM (Core Power Distribution Module)
    |
    |                                   |--- dcMotor (sucker)
    |- Motor Controller 1 (AL00VV17) ---|
    |                                   |--- dcMotor (motor_RF)
    |
    |                                   |--- dcMotor (linear)
    |- Motor Controller 2 (AL00UXSC) ---|
    |                                   |--- dcMotor (motor_LF)
    |
    |                                   |--- Servo (servo_1)
    |- Servo Controller 1 (AL00YBDM) ---|--- Servo (servo_2)
    |                                   |--- Servo (servo_3)
    |                                   |--- Servo (servo_4)
    |                                   |--- Servo (servo_5)
    |                                   |--- Servo (servo_6)
    |
    |                                   |--- dcMotor (motor_RB)
    |- Motor Controller 3 (AL00XUI0) ---|
                                        |--- dcMotor (motor_LB)





    */
    }

    //Variable Init
    // Called when robot is first enabled
    @Override
    public void init() {
        motorLeftFront = hardwareMap.dcMotor.get("motor_LF");
        motorLeftBack = hardwareMap.dcMotor.get("motor_LB");

        motorRightFront = hardwareMap.dcMotor.get("motor_RF");
        motorRightBack = hardwareMap.dcMotor.get("motor_RB");

        sucker = hardwareMap.dcMotor.get("sucker");
        linear = hardwareMap.dcMotor.get("linear");

        servo_right = hardwareMap.servo.get("servo_1");
        servo_left = hardwareMap.servo.get("servo_2");
        servo_lip = hardwareMap.servo.get("servo_3");

        rightArmPosition = 1;
        leftArmPosition = 0;
        lipServoPosition = 0;
    }

    //Loop
    // Called repeatedly every 10 ms
    @Override
    public void loop() {
        motorLeftFront.setPower(-squareInputs(gamepad1.left_stick_y));
        motorLeftBack.setPower(-squareInputs(gamepad1.left_stick_y));

        motorRightFront.setPower(squareInputs(gamepad1.right_stick_y));
        motorRightBack.setPower(squareInputs(gamepad1.right_stick_y));

        if (gamepad2.b) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            rightArmPosition = .25;
            leftArmPosition = .75;
        }

        if (gamepad2.left_bumper) {
            lipServoPosition = 0;
        }

        if (gamepad2.right_bumper) {
            lipServoPosition = .5;
        }


        if (gamepad2.y) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            rightArmPosition = 1;
            leftArmPosition = 0;
        }

        if (gamepad1.a) {
            sucker.setPower(-1);
        }
        if (gamepad1.x)
            sucker.setPower(0);

        if (gamepad2.dpad_down) {
            linear.setPower(1);
        } else if (gamepad2.dpad_up){
            linear.setPower(-1);
        } else {
            linear.setPower(0);
        }

        rightArmPosition = Range.clip(rightArmPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        leftArmPosition = Range.clip(leftArmPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        lipServoPosition = Range.clip(lipServoPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);

        servo_right.setPosition(rightArmPosition);
        servo_left.setPosition(leftArmPosition);
        servo_lip.setPosition(lipServoPosition);

        telemetry.addData("Left Stick", gamepad1.left_stick_y);
        telemetry.addData("Right Stick", gamepad1.right_stick_y);
    }

    // Called when robot is disabled
    @Override
    public void stop() {
        motorLeftFront.setPower(0);
        motorLeftBack.setPower(0);

        motorRightFront.setPower(0);
        motorRightBack.setPower(0);

    }

    // ADDITIONAL METHODS

    float squareInputs(float input) {
        if (input < 0)
            return -(input * input);
        else
            return input * input;
    }
}