package com.qualcomm.ftcrobotcontroller.opmodes.drive;

import com.qualcomm.ftcrobotcontroller.opmodes.DriveMath;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by tdoylend on 2015-12-20.
 *
 * This class interfaces a simple LR drive configuration.
 */
public class Simplex extends Drive {

    DcMotor left;
    DcMotor right;

    public Simplex(DcMotor left, DcMotor right) {
        this.left = left;
        this.right = right;
    }

    public void driveRaw(double left, double right) {
        left  = DriveMath.limit(left, -1, 1);
        right = DriveMath.limit(right,-1, 1);
        this.left.setPower(-left); //Left has to be negated as the motor faces the opposite direction
        this.right.setPower(right);//and thus runs in reverse.
    }

    public void driveStd(double driveRate, double turnRate) {
        driveRate = DriveMath.limit(driveRate, -1, 1);
        turnRate  = DriveMath.limit(turnRate,  -1, 1);
        this.driveRaw(driveRate+turnRate,driveRate-turnRate);
    }
}
