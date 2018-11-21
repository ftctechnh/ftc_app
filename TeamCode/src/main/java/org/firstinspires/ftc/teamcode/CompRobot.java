package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CompRobot extends BasicBot
{
    LinearOpMode linearOpMode;

    public CompRobot(HardwareMap hardwareMap)
    {
        super(hardwareMap);
    }

    public CompRobot(HardwareMap hardwareMap, LinearOpMode linearOpMode_In)
    {
        super(hardwareMap);
        linearOpMode = linearOpMode_In;
    }

    public void driveStraight(float dist_In, float pow)
    {
        super.resetEncoders();
       if (dist_In > 0)
        {
            super.goForward(pow, pow);
        }
        else
        {
            super.goBackwards(pow, pow);
        }

        dist_In = Math.abs(dist_In);


        while (Math.abs(super.getDriveLeftOne().getCurrentPosition()) < dist_In && Math.abs(super.getDriveRightOne().getCurrentPosition()) < dist_In && !linearOpMode.isStopRequested())
        {

        }
        super.stopDriveMotors();
    }

    public void pivotenc(float degrees, float pow)
    {
        pow = Math.abs(pow);
        super.resetEncoders();
        if (degrees > 0)
        {
            super.driveMotors(-pow, pow);
        }
        else
        {
            super.driveMotors(pow, -pow);
        }

        degrees = Math.abs(degrees);

        while (Math.abs(super.getDriveLeftOne().getCurrentPosition()) < degrees && Math.abs(super.getDriveRightOne().getCurrentPosition()) < degrees  && !linearOpMode.isStopRequested())
        {

        }
        super.stopDriveMotors();
    }
}
