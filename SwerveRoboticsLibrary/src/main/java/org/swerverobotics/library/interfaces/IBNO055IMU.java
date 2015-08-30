package org.swerverobotics.library.interfaces;

/**
 * Interface to the Adafruit 9-DOF Absolute Orientation IMU Fusion Breakout - BNO055 sensor.
 * 
 * Undoubtedly this needs further refinement and expansion. All in due time.
 * 
 * @see <a href="http://www.adafruit.com/products/2472">http://www.adafruit.com/products/2472</a>
 * @see <a href="http://www.bosch-sensortec.com/en/homepage/products_3/9_axis_sensors_5/ecompass_2/bno055_3/bno055_4">http://www.bosch-sensortec.com/en/homepage/products_3/9_axis_sensors_5/ecompass_2/bno055_3/bno055_4</a>
 */
public interface IBNO055IMU
    {
    //----------------------------------------------------------------------------------------------
    // Construction 
    //----------------------------------------------------------------------------------------------

    /**
     * Initialize the sensor using the indicated set of parameters.
     * 
     * Note that the execution of this method can take a fairly long while, possibly
     * several tens of milliseconds.
     */
    void initialize(Parameters parameters);

    /**
     * Instances of Parameters contain data indicating how a BNO055 absolute orientation 
     * sensor is to be initialized.
     *
     * @see #initialize(Parameters)
     */
    class Parameters
        {
        // The address at which the sensor resides on the I2C bus
        public I2CADDR          i2cAddr             = I2CADDR.DEFAULT;
        
        // The mode we wish to use the sensor in
        public SENSOR_MODE      mode                = SENSOR_MODE.IMU;
        
        // Whether to use the external or internal 32.768khz crystal
        public boolean          useExternalCrystal  = true;  // external crystal is recommended per BNO055 spec

        // Unit Selection. Section 3.6.1 (p31) of the BNO055 specification
        public TEMPUNIT         temperatureUnit     = TEMPUNIT.CELSIUS;
        public ANGLEUNIT        angleunit           = ANGLEUNIT.RADIANS;
        public ACCELUNIT        accelunit           = ACCELUNIT.METERS_PERSEC_PERSEC;
        public PITCHMODE        pitchmode           = PITCHMODE.ANDROID;    // Section 3.6.2
        }

    //----------------------------------------------------------------------------------------------
    // Reading sensor output
    //----------------------------------------------------------------------------------------------

    /**
     * Return the current temperature. Units are as configured during initialization.
     */
    double              getTemperature();

    MagneticFlux        getMagneticFieldStrength();
    Acceleration        getAcceleration();
    Acceleration        getLinearAcceleration();
    Acceleration        getGravity();
    AngularVelocity     getAngularVelocity();
    EulerAngles         getAngularOrientation();
    Quaternion          getQuaternionOrientation();

    //----------------------------------------------------------------------------------------------
    // Status inquiry
    //----------------------------------------------------------------------------------------------

    /** System Status (see section 4.3.58)
     ---------------------------------
     0 = Idle
     1 = System Error
     2 = Initializing Peripherals
     3 = System Initalization
     4 = Executing Self-Test
     5 = Sensor fusion algorithm running
     6 = System running without fusion algorithms 
   */
    byte getSystemStatus();
    
  /** System Error (see section 4.3.59)
     ---------------------------------
     0 = No error
     1 = Peripheral initialization error
     2 = System initialization error
     3 = Self test result failed
     4 = Register map value out of range
     5 = Register map address out of range
     6 = Register map write error
     7 = BNO low power mode not available for selected operat ion mode
     8 = Accelerometer power mode not available
     9 = Fusion algorithm configuration error
     A = Sensor configuration error */
    byte getSystemError();
    
    boolean isSystemCalibrated();
    boolean isGyroCalibrated();
    boolean isAccelerometerCalibrated();
    boolean isMagnetometerCalibrated();

    //----------------------------------------------------------------------------------------------
    // Low level reading and writing 
    //----------------------------------------------------------------------------------------------

    /**
     * Low level: read the byte at the indicated register
     */
    byte read8(REGISTER register);

    /**
     * Low level: write a byte to the indicated registers
     */
    void write8(REGISTER register, int bVal);
    
    //----------------------------------------------------------------------------------------------
    // Enumerations to make all of the above work 
    //----------------------------------------------------------------------------------------------

    enum I2CADDR    { UNSPECIFIED(-1), DEFAULT(0x28), ALTERNATE(0x29);   public final byte bVal; I2CADDR(int i) { bVal =(byte)i; }}
    enum TEMPUNIT   { CELSIUS(0), FARENHEIT(1);                          public final byte bVal; TEMPUNIT(int i) { bVal =(byte)i; }}
    enum ANGLEUNIT  { DEGREES(0), RADIANS(1);                            public final byte bVal; ANGLEUNIT(int i) { bVal =(byte)i; }}
    enum ACCELUNIT  { METERS_PERSEC_PERSEC(0), MILLIGALS(1);             public final byte bVal; ACCELUNIT(int i) { bVal =(byte)i; }}
    enum PITCHMODE  { WINDOWS(0), ANDROID(1);                            public final byte bVal; PITCHMODE(int i) { bVal =(byte)i; }}

    /**
     * Sensor modes are described in Table 3-5 (p21) of the BNO055 specification,
     * where they are termed "operation modes".
     */
    enum SENSOR_MODE
        {
            CONFIG(0X00),       ACCONLY(0X01),          MAGONLY(0X02),
            GYRONLY(0X03),      ACCMAG(0X04),           ACCGYRO(0X05),
            MAGGYRO(0X06),      AMG(0X07),              IMU(0X08),
            COMPASS(0X09),      M4G(0X0A),              NDOF_FMC_OFF(0X0B),
            NDOF(0X0C);
        //------------------------------------------------------------------------------------------
        public final byte bVal;
        SENSOR_MODE(int i) { this.bVal = (byte) i; }
        }

    /**
     * REGISTER provides symbolic names for each of the BNO055 device registers
     */
    enum REGISTER
        {
            /* Page id register definition */
            PAGE_ID(0X07),

            /* PAGE0 REGISTER DEFINITION START*/
            CHIP_ID(0x00),
            ACCEL_REV_ID(0x01),
            MAG_REV_ID(0x02),
            GYRO_REV_ID(0x03),
            SW_REV_ID_LSB(0x04),
            SW_REV_ID_MSB(0x05),
            BL_REV_ID(0X06),

            /* Accel data register */
            ACCEL_DATA_X_LSB(0X08),
            ACCEL_DATA_X_MSB(0X09),
            ACCEL_DATA_Y_LSB(0X0A),
            ACCEL_DATA_Y_MSB(0X0B),
            ACCEL_DATA_Z_LSB(0X0C),
            ACCEL_DATA_Z_MSB(0X0D),

            /* Mag data register */
            MAG_DATA_X_LSB(0X0E),
            MAG_DATA_X_MSB(0X0F),
            MAG_DATA_Y_LSB(0X10),
            MAG_DATA_Y_MSB(0X11),
            MAG_DATA_Z_LSB(0X12),
            MAG_DATA_Z_MSB(0X13),

            /* Gyro data registers */
            GYRO_DATA_X_LSB(0X14),
            GYRO_DATA_X_MSB(0X15),
            GYRO_DATA_Y_LSB(0X16),
            GYRO_DATA_Y_MSB(0X17),
            GYRO_DATA_Z_LSB(0X18),
            GYRO_DATA_Z_MSB(0X19),

            /* Euler data registers */
            EULER_H_LSB(0X1A),
            EULER_H_MSB(0X1B),
            EULER_R_LSB(0X1C),
            EULER_R_MSB(0X1D),
            EULER_P_LSB(0X1E),
            EULER_P_MSB(0X1F),

            /* Quaternion data registers */
            QUATERNION_DATA_W_LSB(0X20),
            QUATERNION_DATA_W_MSB(0X21),
            QUATERNION_DATA_X_LSB(0X22),
            QUATERNION_DATA_X_MSB(0X23),
            QUATERNION_DATA_Y_LSB(0X24),
            QUATERNION_DATA_Y_MSB(0X25),
            QUATERNION_DATA_Z_LSB(0X26),
            QUATERNION_DATA_Z_MSB(0X27),

            /* Linear acceleration data registers */
            LINEAR_ACCEL_DATA_X_LSB(0X28),
            LINEAR_ACCEL_DATA_X_MSB(0X29),
            LINEAR_ACCEL_DATA_Y_LSB(0X2A),
            LINEAR_ACCEL_DATA_Y_MSB(0X2B),
            LINEAR_ACCEL_DATA_Z_LSB(0X2C),
            LINEAR_ACCEL_DATA_Z_MSB(0X2D),

            /* Gravity data registers */
            GRAVITY_DATA_X_LSB(0X2E),
            GRAVITY_DATA_X_MSB(0X2F),
            GRAVITY_DATA_Y_LSB(0X30),
            GRAVITY_DATA_Y_MSB(0X31),
            GRAVITY_DATA_Z_LSB(0X32),
            GRAVITY_DATA_Z_MSB(0X33),

            /* Temperature data register */
            TEMP(0X34),

            /* Status registers */
            CALIB_STAT(0X35),
            SELFTEST_RESULT(0X36),
            INTR_STAT(0X37),

            SYS_CLK_STAT(0X38),
            SYS_STAT(0X39),
            SYS_ERR(0X3A),

            /* Unit selection register */
            UNIT_SEL(0X3B),
            DATA_SELECT(0X3C),

            /* Mode registers */
            OPR_MODE(0X3D),
            PWR_MODE(0X3E),

            SYS_TRIGGER(0X3F),
            TEMP_SOURCE(0X40),

            /* Axis remap registers */
            AXIS_MAP_CONFIG(0X41),
            AXIS_MAP_SIGN(0X42),

            /* SIC registers */
            SIC_MATRIX_0_LSB(0X43),
            SIC_MATRIX_0_MSB(0X44),
            SIC_MATRIX_1_LSB(0X45),
            SIC_MATRIX_1_MSB(0X46),
            SIC_MATRIX_2_LSB(0X47),
            SIC_MATRIX_2_MSB(0X48),
            SIC_MATRIX_3_LSB(0X49),
            SIC_MATRIX_3_MSB(0X4A),
            SIC_MATRIX_4_LSB(0X4B),
            SIC_MATRIX_4_MSB(0X4C),
            SIC_MATRIX_5_LSB(0X4D),
            SIC_MATRIX_5_MSB(0X4E),
            SIC_MATRIX_6_LSB(0X4F),
            SIC_MATRIX_6_MSB(0X50),
            SIC_MATRIX_7_LSB(0X51),
            SIC_MATRIX_7_MSB(0X52),
            SIC_MATRIX_8_LSB(0X53),
            SIC_MATRIX_8_MSB(0X54),

            /* Accelerometer Offset registers */
            ACCEL_OFFSET_X_LSB(0X55),
            ACCEL_OFFSET_X_MSB(0X56),
            ACCEL_OFFSET_Y_LSB(0X57),
            ACCEL_OFFSET_Y_MSB(0X58),
            ACCEL_OFFSET_Z_LSB(0X59),
            ACCEL_OFFSET_Z_MSB(0X5A),

            /* Magnetometer Offset registers */
            MAG_OFFSET_X_LSB(0X5B),
            MAG_OFFSET_X_MSB(0X5C),
            MAG_OFFSET_Y_LSB(0X5D),
            MAG_OFFSET_Y_MSB(0X5E),
            MAG_OFFSET_Z_LSB(0X5F),
            MAG_OFFSET_Z_MSB(0X60),

            /* Gyroscope Offset register s*/
            GYRO_OFFSET_X_LSB(0X61),
            GYRO_OFFSET_X_MSB(0X62),
            GYRO_OFFSET_Y_LSB(0X63),
            GYRO_OFFSET_Y_MSB(0X64),
            GYRO_OFFSET_Z_LSB(0X65),
            GYRO_OFFSET_Z_MSB(0X66),

            /* Radius registers */
            ACCEL_RADIUS_LSB(0X67),
            ACCEL_RADIUS_MSB(0X68),
            MAG_RADIUS_LSB(0X69),
            MAG_RADIUS_MSB(0X6A);
        //------------------------------------------------------------------------------------------
        public final byte bVal;
        private REGISTER(int i)
            {
            this.bVal = (byte)i;
            }
        }

    /**
     * Instances of MagneticFlux represent a three-dimensional magnetic strength vector. Units
     * are in tesla (NOT microtesla).
     */
    class MagneticFlux
        {
        //----------------------------------------------------------------------------------------------
        // State
        //----------------------------------------------------------------------------------------------
    
        public double x;
        public double y;
        public double z;
    
        //----------------------------------------------------------------------------------------------
        // Construction
        //----------------------------------------------------------------------------------------------
    
        public MagneticFlux()
            {
            this(0,0,0);
            }
        public MagneticFlux(double x, double y, double z)
            {
            this.x = x;
            this.y = y;
            this.z = z;
            }
        public MagneticFlux(double[] xyz)
            {
            this(xyz[0], xyz[1], xyz[2]);
            }
        }

    /**
     * A Quaternion can indicate an orientation in three-space without the trouble of
     * possible gimbal-lock.
     * 
     * @see <a href="https://en.wikipedia.org/wiki/Quaternion">https://en.wikipedia.org/wiki/Quaternion</a>
     * @see <a href="https://en.wikipedia.org/wiki/Gimbal_lock">https://en.wikipedia.org/wiki/Gimbal_lock</a>
     * @see <a href="https://www.youtube.com/watch?v=zc8b2Jo7mno">https://www.youtube.com/watch?v=zc8b2Jo7mno</a>
     * @see <a href="https://www.youtube.com/watch?v=mHVwd8gYLnI">https://www.youtube.com/watch?v=mHVwd8gYLnI</a>
     */
    class Quaternion
        {
        //----------------------------------------------------------------------------------------------
        // State
        //----------------------------------------------------------------------------------------------
    
        public double w;
        public double x;
        public double y;
        public double z;
    
        //----------------------------------------------------------------------------------------------
        // Construction
        //----------------------------------------------------------------------------------------------
    
        public Quaternion()
            {
            this.w = 1;
            this.x = this.y = this.z = 0;
            }
        public Quaternion(double w, double x, double y, double z)
            {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
            }
    
        //----------------------------------------------------------------------------------------------
        // Operations
        //----------------------------------------------------------------------------------------------
    
        public double magnitude()
            {
            return Math.sqrt(w*w + x*x + y*y + z*z);
            }
    
        public void normalize()
            {
            double mag = this.magnitude();
            w /= mag;
            x /= mag;
            y /= mag;
            z /= mag;
            }
        
        public Quaternion congugate()
            {
            Quaternion result = new Quaternion();
            result.w =  w;
            result.x = -x;
            result.y = -y;
            result.z = -y;
            return result;
            }
        
        }

    /**
     * Accleration represents a directed acceleration in three-space. Units are as specified
     * in sensor initialization.
     */
    class Acceleration
        {
        //----------------------------------------------------------------------------------------------
        // State
        //----------------------------------------------------------------------------------------------
    
        public double x;
        public double y;
        public double z;
    
        //----------------------------------------------------------------------------------------------
        // Construction
        //----------------------------------------------------------------------------------------------
    
        public Acceleration()
            {
            this(0,0,0);
            }
        public Acceleration(double x, double y, double z)
            {
            this.x = x;
            this.y = y;
            this.z = z;
            }
        public Acceleration(double[] xyz)
            {
            this(xyz[0], xyz[1], xyz[2]);
            }
        }

    /**
     * AngularVelocity represents a rotation rate in three-space. Units are as specified 
     * in sensor initialization, either radians/second or degrees/second.
     */
    class AngularVelocity
        {
        //----------------------------------------------------------------------------------------------
        // State
        //----------------------------------------------------------------------------------------------
    
        public double rpsX;
        public double rpsY;
        public double rpsZ;
    
        //----------------------------------------------------------------------------------------------
        // Construction
        //----------------------------------------------------------------------------------------------
    
        public AngularVelocity()
            {
            this(0,0,0);
            }
        public AngularVelocity(double rpsX, double rpsY, double rpsZ)
            {
            this.rpsX = rpsX;
            this.rpsY = rpsY;
            this.rpsZ = rpsZ;
            }
        public AngularVelocity(double[] xyz)
            {
            this(xyz[0], xyz[1], xyz[2]);
            }
        }

    /**
     * Instances of EulerAngles represent a direction in three-dimensional space by way of rotations.
     * Units are as specified in sensor initiation. Angles are in rotation order (heading, then roll,
     * then pitch) and are right-handed abou their respective axes.
     */
    class EulerAngles
        {
        //----------------------------------------------------------------------------------------------
        // State
        //----------------------------------------------------------------------------------------------
    
        /**
         * the rotation about the Z axis
         */
        public double heading;
        /**
         * the rotation about the Y axis
         */
        public double roll;
        /**
         * the rotation about the X axix
         */
        public double pitch;
    
        //----------------------------------------------------------------------------------------------
        // Construction
        //----------------------------------------------------------------------------------------------
    
        public EulerAngles()
            {
            this(0,0,0);
            }
        public EulerAngles(double heading, double roll, double pitch)
            {
            this.heading = heading;
            this.roll = roll;
            this.pitch = pitch;
            }
        public EulerAngles(double[] angles)
            {
            this(angles[0], angles[1], angles[2]);
            }
        }
    }
