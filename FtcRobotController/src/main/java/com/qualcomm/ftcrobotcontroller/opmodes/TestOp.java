package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Tim on 8/9/2016.
 */
public class TestOp extends OpMode {

    DcMotor left1, left2;
    DcMotor right1, right2;

    double rightp, leftp;

    public void init(){
        right1 = hardwareMap.dcMotor.get("right1");
        right2 = hardwareMap.dcMotor.get("right2");

        left1 = hardwareMap.dcMotor.get("left1");
        left2 = hardwareMap.dcMotor.get("left2");
    }

    public void loop(){

        double right = gamepad1.right_stick_y;
        double left = gamepad1.left_stick_y;


        right1.setPower(right*right);
        right2.setPower(right*right);

        left1.setPower(left*left);
        left2.setPower(left*left);

    }

    public void stop(){
        right1.setPower(0);
        right2.setPower(0);

        left1.setPower(0);
        left2.setPower(0);

    }
}
