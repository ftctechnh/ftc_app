package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by TPR on 12/14/17.
 */

//The length of time(in seconds) to complete a 90 degree rotation can be modeled by
//90(πx/2πy)/pr where p is the power of the motors, r is the rotations per second,
//x is the diameter of the wheel, and y is the radius of the robot.
//Life is math. Math is life. Exhibit A.

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
    //
    public void waitForStartify(){
        waitForStart();
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
    public void strafeLeft(double power1, double power2, double direction){
            leftBackMotor.setPower(power1 + (direction * power2 / 2));
            leftFrontMotor.setPower(-power1 - (direction * power2 / 2));
            rightBackMotor.setPower(-power1 - (direction * power2 / 2));
            rightFrontMotor.setPower(power1 + (direction * power2 / 2));
    }
    //
    public void strafeRight(double power1, double power2, double direction){
        leftBackMotor.setPower(-power1 - (direction * power2 / 2));
        leftFrontMotor.setPower(power1 + (direction * power2 / 2));
        rightBackMotor.setPower(power1 + (direction * power2 / 2));
        rightFrontMotor.setPower(-power1 - (direction * power2 / 2));
    }
    //</editor-fold>
}
