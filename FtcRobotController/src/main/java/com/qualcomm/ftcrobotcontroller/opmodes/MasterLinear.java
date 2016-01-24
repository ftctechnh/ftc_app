package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Yan Vologzhanin on 1/23/2016.
 */
public class MasterLinear extends LinearOpMode {
    //Parameters for Instance
    private String team;
    private double delay;
    private int posNumber;
    //Robot Hardware
    public void initializeInstance(int startingPos, double myDelay, String myTeam){
        posNumber = startingPos;
        delay = myDelay;
        String team = myTeam.toUpperCase();
        runOpMode();
    }
    public void runOpMode(){
        //initialize Hardware Phase
        //initialize assignment phase
        try{
            waitForStart();
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
}
