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
 * @version 2016-01-18-18-16
 * blame Nate for the 'Larry'
 */
public class DiamondTeleOp_Larry extends DiamondBotHardware_Larry

{
    //private Servo v_servo_bucket;
    //private Servo v_servo_bucket_release;

    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;
    //final static double BUCKET_RELEASE_MIN_RANGE  = 0.3;
    //final static double BUCKET_RELEASE_MAX_RANGE  = 1.0;
    final static double BUMPER_MIN_RANGE  = 0.15;
    final static double BUMPER_MAX_RANGE  = 0.7;
    final static double BACK_BUMPER_MIN_RANGE  = 0.10;
    final static double BACK_BUMPER_MAX_RANGE  = 0.7;

    double v_servo_bumper_position =128;
    double v_servo_back_bumper_position = 128;
    String v_bumper_direction = "down";

    //--------------------------------------------------------------------------
    //
    // RobotMk2
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public DiamondTeleOp_Larry()
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

    @Override public void start ()
    {
        m_right_flipper_position(.25);
        m_left_flipper_position(1);
        m_bumper_position(.5);
        m__back_bumper_position(.5);
        m_triggers_position(0);
        m_other_triggers_position(.89);
    }
    //set the first line of the telemetry data
    public void set_first_message (String p_message)

    {
        telemetry.addData ( "00", p_message);

    } // set_first_message
    //write motor data to driver station
    public void update_telemetry ()

    {
        if (a_warning_generated ())
        {
            set_first_message (a_warning_message ());
        }
        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
                ( "01"
                        , "Left Drive Motor: Power="
                                + a_left_drive_power()
                                + ", Encoder Value="
                                + a_left_encoder_count()
                );
        telemetry.addData
                ( "02"
                        , "Right Drive Motor: Power="
                                + a_right_drive_power()
                                + ", Encoder Value="
                                + a_right_encoder_count()
                );
        telemetry.addData
                ( "03"
                        , "Arm Motor: Power="
                                + a_left_arm_power()
                                + ", Encoder Value="
                                + a_left_arm_encoder_count()
                );
        telemetry.addData
                ( "04"
                        , "Winder Motor: Power="
                                + a_right_arm_power()
                                + ", Encoder Value="
                                + a_right_arm_encoder_count()
                );
        telemetry.addData
                ( "05"
                        , "Front Bumper="
                                +a_bumper_position()
                                +", Back Bumper="
                                +a_back_bumper_position()

                );
        telemetry.addData
                ( "06"
                        , "Trigger="
                                +a_triggers_position()
                                +", Other Trigger="
                                +a_other_triggers_position()

                );

    } // update_telemetry

    //--------------------------------------------------------------------------
    //
    // update_gamepad_telemetry
    //
    /**
     * Update the telemetry with current gamepad readings.
     */
    public void update_gamepad_telemetry ()

