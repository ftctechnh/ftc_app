package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by thund on 10/19/2017.
 */

public class PengwinArm {

    DcMotor upMotor; //motor that goes vertically
    DcMotor acrossMotor; //motor that goes horizontally
    Servo leftGlyphy;
    Servo rightGlyphy;
    //hmmm
    double acrossPower;
    double upPower;
    double resetify = -1;
    static double open = .8;
    static double rightOpen = .8;
    static double closed = -.8;
    static double rightClosed = -.8;
    int upPosition; //Set to the encoder value that is the up position

    public PengwinArm(HardwareMap hardwareMap){
        //get motors
        upMotor = hardwareMap.dcMotor.get("arm"); //left back

        acrossMotor = hardwareMap.dcMotor.get("extend"); //right back
        leftGlyphy = hardwareMap.servo.get("glyphy");
        rightGlyphy = hardwareMap.servo.get("rightglyphy");
        leftGlyphy.setDirection(Servo.Direction.FORWARD);
        leftGlyphy.setDirection(Servo.Direction.REVERSE);
    }
    public void setAcrossPower(double power){
        acrossPower = power;
        acrossMotor.setPower(acrossPower * resetify);
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