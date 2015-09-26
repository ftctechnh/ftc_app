//
// Sensors.h
//
// Kudos and gracious thanks to Xander Soldaat (mightor_at_gmail.com) for his
// original 3rd party driver framework, without the guidance and instruction
// of which the code herein would not have been possible.
//
// References:
//  http://www.robotc.net/forums/viewtopic.php?f=1&t=2114&view=previous
//  http://www.robotc.net/forums/viewtopic.php?f=1&t=703&hilit=sensorI2CCustomFastSkipStates
//  http://carrot.whitman.edu/Robots/NXT/NXT%20I2C%20Device%20Driver%20Design.htm

//------------------------------------------------------------------------------------
// Sensors (general)
//------------------------------------------------------------------------------------

void InitializeSensor_(IN OUT STICKYFAILURE& fOverallSuccess, const string& nm, SENSOR& sensor, int sensnmVal, MUXSENST typeMux, TSensorTypes typeDirect)
    {
    BOOL fSuccess         = true;
    sensor.cDetectRequest = 0;
    sensor.cReadRequest   = 0;
    sensor.fTriggered     = false;
    sensor.fTriggerRequested = false;
    sensor.sensnm         = sensnmVal;
    sensor.sensorReadFrequency = SENSOR_READ_FREQ_NORMAL;
    sensor.displayName    = nm;
    if (FMux(sensor))
        {
        /* Initialize that sensor mux if it's not already been initialized */
        InitializeSensorMux(fOverallSuccess, LINK_MUX(sensor));

        if (fOverallSuccess)
            {
            /* verify that the type of sensor attached is what is expected */
            fSuccess = CheckSensorTypeMux(SID_MUX(sensor), typeMux);
            if (!fSuccess)
                {
                TRACE(("%s: type is:%d want:%d", sensor.displayName, GetSensorTypeMux(SID_MUX(sensor)), typeMux));
                }
            }
        }
    else
        {
        /* Make sure the link type is set correctly */
        if (SensorType[SID_DIRECT(sensor)] != typeDirect)
            {
            SetSensorType(SID_DIRECT(sensor), typeDirect);
            wait1Msec(100); /* this wait duration is rumor and lore */
            }
        }
    if (fOverallSuccess) fOverallSuccess = fSuccess;
    }

#define StartReadingSensor_(sensor)      { sensor.cReadRequest++; }
#define StopReadingSensor_(sensor)       { sensor.cReadRequest--; }

#define StartDetectingSensor_(sensor)                                   \
    {                                                                   \
    if (0 == sensor.cDetectRequest) { sensor.fTriggered = false; }      \
    sensor.cDetectRequest++;                                            \
    }
#define StopDetectingSensor_(sensor)                                    \
    {                                                                   \
    sensor.cDetectRequest--;                                            \
    }
#define CheckSensor_(sensor,fTriggeredParam) { if (!fTriggeredParam && sensor.fTriggerRequested) { fTriggeredParam = sensor.fTriggered; } }

#define FDoSensorRead(sensor,context)       (FAnyReadRequests(sensor) && ((context.sensorReadFrequency & (sensor.sensorReadFrequency)) != 0))
#define FAnyReadRequests(sensor)            (sensor.cDetectRequest > 0 || sensor.cReadRequest > 0)
#define FDoSensorDetection(sensor)          (sensor.cDetectRequest > 0)

#define TriggerSensor_(sensor, context)                                                                     \
    {                                                                                                       \
    switch (context.action)                                                                                 \
        {                                                                                                   \
    case TRIGGER_ACTION_INITIALIZE: sensor.fTriggerRequested = false; break;                                \
    case TRIGGER_ACTION_ARM:        StartDetectingSensor_(sensor); break;                                   \
    case TRIGGER_ACTION_DISARM:     StopDetectingSensor_(sensor); break;                                    \
    case TRIGGER_ACTION_CHECK:      CheckSensor_(sensor,context.fTriggered); break;                         \
        }                                                                                                   \
    }


//------------------------------------------------------------------------------------
// Acceleration sensor
//
// The NXT Acceleration Sensor contains a three axis accelerometer that measures acceleration
// in three axes, x, y and z. Acceleration is measured in the range of –2g to +2g with scaling
// of approximately 200 counts per g.
//
// The Acceleration Sensor connects to an NXT sensor port using a standard NXT wire and uses
// the digital I2C communications protocol. The acceleration measurement for each axis is refreshed
// approximately 100 times per second.
//------------------------------------------------------------------------------------

#define HTAC_I2C_ADDR  0x02
#define HTAC_OFFSET    0x42
#define HTAC_X_UP      0x00
#define HTAC_Y_UP      0x01
#define HTAC_Z_UP      0x02
#define HTAC_X_LOW     0x03
#define HTAC_Y_LOW     0x04
#define HTAC_Z_LOW     0x05

// Convert 2 bytes into a signed 10 bit value.  If the 8 high bits are more than 127, make
// it a signed value before combing it with the lower 2 bits.
#define AssembleAccel(b0, b1)  ((((int)(b0) - (((b0) > 127) ? 256 : 0)) << 2) + (b1))

void ReadAccelSensor(ACCELSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    if (FDoSensorRead(sensor,context))
        {
        BOOL fSuccess = false;
        if (FMux(sensor))
            {
            if (SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 6, HTAC_X_UP))
                fSuccess = true;
            }
        else
            {
            FormatI2CReq(i2cSensorReq, HTAC_I2C_ADDR, HTAC_OFFSET + HTAC_X_UP);
            if (i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 6))
                fSuccess = true;
            }

        if (fSuccess)
            {
            sensor.x = AssembleAccel(i2cSensorRep.rgb[0], i2cSensorRep.rgb[3]);
            sensor.y = AssembleAccel(i2cSensorRep.rgb[1], i2cSensorRep.rgb[4]);
            sensor.z = AssembleAccel(i2cSensorRep.rgb[2], i2cSensorRep.rgb[5]);
            }
        }
    }

