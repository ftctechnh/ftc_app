package com.qualcomm.ftcrobotcontroller.opmodes;

//FTC imports
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

//Walnut Imports
import com.qualcomm.ftcrobotcontroller.walnutLibrary.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public class walnutTeleOp extends OpMode{
    //Initilze new thread
    private static walnutMotor.GamepadUpdater updater = new walnutMotor.GamepadUpdater();
    private static Thread processor = new Thread(updater);

    //Physical Hardware
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor intakeSpinners;
    private DcMotor spool;

    private Servo belt;
    private Servo doors;
    //Button Assignments
    private ArrayList<walnutMotor> motors;
    private IncSpinner leftDrive;
    private IncSpinner rightDrive;
    private DigSpinner intake;
    private IncSpinner slides;
    //No Button assignments available for servos :(

    //FTC Methods
    public void init(){
        //Initilize Hardware
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("slideRight");
        intakeSpinners = hardwareMap.dcMotor.get("spinners");
        spool = hardwareMap.dcMotor.get("slideLeft");
        //Assign Buttons
        leftDrive = new IncSpinner(motorLeft,"Left Drive",true,
                IncSpinner.analogValues.valueOf("LEFTY1"), true, 0.05);
        rightDrive = new IncSpinner(motorRight,"Right Drive",true,
                IncSpinner.analogValues.valueOf("RIGHTY1"), false, 0.05);
        slides = new IncSpinner(spool, "Slider", false,
                IncSpinner.analogValues.valueOf("LEFTY2"),false,0.25);

        intake = new DigSpinner(intakeSpinners,"intake",false,
                "A2",-1,true);
        intake.addButton("B2",0,true);

        //Add Buttons to arrayList
        motors.add(leftDrive);
        motors.add(rightDrive);
        motors.add(slides);
        motors.add(intake);
        //Can't do nutin about dem servos :(
        belt = hardwareMap.servo.get("belt");
        doors = hardwareMap.servo.get("doors");
        belt.setPosition(0.5);
        doors.setPosition(0.5);
    }
    public void start(){
        //Start Gamepad Processing
        updater.setGamepads(gamepad1, gamepad2);
        processor.start();
    }
    public void loop(){
        //Process motors
        for(int i=0;i<motors.size();i++){
            motors.get(i).operate();
        }
        //Why Servos why???
        belt.setPosition(1-(gamepad2.right_stick_x+1.0)/2.0);
        doors.setPosition((gamepad2.left_stick_x/2.0)+0.5);
    }
    public void stop(){
        //Stop motors
        for(int i=0;i<motors.size();i++){
            motors.get(i).stopMotor();
        }
        //Reset Servos
        belt.setPosition(0.5);
        doors.setPosition(0.5);
    }
}
