package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.autonomous.OnAlliance;
import org.firstinspires.ftc.teamcode.programflow.ConsoleManager;

public abstract class BallAuto extends AutoBase implements OnAlliance
{
    private boolean getCapBall = true;
    private boolean parkOnCenterVortex = false;

    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        long delay = 0;
        long lastDelayIncrementTime = 0;

        //Get input stuff for delay, etc.
        while (!opModeIsActive ()) //While start not pressed
        {
            if (System.currentTimeMillis () - lastDelayIncrementTime >= 400)
            {
                //Delay variables.
                if (gamepad1.dpad_up || gamepad2.dpad_up)
                    delay += 1000;
                else if (gamepad1.dpad_down || gamepad2.dpad_down)
                    delay -= 1000;

                delay = (long) (Range.clip (delay, 0, 30000));

                lastDelayIncrementTime = System.currentTimeMillis ();
            }

            if (gamepad1.y || gamepad2.y)
                getCapBall = false;

            if (gamepad1.x || gamepad2.x)
                parkOnCenterVortex = true;

            ConsoleManager.outputConstantDataToDrivers (
                    new String[]
                            {
                                    "Delay (DPAD) is " + delay,
                                    "Getting cap ball (Y) = " + getCapBall,
                                    "Parking on CV (X) = " + parkOnCenterVortex
                            }
            );

            idle();
        }

        sleep (delay);
    }

    protected void driverStationSaysGO() throws InterruptedException
    {
        Alliance alliance = getAlliance ();

        boolean onBlueAlliance = (alliance == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        //Drive to the cap ball.
        ConsoleManager.outputNewLineToDrivers ("Driving to shooting position.");
        drive (SensorStopType.Ultrasonic, 40, PowerUnits.RevolutionsPerMinute, 1);

        //Shoot the balls into the center vortex.
        ConsoleManager.outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        if (parkOnCenterVortex)
        {
            ConsoleManager.outputNewLineToDrivers ("Parking on center vortex.");
            drive(SensorStopType.Distance, 1400, PowerUnits.RevolutionsPerMinute, 2);
            return; //End prematurely
        }

        if (getCapBall)
        {
            //Drive the remainder of the distance.
            ConsoleManager.outputNewLineToDrivers ("Knock the cap ball off of the pedestal.");
            drive(SensorStopType.Distance, 1800, PowerUnits.RevolutionsPerMinute, 3);

            //Turn to face the ramp from the position that we drove.
            ConsoleManager.outputNewLineToDrivers ("Turning to the appropriate heading.");
            turnToHeading (110 * autonomousSign, TurnMode.BOTH, 3000);
        }
        else
        {
            //Turn to face the ramp from the position that we drove.
            ConsoleManager.outputNewLineToDrivers ("Turning to the appropriate heading.");
            turnToHeading (70 * autonomousSign, TurnMode.BOTH, 3000);
        }

        //Drive until we reach the appropriate position.
        ConsoleManager.outputNewLineToDrivers ("Drive to the ramp, stopping upon bottom color sensor reaches the blue region on the ramp.");
        drive(SensorStopType.Distance, 1000, PowerUnits.RevolutionsPerMinute, 1);
    }
}