package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by thund on 10/19/2017.
 */

public class PengwinArm {

    DcMotor upMotor; //motor that goes vertically
    DcMotor acrossMotor; //motor that goes horizontally
    DcMotor retractify;
    Servo leftGlyphy;
    Servo rightGlyphy;
    DigitalChannel stopify;
    //hmmm
    double acrossPower;
    double upPower;
    double resetify = -1;
    static double open = -1;
    static double rightOpen = open;
    static double closed = .8;
    static double rightClosed = closed;
    int upPosition; //Set to the encoder value that is the up position

    public PengwinArm(HardwareMap hardwareMap){
        //get motors
        upMotor = hardwareMap.dcMotor.get("arm"); //left back
        retractify = hardwareMap.dcMotor.get("retract");
        stopify = hardwareMap.digitalChannel.get("arwin");
        acrossMotor = hardwareMap.dcMotor.get("extend"); //right back
        leftGlyphy = hardwareMap.servo.get("glyphy");
        rightGlyphy = hardwareMap.servo.get("rightglyphy");
        leftGlyphy.setDirection(Servo.Direction.FORWARD);
        leftGlyphy.setDirection(Servo.Direction.REVERSE);
        retractify.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void setAcrossPower(double power){
      // if(!stopify.getState() || power > 0 && resetify == 1){
            acrossPower = power;
           if(power *resetify > 0){
               retractify.setPower(-.3);
           }
           else{
               retractify.setPower(acrossPower * resetify*.8);
           }
            acrossMotor.setPower(acrossPower*resetify);
      // }else if(power > 0 && resetify == -1){
         //  acrossMotor.setPower(-acrossPower);
      // }

    }
    public void setUpPower(double power){
        upPower = power;
        upMotor.setPower(upPower);
    }
    //
    public double getAcrossPower(){return acrossPower;}
    public double getUpPower(){return upPower;}


    public double getCalibrate(){
       return upMotor.getCurrentPosition();
    }
    //
    public void close(){
        leftGlyphy.setPosition(closed);
        rightGlyphy.setPosition(rightClosed);
    }
    public void open(){
        leftGlyphy.setPosition(open);
        rightGlyphy.setPosition(rightOpen);
    }
}
/*
okay
 */