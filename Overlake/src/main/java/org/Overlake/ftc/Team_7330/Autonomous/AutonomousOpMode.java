package org.overlake.ftc.team_7330.Autonomous;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * Created by jacks on 11/13/2015.
 */
public abstract class AutonomousOpMode extends SynchronousOpMode {

    final int encRotation = 1120;

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
        double leftMotorPower = motorBackLeft.getPower();     //should these have more scope? so they aren't reset every time ?
        double rightMotorPower = motorBackRight.getPower();
        double increment = .005;

        if (isColor(c, sensorSide))
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

    void driveWithEncoders(double revolutions, double power) throws InterruptedException
    {
        // How far are we to move, in ticks instead of revolutions?
        int denc = (int)Math.round(revolutions * encRotation);

        int frontLeftTarget = this.motorFrontLeft.getCurrentPosition() + denc;
        int frontRightTarget = this.motorFrontRight.getCurrentPosition() + denc;
        int backLeftTarget = this.motorBackLeft.getCurrentPosition() + denc;
        int backRightTarget = this.motorBackRight.getCurrentPosition() + denc;

        // Tell the motors where we are going
        this.motorFrontLeft.setTargetPosition(frontLeftTarget);
        this.motorFrontRight.setTargetPosition(frontRightTarget);
        this.motorBackLeft.setTargetPosition(backLeftTarget);
        this.motorBackRight.setTargetPosition(backRightTarget);

        // Set them a-going
        this.motorFrontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorFrontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        this.motorBackRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Wait until they are done
        while (this.motorFrontLeft.isBusy() && this.motorFrontRight.isBusy() && this.motorBackRight.isBusy() && this.motorBackLeft.isBusy())
        {
            telemetry.update();
            this.idle();

            int minDistance = frontLeftTarget - this.motorFrontLeft.getCurrentPosition();
            minDistance = Math.min(minDistance, frontRightTarget - this.motorFrontRight.getCurrentPosition());
            minDistance = Math.min(minDistance, backLeftTarget - this.motorBackLeft.getCurrentPosition());
            minDistance = Math.min(minDistance, backRightTarget - this.motorBackRight.getCurrentPosition());

            double scaledPower = getPower(power, minDistance);

            this.motorFrontLeft.setPower(scaledPower);
            this.motorFrontRight.setPower(scaledPower);
            this.motorBackLeft.setPower(scaledPower);
            this.motorBackRight.setPower(scaledPower);
        }

        // Now that we've arrived, kill the motors so they don't just sit there buzzing
        this.motorFrontLeft.setPower(0);
        this.motorFrontRight.setPower(0);
        this.motorBackLeft.setPower(0);
        this.motorBackRight.setPower(0);

        // Always leave the screen looking pretty
        telemetry.updateNow();
    }

    double getPower(double power, int distance)
    {
        return power * (((Math.min((double)distance, (double)encRotation) / (double)encRotation) * 0.9) + 0.1);
    }
}
