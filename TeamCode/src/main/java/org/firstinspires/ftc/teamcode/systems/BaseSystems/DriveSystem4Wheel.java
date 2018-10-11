package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.Motors.DriveMotor;

public class DriveSystem4Wheel extends System {

    public DriveMotor motorFrontLeft;
    public DriveMotor motorFrontRight;
    public DriveMotor motorBackLeft;
    public DriveMotor motorBackRight;

    public DriveSystem4Wheel(OpMode opMode, String systemName) {
        super(opMode, "MecanumDrive");

        this.motorFrontLeft = (DriveMotor) map.dcMotor.get("motorFL"/*config.getString("motorFL")*/);
        this.motorFrontRight = (DriveMotor) map.dcMotor.get("motorFR"/*config.getString("motorFR")*/);
        this.motorBackRight = (DriveMotor) map.dcMotor.get("motorBR"/*config.getString("motorBR")*/);
        this.motorBackLeft = (DriveMotor) map.dcMotor.get("motorBL"/*config.getString("motorBL")*/);

        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        this.motorBackRight.setDirection(DcMotor.Direction.FORWARD);

        // Set all drive motors to zero power
        setPower(0);
    }

    public void setPower(double power) {
        this.motorFrontLeft.run(power);
        this.motorFrontRight.run(power);
        this.motorBackLeft.run(power);
        this.motorBackRight.run(power);
    }

    public void setDirection(DcMotorSimple.Direction direction)
    {
        motorFrontLeft.setDirection(direction);
        motorFrontRight.setDirection(direction);
        motorBackLeft.setDirection(direction);
        motorBackRight.setDirection(direction);
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
        motorBackLeft.setTargetPosition(ticks);
        motorBackRight.setTargetPosition(ticks);
        motorFrontLeft.setTargetPosition(ticks);
        motorFrontRight.setTargetPosition(ticks);
    }

    public void setRunMode(DcMotor.RunMode runMode)
    {
        motorFrontLeft.setMode(runMode);
        motorFrontRight.setMode(runMode);
        motorBackLeft.setMode(runMode);
        motorBackRight.setMode(runMode);
    }
}
