package com.qualcomm.ftcrobotcontroller.opmodes.FTC6347;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FTCGearedUP on 8/6/2015.
 */
public class Blue_Tank extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotor motor6;
    Servo hang;
    int blue_toggle;
    int red_toggle;

    @Override
    public void init() {

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");

        motor2.setDirection(DcMotor.Direction.REVERSE);
        motor3.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());

        motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos/////////////////////////////
        hang = hardwareMap.servo.get("sr6");

        hang.setPosition(0.6);//initialize servo
        ////////////////////////////Variables//////////////////////////
        red_toggle = 0;
        blue_toggle = 0;

    }
    @Override
    public void loop() {

        /////////////////////////////////////////////Driver 1/////////////////////////////////////////////////////

        if (gamepad1.right_stick_y > 0.2 || gamepad1.right_stick_y < -0.2 || gamepad1.left_stick_y > 0.2 || gamepad1.left_stick_y < -0.2) {
            motor1.setPower(gamepad1.right_stick_y);  //Main Drive
            motor2.setPower(gamepad1.left_stick_y);
        }

        if (gamepad1.right_stick_y < 0.2 && gamepad1.right_stick_y > -0.2 && gamepad1.left_stick_y < 0.2 && gamepad1.left_stick_y > -0.2) {
            motor1.setPower(0);  //Stop
            motor2.setPower(0);
        }

        if (gamepad1.y) {
            motor1.setPower(-.2);  //Slow Forward
            motor2.setPower(-.2);
        }

        if (gamepad1.a) {
            motor1.setPower(.6);  //Slow Back
            motor2.setPower(.6);
        }

        if (gamepad1.x) {
            motor1.setPower(-.3);  //Slow Left
            motor2.setPower(.3);
        }

        if (gamepad1.b) {
            motor1.setPower(.3);  //Slow Right
            motor2.setPower(-.3);
        }
//////////////////////////////////////////Driver 2////////////////////////////////////////
        if (gamepad2.right_stick_y > 0.2 || gamepad2.right_stick_y < -0.2){
            motor3.setPower(gamepad2.right_stick_y); //intake forward and backward
        }

        if (gamepad2.right_stick_y < 0.2 && gamepad2.right_stick_y > -0.2){
            motor3.setPower(0); //intake stop
        }

        if (gamepad2.left_stick_x > 0.2 || gamepad2.left_stick_x < -0.2){
            motor4.setPower(gamepad2.left_stick_x); //deposit left and right
        }

        if (gamepad2.left_stick_x < 0.2 && gamepad2.left_stick_x > -0.2){
            motor4.setPower(0); //intake stop
        }

        if (gamepad2.left_trigger > 0.2){
            motor5.setPower(0.3);
        }
        else{
            motor5.setPower(0);
        }

        if (gamepad2.right_trigger > 0.2){
            motor5.setPower(-0.3);
        }
        else{
            motor5.setPower(0);
        }

        if(gamepad2.y){
            hang.setPosition(1);
        }

        if(gamepad2.a){
            hang.setPosition(0.6);
        }

        if(gamepad2.dpad_up){
            motor6.setPower(1);
        }
        else{
            motor6.setPower(0);
        }

        if(gamepad2.dpad_down && gamepad2.left_stick_button){
            motor6.setPower(-1);
        }
        else{
            motor6.setPower(0);
        }
///////////////////////////////////////Telemetry//////////////////////////////////////////



    }
}
