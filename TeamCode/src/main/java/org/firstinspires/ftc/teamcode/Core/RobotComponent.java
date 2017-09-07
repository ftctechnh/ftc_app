/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.teamcode.Core;


import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;


/**
 * Base class for robot components (drivetrain, shooter, etc.) The class implements Runnable
 * to allow for parallel command execution instead of creating a new Runnable class for every
 * parallel action we want to accomplish.
 */
public abstract class RobotComponent implements Runnable
{
    /** Hardware mapping object- declare a new instance in the child class to map */
    protected HardwareMapper mapper = null;

    /** Gamepad object for driver 1- used as a wrapper for controls */
    @SuppressWarnings("WeakerAccess")
    protected Gamepad gamepad1 = null;

    /** Gamepad object for driver 2- used as a wrapper for controls */
    @SuppressWarnings("WeakerAccess")
    protected Gamepad gamepad2 = null;


    // Runnable's run() doesn't take any parameters, so we can't pass values to it. The solution
    // is to have a command object outside, and then to reassign the object every time we call run
    private Command m_command = null;


    /**
     * Initializes the hardware mapping object by creating a new instance
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    protected void init(final RobotBase BASE)
    {
        mapper  = new HardwareMapper(BASE);
    }


    /**
     * Overwritten version of Runnable's run()- it is public out of necessity. DO NOT CALL THIS
     * METHOD ON ITS OWN- YOU COULD GET INCORRECT COMMAND EXECUTION OR NULL POINTER EXCEPTION
     */
    @Override
    final public void run()
    {
        try
        {
            m_command.run();
        }
        catch(NullPointerException e)
        {
            Log.e("Error" , "Null pointer, did you call this method by accident?");
        }
    }


    /**
     * Runs the given command sequentially. The vast majority of TeleOp commands should be run
     * sequentially to avoid unnecessary creation of threads.
     *
     * @param COMMAND Command to be run
     */
    final public void runSequential(final Command COMMAND)
    {
        m_command = COMMAND;
        run();
    }


    /**
     * Executes a command on its own thread, hence parallel command execution.
     * Does not need to be called in an op mode loop, unless the command is an expensive
     * process that involves a loop.
     *
     * @param COMMAND Command to be run
     */
    @SuppressWarnings("unused")
    final public void runParallel(final Command COMMAND)
    {
        Thread t = new Thread();

        m_command = COMMAND;

        t.start();                  // Calls the run() method
    }


    /**
     * Grabs input from a gamepad
     *
     * @param GAMEPAD1 Gamepad object for driver 1
     * @param GAMEPAD2 Gamepad object for driver 2
     */
    @SuppressWarnings("unused")
    final protected void getInput(final Gamepad GAMEPAD1 , final Gamepad GAMEPAD2)
    {
        gamepad1 = GAMEPAD1;
        gamepad2 = GAMEPAD2;
    }


    /**
     * Interface for functional programming
     */
    public interface Command
    {
        void run();
    }
}
