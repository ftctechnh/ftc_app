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


import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Base of a robot- every robot base should have these components to run properly. The base acts
 * as a sort of "tie" between components and sensors- it provides a single object to pass around to
 * OpModes and provides easy hardware mapping.
 */
@SuppressWarnings("unused")
public abstract class RobotBase
{
    /** HardwareMap object used for mapping robot hardware. */
    protected HardwareMap hardware = null;


    /**
     * Initializes the robot hardware. I suggest that you perform hardware mapping here, along
     * with things such as assigning default servo positions. I also suggest that you call this
     * right before the opMode loop. Failing to follow these suggestions may result in unmapped
     * hardware and failure for the robot to function.
     *
     * @param HW The hardware mapping variable to use. In an OpMode, just typing in "hardwareMap"
     *           will do the trick.
     */
    public abstract void init(final HardwareMap HW);
}
