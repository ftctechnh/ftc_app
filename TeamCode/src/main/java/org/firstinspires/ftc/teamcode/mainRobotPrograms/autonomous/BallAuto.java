package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous;

import com.qualcomm.robotcore.util.Range;

public abstract class BallAuto extends AutoBase
{
    private boolean getCapBall = true;
    private boolean parkOnCenterVortex = false;

    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        super.driverStationSaysINITIALIZE ();

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

            outputConstantDataToDrivers (
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
        alliance = setAlliance ();

        boolean onBlueAlliance = (alliance == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        //Drive to the cap ball.
        outputNewLineToDrivers ("Driving to shooting position.");
        driveUntilDistanceFromObstacle (40, .27);

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        if (parkOnCenterVortex)
        {
            outputNewLineToDrivers ("Parking on center vortex.");
            driveForDistance (0.5, 1400);
            return; //End prematurely
        }

        if (getCapBall)
        {
            //Drive the remainder of the distance.
            outputNewLineToDrivers ("Knock the cap ball off of the pedestal.");
            driveForDistance (0.5, 1800);

            //Turn to face the ramp from the position that we drove.
            outputNewLineToDrivers ("Turning to the appropriate heading.");
            turnToHeading (110 * autonomousSign, TurnMode.BOTH, 3000);
        }
        else
        {
            //Turn to face the ramp from the position that we drove.
            outputNewLineToDrivers ("Turning to the appropriate heading.");
            turnToHeading (70 * autonomousSign, TurnMode.BOTH, 3000);
        }

        //Drive until we reach the appropriate position.
        outputNewLineToDrivers ("Drive to the ramp, stopping upon bottom color sensor reaches the blue region on the ramp.");
        startDrivingAt (0.6);

        long startDriveTime = System.currentTimeMillis (); //Max time at 6 seconds.
        while (bottomColorSensor.red () <= 2.5 && (System.currentTimeMillis () - startDriveTime) < 6000)
            applySensorAdjustmentsToMotors (true, false, false);

        stopDriving ();
    }
}