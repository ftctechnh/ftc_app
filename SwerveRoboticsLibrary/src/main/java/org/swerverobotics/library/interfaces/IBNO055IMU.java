package org.swerverobotics.library.interfaces;

/**
 * Interface to Adafruit 9-DOF Absolute Orientation IMU Fusion Breakout - BNO055 
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
    public static class Parameters
        {
        // The address at which the sensor resides on the I2C bus
        public I2CADDR          i2cAddr             = I2CADDR.DEFAULT;
        
        // The mode we wish to use the sensor in
        public OPERATION_MODE   mode                = OPERATION_MODE.IMU;
        
        // Whether to use the external or internal crystal
        public boolean          useExternalCrystal  = true;  // external crystal is recommended per BNO055 spec

        // Unit Selection. Section 3.6.1 (p31) of the BNO055 specification
        public TEMPUNIT         temperatureUnit     = TEMPUNIT.CELSIUS;
        public ANGLEUNIT        angleunit           = ANGLEUNIT.RADIANS;
        public ACCELUNIT        accelunit           = ACCELUNIT.METERS_PERSEC_PERSEC;
        public PITCHMODE        pitchmode           = PITCHMODE.ANDROID;    // Section 3.6.2

        // Enums to make all that work
        public enum I2CADDR    { UNSPECIFIED(-1), DEFAULT(0x28), ALTERNATE(0x29);   public final byte bVal; I2CADDR(int i) { bVal =(byte)i; }}
        public enum TEMPUNIT   { CELSIUS(0), FARENHEIT(1);                          public final byte bVal; TEMPUNIT(int i) { bVal =(byte)i; }}
        public enum ANGLEUNIT  { DEGREES(0), RADIANS(1);                            public final byte bVal; ANGLEUNIT(int i) { bVal =(byte)i; }}
        public enum ACCELUNIT  { METERS_PERSEC_PERSEC(0), MILLIGALS(1);             public final byte bVal; ACCELUNIT(int i) { bVal =(byte)i; }}
        public enum PITCHMODE  { WINDOWS(0), ANDROID(1);                            public final byte bVal; PITCHMODE(int i) { bVal =(byte)i; }}

        /**
         * Operation modes are described in Table 3-5 (p21) of the BNO055 specification.
         */
        public enum OPERATION_MODE
            {
            CONFIG(0X00),       ACCONLY(0X01),          MAGONLY(0X02),
            GYRONLY(0X03),      ACCMAG(0X04),           ACCGYRO(0X05),
            MAGGYRO(0X06),      AMG(0X07),              IMU(0X08),
            COMPASS(0X09),      M4G(0X0A),              NDOF_FMC_OFF(0X0B),
            NDOF(0X0C);
            //------------------------------------------------------------------------------------------
            public final byte bVal; 
            OPERATION_MODE(int i) { this.bVal = (byte) i; }
            }
        }

    //----------------------------------------------------------------------------------------------
    // Sensor output
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
    }
