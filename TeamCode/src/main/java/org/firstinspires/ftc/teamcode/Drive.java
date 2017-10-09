package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by gstaats on 18/09/17.
 */

public class Drive
{
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;
    HardwareMap hwMap = null;

    public Drive(DcMotor FL, DcMotor FR, DcMotor RL, DcMotor RR )
    {
        // Define and Initialize Motors

        frontRight = FR;
        frontLeft  = FL;
        rearRight  = RR;
        rearLeft   = RL;

        // Set direction so positive is always forward with respect to
        // the robot. Right side motors need to be set to reverse, because
        // they spin counter-clockwise to move the robot forward.
        frontRight.setDirection( DcMotor.Direction.FORWARD );
        rearRight.setDirection( DcMotor.Direction.FORWARD );
        frontLeft.setDirection( DcMotor.Direction.REVERSE );
        rearLeft.setDirection( DcMotor.Direction.REVERSE );

        // Set all motors to zero power. Don't want robot moving till
        // we're ready.
        frontLeft.setPower( 0 );
        frontRight.setPower( 0 );
        rearLeft.setPower( 0 );
        rearRight.setPower( 0 );

        // Set all motors to run with encoders.

        frontLeft.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER );
        frontRight.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER );
        rearLeft.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER );
        rearRight.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER );

        // Set motors to not brake
        frontLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
        frontRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
        rearLeft.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );
        rearRight.setZeroPowerBehavior( DcMotor.ZeroPowerBehavior.FLOAT );

    }
    public Drive( HardwareMap ahwMap )
    {
        hwMap = ahwMap;

        // Define and Initialize Motors

        frontRight = hwMap.dcMotor.get("front_right");
        frontLeft  = hwMap.dcMotor.get("front_left");
        rearRight  = hwMap.dcMotor.get("rear_right");
        rearLeft   = hwMap.dcMotor.get("rear_left");

        // Set direction so positive is always forward with respect to
        // the robot. Right side motors need to be set to reverse, because
        // they spin counter-clockwise to move the robot forward.
        frontRight.setDirection( DcMotor.Direction.FORWARD );
        rearRight.setDirection( DcMotor.Direction.FORWARD );
        frontLeft.setDirection( DcMotor.Direction.REVERSE );
        rearLeft.setDirection( DcMotor.Direction.REVERSE );

        // Set all motors to zero power. Don't want robot moving till
        // we're ready.
        frontLeft.setPower( 0 );
        frontRight.setPower( 0 );
        rearLeft.setPower( 0 );
        rearRight.setPower( 0 );

        // Set all motors to run with encoders.

        frontLeft.setMode( DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
        rearLeft.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
        rearRight.setMode( DcMotor.RunMode.RUN_USING_ENCODER );
    }

    /*
     * Use X (-1 full left, +1 full right) and Y (-1 backwards, +1 forwards)
     * from the joystick input to determine power settings for each wheel.
     * In order to do this, the X & Y inputs are translated from Cartesian coordinates
     * to polar coordinates. This gives a direction in terms of an angle and a
     * speed in terms of a magnitude. These values can be used with standard formulas
     * found on-line to translate into speeds for each of the wheels.
     *
     */
    public void VectorMove( double xLeftRight, double yFwdRev, double rotate )
    {
        // Create polar coordinate variables theta and magnitude for the angle and magnitude.
        // During creation, calculate the values from the input.
        double magnitude = Math.sqrt( ( xLeftRight * xLeftRight ) + ( yFwdRev * yFwdRev ) ); //pythagorean theory
        double theta = Math.atan2( yFwdRev, xLeftRight ) - ( Math.PI / 4.0 );

        // calculate powers for wheel motors
        double frontLeftPower = magnitude * Math.sin( theta ) + rotate;
        double frontRightPower = magnitude * Math.cos( theta ) - rotate;
        double rearLeftPower = magnitude * Math.cos( theta ) + rotate;
        double rearRightPower = magnitude * Math.sin( theta ) - rotate;

        // Powers can be > 1 using above equations, so scale if they are
        double biggestFront = Math.max( Math.abs( frontLeftPower ), Math.abs( frontRightPower ) );
        double biggestRear = Math.max( Math.abs( rearLeftPower ), Math.abs( rearRightPower ) );
        double biggest = Math.max( biggestFront, biggestFront );

        if ( biggest > 1.0 )
        {
            frontLeftPower = frontLeftPower / biggest;
            frontRightPower = frontRightPower / biggest;
            rearLeftPower = rearLeftPower / biggest;
            rearRightPower = rearRightPower / biggest;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        rearLeft.setPower(rearLeftPower);
        rearRight.setPower(rearRightPower);

    }

    /*
     *
     */
    public void MoveSimple( double xLeftRight, double yFwdRev, double rotate )
    {
        // variables for calculating wheel powers
        double frontLeftPower = ( -1.0 * xLeftRight ) + ( 1.0 * yFwdRev ) + ( 1.0 * rotate );
        double frontRightPower = ( 1.0 * xLeftRight ) + (1.0 * yFwdRev ) + (-1.0 * rotate);
        double rearLeftPower = xLeftRight + yFwdRev + rotate;
        double rearRightPower = (xLeftRight * -1.0) + ( yFwdRev * 1.0) + (-1.0 * rotate);


        // Powers can be > 1 using above equations, so scale if they are
        double biggestFront = Math.max( Math.abs( frontLeftPower ), Math.abs( frontRightPower ) );
        double biggestRear = Math.max( Math.abs( rearLeftPower ), Math.abs( rearRightPower ) );
        double biggest = Math.max( biggestFront, biggestFront );

        if ( biggest > 1.0 )
        {
            frontLeftPower = frontLeftPower / biggest;
            frontRightPower = frontRightPower / biggest;
            rearLeftPower = rearLeftPower / biggest;
            rearRightPower = rearRightPower / biggest;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        rearLeft.setPower(rearLeftPower);
        rearRight.setPower(rearRightPower);

    }

}

