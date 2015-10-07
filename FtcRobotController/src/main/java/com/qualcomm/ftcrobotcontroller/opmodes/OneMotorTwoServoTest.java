package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Op Mode for testing one motor and two servos.
 *
 * Created by Joshua Evans on 10/7/2015.
 */
public class OneMotorTwoServoTest extends OpMode {

    /**
     * These "Members" are the servos and motors that we are using
     */
    DcMotor motor_1;
    Servo servo_1;
    Servo servo_2;

    /**
     * These members keep track of the current status of servos
     */
    double servo_1Position = 0.5;
    double servo_2Position = 0.5;

    /**
     * This member keeps track of how much the servos should be moved by each loop, also
     * known as a "delta"
     */
    double servoDelta = 0.1;

    /**
     * This member represents how far the triggers have to be pressed before recognizing input
     */
    double triggerBuffer = 0.1;

    /**
     * This is the "constructor" It is called immediately upon creation creation.
     * Because of how the robotics system works, we should be very careful about what we put in here.
     *
     * Usually, for our purposes, the constructor will be empty
     */
    public OneMotorTwoServoTest() {}

    /**
     * This is called when the robot initializes action, before the control loop begins.
     *
     * Initialization should go in here.
     */
    @Override
    public void init() {
        // Get the motor and the servos so that we can actually use them
        motor_1 = hardwareMap.dcMotor.get("motor_1");
        servo_1 = hardwareMap.servo.get("servo_1");
        servo_2 = hardwareMap.servo.get("servo_2");
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
        } else if (gamepad1.left_bumper) {
            // if the left bumper is pressed, move the servo another direction
            servo_1Position -= servoDelta;
        }

        // Handle controls for servo_2
        if (gamepad1.right_trigger >= triggerBuffer){
            // If the right trigger is pressed down far enough, move the servo
            // Don't adjust the servo right away. instead, change the position member
            servo_2Position += servoDelta;
        } else if (gamepad1.left_trigger >= triggerBuffer) {
            // if the left bumper is pressed down far enough, move the servo the other direction
            servo_2Position -= servoDelta;
        }

        // Handle controls for motor_1
        // Instead of getting input from a button and powering the motor up or down based on
        // A delta, we get input directly from the left thumb stick
        // Note that the input is reversed (negated). This is because, for some reason, pushing
        // UP on the joy stick gives a negative value, and DOWN gives a positive value
        double throttle = -gamepad1.left_stick_y;

        // Use the power of telemetry to give feedback!
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("servo_1Position", servo_1Position);
        telemetry.addData("serco_2Position", servo_2Position);
        telemetry.addData("motorThrottle", throttle);

        // Set all of the positions and power at the end of the loop
        motor_1.setPower(throttle);
        servo_1.setPosition(servo_1Position);
        servo_2.setPosition(servo_2Position);
    }

    /**
     * This function will be called when the robot stops
     */
    @Override
    public void stop() {

    }
}
