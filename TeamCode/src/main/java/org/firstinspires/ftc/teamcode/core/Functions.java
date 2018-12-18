package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;


public class Functions {

    private GyroSensor gyro;

    Hardware Hw = new Hardware();

    //Move function using INCHES and POWER
    public void move(int distance, double power) {

        int target = distance * (int) (288 / (4 * Math.PI));
        Hw.backLeftDrive.setTargetPosition(target);
        Hw.backRightDrive.setTargetPosition(target);
        Hw.frontLeftDrive.setTargetPosition(target);
        Hw.frontRightDrive.setTargetPosition(target);

        Hw.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hw.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hw.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hw.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Hw.backLeftDrive.setPower(power);
        Hw.backRightDrive.setPower(power);
        Hw.frontLeftDrive.setPower(power);
        Hw.frontRightDrive.setPower(power);

        while (Hw.backLeftDrive.isBusy() && Hw.backRightDrive.isBusy()) {
            Hw.telemetry.addData("BackLeft", Hw.backLeftDrive.isBusy());
        }
        Hw.backLeftDrive.setPower(0);
        Hw.backRightDrive.setPower(0);
        Hw.frontLeftDrive.setPower(0);
        Hw.frontRightDrive.setPower(0);
    }

    //Turn function using DEGREES and POWER
    public void turn(int degree, double power) {
        gyro.resetZAxisIntegrator();
        double multiplier = 1.0;
        if (degree > 180) multiplier = -1.0;
        while (gyro.getHeading() != degree) {
            Hw.backLeftDrive.setPower(power * multiplier);
            Hw.backRightDrive.setPower(-power * multiplier);
            Hw.frontLeftDrive.setPower(power * multiplier);
            Hw.frontRightDrive.setPower(-power * multiplier);
        }
    }


    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignore) {
        }
    }

    //PlaceMarker function that places the marker
    public void PlaceMarker() {
        Hw.markerServo.setPosition(-1);
        Hw.markerServo.setPosition(1);
    }

}