package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * <p>
 *      Utility class that manages niche autonomous-specific functions
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
abstract class UtilAuto extends LinearOpMode
{
    private final long DEFAULT_START_DELAY = 0;             // Default delay before auto executes
    private final SensorColor.Color DEFAULT_TEAM_COLOR = SensorColor.Color.RED;// Default team color

    protected long startDelay = DEFAULT_START_DELAY;                // Start delay
    protected SensorColor.Color teamColor = DEFAULT_TEAM_COLOR;     // Team color

    private UtilPulsar adjustPulsar = new UtilPulsar();     // Used to adjust period between
                                                            // adjustment of values


    /**
     * Called before the autonomous starts. Initializes things such as team color, delay, etc.
     */
    protected void preRunInit()
    {
        final int SMALL_JUMP = 50;
        final int LARGE_JUMP = 500;

        UtilToggle switchAlliance = new UtilToggle();
        UtilToggle restoreDefaults = new UtilToggle();
        UtilToggle quitSetup = new UtilToggle();

        // Create an array of 1 element because I want pass-by-reference
        long[] startingTime = new long[1];
        startingTime[0] = System.currentTimeMillis();


        double startDelayControlSlow;
        double startDelayControlFast;

        while(!quitSetup.isPressed(gamepad1.a) && !isStopRequested())
        {
            displayInitMenu();

            startDelayControlSlow = gamepad1.right_stick_y * -1;
            startDelayControlFast = gamepad1.right_stick_x;

            startDelay = (int)editBuffer(startDelay , startDelayControlSlow ,
                            SMALL_JUMP , startingTime);

            startDelay = (int)editBuffer(startDelay , startDelayControlFast ,
                            LARGE_JUMP , startingTime);


            if(switchAlliance.isPressed(gamepad1.y))
                changeTeamColor();


            if(restoreDefaults.isPressed(gamepad1.x))
                resetInitValues();

            telemetry.update();
        }

        displayInitWarning();
        telemetry.update();
    }


    /**
     * Displays an options menu and their respective controls
     */
    private void displayInitMenu()
    {
        telemetry.addData("Pre-run Initializer" , "");

        telemetry.addData("\n\nControls" , "\n");
        telemetry.addData("y" , "\tSwitch Alliance Color");
        telemetry.addData("x" , "\tRestore Default Settings");
        telemetry.addData("a" , "\tAccept and Resume");
        telemetry.addData("Right Joystick (y)" , "\tChange Start Delay");

        telemetry.addData("\n\nCurrent Settings" , "\n");
        telemetry.addData("Alliance Color" , "\t" + teamColor.asString());
        telemetry.addData("Start Delay" , "\t" + startDelay);
    }


    /**
     * Displays the warning message that precedes the start of autonomous
     */
    private void displayInitWarning()
    {
        telemetry.addData("Alert" , "\tAre these values correct?");
        telemetry.addData("Alert" , "\tMAKE SURE THESE ARE CORRECT BEFORE RUNNING");
        telemetry.addData("Alert" , "\tIF NOT, PRESS STOP AND REINITIALIZE");
        telemetry.addData("Alert" , "\tOTHERWISE, PRESS START WHEN READY");

        telemetry.addData("\n\nAlliance Color" , "\t" + teamColor.asString());
        telemetry.addData("Start Delay" , "\t" + startDelay);
    }


    /**
     * Resets the option values
     */
    private void resetInitValues()
    {
        teamColor = DEFAULT_TEAM_COLOR;
        startDelay = DEFAULT_START_DELAY;
    }


    /**
     * Changes the team color between RED and BLUE
     */
    private void changeTeamColor()
    {
        switch(teamColor)
        {
            case RED:
                teamColor = SensorColor.Color.BLUE;
                break;

            default:
                teamColor = SensorColor.Color.RED;
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
    private double editBuffer(double buffer , double input , final double JUMP , long startingTime[])
    {
        final int MIN_PERIOD = 100;
        int period;

        period = (int)UtilBasic.scaleValue(input);

        if(input != 0)
            period = (int)Math.abs(1 / input * MIN_PERIOD);

        if(adjustPulsar.pulse(period ))
        {
            buffer += JUMP * input / Math.abs(input);
            startingTime[0] = System.currentTimeMillis();
        }

        if(buffer < 0)
            buffer = 0;

        return buffer;
    }
}