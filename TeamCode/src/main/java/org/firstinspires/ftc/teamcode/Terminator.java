package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;



/**
 * Created by Swagster_Wagster on 9/29/17.
 */

@Autonomous(name = "Terminator")
public class Terminator extends LinearOpMode {

    DcMotor F_R = null;
    DcMotor F_L = null;
    DcMotor R_R = null;
    DcMotor R_L = null;



    String forward = "FORWARD";
    String backward = "BACKWARD";
    String left = "LEFT";
    String right = "RIGHT";
    String none = "NONE";

    double fast = .8;
    double medium = .5;
    double normal = .2;
    double slow = .1;

    public void moveMotorWithTime(double power, long time, String direction) {

        if (direction == "FORWARD") {

            try {

                F_L.setPower(-power);
                F_R.setPower(power);
                R_L.setPower(-power);
                R_R.setPower(power);

                telemetry.addData("ACTION COMPLETED", "SLEEPING");
                Thread.sleep(time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (direction == "BACKWARD") {

            try {

                F_L.setPower(power);
                F_R.setPower(-power);
                R_L.setPower(power);
                R_R.setPower(-power);

                Thread.sleep(time);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (direction == "LEFT") {

            try {

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(-power);
                R_R.setPower(-power);

                Thread.sleep(time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (direction == "RIGHT") {

            try {

                F_L.setPower(-power);
                F_R.setPower(-power);
                R_L.setPower(power);
                R_R.setPower(power);

                Thread.sleep(time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (direction == "ANGLE") {

            try {

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(power);
                R_R.setPower(power);

                Thread.sleep(time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stopMotor();
        }
    }




    public void mysleep(long time){

        try {

            Thread.sleep(time);
        } catch (InterruptedException e){

            e.printStackTrace();
        }
    }

    public void stopMotorTime(long time){

                F_L.setPower(0);
                F_R.setPower(0);
                R_L.setPower(0);
                R_R.setPower(0);
                sleep(time);

                telemetry.addData("ACTION COMPLETED", "MOTOR STOPPED");
            }

    public void stopMotor(){

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);

        telemetry.addData("ACTION COMPLETED", "MOTOR STOPPED");
    }


    @Override
    public void runOpMode() throws InterruptedException {

        F_L = hardwareMap.dcMotor.get("F_L");
        F_R = hardwareMap.dcMotor.get("F_R");
        R_L = hardwareMap.dcMotor.get("R_L");
        R_R = hardwareMap.dcMotor.get("R_R");

        telemetry.addData("ACTION COMPLETED", "READY FOR BREACH");

        waitForStart();

        //moveMotorWithTime(normal, 3000, forward);
        //stopMotor();
        //moveMotorWithTime(normal, 3000, backward);
        //stopMotor();
        moveMotorWithTime(normal, 3000, left);
        stopMotor();
        moveMotorWithTime(normal, 3000, right);
    }

}
