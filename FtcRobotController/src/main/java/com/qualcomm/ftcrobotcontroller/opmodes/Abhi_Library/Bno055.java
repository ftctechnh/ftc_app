package com.qualcomm.ftcrobotcontroller.opmodes.Abhi_Library;


import android.util.Log;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;

public class Bno055 {
    static final String logId   = "BNO:";       // Tag identifier in FtcRobotController.LogCat

/*
    Manual can be found at
        https://ae-bst.resource.bosch.com/media/products/dokumente/bno055/BST_BNO055_DS000_13.pdf
 */

    static final int
/*
    BNO MANUAL (p90) 4.6: The default I²C address of the BNO055 device is 0101001b (0x29).
    The alternative address 0101000b (0x28), in I2C mode the input pin COM3 can be used
    to select between the primary and alternative I2C address as shown in Table 4-7.
    ADAFRUIT CIRCUIT DIAGRAM: Shows that COM3 is connected to ground by R3,
    thus the alternative address 0x28 is used.
 */
            ADDRESS             = 0x28,         // I2C device address
/*
    BNO MANUAL (p54) 4.3.x:
        4.3.1 CHIP_ID 0x00, Chip identification code, read-only fixed value 0xA0
        4.3.2 ACC_ID 0x01, Chip ID of the Accelerometer device, read-only fixed value 0xFB
        4.3.3 MAG_ID 0x02, Chip ID of the Magnetometer device, read-only fixed value 0x32
        4.3.4 GYR_ID 0x03, Chip ID of the Gyroscope device, read-only fixed value 0x0F
 */
            CHIP_ID_ADDR        = 0x00,         // Identify the sensor type
            CHIP_ID                 = 0xA0,         // bno055 identification value
            ACC_ID                  = 0xFB,         // Accelerometer identification value
            MAG_ID                  = 0x32,         // Magnetometer identification value
            GYR_ID                  = 0x0F,         // Gyroscope identification value
/*
    BNO MANUAL (p55): 4.3.8 PAGE ID 0x07
        Number of currently selected page 0x00 or 0x01
 */
            PAGE_ID             = 0x07,         // Page selection, 0 or 1
/*
    BNO MANUAL (p70) 4.3.61 OPR_MODE 0x3D
        Current selected operation mode. See section 3.3 for details
    BNO MANUAL (p21 & p23) 3.3.3.5 NDOF: xxxx1100b
        This is a fusion mode with 9 degrees of freedom where the fused
        absolute orientation data is calculated from accelerometer, gyroscope and
        the magnetometer. The advantages of combining all three sensors are
        a fast calculation, resulting in high output data rate, and high robustness
 */
            OPR_MODE            = 0x3D,         // Select the involved sub-sensors
            CONFIGMODE              = 0x00,         // [OPR_MODE]: xxxx0000b
            NDOF                    = 0x0C,         // Nine Degree of Freedom with fusion
/*
    BNO MANUAL (p70) 4.3.62 PWR_MODE 0x3E
        Current selected power mode.  See section 0 for details
    BNO MANUAL (p19) 3.2.1 Normal Mode [PWR_MODE]: xxxxxx00b
        In normal mode all sensors required for the selected
        operating mode (see section 3.3) are alwaccYs switched ON.
 */
            PWR_MODE            = 0x3E,         // Select the consumed power level
            NORMAL_MODE             = 0x00,         //  Full power
/*
    BNO MANUAL (p56) 4.3.9 ACC_DATA_X_LSB 0x08
    BNO MANUAL (p57) 4.3.15 MAG_DATA_X_LSB 0x0E
 */
            ACC_DATA_X_LSB      = 0x08,
            MAG_DATA_X_LSB      = 0x0E,

/*
    BNO MANUAL (p60) 4.3.27 EUL_DATA_X_LSB 0x1A
        Lower byte of heading data, read only
        The output units can be selected using the UNIT_SEL register and data output type can
        be changed by updating the Operation Mode in the OPR_MODE register, see section 3.3
 */
            EUL_DATA_X_LSB      = 0x1A,         // Start of Euler 6 byte XYZ Data

/*
    BNO MANUAL (p62) 4.3.33 QUA_DATA_W_LSB 0x20
        Lower byte of w accXis Quaternion data, read only
        The output units can be selected using the UNIT_SEL register and data output type can
        be changed by updating the Operation Mode in the OPR_MODE register, see section 3.3

    BNO MANUAL (p64) 4.3.41 LIA_DATA_X_LSB 0x28
    BNO MANUAL (p65) 4.3.47 GRV_DATA_X_LSB 0x2E
 */
            QUA_DATA_X_LSB      = 0x1A,         // Start of Quaternion 8 byte WXYZ Data

/*
    bno manual (P67) 4.3.53 TEMP 0x34
        The output units can be selected using the UNIT_SEL register and data output source
        can be selected by updating the TEMP_SOURCE register, see section 3.6.5.8
 */
            SENSOR_TEMP         = 0x34,         // Temperature of selected sensor
/*
    bno manual (P67) 4.3.63 4.3.54 CALIB_STAT 0x35
 */
            CALIB_STAT          = 0x35,         // Calibration levels
                                                    // 0    = not calibrated
                                                    // 1, 2 = calibration in progress
                                                    // 3    = fully calibrated
            SYS_CALIB               = 6,            // System calibration in bits 6 & 7
            GYR_CALIB               = 4,            // Gyro calibration in bits 4 & 5
            ACC_CALIB               = 2,            // Acceleration calibration in bits 2 & 3
            MAG_CALIB               = 0,            // Magnetometer calibration in bits 0 & 1
/*
    bno manual (P68) 4.3.58 SYS_STATUS 0x39
        0 System idle,
        1 System Error,
        2 Initializing peripherals
        3 System Initialization
        4 Executing selftest,
        5 Sensor fusion algorithm running,
        6 System running without fusion algorithm
 */
            SYS_STATUS          = 0x39,         // System status
/*
    bno manual (P69) 4.3.59 SYS_ERR 0x3A
        0 No error
        1 Peripheral initialization error
        2 System initialization error
        3 Self test result failed
        4 Register map value out of range
        5 Register map address out of range
        6 Register map write error
        7 BNO low power mode not available for selected operation mode
        8 Accelerometer power mode not available
        9 Fusion algorithm configuration error
        A Sensor configuration error
 */
            SYS_ERR             = 0x3A,         // System error
/*
    bno manual (P69) 4.3.60 UNIT_SEL 0x3B
 */
            UNIT_SEL            = 0x3B,         // Unit selections
            ORI_ANDROID             = 1<<7,         // 0 = Windows orientation
            TEMP_FAHRENHEIT         = 1<<4,         // 0 = Celcius
            EUL_RADIAN              = 1<<2,         // 0 = Degree
            GYR_RPS                 = 1<<1,         // 0 = DPS
            ACC_MG                  = 1,            // 0 = m/s2

/*
    bno manual (P70) 4.3.63 SYS_TRIGGER 0x3F
 */
            SYS_TRIGGER         = 0x3F,         // System trigger and reset
            RST_SYS                 = 1<<5,         // Reset system
/*
    bno manual (P70) 4.3.64 TEMP_SOURCE 0x40
 */
            TEMP_SOURCE         = 0x40,         // Temperature sensor selection
            TEMP_ACC                = 0x01,         // Accelerometer
            TEMP_GYR                = 0x00,         // Gyroscope
/*
                                                // These are read/write registers
    bno manual (P71) 4.3.67 ACC_OFFSET_X_LSB 0x55
    bno manual (P73) 4.3.73 MAG_OFFSET_X_LSB 0x5B
    bno manual (P74) 4.3.79 GYR_OFFSET_X_LSB 0x61
    bno manual (P76) 4.3.85 ACC_RADIUS_LSB 0x67
    bno manual (P76) 4.3.87 MAG_RADIUS_LSB 0x69
 */
            ACC_OFFSET_X_LSB    = 0x55,         // Accelerometer Offset
            MAG_OFFSET_X_LSB    = 0x5B,         // Magnetometer Offset
            GYR_OFFSET_X_LSB    = 0x61,         // Gyroscope Offset
            ACC_RADIUS_LSB      = 0x67,         // Accelerometer Radius
            MAG_RADIUS_LSB      = 0x69;         // Magnetometer Radius

//------------------------------ Public Enumarations -----------------------------------

