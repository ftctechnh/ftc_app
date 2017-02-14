package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Drive With Encoders", group = "Beta Group")

public class DriveWithEncoders extends _AutonomousBase
{
    //This value will be set a couple times to verify that we don't go too far.
    long startTime = 0;

    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForDistance (0.5, 10000);
    }

    private void pressButton() throws InterruptedException
    {
        //Determine the length to push the pusher out based on the distance from the wall.
        double extendLength = 100 * sideRangeSensor.cmUltrasonic();
        extendLength = Range.clip(extendLength, 0, 3000);
        outputNewLineToDrivers ("Extending the button pusher for " + extendLength + " ms.");

        //Run the continuous rotation servo out to press, then back in.
        rightButtonPusher.setPosition(.2);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(.8);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(.5);
    }
}