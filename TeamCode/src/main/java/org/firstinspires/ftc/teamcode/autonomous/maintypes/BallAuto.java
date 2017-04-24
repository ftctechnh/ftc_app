/**
 * Ball auto is the backup auto we run when our team has a better beacon auto.
 */

package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.autonomous.OnAlliance;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

public abstract class BallAuto extends AutoBase implements OnAlliance
{
    private boolean getCapBall = true;
    private boolean parkOnCenterVortex = false;

    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        long delay = 0;
        long lastDelayIncrementTime = 0;

        NiFTConsole.ProcessConsole processConsole = new NiFTConsole.ProcessConsole ("Selected Options");

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

            NiFTFlow.pauseForSingleFrame ();
        }

        processConsole.destroy ();

        NiFTFlow.pauseForMS (delay);
    }

    protected void driverStationSaysGO() throws InterruptedException
    {
        //Start PID on flywheels.
        flywheels.startPIDTask ();
        flywheels.setRPS (18.2);

        boolean onBlueAlliance = (getAlliance () == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        //Drive to the cap ball.
        NiFTConsole.outputNewSequentialLine ("Driving to shooting position...");
        drive (SensorStopType.Ultrasonic, 40, PowerUnits.RevolutionsPerMinute, 1);

        //Shoot the balls into the center vortex.
        NiFTConsole.outputNewSequentialLine("Shooting balls into center vortex...");
        harvester.setDirectMotorPower (1);
        NiFTFlow.pauseForMS (2200);
        harvester.setDirectMotorPower (0);
        flywheels.setRPS (0);
        flywheels.stopPIDTask ();

        if (parkOnCenterVortex)
        {
            NiFTConsole.outputNewSequentialLine ("Parking on center vortex...");
            drive(SensorStopType.Distance, 1400, PowerUnits.RevolutionsPerMinute, 2);
            return; //End prematurely
        }

        if (getCapBall)
        {
            //Drive the remainder of the distance.
            NiFTConsole.outputNewSequentialLine ("Knocking the cap ball off of the pedestal...");
            drive(SensorStopType.Distance, 1800, PowerUnits.RevolutionsPerMinute, 3);

            //Turn to face the ramp from the position that we drove.
            NiFTConsole.outputNewSequentialLine ("Turning to the appropriate heading...");
            turnToHeading (110 * autonomousSign, TurnMode.BOTH, 3000);
        }
        else
        {
            //Turn to face the ramp from the position that we drove.
            NiFTConsole.outputNewSequentialLine ("Turning to the appropriate heading...");
            turnToHeading (70 * autonomousSign, TurnMode.BOTH, 3000);
        }

        //Drive until we reach the appropriate position.
        NiFTConsole.outputNewSequentialLine ("Driving to the ramp...");
        drive(SensorStopType.Distance, 1000, PowerUnits.RevolutionsPerMinute, 1);
    }
}