#define StartReadingAccelSensor(sensor)       StartReadingSensor_(sensor)
#define StopReadingAccelSensor(sensor)        StopReadingSensor_(sensor)

#define TriggerAccelSensor(sensor,context)  TriggerSensor_(sensor,context)

//------------------------------------------------------------------------------------
// Angle sensor
// http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NAA1030
//------------------------------------------------------------------------------------

#define HTANG_I2C_ADDR         0x02      /*!< HTCS2 I2C device address */
#define HTANG_CMD_REG          0x41      /*!< Command register */
#define HTANG_OFFSET           0x42      /*!< Offset for data registers */
#define HTANG_ANG2             0x00      /*!< Current angle (2 deg increments) */
#define HTANG_ANG1             0x01      /*!< Current angle (1 deg adder) */
#define HTANG_ACC_ANG_B4       0x02      /*!< 32 bit accumulated angle 4th byte */
#define HTANG_ACC_ANG_B3       0x03      /*!< 32 bit accumulated angle 3rd byte */
#define HTANG_ACC_ANG_B2       0x04      /*!< 32 bit accumulated angle 2nd byte */
#define HTANG_ACC_ANG_B1       0x05      /*!< 32 bit accumulated angle 1st byte */
#define HTANG_RPM_H            0x06      /*!< 16 bit rpms, high byte */
#define HTANG_RPM_L            0x07      /*!< 16 bit rpms, low byte */
#define HTANG_CMD_MEASURE      0x00      /*!< Normal angle measurement mode */
#define HTANG_CMD_RST_ANG      0x43      /*!< Resets 0 position to current shaft angle, non-volatile setting */
#define HTANG_CMD_RST_ACC_ANG  0x52      /*!< Resets the accumulated angle */

#define RawReadAngleSensor(sensor)                                                                  \
    {                                                                                               \
    BOOL fSuccess;                                                                                  \
    if (FMux(sensor))                                                                               \
        {                                                                                           \
        fSuccess = SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 2, HTANG_ANG2);                      \
        }                                                                                           \
    else                                                                                            \
        {                                                                                           \
        FormatI2CReq(i2cSensorReq, HTANG_I2C_ADDR, HTANG_OFFSET + HTANG_ANG2);                      \
        fSuccess = i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 2);        \
        }                                                                                           \
    if (fSuccess)                                                                                   \
        {                                                                                           \
        /* value from the sensor is in degCW, so we negate */                                       \
        sensor.degRawPrev = sensor.degRaw;                                                          \
        sensor.degRaw = -AssembleBytes2(i2cSensorRep, int, 1, 0);                                   \
        /* detect wraps */                                                                          \
        float delta = sensor.degRaw - sensor.degRawPrev;                                            \
        if (delta > 180.0 || delta < -180.0)                                                        \
            {                                                                                       \
            /* the value wrapped, but in which direction? */                                        \
            if (delta < 0)                                                                          \
                {                                                                                   \
                /* we were incrementing, but went down instead */                                   \
                delta += 360.0;                                                                     \
                }                                                                                   \
            else                                                                                    \
                {                                                                                   \
                /* we were decrementing, but went up instead */                                     \
                delta -= 360.0;                                                                     \
                }                                                                                   \
            }                                                                                       \
        else                                                                                        \
            {                                                                                       \
            /* no wrap */                                                                           \
            }                                                                                       \
        sensor.degUnwrapped += delta;                                                               \
                                                                                                    \
        /* The actual degrees are the unwrapped values scaled by the gear ratio */                  \
        sensor.deg = sensor.degUnwrapped * sensor.gearing;                                          \
        }                                                                                           \
    }

void ReadAngleSensor(ANGLESENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    if (FDoSensorRead(sensor,context))
        {
        RawReadAngleSensor(sensor);
        /**/
        if (FDoSensorDetection(sensor) && !sensor.fTriggered)
            {
            sensor.fTriggered = Compare(sensor.deg, sensor.degTarget, sensor.comparison);
            }
        }
    }

#define TriggerAngleSensor(sensor,context)    TriggerSensor_(sensor,context)
#define StopReadingAngleSensor(sensor)        StopReadingSensor_(sensor)

ANGLE StartReadingAngleSensor(ANGLESENSOR& sensor, BOOL fForceRead)
    {
    ANGLE degResult = 0.0;
    StartReadingSensor_(sensor);
    if (fForceRead)
        {
        LockBlackboard();
        READ_SENSORS_CONTEXT context;
        InitializeReadSensorsContext(context, SENSOR_READ_FREQ_NORMAL);
        ReadAngleSensor(sensor, context);
        degResult = sensor.deg;
        ReleaseBlackboard();
        }
    return degResult;
    }

//------------------------------------------------------------------------------------
// Color sensor
//------------------------------------------------------------------------------------

