package com.qualcomm.ftcrobotcontroller.opmodes;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.ftcrobotcontroller.opmodes.BasicMoveFunctions;
import com.qualcomm.ftcrobotcontroller.opmodes.HTRGBExample;

/**
 * Created by Robomain on 11/18/2015.
 */


public class Encoders extends  LinearOpMode {
    final static int encoder_kpr = 1120;
    final static double gear_ratio1 = 1;// unkown
    final static double gear_ratio2= 1.14;//unknown
    final static int wheel_diameter = 4;
    final static double Circumfrance = Math.PI *wheel_diameter;
    double activegear;
    double dpk = (encoder_kpr* gear_ratio1) /Circumfrance;
    double distance;
    DcMotor leftmotor;
    DcMotor rightmotor;
    BasicMoveFunctions command;
    double encoder1;
    double encoder2;
    HTRGBExample color;
    int colortemp;
    ColorSensor sensorRGB;
    Servo servo;

    public void initiate () throws InterruptedException{
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");
        activegear = gear_ratio1;
        rightmotor.setDirection(DcMotor.Direction.REVERSE);

        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        color.initsensor();
        servo = hardwareMap.servo.get("servo");
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initiate();
        waitForStart();
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitOneFullHardwareCycle();
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitOneFullHardwareCycle();
        // movebackward(10, .5);
        waitOneFullHardwareCycle();
        // turnright(4, .5);
        waitOneFullHardwareCycle();
        // turnleft(4, .5);
        waitOneFullHardwareCycle();
        // moveforward(10, .5);
        if(color.getBlue()<= color.getBlue())
        {
            servo.setPosition(50);
        }
        else if (color.getRed()<= color.getBlue())
        {
            servo.setPosition(80);
        }
        else {
            System.out.print("checks failed");
        }
    }
    public void moveforward(double move, double power) throws InterruptedException {


        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        int is;
        is = 0;
        encoder1 = encoder1+(int) distancetemp;
        encoder2= encoder2+(int)distancetemp;
        leftmotor.setTargetPosition((int) (encoder1));
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();
        rightmotor.setPower(power);
        while (is <= 1000) { // object not locked by wait
            waitOneFullHardwareCycle();
            is++;
            telemetry.addData("is", is);
        }
        leftmotor.setPower(.0);
        rightmotor.setPower(0);
    }

    public void turnleft(double move, double power) throws InterruptedException {

        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        encoder1 = encoder1- distancetemp;
        encoder2= encoder2+distancetemp;
        int is;
        is = 0;

        leftmotor.setTargetPosition((int) encoder1);
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();
        rightmotor.setPower(power);
        waitOneFullHardwareCycle();
        while (is <=  distance) {
            waitOneFullHardwareCycle();
            is++;
        }
        leftmotor.setPower(0);
        rightmotor.setPower(0);
    }


    public void turnright(double move, double power) throws InterruptedException {

        double distancetemp;
        distancetemp = move * dpk;
        int is;
        is = 0;
        distance = distancetemp;
        encoder1 = encoder1+ distancetemp;
        encoder2= encoder2- distancetemp;

        leftmotor.setTargetPosition((int) encoder1);
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();
        rightmotor.setPower(power);

        while (is <= 3* (int)distance) { // object not locked by wait
            waitOneFullHardwareCycle();
            is++;
            telemetry.addData("is", is);
            telemetry.addData("distance" , distance);
        }
        leftmotor.setPower(0);
        rightmotor.setPower(0);
    }

    public void movebackward(double move, double power) throws InterruptedException {

        double distancetemp;
        distancetemp = move * dpk;
        encoder1 = encoder1-(int) distancetemp;
        encoder2= encoder2-(int)distancetemp;
        int is;
        is = 0;
        distance = distancetemp;
        leftmotor.setTargetPosition((int) encoder1);
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();

        rightmotor.setPower(power);
        while (is <=  distance) { // object not locked by wait
            waitOneFullHardwareCycle();
            is++;
        }
        leftmotor.setPower(0);
        waitOneFullHardwareCycle();
        rightmotor.setPower(0);

    }

}

