package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 11/25/2017.
 */

public class AutoDrive {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private GyroSensor gyro;
    private double cir = 3.937 * Math.PI;
    private int CPR = 1120; //Clicks per rotation of the encoder with the NeveRest motors. Please do not edit...

    public AutoDrive(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight, GyroSensor gyro) {
        this.FrontLeft = FrontLeft;
        this.FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.FrontRight = FrontRight;
        this.RearLeft = RearLeft;
        this.RearLeft.setDirection(DcMotor.Direction.REVERSE);
        this.RearRight = RearRight;
        this.gyro = gyro;
    }

    public void driveTranslateRotate(double x, double y, double z, double distance) {
        resetEncoders();
        double clicks = Math.abs(distance * CPR/cir);
        double fl = clip(y + -x + z);
        double fr = clip(y + x - z);
        double rl = clip(y + x + z);
        double rr = clip(y + -x - z);
        double[] list = {fl, fr, rl, rr};
        double high = findHigh(list);
        driveSpeeds(fl, fr, rl, rr);
        while (!(isMotorAtTarget(FrontLeft, fl / high * clicks)) && (!(isMotorAtTarget(FrontRight, fr / high * clicks))) && (!(isMotorAtTarget(RearLeft, rl / high * clicks))) && (!(isMotorAtTarget(RearRight, rr / high * clicks)))) {

        }
        stopMotors();
    }


    public void driveSpeeds(double flSpeed, double frSpeed, double rlSpeed, double rrSpeed) {
        FrontLeft.setPower(clip(flSpeed));
        FrontRight.setPower(clip(frSpeed));
        RearLeft.setPower(clip(rlSpeed));
        RearRight.setPower(clip(rrSpeed));
    }

    public double clip(double value) {
        return Range.clip(value, -1, 1);
    }

    public double findHigh(double[] nums) {
        double high = 0;
        int i = 0;
        while (i < nums.length) {
            if (Math.abs(nums[i]) > high) {
                high = Math.abs(nums[i]);
            }
            i++;
        }
        return high;
    }

    public void resetEncoders() {
        FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void stopMotors() {
        driveSpeeds(0, 0, 0, 0);
    }

    public int getCurrentPosition(DcMotor motor) {
        return motor.getCurrentPosition();
    }

    public boolean isMotorAtTarget(DcMotor motor, double target) {
        return Math.abs(getCurrentPosition(motor)) >= Math.abs(target);
    }
    public void rightGyro (double x, double y, double z, double degrees) {
        double heading = gyro.getHeading();
        double fl = clip(y + -x + z);
        double fr = clip(y + x - z);
        double rl = clip(y + x + z);
        double rr = clip(y + -x - z);
        double[] list = {fl, fr, rl, rr};
        double high = findHigh(list);
        driveSpeeds(fl, fr, rl, rr);
        while (heading < degrees) {
            heading = gyro.getHeading();
        }
        stopMotors();
    }
    public void leftGyro (double x, double y, double z, double degrees) {
        double heading = gyro.getHeading();
        double fl = clip(y + -x + z);
        double fr = clip(y + x - z);
        double rl = clip(y + x + z);
        double rr = clip(y + -x - z);
        double[] list = {fl, fr, rl, rr};
        double high = findHigh(list);
        driveSpeeds(fl, fr, rl, rr);
        while (heading > degrees) {
            heading = gyro.getHeading();
        }
        stopMotors();
    }
}