    static public enum BnoPolling{
        SENSOR  (CHIP_ID_ADDR,      26),
        FUSION  (EUL_DATA_X_LSB,    26),
        EULER   (EUL_DATA_X_LSB,    6),
        QUATER  (QUA_DATA_X_LSB,    8),
        TEMP    (SENSOR_TEMP,       7),
        CALIB   (ACC_OFFSET_X_LSB,  22);

        private int regNumber,regCount;
        private BnoPolling(int regNumber, int regCount) {
            this.regNumber  = regNumber;
            this.regCount   = regCount;
        }
    }

//------------------------------ Private Variables -------------------------------------

    private Wire        gs;                     // Gyro Sensor
    private ArrayList<ScheduleItem> schedule;

    private InitStates  initState;              // Multistep initialization
    private boolean     stateIsStarting;        // Local state during initialization
    private long        startTime;              // Timeout control for init_loop
    private long        delayStart;             // Delay starting time
    private int         readCount;              // Count of completed read operations
    private int         writeCount;             // Count of completed write operations

//------------------------------- Results from Sensors, in Register Number Order ------

//  SENSOR
    private int         chipId, accId, magId, gyrId;
    private int         swRev, blRev;           // Software and boot loader revisions
    private int         pageNr;                 // This register value == 0 during run
    private int         accX,accY,accZ;         // Acceleration (ACC), i16
    private int         magX,magY,magZ;         // Magnetometer (MAG), i16
    private int         gyrX,gyrY,gyrZ;         // Gyroscope (GYR), i16, angular velocity
    private Box<Long>   sensorMicros;           // Time Stamp for SENSOR data
    
//  FUSION
    private int         eulX,eulY,eulZ;         // Euler data (EUL), i16
    private int         quaW,quaX,quaY,quaZ;    // Quaternion data (QUA), i16
    private int         liaX,liaY,liaZ;         // Linear Acceleration (LIA), i16
    private int         grvX,grvY,grvZ;         // Gravity (GRV), i16
    private Box<Long>   fusionMicros;           // Timestamp for FUSION data
    private Box<Long>   eulerMicros;            // Timestamp for EULER data
    private Box<Long>   quaterMicros;           // Timestamp for QUATER data

//  TEMP
    private int         temp;                   // Temperature of selected sensor
    private int         calibStat;              // Calibration status of S, G, A, M
    private int         stResult;               // Self Test Result
    private int         intStat;                // Interrupt Status
    private int         sysClkStat;             // System Clock Status
    private int         sysSta;                 // System Status
    private int         sysErr;                 // System Error
    private Box<Long>   tempMicros;             // Timestamp for TEMP

// CALIB
    private int         accOsX,accOsY,accOsZ;   // Acceleration offsets, i16
    private int         magOsX,magOsY,magOsZ;   // Magnetometer offsets, i16
    private int         gyrOsX,gyrOsY,gyrOsZ;   // GYroscope offsets, i16
    private int         accRadius,magRadius;    // Acceleration and Magmetometer radius
    private Box<Long>   calibMicros;            // Timestamp for CALIB

//------------------------------------ Class Initialization -----------------------------    
    
