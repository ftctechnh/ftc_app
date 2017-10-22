package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// Created by MRINAAL RAMACHANDRAN on 10/8/17

// Last edit: 10/21/17 BY MRINAAL RAMACHANDRAN

public class Autonomous_Functions {

    // MOTOR NAMES

    protected DcMotor F_L = null;
    protected DcMotor F_R = null;
    protected DcMotor R_L = null;
    protected DcMotor R_R = null;

    protected Servo dropper = null;

    // LOCAL OPMODE MEMBERS
    HardwareMap hwMap = null;
    Telemetry telemetry = null;

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

        F_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        F_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        dropper = hwMap.get(Servo.class, "dropper");


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
    public void stopMotor(long time) {

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);
        mysleep(time);

    }

    // STOP MOTOR
    public void stopMotor() {

        stopMotor(0);

    }

    // MOVES THE MOTOR FOR TIME WITH INPUTS POWER, TIME, AND DIRECTION
    public void moveMotorWithTime(double power, long time, String direction) {

        if (direction == Constants.forward) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        if (direction == Constants.backward) {

            F_R.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        if (direction == Constants.left) {

            R_L.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        if (direction == Constants.right) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.REVERSE);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setPower(-power);
            F_R.setPower(-power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        stopMotor();
    }

    // MOVES THE MOTOR WITH ENCODERS WITH INPUTS POWER, TIME, AND DISTANCE
    public void moveMotorWithEncoder(double power, int distance, String direction) {

        F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (direction == Constants.forward) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power * 10);
            F_R.setPower(power * 10);
            R_L.setPower(power * 10);
            R_R.setPower(power * 10);

            while (F_L.getCurrentPosition() < distance/2) {

                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            F_L.setPower(power * 5);
            F_R.setPower(power * 5);
            R_L.setPower(power * 5);
            R_R.setPower(power * 5);


            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.backward) {

            F_R.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.left) {

            R_L.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.right) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.REVERSE);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.spinRight) {

            F_L.setDirection(DcMotor.Direction.FORWARD);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.spinLeft) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.REVERSE);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }


        stopMotor();
    }
}








