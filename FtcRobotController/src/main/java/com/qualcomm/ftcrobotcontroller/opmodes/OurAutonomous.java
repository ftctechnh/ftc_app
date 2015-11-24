package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.ftcrobotcontroller.opmodes.MC;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

public class OurAutonomous extends OpMode{
    public OurAutonomous(){
    }

    final static int DEFAULT_SPEED = 50;
    static DcMotor armLowerMotor;
    DcMotor armUpperMotor;
    boolean peopleAndLight = true;
    boolean climbing = false;
    static boolean isBlue = true;
    DcMotor rightMotor;
    DcMotor leftMotor;

    public void init(){
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
        moveForward(3000); //move in front of the light
        turnRight(200); //face the light approximately

        moveForward(400); //CAN USE LINE FOLLOWING TO CENTER ROBOT AND TOUCH TO DETECT WALL.
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
        moveBackward(400);//back up and turn (to reposition) to go straight onto mountain
        turnLeft(200);
        moveBackward(1500);
        turnRight(500);
        moveForward(1600, 100);
    }

    public void climbFromStart(){
        //the robot starts from the position on the wall and then goes to climb
        moveForward(1500);
        turnRight(500);
    }

    public void loop(){
    }

    public void stop(){
    }

    
    public void moveForward(int distance, int speed){
        //move for distance at speed using motors right and left
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();
        rightMotor.setTargetPosition(distance + rightPos);
        leftMotor.setTargetPosition(distance + leftPos);
        while(rightMotor.getTargetPosition() > rightMotor.getCurrentPosition()){
            rightMotor.setPower(speed);
            leftMotor.setPower(speed);
        }

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Motor target Pos", "Pos: " + rightMotor.getTargetPosition());
        telemetry.addData("Motor current Pos", "Pos: " + rightMotor.getCurrentPosition());
    }

    public void moveBackward(int distance, int speed){
        //move backward for distance at speed using motors right and left
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();
        rightMotor.setTargetPosition(-distance + rightPos);
        leftMotor.setTargetPosition(-distance + leftPos);
        while(rightMotor.getTargetPosition() < rightMotor.getCurrentPosition()){
            rightMotor.setPower(-speed);
            leftMotor.setPower(-speed);
        }
    }

    public void turnRight(int distance, int speed){
        if(isBlue) {
            //turn right for distance at speed using motors right and left
            int rightPos = rightMotor.getCurrentPosition();
            int leftPos = leftMotor.getCurrentPosition();
            rightMotor.setTargetPosition(-distance + rightPos);
            leftMotor.setTargetPosition(distance + leftPos);
            while (leftMotor.getTargetPosition() > leftMotor.getCurrentPosition()) {
                rightMotor.setPower(-speed);
                leftMotor.setPower(speed);
            }
        }else{
            turnLeft(distance, speed);
        }
    }

    public void turnLeft(int distance, int speed){
        if(isBlue) {
            //turn left for distance at speed using motors right and left
            int rightPos = rightMotor.getCurrentPosition();
            int leftPos = leftMotor.getCurrentPosition();
            rightMotor.setTargetPosition(distance + rightPos);
            leftMotor.setTargetPosition(-distance + leftPos);
            while (leftMotor.getTargetPosition() < leftMotor.getCurrentPosition()) {
                rightMotor.setPower(speed);
                leftMotor.setPower(-speed);
            }
        }else{
            turnRight(distance, speed);
        }
    }

    public void moveForward(int distance){
        moveForward(distance, DEFAULT_SPEED);
    }
    public void moveBackward(int distance){
        moveBackward(distance, DEFAULT_SPEED);
    }
    public void turnRight(int distance){
        turnRight(distance, DEFAULT_SPEED);
    }
    public void turnLeft(int distance){
        turnLeft(distance, DEFAULT_SPEED);
    }
}