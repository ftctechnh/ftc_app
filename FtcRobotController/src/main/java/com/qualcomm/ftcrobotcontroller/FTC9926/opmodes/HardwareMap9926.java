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
//    private DcMotorController v_dc_motor_controller_drive;

    // Motor Left and encoder
//    private DcMotor v_motor_left_drive;
//    final int v_channel_left_drive = 1;

    // Servo ARM
    Servo Servo1;
    double SM1_Position;


    @Override
    public void init(){

        // Define Config Name in Driver Station
        double l_hand_position = 0.5;

        Servo1 = hardwareMap.servo.get("SM1");
        SM1_Position = 0.5;

    }

    @Override public void start(){

    }

    @Override public void loop(){

    }
    @Override public void stop(){

    }

    //--------------------------------------------------------------------------
    // Access the hand position.
    //--------
    double a_hand_position ()
    {
        return Servo1.getPosition();

    } // PushBotManual::a_hand_position

    //--------------------------------------------------------------------------
    // Set the hand position.
    //--------

    void m_hand_position (double p_position)
    {
        // Ensure the specifiec value is legal.
//        double l_position = Range.clip(p_position, Servo.MIN_POSITION,Servo.MAX_POSITION);

        SM1_Position = Range.clip(p_position,0,1);

        Servo1.getPosition();

        // Set the value.
//        v_servo_left_hand.setDirection(1);
//        v_servo_left_hand.setPosition(.2);

    } // PushBotManual::m_hand_position



}