#define HTCS2_I2C_ADDR        0x02      /*!< HTCS2 I2C device address */
#define HTCS2_CMD_REG         0x41      /*!< Command register */
#define HTCS2_OFFSET          0x42      /*!< Offset for data registers */
// Values contained by registers in active mode
#define HTCS2_COLNUM_REG      0x00      /*!< Color number */
#define HTCS2_RED_REG         0x01      /*!< Red reading */
#define HTCS2_GREEN_REG       0x02      /*!< Green reading */
#define HTCS2_BLUE_REG        0x03      /*!< Blue reading */
#define HTCS2_WHITE_REG       0x04      /*!< White channel reading */
#define HTCS2_COL_INDEX_REG   0x05      /*!< Color index number */
#define HTCS2_RED_NORM_REG    0x06      /*!< Normalised red reading */
#define HTCS2_GREEN_NORM_REG  0x07      /*!< Normalised green reading */
#define HTCS2_BLUE_NORM_REG   0x08      /*!< Normalised blue reading */
// Values contained by registers in passive and raw mode
#define HTCS2_RED_MSB         0x00      /*!< Raw red reading - MSB */
#define HTCS2_RED_LSB         0x00      /*!< Raw red reading - LSB */
#define HTCS2_GREEN_MSB       0x00      /*!< Raw green reading - MSB */
#define HTCS2_GREEN_LSB       0x00      /*!< Raw green reading - LSB */
#define HTCS2_BLUE_MSB        0x00      /*!< Raw blue reading - MSB */
#define HTCS2_BLUE_LSB        0x00      /*!< Raw blue reading - LSB */
#define HTCS2_WHITE_MSB       0x00      /*!< Raw white channel reading - MSB */
#define HTCS2_WHITE_LSB       0x00      /*!< Raw white channel reading - LSB */
// Different modes
#define HTCS2_MODE_ACTIVE     0x00      /*!< Use ambient light cancellation */
#define HTCS2_MODE_PASSIVE    0x01      /*!< Disable ambient light cancellation */
#define HTCS2_MODE_RAW        0x03      /*!< Raw data from light sensor */
#define HTCS2_MODE_50HZ       0x35      /*!< Set sensor to 50Hz cancellation mode */
#define HTCS2_MODE_60HZ       0x36      /*!< Set sensor to 60Hz cancellation mode */

#define MatchColorComponent(target, read, tolerance)                                                        \
    ((target) < 0) || (Abs((((target) - (read)) * 100) / (target)) < (tolerance))

void ReadColorSensor(COLORSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    sensor.red = sensor.green = sensor.blue = 0;
    if (FDoSensorRead(sensor,context))
        {
        BOOL fSuccess = false;
        do  {
            if (FMux(sensor))
                {
                fSuccess = SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 3, HTCS2_RED_REG);
                }
            else
                {
                FormatI2CReq(i2cSensorReq, HTCS2_I2C_ADDR, HTCS2_OFFSET + HTCS2_RED_REG);
                fSuccess = i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 3);
                }
            }
        while (!fSuccess);

        sensor.red   = i2cSensorRep.rgb[0];
        sensor.green = i2cSensorRep.rgb[1];
        sensor.blue  = i2cSensorRep.rgb[2];

        if (FDoSensorDetection(sensor) && !sensor.fTriggered)
            {
            sensor.fTriggered = MatchColorComponent(sensor.targetRed,   sensor.red,   sensor.tolerance)
                             && MatchColorComponent(sensor.targetGreen, sensor.green, sensor.tolerance)
                             && MatchColorComponent(sensor.targetBlue,  sensor.blue,  sensor.tolerance);
            }
        }
    }

// Targets of -1 are 'don't care'
#define SetTargetColor(sensor, red, green, blue)  \
    {                                             \
    sensor.targetRed = red;                       \
    sensor.targetGreen = green;                   \
    sensor.targetBlue = blue;                     \
    }

#define TriggerColorSensor(sensor,context) TriggerSensor_(sensor,context)
#define StartReadingColorSensor(sensor)    StartReadingSensor_(sensor)
#define StopReadingColorSensor(sensor)     StopReadingSensor_(sensor)

//------------------------------------------------------------------------------------
// Compass sensor
//------------------------------------------------------------------------------------

#define HTMC_I2C_ADDR       0x02  /*!< HTMC I2C device address */
#define HTMC_MODE           0x41  /*!< HTMC Mode control */
#define HTMC_HEAD_U         0x42  /*!< HTMC Heading Upper bits */
#define HTMC_HEAD_L         0x43  /*!< HTMC Heading Lower bit */
#define HTMC_MEASURE_CMD    0x00  /*!< HTMC measurement mode command */
#define HTMC_CALIBRATE_CMD  0x43  /*!< HTMC calibrate mode command */

// The raw compass returns the heading in degrees (0 - 359). Note that the compass
// reports degress *clockwise*, which is backwards from the usual convention in
// coordinate geometry. So we correct for that.
void ReadCompassSensor(COMPASSSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    sensor.value = intFirst;
    if (FDoSensorRead(sensor,context))
        {
        BOOL fDone = false;
        do  {
            if (FMux(sensor))
                {
                if (SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 2, 0))
                    fDone = true;
                }
            else
                {
                FormatI2CReq(i2cSensorReq, HTMC_I2C_ADDR, HTMC_HEAD_U);
                if (i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 2))
                    fDone = true;
                }
            }
        while (!fDone);

        /* Assemble from the reply. Correct for CW vs CCW. */
        int heading = -AssembleBytes2(i2cSensorRep, int, 1, 0);

        /* Convert relative to the startup angle */
        sensor.value = (float)heading - sensor.bias;

        /* Normalize into [-180,180) */
        NormalizeAngleVar(sensor.value);
        }
    }

// To carry out calibration, one does the following:
//      1. call StartCompassCalibration()
//      2. Rotate the the sesnor a litle more than 360 degrees in the horizontal
//          plane in NO LESS THAN 20 seconds.
//      3. Call StopCompassCalibration()
// The calibrated value will be persistently stored in the sensor until a new
// calibration is carried out.
//
// Note that calibration cannot be done while the sensor is connected to a sensor mux,
// as the commands to enter/exit the calibration mode cannot be sent through the mux.

#define StartCompassCalibration(link, fSuccess)                                         \
    {                                                                                   \
    FormatI2CCmd(i2cSensorReq, HTMC_I2C_ADDR, HTMC_MODE, HTMC_CALIBRATE_CMD);           \
    fSuccess = i2cSendSensor(link, i2cSensorReq, 0);                                    \
    }

