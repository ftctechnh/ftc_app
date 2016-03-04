package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//------------------------------------------------------------------------------
//
// DiamondBotHardware
//

/**
 * Provides a single hardware access point between custom op-modes and the
 * OpMode class for the Diamond Blades Robot.
 *
 * This class prevents the custom op-mode from throwing an exception at runtime.
 * If any hardware fails to map, a warning will be shown via telemetry data,
 * calls to methods will fail, but will not cause the application to crash.
 * blame nate for the larry
 * @author SSI Robotics/Max Davy/Diamond Blades
 * @version 2016-01-29-14-15
 */
public class DiamondBotHardware_Larry extends OpMode

{
    //--------------------------------------------------------------------------
    //
    // v_warning_generated
    //
    /**
     * Indicate whether a message is a available to the class user.
     */
    private boolean v_warning_generated = false;

    //--------------------------------------------------------------------------
    //
    // v_warning_message
    //
    /**
     * Store a message to the user if one has been generated.
     */
    private String v_warning_message;

    //--------------------------------------------------------------------------
    //
    // v_motor_left_drive
    //
    /**
     * Manage the aspects of the left drive motor.
     */
    private DcMotor v_motor_left_drive;

    //--------------------------------------------------------------------------
    //
    // v_motor_right_drive
    //
    /**
     * Manage the aspects of the right drive motor.
     */
    private DcMotor v_motor_right_drive;

    //--------------------------------------------------------------------------
    //
    // v_motor_arm && v_motor_winder
    //
    /**
     * Manage the aspects of the arm motors.
     */
    private DcMotor v_motor_arm;

    private DcMotor v_motor_winder;

    private DcMotor v_motor_tape;

    //--------------------------------------------------------------------------
    //
    //
    //
    /**
     * Manage the aspects of the servos.
     */
    private Servo v_servo_bumper;
    private Servo v_servo_back_bumper;
    private Servo v_servo_triggers;
    private Servo v_servo_other_triggers;
    private Servo v_servo_right_flipper;
    private Servo v_servo_left_flipper;

    //
    // PushBotHardware
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public DiamondBotHardware_Larry()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotHardware

    //--------------------------------------------------------------------------
    //
    // init
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void init ()

