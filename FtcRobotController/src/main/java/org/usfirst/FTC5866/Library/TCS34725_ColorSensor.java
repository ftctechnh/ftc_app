package org.usfirst.FTC5866.library;

/**
 * Created by Olavi Kamppari on 9/25/2015.
 */
import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.concurrent.locks.Lock;

public class TCS34725_ColorSensor implements I2cController.I2cPortReadyCallback {

// --------------------------------- CONSTANTS -------------------------------------------------

    String logId            = "CS:";        // Tag identifier in FtcRobotController.LogCat

    static final byte 
        ADDRESS             = 0x29,         // I2C device address
                                            // ------ CONFIGURATION REGISTERS ------
                                            // In TCS3472x, the actual register address is
                                            // in range from 0x80 - 0x9B.  The bit 7 is turned on
                                            // by the -128 operation,  For example 0x00-128 = 0x80
        ENABLE              = 0x00 - 128,   // Enable Register
        ENABLE_AIEN             = 0x10,         // All Interrupts Enabled
        ENABLE_WEN              = 0x08,         // Wait enable between readings
        ENABLE_ACEN             = 0x02,         // All channels Enabled (
        ENABLE_PON              = 0x01,         // Power ON
        ATIME               = 0x01 - 128,   // ADC Integration Time= 256 - time/2.4 ms
                                                // Time range= 2.4 - 614.4 ms
                                                // Register 0x02 is undefined
        WTIME               = 0x03 - 128,   // Wait time between readings depends on Reg 0x0D bit1
                                                // WLONG= 0, Wait= 256 - time/2.4 ms
                                                // WLONG= 1, Wait= 256 - time/28.8 ms
                                                // Low range= 2.4 - 614.4 ms
                                                // High range= 28.8 - 7,372.8 ms
        AILTL               = 0x04 - 128,   // Clear channel Alarm Interrupt Low Limit Low byte
        AILTH               = 0x05 - 128,   // Low Limit High Byte
        AIHTL               = 0x06 - 128,   // High Limit Low Byte
        AIHTH               = 0x07 - 128,   // High Limit High Byte
                                                // Register 0x08 - 0x0B are undefined
        PERS                = 0x0C - 128,   // Persistence register. 7:4= PPERS, 3:0= APERS
                                                // APERS values
                                                // 0= every cycle interrupt
                                                // 1-3= Clear channel on every N violations
                                                // 4-15= Clear channel on every 5*(N-3) violations
        CONFIG              = 0x0D - 128,   // Configure register 7:2= 0, 1:1= WLONG, 0:0= 0
        CONFIG_WLONG        = 0x02,             // Wait time in 12x2.4 (28.8) ms increments
        CONTROL             = 0x0F - 128,   // Control register 7:2= 0 1:0= Amplifier Gain
        CONTROL_AGAIN1      = 0x00,             // Amplifier Gain= 1
        CONTROL_AGAIN4      = 0x01,             // Amplifier Gain= 4
        CONTROL_AGAIN16     = 0x02,             // Amplifier Gain= 16
        CONTROL_AGAIN60     = 0x03,             // Amplifier Gain= 60
                                            // Registers 0x10 - 0x11 are undefined

                                            // ------ READ ONLY REGISTERS ------
        DATA_START          = 0x14 - 128,   // Start of first color byte
        DATA_LENGTH         = 0x08,             // 2 bytes per colors: Clear Red, Green, Blue

        ID                  = 0x12 - 128,   // 0x44= TCS34721/25, 0x4D= TCS34723/27
        STATUS              = 0x13 - 128,   // Status 7:5= 0, 4:4= AINT, 3:1= 0, 0:0= AVALID
        STATUS_AINT         = 0x10,             // Clear channel interrupt
        STATUS_AVALID       = 0x01,             // All channels valid (ADC integration complete)
                                            // RGBC channel values
        CDATAL              = 0x14 - 128,   // Clear Data Low Byte
        CDATAH              = 0x15 - 128,   // Clear Data High Byte
        RDATAL              = 0x16 - 128,   // Red Data Low Byte
        RDATAH              = 0x17 - 128,   // Red Data High Byte
        GDATAL              = 0x18 - 128,   // Green Data Low Byte
        GDATAH              = 0x19 - 128,   // Green Data High Byte
        BDATAL              = 0x1A - 128,   // Blue Data Low Byte
        BDATAH              = 0x1B - 128;   // Blue Data High Byte