    {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("07", "Gamepad 1 Left Joystick: " + -gamepad1.left_stick_y);
        telemetry.addData ("08", "Gamepad 1 Right Joystick: " + -gamepad1.right_stick_y);
        telemetry.addData ("19", "Gamepad 1 Left Trigger: " + gamepad1.left_trigger);
        telemetry.addData ("10", "Gamepad 1 Right Trigger: " + gamepad1.right_trigger);
        telemetry.addData ("11", "Gamepad 1 A Button: " + gamepad1.a);
        telemetry.addData ("12", "Gamepad 1 B Button: " + gamepad1.b);
        telemetry.addData ("13", "Gamepad 1 X Button: " + gamepad1.x);
        telemetry.addData ("14", "Gamepad 1 Y Button: " + gamepad1.y);
        telemetry.addData ("15", "Gamepad 1 Up Button: " + gamepad1.dpad_up);
        telemetry.addData ("16", "Gamepad 1 Down Button: " + gamepad1.dpad_down);
        telemetry.addData ("17", "Gamepad 1 Left Button: " + gamepad1.dpad_left);
        telemetry.addData ("18", "Gamepad 1 Right Button: " + gamepad1.dpad_right);
        //
        telemetry.addData ("19", "Gamepad 2 Left Joystick: " + -gamepad2.left_stick_y);
        telemetry.addData ("20", "Gamepad 2 Right Joystick: " + -gamepad2.right_stick_y);
        telemetry.addData ("21", "Gamepad 2 Left Trigger: " + gamepad2.left_trigger);
        telemetry.addData ("22", "Gamepad 2 Right Trigger: " + gamepad2.right_trigger);
        telemetry.addData ("23", "Gamepad 2 A Button: " + gamepad2.a);
        telemetry.addData ("24", "Gamepad 2 B Button: " + gamepad2.b);
        telemetry.addData ("25", "Gamepad 2 X Button: " + gamepad2.x);
        telemetry.addData ("26", "Gamepad 2 Y Button: " + gamepad2.y);
        telemetry.addData ("27", "Gamepad 2 Up Button: " + gamepad2.dpad_up);
        telemetry.addData ("28", "Gamepad 2 Down Button: " + gamepad2.dpad_down);
        telemetry.addData ("29", "Gamepad 2 Left Button: " + gamepad2.dpad_left);
        telemetry.addData ("30", "Gamepad 2 Right Button: " + gamepad2.dpad_right);


    } // update_gamepad_telemetry
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

        float l_left_drive_power = scale_motor_power (-gamepad1.left_stick_y); //set the drive motor power based on the joystick position
        float l_right_drive_power = scale_motor_power (-gamepad1.right_stick_y); //set the drive motor power based on the joystick position
        if (gamepad2.right_stick_y > 0)
        {
            m_right_flipper_position(.357);
            m_left_flipper_position(.9);
            set_first_message("Right Flipper Position: " + a_right_flipper_position()+", Left Flipper Position: "+a_left_flipper_position());
        }
        else if (gamepad2.right_stick_y < 0)
        {
            m_right_flipper_position(.25);
            m_left_flipper_position(1);
            set_first_message("Right Flipper Position: " + a_right_flipper_position()+", Left Flipper Position: "+a_left_flipper_position());
        }
        else
        {
            set_first_message("Right Flipper Position: " + a_right_flipper_position()+", Left Flipper Position: "+a_left_flipper_position());
        }

        if (gamepad1.left_stick_button) //but if the joystick button is pressed
        {
            l_left_drive_power = scale_motor_power (-gamepad1.left_stick_y)/8; //set it to half as much
        }
        if (gamepad1.right_stick_button) //but if the joystick button is pressed
        {
            l_right_drive_power = scale_motor_power (gamepad1.right_stick_y)/8; //set it to half as much
        }
        set_drive_power (l_left_drive_power, l_right_drive_power); //apply the drive motor power

        //
        // Manage the arm motors.
        //
        float l_arm_power = scale_motor_power (gamepad2.left_stick_y)/3; //set the arm power based on the joystick position
        float l_winder_power; //initialize variable
        float l_tape_power;

        if (gamepad1.left_bumper || gamepad1.right_bumper)//if either of the bumpers on gamepad 1 are pressed
        {
            l_winder_power = 1; //start winding
        }
        else //otherwise
        {
            l_winder_power = 0; //stop winding
        }
        if (gamepad2.left_stick_button) //but if the joystick button is pressed
        {
            l_arm_power = scale_motor_power (gamepad2.left_stick_y)/2; //set the arm power to half as much
        }
        if (gamepad2.a)
        {
            l_tape_power = 1; //set the arm power based on the joystick position
        }
        else if (gamepad2.y)
        {
            l_tape_power = -1; //set the arm power based on the joystick position
        }
        else
        {
            l_tape_power = 0; //set the arm power based on the joystick position
        }

