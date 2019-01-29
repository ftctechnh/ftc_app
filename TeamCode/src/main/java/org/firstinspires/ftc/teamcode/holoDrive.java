//Razzle Dazzle of Fantazzmagazzles Code
//This was a collective effort by team 13383
//The code was worked on by: Aniketh Kolla, Vasudev Menon, and Vedang Singhal

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Math;



@TeleOp(name = "holoDrive", group = "Tank")
public class holoDrive extends OpMode {

    //this declares the motors
    DcMotor back_right;
    DcMotor back_left;
    DcMotor front_left;
    DcMotor front_right;
    DcMotor lift;
    DcMotor dustbinIntake;
    DcMotor extendIntake;
    DcMotor moveIntake;
    Servo dustBin;

    @Override
    public void init() {
        //this defines what the motors will be named in the robot configuration on the phones
        back_right = hardwareMap.dcMotor.get("back_right");
        back_left = hardwareMap.dcMotor.get("back_left");
        front_left = hardwareMap.dcMotor.get("front_left");
        front_right = hardwareMap.dcMotor.get("front_right");
        lift = hardwareMap.dcMotor.get("lift");
        dustbinIntake = hardwareMap.dcMotor.get("arm_Intake");
        extendIntake = hardwareMap.dcMotor.get("extend_Intake");
        moveIntake = hardwareMap.dcMotor.get("move_intake");
        dustBin = hardwareMap.servo.get("dust_bin");

    }

    @Override
    public void loop() {
        //the doubles are variables that are the gamepad control values
        //this was done so we don't have to change it in every place
        double yPower = gamepad1.left_stick_y;  //power to spin holonomic
        double xPower = gamepad1.left_stick_x;  //power to drive holonomic
        double spinPower = -gamepad1.right_stick_x; //power to drive holonomic
        double liftPower = gamepad2.left_stick_x; //power for lift
        double dustbinPower = -gamepad2.left_stick_y/6; //power for intake dustbin
        double extendPower = gamepad2.right_stick_y/4; //power for intake extension
        double movePower = gamepad2.right_stick_x/2; //power to move arm



//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //the first if statement spins the robot
        if(Math.abs(spinPower)>0.1) {
            back_right.setPower(spinPower);
            back_left.setPower(spinPower);
            front_left.setPower(spinPower);
            front_right.setPower(spinPower);
        }
        //the rest of the else if and else statements drive the robot around
        else if(Math.abs(yPower)>0.1 && (xPower<0.2 && xPower>-0.2)){
            back_right.setPower(yPower);
            back_left.setPower(-yPower);
            front_left.setPower(-yPower);
            front_right.setPower(yPower);
        }
        else if(Math.abs(xPower)>0.1 && (yPower<0.2 && yPower>-0.2)){
            if(xPower>0.1){
                back_right.setPower(-xPower);
                back_left.setPower(-xPower);
                front_left.setPower(xPower);
                front_right.setPower(xPower);
            }
            else if(xPower<0){
                back_right.setPower(-xPower);
                back_left.setPower(-xPower);
                front_left.setPower(xPower);
                front_right.setPower(xPower);
            }

        }
        else  if(xPower>0.1 && yPower>0.1){
            back_right.setPower(0);
            back_left.setPower(yPower);
            front_left.setPower(0);
            front_right.setPower(-yPower);
        }
        else  if(xPower<0 && yPower>0.1){
            back_right.setPower(-yPower);
            back_left.setPower(0);
            front_left.setPower(yPower);
            front_right.setPower(0);
        }
        else  if(xPower<0 && yPower<0){
            back_right.setPower(0);
            back_left.setPower(yPower);
            front_left.setPower(0);
            front_right.setPower(-yPower);

        }
        else  if(xPower>0.1 && yPower<0){
            back_right.setPower(-yPower);
            back_left.setPower(0);
            front_left.setPower(yPower);
            front_right.setPower(0);
        }
        else {
            back_right.setPower(0);
            back_left.setPower(0);
            front_left.setPower(0);
            front_right.setPower(0);
        }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

        lift.setPower(liftPower);
        //this controls the lift

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //these are the intake controls

        dustbinIntake.setPower(dustbinPower);
        //controls dustbin

        extendIntake.setPower(extendPower);
        //controls the intake extention

        moveIntake.setPower(movePower);
        //controls intake moving up and down

        if (gamepad2.y) {
            dustBin.setPosition(0);
        }
        if (gamepad2.a) {
            dustBin.setPosition(0.35);
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //this lets us see the power the motors are being set to
        telemetry.addData("xPower", "%.2f",  xPower);
        telemetry.addData("yPower", "%.2f",  yPower);
        telemetry.addData("liftPower", "%.2f",  liftPower);
        telemetry.addData("dustBinPower", "%.2f",  dustbinPower);
        telemetry.addData("extendPower", "%.2f",  extendPower);
        telemetry.addData("movePower", "%.2f",  movePower);
    }
}}

