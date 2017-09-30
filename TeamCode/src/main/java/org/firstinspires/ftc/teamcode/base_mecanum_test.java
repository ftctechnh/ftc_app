package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.abs;
import java.math.*;

/**
 * Created by Eric on 9/20/2017!
 */

@TeleOp(name="mecanum_test",group="BBBot" )
public class base_mecanum_test extends LinearOpMode {
    HardwareSensorMap robot   = new HardwareSensorMap();   // Use a Pushbot's hardware
    //power variables
    DcMotor lf;
    DcMotor rf;
    DcMotor lb;
    DcMotor rb;

    @Override
    public void runOpMode() throws InterruptedException {

        //get motors
        lb = hardwareMap.dcMotor.get("m0"); //left back
        rb = hardwareMap.dcMotor.get("m1"); //right back
        lf = hardwareMap.dcMotor.get("m2"); //left front
        rf = hardwareMap.dcMotor.get("m3"); //right front

        //neat variables
        double drive; //turn power
        double turn; //turn direction
        double power; //move power
        double leftX; //left x: joystick
        double p1; //lb
        double p2; //rb
        double p3; //lf
        double p4; //rf

        waitForStart();
        drive = gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;
        leftX = gamepad1.left_stick_x;
        power = getPathagorus(leftX, drive);

        //main if/then
        if (turn > 0){

            //turn right
            p1 = drive;
            p2 = -drive;
            p3 = drive;
            p4 = -drive;

        }else if (turn == 0){

            //drive stuff
            if (Math.abs(drive) > Math.abs(leftX)){ //forward/back or left/right?
                if (drive > 0){ //forward
                    p1 = power;
                    p2 = power;
                    p3 = power;
                    p4 = power;
                }else{ //back
                    p1 = -power;
                    p2 = -power;
                    p3 = -power;
                    p4 = -power;
                }
            }else{
                if (drive > 0){ //right
                    p1 = power;
                    p2 = -power;
                    p3 = -power;
                    p4 = power;
                }else{ //left
                    p1 = -power;
                    p2 = power;
                    p3 = power;
                    p4 = -power;
                }
            }
        }else {

            //turn left
            p1 = -drive;
            p2 = drive;
            p3 = -drive;
            p4 = drive;

        }
        telemetry.addData("front left", p3);
        telemetry.addData("front right", p4);
        telemetry.addData("back left", p1);
        telemetry.addData("back right", p2);
        telemetry.update();
    }
    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;
    }


}
