package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.TouchSensor;

import android.graphics.Color;

/**
 * Created by harmony on 12/10/2015.
 */
//------------------------------------------------------------------------------
//
// PushBotAutoCiphersBlueSide
//
public class PushBotAutoCiphersBlueSide extends PushBotTelemetrySensors
{

    public enum ColorSensorDevice {ADAFRUIT, HITECHNIC_NXT, MODERN_ROBOTICS_I2C};

    public ColorSensorDevice device = ColorSensorDevice.MODERN_ROBOTICS_I2C;

    ColorSensor colorSensor;
    DeviceInterfaceModule cdim;

    // Color sensor readings
    int blue;
    int red;
    int green;

    // sRGB Target color space
    int color = android.graphics.Color.BLUE;

    // Geometry of Robot
    int WHEEL_DIAMETER = 4; // in inches
    int WHEEL_CIRCUMFERENCE = (int) (Math.PI * WHEEL_DIAMETER);

    // Geometry of intended autonomous route
    int Y_DEF = 4 * 12; // assume 4 feet for the first segment
    int Z_DEF = 4 * 12; // assume 6 feet for the first segment
    int A_ANGLE_DEGREE = 45;
    int A_ANGLE_RADIAL = (int) ((Math.PI * A_ANGLE_DEGREE) / 180);

