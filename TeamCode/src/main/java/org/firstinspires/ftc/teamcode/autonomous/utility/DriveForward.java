package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;

@Autonomous(name="Drive Forward", group = "Utility Group")

public class DriveForward extends AutoBase
{
    //Custom initialization
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //Set the motor powers.
        setLeftPower(-.6);
        setRightPower(.6);
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        startDrivingAt (0.8);

        while (true)
            idle();
    }
}