    public Bno055(HardwareMap hardwareMap, String deviceName) {
        gs              = new Wire (hardwareMap,deviceName,2 * ADDRESS);
        schedule        = new ArrayList<ScheduleItem>();
        sensorMicros    = new Box<Long>(0L);
        fusionMicros    = new Box<Long>(0L);
        eulerMicros     = new Box<Long>(0L);
        quaterMicros    = new Box<Long>(0L);
        tempMicros      = new Box<Long>(0L);
        calibMicros     = new Box<Long>(0L);
        readCount       = 0;
        writeCount      = 0;
    }

    public void init() {
        setNextState(InitStates.START);
        startTime               = System.currentTimeMillis();   // TO control for init_loop
    }

    private enum InitStates {
        START,
        SET_CONFIGURATION_MODE,
        START_RESET, WAIT_RESET, DELAY_RESET,
        CONFIGURE_PAGE0, INITIAL_POLL, UPDATE_DATA,
        INIT_DONE,INIT_FAILED;
    }

    public void init_loop() {
        long dT = System.currentTimeMillis() - startTime;

        switch (initState) {
            case START:
                if (isIdReceived()) setNextState(InitStates.SET_CONFIGURATION_MODE);
                break;
            case SET_CONFIGURATION_MODE:
                gs.write(OPR_MODE, CONFIGMODE);             // Enable configuration changes
                setNextState(InitStates.START_RESET);
                break;
            case START_RESET:
                                                            // Keep the reply buffer empty
                while (gs.responseCount() > 0) gs.getResponse();
                if (delayIsDone(19)) {                      // After changing OP Mode, wait 19 ms
                    gs.write(SYS_TRIGGER, RST_SYS);         // Turn ON System Reset
                    setNextState(InitStates.WAIT_RESET);    // During reset the ID is not available
                }
                break;
            case WAIT_RESET:
                    if (isIdReceived()) {                   // This could take up to 650 ms
                    gs.write(SYS_TRIGGER,0);                // Turn OFF System Reset
                    setNextState(InitStates.CONFIGURE_PAGE0);
                }
                break;
            case CONFIGURE_PAGE0:
                gs.write(PWR_MODE,NORMAL_MODE);             // Set full power for subsystems
                gs.write(PAGE_ID,0);                        // Select page 0
                gs.write(UNIT_SEL,ORI_ANDROID);             // Switch from Windows to Android
                gs.write(OPR_MODE, NDOF);                   // Start Fusion for 9 DOF
                setNextState(InitStates.INITIAL_POLL);
                break;
            case INITIAL_POLL:
                if (delayIsDone(7)) {                       // After changing OP Mode, wait 7 ms
                    poll(BnoPolling.SENSOR);                // Poll all data sets
                    poll(BnoPolling.FUSION);
                    poll(BnoPolling.TEMP);
                    poll(BnoPolling.CALIB);

                    setNextState(InitStates.UPDATE_DATA);
                }
                break;
            case UPDATE_DATA:
                if (delayIsDone(100)) {                     // Read the poll responses after 0.1 s
                    loop();
                    setNextState(InitStates.INIT_DONE);
                }
        }
        if (dT > 1000) {                                    // Time out after 1000 ms
            if (initState == InitStates.INIT_DONE) {
                        // TO is not applicable after succesful init
            } else if (initState == InitStates.INIT_FAILED) {
                        // TO is already registered
            } else {    // TO is detected
                Log.i(logId,"Initialization timeout at state " + initState.toString());
                initState   = InitStates.INIT_FAILED;
            }
        }
    }

