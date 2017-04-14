package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.autonomous.OnAlliance;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;
import org.firstinspires.ftc.teamcode.threads.ProgramFlow;

public abstract class BallAuto extends AutoBase implements OnAlliance
{
    private boolean getCapBall = true;
    private boolean parkOnCenterVortex = false;

    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        long delay = 0;
        long lastDelayIncrementTime = 0;

        ConsoleManager.ProcessConsole processConsole = new ConsoleManager.ProcessConsole ("Selected Options");

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

            processConsole.updateWith (
                    "Delay (DPAD) is " + delay,
                    "Getting cap ball (Y) = " + getCapBall,
                    "Parking on Center Vortex (X) = " + parkOnCenterVortex
            );

            ProgramFlow.pauseForSingleFrame ();
        }

        processConsole.destroy ();

        ProgramFlow.pauseForMS (delay);
    }

    protected void driverStationSaysGO() throws InterruptedException
    {
        boolean onBlueAlliance = (getAlliance () == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        //Drive to the cap ball.
        ConsoleManager.outputNewSequentialLine ("Driving to shooting position...");
        drive (SensorStopType.Ultrasonic, 40, PowerUnits.RevolutionsPerMinute, 1);

        //Shoot the balls into the center vortex.
        ConsoleManager.outputNewSequentialLine("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        if (parkOnCenterVortex)
        {
            ConsoleManager.outputNewSequentialLine ("Parking on center vortex...");
            drive(SensorStopType.Distance, 1400, PowerUnits.RevolutionsPerMinute, 2);
            return; //End prematurely
        }

        if (getCapBall)
        {
            //Drive the remainder of the distance.
            ConsoleManager.outputNewSequentialLine ("Knocking the cap ball off of the pedestal...");
            drive(SensorStopType.Distance, 1800, PowerUnits.RevolutionsPerMinute, 3);

            //Turn to face the ramp from the position that we drove.
            ConsoleManager.outputNewSequentialLine ("Turning to the appropriate heading...");
            turnToHeading (110 * autonomousSign, TurnMode.BOTH, 3000);
        }
        else
        {
            //Turn to face the ramp from the position that we drove.
            ConsoleManager.outputNewSequentialLine ("Turning to the appropriate heading...");
            turnToHeading (70 * autonomousSign, TurnMode.BOTH, 3000);
        }

        //Drive until we reach the appropriate position.
        ConsoleManager.outputNewSequentialLine ("Driving to the ramp...");
        drive(SensorStopType.Distance, 1000, PowerUnits.RevolutionsPerMinute, 1);
    }
}