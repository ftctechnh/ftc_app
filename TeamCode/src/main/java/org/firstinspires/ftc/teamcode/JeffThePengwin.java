package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

    public void setDegreeOfPower(double degreeOfPower){
        degreeOfPower = degreeOfPower;
    }

    public void setPowerInput(double powerInput){
        powerInput = powerInput;
    }

    public double getPowerInput(){return powerInput;}
    public double getDegreeOfPower(){return degreeOfPower;}
    public double getTotalPower(){return powerInput*degreeOfPower;}

    public JeffThePengwin(HardwareMap hardwareMap){
        //get motors
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);

    }

    public void move(double drive, double turn, double strafe){
        boolean turningRight = turn < 0; //TODO This is not working right, it should be > but it is mixing up and left and righ
        boolean notTurning = turn == 0;
        boolean movingVertical = Math.abs(drive) > Math.abs(strafe);
        boolean strafingLefty = strafe > 0;

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
                if (strafingLefty) { //right
                    strafeLeft();
                } else { //left
                    strafeRight();
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
        leftBackMotor.setPower(powerInput*degreeOfPower);
        leftFrontMotor.setPower(powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }

    public void turnLeft(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(powerInput*degreeOfPower);
        rightFrontMotor.setPower(powerInput*degreeOfPower);
    }

    public void driveForward(){
        leftBackMotor.setPower(powerInput*degreeOfPower);
        leftFrontMotor.setPower(powerInput*degreeOfPower);
        rightBackMotor.setPower(powerInput*degreeOfPower);
        rightFrontMotor.setPower(powerInput*degreeOfPower);
    }

    public void driveBackward(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }

    public void strafeLeft(){
        leftBackMotor.setPower(-powerInput*degreeOfPower);
        leftFrontMotor.setPower(powerInput*degreeOfPower);
        rightBackMotor.setPower(powerInput*degreeOfPower);
        rightFrontMotor.setPower(-powerInput*degreeOfPower);
    }

    public void strafeRight(){
        leftBackMotor.setPower(powerInput*degreeOfPower);
        leftFrontMotor.setPower(-powerInput*degreeOfPower);
        rightBackMotor.setPower(-powerInput*degreeOfPower);
        rightFrontMotor.setPower(powerInput*degreeOfPower);
    }

}
