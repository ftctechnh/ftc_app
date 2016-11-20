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
    FieldNavigator navigator = new FieldNavigator(robot);

    public void runOpMode() throws InterruptedException
    {
        boolean targetDetected = false;
        //double maxDistance = 5.6 * 12, distanceTravelled = 0;
        navigator.setupVuforia();
        robot.init(hardwareMap);

        waitForStart();
        robot.driveStraight(61, 130);
        telemetry.addData("Drive 61 in at 130 deg" , null);
        telemetry.addData("Check to see if can see target", null);
        for(short i = 0; i < 2000; i++)
        {
            navigator.visionTrack();
            if (navigator.lastKnownLocation != null)
            {
                telemetry.addData("Can see target", null);
                targetDetected = true;
                break;
            }
            sleep(100);
        }

        idle(); //idle essentially waits for software to catch up with hardware
        if (!targetDetected)
        {
            navigator.setRobotLocation((24*3)+12, 8, 0);
            telemetry.addData("Setting robot location at 2134, 305; angle 0",null);
        }
        telemetry.addData("moveToPosition(914, 150, -90)", null);
        navigator.moveToPosition(2362, 457, 90);

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
