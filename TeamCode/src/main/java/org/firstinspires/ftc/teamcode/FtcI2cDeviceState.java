/*
 * Copyright (c) 2015 Titan Robotics Club (http://www.titanrobotics.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cControllerPortDevice;

/**
 * This class provides methods to disable/enable the I2c device. The number of active I2C devices in the system
 * greatly affects the performance of other sensors. Most of the I2C sensors are not in-use until they are needed.
 * By disabling the unused sensors and only enabling them when needed will greatly improve the performance of
 * other sensors. Note that this class only works with the older I2cControllerPortDevice. The newer I2c devices
 * that use the I2cDeviceSynch implementation will not work because the methods required to disable/enable the
 * device (i.e. engage, disengage) are "protected" so this class cannot access them unless it extends the
 * I2cDeviceSynchDevice class.
 */
public class FtcI2cDeviceState
{
    private I2cController i2cController = null;
    private int port = 0;
    private I2cController.I2cPortReadyCallback deviceCallback = null;
    private boolean deviceEnabled = true;
    /**
     * Constructor: Creates an instance of the object.
     *
     * @param instanceName specifies the instance name.
     * @param device specifies the I2c device to be enabled/disabled.
     */
    public FtcI2cDeviceState(String instanceName, I2cControllerPortDevice device)
    {
        i2cController = device.getI2cController();
        port = device.getPort();
        deviceCallback = i2cController.getI2cPortReadyCallback(port);
    }   //FtcI2cDeviceState
    /**
     * This method is called to determine if the I2c device is disabled or enabled.
     *
     * @return device state.
     */
    public boolean isEnabled()
    {
        return deviceEnabled;
    }   //isEnabled
    /**
     * This method is called to disable/enable he I2C device.
     *
     * @param enabled specifies true to enable the I2c device, false to disable it.
     */
    public void setEnabled(boolean enabled)
    {
        if (deviceEnabled != enabled)
        {
            if (enabled)
            {
                i2cController.registerForI2cPortReadyCallback(deviceCallback, port);
            }
            else
            {
                i2cController.deregisterForPortReadyCallback(port);
            }
            deviceEnabled = enabled;
        }
    }   //setTaskEnabled
}   //class FtcI2cDeviceState