                                        // Cache buffer constant values
    static final byte
        READ_MODE           = 0x00 - 128,   // MSB = 1
        WRITE_MODE          = 0x00;         // MSB = 0

                                        // Cache buffer index values
    static final int
        CACHE_MODE          = 0,        // MSB = 1 when read mode is active
        DEV_ADDR            = 1,        // Device address
        REG_NUMBER          = 2,        // Register address
        REG_COUNT           = 3,        // Register count
        DATA_OFFSET         = 4,        // First byte of transferred data
        ACTION_FLAG         = 31;       // 0 = idle, -1 = transfer is active

// --------------------------------- CLASS VARIABLES ----------------------------------------------
    private ArrayQueue<I2cTransfer> transferQueue;      // Buffer all register writes and
    private I2cDevice               csDev;              // Color Sensor I2C Device Object
    private byte                    csDevAddr;          // Color Sensor address = 2*0x29
    private byte[]                  rCache;             // Read Cache
    private byte[]                  wCache;             // Write Cache
    private Lock                    rLock;              // Lock for Read Cache
    private Lock                    wLock;              // Lock for Write Cache & request queue

                                                        // Read results from the sensor
    private int                     clear       = 0;    // Color clear (a.k.a. alpha)
    private int                     red         = 0;    // Color red
    private int                     green       = 0;    // Color green
    private int                     blue        = 0;    // Color blue
    private int                     aTimeValue  = 0;    // Received ATIME register value
    private int                     controlValue= 0;    // Received CONTROL register value
    private int                     idValue     = 0;    // Received ID register value

// --------------------------------- CLASS INIT AND CLOSE ----------------------------------------

    public TCS34725_ColorSensor(HardwareMap hardwareMap, String deviceName) {
        transferQueue       = new ArrayQueue<I2cTransfer>();
        csDev               = hardwareMap.i2cDevice.get(deviceName);
        csDevAddr           = 2*ADDRESS;

        rCache              = csDev.getI2cReadCache();
        wCache              = csDev.getI2cWriteCache();
        rLock               = csDev.getI2cReadCacheLock();
        wLock               = csDev.getI2cWriteCacheLock();

//Log.i(logId, "Start --------------------------------------------------------------------------");
        addWriteRequest(ENABLE, (byte) (ENABLE_PON | ENABLE_ACEN)); // Enable power & all colors
        addReadRequest(ID, (byte) 1);       // Get device ID
        executeCommands();
                          // Start the transfer process
        csDev.registerForI2cPortReadyCallback(this);
        setGain(16);                        // Set default amplification gain
        setIntegrationTime(50);             // Set default ADC integration time
    }

    public  void close() {
        transferQueue.close();
        csDev.deregisterForPortReadyCallback();
        csDev.close();
    }

    public void portIsReady(int port) {
        try {
            rLock.lock();
//Log.i(logId, String.format("Polling: mode%d, devAddr=0x%02X, regNr=0x%02X, regCnt=%d",
//                    rCache[0], rCache[1], rCache[2], rCache[3]));
            if (
                rCache[0] == wCache[0] &&
                rCache[1] == wCache[1] &&
                rCache[2] == wCache[2] &&
                rCache[3] == wCache[3]) {

                rCache[DEV_ADDR] = 0;                       // Mark the buffer used
                storeReceivedData();                        // Store read/write data
                executeCommands();                          // Start next transmission
            } else {
//Log.i(logId, "Keep polling");
                csDev.readI2cCacheFromController();         // Keep polling active
            }
        } finally {
            rLock.unlock();
        }
    }

// --------------------------------- COMMAND MANAGEMENT -------------------------------------------

