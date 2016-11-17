package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Jeremy Yao on 11/13/2016.
 */
@Autonomous(name = "TestNavigationClass", group = "Concept")
public class NavigationClassTest extends LinearOpMode {
    OmniDriveBot robot = new OmniDriveBot();
    FieldNavigator navigator = new FieldNavigator();

    public void runOpMode() throws InterruptedException
    {
        double maxDistance = 5.6 * 12, distanceTravelled = 0;
        navigator.setupVuforia();
        robot.init(hardwareMap);

        waitForStart();
        robot.driveStraight(48, 120);

        if (navigator.canSeeTarget())
        {
            for(short i = 0; i < 10000; i++)
                navigator.visionTrack();
        }

        idle(); //idle essentially waits for software to catch up with hardware
        if (navigator.lastKnownLocation != null)
        {
            navigator.moveToPosition(305, 914, -90);
        }

        /*
        while(opModeIsActive())
        {
            while (!navigator.canSeeTarget())
            {
                robot.driveStraight(48, 120);
                distanceTravelled+= 48;
                if (distanceTravelled >= maxDistance)
                {
                    break;
                }
                break;
            }

        }
        */

    }



}
