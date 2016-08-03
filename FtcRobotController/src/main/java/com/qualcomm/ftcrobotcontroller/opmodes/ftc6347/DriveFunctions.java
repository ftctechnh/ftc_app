package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by FTCGearedUP on 1/13/2016.
 */
public class DriveFunctions {
    double offset;
    DcMotor motor1;
    DcMotor motor2;
    HardwareMap hardwareMap;
    Telemetry tl;
    public enum DIRECTION { FORWARD(1), REVERSE(-1), STOP(0);
        private int value; private DIRECTION(int value) { this.value = value; }
    };



    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public DriveFunctions(double offset, HardwareMap hardwareMap, Telemetry _tl) {
        this.motor1 = hardwareMap.dcMotor.get("1");
        this.motor2 = hardwareMap.dcMotor.get("2");
        this.motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.motor2.setDirection(DcMotor.Direction.REVERSE);
        this.hardwareMap = hardwareMap;
        this.offset = offset;  //offset to make the robot drive straight
        this.tl = _tl; //to hold a local copy of the telemetry object
    }

    /**
     * drive
     *
     * @param time
     * @param direction 1 = forward, -1 = backward, 0 = stop
     * @throws InterruptedException
     */
    void drive(int time, DIRECTION direction) throws InterruptedException {
        double m1power = 0.5 * direction.value + offset;
        double m2power = 0.5 * direction.value;
        motor1.setPower(m1power);
        motor2.setPower(m2power);
        tl.addData("Motor 1 Power", m1power);
        tl.addData("Motor 2 Power", m2power);
        sleep(time);
    }



    void slowDrive(int time,DIRECTION direction) throws InterruptedException {

        motor1.setPower(0.25 * direction.value + offset);
        motor2.setPower(0.25 * direction.value);

        sleep(time);
    }

    /**
     * turn robot
     *
     * @param angle -1=right 1=left
     * @param time  in milliseconds
     * @throws InterruptedException 600 ms = 45 degree turn, 1200 ms = 90 degree turn
     */
    void turn(float angle, int time) throws InterruptedException {

        motor1.setPower(0.5 * angle + offset);
        motor2.setPower(0.5 * -angle);

        sleep(time);

        motor1.setPower(0);
        motor2.setPower(0);
    }

    public void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }


}
