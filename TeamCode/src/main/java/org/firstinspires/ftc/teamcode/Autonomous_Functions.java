package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

// Created by MRINAAL RAMACHANDRAN on 10/8/17

// Last edit: 10/13/17 BY MRINAAL RAMACHANDRAN

public class Autonomous_Functions {

    // MOTOR NAMES

    protected DcMotor F_L = null;
    protected DcMotor F_R = null;
    protected DcMotor R_L = null;
    protected DcMotor R_R = null;

    // LOCAL OPMODE MEMBERS
    HardwareMap hwMap = null;

    // HARDWARE INIT
    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        F_L = hwMap.get(DcMotor.class, "F_L");
        F_R = hwMap.get(DcMotor.class, "F_R");
        R_L = hwMap.get(DcMotor.class, "R_L");
        R_R = hwMap.get(DcMotor.class, "R_R");

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);

        F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    // SLEEPING THREAD FUNCTION
    protected void mysleep(long time) {

        try {

            Thread.sleep(time);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    // STOP MOTOR FOR (x) TIME FUNCTION
    public void stopMotorTime(long time) {

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);
        mysleep(time);
    }

    // STOP MOTOR
    public void stopMotor() {

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);
    }

    public void driveForward(double power){

        F_L.setPower(-power);
        F_R.setPower(power);
        R_L.setPower(-power);
        R_R.setPower(power);
    }

    public void driveBackward (double power){

        F_L.setPower(power);
        F_R.setPower(-power);
        R_L.setPower(power);
        R_R.setPower(-power);
    }

    public void driveLeft (double power) {

        F_L.setPower(power);
        F_R.setPower(power);
        R_L.setPower(-power);
        R_R.setPower(-power);
    }

    public void driveRight (double power) {

        F_L.setPower(-power);
        F_R.setPower(-power);
        R_L.setPower(power);
        R_R.setPower(power);
    }

    public void spinLeft ( double power) {

        F_L.setPower(power);
        F_R.setPower(power);
        R_L.setPower(power);
        R_R.setPower(power);
    }

    public void spinRight ( double power) {

        F_L.setPower(-power);
        F_R.setPower(-power);
        R_L.setPower(-power);
        R_R.setPower(-power);
    }

    // MOVES THE MOTOR FOR TIME WITH INPUTS POWER, TIME, AND DIRECTION
    public void moveMotorWithTime(double power, long time, String direction) {

        if (direction == Constants.forward) {

            driveForward(power);
            mysleep(time);
        }

        if (direction == Constants.backward) {

            driveBackward(power);
            mysleep(time);
        }

        if (direction == Constants.left) {

            driveLeft(power);
            mysleep(time);
        }

        if (direction == Constants.right) {

            driveRight(power);
            mysleep(time);
        }

        if (direction == Constants.spinLeft) {

            spinLeft(power);
            mysleep(time);

        }

        if (direction == Constants.spinRight) {

            spinRight(power);
            mysleep(time);

        }

        stopMotor();
    }

    // MOVES THE MOTOR AT AN ANGLE WITH INPUTS POWER, TIME, DIRECTION, AND DEGREES
    public void moveAtAngle(double power, long time, String direction, double degrees){

        if (direction == Constants.angle) {

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }
    }

    // MOVES THE MOTOR WITH ENCODERS WITH INPUTS POWER AND DISTANCE
    public void moveMotorWithEncoder(double power, int distance, String direction) {

        if (direction == Constants.forward) {

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            driveForward(power);

            while (F_L.isBusy() && R_L.isBusy()) {

            }

            stopMotor();

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (direction == Constants.backward) {

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            driveBackward(power);

            while (F_L.isBusy() && R_L.isBusy()) {

            }

            stopMotor();

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (direction == Constants.left) {

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            driveLeft(power);

            while (F_L.isBusy() && R_L.isBusy()) {

            }

            stopMotor();

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }

        if (direction == Constants.right) {

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            driveRight(power);

            while (F_L.isBusy() && R_L.isBusy()) {

            }

            stopMotor();

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
}