    //--------------------------------------------------------------------------
    //
    // PushBotAutoCiphersBlueSide
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public PushBotAutoCiphersBlueSide ()
    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.
        hardwareMap.logDevices();

        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        switch (device) {
            case HITECHNIC_NXT:
                colorSensor = hardwareMap.colorSensor.get("nxt");
                break;
            case ADAFRUIT:
                colorSensor = hardwareMap.colorSensor.get("lady");
                break;
            case MODERN_ROBOTICS_I2C:
                colorSensor = hardwareMap.colorSensor.get("mr");
                break;
        }

    } // PushBotAutoCiphersBlueSide

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
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start ();

        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_drive_encoders ();

        //
        // Reset the gyro.
        //
        //reset_gyro ();

        //
        // Reset Distance Color Sensor
        //
        //reset_distance_color_sensor ();

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
            case 0: // Drive forward for a predefined distance: Y_DEF
                //
                // Wait for the encoders to reset.  This might take multiple cycles.
                //
                if (have_drive_encoders_reset ())
                {
                    //
                    // determine how to use it
                    //
                    read_color_sensor();

                    //
                    // Begin the next state.  Drive forward for a predefined distance: Y_DEF
                    //
                    drive_leg(Y_DEF);

                    //
                    // Transition to the next state.
                    //
                    v_state++;
                }

                break;
            //
            // State 1.
            //
            case 1: // Turn angle A to the right.
                //
                // Wait for the encoders to reset.  This might take multiple cycles.
                //
                if (have_drive_encoders_reset ())
                {
                    //
                    // determine how to use it
                    //
                    read_color_sensor();

                    //
                    // Begin the next state.  Turn angle A to the right.
                    //
                    turn_angle(A_ANGLE_DEGREE);

                    //
                    // The drive wheels have reached the specified encoder values,
                    // so transition to the next state when this method is called
                    // again.
                    //
                    v_state++;
                }
                break;
            //
            // State 2.
            //
            case 2: // Drive forward for a predefined distance: Z_DEF

                //
                // Wait for the encoders to reset.  This might take multiple cycles.
                //
                if (have_drive_encoders_reset())
                {
                    //
                    // determine how to use it
                    //
                    read_color_sensor();

                    //
                    // Begin the next state.  Drive forward for a predefined distance: Z_DEF
                    //
                    drive_leg(Z_DEF);

                    //
                    // Transition to the next state.
                    //
                    v_state++;
                }

                break;

            case 3: // Read color sensor. Based on colors, Turn angle B to the right or to the left.
                //
                // Wait for the encoders to reset.  This might take multiple cycles.
                //
                if (have_drive_encoders_reset ())
                {
                    //
                    read_color_sensor();

                    //
                    // calculate angle B based on color readings
                    //
                    int B_ANGLE_DEGREE = get_angle_towards_color(color);

                    //
                    // Begin the next state.  Turn angle A to the right.
                    //
                    turn_angle(B_ANGLE_DEGREE);

                    //
                    // The drive wheels have reached the specified encoder values,
                    // so transition to the next state when this method is called
                    // again.
                    //
                    v_state++;
                }
                break;

            //
            // State 4.
            //
            case 4:
                //
                // As soon as the drive encoders reset, begin the next state.
                //
                if (have_drive_encoders_reset ())
                {
                    //
                    // Begin the next state.
                    //
                    // There is no conditional (if statement) here, because the
                    // motor power won't be applied until this method exits.
                    //
                    run_without_drive_encoders ();

                    //
                    // Raise Arm.
                    //
                    raise_arm();


                    //
                    // Drive forward to the target button: Y_DEF
                    // TODO: How long of a leg? Use Y_DEF for now.
                    // TODO: How high is the button?
                    //
                    drive_leg(Y_DEF);

                    v_state++;
                }
                break;
            //
            // State 5.
            //
            case 5:
                //
                // FROM HERE ON: NOT YET IMPLEMENTED
                //
                // Drive forward until a white line is detected.
                //
                //
                // If a white line has been detected, then set the power level to
                // zero.
                //
                if (a_ods_white_tape_detected ())
                {
                    set_drive_power (0.0, 0.0);

                    //
                    // Begin the next state.
                    //
                    drive_to_ir_beacon ();

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
            case 6:
                //
                // When the robot is close to the IR beacon, open the hand.
                //
                int l_status = drive_to_ir_beacon ();
                if (l_status == drive_to_ir_beacon_done)
                {
                    //
                    // Begin the next state.  Open the claw.
                    //
                    open_hand ();

                    //
                    // Transition.
                    //
                    v_state++;
                }
                else if (l_status == drive_to_ir_beacon_not_detected)
                {
                    set_error_message ("IR beacon not detected!");
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
        // Update the arm state machine.
        //
        update_arm_state ();

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        telemetry.addData("11", "Drive State: " + v_state);
        telemetry.addData ("12", "Arm State: " + v_arm_state);

    } // loop

    private void read_color_sensor() {
        // Read color sensor. Will decide later if we need its reading.
        red = colorSensor.red();
        green = colorSensor.green();
        blue = colorSensor.blue();
    }

    private void turn_angle(int angle) {
        // Determine the math transformation from angle to wheel count
        // The angle value could be used to provide left/right direction.
        // For now, eyeball it.
        int wheel_count = 20;
        drive_using_encoders(1.0f, -1.0f, wheel_count, -wheel_count);
    }

    private void drive_leg(int leg_length) {
        int wheel_count = leg_length / WHEEL_CIRCUMFERENCE;
        drive_using_encoders (1.0f, 1.0f, wheel_count, wheel_count);
    }

    private int get_angle_towards_color(int color) {
        // TODO;
        return 0;
    }

    private void raise_arm() {
        // TODO;
    }

    //--------------------------------------------------------------------------
    //
    // update_arm_state
    //
    /**
     * Implement a state machine that controls the arm during auto-operation.
     */
    public void update_arm_state ()

    {
        //
        // Update the arm state machine.
        //
        switch (v_arm_state)
        {
            //
            // State 0.
            //
            case 0:
                //
                // Wait until a command is given (i.e. v_state is set to 1).
                //
                break;
            //
            // State 1.
            //
            case 1:
                //
                // Continue moving the arm up.  If the touch sensor is
                // triggered, then the arm will stop and this call will perform
                // no action.  If the touch sensor has not been triggered, then
                // motor power will still be applied.
                //
                if (move_arm_upward_until_touch ())
                {
                    //
                    // Transition to the stop state.
                    //
                    v_arm_state++;
                }
                break;
            //
            // Perform no action - stay in this case until the OpMode is
            // stopped.  This method will still be called regardless of the
            // state machine.
            //
            default:
                //
                // The autonomous actions have been accomplished (i.e. the state
                // has transitioned into its final state.
                //
                break;
        }

    } // update_arm_state

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
    // v_arm_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialize (0).  During the
     * first iteration of the loop method, the state will change from initialize
     * to state_1.  When state_1 actions are complete, the state will change to
     * state_2.  This implements a state machine for the loop method.
     */
    private int v_arm_state = 0;

} // PushBotAutoCiphersBlueSide
