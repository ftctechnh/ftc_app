package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

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


}
