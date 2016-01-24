package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.walnuthillseagles.walnutlibrary.DistanceDrive;
import com.walnuthillseagles.walnutlibrary.DistanceMotor;
import com.walnuthillseagles.walnutlibrary.LinearControlScheme;
import com.walnuthillseagles.walnutlibrary.WalnutServo;

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
        //Push to arraylist
        LinearControlScheme items = new LinearControlScheme();
        items.add(walnutDrive);
        items.add(climberBelt);
        items.add(hook);
        //Wait for Start
        try{
            waitForStart();
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //Linear OpMode GOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        try{
            sleep(1);
        }
        catch(InterruptedException e)
        {
            items.stop();
            Thread.currentThread().interrupt();
        }

    }
}
