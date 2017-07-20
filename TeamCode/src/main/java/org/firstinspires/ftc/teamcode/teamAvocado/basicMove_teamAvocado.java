package org.firstinspires.ftc.teamcode.teamAvocado;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaCodecInfo;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by adityamavalankar on 7/16/17.
 */
@Autonomous(name = "basicMove_teamAvocado")
public class basicMove_teamAvocado extends LinearOpMode{

    DcMotor rightMotor;
    DcMotor leftMotor;
    public void runOpMode(){

        leftMotor= hardwareMap.dcMotor.get("leftMotor");
        rightMotor=  hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        rightMotor.setPower(0.2);
        leftMotor.setPower(0.2);
        sleep(1000);
        rightMotor.setPower(0);
        leftMotor.setPower(0.2);
        sleep(500);
        rightMotor.setPower(0.2);
        leftMotor.setPower(0.2);
        sleep(1000);
        rightMotor.setPower(0);
        leftMotor.setPower(0.2);
        sleep(500);
        rightMotor.setPower(0.2);
        leftMotor.setPower(0.2);
        sleep(1000);
        rightMotor.setPower(0);
        leftMotor.setPower(0.2);
        sleep(500);
        rightMotor.setPower(0.2);
        leftMotor.setPower(0.2);
        sleep(1000);



    }

}
