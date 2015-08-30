package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Instances of AdaFruitBNO055IMU provide API access to an 
 * <a href="http://www.adafruit.com/products/2472">AdaFruit Absolute Orientation Sensor</a> that 
 * is attached to a Modern Robotics Core Device Interface module.
 */
public final class AdaFruitBNO055IMU implements IBNO055IMU
    {
    //------------------------------------------------------------------------------------------
    // State
    //------------------------------------------------------------------------------------------

    private II2cDeviceClient deviceClient;
    private SENSOR_MODE      currentMode;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /** 
     * Instantiate an AdaFruitBNO055IMU on the indicated device whose I2C address is the one
     * indicated.
     */
    public AdaFruitBNO055IMU(I2cDevice i2cDevice, int i2cAddr)
        {
        this.deviceClient = ClassFactory.createI2cDeviceClient(i2cDevice, lowerWindow, i2cAddr);
        this.currentMode  = null;
        }

    /**
     * Instantiate and initialize an AdaFruitBNO055IMU with default parameters
     */
    public static IBNO055IMU create(I2cDevice i2cDevice)
        {
        // Default to the fusion 'IMU' mode
        return create(i2cDevice, new Parameters());
        }

    /**
     * Instantiate and initialize an AdaFruitBNO055IMU with the indicated set of parameters
     */
    public static IBNO055IMU create(I2cDevice i2cDevice, Parameters parameters)
        {
        // Create a sensor which is a client of i2cDevice
        IBNO055IMU result = new AdaFruitBNO055IMU(i2cDevice, parameters.i2cAddr.bVal);
        
        // Initialize it with the indicated parameters
        result.initialize(parameters);
        return result;
        }
    
    //------------------------------------------------------------------------------------------
    // Operations
    //------------------------------------------------------------------------------------------
    
    /**
     * Initialize the device to be running in the indicated operation mode
     */
    public void initialize(Parameters parameters)
        {
        // Make sure we have the right device
        byte id = read8(REGISTER.CHIP_ID); 
        if (id != ID)
            {
            delay(1000); // hold on for boot
            id = read8(REGISTER.CHIP_ID);
            if (id != ID)
                throw new UnexpectedI2CDeviceException(id);
            }
        
        // Switch to config mode (just in case, since this is the default)
        setSensorMode(SENSOR_MODE.CONFIG);
        
        // Reset the system, and wait for the chip id register to switch
        // back from its reset state (0xA0?) to the it's chip id state (also 0xA0?; hmmm
        // something is odd there). This can typically take 650ms (Table 0-2, p13).
        write8(REGISTER.SYS_TRIGGER, 0x20);
        while (read8(REGISTER.CHIP_ID) != ID)
            {
            delay(10);
            }
        delayLore(50);
        
        // Set to normal power mode
        write8(REGISTER.PWR_MODE, POWER_MODE.NORMAL.getValue());
        delayLore(10);

        // Make sure we're looking at register page zero, as the other registers
        // we need to set here are on that page.
        write8(REGISTER.PAGE_ID, 0);
        
        // Set the output units. Section 3.6, p31
        int unitsel = (parameters.pitchmode.bVal << 7) |       // ptich angle convention
                      (parameters.temperatureUnit.bVal << 4) | // temperature
                      (parameters.angleunit.bVal << 2) |       // euler angle units
                      (parameters.angleunit.bVal << 1) |       // gyro units, per second
                      (parameters.accelunit.bVal /*<< 0*/);    // accelerometer units
        write8(REGISTER.UNIT_SEL, unitsel);
        
        // ??? what does this do ???
        write8(REGISTER.SYS_TRIGGER, 0x0);
        delayLore(10);
        
        // Set the requested operating mode (see section 3.3)
        setSensorMode(parameters.mode);
        delayLore(20);
        
        // Use or don't use the external cyrstal
        setExternalCrystalUse(parameters.useExternalCrystal);
        }

    private void setSensorMode(SENSOR_MODE mode)
    /* The default operation mode after power-on is CONFIGMODE. When the user changes to another 
    operation mode, the sensors which are required in that particular sensor mode are powered, 
    while the sensors whose signals are not required are set to suspend mode. */
        {
        // Remember the mode, 'cause that's easy
        this.currentMode = mode;
        
        // Actually change the operation/sensor mode
        this.write8(REGISTER.OPR_MODE, mode.bVal & 0x0F);
        
        // Delay per Table 3-6 of BNO055 Data sheet (p21)
        if (mode == SENSOR_MODE.CONFIG)
            delay(19);
        else
            delay(7);
        }

    /**
     * Use or don't use the external 32.768khz crystal.
     * 
     * See Section 5.5 (p100) of the BNO055 specification.
     */
    public synchronized void setExternalCrystalUse(final boolean useExternalCrystal)
        {
        this.enterConfigModeFor(new IAction()
            {
            @Override public void doAction()
                {
                write8(REGISTER.PAGE_ID, 0);
                if (useExternalCrystal)
                    {
                    write8(REGISTER.SYS_TRIGGER, 0x80);
                    }
                else
                    {
                    write8(REGISTER.SYS_TRIGGER, 0x00);
                    }
                delayLore(10);
                }
            });
        }
    
    public synchronized byte getSystemStatus()
        {
        // It's unclear why we have to be in config mode to read the status,
        // but that's what the AdaFruit library did, so we follow that for now
        // until we might find we can do without.
        return this.enterConfigModeFor(new IFunc<Byte>()
            {
            @Override public Byte value()
                {
                return read8(REGISTER.SYS_STAT);
                }
            });
        }

    public synchronized byte getSystemError()
        {
        // It's unclear why we have to be in config mode to read the error,
        // but that's what the AdaFruit library did, so we follow that for now
        // until we might find we can do without.
        return this.enterConfigModeFor(new IFunc<Byte>()
            {
            @Override public Byte value()
                {
                return read8(REGISTER.SYS_STAT);
                }
            });
        }

    public synchronized boolean isSystemCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT);
        return ((b>>6) & 0x03) == 0x03;
        }

    public synchronized boolean isGyroCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT);
        return ((b>>4) & 0x03) == 0x03;
        }

    public synchronized boolean isAccelerometerCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT);
        return ((b>>2) & 0x03) == 0x03;
        }

    public synchronized boolean isMagnetometerCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT);
        return ((b>>0) & 0x03) == 0x03;
        }
    
    public synchronized double getTemperature()
        {
        byte b = this.read8(REGISTER.TEMP);
        return (double)b;
        }
    
    public synchronized MagneticFlux getMagneticFieldStrength()
        {
        return new MagneticFlux(getVector(VECTOR.MAGNETOMETER, 16. * 1000000));
        }
    public synchronized Acceleration getAcceleration()
        {
        return new Acceleration(getVector(VECTOR.ACCELEROMETER, 100));
        }
    public synchronized Acceleration getLinearAcceleration()
        {
        return new Acceleration(getVector(VECTOR.LINEARACCEL, 100));
        }
    public synchronized Acceleration getGravity()
        {
        return new Acceleration(getVector(VECTOR.GRAVITY, 100));
        }
    public synchronized AngularVelocity getAngularVelocity()
        {
        return new AngularVelocity(getVector(VECTOR.GYROSCOPE, 900));
        }
    public synchronized EulerAngles getAngularOrientation()
        {
        return new EulerAngles(getVector(VECTOR.EULER, 900));
        }
    private II2cDeviceClient.TimestampedData getVector(VECTOR vector, double scale)
        {
        // Ensure that the 6 bytes for this vector are visible in the register window. 
        this.ensureRegisterWindow(new I2cDeviceClient.RegWindow(vector.getValue(), 6));

        // Read the data
        return this.deviceClient.readTimeStamped(vector.getValue(), 6);
        }

    public synchronized Quaternion getQuaternionOrientation()
        {
        // Ensure we can see the registers we need
        this.deviceClient.ensureRegisterWindow(
                new I2cDeviceClient.RegWindow(REGISTER.QUATERNION_DATA_W_LSB.bVal, 8),
                upperWindow
            );
        
        // Section 3.6.5.5 of BNO055 specification
        II2cDeviceClient.TimestampedData ts = this.deviceClient.readTimeStamped(REGISTER.QUATERNION_DATA_W_LSB.bVal, 8);
        final double scale = 1.0 / (1 << 14);
        Quaternion result = new Quaternion(
                Util.makeInt(ts.data[0], ts.data[1]) * scale,
                Util.makeInt(ts.data[2], ts.data[3]) * scale,
                Util.makeInt(ts.data[4], ts.data[5]) * scale,
                Util.makeInt(ts.data[6], ts.data[7]) * scale
            );
        result.nanoTime = ts.nanoTime;
        return result;
        }
    
    //------------------------------------------------------------------------------------------
    // Internal utility
    //------------------------------------------------------------------------------------------

    /**
     * Given the maximum allowable size of a register window, the set of registers on 
     * a BNO055 can be usefully divided into two windows, which we here call lowerWindow
     * and upperWindow. 
     * 
     * When we find the need to change register windows depending on what data is being requested
     * from the sensor, we try to use these two windows so as to reduce the number of register
     * window switching that might be required as other data is read in the future.
     */
    private static final I2cDeviceClient.RegWindow lowerWindow = newWindow(REGISTER.CHIP_ID, REGISTER.EULER_H_LSB);
    /**
     * @see #lowerWindow
     */
    private static final I2cDeviceClient.RegWindow upperWindow = newWindow(REGISTER.EULER_H_LSB, REGISTER.CALIB_STAT);;
    private static I2cDeviceClient.RegWindow newWindow(REGISTER regFirst, REGISTER regMax)
        {
        return new I2cDeviceClient.RegWindow(regFirst.bVal, regMax.bVal-regFirst.bVal);
        }

    private void ensureRegisterWindow(I2cDeviceClient.RegWindow needed)
        {
        I2cDeviceClient.RegWindow set = lowerWindow.contains(needed)
            ? lowerWindow
            : upperWindow.contains(needed)
                ? upperWindow
                : needed;           // just use what's needed if it's not within our two main windows
        this.deviceClient.ensureRegisterWindow(needed, set);
        }

    /**
     * delayLore() implements a delay that only known by lore and mythology to be necessary.
     * 
     * @see #delay(int) 
     */
    private void delayLore(int ms)
        {
        try
            {
            Thread.sleep(ms);
            }
        catch (InterruptedException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    /**
     * delay() implements delays which are known to be necessary according to the BNO055 specification
     * 
     * @see #delayLore(int) 
     */
    private void delay(int ms)
        {
        try
            {
            Thread.sleep(ms);
            }
        catch (InterruptedException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    public synchronized byte read8(REGISTER reg)
        {
        this.ensureRegisterWindow(new I2cDeviceClient.RegWindow(reg.bVal, 1));
        return this.deviceClient.read8(reg.bVal);
        }
    
    public void write8(REGISTER reg, int data)
        {
        this.deviceClient.write8(reg.bVal, data);
        }
    
    private void enterConfigModeFor(IAction action)
        {
        SENSOR_MODE modePrev = this.currentMode;
        setSensorMode(SENSOR_MODE.CONFIG);
        delayLore(25);
        try
            {
            action.doAction();
            }
        finally
            {
            setSensorMode(modePrev);
            delayLore(20);
            }
        }

    private <T> T enterConfigModeFor(IFunc<T> lambda)
        {
        T result;
        
        SENSOR_MODE modePrev = this.currentMode;
        setSensorMode(SENSOR_MODE.CONFIG);
        delayLore(25);
        try
            {
            result = lambda.value();
            }
        finally
            {
            setSensorMode(modePrev);
            delayLore(20);
            }
        //
        return result;
        }
    
    //------------------------------------------------------------------------------------------
    // Constants
    //------------------------------------------------------------------------------------------

    final static int ADDRESS_A = 0x28;
    final static int ADDRESS_B = 0x29;
    final static int ID        = 0xa0;

    enum VECTOR
        {
            ACCELEROMETER   (REGISTER.ACCEL_DATA_X_LSB),
            MAGNETOMETER    (REGISTER.MAG_DATA_X_LSB),
            GYROSCOPE       (REGISTER.GYRO_DATA_X_LSB),
            EULER           (REGISTER.EULER_H_LSB),
            LINEARACCEL     (REGISTER.LINEAR_ACCEL_DATA_X_LSB),
            GRAVITY         (REGISTER.GRAVITY_DATA_X_LSB);
        //------------------------------------------------------------------------------------------
        private byte value;
        private VECTOR(int value) { this.value = (byte)value; }
        private VECTOR(REGISTER register) { this(register.bVal);}
        public byte getValue() { return this.value; }
        }

    enum POWER_MODE
        {
            NORMAL(0X00),
            LOWPOWER(0X01),
            SUSPEND(0X02);
        //------------------------------------------------------------------------------------------
        private byte value;
        private POWER_MODE(int value) { this.value = (byte)value; }
        public byte getValue() { return this.value; }
        }

    }

// This code is modelled after https://github.com/adafruit/Adafruit_BNO055

/***************************************************************************
 This is a library for the BNO055 orientation sensor

 Designed specifically to work with the Adafruit BNO055 Breakout.

 Pick one up today in the adafruit shop!
 ------> http://www.adafruit.com/products

 These sensors use I2C to communicate, 2 pins are required to interface.

 Adafruit invests time and resources providing this open source code,
 please support Adafruit andopen-source hardware by purchasing products
 from Adafruit!

 Written by KTOWN for Adafruit Industries.

 MIT license, all text above must be included in any redistribution
 ***************************************************************************/