    private void setNextState(InitStates nextState) {
        initState       = nextState;
        stateIsStarting = true;
    }

    private boolean delayIsDone(int delay) {
        if (stateIsStarting) {
            stateIsStarting = false;
            delayStart  = System.currentTimeMillis();
        }
        return ((System.currentTimeMillis() - delayStart) >= delay);
    }

    public boolean isInitActive() {
        if (initState   == InitStates.INIT_DONE)    return false;
        if (initState   == InitStates.INIT_FAILED)  return false;
        return true;
    }

    public boolean isInitDone() { return (initState == InitStates.INIT_DONE); }

    private boolean isIdReceived() {
        if (stateIsStarting) {
            stateIsStarting = false;
            gs.requestFrom(CHIP_ID_ADDR, 4);
        } else {
            while (gs.responseCount() > 0) {
                gs.getResponse();
                if (isIdOk()) return true;
                stateIsStarting = true;         // Request retransmission of ID
            }
        }
        return false;
    }

    private boolean isIdOk() {
        if (!gs.isRead())                           return false;
        if (gs.registerNumber() != CHIP_ID_ADDR)    return false;
        if (gs.available()      != 4)               return false;
        if (gs.read()           != CHIP_ID)         return false;
        if (gs.read()           != ACC_ID)          return false;
        if (gs.read()           != MAG_ID)          return false;
        if (gs.read()           != GYR_ID)          return false;
        // All four identifications received correctly
        return true;
    }