    private void readCommand(byte regNumber, byte regCount) {
        try {
            wLock.lock();
            wCache[CACHE_MODE]  = READ_MODE;
            wCache[DEV_ADDR]    = csDevAddr;
            wCache[REG_NUMBER]  = regNumber;
            wCache[REG_COUNT]   = regCount;
            wCache[ACTION_FLAG] = -1;
        } finally {
            wLock.unlock();
        }
        csDev.writeI2cCacheToController();
//Log.i(logId, String.format("readCommand regNumber 0x%02X//%d", regNumber, regCount));
    }
    private void writeCommand(byte regNumber, byte regCount, long regValue, boolean isLowFirst) {
        try {
            wLock.lock();
            wCache[CACHE_MODE]  = WRITE_MODE;
            wCache[DEV_ADDR]    = csDevAddr;
            wCache[REG_NUMBER]  = regNumber;
            wCache[REG_COUNT]   = regCount;
            if (regCount == 1) {
                wCache[DATA_OFFSET] = (byte) (regValue & 0xFF);
            } else if (isLowFirst) {
                for (int i = 0; i < regCount; i++) {
                    wCache[DATA_OFFSET + i] = (byte) (regValue & 0xFF);
                    regValue >>= 8;
                }
            } else {
                for (int i = regCount-1; i >= 0; i--) {
                    wCache[DATA_OFFSET + i] = (byte) (regValue & 0xFF);
                    regValue >>= 8;
                }
            }
            wCache[ACTION_FLAG] = -1;
        } finally {
            wLock.unlock();
        }
        csDev.writeI2cCacheToController();
//Log.i(logId, String.format("writeCommand regNumber 0x%02X//%d <- 0x%02X", regNumber, regCount, regValue));
    }

    private void executeCommands() {
        boolean isRead      = false;
        boolean isWrite     = false;
        boolean isLowFirst  = false;
        byte    regNumber   = 0;
        byte    regCount    = 0;
        long    regValue    = 0;

        try {
            wLock.lock();
            if (!transferQueue.isEmpty()) {
                I2cTransfer element = transferQueue.remove();
                isWrite     = element.mode == WRITE_MODE;
                isRead      = element.mode == READ_MODE;
                regNumber   = element.regNumber;
                regCount    = element.regCount;
                regValue    = element.regValue;
                isLowFirst  = element.isLowFirst;
                element     = null;                     // Support garbage collection
            }
        } finally {
            wLock.unlock();
        }
        if (isWrite) {
            writeCommand(regNumber, regCount, regValue, isLowFirst);
//Log.i(logId, String.format(
//                    "Execute Write:   mode%d, devAddr=0x%02X, regNr=0x%02X, regCnt=%d. value=0x%02X",
//                    wCache[0], wCache[1], wCache[2], wCache[3],wCache[4]));
        } else if (isRead) {
            readCommand(regNumber, regCount);
//Log.i(logId, String.format("Execute Read:    mode%d, devAddr=0x%02X, regNr=0x%02X, regCnt=%d",
//                    wCache[0], wCache[1], wCache[2], wCache[3]));
        } else {
            readCommand(DATA_START,DATA_LENGTH);
//Log.i(logId, String.format("Execute Default: mode%d, devAddr=0x%02X, regNr=0x%02X, regCnt=%d",
//                    wCache[0], wCache[1], wCache[2], wCache[3]));
        }
    }

    private void addWriteRequest(byte regNumber,byte regValue) {
        addRequest(new I2cTransfer(regNumber, (byte) 1, regValue, true));
//Log.i(logId, String.format("aWR reg= 0x%02X, val= 0x%02X, qLength= %d", regNumber, regValue, transferQueue.length()));
    }

    private void addWriteRequest(byte regNumber, byte regCount, long regValue) {
        addRequest(new I2cTransfer(regNumber, regCount, regValue, true));
    }

    private void addWriteRequest(byte regNumber, byte regCount, long regValue, boolean isLowFirst) {
        addRequest(new I2cTransfer(regNumber, regCount, regValue, isLowFirst));
    }

    private void addReadRequest(byte regNumber,byte regCount) {
        transferQueue.add(new I2cTransfer(regNumber, regCount));
//Log.i(logId, String.format("aRR reg= 0x%02X, cnt= 0x%02X, qLength= %d", regNumber, regCount, transferQueue.length()));
    }

    private void addRequest(I2cTransfer element) {
        try {
            rLock.lock();               // Seize the locks in same order as in portIsReady
            try {
                wLock.lock();
                transferQueue.add(element);
            } finally {
                wLock.unlock();
            }
        } finally {
            rLock.unlock();
        }
    }

// --------------------------------- PROCESSING OF RECEIVED DATA ---------------------------------

    private int getWord(int lowByte, int highByte) {
        int low     = rCache[DATA_OFFSET + lowByte  - DATA_START] & 0xFF;
        int high    = rCache[DATA_OFFSET + highByte - DATA_START] & 0xFF;
        return  256*high + low;
    }

