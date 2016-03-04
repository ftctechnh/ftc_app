package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// DiamondAutonomousRed
//
/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the DiamondBot, when playing as red.
 *
 * @author SSI Robotics/DiamondBlades
 * @version 2015-08-01-06-01
 */
public class DiamondAutonomousShort extends DiamondBotHardware_Larry

{
    //--------------------------------------------------------------------------
    //
    // PushBotAuto
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public DiamondAutonomousShort()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // AutonomousRed

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
        // Reset the motor encoders on the drive wheels and arms.
        //
        reset_drive_encoders();
        reset_arm_encoders();
        m_right_flipper_position(.25);
        m_left_flipper_position(1);
        m_bumper_position(.5);
        m__back_bumper_position(.5);
        m_triggers_position(0);
        m_other_triggers_position(.89);

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and encoder input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //----------------------------------------------------------------------
        //
        // State: Initialize (i.e. state_0).
        //
        switch (v_state)
        {
        //
        // Synchronize the state machine and hardware.
        //
        case 0:
            //
            // Reset the encoders to ensure they are at a known good value.
            //
            reset_drive_encoders();


            //
            // Transition to the next state when this method is called again.
            //
            v_state++;

            break;
        //
        // Drive forward until the encoders exceed the specified values.
        //
        case 1:
                m__back_bumper_position(.9);
                v_state ++;
            break;
        case 2:
            //
            // Tell the system that motor encoders will be used.  This call MUST
            // be in this state and NOT the previous or the encoders will not
            // work.  It doesn't need to be in subsequent states.
            //
            run_using_encoders ();

            //
            // Start the drive wheel motors at full power.
            //
            set_drive_power (1.0f, 1.0f);

            //
            // Have the motor shafts turned the required amount?
            //
            // If they haven't, then the op-mode remains in this state (i.e this
            // block will be executed the next time this method is called).
            //
            if (have_drive_encoders_reached (6100, 6100))
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
                v_state++;
            }
            break;
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
        if (v_state == 0)
        {
            telemetry.addData("18", "State: " + v_state + "Initializing");
        }
        else if (v_state == 1)
        {
            telemetry.addData("18", "State: " + v_state + "Lowering bumper");
        }
        else if (v_state == 2)
        {
            telemetry.addData("18", "State: " + v_state + "Driving forward");
        }
        else
        {
            telemetry.addData("18", "State: " + v_state + "Finished");
        }


    } // loop

    //--------------------------------------------------------------------------
    //
    // v_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialized (0).  When the loop
     * starts, the state will change from initialize to state_1.  When state_1
     * actions are complete, the state will change to state_2.  This implements
     * a state machine for the loop method.
     */
    private int v_state = 0;

} // PushBotAuto
