package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
public abstract class VoltageBaseAutonomous extends VoltageBase
{
    public abstract void DriveTheRobot ();

    //if using vuforia, here is the place to initialize it.  I will figure that out soon.

    @Override
    public void DefineOpMode ()
    {
        waitForStart();

        DriveTheRobot();
    }
}
