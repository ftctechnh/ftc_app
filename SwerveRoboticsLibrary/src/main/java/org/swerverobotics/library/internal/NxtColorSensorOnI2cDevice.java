package org.swerverobotics.library.internal;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.*;

/**
 * A class that implements a ColorSensor object for NXT color sensors.
 *
 * @see <a href="http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NCO1038">NXT Color Sensor V2</a>
 */
public class NxtColorSensorOnI2cDevice extends ColorSensor
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private II2cDeviceClient i2cDeviceClient;

    public static final int i2cAddr8Bit = 0x02;

    // Register numbers are from the HiTechnic site
    public static final int iregFirst = 0x43;
    public static final int iregRed   = 0x43;
    public static final int iregGreen = 0x44;
    public static final int iregBlue  = 0x45;
    public static final int iregMax   = 0x46;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public NxtColorSensorOnI2cDevice(OpMode context, I2cDevice i2cDevice)
        {
        i2cDevice.deregisterForPortReadyCallback(); // Disable the previous guy so we're not fighting
        construct(context, ClassFactory.createI2cDevice(i2cDevice));
        }

    public NxtColorSensorOnI2cDevice(OpMode context, I2cController controller, int port)
        {
        construct(context, ClassFactory.createI2cDevice(controller, port));
        }

    public NxtColorSensorOnI2cDevice(OpMode context, II2cDevice ii2cDevice)
        {
        construct(context, ii2cDevice);
        }

    void construct(OpMode context, II2cDevice ii2cDevice)
        {
        this.i2cDeviceClient = ClassFactory.createI2cDeviceClient(context, ii2cDevice, i2cAddr8Bit, true);
        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.ReadWindow(iregFirst, iregMax - iregFirst, II2cDeviceClient.READ_MODE.REPEAT));
        this.i2cDeviceClient.arm();
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return "Swerve NxtColorSensorOnI2cDevice";
        }

    @Override public String getConnectionInfo()
        {
        return this.i2cDeviceClient.getConnectionInfo();
        }

    @Override public int getVersion()
        {
        return 2;   // this is "NXT Color Sensor V2" after all
        }

    @Override public void close()
        {
        this.i2cDeviceClient.close();
        }

    //----------------------------------------------------------------------------------------------
    // ColorSensor
    //----------------------------------------------------------------------------------------------

    @Override public int red()
        {
        return this.i2cDeviceClient.read8(iregRed);
        }

    @Override public int green()
        {
        return this.i2cDeviceClient.read8(iregGreen);
        }

    @Override public int blue()
        {
        return this.i2cDeviceClient.read8(iregBlue);
        }

    @Override public int alpha()
        {
        return 0;   // not supported on this device
        }

    @Override public int argb()
        {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
        }

    @Override public void enableLed(boolean enable)
        {
        // Not sure it's a good idea throwing instead of doing nothing, but this follows
        // the example of FTC HQ in their AdafruitColorSensor code.
        throw new UnsupportedOperationException("enableLed is not implemented.");
        }

    }