#define StopCompassCalibration(link, fSuccess)                                          \
    {                                                                                   \
    FormatI2CCmd(i2cSensorReq, HTMC_I2C_ADDR, HTMC_MODE, HTMC_MEASURE_CMD);             \
    fSuccess = i2cSendReceiveSensor(link, i2cSensorReq, i2cSensorRep, 1);               \
    if (fSuccess)                                                                       \
        {                                                                               \
        /* the compass returns '2' if the calibration failed */                         \
        fSuccess = i2cSensorRep.rgb[0] != 2;                                            \
        }                                                                               \
    }

#define TriggerCompassSensor(sensor,context) TriggerSensor_(sensor,context)
#define StartReadingCompassSensor(sensor)    StartReadingSensor_(sensor)
#define StopReadingCompassSensor(sensor)     StopReadingSensor_(sensor)

//------------------------------------------------------------------------------------
// EOPD sensor
//
// http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NEO1048
//------------------------------------------------------------------------------------

#define InitializeEopdStopConditions(sensor)                                            \
    {                                                                                   \
    sensor.target         = 0;                                                          \
    sensor.comparison     = COMPARE_GT;                                                 \
    sensor.countRequired  = 1;                                                          \
    sensor.msDetectDelay  = 0;                                                          \
    }

void InitializeEopdSensor(STICKYFAILURE& fOverallSuccess, string nm, EOPDSENSOR& sensor, int sensnmVal, EOPD_CONFIG processed, EOPD_RANGE range)
    {
    InitializeSensor(fOverallSuccess, nm, sensor, sensnmVal, MUXSENST_ANALOG, sensorAnalogInactive);
    sensor.fReadProcessed = (processed==EOPD_PROCESSED);
    InitializeEopdStopConditions(sensor);
    BOOL fSuccess = true;
    LockBlackboard(); /* for SMUXsetAnalogActive and SMUXsetAnalogInactive */
    if (EOPD_LONG==range)
        {
        if (FMux(sensor))
            {
            SMUXsetAnalogActive(fSuccess, SID_MUX(sensor));
            }
        else
            {
            SetSensorType(SID_DIRECT(sensor), sensorAnalogActive);
            wait1Msec(10);
            }
        }
    else
        {
        if (FMux(sensor))
            {
            SMUXsetAnalogInactive(fSuccess, SID_MUX(sensor));
            }
        else
            {
            SetSensorType(SID_DIRECT(sensor), sensorAnalogInactive);
            wait1Msec(10);
            }
        }
    ReleaseBlackboard();
    if (fOverallSuccess) fOverallSuccess = fSuccess;
    TraceSensorInitializationResult(nm, fOverallSuccess);
    }

void TriggerEopdSensor(EOPDSENSOR& sensor, TRIGGER_SENSORS_CONTEXT& context)
    {
    switch (context.action)
        {
    case TRIGGER_ACTION_INITIALIZE: sensor.fTriggerRequested = false; break;
    case TRIGGER_ACTION_DISARM:     StopDetectingSensor_(sensor); break;
    case TRIGGER_ACTION_CHECK:      CheckSensor_(sensor,context.fTriggered); break;
    case TRIGGER_ACTION_ARM:
        sensor.countDetected = 0;
        sensor.msDetectThreshold = nSysTime + sensor.msDetectDelay;
        StartDetectingSensor_(sensor);
        break;
        }
    }

#define StartReadingEopdSensor(sensor)          StartReadingSensor_(sensor)
#define StopReadingEopdSensor(sensor)           StopReadingSensor_(sensor)

#define RawReadEopd(sensor)                                                             \
    {                                                                                   \
    if (FMux(sensor))                                                                   \
        {                                                                               \
        SMUXreadAnalog(SID_MUX(sensor), sensor.value);                                  \
        }                                                                               \
    else                                                                                \
        {                                                                               \
        sensor.value = SensorRaw[SID_DIRECT(sensor)];                                   \
        }                                                                               \
    sensor.value = 1023 - sensor.value;                                                 \
    }

void ReadEOPDSensor(EOPDSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    if (FDoSensorRead(sensor,context))
        {
        RawReadEopd(sensor);
        if (sensor.fReadProcessed)
            {
            sensor.value = sqrt(sensor.value * 10);
            }

        if (FDoSensorDetection(sensor))
            {
            if (!sensor.fTriggered && nSysTime >= sensor.msDetectThreshold)
                {
                BOOL fDetected = Compare(sensor.value, sensor.target, sensor.comparison);
                if (fDetected)
                    {
                    sensor.countDetected++;
                    if (sensor.countDetected == sensor.countRequired)
                        {
                        sensor.fTriggered = true;
                        }
                    }
                else
                    {
                    sensor.countDetected = 0;   // we require consecutive detections
                    }
                }
            }
        }
    }

//------------------------------------------------------------------------------------
// Gryo sensor
//
// From HiTechnic.com:
// "The Gyro Sensor connects to an NXT sensor port using a standard NXT wire and utilizes
// the analog sensor interface.  The rotation rate can be read up to approximately 300
// times per second."
//
// Note that the value coming out of the sensor is the rotation rate in the *clockwise*
// direction.
//
// http://nxttime.wordpress.com/2010/11/03/gyro-offset-and-drift/
// http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NGY1044
// http://proj.titanrobotics.net/hg/Frc/2011/code/file/ea68beef9fd9/trclib.nxt/gyro.h
// http://mightor.wordpress.com/2009/11/17/you-spin-me-right-round-baby-right-round/
//
// Sees same error profile as we do:
// http://www.robotc.net/forums/viewtopic.php?f=52&t=2804&hilit=gyro
// http://www.robotc.net/forums/viewtopic.php?f=52&t=3004&hilit=gyro
//------------------------------------------------------------------------------------

