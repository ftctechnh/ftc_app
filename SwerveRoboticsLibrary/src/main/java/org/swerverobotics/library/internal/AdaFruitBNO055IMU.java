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

    BNO055_OPERATION_MODE   mode;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public AdaFruitBNO055IMU(I2cDevice i2cDevice, int i2cAddr)
        {
        super(i2cDevice, i2cAddr, new RegisterWindow(REGISTER.BNO055_CHIP_ID_ADDR.getValue(), 8));
        }
    public AdaFruitBNO055IMU(I2cDevice i2cDevice)
        {
        this(i2cDevice, BNO055_ADDRESS_A);
        }

    public static IAdaFruitBNO055IMU create(I2cDevice i2cDevice) throws InterruptedException
        {
        // Create a sensor which is a client of i2cDevice
        IAdaFruitBNO055IMU result = new AdaFruitBNO055IMU(i2cDevice);
        
        // Initialize it to useful defaults: IMU mode and external crystal (recommended 
        // in BNO055 specification)
        result.initialize(BNO055_OPERATION_MODE.IMU);
        result.setExternalCrystalUse(true);
        return result;
        }

    //------------------------------------------------------------------------------------------
    // Operations
    //------------------------------------------------------------------------------------------
    
    /**
     * Initialize the device to be running in the indicated operation mode
     */
    public void initialize(BNO055_OPERATION_MODE mode) throws InterruptedException
        {
        // Make sure we have the right device
        if (read8(REGISTER.BNO055_CHIP_ID_ADDR) != BNO055_ID)
            {
            delay(1000); // hold on for boot
            if (read8(REGISTER.BNO055_CHIP_ID_ADDR) != BNO055_ID)
                throw new UnexpectedI2CDeviceException();
            }
        
        // Switch to config mode (just in case, since this is the default)
        setOperationMode(BNO055_OPERATION_MODE.CONFIG);
        
        // Reset the system, and wait for the chip id register to switch
        // back from its reset state (0xA0?) to the it's chip id state (also 0xA0?; hmmm
        // something is odd there). This can typically take 650ms (Table 0-2, p13).
        write8(REGISTER.BNO055_SYS_TRIGGER_ADDR, 0x20);
        while (read8(REGISTER.BNO055_CHIP_ID_ADDR) != BNO055_ID)
            {
            delay(10);
            }
        delay(50);
        
        // Set to normal power mode
        write8(REGISTER.BNO055_PWR_MODE_ADDR, POWER_MODE.NORMAL.ordinal());
        delay(10);

        // Make sure we're looking at register page zero
        write8(REGISTER.BNO055_PAGE_ID_ADDR, 0);
        
        // Set the output units. Section 3.6, p30
        int unitsel = (0 << 7) | // Orientation = Android
                      (0 << 4) | // Temperature = Celsius
                      (1 << 2) | // Euler = radians
                      (1 << 1) | // Gyro = radians / second
                      (0 << 0);  // Accelerometer = m/s^2
        write8(REGISTER.BNO055_UNIT_SEL_ADDR, unitsel);
        
        // ???
        write8(REGISTER.BNO055_SYS_TRIGGER_ADDR, 0x0);
        delay(10);
        
        // Set the requested operating mode (see section 3.3)
        setOperationMode(mode);
        delay(20);
        }

    private void setOperationMode(BNO055_OPERATION_MODE mode) throws InterruptedException
    /* The default operation mode after power-on is CONFIGMODE. When the user changes to another 
    operation mode, the sensors which are required in that particular sensor mode are powered, 
    while the sensors whose signals are not required are set to suspend mode. */
        {
        this.mode = mode;
        this.write8(REGISTER.BNO055_OPR_MODE_ADDR, mode.ordinal() & 0x0F);
        
        // Delay per Table 3-6 of BNO055 Data sheet (p21)
        if (mode == BNO055_OPERATION_MODE.CONFIG)
            delay(19);
        else
            delay(7);
        }

    /**
     * Use or don't use the external 32.768khz crystal.
     * 
     * See Section 5.5 (p100) of the BNO055 specification.
     */
    public void setExternalCrystalUse(final boolean useExternalCrystal) throws InterruptedException
        {
        this.enterConfigModeFor(new IInterruptableAction()
        {
        @Override public void doAction() throws InterruptedException
            {
            write8(REGISTER.BNO055_PAGE_ID_ADDR, 0);
            if (useExternalCrystal)
                {
                write8(REGISTER.BNO055_SYS_TRIGGER_ADDR, 0x80);
                }
            else
                {
                write8(REGISTER.BNO055_SYS_TRIGGER_ADDR, 0x00);
                }
            delay(10);
            }
        });
        }
    
    public byte getSystemStatus() throws InterruptedException
        {
        return this.enterConfigModeFor(new IInterruptableFunc<Byte>()
            {
            @Override public Byte value() throws InterruptedException
                {
                return read8(REGISTER.BNO055_SYS_STAT_ADDR);
                }
            });
        }

    public byte getSystemError() throws InterruptedException
        {
        return this.enterConfigModeFor(new IInterruptableFunc<Byte>()
        {
        @Override public Byte value() throws InterruptedException
            {
            return read8(REGISTER.BNO055_SYS_STAT_ADDR);
            }
        });
        }

    //------------------------------------------------------------------------------------------
    // Internal utility
    //------------------------------------------------------------------------------------------

    private void delay(int ms) throws InterruptedException
        {
        Thread.sleep(ms);
        }

    private byte read8(REGISTER reg) throws InterruptedException
        {
        return this.read8(reg.ordinal());
        }
    
    private void write8(REGISTER reg, int data) throws InterruptedException
        {
        this.write8(reg.ordinal(), data);
        }
    
    private void enterConfigModeFor(IInterruptableAction action) throws InterruptedException
        {
        BNO055_OPERATION_MODE modePrev = this.mode;
        setOperationMode(BNO055_OPERATION_MODE.CONFIG);
        // delay(25);
        try
            {
            action.doAction();
            }
        finally
            {
            setOperationMode(modePrev);
            // delay(20);
            }
        }

    private <T> T enterConfigModeFor(IInterruptableFunc<T> lambda) throws InterruptedException
        {
        T result;
        
        BNO055_OPERATION_MODE modePrev = this.mode;
        setOperationMode(BNO055_OPERATION_MODE.CONFIG);
        // delay(25);
        try
            {
            result = lambda.value();
            }
        finally
            {
            setOperationMode(modePrev);
            // delay(20);
            }
        //
        return result;
        }
    
    //------------------------------------------------------------------------------------------
    // Constants
    //------------------------------------------------------------------------------------------

    final static int BNO055_ADDRESS_A = 0x28;
    final static int BNO055_ADDRESS_B = 0x29;
    final static int BNO055_ID        = 0xa0;

    enum REGISTER
        {
        /* Page id register definition */
        BNO055_PAGE_ID_ADDR(0X07),

        /* PAGE0 REGISTER DEFINITION START*/
        BNO055_CHIP_ID_ADDR(0x00),
        BNO055_ACCEL_REV_ID_ADDR(0x01),
        BNO055_MAG_REV_ID_ADDR(0x02),
        BNO055_GYRO_REV_ID_ADDR(0x03),
        BNO055_SW_REV_ID_LSB_ADDR(0x04),
        BNO055_SW_REV_ID_MSB_ADDR(0x05),
        BNO055_BL_REV_ID_ADDR(0X06),

        /* Accel data register */
        BNO055_ACCEL_DATA_X_LSB_ADDR(0X08),
        BNO055_ACCEL_DATA_X_MSB_ADDR(0X09),
        BNO055_ACCEL_DATA_Y_LSB_ADDR(0X0A),
        BNO055_ACCEL_DATA_Y_MSB_ADDR(0X0B),
        BNO055_ACCEL_DATA_Z_LSB_ADDR(0X0C),
        BNO055_ACCEL_DATA_Z_MSB_ADDR(0X0D),

        /* Mag data register */
        BNO055_MAG_DATA_X_LSB_ADDR(0X0E),
        BNO055_MAG_DATA_X_MSB_ADDR(0X0F),
        BNO055_MAG_DATA_Y_LSB_ADDR(0X10),
        BNO055_MAG_DATA_Y_MSB_ADDR(0X11),
        BNO055_MAG_DATA_Z_LSB_ADDR(0X12),
        BNO055_MAG_DATA_Z_MSB_ADDR(0X13),

        /* Gyro data registers */
        BNO055_GYRO_DATA_X_LSB_ADDR(0X14),
        BNO055_GYRO_DATA_X_MSB_ADDR(0X15),
        BNO055_GYRO_DATA_Y_LSB_ADDR(0X16),
        BNO055_GYRO_DATA_Y_MSB_ADDR(0X17),
        BNO055_GYRO_DATA_Z_LSB_ADDR(0X18),
        BNO055_GYRO_DATA_Z_MSB_ADDR(0X19),

        /* Euler data registers */
        BNO055_EULER_H_LSB_ADDR(0X1A),
        BNO055_EULER_H_MSB_ADDR(0X1B),
        BNO055_EULER_R_LSB_ADDR(0X1C),
        BNO055_EULER_R_MSB_ADDR(0X1D),
        BNO055_EULER_P_LSB_ADDR(0X1E),
        BNO055_EULER_P_MSB_ADDR(0X1F),

        /* Quaternion data registers */
        BNO055_QUATERNION_DATA_W_LSB_ADDR(0X20),
        BNO055_QUATERNION_DATA_W_MSB_ADDR(0X21),
        BNO055_QUATERNION_DATA_X_LSB_ADDR(0X22),
        BNO055_QUATERNION_DATA_X_MSB_ADDR(0X23),
        BNO055_QUATERNION_DATA_Y_LSB_ADDR(0X24),
        BNO055_QUATERNION_DATA_Y_MSB_ADDR(0X25),
        BNO055_QUATERNION_DATA_Z_LSB_ADDR(0X26),
        BNO055_QUATERNION_DATA_Z_MSB_ADDR(0X27),

        /* Linear acceleration data registers */
        BNO055_LINEAR_ACCEL_DATA_X_LSB_ADDR(0X28),
        BNO055_LINEAR_ACCEL_DATA_X_MSB_ADDR(0X29),
        BNO055_LINEAR_ACCEL_DATA_Y_LSB_ADDR(0X2A),
        BNO055_LINEAR_ACCEL_DATA_Y_MSB_ADDR(0X2B),
        BNO055_LINEAR_ACCEL_DATA_Z_LSB_ADDR(0X2C),
        BNO055_LINEAR_ACCEL_DATA_Z_MSB_ADDR(0X2D),

        /* Gravity data registers */
        BNO055_GRAVITY_DATA_X_LSB_ADDR(0X2E),
        BNO055_GRAVITY_DATA_X_MSB_ADDR(0X2F),
        BNO055_GRAVITY_DATA_Y_LSB_ADDR(0X30),
        BNO055_GRAVITY_DATA_Y_MSB_ADDR(0X31),
        BNO055_GRAVITY_DATA_Z_LSB_ADDR(0X32),
        BNO055_GRAVITY_DATA_Z_MSB_ADDR(0X33),

        /* Temperature data register */
        BNO055_TEMP_ADDR(0X34),

        /* Status registers */
        BNO055_CALIB_STAT_ADDR(0X35),
        BNO055_SELFTEST_RESULT_ADDR(0X36),
        BNO055_INTR_STAT_ADDR(0X37),

        BNO055_SYS_CLK_STAT_ADDR(0X38),
        BNO055_SYS_STAT_ADDR(0X39),
        BNO055_SYS_ERR_ADDR(0X3A),

        /* Unit selection register */
        BNO055_UNIT_SEL_ADDR(0X3B),
        BNO055_DATA_SELECT_ADDR(0X3C),

        /* Mode registers */
        BNO055_OPR_MODE_ADDR(0X3D),
        BNO055_PWR_MODE_ADDR(0X3E),

        BNO055_SYS_TRIGGER_ADDR(0X3F),
        BNO055_TEMP_SOURCE_ADDR(0X40),

        /* Axis remap registers */
        BNO055_AXIS_MAP_CONFIG_ADDR(0X41),
        BNO055_AXIS_MAP_SIGN_ADDR(0X42),

        /* SIC registers */
        BNO055_SIC_MATRIX_0_LSB_ADDR(0X43),
        BNO055_SIC_MATRIX_0_MSB_ADDR(0X44),
        BNO055_SIC_MATRIX_1_LSB_ADDR(0X45),
        BNO055_SIC_MATRIX_1_MSB_ADDR(0X46),
        BNO055_SIC_MATRIX_2_LSB_ADDR(0X47),
        BNO055_SIC_MATRIX_2_MSB_ADDR(0X48),
        BNO055_SIC_MATRIX_3_LSB_ADDR(0X49),
        BNO055_SIC_MATRIX_3_MSB_ADDR(0X4A),
        BNO055_SIC_MATRIX_4_LSB_ADDR(0X4B),
        BNO055_SIC_MATRIX_4_MSB_ADDR(0X4C),
        BNO055_SIC_MATRIX_5_LSB_ADDR(0X4D),
        BNO055_SIC_MATRIX_5_MSB_ADDR(0X4E),
        BNO055_SIC_MATRIX_6_LSB_ADDR(0X4F),
        BNO055_SIC_MATRIX_6_MSB_ADDR(0X50),
        BNO055_SIC_MATRIX_7_LSB_ADDR(0X51),
        BNO055_SIC_MATRIX_7_MSB_ADDR(0X52),
        BNO055_SIC_MATRIX_8_LSB_ADDR(0X53),
        BNO055_SIC_MATRIX_8_MSB_ADDR(0X54),

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

    enum VECTOR
        {
        ACCELEROMETER   (REGISTER.BNO055_ACCEL_DATA_X_LSB_ADDR),
        MAGNETOMETER    (REGISTER.BNO055_MAG_DATA_X_LSB_ADDR),
        GYROSCOPE       (REGISTER.BNO055_GYRO_DATA_X_LSB_ADDR),
        EULER           (REGISTER.BNO055_EULER_H_LSB_ADDR),
        LINEARACCEL     (REGISTER.BNO055_LINEAR_ACCEL_DATA_X_LSB_ADDR),
        GRAVITY         (REGISTER.BNO055_GRAVITY_DATA_X_LSB_ADDR);
        //------------------------------------------------------------------------------------------
        private byte value;
        private VECTOR(int value) { this.value = (byte)value; }
        private VECTOR(REGISTER register)
            {
            this(register.ordinal());
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
