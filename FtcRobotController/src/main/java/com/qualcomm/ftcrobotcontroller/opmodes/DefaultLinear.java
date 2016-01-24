package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Yan Vologzhanin on 1/23/2016.
 */
public class DefaultLinear extends LinearOpMode {
    public void runOpMode(){
        try{
            MasterLinear runner = new MasterLinear(0,0,"BLUE");
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
