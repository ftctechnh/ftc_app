package com.qualcomm.ftcrobotcontroller.opmodes;


//------------------------------------------------------------------------------
//
// RobotMk2
//

/**
 * Provide a basic manual operational mode that uses the left and right
 * drive motors, left arm motor, servo motors and gamepad input from two
 * gamepads for the Diamond Bot.
 *
 * @author Max Davy/Diamond Blades
 * @version 2016-01-18-18-16
 */
public class DiamondFlipperTest extends DiamondBotHardware_Larry

{


    //--------------------------------------------------------------------------
    //
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public DiamondFlipperTest()
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
        m_bumper_position(.5);
        m__back_bumper_position(.5);
        m_triggers_position(0);
        m_other_triggers_position(.89);
    }

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
        if (gamepad1.dpad_left)
        {
            m_left_flipper_position(a_left_flipper_position()+.01);
        }
        else if (gamepad1.dpad_right)
        {
            m_left_flipper_position(a_left_flipper_position()-.01);
        }
        else if (gamepad1.dpad_up)
        {
            m_left_flipper_position(1);
        }
        else if (gamepad1.dpad_down)
        {
            m_left_flipper_position(.9);
        }
        telemetry.addData("01", "Left Flipper Position: "+a_left_flipper_position());
    } // loop
    
} // PushBotManual
