package org.firstinspires.ftc.teamcode.systems.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveSystem4Wheel extends System
{

    public DcMotor motorFrontLeft;
    public DcMotor motorFrontRight;
    public DcMotor motorBackLeft;
    public DcMotor motorBackRight;

    public DriveSystem4Wheel(OpMode opMode, String systemName) {
        super(opMode, "DriveSystem4Wheel");

        this.motorFrontLeft = hardwareMap.dcMotor.get("motorFL");
        this.motorFrontRight = hardwareMap.dcMotor.get("motorFR");
        this.motorBackRight = hardwareMap.dcMotor.get("motorBR");
        this.motorBackLeft = hardwareMap.dcMotor.get("motorBL");

        setDirection(DriveDirection.FORWARD);

        this.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Set all drive motors to zero power
        setPower(0);
    }

    public void setPower(double power) {
        this.motorFrontLeft.setPower(power);
        this.motorFrontRight.setPower(power);
        this.motorBackLeft.setPower(power);
        this.motorBackRight.setPower(power);
    }

    public boolean anyMotorsBusy()
    {
        return motorFrontLeft.isBusy() ||
                motorFrontRight.isBusy() ||
                motorBackLeft.isBusy() ||
                motorBackRight.isBusy();
    }

    public void setTargetPosition(int ticks)
    {
        motorBackLeft.setTargetPosition(motorBackLeft.getCurrentPosition() + ticks);
        motorBackRight.setTargetPosition(motorBackRight.getCurrentPosition() + ticks);
        motorFrontLeft.setTargetPosition(motorFrontLeft.getCurrentPosition() + ticks);
        motorFrontRight.setTargetPosition(motorFrontRight.getCurrentPosition() + ticks);
    }

    public void setRunMode(DcMotor.RunMode runMode)
    {
        // lick left kneecap daddy pimple
        motorFrontLeft.setMode(runMode);
        motorFrontRight.setMode(runMode);
        motorBackLeft.setMode(runMode);
        motorBackRight.setMode(runMode);
    }

    public void tankDrive(double leftPower, double rightPower) {
        this.motorFrontLeft.setPower(-leftPower);
        this.motorBackLeft.setPower(leftPower);
        this.motorFrontRight.setPower(rightPower);
        this.motorBackRight.setPower(-rightPower);
    }

    public void setDirection(DriveDirection direction) {
        switch (direction){
            case FORWARD:
                motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
                motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
                motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
                motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case BACKWARD:
                motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
                motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
                motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
            case ALL_FORWARD:
                motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
                motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
                motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
        }
    }

    public enum DriveDirection {
        FORWARD, BACKWARD, ALL_FORWARD;
    }
}
