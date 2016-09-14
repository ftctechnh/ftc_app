package com.qualcomm.ftcrobotcontroller.opmodes.smidautils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Nathan.Smith.19 on 9/14/2016.
 */
public class OmniMotor {
    public enum Axis{
        X,Y
    }

    private Axis AXIS;
    public final String NAME;
    private DcMotor MOTOR;

    public OmniMotor(Axis AXIS, String NAME, OpMode ROBOT){
        this.AXIS = AXIS;
        this.NAME = NAME;
        this.MOTOR = ROBOT.hardwareMap.dcMotor.get(NAME);
    }

    public DcMotor getMotor(){
        return this.MOTOR;
    }

    public Axis getAxis(){
        return this.AXIS;
    }

    public void changeAxis(Axis NEW_AXIS){
        this.AXIS = NEW_AXIS;
    }


}
