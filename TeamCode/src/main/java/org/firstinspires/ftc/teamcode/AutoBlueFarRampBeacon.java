package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
Made by jeremy, needs testing with beacons!!!!!!!!!
 */
@Autonomous(name = "AutoBlueFarRampBeacon", group = "Autonomous")
public class AutoBlueFarRampBeacon extends LinearOpMode
{
    //assumes that front omni wheels is on the bottom edge of the 2nd tle from the corner of the alliance splitter, meaning it's farther from alliance's robot from the ramp
    private OmniDriveBot robot = new OmniDriveBot();
    public void runOpMode() throws InterruptedException
    {
        robot.init(hardwareMap);

        waitForStart();

        robot.driveStraight(103, -153);
        sleep(100);
        robot.spin(-90);
        sleep(100);
        /* test if color sensors can get beacon color; basically test code
        telemetry.addData("BLUE:", robot.getSensorBlue());
        telemetry.addData("RED:", robot.getSensorRed());
        telemetry.addData("Seeing RED", robot.isDetectingRed());
        telemetry.addData("Seeing BLUE", robot.isDetectingBlue());
        telemetry.addData("HUE", robot.getSensorHue());
        sleep(10000);\
        */


        //assumes robot is on the left side of the beacon from the side with battery
        if(robot.isDetectingRed() && !robot.isDetectingBlue())
        {
            //robot drive at 90 deg, press button
            robot.driveStraight(8, -90);
            //robot goes back to area before, go back to position before, -90 deg
            robot.driveStraight(8,90);
        }
        else if (robot.isDetectingBlue()) //robot goes to the left, press beacon, go back to position on y plane, go back to original position on x
        {
            robot.driveStraight(6, 0);
            robot.driveStraight(8, -90);
            robot.driveStraight(8,90);
            robot.driveStraight(6,180);
        }

        //Next beacon,
        sleep(100);
        robot.driveStraight(48, 180);
        sleep(100);

        //Same logic as before, the robot's color sensor is assumed on the left side(relative to the side with the battery)
        if(robot.isDetectingRed() && !robot.isDetectingBlue())
        {
            //robot drive at 90 deg, press button
            robot.driveStraight(8, -90);
            //robot goes back to area before, go back to position before, -90 deg
            robot.driveStraight(8,90);
        }
        else if (robot.isDetectingBlue())
        {
            robot.driveStraight(6, 0);
            robot.driveStraight(8, -90);
            robot.driveStraight(8,90);
            robot.driveStraight(6,180);
        }
    }
}
