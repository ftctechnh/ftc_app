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
    boolean isBlue = true;



    public void init(){
        if(isBlue){
            MC.isBlue = true;
        }
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        leftMotor = hardwareMap.dcMotor.get("motor_left");
    }

    public void start(){
        if(peopleAndLight){
            peopleAndLight();
            if(climbing) {
                climbFromBasket();
            }
        }else if(climbing){
            climbFromStart();
        }
    }

    public void peopleAndLight(){
        //dumpAndLight
        MC.moveForward(3000, rightMotor, leftMotor); //move in front of the light
        MC.turnRight(200, rightMotor, leftMotor); //face the light approximately
        MC.moveForward(400, rightMotor, leftMotor); //CAN USE LINE FOLLOWING TO CENTER ROBOT AND TOUCH TO DETECT WALL.
        dumpClimbers(); //method to use the arm to dump the climbers into the bin that should be in front of the robot
        detectAndPushLight();//method to use the light sensor to see which button to push
        //make sure to have the methods return the robot to this position
    }

    public void dumpClimbers(){
        //starting from the point where line following stopped, the robot will use its arm to reach up and dump the people
        //lower motor up so that it will dump the cimers from above the claw
    }

    public void detectAndPushLight(){
        //after dumping (or trying to dump) the people, this is the method for using the light sensor to pursh the right button
    }

    public void climbFromBasket(){
        //the robot starts from the location that is at the end of the light and basket methods
        MC.moveBackward(400, rightMotor, leftMotor);//back up and turn (to reposition) to go straight onto mountain
        MC.turnLeft(200, rightMotor, leftMotor);
        MC.moveBackward(1500, rightMotor, leftMotor);
        MC.turnRight(500, rightMotor, leftMotor);
        MC.moveForward(1600, 100, rightMotor, leftMotor);
    }

    public void climbFromStart(){
        //the robot starts from the position on the wall and then goes to climb
        MC.moveForward(1500, rightMotor, leftMotor);
        MC.turnRight(500, rightMotor, leftMotor);
    }

    public void loop(){
    }

    public void stop(){
    }
}
