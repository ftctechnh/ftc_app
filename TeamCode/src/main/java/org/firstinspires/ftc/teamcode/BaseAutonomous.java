package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Benla on 10/14/2018.
 */

@Disabled
public abstract class BaseAutonomous extends RobotsBase
{
    public abstract void DriveTheRobot ();

    @Override
    public void DefineOpMode ()
    {
        DriveTheRobot();
    }

}
