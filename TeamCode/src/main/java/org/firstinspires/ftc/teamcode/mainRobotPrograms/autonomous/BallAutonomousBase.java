package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous;

import com.qualcomm.robotcore.util.Range;

public abstract class BallAutonomousBase extends AutonomousBase
{
    protected boolean getCapBall = true;
    protected boolean parkOnCenterVortex = false;

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

            if (gamepad1.x || gamepad2.y)
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

    protected abstract void driverStationSaysGO() throws InterruptedException;
}