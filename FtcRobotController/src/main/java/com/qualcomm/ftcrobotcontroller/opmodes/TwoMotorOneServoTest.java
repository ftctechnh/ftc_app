package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Op Mode for testing one motor and two servos.
 *
 * Created by Joshua Evans on 10/7/2015.
 */
public class TwoMotorOneServoTest extends OpMode {

    /**
     * These "Members" are the servos and motors that we are using
     */
    DcMotor motor_1;
    DcMotor motor_2;
    Servo servo_1;


    /**
     * These members keep track of the current status of servo
     */
    double servo_1Position = 0.5;

    /**
     * This member keeps track of how much the servos should be moved by each loop, also
     * known as a "delta"
     */
    double servoDelta = 0.1;

    /**
     * This is the "constructor" It is called immediately upon creation creation.
     * Because of how the robotics system works, we should be very careful about what we put in here.
     *
     * Usually, for our purposes, the constructor will be empty
     */
    public TwoMotorOneServoTest() {}

    /**
     * This is called when the robot initializes action, before the control loop begins.
     *
     * Initialization should go in here.
     */
    @Override
    public void init() {
        // Get the motors and the servo so that we can actually use them
        motor_1 = hardwareMap.dcMotor.get("motor_1");
        motor_2 = hardwareMap.dcMotor.get("motor_2");
        servo_1 = hardwareMap.servo.get("servo_1");
    }

    /**
     * This is the robot control loop, it will be called constantly.
     *
     * This is where control input or AI should go
     */
    @Override
    public void loop() {
        /**
         * CONTROLS
         * Right and Left Bumper control servo_1
         * Right and Left Trigger control servo_2
         * Left Thumb Stick controls motor_1
         */

        // Handle controls for servo_1
        if (gamepad1.right_bumper){
            // If the right bumper is pressed, move the servo one direction
            // Don't adjust the servo right away. instead, change the position member
            servo_1Position += servoDelta;
            if (servo_1Position > 1)
                servo_1Position = 1;
        } else if (gamepad1.left_bumper) {
            // if the left bumper is pressed, move the servo another direction
            servo_1Position -= servoDelta;
            if (servo_1Position < 0)
                servo_1Position = 0;
        }

        // Handle controls for motor_1 and motor_2
        // Instead of getting input from a button and powering the motor up or down based on
        // A delta, we get input directly from the left and right thumb stick
        // Note that the input is reversed (negated). This is because, for some reason, pushing
        // UP on the joy stick gives a negative value, and DOWN gives a positive value
        double throttle_1 = -gamepad1.left_stick_y;
        double throttle_2 = -gamepad1.right_stick_y;

        // Use the power of telemetry to give feedback!
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("servo_1Position", servo_1Position);
        telemetry.addData("motor_1Throttle", throttle_1);
        telemetry.addData("motor_2Throttle", throttle_2);

        // Set all of the positions and power at the end of the loop
        motor_1.setPower(throttle_1);
        motor_2.setPower(throttle_2);
        servo_1.setPosition(servo_1Position);
    }

    /**
     * This function will be called when the robot stops
     */
    @Override
    public void stop() {

    }
}
