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


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.directcurrent.core.TelMet;


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

    /** TelMet object for outputting messages to Telemetry */
    private TelMet _telMet = null;


    /**
     * Initializes the robot hardware and Telemetry logger. When overriding in a child class, be
     * sure to call this method with super.init()
     *
     * In your OpMode, call this before accessing any robot hardware or functions. Failure to do so
     * will make your code go SKRRRRRRRRR AH KA KA KA KA KA SKIDDY YA PA PA
     *
     * JUST PLACE THIS WHERE IT NEEDS TO GO AND IT'LL BE FINE
     *
     * @param HW The hardware mapping variable to use. In an OpMode, just typing in "hardwareMap"
     *           will do the trick.
     *
     * @param OPMODE The OpMode that this base is running in
     */
    public void init(final HardwareMap HW , final OpMode OPMODE)
    {
        hardware = HW;
        _telMet = new TelMet(OPMODE);
    }


    /**
     * @return Returns the TelMet object for use
     */
    public final TelMet telMet()
    {
        return _telMet;
    }
}