#define RawReadGyroSensor(sensor)                                                               \
    {                                                                                           \
    if (FMux(sensor))                                                                           \
        {                                                                                       \
        int degPerSInt;                                                                         \
        SMUXreadAnalog(SID_MUX(sensor), degPerSInt);                                            \
        sensor.degCWPerS = degPerSInt;                                                          \
        }                                                                                       \
    else                                                                                        \
        {                                                                                       \
        sensor.degCWPerS = SensorRaw[SID_DIRECT(sensor)];                                       \
        }                                                                                       \
    sensor.degCWPerS -= sensor.bias;                                                            \
    if (sensor.deadbandMin < sensor.degCWPerS && sensor.degCWPerS < sensor.deadbandMax)         \
        {                                                                                       \
        sensor.degCWPerS = 0;                                                                   \
        }                                                                                       \
    sensor.degCWPerS *= sensor.degCWPerSScale;                                                  \
    }

#define TriggerGyroSensor(sensor,context)       TriggerSensor_(sensor,context)
#define StartReadingGyroSensor(sensor)          { if (!FAnyReadRequests(sensor)) { OnFirstStartReadingGyroSensor(sensor); } StartReadingSensor_(sensor); }
#define StopReadingGyroSensor(sensor)           StopReadingSensor_(sensor)

#define OnFirstStartReadingGyroSensor(sensor)   { sensor.msIntegratePrev = 0; }

// Note: we DON'T normalize the gyro deg value to the usual +-180 range, as doing so would
// hide the 'winding number' and make it very hard to, e.g., distinguish a turn of 360 deg
// from a turn of 0 deg or 720 deg.
void ReadGyroSensor(GYROSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    if (FDoSensorRead(sensor,context))
        {
        MILLI msNow = nSysTime;

        RawReadGyroSensor(sensor);
        /* Keep track of # of reads in order to later be able to print read-rate statistics.    */
        sensor.cRead++;

        if (0==sensor.msIntegratePrev)
            {
            /* First time through, do actual work the next time around */
            }
        else
            {
            /* 'No point in integrating in the same time quantum, and this code structure       */
            /* provides a convenient playground for experimenting with throttles on gyro reads  */
            MILLI dt = (msNow - sensor.msIntegratePrev);
            if (dt > 0)
                {
                /* Multiply by 0.001 since our time measurements are in ms to yield degees      */
                /* instead of degees x 1000. Also negate (thus the constant is -0.001 not 0.001 */
                /* to give the angle in the CCW direction instead of the CW direction           */
                /* Finally, we do trapazoidal integration here: we average the degCWPerS over   */
                /* the interval rather than just using the current value. This cuts the 0.001   */
                /* constant in half to 0.0005 */
                float delta = (float)(dt) * (sensor.degCWPerS + sensor.degCWPerSPrev) * -0.0005;
                sensor.deg += delta;
                }
            }
        /* remember previous sample data for the next time around the loop */
        sensor.msIntegratePrev = msNow;
        sensor.degCWPerSPrev   = sensor.degCWPerS;

        if (FDoSensorDetection(sensor) && !sensor.fTriggered)
            {
            sensor.fTriggered = Compare(sensor.deg, sensor.degTarget, sensor.comparison);
            if (sensor.fTriggered)
                {
                sensor.degDetected = sensor.deg;    /* mostly for debugging */
                }
            }
        }
    }

void InitializeGyroSensor(STICKYFAILURE& fOverallSuccess, string nm, GYROSENSOR& sensor, int sensnmVal, int cSample, MILLI msWait)
// This routine samples the gyro cSample times in order to get a bearing on it's zero-point bias.
// Having an accurate assessment of this reading is extraordinarily important to getting good
// integrated angular measurements from the device. See the declaration of InitializeGyroSensor
// in sensorDecl.h for the default values of cSample and msWait.
    {
    InitializeSensor(fOverallSuccess, nm, sensor, sensnmVal, MUXSENST_ANALOG, sensorAnalogInactive);
    sensor.sensorReadFrequency = SENSOR_READ_FREQ_OFTEN;

    /* Calibrate the sensor by taking the average bias of a few raw readings         */
    /* The bias is the 'zero deg/s' value; higher values are CW, lower are CCW, in   */
    /* units of degrees per second                                                   */
    sensor.bias = 0;
    sensor.deadbandMax = 0;
    sensor.deadbandMin = 0;
    sensor.degCWPerSScale = 1.0;
    float sum = 0;
    float degMin = 1024, degMax = 0;
    //
    if (fOverallSuccess)
        {
        LockBlackboard();
        hogCpuNestable();                   // we really want a good bias computation, so shut down noise sources by hogging
        for (int i = 0; i < cSample; i++)
            {
            RawReadGyroSensor(sensor);
            sum += sensor.degCWPerS;
            MinVar(degMin, sensor.degCWPerS);
            MaxVar(degMax, sensor.degCWPerS);
            if (msWait>0) wait1Msec(msWait);
            }
        releaseCpuNestable()
        ReleaseBlackboard();
        }
    //
    sensor.bias        = sum / (float)cSample;
    sensor.deadbandMax = 0; // degMax - sensor.bias; // disable for now pending testing and actual utility analysis
    sensor.deadbandMin = 0; // degMin - sensor.bias;
    sensor.degCWPerS   = 0;
    sensor.deg         = 0;
    sensor.degDetected = 0;
    sensor.cRead       = 0;
    sensor.msIntegratePrev = 0;
    sensor.degCWPerSPrev = 0;
    sensor.degTarget  = 0;
    sensor.comparison = COMPARE_GT;
    if (fOverallSuccess) { TRACE(("gyro bias=%.3f band=%.3f %.3f", sensor.bias, sensor.deadbandMax, sensor.deadbandMin)); }
    TraceSensorInitializationResult(nm, fOverallSuccess);
    }

