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

    public PengwinArm(HardwareMap hardwareMap){
        //get motors
        upMotor = hardwareMap.dcMotor.get("m0"); //left back
        acrossMotor = hardwareMap.dcMotor.get("m1"); //right back
    }
    public void setAcrossPower(double power){
        acrossPower = power;
    }
    public void setUpPower(double power){upPower = power;}

}
