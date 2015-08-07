package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputController;

/**
 * Another in our series
 */
public class ThreadSafePWMOutput extends PWMOutput
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafePWMOutput(PWMOutputController controller, int channel)
        {
        super(controller, channel);
        }

    public static PWMOutputController getController(PWMOutput target)
        {
        return Util.<PWMOutputController>getPrivateObjectField(target, 0);
        }
    public static int getChannel(PWMOutput target)
        {
        return Util.getPrivateIntField(target, 1);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void setPulseWidthOutputTime(int time) 
        {
        super.setPulseWidthOutputTime(time);
        }

    @Override public synchronized double getPulseWidthOutputTime() 
        {
        return super.getPulseWidthOutputTime();
        }

    @Override public synchronized void setPulseWidthPeriod(int period) 
        {
        super.setPulseWidthPeriod(period);
        }

    @Override public synchronized double getPulseWidthPeriod() 
        {
        return super.getPulseWidthPeriod();
        }
    }
