package com.qualcomm.ftcrobotcontroller.codelib;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by JackV on 9/20/15.
 */
public class ArcadeDrive {
    DcMotor motorRight, motorLeft;
    public ArcadeDrive(DcMotor motorRight, DcMotor motorLeft) {
        this.motorRight = motorRight;
        this.motorLeft = motorLeft;
    }

    public void arcadeDrive(float y, float x) {
        float max = Math.abs(x);
        if (Math.abs(y) > max)
            max = Math.abs(y);
        float sum = y + x;
        float dif = y - x;
        if(y <= 0) {
            if(x >= 0) {
                motorLeft.setPower(max);
                motorRight.setPower(-sum);
            } else {
                motorLeft.setPower(dif);
                motorRight.setPower(max);
            }
        } else {
            if(y >= 0) {
                motorLeft.setPower(dif);
                motorRight.setPower(-max);
            } else {
                motorLeft.setPower(-max);
                motorRight.setPower(-sum);
            }
        }
    }
}
