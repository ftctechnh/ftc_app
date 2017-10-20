package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/6/2017.
 */

public class GlyphLift9330 {

    private Hardware9330 hwMap;

    public GlyphLift9330(Hardware9330 robotMap){
        hwMap = robotMap;
    }

    public void liftUp(){
        hwMap.liftMotor.setPower(50);
        //Maybe a second one is required?
    }

    public void liftDown(){
        hwMap.liftMotor.setPower(-50);
    }
}
