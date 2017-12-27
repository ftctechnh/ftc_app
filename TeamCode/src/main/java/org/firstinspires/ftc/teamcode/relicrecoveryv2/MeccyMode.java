package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by  on 12/14/17.
 */

public abstract class MeccyMode extends LinearOpMode{
    //<editor-fold desc="Yay">
    abstract public void runOpMode();
    //
    DcMotor leftFrontMotor;
    DcMotor rightFrontMotor;
    DcMotor leftBackMotor;
    DcMotor rightBackMotor;
    //
    public void configureMotors(String leftFront, String rightFront, String leftBack, String rightBack){
        leftBackMotor = hardwareMap.dcMotor.get(leftFront);
        rightBackMotor = hardwareMap.dcMotor.get(rightFront);
        leftFrontMotor = hardwareMap.dcMotor.get(leftBack);
        rightFrontMotor = hardwareMap.dcMotor.get(rightBack);
        //
        rightBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    //
    public boolean isMoving(){
        return leftBackMotor.isBusy() &&
                leftFrontMotor.isBusy() &&
                rightBackMotor.isBusy() &&
                rightFrontMotor.isBusy();
    }
    //</editor-fold>
    //
    //<editor-fold desc="Turning">
    public void turnRight(double powerInput){
        leftBackMotor.setPower(powerInput);
        leftFrontMotor.setPower(powerInput);
        rightBackMotor.setPower(-powerInput);
        rightFrontMotor.setPower(-powerInput);
    }
    //
    public void turnLeft(double powerInput){
        leftBackMotor.setPower(-powerInput);
        leftFrontMotor.setPower(-powerInput);
        rightBackMotor.setPower(powerInput);
        rightFrontMotor.setPower(powerInput);
    }
    //</editor-fold>
    //
    //<editor-fold desc="Driving">
    public void driveForward(double powerInput){
        leftBackMotor.setPower(powerInput);
        leftFrontMotor.setPower(powerInput);
        rightBackMotor.setPower(powerInput);
        rightFrontMotor.setPower(powerInput);
    }
    //
    public void driveBackward(double powerInput){
        leftBackMotor.setPower(-powerInput);
        leftFrontMotor.setPower(-powerInput);
        rightBackMotor.setPower(-powerInput);
        rightFrontMotor.setPower(-powerInput);
    }
    //</editor-fold>
    //
    //<editor-fold desc="Strafing">
    public void strafeLeft(double powerInput){
            leftBackMotor.setPower(-powerInput);
            leftFrontMotor.setPower(powerInput);
            rightBackMotor.setPower(powerInput);
            rightFrontMotor.setPower(-powerInput);
    }
    //
    public void strafeRight(double powerInput){
        leftBackMotor.setPower(powerInput);
        leftFrontMotor.setPower(-powerInput);
        rightBackMotor.setPower(-powerInput);
        rightFrontMotor.setPower(powerInput);
    }
    //</editor-fold>
}
