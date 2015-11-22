package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;

/**
 * Another in our series
 */
public class ThreadSafeAnalogOutput extends AnalogOutput
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeAnalogOutput(AnalogOutputController controller, int channel)
        {
        super(controller, channel);
        }

    public static AnalogOutputController getController(AnalogOutput target)
        {
        return Util.<AnalogOutputController>getPrivateObjectField(target, 0);
        }
    public static int getChannel(AnalogOutput target)
        {
        return Util.getPrivateIntField(target, 1);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override synchronized public void setAnalogOutputVoltage(int voltage) 
        {
        super.setAnalogOutputVoltage(voltage);
        }

    @Override synchronized public  void setAnalogOutputFrequency(int freq) 
        {
        super.setAnalogOutputFrequency(freq);
        }

    @Override synchronized public  void setAnalogOutputMode(byte mode) 
        {
        super.setAnalogOutputMode(mode);
        }
    }
