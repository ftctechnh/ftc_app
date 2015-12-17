package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class Intake {
    public DcMotor motor;

    double inwardSpeed = 1;
    double outwardSpeed = -1;

    public Intake(){
    }

    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("intakeMotor");
        motor.setDirection(DcMotor.Direction.REVERSE);
    }

    public void inward(){
        motor.setPower(inwardSpeed);
    }

    public void outward(){
        motor.setPower(outwardSpeed);
    }

    public void stop() { motor.setPower(0);}

}