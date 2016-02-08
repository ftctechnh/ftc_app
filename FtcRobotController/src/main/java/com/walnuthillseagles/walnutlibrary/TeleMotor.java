package com.walnuthillseagles.walnutlibrary;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public abstract class TeleMotor extends SimpleMotor implements Drivable{
    //Field
    private DcMotor motor;
    //Used for telemetry
    private String name;
    private boolean hasEncoders;

    //Constructor
    TeleMotor(DcMotor myMotor, String myName, boolean encoderCheck){
        super(myMotor, myName,encoderCheck);
    }
    //Getters and Setters

    public abstract void operate();

}
