package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import org.swerverobotics.library.exceptions.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Various internal utilities that assist us.
 */
public class Util
    {
    //----------------------------------------------------------------------------------------------
    // Hardware mappings
    //----------------------------------------------------------------------------------------------

    public static List<HardwareMap.DeviceMapping<?>> deviceMappings(HardwareMap map)
        // Returns all the device mappings within the map
        {
        List<HardwareMap.DeviceMapping<?>> result = new LinkedList<HardwareMap.DeviceMapping<?>>();

        result.add(map.dcMotorController);
        result.add(map.dcMotor );
        result.add(map.servoController );
        result.add(map.servo );
        result.add(map.crservo );
        result.add(map.legacyModule );
        result.add(map.touchSensorMultiplexer );
        result.add(map.deviceInterfaceModule );
        result.add(map.analogInput );
        result.add(map.digitalChannel );
        result.add(map.opticalDistanceSensor );
        result.add(map.touchSensor );
        result.add(map.pwmOutput );
        result.add(map.i2cDevice );
        result.add(map.i2cDeviceSynch );
        result.add(map.analogOutput );
        result.add(map.colorSensor );
        result.add(map.led );
        result.add(map.accelerationSensor );
        result.add(map.compassSensor );
        result.add(map.gyroSensor );
        result.add(map.irSeekerSensor );
        result.add(map.lightSensor );
        result.add(map.ultrasonicSensor );
        result.add(map.voltageSensor );

        return result;
        }

    //----------------------------------------------------------------------------------------------
    // Miscellany
    //----------------------------------------------------------------------------------------------

    static public String getStackTrace(Exception e)
        {
        StringBuilder result = new StringBuilder();
        result.append(e.toString());
        for (StackTraceElement ele : e.getStackTrace())
            {
            result.append("\n");
            result.append(ele.toString());
            }
        return result.toString();
        }

    //----------------------------------------------------------------------------------------------
    // Dealing with captured exceptions
    //
    // That InterruptedException is a non-runtime exception is, I believe, a bug: I could go
    // on at great length here about that, but for the moment will refrain and defer until another
    // time: the issue is a lengthy discussion.
    //
    // But we have the issue of what to do. That the fellow has captured the interrupt means that
    // he doesn't want an InterruptedException to propagate. Yet somehow we must in effect do so:
    // the thread needs to be torn down. Ergo, we seem to have no choice but to throw a runtime
    // version of the interrupt.
    //----------------------------------------------------------------------------------------------

    public static void handleCapturedInterrupt(InterruptedException e)
        {
        Thread.currentThread().interrupt();
        }
    }