//------------------------------------------------------------------------------------
// IR sensor
//------------------------------------------------------------------------------------

#define StartDetectingIRSensor(sensor)                                                          \
    {                                                                                           \
    StartDetectingSensor_(sensor);                                                              \
    }
#define StopDetectingIRSensor(sensor)                                                           \
    {                                                                                           \
    StopDetectingSensor_(sensor);                                                               \
    }

#define StartReadingIRSensor(sensor)      StartReadingSensor_(sensor)
#define StopReadingIRSensor(sensor)       StopReadingSensor_(sensor)

#define HTIRS2_I2C_ADDR    0x10      /*!< IR Seeker I2C device address                           */
#define HTISR2_REG_MODE    0x41      /* register address of the IR reading mode                  */
#define HTIRS2_DIR_DC      0x42      /* DC direction: 0-9                                        */
#define HTIRS2_REG_DC      0x43      /* register address of first DC signal strength 0x43 - 0x47 */
#define HTIRS2_MEAN_DC     0x48      /* sensor DC mean                                           */
#define HTIRS2_DIR_AC      0x49      /* AC direction: 0-9                                        */
#define HTIRS2_REG_AC      0x4A      /* register address of first AC signal strength 0x4A - 0x4E */

#define HTIRS2_READ_BASE_DC   0x42
#define HTIRS2_READ_BASE_AC   0x49

void InitializeIRSensor(STICKYFAILURE& fOverallSuccess, string nm, IRSENSOR& sensor, int sensnm, IRSENSOR_MODE mode)
    {
    InitializeSensor(fOverallSuccess, nm, sensor, sensnm, MUXSENST_IRSEEK, sensorI2CCustom);
    sensor.mode = mode;
    TraceSensorInitializationResult(nm, fOverallSuccess);
    }

void ReadIRSensor(IRSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    if (FDoSensorRead(sensor,context))
        {
        if (FMux(sensor))
            {
            if (IRSENSOR_MODE_DC==sensor.mode || IRSENSOR_MODE_ACDC==sensor.mode)
                {
                // Read the DC data
                if (SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 6, HTIRS2_READ_BASE_DC))
                    {
                    sensor.dirDC       = i2cSensorRep.rgb[0];
                    sensor.signalDC[0] = i2cSensorRep.rgb[1];
                    sensor.signalDC[1] = i2cSensorRep.rgb[2];
                    sensor.signalDC[2] = i2cSensorRep.rgb[3];
                    sensor.signalDC[3] = i2cSensorRep.rgb[4];
                    sensor.signalDC[4] = i2cSensorRep.rgb[5];
                    }
                }
            if (IRSENSOR_MODE_AC==sensor.mode || IRSENSOR_MODE_ACDC==sensor.mode)
                {
                // Read the AC data
                if (SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 6, HTIRS2_READ_BASE_AC))
                    {
                    sensor.dirAC       = i2cSensorRep.rgb[0];
                    sensor.signalAC[0] = i2cSensorRep.rgb[1];
                    sensor.signalAC[1] = i2cSensorRep.rgb[2];
                    sensor.signalAC[2] = i2cSensorRep.rgb[3];
                    sensor.signalAC[3] = i2cSensorRep.rgb[4];
                    sensor.signalAC[4] = i2cSensorRep.rgb[5];
                    }
                }
            }
        else
            {
            if (IRSENSOR_MODE_DC==sensor.mode || IRSENSOR_MODE_ACDC==sensor.mode)
                {
                // Read the DC data
                FormatI2CReq(i2cSensorReq, HTIRS2_I2C_ADDR, HTIRS2_READ_BASE_DC);
                if (i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 6))
                    {
                    sensor.dirDC       = i2cSensorRep.rgb[0];
                    sensor.signalDC[0] = i2cSensorRep.rgb[1];
                    sensor.signalDC[1] = i2cSensorRep.rgb[2];
                    sensor.signalDC[2] = i2cSensorRep.rgb[3];
                    sensor.signalDC[3] = i2cSensorRep.rgb[4];
                    sensor.signalDC[4] = i2cSensorRep.rgb[5];
                    }
                }
            if (IRSENSOR_MODE_AC==sensor.mode || IRSENSOR_MODE_ACDC==sensor.mode)
                {
                // Read the AC data
                FormatI2CReq(i2cSensorReq, HTIRS2_I2C_ADDR, HTIRS2_READ_BASE_AC);
                if (i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 6))
                    {
                    sensor.dirAC       = i2cSensorRep.rgb[0];
                    sensor.signalAC[0] = i2cSensorRep.rgb[1];
                    sensor.signalAC[1] = i2cSensorRep.rgb[2];
                    sensor.signalAC[2] = i2cSensorRep.rgb[3];
                    sensor.signalAC[3] = i2cSensorRep.rgb[4];
                    sensor.signalAC[4] = i2cSensorRep.rgb[5];
                    }
                }
            }
        }
    }

#define TriggerIRSensor(sensor,context)  TriggerSensor_(sensor,context)

//------------------------------------------------------------------------------------
// Magnetic sensor
//
// We 'detect' on the magnetic sensor when we reach an apogee in the readings. Or,
// more correctly, just after we reach the apogee. In determining the apogee, we have
// to be careful of noise in the readings.
//
// http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NMS1035
//------------------------------------------------------------------------------------

#define RawReadMagneticSensor(sensor)                                                           \
    {                                                                                           \
    if (FMux(sensor))                                                                           \
        {                                                                                       \
        SMUXreadAnalog(SID_MUX(sensor), sensor.value);                                          \
        }                                                                                       \
    else                                                                                        \
        {                                                                                       \
        sensor.value = SensorRaw[SID_DIRECT(sensor)];                                           \
        }                                                                                       \
    }

