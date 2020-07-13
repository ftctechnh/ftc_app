package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Robot {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public Robot(DcMotor fL, DcMotor fR, DcMotor bL, DcMotor bR)
    {
        frontLeft = fL;
        frontRight = fR;
        backLeft = bL;
        backRight = bR;
    }

    public void Strafe(int power)
    {
        frontLeft.setPower(power);
        backRight.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
    }

    public void Stop()
    {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }


}
