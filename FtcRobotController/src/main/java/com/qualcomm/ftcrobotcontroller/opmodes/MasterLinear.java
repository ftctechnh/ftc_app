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
    private int posNumber;
    //Important Constants
    public static final double MSECSTOSECS = 1000;
    //@param myDelay is in seconds
    public void initializeInstance(int startingPos, double myDelay, String myTeam){
        posNumber = startingPos;
        delay = (long) (myDelay*MSECSTOSECS);
        String team = myTeam.toUpperCase();
        if(team.equals("RED"))
            turnorientation = -1;
        else if(team.equals("BLUE"))
            turnorientation = 1;
        else{
            telemetry.addData("ERROR:","Invalid team given");
            throw(new IndexOutOfBoundsException());
        }
        runOpMode();
    }
    public void runOpMode(){
        //initialize Hardware Phase
        //Drive
        DcMotor leftDrive = hardwareMap.dcMotor.get("leftMotor");
        DcMotor rightDrive = hardwareMap.dcMotor.get("rightMotor");
        //Other
        DcMotor slides = hardwareMap.dcMotor.get("slider");
        WalnutServo climberBelt = new WalnutServo(hardwareMap.servo.get("belt"),0.5);
        WalnutServo hook = new WalnutServo(hardwareMap.servo.get("hook"),0);
        //initialize assignment phase
        DistanceDrive walnutDrive = new DistanceDrive(
                new DistanceMotor(leftDrive,"Left",true, false,4,1,1440),
                new DistanceMotor(rightDrive,"Right",true,true,4,1,1440),
                18);
        TimedMotor slider = new TimedMotor(slides,"slides",false,false);
        //Push to arraylist
        LinearControlScheme items = new LinearControlScheme();
        items.add(walnutDrive);
        items.add(climberBelt);
        items.add(hook);
        items.add(slider);
        //Wait for Start
        try{
            waitForStart();
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //Linear OpMode GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        try{
            sleep(delay);
            walnutDrive.linearDrive(-3.024,1);
            walnutDrive.waitForCompletion();
            sleep(500);
            slider.operate(3.25,1);
            slider.waitForCompletion();
            climberBelt.getServo().setPosition(0);
        }
        catch(InterruptedException e)
        {
            items.stop();
            Thread.currentThread().interrupt();
        }

    }
}
