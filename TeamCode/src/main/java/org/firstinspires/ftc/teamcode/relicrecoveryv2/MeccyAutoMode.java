package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by christinecarroll on 12/14/17.
 */

public abstract class MeccyAutoMode extends MeccyMode{
    abstract public void runOpMode();
    //
    static final double countify = 116.501;
    //
    public void configureMotors(String leftFront, String rightFront, String leftBack, String rightBack){
        super.configureMotors(leftFront, rightFront, leftBack,rightBack);
        //
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
    public void forwardToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //
    public void backToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //
    public void rightToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() - move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() - move);
        //
        setSpeed(speed);
    }
    //
    public void turnRightToPostion(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() + move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() - move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() - move);
        //
        setSpeed(speed);
    }
    //
    public void leftToPosition(double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() + move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() - move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() - move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //
    public void turnLeftToPosition (double inches, double speed){
        int move = (int)(Math.round(inches*countify));
        //
        leftBackMotor.setTargetPosition(leftBackMotor.getCurrentPosition() - move);
        leftFrontMotor.setTargetPosition(leftFrontMotor.getCurrentPosition() - move);
        rightBackMotor.setTargetPosition(rightBackMotor.getCurrentPosition() + move);
        rightFrontMotor.setTargetPosition(rightFrontMotor.getCurrentPosition() + move);
        //
        setSpeed(speed);
    }
    //
    private void setSpeed(double speed){
        leftBackMotor.setPower(speed);
        rightBackMotor.setPower(speed);
        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
    }
}