    public ScheduleItem startSchedule(BnoPolling request, int interval) {
        ScheduleItem item   = new ScheduleItem();
        if (interval < 15)      interval    = 15;       // Coerse interval into 15 ms to 10 sec
        if (interval > 10000)   interval    = 10000;
        item.request        = request;
        item.interval       = interval;
        item.prevTimeStamp  = -1;
        item.prevTime       = System.currentTimeMillis();
                                                        // This functionality requires the box
        switch (request) {
            case SENSOR:    item.timeStamp  = sensorMicros;     break;
            case FUSION:    item.timeStamp  = fusionMicros;     break;
            case EULER:     item.timeStamp  = eulerMicros;      break;
            case QUATER:    item.timeStamp  = quaterMicros;     break;
            case TEMP:      item.timeStamp  = tempMicros;       break;
            case CALIB:     item.timeStamp  = calibMicros;      break;
        }
        schedule.add(item);
        poll(request);                                  // Immediate polling
        return item;
    }

    public class ScheduleItem {
        BnoPolling  request;
        int         interval;
        Box<Long>   timeStamp;
        long        prevTimeStamp;
        long        prevTime;

        public boolean isChanged() {
            if (prevTimeStamp == timeStamp.value) return false;
            prevTimeStamp  = timeStamp.value;
            return true;
        }
    }

