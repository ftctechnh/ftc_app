package org.firstinspires.ftc.teamcode.Core;


public abstract class RobotCommand
{
    protected RobotComponent component = null;

    protected Thread t = null;


    public RobotCommand(RobotComponent COMPONENT)
    {
        component = COMPONENT;
    }


    /**
     * Runs the command on the main thread.
     */
    abstract void runSequentially();


    /**
     * Runs the command on a new thread
     */
    protected void runParallel()
    {
        if(t == null)
        {
//            t = new Thread(this::runSequentially);
//            t.start();
        }
    }
}
