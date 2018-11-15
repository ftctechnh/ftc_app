package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Benla on 11/14/2018.
 */

@Autonomous(group = "Sprockets", name = "Basic Left")
public class AutoHangLeft extends RobotsBase
{
    public void DefineOpMode ()
    {
        waitForStart();

        OffTheLander();

        TurnLeftDegrees(AutonomousBaseSpeed, 45);

        DriveForwardsDistance(AutonomousBaseSpeed, 90);

        TurnRightDegrees(AutonomousBaseSpeed, 90);

        DriveForwardsDistance(AutonomousBaseSpeed, 60);

        DropMarker();

        DriveBackwards(AutonomousBaseSpeed, 84);
    }
}
