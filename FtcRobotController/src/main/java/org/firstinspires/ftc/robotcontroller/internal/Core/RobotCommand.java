/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core;


/**
 * Class used to manage commands that can be run either sequentially or in series. In other words,
 * if you need something to run on a separate thread, this class is for you. Commands that are to be
 * run on the main thread to not need to be wrapped by this class.
 *
 * For example, a getInput() function need not be wrapped by this class, because such a function can
 * be called every iteration in the OpMode loop. However, a raiseLiftToPosition() function might
 * need to be wrapped by this class because any looping in raiseLiftToPosition() pauses the OpMode
 * loop and prevents anything else from being run.
 */
public abstract class RobotCommand
{
    /** Component this command is attached to */
    @SuppressWarnings("WeakerAccess")
    protected RobotComponent component = null;

    /** Thread object parallel command is run on */
    protected Thread t = null;


    /**
     * Default constructor- does nothing
     */
    public RobotCommand()
    {
        // Do nothing :)
    }


    /**
     * Constructor- ties the command to a component, giving it all of its internal components and
     * accessible methods
     *
     * @param COMPONENT Component to tie the command to
     */
    @SuppressWarnings("unused")
    public RobotCommand(RobotComponent COMPONENT)
    {
        component = COMPONENT;
    }


    /**
     * Runs the command on the main thread.
     */
    public abstract void runSequentially();


    /**
     * Runs the command on a new thread
     */
    public void runParallel()
    {
        if(t == null)
        {
            t = new Thread(this::runSequentially);

            t.start();
        }
    }


    /**
     * Stops current command execution
     */
    public abstract void stop();
}
