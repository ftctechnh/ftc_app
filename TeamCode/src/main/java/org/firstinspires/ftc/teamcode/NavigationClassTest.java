package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy Yao on 11/13/2016.
 */
@Autonomous(name = "NavigationClassTest", group = "Concept")
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
        telemetry.addData("telemetry is wpromg ", null);
        telemetry.update();
        robot.driveStraight(61, 130);
        telemetry.addData("Drive 61 in at 130 deg" , null);
        telemetry.update();
        telemetry.addData("Check to see if can see target", null);
        telemetry.update();

        for(short i = 0; i < 200; i++)
        {
            navigator.visionTrack();
            if (navigator.isDetectingTarget())
            {
                telemetry.addData("Can see target", null);
                telemetry.update();
                targetDetected = true;
                break;
            }
            sleep(10);
        }
        /*while (true)
        {
            if (targetDetected == true)
            {
                telemetry.addData("Difference in (x) (in):" + (2134-navigator.currentX) * 0.0393701, null);
                telemetry.addData("Difference in y (in)" + (203-navigator.getcurrentY) * 0.0393701, null);
            }
            sleep(100);
            telemetry.update();
        }*/
       if (!targetDetected)
       {
           navigator.setRobotLocation(84, 24, 0);
       }
        telemetry.addData("Current X", navigator.returnCurrentX());
        telemetry.addData("Current Y", navigator.returnCurrentY());
        telemetry.addData("Drive X", 84-navigator.returnCurrentX());
        telemetry.addData("Drive Y", 13-navigator.returnCurrentY());
        telemetry.update();
        sleep(10000);
        //navigator.moveToPosition(84, 13, 90); //only spins
        //orientation is -90 on phone, robot is

        /*robot.driveStraight(84 - navigator.returnCurrentX(), -180);
        robot.driveStraight(24 - navigator.returnCurrentY(), 90); //robot is now at beacon*/

       /*
        idle(); //idle essentially waits for software to catch up with hardware
        if (!targetDetected) //assume location
        {
            navigator.setRobotLocation((24*3)+12, 8, 0);
            telemetry.addData("Setting robot location at 2134, 305; angle 0",null);
        }
        telemetry.addData("moveToPosition(914, 150, -90)", null);
        navigator.moveToPosition(2362, 457, 90);

        navigator.moveToPosition(2134, 305, 0);
        */
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
