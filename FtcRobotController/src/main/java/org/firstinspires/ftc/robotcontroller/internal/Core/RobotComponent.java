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
 * Base class for robot components (drivetrain, shooter, etc.) The class implements Runnable
 * to allow for parallel command execution instead of creating a new Runnable class for every
 * parallel action we want to accomplish.
 */
public abstract class RobotComponent
{
    /** Hardware mapping object- declare a new instance in the child class to map */
    protected HardwareMapper mapper = null;


    /**
     * Initializes the hardware mapping object by creating a new instance. When overriding this
     * method, do any hardware mapping specific to your component in here.
     *
     * Call this for each component in your RobotBase.init() method
     *
     * @param BASE The robot base used to create the hardware mapper
     */
    protected void init(final RobotBase BASE)
    {
        mapper  = new HardwareMapper(BASE);
    }
}