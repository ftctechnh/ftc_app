package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by wolfie on 2/7/16.
 */
public class GyroMotorClass {
    private LinearOpMode opMode;
    private DcMotor left;
    private DcMotor right;
    private int targetHeading;
    private GyroSensor gyro;
    private boolean running;
    private Direction currMotor;

    public GyroMotorClass(LinearOpMode opMode, GyroSensor gyro, DcMotor left, DcMotor right) {
        this.opMode = opMode;
        this.left = left;
        this.right = right;
        this.gyro = gyro;
        this.running = false;
        this.targetHeading = 0;
    }

    public void startPivotTurn(int targetHeading, double power, Direction direction) {
        // heading should be relative to current heading, pos or neg
        int currHeading = this.gyro.getHeading();
        if (targetHeading + currHeading < 0) {
            this.targetHeading = targetHeading + currHeading + 360;
        } else if (targetHeading + currHeading > 359) {
            this.targetHeading = (targetHeading + (currHeading - 360));
        } else {
            this.targetHeading = targetHeading + currHeading;
        }
        if (Direction.MOTOR_RIGHT == direction) {
            this.right.setPower(power);
            this.currMotor = Direction.MOTOR_RIGHT;
        } else {
            this.left.setPower(power);
            this.currMotor = Direction.MOTOR_LEFT;
        }
        this.running = true;
    }

    public boolean targetReached() {

        boolean reached = false;

        if (gyro != null) {
            int position = gyro.getHeading();
            //opMode.getTelemetryUtil().addData(name + ": B - Current position", position);

            //choosing within 3 ( close enough)
            if (Math.abs(position - this.targetHeading) <= 3) {
                reached = true;
            }
        }

        return reached;

    }

    public void stop() throws InterruptedException {
        running = false;
        if (this.currMotor == Direction.MOTOR_RIGHT) {
            this.right.setPower(0);
        } else if (this.currMotor == Direction.MOTOR_LEFT) {
            this.left.setPower(0);
        }
    }

    public boolean isRunning() {
        return running;
    }
}
