package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by josesulaimanmanzur on 2/7/18.
 */

public class WestCoastRobot extends LinearOpMode{
    public DcMotor m1,m2,m3,m4,lift,grabber;
    public Servo grab1, grab2, drop;
    public ColorSensor cs;

    private int deployedPos = 200;
    private int placeBlockPos = -20;
    private double deployPower = 0.2;

    private double liftPower = 0.2;

    private double grab1Max = 0.28;
    private double grab2Min = 0.65;

    private double drop1Min = 0.5 ;

    private double interval = 0.01;


    public WestCoastRobot(String motor1, String motor2,String motor3,String motor4,String liftName,String grabberName,String grab1Name,String grab2Name, String dropName,String csName){
        this.m1 = hardwareMap.get(DcMotor.class,motor1);
        this.m2 = hardwareMap.get(DcMotor.class,motor2);
        this.m3 = hardwareMap.get(DcMotor.class,motor3);
        this.m4 = hardwareMap.get(DcMotor.class,motor4);

        this.lift = hardwareMap.get(DcMotor.class,liftName);
        this.grabber = hardwareMap.get(DcMotor.class,grabberName);

        this.grab1 = hardwareMap.get(Servo.class,grab1Name);
        this.grab2 = hardwareMap.get(Servo.class,grab2Name);
        this.drop = hardwareMap.get(Servo.class,dropName);

        this.cs = hardwareMap.get(ColorSensor.class,csName);

    }

    public void setMotorPower(boolean samePowers,double ... powers){
        try{
            if (samePowers){
                m1.setPower(powers[0]);
                m2.setPower(powers[0]);
                m3.setPower(powers[0]);
                m4.setPower(powers[0]);
            }
            else{
                m1.setPower(powers[0]);
                m2.setPower(powers[1]);
                m3.setPower(powers[2]);
                m4.setPower(powers[3]);
            }

        }catch (IndexOutOfBoundsException exception) {
            System.out.println("Add more goddamn motors cuh");
        }

    }

    public void setUpEncoders(){
        this.m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.grabber.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void goToPosition(int encoderPos1,int encoderPos2, double power){
        this.m1.setTargetPosition(encoderPos1);
        this.m2.setTargetPosition(encoderPos2);

        this.m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double p1 = (encoderPos1 > 0)? power:-power;
        double p2 = (encoderPos2 > 0)? power:-power;

        while (m1.isBusy() || m2.isBusy()){
            setMotorPower(false,p1,p2,p1,p2);
        }

        m1.setPower(0);
        m2.setPower(0);

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void rotate(boolean right,int rotatePos,double power){
        if (right){
            goToPosition(rotatePos,-rotatePos,power);
        }
        else{
            goToPosition(-rotatePos,rotatePos,power);
        }
    }

    public void liftToPosition(int liftingPos){
        lift.setTargetPosition(liftingPos);
        if (liftingPos > lift.getCurrentPosition()){
            double power = liftPower;
        }
        else{
            double power = -liftPower;
        }

        while (lift.isBusy()){
            lift.setPower(liftPower);
        }
        lift.setPower(0);
    }

    public void collectBlock(boolean deploy){
        double power;
        if (deploy){
            power = deployPower;
            grabber.setTargetPosition(deployedPos);
        }
        else{
            power = -deployPower;
            grabber.setTargetPosition(placeBlockPos);
        }

        while (grabber.isBusy()){
            grabber.setPower(power);
        }

        grabber.setPower(0);


    }

    public void openServos(boolean open){
        if (open){
            double current1 = grab1Max;
            double current2 = grab2Min;
            while ((current1 > interval) || (current2 < 1)){
                current1 = (current1 > 0)? current1 - interval:current1;
                current2 = (current2 < 1)? current2 + interval:current2;
                grab1.setPosition(current1);
                grab2.setPosition(current2);
            }
        }
        else{
            double current1 = interval;
            double current2 = 1;

            while ((current1 < grab1Max) || (current2 > grab2Min)){
                current1 = (current1 < grab1Max)? current1 + interval:current1;
                current2 = (current2 > grab2Min)? current2 - interval:current2;
                grab1.setPosition(current1);
                grab2.setPosition(current2);
            }
        }
    }

    public void dropBlock(boolean dropBlock){
        if (dropBlock){
            double current1 = 1;
            while (current1 > drop1Min){
                current1 = (current1 > drop1Min)? current1 - interval:current1;
                drop.setPosition(current1);
            }
        }
        else{
            double current1 = drop1Min;
            while ((current1 < 1)){
                current1 = (current1 < 1)? current1 + interval:current1;
                drop.setPosition(current1);

            }
        }
    }

    @Override
    public void runOpMode(){

    }



}
