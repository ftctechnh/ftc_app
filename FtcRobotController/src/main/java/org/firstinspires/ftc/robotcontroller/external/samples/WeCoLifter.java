package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Connor on 12/13/2015.
 */
public class WeCoLifter extends OpMode {


    @Override
    public void init() {

    }
    DcMotor motor3 ;
    Servo servo1 ;
    Servo servo2 ;

    float motorPowerMin = -1 ;
    float motorPowerMax = 1  ;
    int scaleNum = 0 ;

    @Override
    public void start() {
        motor3 = hardwareMap.dcMotor.get("motor_3") ;

    }

    public void loop() {

    }

}

