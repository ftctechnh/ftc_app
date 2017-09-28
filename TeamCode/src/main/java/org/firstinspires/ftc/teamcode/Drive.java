package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by gstaats on 18/09/17.
 */

public class Drive
{
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;

    // function to move forward
    public void forward( double power )
    {
        frontLeft.setPower(-power);
        rearLeft.setPower(power);
        frontRight.setPower(-power);
        rearRight.setPower(power);
    }

    // function to move forward
    public void forward( )
    {
        frontLeft.setPower(1);
        frontRight.setPower(-1);
        rearLeft.setPower(1);
        rearRight.setPower(-1);
    }


    // function to move backward
    public void backward( double power)
    {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        rearRight.setPower(power);
        rearLeft.setPower(-power);

    }
    // function to move backward
    public void backward()
    {
        frontLeft.setPower(-1);
        frontRight.setPower(1);
        rearLeft.setPower(-1);
        rearRight.setPower(1);
    }


    // function to move left (not turn!)
    public void turn_left()
    {
        frontLeft.setPower(-1);
        frontRight.setPower(1);
        rearLeft.setPower(-1);
        rearRight.setPower(1);
    }

    // function to move right (not turn!)
    public void turn_right()
    {
        frontLeft.setPower(1);
        frontRight.setPower(-1);
        rearLeft.setPower(1);
        rearRight.setPower(-1);
    }

    // function to move diagonal 45 degrees from the front of the robot
    public void diagonal45()
    {
        frontRight.setPower(1);
        rearLeft.setPower(-1);

    }
    //function to move 225 degrees
    public void diagonal225()
    {
        frontRight.setPower(-1);
        rearLeft.setPower(1);
    }

    public void diagonal135()
    {frontLeft.setPower(-1);


        rearRight.setPower(1);

    }
    public void diagonal315()
    {frontLeft.setPower(1);

        rearRight.setPower(-1);
    }

    // function to move left (not turn!)
    public void left(double power  )
    {
      frontLeft.setPower(-power);
      rearLeft.setPower(power);
      rearRight.setPower(power);
      frontRight.setPower(-power);

    }

    // function to go left
    public void left()
    { frontLeft.setPower(1);
        frontRight.setPower(-1);
        rearLeft.setPower(-1);
        rearRight.setPower(1);
    }

    // function to move right (not turn!)
    public void right(double power)
    {
      frontLeft.setPower(power);
        rearLeft.setPower(-power);
        rearRight.setPower(-power);
        frontRight.setPower(power);
    }

    //function to go left
    public void right() {
        frontLeft.setPower(-1);
        frontRight.setPower(1);
        rearLeft.setPower(1);
        rearRight.setPower(-1);
    }

    // function to move diagonal
    public void diagonal( double power )
    {
        frontLeft.setPower( power1 );
        rearRight.setPower( power1 );
        rearRight.setPower( -power2 );
        rearLeft.setPower( -power2 );
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
}

