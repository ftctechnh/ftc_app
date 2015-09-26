package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Another in our series...
 */
public class ThreadSafeDigitalChannel extends DigitalChannel
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeDigitalChannel(DigitalChannelController controller, int channel)
        {
        super(controller, channel);
        }

    public static DigitalChannelController getController(DigitalChannel target)
        {
        return Util.<DigitalChannelController>getPrivateObjectField(target, 0);
        }
    public static int getChannel(DigitalChannel target)
        {
        return Util.getPrivateIntField(target, 1);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override public synchronized DigitalChannelController.Mode getMode() 
        {
        return super.getMode();
        }

    @Override public synchronized void setMode(DigitalChannelController.Mode mode) 
        {
        super.setMode(mode);
        }

    @Override public synchronized boolean getState() 
        {
        return super.getState();
        }

    @Override public synchronized void setState(boolean state) 
        {
        super.setState(state);
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
