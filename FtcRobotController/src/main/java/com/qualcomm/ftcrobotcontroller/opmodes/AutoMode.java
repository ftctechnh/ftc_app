package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by DKrakauer on 2/8/16.
 */
public class AutoMode extends LinearOpMode{

    final static double ARM_MIN_RANGE  = 0;
    final static double ARM_MAX_RANGE  = 1;

    static Boolean servo_right_on = false;
    static Boolean servo_left_on;

    double armDelta = 0.05;

    // MOTOR VALUES
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    DcMotor motorRightFront;
    DcMotor motorLeftFront;
    DcMotor sucker;
    DcMotor linear;
    //Arm Servos
    Servo servo_right, servo_left;
    //Tube Servos
    Servo servo_tube,  servo_free;
    private int rarmon = 0;
    private int larmon = 0;

    double rightArmPosition, leftArmPosition;
    double tubeArmPosition, freeArmPositon;


    // CONSTRUCTOR and Electronics Diagram
    public AutoMode() {

    }

    @Override
    public void runOpMode() throws InterruptedException{

        //Motors
        motorLeftFront = hardwareMap.dcMotor.get("motor_LF");
        motorLeftBack = hardwareMap.dcMotor.get("motor_LB");

        motorRightFront = hardwareMap.dcMotor.get("motor_RF");
        motorRightBack = hardwareMap.dcMotor.get("motor_RB");

        motorLeftBack.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorRightBack.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorLeftFront.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorRightFront.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        //Sucker
        sucker = hardwareMap.dcMotor.get("sucker");
        //Linear Slides
        linear = hardwareMap.dcMotor.get("linear");

        //Servo arms
        servo_right = hardwareMap.servo.get("servo_1");
        servo_left = hardwareMap.servo.get("servo_2");

        //Tube servos
        servo_tube = hardwareMap.servo.get("servo_3");
        servo_free = hardwareMap.servo.get("servo_4");

        rightArmPosition = 0.93;
        leftArmPosition = 0.15;

        closeHatch();
    }



    // ADDITIONAL METHODS

    public void moveForward(int Time) throws InterruptedException {
        motorLeftBack.setPower(-1);
        motorLeftFront.setPower(1);
        motorRightFront.setPower(1);
        motorRightBack.setPower(1);
        Thread.sleep(Time);
        motorLeftBack.setPower(0);
        motorLeftFront.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);

    }

    public void moveBackwards(int Time) throws InterruptedException {
        motorLeftBack.setPower(1);
        motorLeftFront.setPower(-1);
        motorRightFront.setPower(-1);
        motorRightBack.setPower(-1);
        Thread.sleep(Time);
        motorLeftBack.setPower(0);
        motorLeftFront.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);

    }

    public void turnRight(int Time) throws InterruptedException {
        motorLeftBack.setPower(1);
        motorLeftFront.setPower(-1);
        motorRightFront.setPower(1);
        motorRightBack.setPower(1);
        Thread.sleep(Time);
        motorLeftBack.setPower(0);
        motorLeftFront.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);
    }

    public void turnLeft(int Time) throws InterruptedException {
        motorLeftBack.setPower(-1);
        motorLeftFront.setPower(1);
        motorRightFront.setPower(-1);
        motorRightBack.setPower(-1);
        Thread.sleep(Time);
        motorLeftBack.setPower(0);
        motorLeftFront.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);
    }


    public void flipTube(){
        servo_tube.setPosition(1.0);
    }
    public void returnTube(){
        servo_tube.setPosition(0.40);
    }
    public void extendSlides(int Time) throws InterruptedException{
        linear.setPower(-1);
        Thread.sleep(Time);
        linear.setPower(0);
    }
    public void retractSlides(int Time) throws InterruptedException{
        linear.setPower(1);
        Thread.sleep(Time);
        linear.setPower(0);
    }
    public void openHatch(){
        servo_free.setPosition(0);
    }
    public void closeHatch(){
        servo_free.setPosition(1);
    }
    public void lowerRightArm(){
        servo_right.setPosition(.30);
    }
    public void raiseRightArm(){
        servo_right.setPosition(0.93);
    }
    public void lowerLeftArm(){
        servo_left.setPosition(0.70);
    }
    public void raiseLeftArm(){
        servo_left.setPosition(0.15);
    }

    public void emptyTube() throws InterruptedException{
        flipTube();
        openHatch();
        Thread.sleep(3000);
        returnTube();
        closeHatch();
    }
    public void startSpinner(){
        sucker.setPower(1);
    }
    public void stopSpinner(){
        sucker.setPower(0);
    }
    public void checkTube(){
        returnTube();
        closeHatch();
    }
    public void stopMoving(){
        motorLeftBack.setPower(0);
        motorLeftFront.setPower(0);
        motorRightFront.setPower(0);
        motorRightBack.setPower(0);
    }
    public void checkEverything(){
        stopMoving();
        stopSpinner();
        checkTube();
    }

    public void startChildMode(int m) throws InterruptedException{
        moveForward(20 * m);
        turnRight(5 * m);
        moveForward(1 * m);
        turnLeft(1 * m);
        moveForward(1 * m);
        moveBackwards(10 * m);
        turnRight(10 * m);
        moveBackwards(1 * m);
        turnLeft(25 * (m / 10));
        moveForward(5 * m);
        turnRight(5 * m);
        moveBackwards(1 * m);
        Thread.sleep(5 * m);
        moveBackwards(4 * m);
        turnLeft(25 * (m/10));
        moveBackwards(3 * m);
        turnRight(1 * m);
        moveBackwards(25 * (m/10));
        turnLeft(4 * m);
        moveBackwards(25 * (m/10));
        Thread.sleep(10 * m);
        turnRight(1 * m);
        moveForward(1 * m);
        turnLeft(5 * m);
        moveForward(1 * m);
        turnLeft(5 * m);
        moveBackwards(5 * m);
        turnLeft(3 * m);
        checkEverything();
    }

}
