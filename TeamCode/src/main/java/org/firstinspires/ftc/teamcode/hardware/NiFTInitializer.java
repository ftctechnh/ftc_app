/**
 * This code enables hardware initialization to be done outside of the op mode with a more generic format.
 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class NiFTInitializer
{
    //Enables the initialization of generic hardware components.
    private static HardwareMap mainHardwareMap;
    public static void setHardwareMap(HardwareMap hardwareMap)
    {
        mainHardwareMap = hardwareMap;
    }

    //Initializes any hardware device provided by some class, if it exists o_O
    public static <T extends HardwareDevice> T initialize (Class<T> hardwareDevice, String name)
    {
        try
        {
            //Returns the last subclass (if this were a DcMotor it would pass back a Dc Motor.
            return hardwareDevice.cast (mainHardwareMap.get (name));
        }
        catch (Exception e) //There might be other exceptions that this throws, not entirely sure about which so I am general here.
        {
            throw new NullPointerException ("Couldn't find " + name + " in the configuration file!");
        }
    }
}
