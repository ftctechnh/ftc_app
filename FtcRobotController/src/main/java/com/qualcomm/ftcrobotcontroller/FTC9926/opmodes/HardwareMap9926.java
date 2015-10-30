package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ibravo on 10/30/15.
 * To place all the hardware components
 *
 */
public class HardwareMap9926 extends OpMode {

    //Define what hardware to use:
    // Motor Controller
    private DcMotorController v_dc_motor_controller_drive;

    // Motor Left and encoder
    private DcMotor v_motor_left_drive;
    final int v_channel_left_drive = 1;

    // Servo ARM
    Servo v_servo_left_hand;


    @Override
    public void init(){

        // Define Config Name in Driver Station
        double l_hand_position = 0.5;

        v_servo_left_hand = hardwareMap.servo.get ("SV1");
        v_servo_left_hand.setPosition (l_hand_position);

    }

    public void start(){

    }

    public void loop(){

    }
    public void stop(){

    }

    //--------------------------------------------------------------------------
    // Access the hand position.
    //--------
    double a_hand_position ()
    {
        return v_servo_left_hand.getPosition();

    } // PushBotManual::a_hand_position

    //--------------------------------------------------------------------------
    // Set the hand position.
    //--------

    void m_hand_position (double p_position)
    {
        // Ensure the specifiec value is legal.
        double l_position = Range.clip
                (p_position
                        , Servo.MIN_POSITION
                        , Servo.MAX_POSITION
                );

        // Set the value.
        v_servo_left_hand.setPosition (l_position);

    } // PushBotManual::m_hand_position



}
