package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by thund on 10/19/2017.
 */

public class JeffThePengwin {


    //power variables
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;
    double powerInput = 0;
    double degreeOfPower = 1;
    double totalPower = powerInput * degreeOfPower;

    public void setDegreeOfPower(double degreeOfPower){
        degreeOfPower = degreeOfPower;
    }

    public void setPowerInput(double powerInput){
        powerInput = powerInput;
    }


    public JeffThePengwin(HardwareMap hardwareMap){
        //get motors
        leftBackMotor = hardwareMap.dcMotor.get("m0"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("m1"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("m2"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("m3"); //right front

    }

    public void move(double drive, double turn, double strafe){
        boolean turningRight = turn < 0; //TODO This is not working right, it should be > but it is mixing up and left and righ
        boolean notTurning = turn == 0;
        boolean movingVertical = Math.abs(drive) > Math.abs(strafe);
        boolean strafingRight = strafe > 0;

        if (notTurning) {
            //no movement in right joystick
            //start of driving section
            if (movingVertical) { //forward/back or left/right?
                if (drive > 0) { //forward
                    driveForward();
                } else { //back
                    driveBackward();
                }
            } else {
                if (strafingRight) { //right
                    strafeRight();
                } else { //left
                    strafeLeft();
                }
            }
        } else if (turningRight) {
            //pushing right joystick to the right
            //turn right by left wheels going forward and right going backwards
            turnRight();
        } else {
            //turn left
            turnLeft();
        }
    }


    public void turnRight(){
        leftBackMotor.setPower(totalPower);
        leftFrontMotor.setPower(totalPower);
        rightBackMotor.setPower(-totalPower);
        rightFrontMotor.setPower(-totalPower);
    }

    public void turnLeft(){
        leftBackMotor.setPower(-totalPower);
        leftFrontMotor.setPower(-totalPower);
        rightBackMotor.setPower(totalPower);
        rightFrontMotor.setPower(totalPower);
    }

    public void driveForward(){
        leftBackMotor.setPower(totalPower);
        leftFrontMotor.setPower(totalPower);
        rightBackMotor.setPower(totalPower);
        rightFrontMotor.setPower(totalPower);
    }

    public void driveBackward(){
        leftBackMotor.setPower(-totalPower);
        leftFrontMotor.setPower(-totalPower);
        rightBackMotor.setPower(-totalPower);
        rightFrontMotor.setPower(-totalPower);
    }

    public void strafeLeft(){
        leftBackMotor.setPower(-totalPower);
        leftFrontMotor.setPower(totalPower);
        rightBackMotor.setPower(totalPower);
        rightFrontMotor.setPower(-totalPower);
    }

    public void strafeRight(){
        leftBackMotor.setPower(totalPower);
        leftFrontMotor.setPower(-totalPower);
        rightBackMotor.setPower(-totalPower);
        rightFrontMotor.setPower(totalPower);
    }

}
