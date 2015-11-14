package org.Overlake.ftc.Team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * Created by jacks on 11/13/2015.
 */
public abstract class AutonomousOpMode extends SynchronousOpMode {
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;

    ColorSensor sensorRGBLeft;
    ColorSensor sensorRGBRight;

    public enum Color { Red, Blue, White }
    public enum Side { Left, Right }

    //sensorSide refers to which colorSensor we are using, the one on the left or right side of robot
    //colorSide tells if the color of the line we are following is on the left or right

    public boolean isColor(Color c, Side sensorSide)
    {
        //if the sensor on the side from the parameter sees the color,
        return true;

        //else if this sensor doesn't see the color
        // return false;
    }

    public void followColor(Color c, Side sensorSide, Side colorSide)
    {
        double leftMotorPower = .25;     //should these have more scope? so they aren't reset every time ?
        double rightMotorPower = .25;
        double increment = .005;

        while (true)
        {
        if (isColor(c, sensorSide) == true)
        {
            if (colorSide == Side.Left)      //if color side is left, veer right
            {
                leftMotorPower += increment;
                rightMotorPower -= increment;
            }
            else                        //if color side is right, veer left
            {
                leftMotorPower -= increment;
                rightMotorPower += increment;
            }
        }
        else
        {
            //the opposite of above, so the robot turns towards the colored line
            if (colorSide == Side.Left)      //if color side is left, veer left to find line
            {
                leftMotorPower -= increment;
                rightMotorPower += increment;
            }
            else                        //if color side is right, veer right to find line
            {
                leftMotorPower += increment;
                rightMotorPower -= increment;
            }
        }

            motorFrontRight.setPower(rightMotorPower);
            motorBackRight.setPower(rightMotorPower);
            motorFrontLeft.setPower(leftMotorPower);
            motorBackLeft.setPower(leftMotorPower);

            //drive forward a bit
    }
}
