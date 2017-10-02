package org.firstinspires.ftc.teamcode.Core;


public abstract class RobotCommand
{
    protected RobotComponent component = null;

    protected Thread t = null;


    public RobotCommand()
    {
        // Do nothing
    }


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
            t = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    runSequentially();
                }
            });

            t.start();
        }
    }


    public abstract void stop();
}
