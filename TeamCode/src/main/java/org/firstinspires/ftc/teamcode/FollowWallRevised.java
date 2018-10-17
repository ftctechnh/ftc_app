package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi Thumuluri on 10/5/18.
 */
@Autonomous(name = "FollowWallRevised", group = "Auto")
public class FollowWallRevised extends LinearOpMode
{
    private ParadeBot paradeBot;

    public void runOpMode()
    {
        paradeBot = new ParadeBot(hardwareMap, this);
        double distRight = paradeBot.getDistFromRight_In();
        telemetry.addData("distance right = ", distRight);
        telemetry.update();
        waitForStart();

        final int CENTER = 0;
        final int RIGHT = -1;
        final int LEFT = 1;

        int currentDirection = CENTER;


        while (paradeBot.getDistFromFront_In() > 18)
        {
            telemetry.addData("front", paradeBot.getDistFromFront_In());
            telemetry.addData("right", paradeBot.getDistFromRight_In());
            telemetry.update();

            if (paradeBot.getDistFromRight_In() > 6 && currentDirection != RIGHT)
            {
                paradeBot.pivot_IMU(-10);
                currentDirection = RIGHT;
            }
            else if (paradeBot.getDistFromRight_In() < 4 && currentDirection != LEFT)
            {
                paradeBot.pivot_IMU(10);
                currentDirection = LEFT;
            }

            if ((paradeBot.getDistFromRight_In() > 4 && paradeBot.getDistFromRight_In() < 6) || currentDirection != CENTER)
            {
                paradeBot.driveStraight_In(10, .5);

                if (currentDirection == LEFT)
                    paradeBot.pivot_IMU(-10);
                else if (currentDirection == RIGHT)
                    paradeBot.pivot_IMU(10);

                currentDirection = CENTER;
            }
        }
        paradeBot.driveMotorsAuto(0, 0);
    }

}

