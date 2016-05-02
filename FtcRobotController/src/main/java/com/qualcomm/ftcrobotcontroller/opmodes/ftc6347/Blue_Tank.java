<<<<<<< HEAD
package com.qualcomm.ftcrobotcontroller.opmodes.FTC6347;
=======
package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;
>>>>>>> b203628... Rename OpModes

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
<<<<<<< HEAD
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
=======
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
>>>>>>> b203628... Rename OpModes

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
<<<<<<< HEAD
    Servo hang;
    int blue_toggle;
    int red_toggle;
=======
    DcMotor motor7;
    DcMotorController winch;
    DcMotorController wheelie;
    Servo red;
    Servo blue;
    Servo arm1;
    Servo climberExtend;
    Servo climberDump;
    UltrasonicSensor ultrasonic4;
    UltrasonicSensor ultrasonic5;
    LightSensor reflectedLight;
    TouchSensor touch1;
    GyroSensor xAxis;
    GyroSensor zAxis;
    double climberExtendVar;
    double climberDumpVar;
    int toggle;
    double armState;
>>>>>>> b203628... Rename OpModes

    @Override
    public void init() {

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");
<<<<<<< HEAD

        motor2.setDirection(DcMotor.Direction.REVERSE);
        motor3.setDirection(DcMotor.Direction.REVERSE);
=======
        motor7 = hardwareMap.dcMotor.get("7");

        winch = hardwareMap.dcMotorController.get("c1"); //0
        wheelie = hardwareMap.dcMotorController.get("c2"); //1

        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.REVERSE);
        motor7.setDirection(DcMotor.Direction.REVERSE);
>>>>>>> b203628... Rename OpModes

        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());

<<<<<<< HEAD
        motor1.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos/////////////////////////////
        hang = hardwareMap.servo.get("sr6");

        hang.setPosition(0.6);//initialize servo
        ////////////////////////////Variables//////////////////////////
        red_toggle = 0;
        blue_toggle = 0;
=======
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

        //////////////////////////////Sensors///////////////////////////
        //////////////Legacy/////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3");
        reflectedLight.enableLed(true);
        ultrasonic4 = hardwareMap.ultrasonicSensor.get("s4"); //s4
        ultrasonic5 = hardwareMap.ultrasonicSensor.get("s5"); //s5
        ////////////CDI////////////
        touch1 = hardwareMap.touchSensor.get("t1"); //D 0
        xAxis = hardwareMap.gyroSensor.get("g2");//I2c 1
        zAxis = hardwareMap.gyroSensor.get("g1");//I2c 0
        xAxis.calibrate(); //calibrate both sensors
        zAxis.calibrate();

        ////////////////////////////Variables//////////////////////////
        climberExtendVar = 0.6;
        climberDumpVar = 0.82;
        toggle = 0;
        armState = 0;
