package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by akhil on 9/29/2015.
 */
public class WeCoAutoTriangle extends OpMode {

    @Override
    public void init(){

    }
    DcMotor motor1 ;
    DcMotor motor2 ;
    DcMotor motor3 ;
    DcMotor motor4 ;

    public WeCoAutoTriangle() {

    }
    static final double normalTurnSpeed = 0.15;
    static final double normalSpeed = 0.25;
    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void start() {
        motor1 = hardwareMap.dcMotor.get("motor_1") ;
        motor2 = hardwareMap.dcMotor.get("motor_2") ;
        motor3 = hardwareMap.dcMotor.get("motor_3") ;
        motor4 = hardwareMap.dcMotor.get("motor_4") ;

        motor1.setDirection(DcMotor.Direction.REVERSE) ;
        motor2.setDirection(DcMotor.Direction.REVERSE) ;

        moveForward();
    }

    @Override
    public void loop(){

    }

    public void startLeftTurn(){
        motor1.setPower(normalTurnSpeed);
        motor2.setPower(normalTurnSpeed);
        motor3.setPower(normalSpeed);
        motor4.setPower(normalSpeed);

    }
    public void moveForward(){
        motor1.setPower(normalSpeed);
        motor2.setPower(normalSpeed);
        motor3.setPower(normalSpeed);
        motor4.setPower(normalSpeed);
    }
}
