package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by gstaats on 18/09/17.
 */

public class Drive {
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;

    // function to move forward
    public void forward( double power, double negativepower )
    {
        frontLeft.setPower(negativepower);
        rearLeft.setPower(power);
        frontRight.setPower(negativepower);
        rearRight.setPower(power);
    }

    // function to move backward
    public void backward(double negativepower, double power)
    {
        frontLeft.setPower(negativepower);
        frontRight.setPower(power);
        rearRight.setPower(power);
        rearLeft.setPower(negativepower);

    }

    // function to move left (not turn!)
    public void left(double power, double negativepower )
    {
      frontLeft.setPower(negativepower);
      rearLeft.setPower(power);
      rearRight.setPower(power);
      frontRight.setPower(negativepower);

    }

    // function to move right (not turn!)
    public void right(double power, double negativepower)
    {
      frontLeft.setPower(power);
        rearLeft.setPower(negativepower);
        rearRight.setPower(negativepower);
        frontRight.setPower(power);
    }

    // function to move diagonal
    public void diagonal(double power1, double power2)
    {
        frontLeft.setPower(power1);
        rearRight.setPower(power1);
        rearRight.setPower(-power2);
        rearLeft.setPower(-power2);
    }

    // function to turn
    public void turn( double signedSpeed )
    {
        if ( signedSpeed <= 0.0 )
        {
            // when speed is < 0, rotate CCW. To rotate CCW
            // set ..... <this is not correct, yet>
            double power = -1.0 * signedSpeed;

            frontLeft.setPower(signedSpeed);
            rearLeft.setPower(power);
            frontRight.setPower(power);
            rearRight.setPower(signedSpeed);
        }
    }
//assuming that we are turning in one direction
    //this turns  counterclockwise
}

