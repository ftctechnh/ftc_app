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


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    public HardwareMap hardware = null;

    /** TelMet object for outputting messages to Telemetry */
    private TelMet _telMet = null;

    /** OpMode for goodies such as telemetry and actually being able to stop robot code HAHA */
    private LinearOpMode _opmode = null;


    /**
     * Initializes the robot hardware and Telemetry logger. When overriding in a child class, be
     * sure to call this method with super.init()
     *
     * In your OpMode, call this before accessing any robot hardware or functions. Failure to do so
     * will make your code go SKRRRAHH. PAP PAP KA KA KA. SKIDIKI PAP PAP. AND A PU PU PUDRRRR BOOM.
     *
     * JUST PLACE THIS WHERE IT NEEDS TO GO AND IT'LL BE FINE
     *
     * @param HW The hardware mapping variable to use. In an OpMode, just typing in "hardwareMap"
     *           will do the trick.
     *
     * @param OPMODE The OpMode that this base is running in. Make sure it's LinearOpMode
     */
    public void init(final HardwareMap HW , final LinearOpMode OPMODE)
    {
        hardware = HW;
        _telMet = new TelMet(OPMODE.telemetry);
        _opmode = OPMODE;
    }


    /**
     * @deprecated Just use opMode().telemetry()
     * @return Returns the TelMet object for use
     */
    public final TelMet telMet()
    {
        return _telMet;
    }


    /**
     * @return Returns the OpMode the robot is currently running in.
     */
    public final LinearOpMode opMode()
    {
        return _opmode;
    }


    /**
     * Stops the robot
     */
    public abstract void stop();
}
