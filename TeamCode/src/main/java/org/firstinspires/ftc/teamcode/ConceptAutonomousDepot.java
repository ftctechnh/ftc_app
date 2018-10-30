package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "ConceptAutonomousDepot")
public class ConceptAutonomousDepot extends LinearOpMode
{

    ParadeBot walle;

    public void runOpMode()
    {

        walle = new ParadeBot(hardwareMap, this);
        waitForStart();

        // have servos move robot down to the ground
        walle.driveStraight_In(36, .4f);
        walle.stopDriveMotors();
        sleep(500);

        while (walle.getDistFromFront_In() > 24 && walle.getDistFromRight_In() > 24)
        {
            telemetry.addData("leftdist", walle.getDistFromFront_In());
            telemetry.addData("righdist", walle.getDistFromRight_In());
            telemetry.update();

            walle.driveMotorsAuto(.2f, .4f);
        }
        walle.stopDriveMotors();

        //drop marker into depot.
        walle.pivot_IMU(120f);
        wallHug();
    }

    public void wallHug()
    {
        final int CENTER = 0;
        final int RIGHT = -1;
        final int LEFT = 1;

        int currentDirection = CENTER;

        while (walle.getDistFromFront_In() > 18)
        {
            sleep(300);
            telemetry.addData("front", walle.getDistFromFront_In());
            telemetry.addData("right", walle.getDistFromRight_In());

            if (walle.getDistFromRight_In() > 7)
            {
                if (currentDirection != RIGHT)
                {
                    walle.pivot_IMU(-10);
                    currentDirection = RIGHT;
                }

                walle.driveStraight_In(8, .75);
            } else if (walle.getDistFromRight_In() < 3)
            {
                if (currentDirection != LEFT)
                {
                    walle.pivot_IMU(10);
                    currentDirection = LEFT;
                }
                walle.driveStraight_In(8, .75);
            } else
            {
                walle.driveStraight_In(8, .75);
            }

            if (currentDirection != CENTER)
            {
                if (currentDirection == LEFT && walle.getDistFromRight_In() > 3 / Math.cos(.26))
                {
                    walle.pivot_IMU(-10);
                    currentDirection = CENTER;
                } else if (currentDirection == RIGHT && walle.getDistFromRight_In() < 7 / Math.cos(.26))
                {
                    walle.pivot_IMU(10);
                    currentDirection = CENTER;
                }
                telemetry.addData("Current direction is center", null);
            } else
            {
                walle.driveStraight_In(4, .75);
            }

            telemetry.update();
        }
    }
}



