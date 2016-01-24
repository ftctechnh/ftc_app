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
    public MasterLinear(int startingPos, double myDelay, String myTeam)throws InterruptedException{
        posNumber = startingPos;
        delay = (long) (myDelay*MSECSTOSECS);
        String team = myTeam.toUpperCase();
        if(team.equals("RED"))
            turnorientation = -1;
        else if(team.equals("BLUE"))
            turnorientation = 1;
        else{
            telemetry.addData("ERROR:","Invalid team given");
            Thread.sleep(5000);
            throw(new IndexOutOfBoundsException());
        }
        runOpMode();
    }
    @Override
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
            climberBelt.operate(0);
            sleep(1500);
            climberBelt.operate(0.5);
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
            hook.operate(1);
            walnutDrive.waitForCompletion();
        }
        catch(InterruptedException e)
        {
            items.stop();
            Thread.currentThread().interrupt();
        }

    }
}
