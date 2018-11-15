package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Benla on 11/14/2018.
 */

@Autonomous(group = "Sprockets", name = "Basic Right")
public class AutoHangRight extends RobotsBase
{
    public void DefineOpMode ()
    {
        waitForStart();

        OffTheLander();

        TurnRightDegrees(AutonomousBaseSpeed, 45);

        DriveForwardsDistance(AutonomousBaseSpeed, 90);

        TurnLeftDegrees(AutonomousBaseSpeed, 90);

        DriveForwardsDistance(AutonomousBaseSpeed, 60);

        DropMarker();

        DriveBackwards(AutonomousBaseSpeed, 84);
    }
}
