package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Instances of AdaFruitBNO055IMU provide API access to an 
 * <a href="http://www.adafruit.com/products/2472">AdaFruit Absolute Orientation Sensor</a> that 
 * is attached to a Modern Robotics Core Device Interface module.
 */
public final class AdaFruitBNO055IMU implements IBNO055IMU, II2cDeviceClientUser
    {
    //------------------------------------------------------------------------------------------
    // State
    //------------------------------------------------------------------------------------------

    private II2cDeviceClient       deviceClient;
    private Parameters             parameters;
    private SuicideWatch           suicideWatch;
    private SENSOR_MODE            currentMode;

    private final Object           dataLock = new Object();
    private Acceleration           acceleration;
    private Velocity               velocity;
    private Position               position;

    private HandshakeThreadStarter accelerationIntegration;
    private static final int       msAccelerationIntegrationStopWait = 20;
    private static final int       msAwaitSelfTest                   = 500;

    // we poll essentially as fast as we can, since measurements show that
    // we only get an I2C cycle every 70 ms or so. 'no point in waiting longer.
    private static final int       msAccelerationIntegrationDefaultPollInterval = 0;    // ASAP

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /** 
     * Instantiate an AdaFruitBNO055IMU on the indicated device whose I2C address is the one indicated.
     */
    public AdaFruitBNO055IMU(I2cDevice i2cDevice, int i2cAddr8Bit)
        {
        this.deviceClient = ClassFactory.createI2cDeviceClient(i2cDevice, i2cAddr8Bit, lowerWindow);
        this.parameters   = null;
        this.currentMode  = null;
        this.acceleration = null;
        this.velocity     = new Velocity();
        this.position     = new Position();
        this.accelerationIntegration = null;
        this.suicideWatch = null;
        }

    /**
     * Instantiate an AdaFruitBNO055IMU and then initialize it with the indicated set of parameters.
     */
    public static IBNO055IMU create(I2cDevice i2cDevice, Parameters parameters)
        {
        // Create a sensor which is a client of i2cDevice
        IBNO055IMU result = new AdaFruitBNO055IMU(i2cDevice, parameters.i2cAddr8Bit.bVal);
        
        // Initialize it with the indicated parameters
        result.initialize(parameters);
        return result;
        }

    //------------------------------------------------------------------------------------------
    // II2cDeviceClientUser
    //------------------------------------------------------------------------------------------

    @Override public II2cDeviceClient getI2cDeviceClient()
        {
        return this.deviceClient;
        }
    
    //------------------------------------------------------------------------------------------
    // IBNO055IMU initialization
    //------------------------------------------------------------------------------------------
    
    /**
     * Initialize the device to be running in the indicated operation mode
     */
    public void initialize(Parameters parameters)
        {
        // Remember the parameters for future use
        this.parameters = parameters;

        // Turn on the logging (or not) so we can see what happens
        this.getI2cDeviceClient().setLoggingEnabled(parameters.loggingEnabled);

        // Lore: "send a throw-away command [...] just to make sure the BNO is in a good state
        // and ready to accept commands (this seems to be necessary after a hard power down)."
        write8(REGISTER.PAGE_ID, 0);

        // Make sure we have the right device
        byte id = read8(REGISTER.CHIP_ID); 
        if (id != bCHIP_ID_VALUE)
            {
            delay(650); // delay value is from from Table 0-2
            id = read8(REGISTER.CHIP_ID);
            if (id != bCHIP_ID_VALUE)
                throw new UnexpectedI2CDeviceException(id);
            }
        
        // Switch to config mode (just in case, since this is the default)
        setSensorMode(SENSOR_MODE.CONFIG);
        
        // Reset the system, and wait for the chip id register to switch back from its reset state 
        // to the it's chip id state. This can take a very long time, some 650ms (Table 0-2, p13) 
        // perhaps. While in the reset state the chip id (and other registers) reads as 0xFF.
        write8(REGISTER.SYS_TRIGGER, 0x20);
        while (read8(REGISTER.CHIP_ID) != bCHIP_ID_VALUE)
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
        int unitsel = (parameters.pitchmode.bVal << 7) |       // pitch angle convention
                      (parameters.temperatureUnit.bVal << 4) | // temperature
                      (parameters.angleunit.bVal << 2) |       // euler angle units
                      (parameters.angleunit.bVal << 1) |       // gyro units, per second
                      (parameters.accelunit.bVal /*<< 0*/);    // accelerometer units
        write8(REGISTER.UNIT_SEL, unitsel);
        
        // Use or don't use the external crystal
        // See Section 5.5 (p100) of the BNO055 specification.
        write8(REGISTER.SYS_TRIGGER, parameters.useExternalCrystal ? 0x80 : 0x00);
        delayLore(10);

        // Run a self test. This appears to be a necessary step in order for the 
        // sensor to be able to actually be used.
        write8(REGISTER.SYS_TRIGGER, read8(REGISTER.SYS_TRIGGER) | 0x01);
        ElapsedTime time = new ElapsedTime();
        boolean selfTestSuccessful = false;
        while (!selfTestSuccessful && time.time()*1000 < msAwaitSelfTest)
            {
            selfTestSuccessful = (read8(REGISTER.SELFTEST_RESULT)&0x0F) == 0x0F;
            }
        if (!selfTestSuccessful)
            throw new BNO055InitializationException(this, "self test failed");
        
        // Finally, enter the requested operating mode (see section 3.3)
        setSensorMode(parameters.mode);
        delayLore(20);
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

    public synchronized byte getSystemStatus()
        {
        return read8(REGISTER.SYS_STAT);
        }

    public synchronized byte getSystemError()
        {
        return read8(REGISTER.SYS_ERR);
        }

    //------------------------------------------------------------------------------------------
    // Calibration
    //------------------------------------------------------------------------------------------

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
        return ((b/*>>0*/) & 0x03) == 0x03;
        }

    public byte[] readCalibrationData()
        {
        // From Section 3.11.4 of the datasheet:
        //
        // "The calibration profile includes sensor offsets and sensor radius. Host system can
        // read the offsets and radius only after a full calibration is achieved and the operation
        // mode is switched to CONFIG_MODE. Refer to sensor offsets and sensor radius registers."

        SENSOR_MODE prevMode = this.currentMode;
        setSensorMode(SENSOR_MODE.CONFIG);

        // Read the calibration data
        byte[] result = this.read(REGISTER.ACCEL_OFFSET_X_LSB, cbCalibrationData);

        // Restore the previous mode and return
        setSensorMode(prevMode);
        return result;
        }

    public void writeCalibrationData(byte[] data)
        {
        // Section 3.11.4:
        //
        // It is important that the correct offsets and corresponding sensor radius are used.
        // Incorrect offsets may result in unreliable orientation data even at calibration
        // accuracy level 3. To set the calibration profile the following steps need to be taken
        //
        //    1. Select the operation mode to CONFIG_MODE
        //    2. Write the corresponding sensor offsets and radius data
        //    3. Change operation mode to fusion mode

        if (data.length != cbCalibrationData)
            throw new IllegalArgumentException(String.format("illegal calibration data size: %d; expected: %d", data.length, cbCalibrationData));

        SENSOR_MODE prevMode = this.currentMode;
        setSensorMode(SENSOR_MODE.CONFIG);

        // Write the calibration data
        this.write(REGISTER.ACCEL_OFFSET_X_LSB, data);

        // Restore the previous mode and return
        setSensorMode(prevMode);
        }

    //------------------------------------------------------------------------------------------
    // IBNO055IMU data retrieval
    //------------------------------------------------------------------------------------------

    public synchronized double getTemperature()
        {
        byte b = this.read8(REGISTER.TEMP);
        return (double)b;
        }

    public synchronized MagneticFlux getMagneticFieldStrength()
        {
        return new MagneticFlux(getVector(VECTOR.MAGNETOMETER), getFluxScale());
        }
    public synchronized Acceleration getAcceleration()
        {
        return new Acceleration(getVector(VECTOR.ACCELEROMETER), getAccelerationScale());
        }
    public synchronized Acceleration getLinearAcceleration()
        {
        return new Acceleration(getVector(VECTOR.LINEARACCEL), getAccelerationScale());
        }
    public synchronized Acceleration getGravity()
        {
        return new Acceleration(getVector(VECTOR.GRAVITY), getAccelerationScale());
        }
    public synchronized AngularVelocity getAngularVelocity()
        {
        return new AngularVelocity(getVector(VECTOR.GYROSCOPE), getAngularScale());
        }
    public synchronized EulerAngles getAngularOrientation()
        {
        return new EulerAngles(getVector(VECTOR.EULER), getAngularScale());
        }

    public synchronized Quaternion getQuaternionOrientation()
        {
        // Ensure we can see the registers we need
        this.deviceClient.ensureReadWindow(
                new I2cDeviceClient.RegWindow(REGISTER.QUATERNION_DATA_W_LSB.bVal, 8),
                upperWindow
        );
        
        // Section 3.6.5.5 of BNO055 specification
        II2cDeviceClient.TimestampedData ts = this.deviceClient.readTimeStamped(REGISTER.QUATERNION_DATA_W_LSB.bVal, 8);
        final double scale = 1.0 / (1 << 14);
        Quaternion result = new Quaternion(
                Util.makeIntLittle(ts.data[0], ts.data[1]) * scale,
                Util.makeIntLittle(ts.data[2], ts.data[3]) * scale,
                Util.makeIntLittle(ts.data[4], ts.data[5]) * scale,
                Util.makeIntLittle(ts.data[6], ts.data[7]) * scale
            );
        result.nanoTime = ts.nanoTime;
        return result;
        }


    /**
     * Return the number by which we need to divide a raw angle as read from the device in order
     * to convert it to our current angular units. See Table 3-22 of the BNO055 spec
     */
    private double getAngularScale()
        {
        return this.parameters.angleunit == ANGLEUNIT.DEGREES ? 16.0 : 900.0;
        }

    /**
     * Return the number by which we need to divide a raw acceleration as read from the device in order
     * to convert it to our current angular units. See Table 3-17 of the BNO055 spec.
     */
    private double getAccelerationScale()
        {
        return this.parameters.accelunit == ACCELUNIT.METERS_PERSEC_PERSEC ? 100.0 : 1.0;
        }

    /**
     * Return the number by which we need to divide a raw acceleration as read from the device in order
     * to convert it to our current angular units. See Table 3-19 of the BNO055 spec. Note that the
     * BNO055 natively uses micro Teslas; we instead use Teslas.
     */
    private double getFluxScale()
        {
        return 16.0 * 1000000.0;
        }

    private II2cDeviceClient.TimestampedData getVector(VECTOR vector)
        {
        // Ensure that the 6 bytes for this vector are visible in the register window.
        this.ensureRegisterWindow(new I2cDeviceClient.RegWindow(vector.getValue(), 6));

        // Read the data
        return this.deviceClient.readTimeStamped(vector.getValue(), 6);
        }

    //------------------------------------------------------------------------------------------
    // Position and velocity management
    //------------------------------------------------------------------------------------------
    
    public Velocity getVelocity()
        {
        synchronized (dataLock)
            {
            return this.velocity;
            }
        }
    public Position getPosition()
        {
        synchronized (dataLock)
            {
            return this.position;
            }
        }
    public void setPositionAndVelocity(Position position, Velocity velocity)
        {
        synchronized (dataLock)
            {
            if (position != null)
                this.position = position;
            if (velocity != null)
                this.velocity = velocity;
            }
        }
    
    public synchronized void startAccelerationIntegration(Position initalPosition, Velocity initialVelocity)
        {
        this.startAccelerationIntegration(initalPosition, initialVelocity, msAccelerationIntegrationDefaultPollInterval);
        }

    public synchronized void startAccelerationIntegration(Position initalPosition, Velocity initialVelocity, int msPollInterval)
    // Start integrating acceleration to determine position and velocity by polling for acceleration every while
        {
        // Stop doing this if we're already in flight
        this.stopAccelerationIntegration();

        // Set the current position and velocity if asked
        this.setPositionAndVelocity(initalPosition, initialVelocity);
        
        // Make a new thread on which to do the integration        
        this.accelerationIntegration = new HandshakeThreadStarter("integrator", new Integrator(msPollInterval));
        
        // Set up a suicide watch that will shutdown that integration thread if
        // the I2cDevice is shutdown. This is a bit of a hack, perhaps, but
        // is the only way given the current architecture that we've figured out
        // how to auto-stop integration if the robot is shutdown
        while (this.deviceClient.getCallbackThread() == null)
            {
            // Don't yet know the callback thread. Spin until we do.
            Thread.yield();
            }
        this.suicideWatch = new SuicideWatch(this.deviceClient.getCallbackThread(), this.accelerationIntegration);
        this.suicideWatch.start();
        
        // Start the whole schebang a rockin...
        this.accelerationIntegration.start();
        }
    
    public synchronized void stopAccelerationIntegration()
        {
        if (this.accelerationIntegration != null)
            {
            // Disarm our monitor
            this.suicideWatch.stop(msAccelerationIntegrationStopWait);
            this.suicideWatch = null;
            
            // Stop the integration thread
            this.accelerationIntegration.stop(msAccelerationIntegrationStopWait);
            this.accelerationIntegration = null;
            }
        }

    /** Integrator maintains current velocity and position by integrating off of acceleration */
    class Integrator implements IHandshakeable
        {
        private final int msPollInterval;
        
        Integrator(int msPollInterval)
            {
            this.msPollInterval = msPollInterval;
            }
        
        @Override public void run(HandshakeThreadStarter starter)
            {
            // Let the starter know we're up and running
            starter.handshake();

            // Any extant acceleration is stale. Null it out so that we will have
            // fresh and accurate intervals
            synchronized (dataLock)
                {
                acceleration = null;                
                }
            
            // Don't let inappropriate exceptions sneak out
            try
                {
                // Loop until we're asked to stop
                while (!starter.stopRequested())
                    {
                    // Read the latest available acceleration
                    Acceleration accelNext = AdaFruitBNO055IMU.this.getLinearAcceleration();
                    
                    // Update our state variables based thereon
                    synchronized (dataLock)
                        {
                        // We can only integrate if we have a previous acceleration to baseline from
                        if (acceleration != null && acceleration.nanoTime != 0)
                            {
                            Acceleration accelPrev    = acceleration;
                            Velocity     velocityPrev = velocity;
                            
                            acceleration = accelNext;

                            Velocity deltaVelocity = acceleration.integrate(accelPrev);                         
                            velocity.accumulate(deltaVelocity);
                            
                            Position deltaPosition = velocity.integrate(velocityPrev);
                            position.accumulate(deltaPosition);
                            }
                        else
                            {
                            acceleration = accelNext;
                            }
                        }
                    
                    // Wait a bit before polling again
                    if (msPollInterval > 0)
                        Thread.sleep(msPollInterval);
                    }
                }
            catch (InterruptedException|RuntimeInterruptedException ignored)
                {
                Util.handleCapturedInterrupt();
                }
            }
        }

    //------------------------------------------------------------------------------------------
    // Internal utility
    //------------------------------------------------------------------------------------------

    /**
     * One of two primary register windows we use for reading from the BNO055.
     * 
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
     * A second of two primary register windows we use for reading from the BNO055.
     * We'd like to include the temperature register, too, but that would make a 27-byte window, and
     * those don't (currently) work in the CDIM.
     *
     * @see #lowerWindow
     */
    private static final I2cDeviceClient.RegWindow upperWindow = newWindow(REGISTER.EULER_H_LSB, REGISTER.TEMP);
    
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
        this.deviceClient.ensureReadWindow(needed, set);
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
            Util.handleCapturedInterrupt();
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
            Util.handleCapturedInterrupt();
            }
        }

    public synchronized byte read8(REGISTER reg)
        {
        this.ensureRegisterWindow(new I2cDeviceClient.RegWindow(reg.bVal, 1));
        return this.deviceClient.read8(reg.bVal);
        }
    public synchronized byte[] read(REGISTER reg, int cb)
        {
        this.ensureRegisterWindow(new I2cDeviceClient.RegWindow(reg.bVal, cb));
        return this.deviceClient.read(reg.bVal, cb);
        }

    public void write8(REGISTER reg, int data)
        {
        this.deviceClient.write8(reg.bVal, data);
        }
    public void write(REGISTER reg, byte[] data)
        {
        this.deviceClient.write(reg.bVal, data);
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

    final static byte bCHIP_ID_VALUE = (byte)0xa0;

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

// This code is in part modelled after https://github.com/adafruit/Adafruit_BNO055

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
