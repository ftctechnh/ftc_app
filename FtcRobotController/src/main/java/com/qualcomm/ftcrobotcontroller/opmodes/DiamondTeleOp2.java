package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// RobotMk2
//

import com.qualcomm.robotcore.util.Range;

/**
 * Provide a basic manual operational mode that uses the left and right
 * drive motors, left arm motor, servo motors and gamepad input from two
 * gamepads for the Diamond Bot.
 *
 * @author Max Davy/Diamond Blades
 * @version 2016-01-18-18-15
 */
public class DiamondTeleOp2 extends DiamondBotTelemetry

{
    //private Servo v_servo_bucket;
    //private Servo v_servo_bucket_release;

    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;
    final static double BUCKET_RELEASE_MIN_RANGE  = 0.3;
    final static double BUCKET_RELEASE_MAX_RANGE  = 1.0;
    final static double BUCKET_MIN_RANGE  = 0.15;
    final static double BUCKET_MAX_RANGE  = 0.7;

    double v_servo_bucket_position = .5;
    double v_servo_bucket_release_position = .3;

    //--------------------------------------------------------------------------
    //
    // RobotMk2
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public DiamondTeleOp2()
    {

        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // RobotMk2

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during
     * manual-operation.  The state machine uses gamepad input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()
    {
        //----------------------------------------------------------------------
        //
        // DC Motors
        //
        // Obtain the current values of the joystick controllers.
        //
        // Note that x and y equal -1 when the joystick is pushed all of the way
        // forward (i.e. away from the human holder's body).
        //
        // The clip method guarantees the value never exceeds the range +-1.
        //
        // The DC motors are scaled to make it easier to control them at slower
        // speeds.
        //
        // The setPower methods write the motor power values to the DcMotor
        // class, but the power levels aren't applied until this method ends.
        //

        //
        // Manage the drive wheel motors.
        //

        float l_left_drive_power = scale_motor_power (gamepad1.left_stick_y); //set the drive motor power based on the joystick position
        float l_right_drive_power = scale_motor_power (gamepad1.right_stick_y); //set the drive motor power based on the joystick position

        if (gamepad1.left_stick_button) //but if the joystick button is pressed
        {
            l_left_drive_power = scale_motor_power (gamepad1.left_stick_y)/8; //set it to half as much
        }
        if (gamepad1.right_stick_button) //but if the joystick button is pressed
        {
            l_right_drive_power = scale_motor_power (gamepad1.right_stick_y)/8; //set it to half as much
        }
        set_drive_power (l_left_drive_power, l_right_drive_power); //apply the drive motor power

        if (gamepad1.dpad_up) //if the up pad button is pressed
        {
            set_drive_power(-0.2, -0.2); //set both motors to 20% forward
        }
        else if (gamepad1.atRest()) //otherwise if no buttons are pressed
        {
            set_drive_power (0.0, 0.0); //stop both motors
        }

        if (gamepad1.dpad_down) //if the down pad button is pressed
        {
            set_drive_power(0.2, 0.2); //set both motors to 20% backward
        }
        else if (gamepad1.atRest()) //otherwise if no buttons are pressed
        {
            set_drive_power(0.0, 0.0); //stop both motors
        }

        //
        // Manage the arm motors.
        //
        float l_left_arm_power = scale_motor_power (-gamepad2.left_stick_y); //set the arm power based on the joystick position
        float l_right_arm_power = scale_motor_power (gamepad2.left_stick_y); //set the arm power based on the joystick position

        if (gamepad2.left_stick_button) //but if the joystick button is pressed
        {
            l_left_arm_power = scale_motor_power (-gamepad2.left_stick_y)/2; //set the arm power to half as much
            l_right_arm_power = scale_motor_power (gamepad2.left_stick_y)/2; //set the arm power to half as much
        }

        m_left_arm_power (l_left_arm_power); //apply the arm power
        m_right_arm_power (l_right_arm_power); //apply the arm power

        if (gamepad2.dpad_up) //if the up button on the pad is pressed
        {
            m_left_arm_power(.30); //set the arm motor power to 30% forward
            m_right_arm_power(-.30); //set the arm motor power to 30% forward
        }
        else if (gamepad2.atRest()) //if the no buttons are pressed
        {
            m_left_arm_power(0.0); //stop the arm motor
            m_right_arm_power(0.0); //stop the arm motor
        }

        if (gamepad2.dpad_down) //if the down button on the pad is pressed
        {
            m_left_arm_power(-.30); //set the arm motor power to 30% backward
            m_right_arm_power(.30); //set the arm motor power to 30% backward
        }
        else if (gamepad2.atRest()) //if the no buttons are pressed
        {
            m_left_arm_power(0.0); //stop the arm motor
            m_right_arm_power(0.0); //stop the arm motor
        }

        //----------------------------------------------------------------------
        //
        // Servo Motors
        //
        // Obtain the current values of the gamepad bumper and trigger buttons.
        //
        // Note that these buttons have boolean values of true and false.
        //
        // The clip method guarantees the value never exceeds the allowable range of
        // [0,1].
        //
        // The setPosition methods write the motor power values to the Servo
        // class, but the positions aren't applied until this method ends.
        //

        if (gamepad1.left_bumper)
        {
            try
            {
                v_servo_bucket_release_position = a_bucket_release_position();
                v_servo_bucket_release_position += .05;
                // clip the position values so that they never exceed their allowed range.
                v_servo_bucket_release_position = Range.clip(v_servo_bucket_release_position, BUCKET_RELEASE_MIN_RANGE, BUCKET_RELEASE_MAX_RANGE);

                m_bucket_release_position(v_servo_bucket_release_position);
            }
            catch (Exception p_exception)
            {

            }
        }
        else if (gamepad1.left_trigger != 0)
        {
            try
            {
                v_servo_bucket_release_position = a_bucket_release_position();
                v_servo_bucket_release_position -= .05;
                // clip the position values so that they never exceed their allowed range.
                v_servo_bucket_release_position = Range.clip(v_servo_bucket_release_position, BUCKET_RELEASE_MIN_RANGE, BUCKET_RELEASE_MAX_RANGE);

                m_bucket_release_position(v_servo_bucket_release_position);
            }
            catch (Exception p_exception)
            {

            }
        }

        if (gamepad1.right_bumper)
        {
            try
            {
                v_servo_bucket_position = a_bucket_position();
                v_servo_bucket_position += .05;
                // clip the position values so that they never exceed their allowed range.
                v_servo_bucket_position = Range.clip(v_servo_bucket_position, BUCKET_MIN_RANGE, BUCKET_MAX_RANGE);

                m_bucket_position(v_servo_bucket_position);
            }
            catch (Exception p_exception)
            {

            }
        }
        else if (gamepad1.right_trigger != 0)
        {
            try
            {
                v_servo_bucket_position = a_bucket_position();
                v_servo_bucket_position -= .05;
                // clip the position values so that they never exceed their allowed range.
                v_servo_bucket_position = Range.clip(v_servo_bucket_position, BUCKET_MIN_RANGE, BUCKET_MAX_RANGE);

                m_bucket_position(v_servo_bucket_position);
            }
            catch (Exception p_exception)
            {

            }
        }

        // Encoder Reset
        //
        // Obtain the current values of the gamepad 'a' buttons.
        //
        // Note that the 'a' button has a boolean value of true or false.
        //
        if (gamepad2.a) { //if the a button is pressed
            reset_all_encoders(); //reset the encoders
        }
        if (gamepad1.a) { //if the other a button is pressed
            reset_all_encoders(); //reset the encoders
        }
        //
        // Send telemetry data to the driver station.
        //
        update_telemetry(); // Update common telemetry
        update_gamepad_telemetry();


    } // loop

} // PushBotManual
