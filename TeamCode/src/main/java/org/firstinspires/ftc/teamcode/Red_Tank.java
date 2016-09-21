package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FTCGearedUP on 8/6/2015.
 */
public class Red_Tank extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    Servo climbers;
    Servo red;
    Servo blue;
    UltrasonicSensor ultrasonicSensor;
    LightSensor reflectedLight;
    ColorSensor colorSensor;
    int blue_toggle;
    int red_toggle;

    @Override
    public void init() {

        /////////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");

        motor2.setDirection(DcMotor.Direction.REVERSE);
        motor3.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Motor 1 Dir", motor1.getDirection().toString());
        telemetry.addData("Motor 2 Dir", motor2.getDirection().toString());

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos/////////////////////////////
        climbers = hardwareMap.servo.get("sr2");
        red = hardwareMap.servo.get("sr3");
        blue = hardwareMap.servo.get("sr1");

        climbers.setPosition(1.0); //initialize arm
        red.setPosition(1); //initialize red arm
        blue.setPosition(0); //initialize blue arm

        //////////////////////////////Sensors///////////////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("s4");
        colorSensor = hardwareMap.colorSensor.get("s1");

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

        if (gamepad2.x){
            red.setPosition(0);  //Blue toggle up
        }

        if (gamepad2.b){
            red.setPosition(0.5);  //Blue toggle down
        }

        if (gamepad2.y){
            climbers.setPosition(0.3); //throw climbers
        }

        if (gamepad2.a){
            climbers.setPosition(1.0); //retract arm
        }
///////////////////////////////////////Telemetry//////////////////////////////////////////
        //reflection = reflectedLight.getLightDetectedRaw();
        telemetry.addData("red", Double.toString(colorSensor.red()));
        telemetry.addData("blue", Double.toString(colorSensor.blue()));
        telemetry.addData("green", Double.toString(colorSensor.green()));
        telemetry.addData("color", Double.toString(colorSensor.alpha()));
        //telemetry.addData("Ultrasonic Status" , ultrasonicSensor.status());
        //telemetry.addData("Ultrasonic Level", Double.toString(ultrasonicSensor.getUltrasonicLevel()));
        //telemetry.addData("ods value", Double.toString(ods.getLightDetected()));
        //telemetry.addData("ods raw", Double.toString(ods.getLightDetectedRaw()));
        //telemetry.addData("Blue value", Double.toString(blue.getPosition()));
        //telemetry.addData("Red value", Double.toString(red.getPosition()));
        telemetry.addData("Color argb", Double.toString(colorSensor.argb()));
        //telemetry.addData("Ultrasonic sensor", ultrasonicSensor.getUltrasonicLevel());

    }
}
