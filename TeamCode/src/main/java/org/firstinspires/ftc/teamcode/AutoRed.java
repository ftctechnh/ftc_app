package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Benla on 10/14/2018.
 */

@Autonomous (group = "Sprockets", name = "AutoRed")
public class AutoRed extends BaseAutonomous
{
    public float MidX = 100;
    public float MidY = 100;

    public float BaseX = 100;
    public float BaseY = 100;

    public float CraterX = 100;
    public float CraterY = 100;

    @Override
    public void DriveTheRobot()
    {
        GoToDesiredPosition(MidX, MidY, AutonomousBaseSpeed);

        GoToDesiredPosition(BaseX, BaseY, AutonomousBaseSpeed);

        ArmsDown();

        DropMarker();

        ArmsUp();

        GoToDesiredPosition(CraterX, CraterY, AutonomousBaseSpeed);
    }
}
