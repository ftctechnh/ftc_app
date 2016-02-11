package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAutoSensors
//
/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders, the left arm motor and associated touch
 * sensor, IR seeker V3 and optical distance sensor implemented using a state
 * machine for the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-13-19-48
 */
public class Autobots extends PushBotTelemetrySensors

{




    //--------------------------------------------------------------------------
    //
    // v_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialize (0).  During the
     * first iteration of the loop method, the state will change from initialize
     * to state_1.  When state_1 actions are complete, the state will change to
     * state_2.  This implements a state machine for the loop method.
     */
    private int v_state = 0;


    //--------------------------------------------------------------------------
    //
    // PushBotAutoSensors
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public Autobots ()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotAutoSensors

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
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start ();

        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_drive_encoders();

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and sensor input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //
        // Update the state machines
        //
        switch (v_state)
        {
            //
            // State 0.
            //
            case 0:
                //
                // Wait for the encoders to reset.  This might take multiple cycles.
                //
                if (have_drive_encoders_reset ())
                {
                    //
                    // Begin the next state.  Drive forward.
                    //
                    drive_using_encoders (-1.0f, 1.0f, -2880, 2880);

                    //
                    // Transition to the next state.
                    //
                    v_state++;
                }

                break;
            //
            case 1:
                //
                // Drive forward until a white line is detected.
                //
                //
                // If a white line has been detected, then set the power level to
                // zero.
                //
                if (a_ods_white_tape_detected ())
                {
                    telemetry.addData ("Tape Detected", "Stopping");
                    set_drive_power (0.0, 0.0);


                    //
                    // Transition.
                    //
                    v_state++;
                }
                //
                // Else a white line has not been detected, so set the power level
                // to full forward.
                //
                else
                {
                    set_drive_power (1.0, 1.0);
                }
                break;
            //
            // State 6.
            //
            //
            // Perform no action - stay in this case until the OpMode is stopped.
            // This method will still be called regardless of the state machine.
            //
            default:
                //
                // The autonomous actions have been accomplished (i.e. the state has
                // transitioned into its final state.
                //
                break;
        }


        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        telemetry.addData ("11", "Drive State: " + v_state);

    } // loop

}
