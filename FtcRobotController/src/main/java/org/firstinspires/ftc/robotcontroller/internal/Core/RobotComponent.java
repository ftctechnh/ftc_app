package org.firstinspires.ftc.robotcontroller.internal.Core;

/**
 * Created by Computer on 9/8/2018.
 */

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.HardwareMapper;

public abstract class RobotComponent
{
    protected HardwareMapper mapper = null;

    protected RobotBase base = null;

    //Initialize robot component

    protected void init(final RobotBase BASE)
    {
        base = BASE;
        mapper = new HardwareMapper(base);
    }

    //Returns base object of the component

    public final RobotBase base()
    {
        return base;
    }

    //Stop compononent
    public abstract void stop();
    

}
