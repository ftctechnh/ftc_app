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
	try{
	    runOpMode();
	}
	catch(InterruptedException e){
	    Thread.currentThread().interrupt();
	}
        
    }
    @Override
    public void runOpMode(){
        //initialize Hardware Phase
        //Drive
        DcMotor leftMotor = hardwareMap.dcMotor.get("leftMotor");
        DcMotor rightMotor = hardwareMap.dcMotor.get("rightMotor");
        //Other
        DcMotor slideMotors = hardwareMap.dcMotor.get("slider");
        WalnutServo beltServo = new WalnutServo(hardwareMap.servo.get("belt"),0.5);
        WalnutServo hookServo = new WalnutServo(hardwareMap.servo.get("hook"),0);
        //initialize assignment phase
        DistanceMotor leftDrive = new DistanceMotor(leftMotor,"Left",true, false,4,1,1440);
        DistanceMotor rightDrive = new DistanceMotor(rightMotor,"Right",true,true,4,1,1440);

        DistanceDrive walnutDrive = new DistanceDrive(leftDrive, rightDrive,18);
        TimedMotor slider = new TimedMotor(slideMotors,"slides",false,false);
        //Push to arraylist
        LinearControlScheme items = new LinearControlScheme();
        items.add(leftDrive);
        items.add(rightDrive);
        items.add(beltServo);
        items.add(hookServo);
        items.add(slider);
        //Wait for Start
        try{
            waitForStart();
        }
        catch (InterruptedException e){
	    items.stop();
            Thread.currentThread().interrupt();
        }
        //Linear OpMode GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try{
            //Implement Delay
            sleep(delay);
            //@TODO Modify Distances
            //@TODO Improve Readability
            //Drive out
            walnutDrive.linearDrive(-115, 1);
            walnutDrive.waitForCompletion();
            sleep(500);
            slider.operate(3.25, 1);
            slider.waitForCompletion();
            //Deposit Climber
            beltServo.operate(0);
            sleep(1500);
            beltServo.operate(0.5);
            //Drive some more???
            walnutDrive.linearDrive(40, 1);
            walnutDrive.waitForCompletion();
            walnutDrive.tankTurn(turnorientation*90,1);
            walnutDrive.waitForCompletion();
            //Reverse to turn and climb mountain
            walnutDrive.linearDrive(-100, 1);
            walnutDrive.waitForCompletion();
            walnutDrive.tankTurn(turnorientation*-90,1);
            walnutDrive.waitForCompletion();
            walnutDrive.linearDrive(100,1);
            hookServo.operate(1);
            walnutDrive.waitForCompletion();
	    items.stop();
        }
        catch(InterruptedException e)
        {
            items.stop();
            Thread.currentThread().interrupt();
        }

    }
}
