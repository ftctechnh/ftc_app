/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Color.ColorID;


/**
 * Manages the display and interaction of an pre-run autonomous menu that appears on the driver
 * station. It can be used to set delay and team color. Note that this class does not actually
 * perform any actions such as delaying- it merely holds the values for reading by another class.
 */
@SuppressWarnings({"unused" , "WeakerAccess"})
public class AutoMenu
{
    // Default values
    protected final long DEFAULT_START_DELAY = 0;
    protected ColorID DEFAULT_TEAM_COLOR = ColorID.BLUE;

    protected long startDelay = DEFAULT_START_DELAY;
    protected ColorID teamColor = DEFAULT_TEAM_COLOR;

    protected Clock adjustmentClock = new Clock();

    protected LinearOpMode auto;                // Autonomous the menu is a part of


    /**
     * Creates an auto menu given an OpMode
     *
     * @param auto Autonomous that the menu is a part of
     */
    public AutoMenu(LinearOpMode auto)
    {
        this.auto = auto;
    }


    /**
     * @return Returns the autonomous start delay
     */
    public long startDelay()
    {
        return startDelay;
    }


    /**
     * @return Returns the autonomous team color
     */
    public ColorID teamColor()
    {
        return teamColor;
    }


    /**
     * Called before the autonomous starts. Initializes things such as team color, delay, etc.
     */
    public void preRunInit()
    {
        final int SMALL_JUMP = 50;
        final int LARGE_JUMP = 500;

        Toggle switchAlliance = new Toggle();
        Toggle restoreDefaults = new Toggle();
        Toggle quitSetup = new Toggle();

        // Create an array of 1 element because I want pass-by-reference
        long[] startingTime = new long[1];
        startingTime[0] = System.currentTimeMillis();


        double startDelayControlSlow;
        double startDelayControlFast;

        while(!quitSetup.isPressed(auto.gamepad1.a) && !auto.isStopRequested())
        {
            displayInitMenu();

            startDelayControlSlow = auto.gamepad1.right_stick_y * -1;
            startDelayControlFast = auto.gamepad1.right_stick_x;

            startDelay = (int)editBuffer(startDelay, startDelayControlSlow ,
                            SMALL_JUMP , startingTime);

            startDelay = (int)editBuffer(startDelay, startDelayControlFast ,
                            LARGE_JUMP , startingTime);


            if(switchAlliance.isPressed(auto.gamepad1.y))
            {
                changeTeamColor();
            }


            if(restoreDefaults.isPressed(auto.gamepad1.x))
            {
                resetInitValues();
            }

            auto.telemetry.update();
        }

        displayInitWarning();
        auto.telemetry.update();
    }


    /**
     * Displays an options menu and their respective controls
     */
    protected void displayInitMenu()
    {
        auto.telemetry.addLine("Pre-run Initializer");

        auto.telemetry.addLine();
        auto.telemetry.addLine();

        auto.telemetry.addLine("--- Controls ---");
        auto.telemetry.addLine("Use GamePad 1");
        auto.telemetry.addData("y" , "\tSwitch Alliance Color");
        auto.telemetry.addData("x" , "\tRestore Default Settings");
        auto.telemetry.addData("a" , "\tAccept and Resume");
        auto.telemetry.addData("Right Joystick (y)" , "\tChange Start Delay");

        auto.telemetry.addLine();
        auto.telemetry.addLine();

        auto.telemetry.addLine("--- Current Settings ---");
        auto.telemetry.addData("Alliance Color" , "\t" + teamColor.asString());
        auto.telemetry.addData("Start Delay" , "\t" + startDelay);
    }


    /**
     * Displays the warning message that precedes the start of autonomous
     */
    protected void displayInitWarning()
    {
        auto.telemetry.addData("Alert" , "\tAre these values correct?");
        auto.telemetry.addData("Alert" , "\tMAKE SURE THESE ARE CORRECT BEFORE RUNNING");
        auto.telemetry.addData("Alert" , "\tIF NOT, PRESS STOP AND REINITIALIZE");
        auto.telemetry.addData("Alert" , "\tOTHERWISE, PRESS START WHEN READY");

        auto.telemetry.addData("\n\nAlliance Color" , "\t" + teamColor.asString());
        auto.telemetry.addData("Start Delay" , "\t" + startDelay);
    }


    /**
     * Resets the option values
     */
    protected void resetInitValues()
    {
        teamColor = DEFAULT_TEAM_COLOR;
        startDelay = DEFAULT_START_DELAY;
    }


    /**
     * Changes the team color between RED and BLUE
     */
    protected void changeTeamColor()
    {
        switch(teamColor)
        {
            case RED:
                teamColor = ColorID.BLUE;
                break;

            default:
                teamColor = ColorID.RED;
        }
    }


    /**
     * Adjusts the start delay buffer depending on input
     *
     * @param buffer Current value of the buffer
     * @param input A value between 0 and 1 to denote the speed at which to adjust the buffer
     * @param JUMP The value to jump by every time the buffer is adjusted
     * @param startingTime The starting time of the menu execution
     *
     * @return Returns the modified buffer.
     */
    protected double editBuffer(double buffer , double input , final double JUMP , long startingTime[])
    {
        final int MIN_PERIOD = 100;
        int period;

        period = (int) Util.scaleValue(input);

        if(input != 0)
            period = (int)Math.abs(1 / input * MIN_PERIOD);

        if(adjustmentClock.tick(period))
        {
            buffer += JUMP * input / Math.abs(input);
            startingTime[0] = System.currentTimeMillis();
        }

        if(buffer < 0)
            buffer = 0;

        return buffer;
    }
}