package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Created by Swagster_Wagster on 10/8/17.
 */

public class Autonomous_Functions {

    // MOTOR NAMES

    public DcMotor  F_L = null;
    public DcMotor  F_R = null;
    public DcMotor  R_L = null;
    public DcMotor  R_R = null;

    // LOCAL OPMODE MEMBERS
    HardwareMap hwMap  =  null;

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

    // MOVES THE MOTOR FOR TIME WITH INPUTS POWER, TIME, AND DIRECTION
    public void moveMotorWithTime(double power, long time, String direction) {

        if (direction == Constants.forward) {

            F_L.setPower(-power);
            F_R.setPower(power);
            R_L.setPower(-power);
            R_R.setPower(power);

            mysleep(time);


        }

        if (direction == Constants.backward) {



                F_L.setPower(power);
                F_R.setPower(-power);
                R_L.setPower(power);
                R_R.setPower(-power);
                mysleep(time);
        }

        if (direction == Constants.left) {

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(-power);
                R_R.setPower(-power);
                mysleep(time);
        }

        if (direction == Constants.right) {

                F_L.setPower(-power);
                F_R.setPower(-power);
                R_L.setPower(power);
                R_R.setPower(power);
                mysleep(time);
        }

        if (direction == Constants.spin) {

                F_L.setPower(power);
                R_L.setPower(power);
                R_R.setPower(power);
                mysleep(time);

        }

        stopMotor();
    }

    // MOVES THE MOTOR AT AN ANGLE WITH THE INPUTS POWER, TIME, DIRECTION, AND DEFREES
    public void moveAtAngle(double power, long time, String direction, double degrees){

        if (direction == Constants.angle) {

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }
    }

    // MOVES THE MOTOR WITH ENCODERS WITH INPUTS POWER, TIME, AND DISTANCE
    public void moveMotorWithEncoder(double power, long time, double distance) {


    }

}









