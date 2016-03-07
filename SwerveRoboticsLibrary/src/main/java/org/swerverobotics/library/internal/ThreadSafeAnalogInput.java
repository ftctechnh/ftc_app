package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;

/**
 * ThreadSafeAnalogInput modifies the FTC-provided AnalogInput so that it is thread-safe.
 * Accessors are also provided for fetching the controller and channel of an AnalogInput.
 */
public class ThreadSafeAnalogInput extends AnalogInput
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    public ThreadSafeAnalogInput(AnalogInputController controller, int channel) 
        {
        super(controller, channel); 
        }
    
    public static AnalogInputController getController(AnalogInput target)
        {
        return Util.<AnalogInputController>getPrivateObjectField(target, 0);
        }
    public static int getChannel(AnalogInput target)
        {
        return Util.getPrivateIntField(target, 1);
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override public synchronized int getValue() 
        {
        return super.getValue();
        }

    @Override public synchronized String getDeviceName() 
        {
        return super.getDeviceName();
        }

    @Override public synchronized String getConnectionInfo() 
        {
        return super.getConnectionInfo();
        }

    @Override public synchronized int getVersion()
        {
        return super.getVersion();
        }

    @Override public synchronized void close() 
        {
        super.close();
        }
    }