    private void storeReceivedData() {
        byte    regNumber   = rCache[REG_NUMBER];
        byte    regCount    = rCache[REG_COUNT];
//Log.i(logId,String.format("Store regNr=0x%02X, regCount=0x%02X",regNumber,regCount));
        switch (regNumber) {
            case DATA_START:
                if (regCount == DATA_LENGTH) {
                    clear   = getWord(CDATAL, CDATAH);
                    red     = getWord(RDATAL, RDATAH);
                    green   = getWord(GDATAL, GDATAH);
                    blue    = getWord(BDATAL, BDATAH);
//Log.i(logId,String.format("Clear = %d",clear));
                }
                break;
            case ENABLE:
//Log.i(logId,String.format("enable = 0x%02X",rCache[DATA_OFFSET]));
                break;
            case ATIME:
                aTimeValue      = rCache[DATA_OFFSET] & 0xFF;
//Log.i(logId,String.format("aTime = 0x%02X",aTimeValue));
                break;
            case CONTROL:
                controlValue    = rCache[DATA_OFFSET] & 0xFF;
//Log.i(logId,String.format("control = 0x%02X",controlValue));
                break;
            case ID:
                idValue         = rCache[DATA_OFFSET] & 0xFF;
//Log.i(logId,String.format("idValue = 0x%02X",idValue));
                break;
            default:
                Log.e(logId, String.format("Unexpected R[0x%02X] = 0x%02X received", regNumber,rCache[DATA_OFFSET]));
                break;
        }

    }

// --------------------------------- APPLICATION PROGRAM INTERFACE -----------------------

    private int colorTemperature() {
                                // Ref: https://en.wikipedia.org/wiki/Color_temperature
                                //      https://en.wikipedia.org/wiki/CIE_1931_color_space

        if (red + green + blue == 0) return 999;    // Prevent divide by zero

        double  r   = red;      // Units or scale is irrelevant, freeze the current values
        double  g   = green;
        double  b   = blue;
                                // Convert from RGB to CIE XYZ color space
        double  x   = (-0.14282 * r) + (1.54924 * g) + (-0.95641 * b);
        double  y   = (-0.32466 * r) + (1.57837 * g) + (-0.73191 * b);
        double  z   = (-0.68202 * r) + (0.77073 * g) + ( 0.56332 * b);
                                // Convert to chromatic X and Y coordinates
        double  xyz = x + y + z;

        double  xC  = x/xyz;
        double  yC  = y/xyz;
                                // Convert to Correlated Color Temparature (CCT)
                                // Using McCamy's cubic approximation
                                // Based on the inverse slope n at epicenter
        double  n   = (xC - 0.3320) / (0.1858 - yC);
        double  CCT =  449.0 *n*n*n + 3525.0 *n*n + 6823.3 * n + 5520.33;
                                // This formula is not working properly with high
                                // green values, clamp the result to 1,000 - 29,999
        if (CCT > 29999) CCT = 29999;
        if (CCT < 1000) CCT = 1000;
        return  (int) CCT;
    }

// --------------------------------- PUBLIC METHODS -----------------------------------------------

    public int clearColor() {
        return clear;
    }

    public int redColor() {
        return red;
    }

    public int greenrColor() {
        return green;
    }

    public int blueColor() {
        return blue;
    }

    public int colorTemp() {
        return colorTemperature();
    }

    public boolean isIdOk() {
        return (idValue == 0x44) || (idValue == 0x4d);
    }

    public void setIntegrationTime(double milliSeconds) {
        int count   = (int)(milliSeconds/2.4);
        if (count<1)    count = 1;          // Clamp the time range
        if (count>256)  count = 256;
        addWriteRequest(ATIME, (byte) (256 - count));
    }

    public double getIntegrationTime() {
        return (double)(256 - aTimeValue) * 2.4;
    }

    public void setGain(int gain) {
        byte amplifierGain;
        if (gain < 4) {         amplifierGain   = CONTROL_AGAIN1;
        } else if (gain < 16) { amplifierGain   = CONTROL_AGAIN4;
        } else if (gain < 60) { amplifierGain   = CONTROL_AGAIN16;
        } else {                amplifierGain   = CONTROL_AGAIN60;
        }
        addWriteRequest(CONTROL, amplifierGain);
    }

    public int getGain() {
        if (controlValue == CONTROL_AGAIN1)     return 1;
        if (controlValue == CONTROL_AGAIN4)     return 4;
        if (controlValue == CONTROL_AGAIN16)    return 16;
        return 60;
    }
}
