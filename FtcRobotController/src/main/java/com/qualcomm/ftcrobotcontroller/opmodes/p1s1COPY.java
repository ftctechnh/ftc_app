package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import android.util.Log;

import com.qualcomm.ftcrobotcontroller.opmodes.sensorCode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.*;


/**
 * Created by baptiste on 15/11/2015.
 */
public class p1s1COPY extends OpMode {

    sensorCode boschBNO055;

    //The following arrays contain both the Euler angles reported by the IMU (indices = 0) AND the
    // Tait-Bryan angles calculated from the 4 components of the quaternion vector (indices = 1)
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];

    long systemTime;//Relevant values of System.nanoTime


    //This declares two motors
    DcMotor rightMotor;
    DcMotor leftMotor;
    //This declares the constants we'll use to work out how many counts we need
    final static int ENCODER_CPR = 757;     //Encoder Counts per Revolution
    final static double GEAR_RATIO = 56/24;      //Gear Ratio
    final static double WHEEL_DIAMETER = 0.94/Math.PI;     //Diameter of the wheel in m
    final static double CIRCUMFERENCE = 0.94;
    //Works out the number of encoder counts we need given the number of squares we have to travel
    public double returnCountsFromNumberOfSquares (double squares) {

//      double distance2 = squares*0.61;

        double distance2 = squares*0.5;

        double ROTATIONS = distance2 / CIRCUMFERENCE;

        double valueToReturn = ENCODER_CPR * ROTATIONS * GEAR_RATIO * 10 * (480/500);

        telemetry.addData("Motor Target", valueToReturn);

        return valueToReturn;


    }
    //Works out the number of encoder counts we need given the distance we have to travel
    public double returnCountsFromDistance (double distance) {


        double ROTATIONS = distance / CIRCUMFERENCE;
        double valueToReturn = ENCODER_CPR * ROTATIONS * GEAR_RATIO * 10 * (480/500);

        telemetry.addData("Motor Target", valueToReturn);
        return valueToReturn;
    }


    @Override
    public void init() {

        try {
            boschBNO055 = new sensorCode(hardwareMap, "bno055"

                    //The following was required when the definition of the "I2cDevice" class was incomplete.
                    //, "cdim", 5

                    , (byte)(sensorCode.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    //addressing
                    , (byte)sensorCode.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: " + e.getMessage());
        }

        Log.i("FtcRobotController", "IMU Init method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");


        //The code that runs when we start the robot. We assign the motors and reset the encoder count
        rightMotor = hardwareMap.dcMotor.get("motor_1");
        leftMotor = hardwareMap.dcMotor.get("motor_2");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }
    //This makes us tell the motors how far to travel, and which track/s to move
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
        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //This tries tell us what went wrong in the program?
    }

    public void turn() {
        systemTime = System.nanoTime();
        boschBNO055.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
        Log.i("FtcRobotController", "IMU Start method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");

        boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);

        double currentYawAngle = yawAngle[0];

        while (yawAngle[0] < currentYawAngle+90) {

            boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);

            rightMotor.setPower(0.2);

        }

        rightMotor.setPower(0);
    }

    @Override
    public void start() {


        turn();


        //Tells us how far the robot has to travel to turn 90 degrees
        double distanceToTurn90Degrees = 0.63;
        double motorPower = 0.5;

    //    drive(1, motorPower, "both"); //forward 1 squares (50 cm) // 0.61
        stop();

    }
    //Not relevant because we just run the program once
    @Override
    public void loop() {

        boschBNO055.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
		/*
		 * Send whatever telemetry data you want back to driver station.
		 */
        //telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Headings(yaw): ",
                String.format("Euler= %4.5f, Quaternion calculated= %4.5f", yawAngle[0], yawAngle[1]));
        telemetry.addData("Pitches: ",
                String.format("Euler= %4.5f, Quaternion calculated= %4.5f", pitchAngle[0], pitchAngle[1]));
        telemetry.addData("Max I2C read interval: ",
                String.format("%4.4f ms. Average interval: %4.4f ms.", boschBNO055.maxReadInterval
                        , boschBNO055.avgReadInterval));


        telemetry.addData("Left Position", leftMotor.getCurrentPosition());
        telemetry.addData("Right Position", rightMotor.getCurrentPosition());

    }

    @Override
    public void stop() {
        systemTime = System.nanoTime();
        Log.i("FtcRobotController", "IMU Stop method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");
        super.stop();
    }
}


