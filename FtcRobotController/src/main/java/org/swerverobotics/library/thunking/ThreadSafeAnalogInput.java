package org.swerverobotics.library.thunking;

import java.lang.reflect.Field;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;

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
    
    public static AnalogInputController getController(AnalogInput input)
    // controller is private, so we have to be nefarious            
        {
        Class<?> c = input.getClass();
        Field field = c.getDeclaredFields()[0];
        try {
            return (AnalogInputController) (field.get(input));
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }

    public static int getChannel(AnalogInput input)
    // channel is private, so we have to be nefarious
        {
        Class<?> c = input.getClass();
        Field field = c.getDeclaredFields()[1];
        try {
            return field.getInt(input);
            }
        catch (IllegalAccessException e)
            {
            throw SwerveRuntimeException.Wrap(e);
            }
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public synchronized int getValue() 
        {
        return super.getValue();
        }

    public synchronized String getDeviceName() 
        {
        return super.getDeviceName();
        }

    public synchronized String getConnectionInfo() 
        {
        return super.getConnectionInfo();
        }

    public synchronized int getVersion()
        {
        return super.getVersion();
        }

    public synchronized void close() 
        {
        super.close();
        }
    
    }
