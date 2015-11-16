package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * HI.
 */
public class MC {
    final static int DEFAULT_SPEED = 50;

    public static void moveForward(int distance, int speed, DcMotor rightMotor, DcMotor leftMotor){
        //move for distance at speed using motors right and left
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();
        rightMotor.setTargetPosition(distance + rightPos);
        leftMotor.setTargetPosition(distance + leftPos);
        while(rightMotor.getTargetPosition() > rightMotor.getCurrentPosition()){
            rightMotor.setPower(speed);
            leftMotor.setPower(speed);
        }
    }

    public static void moveBackward(int distance, int speed, DcMotor rightMotor, DcMotor leftMotor){
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

    public static void turnRight(int distance, int speed, DcMotor rightMotor, DcMotor leftMotor){
        //turn right for distance at speed using motors right and left
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();
        rightMotor.setTargetPosition(-distance + rightPos);
        leftMotor.setTargetPosition(distance + leftPos);
        while(leftMotor.getTargetPosition() > leftMotor.getCurrentPosition()){
            rightMotor.setPower(-speed);
            leftMotor.setPower(speed);
        }
    }

    public static void turnLeft(int distance, int speed, DcMotor rightMotor, DcMotor leftMotor){
        //turn left for distance at speed using motors right and left
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();
        rightMotor.setTargetPosition(distance + rightPos);
        leftMotor.setTargetPosition(-distance + leftPos);
        while(leftMotor.getTargetPosition() < leftMotor.getCurrentPosition()){
            rightMotor.setPower(speed);
            leftMotor.setPower(-speed);
        }
    }

    public static void moveForward(int distance, DcMotor rightMotor, DcMotor leftMotor){
        moveForward(distance, DEFAULT_SPEED, rightMotor, leftMotor);
    }
    public static void moveBackward(int distance, DcMotor rightMotor, DcMotor leftMotor){
        moveBackward(distance, DEFAULT_SPEED, rightMotor, leftMotor);
    }
    public static void turnRight(int distance, DcMotor rightMotor, DcMotor leftMotor){
        turnRight(distance, DEFAULT_SPEED, rightMotor, leftMotor);
    }
    public static void turnLeft(int distance, DcMotor rightMotor, DcMotor leftMotor){
        turnLeft(distance, DEFAULT_SPEED, rightMotor, leftMotor);
    }
}