    {
        //
        // Use the hardwareMap to associate class members to hardware ports.
        //
        // Note that the names of the devices (i.e. arguments to the get method)
        // must match the names specified in the configuration file created by
        // the FTC Robot Controller (Settings-->Configure Robot).
        //
        // The variable below is used to provide telemetry data to a class user.
        //
        v_warning_generated = false;
        v_warning_message = "Can't map; ";

        //
        // Connect the drive wheel motors.
        //
        // The direction of the right motor is reversed, so joystick inputs can
        // be more generically applied.
        //
        try
        {
            v_motor_left_drive = hardwareMap.dcMotor.get ("left_drive");
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("left_drive");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_motor_left_drive = null;
        }

        try
        {
            v_motor_right_drive = hardwareMap.dcMotor.get ("right_drive");
            v_motor_right_drive.setDirection (DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("right_drive");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_motor_right_drive = null;
        }

        //
        // Connect the arm motor.
        //
        try
        {
            v_motor_arm = hardwareMap.dcMotor.get ("arm");
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("arm");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_motor_arm = null;
        }

        try
        {
            v_motor_winder = hardwareMap.dcMotor.get ("winder");
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("winder");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_motor_winder = null;
        }

        try
        {
            v_motor_tape = hardwareMap.dcMotor.get ("tape");
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("tape");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_motor_tape = null;
        }

        //
        // Connect the servo motors.
        //
        // Indicate the initial position of both the left and right servos.  The
        // hand should be halfway opened/closed.
        //
        double initial_bumper_position = 0.5;
        double initial_back_bumper_position = 0.5;

        try
        {
            v_servo_bumper = hardwareMap.servo.get ("bumper");
            //v_servo_bumper.setPosition (initial_bumper_position);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("bumper");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_servo_bumper = null;
        }
        try
        {
            v_servo_back_bumper = hardwareMap.servo.get ("back_bumper");
            //v_servo_back_bumper.setPosition (initial_back_bumper_position);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("back_bumper");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_servo_back_bumper = null;
        }
        try
        {
            v_servo_triggers = hardwareMap.servo.get ("triggers");
            //v_servo_back_bumper.setPosition (initial_back_bumper_position);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("triggers");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_servo_triggers = null;
        }
        try
        {
            v_servo_other_triggers = hardwareMap.servo.get ("other_triggers");
            //v_servo_back_bumper.setPosition (initial_back_bumper_position);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("other_triggers");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_servo_other_triggers = null;
        }
        try
        {
            v_servo_left_flipper = hardwareMap.servo.get ("left_flipper");
            //v_servo_back_bumper.setPosition (initial_back_bumper_position);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("left_flipper");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_servo_left_flipper = null;
        }
        try
        {
            v_servo_right_flipper = hardwareMap.servo.get ("right_flipper");
            //v_servo_back_bumper.setPosition (initial_back_bumper_position);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("right_flipper");
            DbgLog.msg (p_exeception.getLocalizedMessage ());

            v_servo_right_flipper = null;
        }
    } // init

    //--------------------------------------------------------------------------
    //
    // a_warning_generated
    //
    /**
     * Access whether a warning has been generated.
     */
    boolean a_warning_generated ()

    {
        return v_warning_generated;

    } // a_warning_generated

    //--------------------------------------------------------------------------
    //
    // a_warning_message
    //
    /**
     * Access the warning message.
     */
    String a_warning_message ()

    {
        return v_warning_message;

    } // a_warning_message

    //--------------------------------------------------------------------------
    //
    // m_warning_message
    //
    /**
     * Mutate the warning message by ADDING the specified message to the current
     * message; set the warning indicator to true.
     *
     * A comma will be added before the specified message if the message isn't
     * empty.
     */
    void m_warning_message (String p_exception_message)

    {
        if (v_warning_generated)
        {
            v_warning_message += ", ";
        }
        v_warning_generated = true;
        v_warning_message += p_exception_message;

    } // m_warning_message

    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()

    {
        //
        // Only actions that are common to all Op-Modes (i.e. both automatic and
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Perform any actions that are necessary while the OpMode is running.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //
        // Only actions that are common to all OpModes (i.e. both auto and\
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // loop

    //--------------------------------------------------------------------------
    //
    // stop
    //
    /**
     * Perform any actions that are necessary when the OpMode is disabled.
     *
     * The system calls this member once when the OpMode is disabled.
     */
    @Override public void stop ()
    {
        //
        // Nothing needs to be done for this method.
        //

    } // stop

    //--------------------------------------------------------------------------
    //
    // scale_motor_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scale_motor_power (float p_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip (p_power, -1, 1);

        float[] l_array =
            { 0.00f, 0.06f, 0.12f, 0.18f, 0.24f
            , 0.30f, 0.36f, 0.42f, 0.48f, 0.54f
            , 0.60f, 0.66f, 0.72f, 0.78f, 0.84f
            , 0.90f, 0.96f
            };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }

        return l_scale;

    } // scale_arm_motor_power

    float scale_arm_motor_power (float p_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip (p_power, -1, 1);

        float[] l_array =
                { 0.00f, 0.06f, 0.12f, 0.18f, 0.24f
                , 0.30f, 0.36f, 0.42f, 0.48f, 0.54f
                , 0.60f, 0.66f, 0.72f, 0.78f, 0.84f
                , 0.90f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = (float) (-l_array[l_index]*.8);
        }
        else
        {
            l_scale = (float) (l_array[l_index]*.8);
        }

        return l_scale;

    } // scale_motor_power

