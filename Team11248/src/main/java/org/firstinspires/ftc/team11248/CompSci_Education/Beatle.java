package org.firstinspires.ftc.team11248.CompSci_Education;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Tony_Air on 12/18/17.
 */

public class Beatle
{
    private DcMotor left, right;
    private Servo leftClaw, rightClaw;

    double lo = 0;
    double ro = 0;
    double rc = 0;
    double lc = 0;

    boolean isOpen = true;

    public Beatle(HardwareMap hardwareMap, Telemetry telemetry){

        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        leftClaw = hardwareMap.servo.get("servo1");
        rightClaw = hardwareMap.servo.get("servo2");

    }


    public void init(){
        leftClaw.setPosition(lo);
        rightClaw.setPosition(ro);
    }

    public void drive(double x, double y){

        double speed = .75*y;
        double rotation = .25*x;

        left.setPower(speed-rotation);
        right.setPower(-(speed+rotation));
    }


    public void toggle_claw(){

        if(isOpen){
            leftClaw.setPosition(lc);
            rightClaw.setPosition(rc);

        }else{
            leftClaw.setPosition(lo);
            rightClaw.setPosition(ro);
        }

        isOpen = !isOpen;
    }

}
