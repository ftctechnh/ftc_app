package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by makiah on 9/27/16.
 */

@Autonomous(name="Tank Mode", group="Tank Programs")

public class Tank extends AutonomousBase
{
    DcMotor leftMotor, rightMotor;
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        leftMotor = Initialize(DcMotor.class, "Left Motor");
        rightMotor = Initialize(DcMotor.class, "Right Motor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        PlayAudio(DownloadedSongs.values()[(int) (Math.random() * DownloadedSongs.values().length)]);
    }

    protected void driverStationSaysGO() throws InterruptedException
    {
        boolean backwards = false;
        float left, right;
        //Keep looping while opmode is active (waiting a hardware cycle after all of this is completed, just like loop()).
        while (true)
        {
            /******************** DRIVING CONTROL ********************/

            //Driving toggle
            if (backwards)
            { // Driving backwards
                left = -gamepad1.right_stick_y;
                right = -gamepad1.left_stick_y;
            } else
            { // Driving forwards
                left = gamepad1.left_stick_y;
                right = gamepad1.right_stick_y;
            }

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // Write the values to the motors.  Scale the robot in order to run the robot more effectively at slower speeds.
            leftMotor.setPower(scaleInput(left));
            rightMotor.setPower(scaleInput(right));

            //Wait a second before switching to backwards again (can only toggle once every second).
            if (gamepad1.a && (System.currentTimeMillis() - startTime) > 1000)
            {
                backwards = !backwards; // Switch driving direction
                startTime = System.currentTimeMillis();
            }
            idle();
        }
    }

    @Override
    protected void driverStationSaysSTOP()
    {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }
}
