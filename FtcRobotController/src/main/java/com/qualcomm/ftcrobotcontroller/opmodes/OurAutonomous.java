package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.MC;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by patrick.brady on 11/12/2015.
 */



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.ftcrobotcontroller.opmodes.MC;


public class OurAutonomous {


    public OurAutonomous(){

    }

    DcMotor armLowerMotor;
    DcMotor armUpperMotor;
    DcMotor rightMotor;
    DcMotor leftMotor;

    public void init(){
        DcMotor armLowerMotor;
        DcMotor armUpperMotor;
        DcMotor rightMotor = hardwareMap.dcMotor.get("motor_right");
        DcMotor leftMotor = hardwareMap.dcMotor.get("motor_left");
    }

    public void start(){

    }

    public void loop(){

    }

    public void stop(){

    }
    //public static light(){

    // go straight some amount
    MC.moveForward(100, 34);
    //open claw...

    //**claw is to remain open for autonomous

    //turn left/right goes to button (depending on position)
    MC.turnRight(100, 34);
    //extend arm and press light button

//}
    //public static climb(){

    //back up/ turn right/ left(depending on position)
   turnandbackup.BackTurn(100,34);
    //go forward to press button to release climbers
    MC.moveForward(100, 34);
    //extend arm to press button to release climbers

    //back up and turn (to reposition) to go straight onto mountain
    turnandbackup.BackTurn(100,34);
    //with all force climb to highest point on mountain
    MC.moveForward(100, 34);
    //}

    MC.moveForward(100, 34, rightMotor, leftMotor);
}