    public void loop() {
        for (ScheduleItem item : schedule) {
            if ((System.currentTimeMillis() - item.prevTime) >= item.interval) {
                item.prevTime   = System.currentTimeMillis();
                poll(item.request);
            }
        }

        while (gs.responseCount() > 0) {
            gs.getResponse();
            if (gs.isRead()) {
                readCount++;
                int regNumber = gs.registerNumber();
                if (regNumber == BnoPolling.FUSION.regNumber) {
                    int regCount = gs.available();
                    if (regCount == BnoPolling.FUSION.regCount) {
                        fusionMicros.value = gs.micros();
                        eulX = u16ToI16(gs.readLH());
                        eulY = u16ToI16(gs.readLH());
                        eulZ = u16ToI16(gs.readLH());
                        quaW = u16ToI16(gs.readLH());
                        quaX = u16ToI16(gs.readLH());
                        quaY = u16ToI16(gs.readLH());
                        quaZ = u16ToI16(gs.readLH());
                        liaX = u16ToI16(gs.readLH());
                        liaY = u16ToI16(gs.readLH());
                        liaZ = u16ToI16(gs.readLH());
                        grvX = u16ToI16(gs.readLH());
                        grvY = u16ToI16(gs.readLH());
                        grvZ = u16ToI16(gs.readLH());
                    } else if (regCount == BnoPolling.EULER.regCount) {
                        eulerMicros.value = gs.micros();
                        eulX = u16ToI16(gs.readLH());
                        eulY = u16ToI16(gs.readLH());
                        eulZ = u16ToI16(gs.readLH());
                    }
                } else if (regNumber == BnoPolling.QUATER.regNumber) {
                    if (gs.available() == BnoPolling.QUATER.regCount) {
                        quaterMicros.value = gs.micros();
                        quaW = u16ToI16(gs.readLH());
                        quaX = u16ToI16(gs.readLH());
                        quaY = u16ToI16(gs.readLH());
                        quaZ = u16ToI16(gs.readLH());
                    }
                } else if (regNumber == BnoPolling.SENSOR.regNumber) {
                    if (gs.available() == BnoPolling.SENSOR.regCount) {
                        sensorMicros.value = gs.micros();
                        chipId  = gs.read();
                        accId   = gs.read();
                        magId   = gs.read();
                        gyrId   = gs.read();
                        swRev   = gs.readLH();
                        blRev   = gs.read();
                        pageNr  = gs.read();
                        accX    = u16ToI16(gs.readLH());
                        accY    = u16ToI16(gs.readLH());
                        accZ    = u16ToI16(gs.readLH());
                        magX    = u16ToI16(gs.readLH());
                        magY    = u16ToI16(gs.readLH());
                        magZ    = u16ToI16(gs.readLH());
                        gyrX    = u16ToI16(gs.readLH());
                        gyrY    = u16ToI16(gs.readLH());
                        gyrZ    = u16ToI16(gs.readLH());
                    }
                } else if (regNumber == BnoPolling.CALIB.regNumber) {
                    if (gs.available() == BnoPolling.CALIB.regCount) {
                        calibMicros.value = gs.micros();
                        accOsX      = u16ToI16(gs.readLH());
                        accOsY      = u16ToI16(gs.readLH());
                        accOsZ      = u16ToI16(gs.readLH());
                        magOsX      = u16ToI16(gs.readLH());
                        magOsY      = u16ToI16(gs.readLH());
                        magOsZ      = u16ToI16(gs.readLH());
                        gyrOsX      = u16ToI16(gs.readLH());
                        gyrOsY      = u16ToI16(gs.readLH());
                        gyrOsZ      = u16ToI16(gs.readLH());
                        accRadius   = u16ToI16(gs.readLH());
                        magRadius   = u16ToI16(gs.readLH());
                    }
                } else if (regNumber == BnoPolling.TEMP.regNumber) {
                    if (gs.available() == BnoPolling.TEMP.regCount) {
                        tempMicros.value = gs.micros();
                        temp        = gs.read();
                        calibStat   = gs.read();
                        stResult    = gs.read();
                        intStat     = gs.read();
                        sysClkStat  = gs.read();
                        sysSta      = gs.read();
                        sysErr      = gs.read();
                    }
                }
            } else if (gs.isWrite()){
                writeCount++;
            }
        }
    }

    private int u16ToI16(int x) {return (x<0x8000) ? x : x - 0x10000;}

//--------------------------------- Public Methods -----------------------------------

    private void poll(BnoPolling request) {
        gs.requestFrom(request.regNumber, request.regCount);
    }

//  SENSOR

    public int  chipId()                    {return chipId;}
    public int  accId()                     {return accId;}
    public int  magId()                     {return magId;}
    public int  gyrId()                     {return gyrId;}
    public int  swRev()                     {return swRev;}
    public int  blRev()                     {return blRev;}

    public int  accX()                      {return accX;}
    public int  accY()                      {return accY;}
    public int  accZ()                      {return accZ;}

    public int  magX()                      {return magX;}
    public int  magY()                      {return magY;}
    public int  magZ()                      {return magZ;}

    public int  gyrX()                      {return gyrX;}
    public int  gyrY()                      {return gyrY;}
    public int  gyrZ()                      {return gyrZ;}

    public long sensorMicros()              {return sensorMicros.value;}

//  FUSION

    public int  eulerX()                    {return eulX;}
    public int  eulerY()                    {return eulY;}
    public int  eulerZ()                    {return eulZ;}