// Variables used to manage not losing magnetic batons that we've detected in the hand
// REVIEW: These are GetOverIt-specific, and would be better put in some other header file
ANGLE degRotorDispensingStop;
BOOL  fRotorDispensingStopValid = false;    // whether degRotorDispensingStop is valid or not

void ReadMagneticSensor(MAGNETICSENSOR& sensor, READ_SENSORS_CONTEXT& context, ANGLE deg /*only necessary if there is an assoc'd angle sensor */)
    {
    if (FDoSensorRead(sensor,context))
        {
        RawReadMagneticSensor(sensor);

        /* subtract the bias so that 'no magnet around' yields a zero value */
        sensor.value -= sensor.bias;

        /* Take absolute value so 'nothing' is zero and 'magnet' is > 0, independent */
        /* of the North-South orientation of the magnet relative to the sensor       */
        sensor.value = abs(sensor.value);

        /* Compute whether values are increasing or decreasing, and whether that's a change */
        int delta = sensor.value - sensor.valuePrev;
        if (delta >= -1 && delta <= 1)
            {
            /* jitter - ignore until the reading moves further */
            /* isn't this VALUECHANGE_STEADY? */
            }
        else
            {
            /* value is significantly different than the one we previously took note of */
            sensor.valueChangePrev = sensor.valueChange;
            sensor.valueChange     = delta > 0 ? VALUECHANGE_ASCENDING : VALUECHANGE_DESCENDING;
            sensor.valuePrev       = sensor.value;
            }

        /* "If we're detecting and there's a magnet in somewhat close proximity..." */
        if (FDoSensorDetection(sensor) && sensor.value >= sensor.threshold)
            {
            /* "If we just started our descent..." */
            if (VALUECHANGE_DESCENDING==sensor.valueChange && VALUECHANGE_DESCENDING!=sensor.valueChangePrev)
                {
                /* We're just after the apogee. Detect! */
                sensor.fTriggered = true;
                PlayHappyNoWait();

                /* If we have an associated angle sensor, record the angle at which the detection occurred */
                if (sensor.isensangleAssociated >= 0)
                    {
                    /* Remember the angle in the next avaialable slot */
                    sensor.rgdegDetectionRecord[sensor.idegDetectionRecordNext]    = deg;
                    sensor.rgfDetectionRecordValid[sensor.idegDetectionRecordNext] = true;
                    sensor.idegDetectionRecordNext++;

                    /* Wrap the slot index it's always a valid index */
                    if (cdegMagDetectionRecordMax == sensor.idegDetectionRecordNext)
                        sensor.idegDetectionRecordNext = 0;

                    /* Be sure the magnetic baton isn't rotated all the way off the rotor */
                    #if defined(DEG_ROTOR_TURN_MAG_STABLE_CARRY)
                    if (!fRotorDispensingStopValid)
                        {
                        degRotorDispensingStop    = deg + DEG_ROTOR_TURN_MAG_STABLE_CARRY;
                        fRotorDispensingStopValid = true;
                        }
                    #endif
                    }
                }
            }
        }
    }

#define ResetMagneticSensorDetection(sensor)                                                    \
    {                                                                                           \
    sensor.valueChange     = VALUECHANGE_STEADY;                                                \
    sensor.valueChangePrev = VALUECHANGE_STEADY;                                                \
    sensor.idegDetectionRecordNext = 0;                                                         \
    }

void InitializeMagneticSensor(STICKYFAILURE& fOverallSuccess, string nm, MAGNETICSENSOR& sensor, int sensnmVal)
    {
    InitializeSensor(fOverallSuccess, nm, sensor, sensnmVal, MUXSENST_ANALOG, sensorRawValue);
    ResetMagneticSensorDetection(sensor);
    sensor.isensangleAssociated = -1;
    sensor.threshold = 30;
    sensor.bias = 0;
    /* Calibrate the sensor by taking the average bias of a few raw readings */
    int sum = 0;
    const int cTrial = 5;
    if (fOverallSuccess)
        {
        LockBlackboard();
        for (int i = 0; i < cTrial; )
            {
            RawReadMagneticSensor(sensor);
            sum += sensor.value;
            i++;
            wait1Msec(50);
            }
        ReleaseBlackboard();
        }
    sensor.bias  = sum / cTrial;
    sensor.value = 0;
    if (fOverallSuccess) { TRACE(("mag bias=%d", sensor.bias)); }
    TraceSensorInitializationResult(nm, fOverallSuccess);
    }

#define StartReadingMagneticSensor(sensor)          StartReadingSensor_(sensor)
#define StopReadingMagneticSensor(sensor)           StopReadingSensor_(sensor)

void TriggerMagneticSensor(MAGNETICSENSOR& sensor, TRIGGER_SENSORS_CONTEXT& context)
    {
    switch (context.action)
        {
    case TRIGGER_ACTION_INITIALIZE: sensor.fTriggerRequested = false; break;
    case TRIGGER_ACTION_DISARM:     StopDetectingSensor_(sensor); break;
    case TRIGGER_ACTION_CHECK:      CheckSensor_(sensor,context.fTriggered); break;
    case TRIGGER_ACTION_ARM:
        if (!FDoSensorDetection(sensor)) { ResetMagneticSensorDetection(sensor); }
        StartDetectingSensor_(sensor);
        break;
        }
    }

#define StartDetectingMagneticSensor(sensor) { TRIGGER_SENSORS_CONTEXT context; InitializeTriggerSensorsContext(context, TRIGGER_ACTION_ARM);    TriggerMagneticSensor(sensor, context); }
#define StopDetectingMagneticSensor(sensor)  { TRIGGER_SENSORS_CONTEXT context; InitializeTriggerSensorsContext(context, TRIGGER_ACTION_DISARM); TriggerMagneticSensor(sensor, context); }

