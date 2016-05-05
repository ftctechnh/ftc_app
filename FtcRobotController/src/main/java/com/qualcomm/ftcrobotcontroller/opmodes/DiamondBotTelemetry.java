package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotTelemetry
//
/**
 * Provide telemetry provided by the PushBotHardware class.
 *
 * Insert this class between a custom op-mode and the PushBotHardware class to
 * display telemetry available from the hardware class.
 *
 * @author SSI Robotics
 * @version 2015-08-02-13-57
 *
 * Telemetry Keys
 *     00 - Important message; sometimes used for error messages.
 *     01 - The power being sent to the left drive's motor controller and the
 *          encoder count returned from the motor controller.
 *     02 - The power being sent to the right drive's motor controller and the
 *          encoder count returned from the motor controller.
 *     03 - The power being sent to the left arm's motor controller.
 *     04 - The position being sent to the left and right hand's servo
 *          controller.
 *     05 - The negative value of gamepad 1's left stick's y (vertical; up/down)
 *          value.
 *     06 - The negative value of gamepad 1's right stick's y (vertical;
 *          up/down) value.
 *     07 - The negative value of gamepad 2's left stick's y (vertical; up/down)
 *          value.
 *     08 - The value of gamepad 2's X button (true/false).
 *     09 - The value of gamepad 2's Y button (true/false).
 *     10 - The value of gamepad 1's left trigger value.
 *     11 - The value of gamepad 1's right trigger value.
 */
public class DiamondBotTelemetry extends DiamondBotHardware

{
    //--------------------------------------------------------------------------
    //
    // PushBotTelemetry
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public DiamondBotTelemetry()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.

    } // PushBotTelemetry

    //--------------------------------------------------------------------------
    //
    // update_telemetry
    //
    /**
     * Update the telemetry with current values from the base class.
     */
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
                + a_left_drive_power ()
                + ", Encoder Value="
                + a_left_encoder_count ()
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
            , "Left Arm Motor: Power="
                    + a_left_arm_power()
                    + ", Encoder Value="
                    + a_left_arm_encoder_count()
            );
        telemetry.addData
            ( "04"
            , "Right Arm Motor: Power="
                + a_right_arm_power()
                + ", Encoder Value="
                + a_right_arm_encoder_count()
            );
        telemetry.addData
                ( "05"
                , "Press 'A' button on either gamepad to reset encoders"
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
        telemetry.addData ("06", "Gamepad 1 Left Joystick: " + -gamepad1.left_stick_y);
        telemetry.addData ("07", "Gamepad 1 Right Joystick: " + -gamepad1.right_stick_y);
        telemetry.addData ("08", "Gamepad 1 Left Trigger: " + gamepad1.left_trigger);
        telemetry.addData ("09", "Gamepad 1 Right Trigger: " + gamepad1.right_trigger);
        telemetry.addData ("10", "Gamepad 1 A Button: " + gamepad1.a);
        telemetry.addData ("11", "Gamepad 1 B Button: " + gamepad1.b);
        telemetry.addData ("12", "Gamepad 1 X Button: " + gamepad1.x);
        telemetry.addData ("13", "Gamepad 1 Y Button: " + gamepad1.y);
        telemetry.addData ("14", "Gamepad 1 Up Button: " + gamepad1.dpad_up);
        telemetry.addData ("15", "Gamepad 1 Down Button: " + gamepad1.dpad_down);
        telemetry.addData ("16", "Gamepad 1 Left Button: " + gamepad1.dpad_left);
        telemetry.addData ("17", "Gamepad 1 Right Button: " + gamepad1.dpad_right);
        //
        telemetry.addData ("18", "Gamepad 2 Left Joystick: " + -gamepad2.left_stick_y);
        telemetry.addData ("19", "Gamepad 2 Right Joystick: " + -gamepad2.right_stick_y);
        telemetry.addData ("20", "Gamepad 2 Left Trigger: " + gamepad2.left_trigger);
        telemetry.addData ("21", "Gamepad 2 Right Trigger: " + gamepad2.right_trigger);
        telemetry.addData ("22", "Gamepad 2 A Button: " + gamepad2.a);
        telemetry.addData ("23", "Gamepad 2 B Button: " + gamepad2.b);
        telemetry.addData ("24", "Gamepad 2 X Button: " + gamepad2.x);
        telemetry.addData ("25", "Gamepad 2 Y Button: " + gamepad2.y);
        telemetry.addData ("26", "Gamepad 2 Up Button: " + gamepad2.dpad_up);
        telemetry.addData ("27", "Gamepad 2 Down Button: " + gamepad2.dpad_down);
        telemetry.addData ("28", "Gamepad 2 Left Button: " + gamepad2.dpad_left);
        telemetry.addData ("29", "Gamepad 2 Right Button: " + gamepad2.dpad_right);


    } // update_gamepad_telemetry

    //--------------------------------------------------------------------------
    //
    // set_first_message
    //
    /**
     * Update the telemetry's first message with the specified message.
     */
    public void set_first_message (String p_message)

    {
        telemetry.addData ( "00", p_message);

    } // set_first_message

    //--------------------------------------------------------------------------
    //
    // set_error_message
    //
    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void set_error_message (String p_message)

    {
        set_first_message ("ERROR: " + p_message);

    } // set_error_message

} // PushBotTelemetry
