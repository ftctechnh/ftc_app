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
    //Hardware
    private DcMotor leftDriveMotor;
    private DcMotor rightDriveMotor;
    private DistanceMotor leftDrive;
    private DistanceMotor rightDrive;
    //Assignment


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
    public void initRobot(){
        leftDriveMotor = hardwareMap.dcMotor.get("motorLeft");
        rightDriveMotor = hardwareMap.dcMotor.get("motorRight");
        leftDrive = new DistanceMotor(leftDriveMotor, "Left",true,false,4,1,1440);
        rightDrive = new DistanceMotor(rightDriveMotor, "Right",true, true, 4,1,1440);
    }
    @Override
    public void runOpMode(){
        initRobot();
        try{
       	    telemetry.addData("Tests", "Waiting for start");
            waitForStart();
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        try{

            telemetry.addData("Tests", "Starting First Test");
            leftDrive.operate(20);
            leftDrive.waitForCompletion();
            telemetry.addData("Tests", "Finished First Test");
            sleep(2000);
            telemetry.addData("Tests", "Starting Second Test");
            leftDrive.operate(20);
            leftDrive.waitForCompletion();
            telemetry.addData("Tests", "Finished Second Test");
            sleep(2000);
            leftDrive.operate(-20);
            leftDrive.waitForCompletion();
            sleep(2000);
            telemetry.addData("Tests", "Complete");
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
}
