package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class Intake {
    public DcMotor motor;

    double forwardSpeed;
    double backwardSpeed;

    public Intake(){
    }

    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("intakeMotor");
    }

    public void forward(){
        motor.setPower(forwardSpeed);
    }

    public void backward(){
        motor.setPower(backwardSpeed);
    }

}