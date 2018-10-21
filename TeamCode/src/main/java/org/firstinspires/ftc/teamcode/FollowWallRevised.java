package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi Thumuluri on 10/5/18.
 */
@Autonomous(name = "FollowWallRevised", group = "Auto")
public class FollowWallRevised extends LinearOpMode
{
    ParadeBot paradeBot;
    public void runOpMode()
    {
        paradeBot = new ParadeBot(hardwareMap, this);
        waitForStart();
        double distRight = paradeBot.getDistFromRight_In();
        double preferedDist = 5;
        telemetry.addData("distance right = ", distRight);
        telemetry.update();
        while (paradeBot.getDistFromFront_In() > 18)
        {
            paradeBot.driveMotorsAuto(.5f, .5f);
         if (distRight > 6)
         {
             paradeBot.pivot_IMU(-15);
             paradeBot. driveStraight_In(3);

         }
         if (distRight < 4)
         {
             paradeBot.pivot_IMU(15);
         }
        }

    }

}

