package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.walnuthillseagles.walnutlibrary.DistanceDrive;
import com.walnuthillseagles.walnutlibrary.DistanceMotor;
import com.walnuthillseagles.walnutlibrary.LinearControlScheme;
import com.walnuthillseagles.walnutlibrary.TimedMotor;
import com.walnuthillseagles.walnutlibrary.WalnutServo;

/**
 * Created by Yan Vologzhanin on 1/23/2016.
 */
public class MasterLinear extends LinearOpMode {
    //Parameters for Instance
    private int turnorientation;
    private long delay;
    //@TODO Use this guy
    private int posNumber;
    //Important Constants
    public static final double MSECSTOSECS = 1000;
    //@param myDelay is in seconds
    public MasterLinear(int startingPos, double myDelay, String myTeam){
        posNumber = startingPos;
        delay = (long) (myDelay*MSECSTOSECS);
        String team = myTeam.toUpperCase();
        if(team.equals("RED"))
            turnorientation = -1;
        else if(team.equals("BLUE"))
            turnorientation = 1;
        else{
            throw(new IndexOutOfBoundsException("Invalid Team Name given for Auto Program"));
        }
	    runOpMode();
        
    }
    @Override
    public void runOpMode(){
        return;
    }
}