    public int  quaternionW()               {return quaW;}
    public int  quaternionX()               {return quaX;}
    public int  quaternionY()               {return quaY;}
    public int  quaternionZ()               {return quaZ;}

    public int  linearAccelerationX()       {return liaX;}
    public int  linearAccelerationY()       {return liaY;}
    public int  linearAccelerationZ()       {return liaZ;}

    public int  gravityX()                  {return grvX;}
    public int  gravityY()                  {return grvY;}
    public int  gravityZ()                  {return grvZ;}
    public long fusionMicros()              {return fusionMicros.value;}
    public long eulerMicros()               {return eulerMicros.value;}
    public long quaterMicros()              {return quaterMicros.value;}

//  TEMP

    public int  temperature()               {return temp;}
    public int  sysCalibrationLevel()       {return ((calibStat >> SYS_CALIB) & 0x03);}
    public int  gyrCalibrationLevel()       {return ((calibStat >> GYR_CALIB) & 0x03);}
    public int  accCalibrationLevel()       {return ((calibStat >> ACC_CALIB) & 0x03);}
    public int  magCalibrationLevel()       {return ((calibStat >> MAG_CALIB) & 0x03);}
    public long selfTestResult()            {return stResult;}
    public long interruptStatus()           {return intStat;}
    public long sysClockStatus()            {return sysClkStat;}
    public long sysStatus()                 {return sysSta;}
    public long sysError()                  {return sysErr;}
    public long tempMicros()                {return tempMicros.value;}

// CALIB

    public int  accelerationOffsetX()       {return accOsX;}
    public int  accelerationOffsetY()       {return accOsY;}
    public int  accelerationOffsetZ()       {return accOsZ;}

    public int  magnetometerOffsetX()       {return magOsX;}
    public int  magnetometerOffsetY()       {return magOsY;}
    public int  magnetometerOffsetZ()       {return magOsZ;}

    public int  gyroscopeOffsetX()          {return gyrOsX;}
    public int  gyroscopeOffsetY()          {return gyrOsY;}
    public int  gyroscopeOffsetZ()          {return gyrOsZ;}

    public int  accRadius()                 {return accRadius;}
    public int  magRadius()                 {return magRadius;}

    public long calibMicros()               {return calibMicros.value;}

    public void setAccelerationOffset   (int offsetX, int offsetY, int offsetZ) {
        gs.writeLH(ACC_OFFSET_X_LSB + 0,offsetX);
        gs.writeLH(ACC_OFFSET_X_LSB + 2,offsetY);
        gs.writeLH(ACC_OFFSET_X_LSB + 4,offsetZ);
    }
    public void setMagnetometerOffset   (int offsetX, int offsetY, int offsetZ) {
        gs.writeLH(MAG_OFFSET_X_LSB + 0,offsetX);
        gs.writeLH(MAG_OFFSET_X_LSB + 2,offsetY);
        gs.writeLH(MAG_OFFSET_X_LSB + 4,offsetZ);
    }
    public void setGyroscopeOffset      (int offsetX, int offsetY, int offsetZ) {
        gs.writeLH(GYR_OFFSET_X_LSB + 0,offsetX);
        gs.writeLH(GYR_OFFSET_X_LSB + 2,offsetY);
        gs.writeLH(GYR_OFFSET_X_LSB + 4,offsetZ);
    }

    public void setAccelerometerRadius  (int radius) {
        gs.writeLH(ACC_RADIUS_LSB,radius);
    }

    public void setMagnetometerRadius(int radius) {
        gs.writeLH(MAG_RADIUS_LSB,radius);
    }

    public int requestCount()               {return gs.requestCount();}
    public int responseCount()              {return gs.responseCount();}
    public int readCount()                  {return readCount;}
    public int writeCount()                 {return writeCount;}

    public void stop() {
        gs.close();
    }
}
