package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by thund on 10/19/2017.
 */

public class PengwinArm {

    DcMotor upMotor; //motor that goes vertically
    DcMotor acrossMotor; //motor that goes horazontaily
    double acrossPower;
    double upPower;
    int upPosition; //Set to the encoder value that is the up position

    public PengwinArm(HardwareMap hardwareMap){
        //get motors
        upMotor = hardwareMap.dcMotor.get("arm"); //left back
        acrossMotor = hardwareMap.dcMotor.get("extend"); //right back
    }
    public void setAcrossPower(double power){
        acrossPower = power;
        acrossMotor.setPower(acrossPower);
    }
    public void setUpPower(double power){
        upPower = power;
        upMotor.setPower(upPower);
    }
    public double getAcrossPower(){return acrossPower;}
    public double getUpPower(){return upPower;}

    public void setHome(){
        upMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double getCalibrate(){
       return upMotor.getCurrentPosition();
    }
    public void goeyHomey(){
        upMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        upMotor.setTargetPosition(0);
        upMotor.setPower(.4);
    }
    //
    public void goUp(){
        upMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        upMotor.setTargetPosition(upPosition);
        upMotor.setPower(.4);
    }
    //
    //
}
/*
okay
 */