    //--------------------------------------------------------------------------
    //
    // a_left_drive_power
    //
    /**
     * Access the left drive motor's power level.
     */
    double a_left_drive_power ()
    {
        double l_return = 0.0;

        if (v_motor_left_drive != null)
        {
            l_return = v_motor_left_drive.getPower ();
        }

        return l_return;

    } // a_left_drive_power

    //--------------------------------------------------------------------------
    //
    // a_right_drive_power
    //
    /**
     * Access the right drive motor's power level.
     */
    double a_right_drive_power ()
    {
        double l_return = 0.0;

        if (v_motor_right_drive != null)
        {
            l_return = v_motor_right_drive.getPower ();
        }

        return l_return;

    } // a_right_drive_power

    //--------------------------------------------------------------------------
    //
    // set_drive_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    void set_drive_power (double p_left_power, double p_right_power)

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setPower (p_left_power);
        }
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setPower (p_right_power);
        }

    } // set_drive_power

    //--------------------------------------------------------------------------
    //
    // run_using_left_arm_encoder
    //
    /**
     * Set the left arm encoder to run, if the mode is appropriate.
     */
    public void run_using_left_arm_encoder ()

    {
        if (v_motor_arm != null)
        {
            v_motor_arm.setMode
                ( DcMotorController.RunMode.RUN_USING_ENCODERS
                );
        }

    } // run_using_left_arm_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_right_arm_encoder
    //
    /**
     * Set the right arm encoder to run, if the mode is appropriate.
     */
    public void run_using_right_arm_encoder ()

    {
        if (v_motor_winder != null)
        {
            v_motor_winder.setMode
                    (DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_left_drive_encoder
    //
    /**
     * Set the left drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_right_drive_encoder
    //
    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_right_drive_encoder

    public void run_using_tape_encoder ()

    {
        if (v_motor_tape != null)
        {
            v_motor_tape.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    }
    //--------------------------------------------------------------------------
    //
    // run_using_encoders
    //
    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_using_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_using_left_drive_encoder ();
        run_using_right_drive_encoder();
        run_using_left_arm_encoder();
        run_using_right_arm_encoder();
        run_using_tape_encoder();

    } // run_using_encoders

    //--------------------------------------------------------------------------
    //
    // run_without_left_drive_encoder
    //
    /**
     * Set the left drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_without_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            if (v_motor_left_drive.getMode () ==
                DcMotorController.RunMode.RESET_ENCODERS)
            {
                v_motor_left_drive.setMode
                    ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                    );
            }
        }

    } // run_without_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_without_right_drive_encoder
    //
    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_without_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            if (v_motor_right_drive.getMode () ==
                DcMotorController.RunMode.RESET_ENCODERS)
            {
                v_motor_right_drive.setMode
                    ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                    );
            }
        }

    } // run_without_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_without_drive_encoders
    //
    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_without_drive_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_without_left_drive_encoder ();
        run_without_right_drive_encoder ();

    } // run_without_drive_encoders

    //--------------------------------------------------------------------------
    //
    // reset_left_drive_encoder
    //
    /**
     * Reset the left drive wheel encoder.
     */
    public void reset_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setMode
                ( DcMotorController.RunMode.RESET_ENCODERS
                );
        }

    } // reset_left_drivem_encoder

    public void reset_arm_encoder ()

    {
        if (v_motor_arm != null)
        {
            v_motor_arm.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_arm_encoder

    public void reset_winder_encoder ()

    {
        if (v_motor_winder != null)
        {
            v_motor_winder.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_left_drive_encoder
    public void reset_tape_encoder ()

    {
        if (v_motor_tape != null)
        {
            v_motor_tape.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }

    //--------------------------------------------------------------------------
    //
    // reset_right_drive_encoder
    //
    /**
     * Reset the right drive wheel encoder.
     */
    public void reset_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setMode
                ( DcMotorController.RunMode.RESET_ENCODERS
                );
        }

    } // reset_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_drive_encoders
    //
    /**
     * Reset both drive wheel encoders.
     */
    public void reset_drive_encoders ()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_left_drive_encoder ();
        reset_right_drive_encoder ();

    } // reset_drive_encoders

    //--------------------------------------------------------------------------
    //
    // reset_arm_encoders
    //
    /**
     * Reset both drive wheel encoders.
     */
    public void reset_arm_encoders ()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_arm_encoder();
        reset_winder_encoder();
        reset_tape_encoder();

    } // reset_arm_encoders

    public void reset_all_encoders ()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_left_drive_encoder ();
        reset_right_drive_encoder();
        reset_arm_encoder();
        reset_winder_encoder();
        reset_tape_encoder();

    }

    //--------------------------------------------------------------------------
    //
    // a_left_encoder_count
    //
    /**
     * Access the left encoder's count.
     */
    int a_left_encoder_count ()
    {
        int l_return = 0;

        if (v_motor_left_drive != null)
        {
            l_return = v_motor_left_drive.getCurrentPosition ();
        }

        return l_return;

    } // a_left_encoder_count

    //--------------------------------------------------------------------------
    //
    // a_right_encoder_count
    //
    /**
     * Access the right encoder's count.
     */
    int a_right_encoder_count ()

    {
        int l_return = 0;

        if (v_motor_right_drive != null)
        {
            l_return = v_motor_right_drive.getCurrentPosition ();
        }

        return l_return;

    } // a_right_encoder_count

    int a_left_arm_encoder_count ()

    {
        int l_return = 0;

        if (v_motor_arm != null)
        {
            l_return = v_motor_arm.getCurrentPosition ();
        }

        return l_return;

    }

    int a_right_arm_encoder_count ()

    {
        int l_return = 0;

        if (v_motor_winder != null)
        {
            l_return = v_motor_winder.getCurrentPosition ();
        }

        return l_return;

    }

    int a_tape_encoder_count ()

    {
        int l_return = 0;

        if (v_motor_tape != null)
        {
            l_return = v_motor_tape.getCurrentPosition ();
        }

        return l_return;

    }

    //--------------------------------------------------------------------------
    //
    // set_drive_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    void set_arm_power (double p_left_power, double p_right_power)

    {
        if (v_motor_arm != null)
        {
            v_motor_arm.setPower (p_left_power);
        }
        if (v_motor_winder != null)
        {
            v_motor_winder.setPower (p_right_power);
        }

    } // set_drive_power

    //--------------------------------------------------------------------------
    //
    // has_left_arm_encoder_reached
    //
    /**
     * Indicate whether the left arm motor's encoder has reached a value.
     */
    boolean has_left_arm_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (v_motor_arm != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            if (Math.abs (v_motor_arm.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_arm_encoder_reached

    //--------------------------------------------------------------------------
    //
    // has_right_arm_encoder_reached
    //
    /**
     * Indicate whether the right arm motor's encoder has reached a value.
     */
    boolean has_right_arm_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (v_motor_winder != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            if (Math.abs (v_motor_winder.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_arm_encoder_reached

    //--------------------------------------------------------------------------
    //
    // have_arm_encoders_reached
    //
    /**
     * Indicate whether the arm motors' encoders have reached a value.
     */
    boolean have_arm_encoders_reached
        ( double p_left_count
        , double p_right_count
        )

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached the specified values?
        //
        if (has_left_arm_encoder_reached(p_left_count) &&
            has_right_arm_encoder_reached(p_right_count))
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_arm_encoders_reached

    //--------------------------------------------------------------------------
    //
    // arm_using_encoders
    //
    /**
     * Indicate whether the arm motors' encoders have reached a value.
     */
    boolean arm_using_encoders
        ( double p_left_power
        , double p_right_power
        , double p_left_count
        , double p_right_count
        )

    {
        //
        // Assume the encoders have not reached the limit.
        //
        boolean l_return = false;

        //
        // Tell the system that motor encoders will be used.
        //
        run_using_encoders ();

        //
        // Start the drive wheel motors at full power.
        //
        set_arm_power(p_left_power, p_right_power);

        //
        // Have the motor shafts turned the required amount?
        //
        // If they haven't, then the op-mode remains in this state (i.e this
        // block will be executed the next time this method is called).
        //
        if (have_arm_encoders_reached(p_left_count, p_right_count))
        {
            //
            // Reset the encoders to ensure they are at a known good value.
            //
            reset_arm_encoders();

            //
            // Stop the motors.
            //
            set_arm_power(0.0f, 0.0f);

            //
            // Transition to the next state when this method is called
            // again.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // drive_using_encoders

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_left_arm_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the left encoder reached zero?
        //
        if (a_left_arm_encoder_count() == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_right_arm_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the right encoder reached zero?
        //
        if (a_right_arm_encoder_count() == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reset
    //
    /**
     * Indicate whether the encoders have been completely reset.
     */
    boolean have_arm_encoders_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached zero?
        //
        if (has_left_arm_encoder_reset() && has_right_arm_encoder_reset())
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_drive_encoders_reset

    //--------------------------------------------------------------------------
    //
    // a_left_arm_power
    //
    /**
     * Access the left arm motor's power level.
     */
    double a_left_arm_power ()
    {
        double l_return = 0.0;

        if (v_motor_arm != null)
        {
            l_return = v_motor_arm.getPower ();
        }

        return l_return;

    } // a_left_arm_power

    double a_right_arm_power ()
    {
        double l_return = 0.0;

        if (v_motor_winder != null)
        {
            l_return = v_motor_winder.getPower ();
        }

        return l_return;

    } // a_left_arm_power

    double a_tape_power ()
    {
        double l_return = 0.0;

        if (v_motor_tape != null)
        {
            l_return = v_motor_tape.getPower ();
        }

        return l_return;

    }

    //--------------------------------------------------------------------------
    //
    // m_left_arm_power
    //
    /**
     * Access the left arm motor's power level.
     */
    void m_arm_power (double p_level)
    {
        if (v_motor_arm != null)
        {
            v_motor_arm.setPower (p_level);
        }

    } // m_left_arm_power

    void m_winder_power (double p_level)
    {
        if (v_motor_winder != null)
        {
            v_motor_winder.setPower (p_level);
        }

    } // m_left_arm_power

    void m_tape_power (double p_level)
    {
        if (v_motor_tape != null)
        {
            v_motor_tape.setPower (p_level);
        }

    }
    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reached
    //
    /**
     * Indicate whether the left drive motor's encoder has reached a value.
     */
    boolean has_left_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (v_motor_left_drive != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            //
            if (Math.abs (v_motor_left_drive.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reached
    //
    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (v_motor_right_drive != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            //
            if (Math.abs (v_motor_right_drive.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reached
    //
    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean have_drive_encoders_reached
    ( double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached the specified values?
        //
        if (has_left_drive_encoder_reached (p_left_count) &&
                has_right_drive_encoder_reached (p_right_count))
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_encoders_reached

    //--------------------------------------------------------------------------
    //
    // drive_using_encoders
    //
    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean drive_using_encoders
    ( double p_left_power
            , double p_right_power
            , double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume the encoders have not reached the limit.
        //
        boolean l_return = false;

        //
        // Tell the system that motor encoders will be used.
        //
        run_using_encoders ();

        //
        // Start the drive wheel motors at full power.
        //
        set_drive_power (p_left_power, p_right_power);

        //
        // Have the motor shafts turned the required amount?
        //
        // If they haven't, then the op-mode remains in this state (i.e this
        // block will be executed the next time this method is called).
        //
        if (have_drive_encoders_reached (p_left_count, p_right_count))
        {
            //
            // Reset the encoders to ensure they are at a known good value.
            //
            reset_drive_encoders ();

            //
            // Stop the motors.
            //
            set_drive_power (0.0f, 0.0f);

            //
            // Transition to the next state when this method is called
            // again.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // drive_using_encoders

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_left_drive_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the left encoder reached zero?
        //
        if (a_left_encoder_count () == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_right_drive_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the right encoder reached zero?
        //
        if (a_right_encoder_count() == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reset
    //
    /**
     * Indicate whether the encoders have been completely reset.
     */
    boolean have_drive_encoders_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached zero?
        //
        if (has_left_drive_encoder_reset() && has_right_drive_encoder_reset ())
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_drive_encoders_reset


    //--------------------------------------------------------------------------
    //
    // a_bumper_position
    //
    /**
     * Access the bucket position.
     */
    double a_bumper_position ()
    {
        double l_return = 0.0;

        if (v_servo_bumper != null)
        {
            l_return = v_servo_bumper.getPosition ();
        }

        return l_return;

    } // a_bumper_position
    double a_back_bumper_position ()
    {
        double l_return = 0.0;

        if (v_servo_back_bumper != null)
        {
            l_return = v_servo_back_bumper.getPosition ();
        }

        return l_return;

    }

    double a_triggers_position ()
    {
        double l_return = 0.0;

        if (v_servo_triggers != null)
        {
            l_return = v_servo_triggers.getPosition ();
        }

        return l_return;

    }

    double a_other_triggers_position ()
    {
        double l_return = 0.0;

        if (v_servo_other_triggers != null)
        {
            l_return = v_servo_other_triggers.getPosition ();
        }

        return l_return;

    }

    double a_left_flipper_position ()
    {
        double l_return = 0.0;

        if (v_servo_left_flipper != null)
        {
            l_return = v_servo_left_flipper.getPosition ();
        }

        return l_return;

    }


    double a_right_flipper_position ()
    {
        double l_return = 0.0;

        if (v_servo_right_flipper != null)
        {
            l_return = v_servo_right_flipper.getPosition ();
        }

        return l_return;

    }

    //--------------------------------------------------------------------------
    //
    // m_bumper_position
    //
    /**
     * Change the bumper positions.
     */

    void m_bumper_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
            ( p_position
            , Servo.MIN_POSITION
            , Servo.MAX_POSITION
            );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (v_servo_bumper != null)
        {
            v_servo_bumper.setPosition (l_position);
        }

    } // m_bumper_position
    void m__back_bumper_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , Servo.MIN_POSITION
                        , Servo.MAX_POSITION
                );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (v_servo_back_bumper != null)
        {
            v_servo_back_bumper.setPosition (l_position);
        }

    }
    //set the trigger positions
    void m_triggers_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , Servo.MIN_POSITION
                        , Servo.MAX_POSITION
                );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (v_servo_triggers != null)
        {
            v_servo_triggers.setPosition (l_position);
        }

    }

    void m_other_triggers_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , Servo.MIN_POSITION
                        , Servo.MAX_POSITION
                );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (v_servo_other_triggers != null)
        {
            v_servo_other_triggers.setPosition (l_position);
        }

    }
    //set the flipper positions
    void m_left_flipper_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , Servo.MIN_POSITION
                        , Servo.MAX_POSITION
                );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (v_servo_left_flipper != null)
        {
            v_servo_left_flipper.setPosition (l_position);
        }

    }

    void m_right_flipper_position (double p_position)
    {
        //
        // Ensure the specific value is legal.
        //
        double l_position = Range.clip
                ( p_position
                        , Servo.MIN_POSITION
                        , Servo.MAX_POSITION
                );

        //
        // Set the value.  The right hand value must be opposite of the left
        // value.
        //
        if (v_servo_right_flipper != null)
        {
            v_servo_right_flipper.setPosition (l_position);
        }

    }



} // PushBotHardware
