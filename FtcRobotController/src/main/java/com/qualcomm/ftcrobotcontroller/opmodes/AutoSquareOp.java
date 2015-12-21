package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.Direction;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by akhil on 8/30/2015.
 */
public class AutoSquareOp extends OpMode {



    @Override
    public void init() {

    }

    DcMotor motor1;
    DcMotor motor2;

    public AutoSquareOp() {


    }
    static final double normalTurnSpeed = 0.15;
    static final double normalSpeed = 0.25;
    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void start() {
        motor1 = hardwareMap.dcMotor.get("motor_1");
        motor2 = hardwareMap.dcMotor.get("motor_2");

        motor1.setDirection(Direction.REVERSE);


        elapsedTime.reset();
        elapsedTime.startTime();

        moveForward();
    }

    @Override
    public void loop(){
        double etime;
        etime = elapsedTime.time();

        if (etime % 2==0){
           startLeftTurn();
    }
        else if (etime % 2==0.2){
           moveForward();
        }


    }
    public void startLeftTurn(){
        motor1.setPower(normalTurnSpeed);
        motor2.setPower(normalTurnSpeed);

    }
    public void moveForward(){
        motor1.setPower(normalSpeed);
        motor2.setPower(normalSpeed);
    }






}

