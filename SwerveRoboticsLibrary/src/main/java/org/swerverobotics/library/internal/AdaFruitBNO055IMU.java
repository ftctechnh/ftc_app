package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.I2cDevice;

import org.swerverobotics.library.*;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;

/**
 * (documentation to come) (implementation far from complete)
 */
public class AdaFruitBNO055IMU extends I2cDeviceClient implements IAdaFruitBNO055IMU
    {
    //------------------------------------------------------------------------------------------
    // State
    //------------------------------------------------------------------------------------------

    OPERATION_MODE   mode;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public AdaFruitBNO055IMU(I2cDevice i2cDevice, int i2cAddr)
        {
        super(i2cDevice, i2cAddr, new RegisterWindow(REGISTER.CHIP_ID_ADDR.getValue(), 8));
        }
    public AdaFruitBNO055IMU(I2cDevice i2cDevice)
        {
        this(i2cDevice, ADDRESS_A);
        }

    public static IAdaFruitBNO055IMU create(I2cDevice i2cDevice) throws InterruptedException
        {
        // Create a sensor which is a client of i2cDevice
        IAdaFruitBNO055IMU result = new AdaFruitBNO055IMU(i2cDevice);
        
        // Initialize it to useful defaults: IMU mode and external crystal (recommended 
        // in BNO055 specification)
        result.initialize(OPERATION_MODE.IMU);
        result.setExternalCrystalUse(true);
        return result;
        }

    //------------------------------------------------------------------------------------------
    // Operations
    //------------------------------------------------------------------------------------------
    
    /**
     * Initialize the device to be running in the indicated operation mode
     */
    public void initialize(OPERATION_MODE mode)
        {
        // Make sure we have the right device
        byte id = read8(REGISTER.CHIP_ID_ADDR); 
        if (id != ID)
            {
            delay(1000); // hold on for boot
            id = read8(REGISTER.CHIP_ID_ADDR);
            if (id != ID)
                throw new UnexpectedI2CDeviceException(id);
            }
        
        // Switch to config mode (just in case, since this is the default)
        setOperationMode(OPERATION_MODE.CONFIG);
        
        // Reset the system, and wait for the chip id register to switch
        // back from its reset state (0xA0?) to the it's chip id state (also 0xA0?; hmmm
        // something is odd there). This can typically take 650ms (Table 0-2, p13).
        write8(REGISTER.SYS_TRIGGER_ADDR, 0x20);
        while (read8(REGISTER.CHIP_ID_ADDR) != ID)
            {
            delay(10);
            }
        delayLore(50);
        
        // Set to normal power mode
        write8(REGISTER.PWR_MODE_ADDR, POWER_MODE.NORMAL.ordinal());
        delayLore(10);

        // Make sure we're looking at register page zero
        write8(REGISTER.PAGE_ID_ADDR, 0);
        
        // Set the output units. Section 3.6, p30
        int unitsel = (0 << 7) | // Orientation = Android
                      (0 << 4) | // Temperature = Celsius
                      (1 << 2) | // Euler = radians
                      (1 << 1) | // Gyro = radians / second
                      (0 << 0);  // Accelerometer = m/s^2
        write8(REGISTER.UNIT_SEL_ADDR, unitsel);
        
        // ???
        write8(REGISTER.SYS_TRIGGER_ADDR, 0x0);
        delayLore(10);
        
        // Set the requested operating mode (see section 3.3)
        setOperationMode(mode);
        delayLore(20);
        }

    private void setOperationMode(OPERATION_MODE mode)
    /* The default operation mode after power-on is CONFIGMODE. When the user changes to another 
    operation mode, the sensors which are required in that particular sensor mode are powered, 
    while the sensors whose signals are not required are set to suspend mode. */
        {
        this.mode = mode;
        this.write8(REGISTER.OPR_MODE_ADDR, mode.ordinal() & 0x0F);
        
        // Delay per Table 3-6 of BNO055 Data sheet (p21)
        if (mode == OPERATION_MODE.CONFIG)
            delay(19);
        else
            delay(7);
        }

    /**
     * Use or don't use the external 32.768khz crystal.
     * 
     * See Section 5.5 (p100) of the BNO055 specification.
     */
    public void setExternalCrystalUse(final boolean useExternalCrystal)
        {
        this.enterConfigModeFor(new IAction()
            {
            @Override public void doAction()
                {
                write8(REGISTER.PAGE_ID_ADDR, 0);
                if (useExternalCrystal)
                    {
                    write8(REGISTER.SYS_TRIGGER_ADDR, 0x80);
                    }
                else
                    {
                    write8(REGISTER.SYS_TRIGGER_ADDR, 0x00);
                    }
                delayLore(10);
                }
            });
        }
    
    public byte getSystemStatus()
        {
        return this.enterConfigModeFor(new IFunc<Byte>()
            {
            @Override public Byte value()
                {
                return read8(REGISTER.SYS_STAT_ADDR);
                }
            });
        }

    public byte getSystemError()
        {
        return this.enterConfigModeFor(new IFunc<Byte>()
            {
            @Override public Byte value()
                {
                return read8(REGISTER.SYS_STAT_ADDR);
                }
            });
        }

    public boolean isSystemCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT_ADDR);
        return ((b>>6) & 0x03) == 0x03;
        }

    public boolean isGyroCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT_ADDR);
        return ((b>>4) & 0x03) == 0x03;
        }

    public boolean isAccelerometerCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT_ADDR);
        return ((b>>2) & 0x03) == 0x03;
        }

    public boolean isMagnetometerCalibrated()
        {
        byte b = this.read8(REGISTER.CALIB_STAT_ADDR);
        return ((b>>0) & 0x03) == 0x03;
        }
    
    public double getTemperature()
        {
        byte b = this.read8(REGISTER.TEMP_ADDR);
        return (double)b;
        }
    
    public MagneticFlux getMagneticFieldStrength()
        {
        return new MagneticFlux(getVector(VECTOR.MAGNETOMETER, 16 * 1000000));
        }
    public Acceleration getAcceleration()
        {
        return new Acceleration(getVector(VECTOR.ACCELEROMETER, 100));
        }
    public Acceleration getLinearAcceleration()
        {
        return new Acceleration(getVector(VECTOR.LINEARACCEL, 100));
        }
    public Acceleration getGravity()
        {
        return new Acceleration(getVector(VECTOR.GRAVITY, 100));
        }
    public AngularVelocity getAngularVelocity()
        {
        return new AngularVelocity(getVector(VECTOR.GYROSCOPE, 900));
        }
    public EulerAngles getAngularOrientation()
        {
        return new EulerAngles(getVector(VECTOR.EULER, 900));
        }
    
    private double[] getVector(VECTOR vector, double scale)
        {
        byte[] buffer = this.read(vector.getValue(), 6);
        return new double[] {
                Util.makeInt(buffer[0], buffer[1]) / scale,
                Util.makeInt(buffer[2], buffer[3]) / scale,
                Util.makeInt(buffer[4], buffer[5]) / scale };
        }

    public Quaternion getQuaternionOrientation()
        {
        // Section 3.6.5.5 of BNO055 specification
        byte[] buffer = this.read(REGISTER.QUATERNION_DATA_W_LSB_ADDR.getValue(), 8);
        final double scale = 1.0 / (1 << 14);
        return new Quaternion(
                Util.makeInt(buffer[0], buffer[1]) * scale,
                Util.makeInt(buffer[2], buffer[3]) * scale,
                Util.makeInt(buffer[4], buffer[5]) * scale,
                Util.makeInt(buffer[6], buffer[7]) * scale
            );
        }
    
    //------------------------------------------------------------------------------------------
    // Internal utility
    //------------------------------------------------------------------------------------------
    
    private void delayLore(int ms)
        {
        delay(ms);
        }

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

    private byte read8(REGISTER reg)
        {
        return this.read8(reg.ordinal());
        }
    
    private void write8(REGISTER reg, int data)
        {
        this.write8(reg.ordinal(), data);
        }
    
    private void enterConfigModeFor(IAction action)
        {
        OPERATION_MODE modePrev = this.mode;
        setOperationMode(OPERATION_MODE.CONFIG);
        delayLore(25);
        try
            {
            action.doAction();
            }
        finally
            {
            setOperationMode(modePrev);
            delayLore(20);
            }
        }

    private <T> T enterConfigModeFor(IFunc<T> lambda)
        {
        T result;
        
        OPERATION_MODE modePrev = this.mode;
        setOperationMode(OPERATION_MODE.CONFIG);
        delayLore(25);
        try
            {
            result = lambda.value();
            }
        finally
            {
            setOperationMode(modePrev);
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
            ACCELEROMETER   (REGISTER.ACCEL_DATA_X_LSB_ADDR),
            MAGNETOMETER    (REGISTER.MAG_DATA_X_LSB_ADDR),
            GYROSCOPE       (REGISTER.GYRO_DATA_X_LSB_ADDR),
            EULER           (REGISTER.EULER_H_LSB_ADDR),
            LINEARACCEL     (REGISTER.LINEAR_ACCEL_DATA_X_LSB_ADDR),
            GRAVITY         (REGISTER.GRAVITY_DATA_X_LSB_ADDR);
        //------------------------------------------------------------------------------------------
        private byte value;
        private VECTOR(int value) { this.value = (byte)value; }
        private VECTOR(REGISTER register) { this(register.getValue());}
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

    enum REGISTER
        {
            /* Page id register definition */
            PAGE_ID_ADDR(0X07),

            /* PAGE0 REGISTER DEFINITION START*/
            CHIP_ID_ADDR(0x00),
            ACCEL_REV_ID_ADDR(0x01),
            MAG_REV_ID_ADDR(0x02),
            GYRO_REV_ID_ADDR(0x03),
            SW_REV_ID_LSB_ADDR(0x04),
            SW_REV_ID_MSB_ADDR(0x05),
            BL_REV_ID_ADDR(0X06),

            /* Accel data register */
            ACCEL_DATA_X_LSB_ADDR(0X08),
            ACCEL_DATA_X_MSB_ADDR(0X09),
            ACCEL_DATA_Y_LSB_ADDR(0X0A),
            ACCEL_DATA_Y_MSB_ADDR(0X0B),
            ACCEL_DATA_Z_LSB_ADDR(0X0C),
            ACCEL_DATA_Z_MSB_ADDR(0X0D),

            /* Mag data register */
            MAG_DATA_X_LSB_ADDR(0X0E),
            MAG_DATA_X_MSB_ADDR(0X0F),
            MAG_DATA_Y_LSB_ADDR(0X10),
            MAG_DATA_Y_MSB_ADDR(0X11),
            MAG_DATA_Z_LSB_ADDR(0X12),
            MAG_DATA_Z_MSB_ADDR(0X13),

            /* Gyro data registers */
            GYRO_DATA_X_LSB_ADDR(0X14),
            GYRO_DATA_X_MSB_ADDR(0X15),
            GYRO_DATA_Y_LSB_ADDR(0X16),
            GYRO_DATA_Y_MSB_ADDR(0X17),
            GYRO_DATA_Z_LSB_ADDR(0X18),
            GYRO_DATA_Z_MSB_ADDR(0X19),

            /* Euler data registers */
            EULER_H_LSB_ADDR(0X1A),
            EULER_H_MSB_ADDR(0X1B),
            EULER_R_LSB_ADDR(0X1C),
            EULER_R_MSB_ADDR(0X1D),
            EULER_P_LSB_ADDR(0X1E),
            EULER_P_MSB_ADDR(0X1F),

            /* Quaternion data registers */
            QUATERNION_DATA_W_LSB_ADDR(0X20),
            QUATERNION_DATA_W_MSB_ADDR(0X21),
            QUATERNION_DATA_X_LSB_ADDR(0X22),
            QUATERNION_DATA_X_MSB_ADDR(0X23),
            QUATERNION_DATA_Y_LSB_ADDR(0X24),
            QUATERNION_DATA_Y_MSB_ADDR(0X25),
            QUATERNION_DATA_Z_LSB_ADDR(0X26),
            QUATERNION_DATA_Z_MSB_ADDR(0X27),

            /* Linear acceleration data registers */
            LINEAR_ACCEL_DATA_X_LSB_ADDR(0X28),
            LINEAR_ACCEL_DATA_X_MSB_ADDR(0X29),
            LINEAR_ACCEL_DATA_Y_LSB_ADDR(0X2A),
            LINEAR_ACCEL_DATA_Y_MSB_ADDR(0X2B),
            LINEAR_ACCEL_DATA_Z_LSB_ADDR(0X2C),
            LINEAR_ACCEL_DATA_Z_MSB_ADDR(0X2D),

            /* Gravity data registers */
            GRAVITY_DATA_X_LSB_ADDR(0X2E),
            GRAVITY_DATA_X_MSB_ADDR(0X2F),
            GRAVITY_DATA_Y_LSB_ADDR(0X30),
            GRAVITY_DATA_Y_MSB_ADDR(0X31),
            GRAVITY_DATA_Z_LSB_ADDR(0X32),
            GRAVITY_DATA_Z_MSB_ADDR(0X33),

            /* Temperature data register */
            TEMP_ADDR(0X34),

            /* Status registers */
            CALIB_STAT_ADDR(0X35),
            SELFTEST_RESULT_ADDR(0X36),
            INTR_STAT_ADDR(0X37),

            SYS_CLK_STAT_ADDR(0X38),
            SYS_STAT_ADDR(0X39),
            SYS_ERR_ADDR(0X3A),

            /* Unit selection register */
            UNIT_SEL_ADDR(0X3B),
            DATA_SELECT_ADDR(0X3C),

            /* Mode registers */
            OPR_MODE_ADDR(0X3D),
            PWR_MODE_ADDR(0X3E),

            SYS_TRIGGER_ADDR(0X3F),
            TEMP_SOURCE_ADDR(0X40),

            /* Axis remap registers */
            AXIS_MAP_CONFIG_ADDR(0X41),
            AXIS_MAP_SIGN_ADDR(0X42),

            /* SIC registers */
            SIC_MATRIX_0_LSB_ADDR(0X43),
            SIC_MATRIX_0_MSB_ADDR(0X44),
            SIC_MATRIX_1_LSB_ADDR(0X45),
            SIC_MATRIX_1_MSB_ADDR(0X46),
            SIC_MATRIX_2_LSB_ADDR(0X47),
            SIC_MATRIX_2_MSB_ADDR(0X48),
            SIC_MATRIX_3_LSB_ADDR(0X49),
            SIC_MATRIX_3_MSB_ADDR(0X4A),
            SIC_MATRIX_4_LSB_ADDR(0X4B),
            SIC_MATRIX_4_MSB_ADDR(0X4C),
            SIC_MATRIX_5_LSB_ADDR(0X4D),
            SIC_MATRIX_5_MSB_ADDR(0X4E),
            SIC_MATRIX_6_LSB_ADDR(0X4F),
            SIC_MATRIX_6_MSB_ADDR(0X50),
            SIC_MATRIX_7_LSB_ADDR(0X51),
            SIC_MATRIX_7_MSB_ADDR(0X52),
            SIC_MATRIX_8_LSB_ADDR(0X53),
            SIC_MATRIX_8_MSB_ADDR(0X54),

            /* Accelerometer Offset registers */
            ACCEL_OFFSET_X_LSB_ADDR(0X55),
            ACCEL_OFFSET_X_MSB_ADDR(0X56),
            ACCEL_OFFSET_Y_LSB_ADDR(0X57),
            ACCEL_OFFSET_Y_MSB_ADDR(0X58),
            ACCEL_OFFSET_Z_LSB_ADDR(0X59),
            ACCEL_OFFSET_Z_MSB_ADDR(0X5A),

            /* Magnetometer Offset registers */
            MAG_OFFSET_X_LSB_ADDR(0X5B),
            MAG_OFFSET_X_MSB_ADDR(0X5C),
            MAG_OFFSET_Y_LSB_ADDR(0X5D),
            MAG_OFFSET_Y_MSB_ADDR(0X5E),
            MAG_OFFSET_Z_LSB_ADDR(0X5F),
            MAG_OFFSET_Z_MSB_ADDR(0X60),

            /* Gyroscope Offset register s*/
            GYRO_OFFSET_X_LSB_ADDR(0X61),
            GYRO_OFFSET_X_MSB_ADDR(0X62),
            GYRO_OFFSET_Y_LSB_ADDR(0X63),
            GYRO_OFFSET_Y_MSB_ADDR(0X64),
            GYRO_OFFSET_Z_LSB_ADDR(0X65),
            GYRO_OFFSET_Z_MSB_ADDR(0X66),

            /* Radius registers */
            ACCEL_RADIUS_LSB_ADDR(0X67),
            ACCEL_RADIUS_MSB_ADDR(0X68),
            MAG_RADIUS_LSB_ADDR(0X69),
            MAG_RADIUS_MSB_ADDR(0X6A);
        //------------------------------------------------------------------------------------------
        private byte value;
        private REGISTER(int value)
            {
            this.value = (byte)value;
            }
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
