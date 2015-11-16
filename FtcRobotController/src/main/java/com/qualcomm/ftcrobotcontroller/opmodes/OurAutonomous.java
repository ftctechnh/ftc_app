package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.MC;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class OurAutonomous extends OpMode{


    public OurAutonomous(){

    }

    DcMotor armLowerMotor;
    DcMotor armUpperMotor;
    DcMotor rightMotor;
    DcMotor leftMotor;

    public void init(){
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        leftMotor = hardwareMap.dcMotor.get("motor_left");
    }

    public void start(){
    //public static light(){

            // go straight some amount
            MC.moveForward(100, 34, rightMotor, leftMotor);
            //open claw...

            //**claw is to remain open for autonomous

            //turn left/right goes to button (depending on position)
            MC.turnRight(100, 34, rightMotor, leftMotor);
            //extend arm and press light button

    //}
            //public static climb(){

            //back up/ turn right/ left(depending on position)
            turnandbackup.BackTurn(100,34, rightMotor, leftMotor);
            //go forward to press button to release climbers
            MC.moveForward(100, 34, rightMotor, leftMotor);
            //extend arm to press button to release climbers

            //back up and turn (to reposition) to go straight onto mountain
            //turnandbackup.BackTurn(100,34, rightMotor, leftMotor);
            //with all force climb to highest point on mountain
            MC.moveForward(100, 34, rightMotor, leftMotor);
            //}

            MC.moveForward(100, 34, rightMotor, leftMotor);
    }

    public void loop(){

    }

    public void stop(){

    }

}
