package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi and Jeremy
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
            sleep(300);
            telemetry.addData("front", paradeBot.getDistFromFront_In());
            telemetry.addData("right", paradeBot.getDistFromRight_In());

            if (paradeBot.getDistFromRight_In() > 7)
            {
                if(currentDirection != RIGHT)
                {
                    paradeBot.pivot_IMU(-10);
                    currentDirection = RIGHT;
                }

                paradeBot.driveStraight_In(8, .75);
            }
            else if (paradeBot.getDistFromRight_In() < 3 )
            {
                if (currentDirection != LEFT)
                {
                    paradeBot.pivot_IMU(10);
                    currentDirection = LEFT;
                }
                paradeBot.driveStraight_In(8, .75);
            }
            else
            {
                paradeBot.driveStraight_In(8, .75);
            }

            if (currentDirection != CENTER)
            {
                if (currentDirection == LEFT && paradeBot.getDistFromRight_In() > 3/Math.cos(.26))
                {
                    paradeBot.pivot_IMU(-10);
                    currentDirection = CENTER;
                }
                else if (currentDirection == RIGHT && paradeBot.getDistFromRight_In() < 7/Math.cos(.26))
                {
                    paradeBot.pivot_IMU(10);
                    currentDirection = CENTER;
                }
                telemetry.addData("Current direction is center", null);
            }
            else
            {
                paradeBot.driveStraight_In(4, .75);
            }

            telemetry.update();
        }
    }

}

