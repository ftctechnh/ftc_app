/**
 * This opmode improvement class comes with error handling and a number of other improvements.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.hardware.NiFTInitializer;
import org.firstinspires.ftc.teamcode.music.NiFTMusic;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

public abstract class NiFTBase extends LinearOpMode
{
    @Override
    public void runOpMode () throws InterruptedException
    {
        try
        {
            //Comment out as desired, but then you can't use the component.
            NiFTFlow.initializeWithOpMode (this);
            NiFTConsole.initializeWith (telemetry);
            NiFTMusic.initializeWith (hardwareMap.appContext);
            NiFTInitializer.setHardwareMap (hardwareMap);

            //REQUIRED in child classes.
            initializeHardware ();

            //May be used in different programs.
            driverStationSaysINITIALIZE ();

            //Wait for the start button to be pressed.
            waitForStart ();

            //This is where the child classes mainly differ in their instructions.
            driverStationSaysGO ();
        }
        catch (InterruptedException e) {} //If this is caught, then the user requested program stop.
        catch (Exception e) //If this is caught, it wasn't an InterruptedException and wasn't requested, so the user is notified.
        {
            NiFTConsole.outputNewSequentialLine ("UH OH!  A fatal error was just thrown!");
            NiFTConsole.outputNewSequentialLine (e.getMessage ());
            NiFTConsole.outputNewSequentialLine ("Will end upon tapping stop...");

            //Wait until stop is requested.
            try
            {
                while (true)
                    NiFTFlow.pauseForSingleFrame ();
            }
            catch (InterruptedException e2) {} //The user has read the message and stops the program.
        }
        finally //Occurs after all possible endings.
        {
            NiFTMusic.quit ();
            driverStationSaysSTOP ();
        }
    }

    /**
     * Each method below are the main methods with which you can run your OpModes.  This enables quick and easy access to required methods.  Edit at will, but be careful to avoid messing with the exceptions too much.
     *
     * InterruptedExceptions indicate from NiFTFlow that the stop was requested.
     */
    //Required overload.
    protected void initializeHardware () throws InterruptedException {};

    //Optional overload.
    protected void driverStationSaysINITIALIZE () throws InterruptedException {}

    //Required overload.
    protected abstract void driverStationSaysGO () throws InterruptedException;

    //Optional overload.
    protected void driverStationSaysSTOP () {}
}