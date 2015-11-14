package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.Drivetrain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nikhil on 11/12/2015.
 */
public class BlueAuto extends LinearOpMode {

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
    public void runOpMode() throws InterruptedException {

        //lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
        //arm.init(hardwareMap);
        //intake.init(hardwareMap);
        sensorRGB = hardwareMap.colorSensor.get("sensorRGB");
        rightButtonServo = hardwareMap.servo.get("rightButtonServo");
        leftButtonServo = hardwareMap.servo.get("leftButtonServo");

        rightButtonServo.setPosition(0.5);
        leftButtonServo.setPosition(0.5);

        drivetrain.frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        drivetrain.frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        drivetrain.backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        drivetrain.backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        drivetrain.frontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        drivetrain.frontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        drivetrain.backLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        drivetrain.backRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);



        waitForStart();

        //drivetrain.drive(12,-0.5);

        while (drivetrain.frontLeft.getCurrentPosition() < 1049 ){

            drivetrain.frontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            drivetrain.frontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            drivetrain.backLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            drivetrain.backRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        }

        while (opModeIsActive()){

            telemetry.addData("rightMotor1", drivetrain.frontRight.getCurrentPosition());
            telemetry.addData("leftMotor1", drivetrain.frontLeft.getCurrentPosition());
            telemetry.addData("rightMotor2", drivetrain.backRight.getCurrentPosition());
            telemetry.addData("leftMotor2", drivetrain.backLeft.getCurrentPosition());
        }


    }
}
