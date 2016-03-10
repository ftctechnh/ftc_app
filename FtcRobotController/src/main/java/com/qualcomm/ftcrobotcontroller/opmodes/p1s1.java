package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;



import java.lang.*;


/**
 * Created by baptiste on 15/11/2015.
 */
public class p1s1 extends OpMode {
    //This declares two motors
    DcMotor rightMotor;
    DcMotor leftMotor;
    //the length the track of the track is 98 cm
    //This declares the constants we'll use to work out how many counts we need
    final static int ENCODER_CPR = 757;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 56/24;      //Gear Ratio
    final static double WHEEL_DIAMETER = 0.98/Math.PI;     //Diameter of the wheel in m
    final static double CIRCUMFERENCE = 0.98;
    //Works out the number of encoder counts we need given the number of squares we have to travel
    public double returnCountsFromNumberOfSquares (double squares) {

//        double distance2 = squares*0.61;
        double distance2 = squares*0.5;

        double ROTATIONS = distance2 / CIRCUMFERENCE;
        double valueToReturn = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        telemetry.addData("Motor Target", valueToReturn);
        return valueToReturn;

    }
    //Works out the number of encoder counts we need given the distance we have to travel
    public double returnCountsFromDistance (double distance) {
        double ROTATIONS = distance/CIRCUMFERENCE;
        double valueToReturn = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        telemetry.addData("Motor Target", valueToReturn);
        return valueToReturn;
    }

    @Override
    public void init() {
        //The code that runs when we start the robot. We assign the motors and reset the encoder count
        rightMotor = hardwareMap.dcMotor.get("motor_1");
        leftMotor = hardwareMap.dcMotor.get("motor_2");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    //This makes us tell the motors how far to travel, and which tracks to move
    public void drive (double numberOfSquares, double power, String mode) {
        //Makes both motors go forwards
        if (mode.equals("both")) {

            leftMotor.setTargetPosition((int) returnCountsFromNumberOfSquares(numberOfSquares));
            rightMotor.setTargetPosition((int) returnCountsFromNumberOfSquares(numberOfSquares));
            leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

            leftMotor.setPower(power);
            rightMotor.setPower(power);
        }
        //Makes the robot turn right
        if (mode.equals("left")) {

   //         leftMotor.setTargetPosition(-((int) returnCountsFromDistance(numberOfSquares)));
            rightMotor.setTargetPosition((int) returnCountsFromDistance(numberOfSquares));

    //        leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

       //     leftMotor.setPower(-power);
            rightMotor.setPower(power);

        }
        //Makes the robot turn left
        if (mode.equals("right")) {

            leftMotor.setTargetPosition((int) returnCountsFromDistance(numberOfSquares));
         //   rightMotor.setTargetPosition(-((int) returnCountsFromDistance(numberOfSquares)));

            leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
       //     rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

            leftMotor.setPower(power);
        //    rightMotor.setPower(-power);

        }
        //This tries tell us what went wrong in the program?

    }

    @Override
    public void start() {
        //Tells us how far the robot has to travel to turn 90 degrees
        double distanceToTurn90Degrees = 0.63;
        double motorPower = 0.5;

        telemetry.addData("About to start", "0");

    //    leftMotor.setPower(0.5);

        drive(1, motorPower, "both"); //forward 1 squares (50 cm) // 0.61
        telemetry.addData("did drive", "1");

//
//                try {
//            wait(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        drive(distanceToTurn90Degrees, motorPower, "left"); //90 degress anticlockwise
//        drive(2, motorPower, "both"); // move 2 squares forward

      //  stop();

    }
    //Not relevant because we just run the program once
    @Override
    public void loop() {

        telemetry.addData("Left Position", leftMotor.getCurrentPosition());
        telemetry.addData("Right Position", rightMotor.getCurrentPosition());

    }}
