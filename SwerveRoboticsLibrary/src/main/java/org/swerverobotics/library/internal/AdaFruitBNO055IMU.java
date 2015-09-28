package org.swerverobotics.library.internal;

import android.util.Log;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import static org.swerverobotics.library.internal.Util.*;
import static junit.framework.Assert.*;
import static org.swerverobotics.library.interfaces.NavUtil.*;

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
    private DeathWatch             deathWatch;
    private SENSOR_MODE            currentMode;

    private final Object           dataLock = new Object();
    private IAccelerationIntegrator accelerationAlgorithm;

    private final Object           startStopLock = new Object();
    private HandshakeThreadStarter accelerationMananger;
    private static final int       msAccelerationIntegrationStopWait = 20;
    private static final int       msAwaitChipId                     = 2000;
    private static final int       msAwaitSelfTest                   = 500;

    // We always read as much as we can when we have nothing else to do
    private static final II2cDeviceClient.READ_MODE readMode = II2cDeviceClient.READ_MODE.REPEAT;

    // There is a big difference in the polling interval we see when debugging
    // and what we see when running free. The former is ~70ms, the latter about ~15ms.
    // That's just a bit of contextual background for the magnitude here
    private static final int       msAccelerationIntegrationDefaultPollInterval = 100;   // so far only a guess

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /** 
     * Instantiate an AdaFruitBNO055IMU on the indicated device whose I2C address is the one indicated.
     */
    public AdaFruitBNO055IMU(I2cDevice i2cDevice, int i2cAddr8Bit)
        {
        this.deviceClient = ClassFactory.createI2cDeviceClient(ClassFactory.createI2cDevice(i2cDevice), i2cAddr8Bit);
        this.deviceClient.setReadWindow(lowerWindow);
        this.parameters   = null;
        this.currentMode  = null;
        this.accelerationAlgorithm = new NaiveAccelerationIntegrator();
        this.accelerationMananger = null;
        this.deathWatch   = null;
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
        ElapsedTime elapsed = new ElapsedTime();
        if (parameters.accelerationIntegrationAlgorithm != null)
            this.accelerationAlgorithm = parameters.accelerationIntegrationAlgorithm;

        // Propagate relevant parameters to our device client
        this.getI2cDeviceClient().setLogging(parameters.loggingEnabled);
        this.getI2cDeviceClient().setLoggingTag(parameters.loggingTag);
        this.getI2cDeviceClient().setThreadPriorityBoost(parameters.threadPriorityBoost);

        // Lore: "send a throw-away command [...] just to make sure the BNO is in a good state
        // and ready to accept commands (this seems to be necessary after a hard power down)."
        write8(REGISTER.PAGE_ID, 0);

        // Make sure we have the right device
        byte chipId = read8(REGISTER.CHIP_ID);
        if (chipId != bCHIP_ID_VALUE)
            {
            delayExtra(650);     // delay value is from from Table 0-2
            chipId = read8(REGISTER.CHIP_ID);
            if (chipId != bCHIP_ID_VALUE)
                throw new UnexpectedI2CDeviceException(chipId);
            }
        
        // Make sure we are in config mode
        setSensorMode(SENSOR_MODE.CONFIG);
        
        // Reset the system, and wait for the chip id register to switch back from its reset state 
        // to the it's chip id state. This can take a very long time, some 650ms (Table 0-2, p13) 
        // perhaps. While in the reset state the chip id (and other registers) reads as 0xFF.
        elapsed.reset();
        write8(REGISTER.SYS_TRIGGER, 0x20);
        for (;;)
            {
            chipId = read8(REGISTER.CHIP_ID);
            if (chipId == bCHIP_ID_VALUE)
                break;
            delayExtra(10);
            if (milliseconds(elapsed) > msAwaitChipId)
                throw new BNO055InitializationException(this, "failed to retrieve chip id");
            }
        delayLoreExtra(50);
        
        // Set to normal power mode
        write8(REGISTER.PWR_MODE, POWER_MODE.NORMAL.getValue());
        delayLoreExtra(10);

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
        delayLoreExtra(10);

        // Run a self test. This appears to be a necessary step in order for the 
        // sensor to be able to actually be used.
        write8(REGISTER.SYS_TRIGGER, read8(REGISTER.SYS_TRIGGER) | 0x01);           // SYS_TRIGGER=0x3F
        elapsed.reset();
        boolean selfTestSuccessful = false;
        while (!selfTestSuccessful && milliseconds(elapsed) < msAwaitSelfTest)
            {
            selfTestSuccessful = (read8(REGISTER.SELFTEST_RESULT)&0x0F) == 0x0F;    // SELFTEST_RESULT=0x36
            }
        if (!selfTestSuccessful)
            throw new BNO055InitializationException(this, "self test failed");

        if (this.parameters.calibrationData != null)
            writeCalibrationData(this.parameters.calibrationData);

        // Finally, enter the requested operating mode (see section 3.3)
        setSensorMode(parameters.mode);
        delayLoreExtra(200);
        }

    @Override public void close()
        {
        stopAccelerationIntegration();
        this.deviceClient.close();
        }

    private void setSensorMode(SENSOR_MODE mode)
    /* The default operation mode after power-on is CONFIGMODE. When the user changes to another 
    operation mode, the sensors which are required in that particular sensor mode are powered, 
    while the sensors whose signals are not required are set to suspend mode. */
        {
        // Remember the mode, 'cause that's easy
        this.currentMode = mode;
        
        // Actually change the operation/sensor mode
        this.write8(REGISTER.OPR_MODE, mode.bVal & 0x0F);                           // OPR_MODE=0x3D
        
        // Delay per Table 3-6 of BNO055 Data sheet (p21)
        if (mode == SENSOR_MODE.CONFIG)
            delayExtra(19);
        else
            delayExtra(7);
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
        if (prevMode != SENSOR_MODE.CONFIG) setSensorMode(SENSOR_MODE.CONFIG);

        // Read the calibration data
        byte[] result = this.read(REGISTER.ACCEL_OFFSET_X_LSB, cbCalibrationData);

        // Restore the previous mode and return
        if (prevMode != SENSOR_MODE.CONFIG) setSensorMode(prevMode);
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
        if (prevMode != SENSOR_MODE.CONFIG) setSensorMode(SENSOR_MODE.CONFIG);

        // Write the calibration data
        this.write(REGISTER.ACCEL_OFFSET_X_LSB, data);

        // Restore the previous mode and return
        if (prevMode != SENSOR_MODE.CONFIG) setSensorMode(prevMode);
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
    public synchronized Acceleration getOverallAcceleration()
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
        return this.deviceClient.executeFunctionWhileLocked(new IFunc<Quaternion>()
            {
            @Override public Quaternion value()
                {
                // Ensure we can see the registers we need
                deviceClient.ensureReadWindow(
                        new II2cDeviceClient.ReadWindow(REGISTER.QUATERNION_DATA_W_LSB.bVal, 8, readMode),
                        upperWindow);

                // Section 3.6.5.5 of BNO055 specification
                II2cDeviceClient.TimestampedData ts = deviceClient.readTimeStamped(REGISTER.QUATERNION_DATA_W_LSB.bVal, 8);
                return new Quaternion(ts, (1 << 14));
                }
            });
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

    private II2cDeviceClient.TimestampedData getVector(final VECTOR vector)
        {
        return this.deviceClient.executeFunctionWhileLocked(new IFunc<II2cDeviceClient.TimestampedData>()
            {
            @Override public II2cDeviceClient.TimestampedData value()
                {
                // Ensure that the 6 bytes for this vector are visible in the register window.
                ensureReadWindow(new II2cDeviceClient.ReadWindow(vector.getValue(), 6, readMode));

                // Read the data
                return deviceClient.readTimeStamped(vector.getValue(), 6);
                }
            });
        }

    //------------------------------------------------------------------------------------------
    // Position and velocity management
    //------------------------------------------------------------------------------------------
    
    public Acceleration getAcceleration()
        {
        synchronized (dataLock)
            {
            Acceleration result = this.accelerationAlgorithm.getAcceleration();
            if (result == null) result = new Acceleration();
            return result;
            }
        }
    public Velocity getVelocity()
        {
        synchronized (dataLock)
            {
            Velocity result = this.accelerationAlgorithm.getVelocity();
            if (result == null) result = new Velocity();
            return result;
            }
        }
    public Position getPosition()
        {
        synchronized (dataLock)
            {
            Position result = this.accelerationAlgorithm.getPosition();
            if (result == null) result = new Position();
            return result;
            }
        }

    public void startAccelerationIntegration(Position initalPosition, Velocity initialVelocity)
        {
        this.startAccelerationIntegration(initalPosition, initialVelocity, msAccelerationIntegrationDefaultPollInterval);
        }

    public void startAccelerationIntegration(Position initalPosition, Velocity initialVelocity, int msPollInterval)
    // Start integrating acceleration to determine position and velocity by polling for acceleration every while
        {
        synchronized (this.startStopLock)
            {
            // Stop doing this if we're already in flight
            this.stopAccelerationIntegration();

            // Set the current position and velocity
            this.accelerationAlgorithm.initialize(initalPosition, initialVelocity);

            // Make a new thread on which to do the integration
            this.accelerationMananger = new HandshakeThreadStarter("integrator", new AccelerationManager(msPollInterval));

            IStopActionRegistrar registrar = SynchronousOpMode.getStopActionRegistrar();
            if (registrar != null)
                {
                // Running on synchronous thread; we can use the SynchronousOpMode to
                // shut us down automatically.
                registrar.registerActionOnStop(new IAction() {
                    @Override public void doAction()
                        {
                        AdaFruitBNO055IMU.this.close();
                        }
                    });
                }
            else
                {
                // Set up a suicide watch that will shutdown that integration thread if
                // the I2cDevice is shutdown. This is a bit of a hack, perhaps, but
                // is the only way given the current architecture that we've figured out
                // how to auto-stop integration if the robot is shutdown and we're not
                // on a synchronous thread.
                while (this.deviceClient.getCallbackThread() == null)
                    {
                    // Don't yet know the callback thread. Spin until we do.
                    Thread.yield();
                    }
                this.deathWatch = new DeathWatch(this.deviceClient.getCallbackThread(), new IAction() {
                    @Override public void doAction()
                        {
                        AdaFruitBNO055IMU.this.close();
                        }
                    });
                this.deathWatch.start();
                }

            // Start the whole schebang a rockin...
            this.accelerationMananger.start();
            }
        }
    
    public void stopAccelerationIntegration() // needs a different lock
        {
        synchronized (this.startStopLock)
            {
            // Disarm our monitor
            if (this.deathWatch != null)
                {
                this.deathWatch.stop(msAccelerationIntegrationStopWait);
                this.deathWatch = null;
                }

            // Stop the integration thread
            if (this.accelerationMananger != null)
                {
                this.accelerationMananger.stop(msAccelerationIntegrationStopWait);
                this.accelerationMananger = null;
                }
            }
        }

    /**
     * NaiveAccelerationIntegrator provides a very naive implementation of
     * an acceleration integration algorithm. It just does the basic physics.
     * One you would actually want to use in a robot would, for example, likely
     * filter noise out the acceleration data.
     */
    class NaiveAccelerationIntegrator implements IAccelerationIntegrator
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        Position        position;
        Velocity        velocity;
        Acceleration    acceleration;

        public Position getPosition() { return this.position; }
        public Velocity getVelocity() { return this.velocity; }
        public Acceleration getAcceleration() { return this.acceleration; }

        //------------------------------------------------------------------------------------------
        // Construction
        //------------------------------------------------------------------------------------------

        NaiveAccelerationIntegrator()
            {
            this.position = null;
            this.velocity = null;
            this.acceleration = null;
            }

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        @Override public void initialize(Position initialPosition, Velocity initialVelocity)
            {
            this.position = initialPosition;
            this.velocity = initialVelocity;
            this.acceleration = null;
            }

        @Override public void update(Acceleration accelNext)
            {
            // We should always be given a timestamp here
            assertTrue(!BuildConfig.DEBUG || accelNext.nanoTime != 0);

            // Log the incoming accelerations
            log_v("a: %f %f %f %f", accelNext.accelX, accelNext.accelY, accelNext.accelZ, acceleration == null ? 0 : (accelNext.nanoTime - acceleration.nanoTime) * 1e-9);

            // We can only integrate if we have a previous acceleration to baseline from
            if (acceleration != null)
                {
                Acceleration accelPrev    = acceleration;
                Velocity     velocityPrev = velocity;

                acceleration = accelNext;

                Velocity deltaVelocity = meanIntegrate(acceleration, accelPrev);
                velocity = plus(velocity, deltaVelocity);

                Position deltaPosition = meanIntegrate(velocity, velocityPrev);
                position = plus(position, deltaPosition);
                }
            else
                acceleration = accelNext;
            }
        }

    /** Maintains current velocity and position by integrating acceleration */
    class AccelerationManager implements IHandshakeable
        {
        private final int msPollInterval;
        private final static long nsPerMs = 1000000;
        
        AccelerationManager(int msPollInterval)
            {
            this.msPollInterval = msPollInterval;
            }
        
        @Override public void run(HandshakeThreadStarter starter)
            {
            // Let the starter know we're up and running
            starter.doHandshake();

            // Don't let inappropriate exceptions sneak out
            try
                {
                // Loop until we're asked to stop
                while (!starter.isStopRequested())
                    {
                    // Read the latest available acceleration
                    final Acceleration accelNext = AdaFruitBNO055IMU.this.getLinearAcceleration();

                    // Have the algorithm do its thing
                    synchronized (dataLock)
                        {
                        accelerationAlgorithm.update(accelNext);
                        }
                    
                    // Wait a bit before polling again
                    if (msPollInterval > 0)
                        {
                        long msSoFar = (System.nanoTime() - accelNext.nanoTime) / nsPerMs;
                        Thread.sleep(Math.max(0,msPollInterval - msSoFar));
                        }
                    else
                        Thread.yield(); // never do a hard spin
                    }
                }
            catch (InterruptedException|RuntimeInterruptedException e)
                {
                return;
                }
            }
        }

    @Override public synchronized byte read8(final REGISTER reg)
        {
        return this.deviceClient.executeFunctionWhileLocked(new IFunc<Byte>()
            {
            @Override public Byte value()
                {
                ensureReadWindow(new II2cDeviceClient.ReadWindow(reg.bVal, 1, readMode));
                return deviceClient.read8(reg.bVal);
                }
            });
        }

    @Override public synchronized byte[] read(final REGISTER reg, final int cb)
        {
        return this.deviceClient.executeFunctionWhileLocked(new IFunc<byte[]>()
            {
            @Override public byte[] value()
                {
                ensureReadWindow(new II2cDeviceClient.ReadWindow(reg.bVal, cb, readMode));
                return deviceClient.read(reg.bVal, cb);
                }
            });
        }

    @Override public void write8(REGISTER reg, int data)
        {
        this.deviceClient.write8(reg.bVal, data);
        }
    @Override public void write(REGISTER reg, byte[] data)
        {
        this.deviceClient.write(reg.bVal, data);
        }


    //------------------------------------------------------------------------------------------
    // Internal utility
    //------------------------------------------------------------------------------------------
    
    private String getLoggingTag()
        {
        return parameters.loggingTag + ":"; // add suffix so we can filter out our I2C logging if we wish
        }

    private void log_v(String format, Object... args)
        {
        if (this.parameters.loggingEnabled)
            {
            String message = String.format(format, args);
            Log.v(getLoggingTag(), message);
            }
        }

    private void log_d(String format, Object... args)
        {
        if (this.parameters.loggingEnabled)
            {
            String message = String.format(format, args);
            Log.d(getLoggingTag(), message);
            }
        }

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
    private static final II2cDeviceClient.ReadWindow lowerWindow = newWindow(REGISTER.CHIP_ID, REGISTER.EULER_H_LSB);
    /**
     * A second of two primary register windows we use for reading from the BNO055.
     * We'd like to include the temperature register, too, but that would make a 27-byte window, and
     * those don't (currently) work in the CDIM.
     *
     * @see #lowerWindow
     */
    private static final II2cDeviceClient.ReadWindow upperWindow = newWindow(REGISTER.EULER_H_LSB, REGISTER.TEMP);
    
    private static II2cDeviceClient.ReadWindow newWindow(REGISTER regFirst, REGISTER regMax)
        {
        return new II2cDeviceClient.ReadWindow(regFirst.bVal, regMax.bVal-regFirst.bVal, readMode);
        }

    private void ensureReadWindow(II2cDeviceClient.ReadWindow needed)
    // We optimize small windows into larger ones if we can
        {
        II2cDeviceClient.ReadWindow windowToSet = lowerWindow.containsWithSameMode(needed)
            ? lowerWindow
            : upperWindow.containsWithSameMode(needed)
                ? upperWindow
                : needed;           // just use what's needed if it's not within our two main windows
        this.deviceClient.ensureReadWindow(needed, windowToSet);
        }

    // Our write logic doesn't actually know when the I2C writes are issued. All it knows is
    // when it has queued the write to the USB Core Device Interface Module. It's a pretty
    // deterministic interval after that that the I2C write occurs, we guess, but we don't
    // really know what that is. To account for this, we slop in some extra time to the
    // delays so that we're not cutting things too close to the edge. And given that this is
    // initialization logic and so not time critical, we err on being generous.

    private final static int msExtra = 50;

    private void delayExtra(int ms)
        {
        delay(ms + msExtra);
        }
    private void delayLoreExtra(int ms)
        {
        delayLore(ms + msExtra);
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
            Util.handleCapturedInterrupt(e);
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
            Util.handleCapturedInterrupt(e);
            }
        }

    private void enterConfigModeFor(IAction action)
        {
        SENSOR_MODE modePrev = this.currentMode;
        setSensorMode(SENSOR_MODE.CONFIG);
        delayLoreExtra(25);
        try
            {
            action.doAction();
            }
        finally
            {
            setSensorMode(modePrev);
            delayLoreExtra(20);
            }
        }

    private <T> T enterConfigModeFor(IFunc<T> lambda)
        {
        T result;
        
        SENSOR_MODE modePrev = this.currentMode;
        setSensorMode(SENSOR_MODE.CONFIG);
        delayLoreExtra(25);
        try
            {
            result = lambda.value();
            }
        finally
            {
            setSensorMode(modePrev);
            delayLoreExtra(20);
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
