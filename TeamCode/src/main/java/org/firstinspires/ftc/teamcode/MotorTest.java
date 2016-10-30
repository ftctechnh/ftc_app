package org.firstinspires.ftc.teamcode;

import android.location.LocationListener;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//Created by Michael on 10/14/2016.

@TeleOp(name="MotorTest")
public class MotorTest extends RobotHardware {


    boolean toggle;
    boolean bPress;

    @Override public void loop() {


        if (gamepad1.x){
            if (!bPress){
                bPress = true;
                toggle = !toggle;
            }
        }else{
            bPress = false;
        }

        if (toggle){
            scooperMotor.setPower(1);
        }else{
            scooperMotor.setPower(0);
        }
    }
}