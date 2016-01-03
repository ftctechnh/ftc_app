package com.qualcomm.ftcrobotcontroller.opmodes.drive;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This class provides access to a Tesla-style four-motor drive.
 */

public class Tesla extends Drive {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;

    public void setRearMultiplier(double rearMultiplier) {
        this.rearMultiplier = rearMultiplier;
    }

    private double rearMultiplier=1.0;

    public Tesla(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight) {
        this.frontLeft  = frontLeft;
        this.frontRight = frontRight;
        this.rearLeft   = rearLeft;
        this.rearRight  = rearRight;
    }

    public void driveRaw(double left, double right) {
        //Raw (LR Direct) drive.
        left = DriveMath.limit(left, -1, 1);
        right= DriveMath.limit(right,-1, 1);
        this.frontLeft.setPower(-left);                     //Left values are negated because the left motors face the
        this.rearLeft.setPower(-left*this.rearMultiplier);  //opposite direction and thus need to move in reverse.
        this.frontRight.setPower(right);
        this.rearRight.setPower(right*this.rearMultiplier);
    }

    public void driveStd(double driveRate, double turnRate) {
        //Standard drive.
        driveRate = DriveMath.limit(driveRate,-1,1);
        turnRate  = DriveMath.limit(turnRate, -1,1);
        double left = driveRate + turnRate;
        double right= driveRate - turnRate;
        this.driveRaw(left, right);
    }

    public void driveMtn(double driveRate, double turnRate) {
        //Mountain drive.
        driveRate = DriveMath.limit(driveRate,-1,1);
        turnRate  = DriveMath.limit(turnRate, -1,1);
        double left = driveRate;
        double right = driveRate;
        if (turnRate>0) left += turnRate;
        if (turnRate<0) right-= turnRate; //Apply positive turn _only_.
        this.driveRaw(left,right);
    }
}
