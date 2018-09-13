package org.firstinspires.ftc.robotcontroller.internal.Core;

public abstract class RobotCommand
{

    protected RobotComponent component = null;

    protected Thread t = null;

    public RobotCommand()
    {

    }

    //Constructor - ties teh command to a compononent.

    public RobotCommand(RobotComponent COMPONENT)
    {
        component = COMPONENT;
    }

    //Runs the command on the main thread.
    public abstract void runSequentially();

    //Runs command parallel to main thread.
    public abstract void runParallel();

    //Stops current command execution
    public abstract void stop();
}
