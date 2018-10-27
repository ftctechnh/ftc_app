package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.Motors.DriveMotor;

public class DriveSystem4Wheel extends System {

    public DcMotor motorFrontLeft;
    public DcMotor motorFrontRight;
    public DcMotor motorBackLeft;
    public DcMotor motorBackRight;

    public DriveSystem4Wheel(OpMode opMode, String systemName) {
        super(opMode, "MecanumDrive");

        this.motorFrontLeft = map.dcMotor.get("motorFL"/*config.getString("motorFL")*/);
        this.motorFrontRight = map.dcMotor.get("motorFR"/*config.getString("motorFR")*/);
        this.motorBackRight = map.dcMotor.get("motorBR"/*config.getString("motorBR")*/);
        this.motorBackLeft = map.dcMotor.get("motorBL"/*config.getString("motorBL")*/);

        this.motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        this.motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        this.motorBackRight.setDirection(DcMotor.Direction.FORWARD);

        this.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // Set all drive motors to zero power
        setPower(0);
    }

    public void setPower(double power) {
        this.motorFrontLeft.setPower(power);
        this.motorFrontRight.setPower(power);
        this.motorBackLeft.setPower(power);
        this.motorBackRight.setPower(power);
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
