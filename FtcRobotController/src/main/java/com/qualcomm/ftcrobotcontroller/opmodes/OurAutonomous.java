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
    boolean peopleAndLight = true;
    boolean climbing = true;

    public void init(){
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        leftMotor = hardwareMap.dcMotor.get("motor_left");
    }

    public void start(){
        if(peopleAndLight) {
            //dumpAndLight
            MC.moveForward(3000, rightMotor, leftMotor); //move in front of the light
            MC.turnRight(200, rightMotor, leftMotor); //face the light approximately
            MC.moveForward(400, rightMotor, leftMotor); //CAN USE LINE FOLLOWING TO CENTER ROBOT AND TOUCH TO DETECT WALL.
            dumpClimbers(); //method to use the arm to dump the climbers into the bin that should be in front of the robot
            detectAndPushLight();//method to use the light sensor to see which button to push
            //make sure to have the methods return the robot to this position
        }

        if(climbing && peopleAndLight){
            climbFromBasket();
        }
        else if(climbing){
            climbFromStart();
        }


        //turn left/right goes to button (depending on position)
        MC.turnRight(100, rightMotor, leftMotor);
        //extend arm and press light button

//}
        //public static climb(){

        //back up/ turn right/ left(depending on position)
        //turnandbackup.BackTurn(100,34, rightMotor, leftMotor);
        //go forward to press button to release climbers
        MC.moveForward(100, rightMotor, leftMotor);
        //extend arm to press button to release climbers

        //back up and turn (to reposition) to go straight onto mountain
        //turnandbackup.BackTurn(100,34, rightMotor, leftMotor);
        //with all force climb to highest point on mountain
        MC.moveForward(100, rightMotor, leftMotor);
        //}

        MC.moveForward(100, rightMotor, leftMotor);
    }

    public void dumpClimbers(){
        //starting from the point where line following stopped, the robot will use its arm to reach up and dump the people

    }

    public void detectAndPushLight(){
        //after dumping (or trying to dump) the people, this is the method for using the light sensor to pursh the right button
    }

    public void climbFromBasket(){
        //the robot starts from the location that is at the end of the light and basket methods
    }

    public void climbFromStart(){
        //the robot starts from the position on the wall
    }

    public void loop(){

    }

    public void stop(){

    }

}