//------------------------------------------------------------------------------------
// (Ultra)Sonic sensor
//------------------------------------------------------------------------------------

#define LEGOUS_I2C_ADDR    0x10      /*!< Lego US I2C address */
#define LEGOUS_REG_CMD     0x41      /*!< Command register */
#define LEGOUS_REG_DATA    0x42      /*!< Start of measurement data registers */
#define LEGOUS_CMD_OFF    0x00      /*!< Command to switch US off */
#define LEGOUS_CMD_SSHOT  0x01      /*!< Command to turn on Single Shot mode */
#define LEGOUS_CMD_CONT   0x02      /*!< Command to turn on Continuous Mode */
#define LEGOUS_CMD_ECAPT  0x03      /*!< Command to turn on Event Capture Mode */
#define LEGOUS_CMD_RST    0x04      /*!< Command to request a warm reset */

#define cmSonicNil      255         // value to return when no sonic reading is available
#define cmSonicRawNil   cmSonicNil  // difference is deprecated

void RawReadSonic(SONICSENSOR& sensor)
    {
    sensor.cm = cmSonicNil;

    BOOL fSuccess;
    if (FMux(sensor))
        {
        fSuccess = SMUXreadPort(SID_MUX(sensor), i2cSensorRep, 1, 0);
        }
    else
        {
        FormatI2CReq(i2cSensorReq, LEGOUS_I2C_ADDR, LEGOUS_REG_DATA);
        fSuccess = i2cSendReceiveSensor(LINK_DIRECT(sensor), i2cSensorReq, i2cSensorRep, 1);
        }
    if (fSuccess)
        sensor.cm = i2cSensorRep.rgb[0];
    }

// Wait until a new reading is available on this sonic sensor
#define WaitForNewSonicReading(sensor)                                                      \
    {                                                                                       \
    /* The rate at which ultrasonic sensor can be read is limited, both by the speed of */  \
    /* sound and internal implementation issues:                                        */  \
    /*      http://www.robotc.net/forums/viewtopic.php?f=1&t=2224&hilit=ultrasonic      */  \
    /* In short, the ultrasonic sensors take one reading in ~77ms, so if we want a new  */  \
    /* reading we need to be at least that long since the last one; we use 80 to be     */  \
    /* sure. More correctly, that was the native RobotC read speed; with us here using  */  \
    /* own I2C routines, we might be able to do faster (see end of the thread above).   */  \
    const MILLI msMin   = 80;                                                               \
    const MILLI msNow   = nSysTime;                                                         \
    const MILLI msDelta = msNow - sensor.msLastRead;                                        \
    const MILLI msWait  = msMin - msDelta;                                                  \
    if (msWait > 0)                                                                         \
        {                                                                                   \
        ReleaseBlackboard();                                                                \
        wait1Msec(msWait);                                                                  \
        LockBlackboard();                                                                   \
        }                                                                                   \
    /**/                                                                                    \
    sensor.msLastRead = nSysTime;                                                           \
    }

int CALLEDBY(iTaskMain) ReadSonic_Main(SONICSENSOR& sensor, BOOL fRequireFinite)
// Because they take so long to read, and their calling thread blocks while we're doing
// so, we treat sonic reading as different than other reads.
//
// REVIEW: it doesn't have to be that way; that is, we can incrementalize this and
// do distance events based on the sonic. We should do that.
//
// We read until we get two readings in a row with basically the same value.
// Note that this function can a very long time to complete.
// Return cmSonicNil if there's no reading available; that will never happen
// if fAllowInfinite is false (we'll spin until that doesn't happen).
    {
    LockBlackboard();
    RawReadSonic(sensor); int cmCur = sensor.cm;
    int cmPrev;
    for (int iRetry=0; iRetry < 10 || (fRequireFinite && (cmSonicRawNil==cmCur || cmSonicRawNil==cmPrev)); iRetry++)
        {
        cmPrev = cmCur;
        WaitForNewSonicReading(sensor);
        RawReadSonic(sensor);
        cmCur  = sensor.cm;
        if (fRequireFinite && (cmSonicRawNil==cmCur || cmSonicRawNil==cmPrev))
            continue;
        if (Abs(cmCur - cmPrev) <= 1)
            break;
        }
    int result = cmCur==cmSonicRawNil ? cmSonicNil : cmCur;
    ReleaseBlackboard();
    return result;
    }

// REVIEW: for the sonic sensor, these don't currently have any significance
#define StartDetectingSonicSensor(sensor)     StartDetectingSensor_(sensor)
#define StopDetectingSonicSensor(sensor)      StopDetectingSensor_(sensor)
#define StartReadingSonicSensor(sensor)       StartReadingSensor_(sensor)
#define StopReadingSonicSensor(sensor)        StopReadingSensor_(sensor)



//------------------------------------------------------------------------------------
// Touch sensor
//------------------------------------------------------------------------------------

void ReadTouchSensor(TOUCHSENSOR& sensor, READ_SENSORS_CONTEXT& context)
    {
    if (FDoSensorRead(sensor,context))
        {
        int value;
        if (FMux(sensor))
            {
            SMUXreadAnalog(SID_MUX(sensor), value);
            }
        else
            {
            value = SensorRaw[SID_DIRECT(sensor)];
            }
        sensor.fValue = (value < 500);
        if (FDoSensorDetection(sensor) && !sensor.fTriggered)
            {
            sensor.fTriggered = sensor.fValue;
            }
        }
    }

#define StartReadingTouchSensor(sensor)    StartReadingSensor_(sensor)
#define StopReadingTouchSensor(sensor)     StopReadingSensor_(sensor)

#define TriggerTouchSensor(sensor,context) TriggerSensor_(sensor,context)