>>>>>>> b203628... Rename OpModes

    }
    @Override
    public void loop() {

        /////////////////////////////////////////////Driver 1/////////////////////////////////////////////////////

        if (gamepad1.right_stick_y > 0.2 || gamepad1.right_stick_y < -0.2 || gamepad1.left_stick_y > 0.2 || gamepad1.left_stick_y < -0.2) {
            motor1.setPower(gamepad1.right_stick_y);  //Main Drive
            motor2.setPower(gamepad1.left_stick_y);
        }
<<<<<<< HEAD

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

=======
        else{
            motor1.setPower(0);  //Stop
            motor2.setPower(0);
        }
////////////////////////////////////////////Driver 1 Slow Buttons////////////////////////////////////////////////
        if (gamepad1.y) {
            motor1.setPower(-.3);  //Slow Forward
            motor2.setPower(-.3);
        }
        if (gamepad1.a) {
            motor1.setPower(.3);  //Slow Back
            motor2.setPower(.3);
        }
>>>>>>> b203628... Rename OpModes
        if (gamepad1.x) {
            motor1.setPower(-.3);  //Slow Left
            motor2.setPower(.3);
        }
<<<<<<< HEAD

=======
>>>>>>> b203628... Rename OpModes
        if (gamepad1.b) {
            motor1.setPower(.3);  //Slow Right
            motor2.setPower(-.3);
        }
<<<<<<< HEAD
//////////////////////////////////////////Driver 2////////////////////////////////////////
        if (gamepad2.right_stick_y > 0.2 || gamepad2.right_stick_y < -0.2){
            motor3.setPower(gamepad2.right_stick_y); //intake forward and backward
        }

        if (gamepad2.right_stick_y < 0.2 && gamepad2.right_stick_y > -0.2){
=======
//////////////////////////////////////Driver 1 Climber Triggers//////////////////////////////
        if (gamepad1.right_trigger > .5) {
            blue.setPosition(0.65);  //Blue toggle down
        }
        if (gamepad1.right_bumper) {
            blue.setPosition(0.02);  //Blue toggle up
        }

        if (gamepad1.left_trigger > .5) {
            red.setPosition(0.35);  //Red toggle down
        }
        if (gamepad1.left_bumper) {
            red.setPosition(0.91);  //Red toggle up
        }
//////////////////////////////////////////Driver 2////////////////////////////////////////
//////////////////////////////////Driver 2 Intake and Deposit/////////////////////////////
        if (gamepad2.right_stick_y > 0.2 || gamepad2.right_stick_y < -0.2){
            motor3.setPower(gamepad2.right_stick_y); //intake forward and backward
        }
        else{
>>>>>>> b203628... Rename OpModes
            motor3.setPower(0); //intake stop
        }

        if (gamepad2.left_stick_x > 0.2 || gamepad2.left_stick_x < -0.2){
            motor4.setPower(gamepad2.left_stick_x); //deposit left and right
        }
<<<<<<< HEAD

        if (gamepad2.left_stick_x < 0.2 && gamepad2.left_stick_x > -0.2){
            motor4.setPower(0); //intake stop
        }

        if (gamepad2.left_trigger > 0.2){
            motor5.setPower(0.3);
=======
        else{
            motor4.setPower(0); //intake stop
        }
/////////////////////////Driver 2 Hanging////////////////////////////////
        if (gamepad2.dpad_up){
            motor5.setPower(1); //winch up
        }
        else if (gamepad2.dpad_down && gamepad2.right_stick_button){
            motor5.setPower(-1); //winch down ONLY USE TO RESET, NEVER USE IN MATCH
>>>>>>> b203628... Rename OpModes
        }
        else{
            motor5.setPower(0);
        }

<<<<<<< HEAD
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
=======
        if (gamepad2.right_bumper){
            arm1.setPosition(0.8);//Hang arm down
        }

        if (gamepad2.left_bumper){
            arm1.setPosition(0.4); //Hang arm up
        }

        if (gamepad2.left_trigger > 0.5){
            motor6.setPower(0.3); //hang arm motor down
        }
        else if (gamepad2.right_trigger > 0.5){
            motor6.setPower(-0.3); //hang arm motor up
        }
        else{
            motor6.setPower(0);//stop
        }
//////////////////////////////////Driver 2 Climbers servo/////////////////////////////////
        if(gamepad2.x){

            armState =  armState + 0.01;//add one to state to change state of arm
        }
        if((int)armState%3 == 0){

            climberExtend.setPosition(0.6); //arm retracted
            climberDump.setPosition(0.82);
        }
        if((int)armState%3 == 1){

            climberExtend.setPosition(1);//arm extended, not dumped
            climberDump.setPosition(0.82);
        }
        if((int)armState%3 == 2){

            climberExtend.setPosition(1);//arm extended, dumped
            climberDump.setPosition(0);
        }
        if(armState > 3){
            armState = 0;
        }
//////////////////////////////////////Driver 2 wheelie bar//////////////////////////////////
        if(gamepad2.y){
            motor7.setPower(0.1);
            toggle = 0;
        }
        else if(gamepad2.a){

                toggle = 1;
        }
        else if (toggle == 1 && touch1.getValue() == 0){//not pushed and toggle = 1
            motor7.setPower(-0.4); //move down when moved up but should be down
        }
        else{
            motor7.setPower(0);
        }
///////////////////////////////////////Telemetry//////////////////////////////////////////
        telemetry.addData("Ultrasonic sensor4", ultrasonic4.getUltrasonicLevel());
        telemetry.addData("Ultrasonic sensor5", ultrasonic5.getUltrasonicLevel());
        telemetry.addData("Touch", touch1.getValue());
        telemetry.addData("Light", Double.toString(reflectedLight.getLightDetected()));
        telemetry.addData("Light Raw", Double.toString(reflectedLight.getLightDetectedRaw()));
        telemetry.addData("xAxis Heading", Double.toString(xAxis.getHeading()));
        telemetry.addData("zAxis Heading", Double.toString(zAxis.getHeading()));
        telemetry.addData("Toggle", Double.toString(toggle));
        telemetry.addData("Extend", Double.toString(climberExtendVar));
        telemetry.addData("Dump", Double.toString(climberDumpVar));
        telemetry.addData("ArmState", Double.toString(armState));
    }
}


>>>>>>> b203628... Rename OpModes
