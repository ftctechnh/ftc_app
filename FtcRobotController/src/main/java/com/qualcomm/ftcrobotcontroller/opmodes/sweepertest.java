package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

public class sweepertest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor rightWheel;
        DcMotor leftWheel;

        Servo buttonServo;
        Servo button2Servo;
        Servo leftsweeper;
        Servo rightsweeper;
        Servo climberservo;


        OpticalDistanceSensor opticalDistanceSensor;
        UltrasonicSensor ultrasonicSensor;
        ColorSensor colorsensor;

        double reflectance = 0;
        double TARGET_REFLECTANCE = 0.1;
        double BLACKVALUE = 0.023;
        double WHITEVALUE = 0.29;
        double REDVALUE = 0.19; //NEED TO TEST
        double BLUEVALUE = 0.12; //NEED TO TEST
        double EOPDThreshold = 0.5 * (REDVALUE + WHITEVALUE);
        double ultrasonicThreshold = 15;
        String date;


        final double POWER = 0.3;
        final double BASEPOWER = 0.2;

        final double BUTTONSERVO_MIN_RANGE = 0.01;
        final double BUTTONSERVO_MAX_RANGE = 1;
        final double BUTTON2SERVO_MIN_RANGE = 0.01;
        final double BUTTON2SERVO_MAX_RANGE = 1;
        final double leftsweeper_MIN_RANGE = 0.01;
        final double leftsweeper_MAX_RANGE = 1;
        final double rightsweeper_MIN_RANGE = 0.01;
        final double rightsweeper_MAX_RANGE = 1;
        final double climberservo_MIN_RANGE = 0.01;
        final double climberservo_MAX_RANGE = 1;

        double buttonservoPosition;
        double button2servoPosition;
        double leftsweeperPosition;
        double rightsweeperPosition;
        double climberservoPosition;

        double buttonServoDelta = 0.49;
        double button2ServoDelta = 0.49;
        double leftsweeperDelta = 0.49;
        double rightsweeperDelta = 0.49;
        double climberservoDelta = 0.49;

        double value;


        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel.setDirection(DcMotor.Direction.FORWARD);

        buttonServo = hardwareMap.servo.get("buttonservo");
        buttonservoPosition = 0.3;
        buttonServo.setPosition(buttonservoPosition);
        button2Servo = hardwareMap.servo.get("button2servo");
        button2servoPosition = 0.3;
        button2Servo.setPosition(button2servoPosition);

        leftsweeper = hardwareMap.servo.get("leftsweeper");
        leftsweeperPosition = 0.8;
        leftsweeper.setPosition(leftsweeperPosition);
        rightsweeper = hardwareMap.servo.get("rightsweeper");
        rightsweeperPosition = 0.2;
        rightsweeper.setPosition(rightsweeperPosition);
        climberservo = hardwareMap.servo.get("climberservo");
        climberservoPosition = 0.0;
        climberservo.setPosition(climberservoPosition);

        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
        colorsensor = hardwareMap.colorSensor.get("colorsensor");
        colorsensor.enableLed(false);

        waitForStart();
      /* buttonServoPosition = 0.7;
       buttonServoPosition = Range.clip(buttonServoPosition, BUTTONSERVO_MIN_RANGE, BUTTONSERVO_MAX_RANGE);
        buttonServo.setPosition(buttonServoPosition);*/
        /*rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setTargetPosition(5000);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);*/


        //Sweeps the balls and boxes
        rightsweeper.setPosition(1);
        leftsweeper.setPosition(0);

        sleep(2000);

        rightsweeper.setPosition(0);
        leftsweeper.setPosition(1);

        sleep(2000);


    }
}

