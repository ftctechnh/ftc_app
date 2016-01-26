package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.walnuthillseagles.walnutlibrary.*;
import com.walnuthillseagles.walnutlibrary.DistanceDrive;
import com.walnuthillseagles.walnutlibrary.DistanceMotor;
import com.walnuthillseagles.walnutlibrary.LinearControlScheme;
import com.walnuthillseagles.walnutlibrary.TimedMotor;
import com.walnuthillseagles.walnutlibrary.WalnutServo;

public class MasterTeleOp extends OpMode{
    //Hardware
    //Drive Stuff
    private DcMotor right;
    private DcMotor left;
    //Other
    private DcMotor spinMotor;
    //@NOTE: Motor is Y Split
    private DcMotor slideMotors;
    
    private Servo beltServo;
    private Servo doorServo;
    private Servo hookServo;
    
    //Assignment
    private IncMotor rightDrive;
    private IncMotor leftDrive;

    private IncMotor slider;
    private DigMotor spinner;
    
    private WalnutServo belt;
    private WalnutServo door;
    private WalnutServo hook;
    //Control Scheme
    ControlScheme buttons;
    public void init(){
	//Init Hardware
	right = hardwareMap.dcMotor.get("motorRight");
	left = hardwareMap.dcMotor.get("motorLeft");
	slideMotors = hardwareMap.dcMotor.get("slideLeft");
	spinMotor = hardwareMap.dcMotor.get("spinners");
	
	beltServo = hardwareMap.servo.get("belt");
	doorServo = hardwareMap.servo.get("doors");
	//Create Assignment
	//Drive
	rightDrive = 
	    new IncMotor(right,"Right Drive",true,"RIGHTY1",false,0.05);
	leftDrive =
	    new IncMotor(left, "Left Drive", true, "LEFTY1", true, 0.05);
	//Spinners
	spinners = 
	    new DigMotor(spinMotor, "Spinners", false, "B2", true, 0);
	spinners.addButton("A2", true, -1);
	spinners.addButton("X2", true, 1);
	//Other
	slider = 
	    new IncMotor(slideMotors, "Sliders", false, "LEFTY2", false, 0.25);
	//@TODO Figure out how Servos want to be used
	belt = new WalnutServo(beltServo, 0.5, "LEFT2", 1, false);
	belt.addButton(RIGHT2, -1, false);
	
	door = new WalnutServo(doorServo, 1,"LBUMP2", 1, true);
	door.addButton("RBUMP2", -1, true);
	//Add all items to control scheme
	buttons = new ControlScheme();
	buttons.add(leftDrive);
	buttons.add(rightDrive);
	//Uncomment these lines when all objects made 
	
	buttons.add(slider);
	buttons.add(spinner);
	
	buttons.add(belt);
	buttons.add(door);
	//buttons.add(hook);
    }
    public void start(){
	WalnutMotor.GamepadUpdater.startProcessing(this);
    }
    public void loop(){
	buttons.operate();
    }
    public void stop(){
	buttons.stop();
    }
}
