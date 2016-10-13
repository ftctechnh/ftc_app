package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Connor on 8/2/2016.
 */
public class WeCoAutoOp extends OpMode {

    @Override
    public void init (){
        count = 0;
    }

    DcMotor motor1;
    DcMotor motor2;

    double count;
    double etime;
    int whattodo = 0; //0 do nothing; 1 moveforward; 2 turn


    public WeCoAutoOp(){

    }

    static final float normalTurnSpeed = (float) 0.75;
    static final float normalSpeed = (float) 0.75;

    ElapsedTime elapsedTime = new ElapsedTime();



    float motor1power = 0;
    float motor2power = 0;

    @Override
    public void start(){
        motor1 = hardwareMap.dcMotor.get("motorRight");
        motor2 = hardwareMap.dcMotor.get("motorLeft");

        motor1.setDirection(DcMotor.Direction.REVERSE);
        count = 0;


    }

    @Override
    public void loop (){
        etime = elapsedTime.time() * 100;
        // double etimemod2 = etime % 2
        if (count < 4);
        }

    }



