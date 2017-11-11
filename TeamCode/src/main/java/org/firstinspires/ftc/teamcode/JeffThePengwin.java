package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by thund on 10/19/2017.
 */

public class JeffThePengwin {
    //diameter stuff
    static final double WHEEL_DIAMETER_INCHEYS = 4.0;

    static final double countsPerRevolution = 1220 ;//TODO Add gear reduction if needed
    static final double Pi = 3.141592653589793238462643383279502;
    static final double countify = countsPerRevolution/(WHEEL_DIAMETER_INCHEYS*Pi);//Counts per inch

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
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front

        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    //christine added, explain!
    public boolean isMoving(){
        return leftBackMotor.isBusy()&& leftFrontMotor.isBusy() && rightBackMotor.isBusy() && rightFrontMotor.isBusy();
    }

    public void move(double drive, double turn, double strafe){
        boolean turningRight = turn < 0; //TODO This is not working right, it should be > but it is mixing up and left and right
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

    public void forwardToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        moveAllMotorsSameDirectionAndDistance(move);
        //
        switchify();//switch to rut to position
        //
        setPowerInput(speed);
        justDrive();
    }

    private void backToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        moveAllMotorsSameDirectionAndDistance(-move);
        driveBackward();
        //
        switchify();//switch to rut to position
        //
        setPowerInput(speed);
        justDrive();
        //
    }
    private void rightToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        turnRightACertainDistance(move);
        //
        switchify();//switch to rut to position
        //
        setPowerInput(speed);
        justDrive();
        //
    }
    //
    private void leftToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        turnLeftACertainDistance(move);
        switchify();//switch to rut to position
        //
        setPowerInput(speed);
        justDrive();
        //
    }



    public void setTargetPosition(int leftBackPosition, int leftFrontPosition, int rightBackPosition, int rightFrontPosition){
        leftBackMotor.setTargetPosition(leftBackPosition);
        leftFrontMotor.setTargetPosition(leftFrontPosition);
        rightBackMotor.setTargetPosition(rightBackPosition);
        rightFrontMotor.setTargetPosition(rightFrontPosition);
    }

    public void moveAllMotorsSameDirectionAndDistance(int move){
        int leftFrontEnd = (leftFrontMotor.getCurrentPosition() + move);
        int leftBackEnd = (leftFrontMotor.getCurrentPosition() + move);
        int rightFrontEnd = leftFrontMotor.getCurrentPosition() + move;
        int rightBackEnd = leftFrontMotor.getCurrentPosition() + move;
        setTargetPosition(leftBackEnd, leftFrontEnd, rightBackEnd, rightFrontEnd);
    }

    public void turnRightACertainDistance(int move){
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + -move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + -move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);

    }
    public void turnLeftACertainDistance(int move){
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + -move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + -  move);

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
        justDrive();
    }

    public void justDrive(){
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

    public void switchify(){
        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void startify(){
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //

    //
    public void switcheroo(){
        leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //
        leftBackMotor.setPower(0);
        leftFrontMotor.setPower(0);
        rightBackMotor.setPower(0);
        rightFrontMotor.setPower(0);
    }


}
