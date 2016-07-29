package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;

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
    DcMotorController winch;
    DcMotorController wheelie;
    Servo red;
    Servo blue;
    Servo arm1;
    Servo climberExtend;
    Servo climberDump;
    UltrasonicSensor ultrasonic4;
    UltrasonicSensor ultrasonic5;
    TouchSensor touch1;
    GyroSensor xAxis;
    GyroSensor zAxis;
    double climberExtendVar;
    double climberDumpVar;
    int toggle;
    double armState;

    @Override
    public void init() {

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");

        winch = hardwareMap.dcMotorController.get("c1"); //0


        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.REVERSE);


        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());


        /////////////////////////////Servos/////////////////////////////
        hang = hardwareMap.servo.get("sr6");

        hang.setPosition(0.6);//initialize servo
        ////////////////////////////Variables//////////////////////////
        red_toggle = 0;
        blue_toggle = 0;
        motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos////////////////////////////
        red = hardwareMap.servo.get("sr3");// 3
        blue = hardwareMap.servo.get("sr1");// 1
        arm1 = hardwareMap.servo.get("sr6");// 6
        climberExtend = hardwareMap.servo.get("sr4");// 4
        climberDump = hardwareMap.servo.get("sr5"); // 5

        red.setPosition(0.91); //initialize red arm
        blue.setPosition(0.02); //initialize blue arm
        arm1.setPosition(0.85); //initialize hang arm1, higher servo
        climberExtend.setPosition(0.6); //initialize climber extender
        climberDump.setPosition(0.82); //initialize climbers to be facing up


        ////////////////////////////Variables//////////////////////////
        climberExtendVar = 0.6;
        climberDumpVar = 0.82;
        toggle = 0;
        armState = 0;

    }
    @Override
    public void loop() {

        /////////////////////////////////////////////Driver 1/////////////////////////////////////////////////////

        handleStick();

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

        // Red and blue arms
        if(gamepad1.left_trigger > 0.2) {
            blue.setPosition(0.6);
        } else {
            blue.setPosition(0.02);
        }
        if(gamepad1.right_trigger > 0.2) {
            red.setPosition(0.47);
        } else {
            red.setPosition(0.91);
        }
///////////////////////////////////////Telemetry//////////////////////////////////////////



    }

    private void handleStick() {
        if (gamepad1.right_stick_y > 0.2 || gamepad1.right_stick_y < -0.2) {
            motor1.setPower(gamepad1.right_stick_y);  //Main Drive
        } else
        {
            motor1.setPower(0);
        }

        if (gamepad1.left_stick_y > 0.2 || gamepad1.left_stick_y < -0.2)
        {
            motor2.setPower(gamepad1.left_stick_y);
        } else
        {
            motor2.setPower(0);
        }
        telemetry.addData("Right Power",gamepad1.right_stick_y);
        telemetry.addData("Left Power",gamepad1.left_stick_y);

    }
}
