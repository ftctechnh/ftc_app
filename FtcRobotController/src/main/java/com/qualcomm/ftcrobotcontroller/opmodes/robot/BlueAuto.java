package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nikhil on 11/12/2015.
 */
public class BlueAuto extends OpMode {


    //Lift lift = new Lift();
    Drivetrain drivetrain = new Drivetrain();
    //Arm arm = new Arm();
    //Intake intake = new Intake();

    Servo rightButtonServo;
    Servo leftButtonServo;
    ColorSensor sensorRGB;

    double rightButtonServoPressed = 0.45;
    double leftButtonServoPressed = 0.57;




    @Override
    public void init() {

        //lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
        //arm.init(hardwareMap);
        //intake.init(hardwareMap);
        sensorRGB = hardwareMap.colorSensor.get("sensorRGB");
        rightButtonServo = hardwareMap.servo.get("rightButtonServo");
        leftButtonServo = hardwareMap.servo.get("leftButtonServo");

        rightButtonServo.setPosition(0.5);
        leftButtonServo.setPosition(0.5);

        try {
            drivetrain.resetEncoders();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drivetrain.frontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        drivetrain.frontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        drivetrain.backLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        drivetrain.backRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }

    /*
    public void init_loop(){
        super.init_loop();

        drivetrain.resetEncoders();
    }
    */



    @Override
    public void loop() {

        try {
            drivetrain.drive(12,-0.5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        telemetry.addData("rightMotor1", drivetrain.frontRight.getCurrentPosition());
        telemetry.addData("leftMotor1", drivetrain.frontLeft.getCurrentPosition());
        telemetry.addData("rightMotor2", drivetrain.backRight.getCurrentPosition());
        telemetry.addData("leftMotor2", drivetrain.backLeft.getCurrentPosition());

    }
}
