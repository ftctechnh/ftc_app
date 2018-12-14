package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;


public class Functions{

    private static GyroSensor gyro;

    //Move function using INCHES and POWER
    public static void move(int distance, double power){

        int target = distance * (int) (288 / (4 * Math.PI));
        //When hardware is updated things will need to change here
        Hardware.backLeftDrive.setTargetPosition(target);
        Hardware.backRightDrive.setTargetPosition(target);
        Hardware.frontLeftDrive.setTargetPosition(target);
        Hardware.frontRightDrive.setTargetPosition(target);

        Hardware.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Hardware.backLeftDrive.setPower(power);
        Hardware.backRightDrive.setPower(power);
        Hardware.frontLeftDrive.setPower(power);
        Hardware.frontRightDrive.setPower(power);

        while (Hardware.backLeftDrive.isBusy() && Hardware.backRightDrive.isBusy()) {
            Hardware.telemetry.addData("BackLeft", Hardware.backLeftDrive.isBusy());
        }
        Hardware.backLeftDrive.setPower(0);
        Hardware.backRightDrive.setPower(0);
        Hardware.frontLeftDrive.setPower(0);
        Hardware.frontRightDrive.setPower(0);
    }

    //Turn function using DEGREES and POWER
    public static void turn(int degree, double power){
        gyro.resetZAxisIntegrator();
        double multiplier = 1.0;
        if(degree > 180) multiplier = -1.0;
       while(gyro.getHeading() != degree){
            Hardware.backLeftDrive.setPower(power * multiplier);
            Hardware.backRightDrive.setPower(-power * multiplier);
            Hardware.frontLeftDrive.setPower(power * multiplier);
            Hardware.frontRightDrive.setPower(-power * multiplier);
        }
    }


    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignore) {}
    }

    //PlaceMarker function that places the marker
    public static void PlaceMarker(){
        Hardware.markerServo.setPosition(-1);
        Hardware.markerServo.setPosition(1);
    }

    //The arm that drops the Hardware down
    public static void OffLander(double power){
        Hardware.armServo.setPower(1);
        sleep(2500);
        Hardware.armServo.setPower(0);

        Hardware.armLiftMotorTop.setPower(power);
        Hardware.armLiftMotorBottom.setPower(power);
        }
}