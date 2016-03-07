package org.swerverobotics.library.internal;

import android.graphics.Color;
import com.qualcomm.hardware.adafruit.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.exceptions.UnexpectedI2CDeviceException;
import org.swerverobotics.library.interfaces.*;
import java.nio.ByteOrder;

import static org.swerverobotics.library.internal.Util.handleCapturedInterrupt;

/**
 * Note: this is not yet used
 *
 *
 * http://adafru.it/1334
 * https://www.adafruit.com/products/1334?&main_page=product_info&products_id=1334
 * https://github.com/adafruit/Adafruit_TCS34725
 */
public class AdaFruitTCS34725ColorSensor implements ColorSensor, IOpModeStateTransitionEvents
    {
    //----------------------------------------------------------------------------------------------
    // State
    /*
    ADDRESS NAME        R/W     FUNCTION                            RESET VALUE
    −−     COMMAND     W       Specifies register address              0x00
    0x00    ENABLE      R/W     Enables states and interrupts           0x00
    0x01    ATIME       R/W     RGBC time                               0xFF
    0x03    WTIME       R/W     Wait time                               0xFF
    0x04    AILTL       R/W     Clear interrupt low threshold low byte  0x00
    0x05    AILTH       R/W     Clear interrupt low threshold high byte 0x00
    0x06    AIHTL       R/W     Clear interrupt high threshold low byte 0x00
    0x07    AIHTH       R/W     Clear interrupt high threshold high byte 0x00
    0x0C    PERS        R/W     Interrupt persistence filter            0x00
    0x0D    CONFIG      R/W     Configuration                           0x00
    0x0F    CONTROL     R/W     Control                                 0x00
    0x12    ID          R       Device ID                                ID
    0x13    STATUS      R       Device status                           0x00
    0x14    CDATAL      R       Clear data low byte                     0x00
    0x15    CDATAH      R       Clear data high byte                    0x00
    0x16    RDATAL      R       Red data low byte                       0x00
    0x17    RDATAH      R       Red data high byte                      0x00
    0x18    GDATAL      R       Green data low byte                     0x00
    0x19    GDATAH      R       Green data high byte                    0x00
    0x1A    BDATAL      R       Blue data low byte                      0x00
    0x1B    BDATAH      R       Blue data high byte                     0x00
    */
    //----------------------------------------------------------------------------------------------

    public static final int TCS34725_ADDRESS          = 0x29;

    public static final int TCS34725_COMMAND_BIT      = 0x80;

    public static final int TCS34725_ENABLE           = 0x00;
    public static final int TCS34725_ENABLE_AIEN      = 0x10;    /* RGBC Interrupt Enable */
    public static final int TCS34725_ENABLE_WEN       = 0x08;    /* Wait enable - Writing 1 activates the wait timer */
    public static final int TCS34725_ENABLE_AEN       = 0x02;    /* RGBC Enable - Writing 1 actives the ADC, 0 disables it */
    public static final int TCS34725_ENABLE_PON       = 0x01;    /* Power on - Writing 1 activates the internal oscillator, 0 disables it */
    public static final int TCS34725_ATIME            = 0x01;    /* Integration time */
    public static final int TCS34725_WTIME            = 0x03;    /* Wait time = if TCS34725_ENABLE_WEN is asserted; */
    public static final int TCS34725_WTIME_2_4MS      = 0xFF;    /* WLONG0 = 2.4ms   WLONG1 = 0.029s */
    public static final int TCS34725_WTIME_204MS      = 0xAB;    /* WLONG0 = 204ms   WLONG1 = 2.45s  */
    public static final int TCS34725_WTIME_614MS      = 0x00;    /* WLONG0 = 614ms   WLONG1 = 7.4s   */
    public static final int TCS34725_AILTL            = 0x04;    /* Clear channel lower interrupt threshold */
    public static final int TCS34725_AILTH            = 0x05;
    public static final int TCS34725_AIHTL            = 0x06;    /* Clear channel upper interrupt threshold */
    public static final int TCS34725_AIHTH            = 0x07;
    public static final int TCS34725_PERS             = 0x0C;    /* Persistence register - basic SW filtering mechanism for interrupts */
    public static final int TCS34725_PERS_NONE        = 0b0000;  /* Every RGBC cycle generates an interrupt                                */
    public static final int TCS34725_PERS_1_CYCLE     = 0b0001;  /* 1 clean channel value outside threshold range generates an interrupt   */
    public static final int TCS34725_PERS_2_CYCLE     = 0b0010;  /* 2 clean channel values outside threshold range generates an interrupt  */
    public static final int TCS34725_PERS_3_CYCLE     = 0b0011;  /* 3 clean channel values outside threshold range generates an interrupt  */
    public static final int TCS34725_PERS_5_CYCLE     = 0b0100;  /* 5 clean channel values outside threshold range generates an interrupt  */
    public static final int TCS34725_PERS_10_CYCLE    = 0b0101;  /* 10 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_15_CYCLE    = 0b0110;  /* 15 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_20_CYCLE    = 0b0111;  /* 20 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_25_CYCLE    = 0b1000;  /* 25 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_30_CYCLE    = 0b1001;  /* 30 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_35_CYCLE    = 0b1010;  /* 35 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_40_CYCLE    = 0b1011;  /* 40 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_45_CYCLE    = 0b1100;  /* 45 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_50_CYCLE    = 0b1101;  /* 50 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_55_CYCLE    = 0b1110;  /* 55 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_PERS_60_CYCLE    = 0b1111;  /* 60 clean channel values outside threshold range generates an interrupt */
    public static final int TCS34725_CONFIG           = 0x0D;
    public static final int TCS34725_CONFIG_WLONG     = 0x02;    /* Choose between short and long = 12x; wait times via TCS34725_WTIME */
    public static final int TCS34725_CONTROL          = 0x0F;    /* Set the gain level for the sensor */
    public static final int TCS34725_ID               = 0x12;    /* 0x44 = TCS34721/TCS34725, 0x4D = TCS34723/TCS34727 */
    public static final int TCS34725_STATUS           = 0x13;
    public static final int TCS34725_STATUS_AINT      = 0x10;    /* RGBC Clean channel interrupt */
    public static final int TCS34725_STATUS_AVALID    = 0x01;    /* Indicates that the RGBC channels have completed an integration cycle */
    public static final int TCS34725_CDATAL           = 0x14;    /* Clear channel data */
    public static final int TCS34725_CDATAH           = 0x15;
    public static final int TCS34725_RDATAL           = 0x16;    /* Red channel data */
    public static final int TCS34725_RDATAH           = 0x17;
    public static final int TCS34725_GDATAL           = 0x18;    /* Green channel data */
    public static final int TCS34725_GDATAH           = 0x19;
    public static final int TCS34725_BDATAL           = 0x1A;    /* Blue channel data */
    public static final int TCS34725_BDATAH           = 0x1B;

    public static final int ADDRESS_I2C         = TCS34725_ADDRESS * 2;
    public static final int IREG_READ_FIRST     = TCS34725_CDATAL;
    public static final int IREG_READ_LAST      = TCS34725_BDATAH;

    final I2cDeviceClient                       i2cDeviceClient;
    boolean                                     ledIsEnabled;
    boolean                                     ledStateIsKnown;
    I2cDeviceReplacementHelper<ColorSensor>     helper;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private AdaFruitTCS34725ColorSensor(OpMode context, I2cDeviceClient i2cDeviceClient, ColorSensor target, I2cController controller, int targetPort)
        {
        this.helper          = new I2cDeviceReplacementHelper<ColorSensor>(context, this, target, controller, targetPort);
        this.i2cDeviceClient = i2cDeviceClient;
        this.ledIsEnabled    = false;
        this.ledStateIsKnown = false;

        this.i2cDeviceClient.setReadWindow(new II2cDeviceClient.ReadWindow(
                IREG_READ_FIRST, IREG_READ_LAST - IREG_READ_FIRST + 1,
                II2cDeviceClient.READ_MODE.REPEAT));

        RobotStateTransitionNotifier.register(context, this);
        }

    public static ColorSensor create(OpMode context, ColorSensor target)
        {
        I2cController controller;
        int port;
        int i2cAddr8Bit;

        if (target instanceof AdafruitI2cColorSensor)
            {
            AdafruitI2cColorSensor colorTarget = (AdafruitI2cColorSensor)target;
            controller  = colorTarget.getI2cController();
            port        = colorTarget.getPort();
            i2cAddr8Bit = ADDRESS_I2C;
            }
        else
            throw new IllegalArgumentException(String.format("incorrect color sensor class: %s", target.getClass().getSimpleName()));

        return create(context, controller, port, i2cAddr8Bit, target);
        }

    public static ColorSensor create(OpMode context, I2cController controller, int port, int i2cAddr8Bit, ColorSensor target)
        {
        II2cDevice i2cDevice               = new I2cDeviceOnI2cDeviceController(controller, port);
        I2cDeviceClient i2cDeviceClient    = new I2cDeviceClient(context, i2cDevice, i2cAddr8Bit, false);
        AdaFruitTCS34725ColorSensor result = new AdaFruitTCS34725ColorSensor(context, i2cDeviceClient, target, controller, port);
        result.engage();
        result.initialize(new Parameters());
        return result;
        }

    public static class Parameters
        {
        public INTEGRATION_TIME integrationTime = INTEGRATION_TIME.MS_2_4;
        public GAIN             gain            = GAIN.X4;
        }

    public enum INTEGRATION_TIME
        {
        MS_2_4(0xFF), MS_24(0xF6), MS_50(0xEB), MS_101(0xD5), MS_154(0xC0), MS_700(0x00);
        public final byte byteVal;
        INTEGRATION_TIME(int i) { this.byteVal = (byte)i; }
        }

    public enum GAIN
        {
        X1(0x00), X4(0x01), X16(0x02), X60(0x03);
        public final byte byteVal;
        GAIN(int i) { this.byteVal = (byte)i; }
        }

    public void initialize(Parameters parameters)
        {
        // Verify that we're talking to whom we think we're talking to
        byte id = this.i2cDeviceClient.read8(TCS34725_ID);
        if (id != 0x44 && id != 0x10)
            throw new UnexpectedI2CDeviceException(id);

        // Set the gain an integration time
        this.i2cDeviceClient.write8(TCS34725_ATIME,   parameters.integrationTime.byteVal);
        this.i2cDeviceClient.write8(TCS34725_CONTROL, parameters.gain.byteVal);

        // Enable the device
        this.i2cDeviceClient.write8(TCS34725_ENABLE, TCS34725_ENABLE_PON);
        delayExtra(3);
        this.i2cDeviceClient.write8(TCS34725_ENABLE, TCS34725_ENABLE_PON | TCS34725_ENABLE_AEN);
        }

    void delayExtra(int ms)
        {
        delay(ms + 10);
        }

    void delay(int ms)
        {
        try
            {
            Thread.sleep(ms);
            }
        catch (InterruptedException e)
            {
            handleCapturedInterrupt(e);
            }
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
        return 1;
        }

    @Override public String getConnectionInfo()
        {
        return this.i2cDeviceClient.getConnectionInfo();
        }

    @Override public String getDeviceName()
        {
        return "Swerve AdaFruit I2C Color Sensor";
        }

    //----------------------------------------------------------------------------------------------
    // ColorSensor
    //----------------------------------------------------------------------------------------------

    int read8(int ireg)
        {
        byte b = this.i2cDeviceClient.read8(ireg);
        return TypeConversion.unsignedByteToInt(b);
        }

    int readLH(int ireg)
        {
        byte[] bytes = this.i2cDeviceClient.read(ireg, 2);
        return TypeConversion.byteArrayToInt(bytes, ByteOrder.LITTLE_ENDIAN);
        }

    @Override public synchronized int red()
        {
        return this.readLH(0x16);
        }

    @Override public synchronized int green()
        {
        return this.readLH(0x18);
        }

    @Override public synchronized int blue()
        {
        return this.readLH(0x1a);
        }

    @Override public synchronized int alpha()
        {
        return this.readLH(0x14);
        }

    @Override public synchronized int argb()
        {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
        }

    @Override public synchronized void enableLed(boolean enable)
    // We can't directly control the LED with I2C; it's always on
        {
        if (!this.ledStateIsKnown || this.ledIsEnabled != enable)
            {
            if (enable)
                {
                this.ledIsEnabled = enable;
                this.ledStateIsKnown = true;
                }
            else
                throw new IllegalArgumentException("disabling LED is not supported");
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