        m_arm_power (l_arm_power); //apply the arm power
        m_winder_power (l_winder_power); //apply the arm power
        m_tape_power(l_tape_power); //apply the tape power

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
        if (gamepad2.dpad_up) //if the up pad is pressed
        {
            try
            {
                //v_servo_bumper_position = a_bumper_position();
                //v_servo_bumper_position += .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_bumper_position = Range.clip(v_servo_bumper_position, BUMPER_MIN_RANGE, BUMPER_MAX_RANGE);

                m_bumper_position(.9); //raise the bumpers. continuous servo, so 1 = clockwise, 0 = counterclockwise, .5 = stopped
                v_bumper_direction = "up"; //save variable. this is used to power off the servos if the bumpers are down.
            }
            catch (Exception p_exception)
            {
                //something went wrong, but the program won't crash
            }
            try
            {
                //v_servo_back_bumper_position = a_back_bumper_position();
                //v_servo_back_bumper_position += .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_back_bumper_position = Range.clip(v_servo_back_bumper_position, BACK_BUMPER_MIN_RANGE, BACK_BUMPER_MAX_RANGE);

                m__back_bumper_position(.1); //raise the bumpers
            }
            catch (Exception p_exception)
            {
                //something happened bad
            }
        }
        else if (gamepad2.dpad_down)
        {
            try
            {
                //v_servo_bumper_position = a_bumper_position();
                //v_servo_bumper_position -= .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_bumper_position = Range.clip(v_servo_bumper_position, BUMPER_MIN_RANGE, BUMPER_MAX_RANGE);

                m_bumper_position(.1);
                v_bumper_direction = "down";
            }
            catch (Exception p_exception)
            {

            }
            try
            {
                //v_servo_back_bumper_position = a_back_bumper_position();
                //v_servo_back_bumper_position -= .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_back_bumper_position = Range.clip(v_servo_back_bumper_position, BACK_BUMPER_MIN_RANGE, BACK_BUMPER_MAX_RANGE);

                m__back_bumper_position(.9);
            }
            catch (Exception p_exception)
            {

            }
        }
        else if (v_bumper_direction == "down")
        {
            m_bumper_position(.5);
            m__back_bumper_position(.5);
        }
        else if (v_bumper_direction == "up")
        {
            m_bumper_position(.7);
            m__back_bumper_position(.3);
        }

        if (gamepad1.dpad_right)
        {
            try
            {
                //v_servo_bumper_position = a_bumper_position();
                //v_servo_bumper_position += .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_bumper_position = Range.clip(v_servo_bumper_position, BUMPER_MIN_RANGE, BUMPER_MAX_RANGE);

                m_triggers_position(.8); //according to the manual for the continuous servos, 128 is stopped, 0 is full speed counterclockwise, and 255 is full speed clockwise
            }
            catch (Exception p_exception)
            {

            }
            try
            {
                //v_servo_back_bumper_position = a_back_bumper_position();
                //v_servo_back_bumper_position += .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_back_bumper_position = Range.clip(v_servo_back_bumper_position, BACK_BUMPER_MIN_RANGE, BACK_BUMPER_MAX_RANGE);

                m_other_triggers_position(.9);
            }
            catch (Exception p_exception)
            {

            }
        }
        else if (gamepad1.dpad_left)
        {
            try
            {
                //v_servo_bumper_position = a_bumper_position();
                //v_servo_bumper_position -= .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_bumper_position = Range.clip(v_servo_bumper_position, BUMPER_MIN_RANGE, BUMPER_MAX_RANGE);

                m_triggers_position(0);
            }
            catch (Exception p_exception)
            {

            }
            try
            {
                //v_servo_back_bumper_position = a_back_bumper_position();
                //v_servo_back_bumper_position -= .05;
                //// clip the position values so that they never exceed their allowed range.
                //v_servo_back_bumper_position = Range.clip(v_servo_back_bumper_position, BACK_BUMPER_MIN_RANGE, BACK_BUMPER_MAX_RANGE);

                m_other_triggers_position(.2);
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
        //if (gamepad2.a) { //if the a button is pressed
        //    reset_all_encoders(); //reset the encoders
        //}
        //if (gamepad1.a) { //if the other a button is pressed
        //    reset_all_encoders(); //reset the encoders
        //}
        //
        // Send telemetry data to the driver station.
        //
        update_telemetry(); // Update common telemetry
        update_gamepad_telemetry();


    } // loop
    
} // PushBotManual
