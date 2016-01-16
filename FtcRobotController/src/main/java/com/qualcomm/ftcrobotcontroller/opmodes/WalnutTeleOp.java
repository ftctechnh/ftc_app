package com.qualcomm.ftcrobotcontroller.opmodes;

//FTC imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

//Walnut Imports
import com.walnutHillsEagles.WalnutLibrary.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class WalnutTeleOp extends OpMode{
    //Initilze new thread

    //Physical Hardware
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor intakeSpinners;
    private DcMotor spool;

    private Servo belt;
    private Servo doors;
    //Button Assignments
    private ArrayList<WalnutMotor> motors;
    private IncMotor leftDrive;
    private IncMotor rightDrive;
    private DigMotor intake;
    private IncMotor slides;
    //No Button assignments available for servos :(

    //FTC Methods
    public void init(){
        //Initilize Hardware
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        intakeSpinners = hardwareMap.dcMotor.get("spinners");
        spool = hardwareMap.dcMotor.get("slideLeft");
        //Assign Buttons
        leftDrive = new IncMotor(motorLeft,"Left SimpleDrive",true,"LEFTY1", false, 0.05);
        rightDrive = new IncMotor(motorRight,"Right SimpleDrive",true,"RIGHTY1", false, 0.05);
        slides = new IncMotor(spool, "Slider", false, "LEFTY2",false,0.25);

        intake = new DigMotor(intakeSpinners,"intake",false,
                "A2",-1,true);
        intake.addButton("B2", 0, true);
        //Add Buttons to arrayList and initilize
        motors = new ArrayList<WalnutMotor>();
        motors.add(leftDrive);
        motors.add(rightDrive);
        motors.add(slides);
        motors.add(intake);
        //Can't do nutin about dem servos :(
        belt = hardwareMap.servo.get("belt");
        doors = hardwareMap.servo.get("doors");
        belt.setPosition(0.5);
        doors.setPosition(0.5);
        //Initilize Stuff for Processing

    }
    public void start(){
        //Start Gamepad Processing
        WalnutMotor.GamepadUpdater.startProcessing(this);
    }
    public void loop(){
        //Process motors
        for(int i=0;i<motors.size();i++){
            motors.get(i).operate();
        }
        //Why Servos why???
        belt.setPosition(1 - (gamepad2.right_stick_x + 1.0) / 2.0);
        doors.setPosition((gamepad2.left_stick_x / 2.0) + 0.5);
        //motorLeft.setPower(WalnutMotor.GamepadUpdater.doubleValues[1]);
        //Used to compare actual and abstract values to see if value is updating fast enough
        telemetry.addData("Direct Gamepad", String.format("%d", motorRight.getCurrentPosition()));
    }
    public void stop(){
       for(int i = 0;i<motors.size();i++){
           motors.get(i).stop();
       }
        belt.setPosition(0.5);
        doors.setPosition(0.5);
        WalnutMotor.GamepadUpdater.stopProcessing();
    }
}
