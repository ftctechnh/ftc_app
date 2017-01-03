package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

public class FtcI2cDeviceState
{
    private I2cDevice device;
    private I2cController.I2cPortReadyCallback deviceCallback;
    private boolean deviceEnabled;

    public FtcI2cDeviceState(I2cDevice device)
    {
        this.device = device;
        deviceCallback = device.getI2cPortReadyCallback();
        deviceEnabled = true;
        if (device.getI2cController().getI2cPortReadyCallback(device.getPort()) != deviceCallback)
            throw new IllegalStateException("PortReadyCallback don't match!");
    }   //FtcI2cDeviceState

public boolean isEnabled()
    {
        return deviceEnabled;
    }   //isEnabled

public void setEnabled(boolean enabled)
    {
        if (deviceEnabled != enabled)
        {
            if (enabled)
            {
                device.registerForI2cPortReadyCallback(deviceCallback);
            }
            else
{
                device.deregisterForPortReadyCallback();
            }
            deviceEnabled = enabled;
        }
    }   //setEnabled

}   //class FtcI2cDeviceState