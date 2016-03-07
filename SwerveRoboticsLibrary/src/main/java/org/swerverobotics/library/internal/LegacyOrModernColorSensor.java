package org.swerverobotics.library.internal;

import android.graphics.Color;

import com.qualcomm.hardware.hitechnic.*;
import com.qualcomm.hardware.modernrobotics.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.*;

/**
 * This class implements a driver for either a HiTechnic color sensor or a
 * Modern Robotics color sensor. The two are very similar I2C devices; it's easy
 * for them to share code.
 */
public class LegacyOrModernColorSensor implements ColorSensor, IOpModeStateTransitionEvents
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // See http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NCO1038
    // See http://www.modernroboticsinc.com/color-sensor

    public static final int ADDRESS_I2C_HITECHNIC       = 2;
    public static final int ADDRESS_I2C_MODERN          = 60;   // == 0x3c, but changeable

    public static final int REGISTER_COMMAND_HITECHNIC  = 65;   // == 0x41
    public static final int REGISTER_COMMAND_MODERN     = 3;

    public static final int OFFSET_COMMAND              = 4;
    public static final int OFFSET_COLOR_NUMBER         = 5;
    public static final int OFFSET_RED_READING          = 6;
    public static final int OFFSET_GREEN_READING        = 7;
    public static final int OFFSET_BLUE_READING         = 8;
    public static final int OFFSET_ALPHA_VALUE          = 9;                        // MR sensor only
    public static final int OFFSET_READ_FIRST           = OFFSET_COMMAND;
    public static final int OFFSET_READ_MAX             = OFFSET_ALPHA_VALUE + 1;

    public static final int COMMAND_PASSIVE_LED         = 1;
    public static final int COMMAND_ACTIVE_LED          = 0;
    public static final int COMMAND_BLACK_CALIBRATION   = 0x42;                     // MR sensor only
    public static final int COMMAND_WHITE_CALIBRATION   = 0x43;                     // MR sensor only
    public static final int COMMAND_50HZ                = 0x35;                     // MR sensor only
    public static final int COMMAND_60HZ                = 0x36;                     // MR sensor only

    final I2cDeviceClient                       i2cDeviceClient;
    final ClassFactory.SENSOR_FLAVOR            flavor;
          boolean                               ledIsEnabled;
          boolean                               ledStateIsKnown;
    I2cDeviceReplacementHelper<ColorSensor>     helper;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private LegacyOrModernColorSensor(OpMode context, I2cDeviceClient i2cDeviceClient, ClassFactory.SENSOR_FLAVOR flavor, ColorSensor target, I2cController controller, int targetPort)
        {
        switch (flavor)
            {
            case HITECHNIC:
            case MODERNROBOTICS:
                break;
            default:
                throw new IllegalArgumentException(String.format("unimplemented color sensor flavor: %s", flavor.toString()));
            }

        this.helper          = new I2cDeviceReplacementHelper<ColorSensor>(context, this, target, controller, targetPort);
        this.i2cDeviceClient = i2cDeviceClient;
        this.flavor          = flavor;
        this.ledIsEnabled    = false;
        this.ledStateIsKnown = false;

        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.ReadWindow(
                this.getOffsetBase() + OFFSET_READ_FIRST,
                OFFSET_READ_MAX - OFFSET_READ_FIRST,
                II2cDeviceClient.READ_MODE.REPEAT));

        RobotStateTransitionNotifier.register(context, this);
        }

    public static ColorSensor create(OpMode context, ColorSensor target)
        {
        I2cController controller;
        int port;
        int i2cAddr8Bit;
        ClassFactory.SENSOR_FLAVOR flavor;

        if (target instanceof HiTechnicNxtColorSensor)
            {
            HiTechnicNxtColorSensor colorTarget = (HiTechnicNxtColorSensor)target;
            controller  = colorTarget.getI2cController();
            port        = colorTarget.getPort();
            i2cAddr8Bit = ADDRESS_I2C_HITECHNIC;
            flavor      = ClassFactory.SENSOR_FLAVOR.HITECHNIC;
            }
        else if (target instanceof ModernRoboticsI2cColorSensor)
            {
            ModernRoboticsI2cColorSensor colorTarget = (ModernRoboticsI2cColorSensor)target;
            controller  = colorTarget.getI2cController();
            port        = colorTarget.getPort();
            i2cAddr8Bit = target.getI2cAddress();
            flavor      = ClassFactory.SENSOR_FLAVOR.MODERNROBOTICS;
            }
        else
            throw new IllegalArgumentException(String.format("incorrect color sensor class: %s", target.getClass().getSimpleName()));

        return create(context, controller, port, i2cAddr8Bit, flavor, target);
        }

    public static ColorSensor create(OpMode context, I2cController controller, int port, int i2cAddr8Bit, ClassFactory.SENSOR_FLAVOR flavor, ColorSensor target)
        {
        II2cDevice i2cDevice             = new I2cDeviceOnI2cDeviceController(controller, port);
        I2cDeviceClient i2cDeviceClient  = new I2cDeviceClient(context, i2cDevice, i2cAddr8Bit, false);
        LegacyOrModernColorSensor result = new LegacyOrModernColorSensor(context, i2cDeviceClient, flavor, target, controller, port);
        result.engage();
        return result;
        }

    private void engage()
        {
        if (!this.helper.isEngaged())
            {
            this.helper.engage();
            this.i2cDeviceClient.engage();
            }
        }

    private void disengage()
        {
        if (this.helper.isEngaged())
            {
            this.i2cDeviceClient.disengage();
            this.helper.disengage();
            }
        }

    //----------------------------------------------------------------------------------------------
    // IOpModeStateTransitionEvents
    //----------------------------------------------------------------------------------------------

    @Override synchronized public boolean onUserOpModeStop()
        {
        this.disengage();
        return true;    // unregister us
        }

    @Override synchronized public boolean onRobotShutdown()
        {
        // We actually shouldn't be here by now, having received a onUserOpModeStop()
        // after which we should have been unregistered. But we close down anyway.
        this.close();
        return true;    // unregister us
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        this.i2cDeviceClient.close();
        }

    @Override public int getVersion()
        {
        return this.flavor == ClassFactory.SENSOR_FLAVOR.HITECHNIC ? 2 : 1;
        }

    @Override public String getConnectionInfo()
        {
        return this.i2cDeviceClient.getConnectionInfo();
        }

    @Override public String getDeviceName()
        {
        return this.flavor == ClassFactory.SENSOR_FLAVOR.HITECHNIC
                ? "Swerve NXT Color Sensor"
                : "Swerve Modern Robotics I2C Color Sensor";
        }

    //----------------------------------------------------------------------------------------------
    // ColorSensor
    //----------------------------------------------------------------------------------------------

    int getOffsetBase()
        {
        return this.flavor == ClassFactory.SENSOR_FLAVOR.HITECHNIC
                ? (REGISTER_COMMAND_HITECHNIC - OFFSET_COMMAND)
                : (REGISTER_COMMAND_MODERN - OFFSET_COMMAND);
        }

    int read(int dib)
        {
        byte b = this.i2cDeviceClient.read8(getOffsetBase() + dib);
        return TypeConversion.unsignedByteToInt(b);
        }

    @Override public synchronized int red()
        {
        return this.read(OFFSET_RED_READING);
        }

    @Override public synchronized int green()
        {
        return this.read(OFFSET_GREEN_READING);
        }

    @Override public synchronized int blue()
        {
        return this.read(OFFSET_BLUE_READING);
        }

    @Override public synchronized int alpha()
        {
        return this.read(OFFSET_ALPHA_VALUE);
        }

    @Override public synchronized int argb()
        {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
        }

    @Override public synchronized void enableLed(boolean enable)
        {
        if (!this.ledStateIsKnown || this.ledIsEnabled != enable)
            {
            this.ledIsEnabled = enable;
            this.ledStateIsKnown = true;
            this.i2cDeviceClient.write8(getOffsetBase() + OFFSET_COMMAND, enable ? COMMAND_ACTIVE_LED : COMMAND_PASSIVE_LED);
            }
        }

    @Override public synchronized int getI2cAddress()
        {
        return this.i2cDeviceClient.getI2cAddr();
        }

    @Override public synchronized void setI2cAddress(int i2cAddr8Bit)
        {
        this.i2cDeviceClient.setI2cAddr(i2cAddr8Bit);
        }
    }
