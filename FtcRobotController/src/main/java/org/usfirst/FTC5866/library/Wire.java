package org.usfirst.FTC5866.library;

/**
 * Created by Olavi Kamppari on 10/25/2015.
 */
import android.renderscript.Element;
import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.concurrent.locks.Lock;

public class Wire implements I2cController.I2cPortReadyCallback {
// --------------------------------- CONSTANTS -------------------------------------------------

    // Cache buffer constant values
    static final byte
            READ_MODE       = 0x00 - 128,   // MSB = 1
            WRITE_MODE      = 0x00;         // MSB = 0

    // Cache buffer index values
    static final int
            CACHE_MODE      = 0,            // MSB = 1 when read mode is active
            DEV_ADDR        = 1,            // Device address
            REG_NUMBER      = 2,            // Register address
            REG_COUNT       = 3,            // Register count
            DATA_OFFSET     = 4,            // First byte of transferred data
            LAST_INDEX      = 29,           // Last index available for data
            ACTION_FLAG     = 31,           // 0 = idle, -1 = transfer is active
            CACHE_SIZE      = 32;           // dCache fixed size

// --------------------------------- CLASS VARIABLES -------------------------------------------
    private ArrayQueue downQueue;  // Down stream buffer
    private ArrayQueue upQueue;    // Up stream buffer
    private I2cDevice wireDev;              // Generic I2C Device Object
    private byte wireDevAddr;               // Generic Device Address
    private byte[] rCache;                  // Read Cache
    private byte[] wCache;                  // Write Cache
    private Lock rLock;                     // Lock for Read Cache
    private Lock wLock;                     // Lock for Write Cache & request queue

    private byte[] dCache;                  // Buffer for down stream details
    private int dNext;                      // Next location for incoming bytes
    private byte[] uCache;                  // Buffer for up stream response
    private int uNext;                      // Next location for response bytes
    private int uLimit;                     // Last location for response bytes
    private long uMicros;                   // Time stamp, microseconds since start
    private long startTime;                 // Start time in nanoseconds
    private boolean isIdle;                 // Mechanism to control polling

// --------------------------------- CLASS INIT AND CLOSE ---------------------------------------

    public Wire(HardwareMap hardwareMap, String deviceName, int devAddr) {
        downQueue   = new ArrayQueue();
        upQueue     = new ArrayQueue();
        wireDev     = hardwareMap.i2cDevice.get(deviceName);
        wireDevAddr = (byte) devAddr;

        rCache      = wireDev.getI2cReadCache();
        wCache      = wireDev.getI2cWriteCache();
        rLock       = wireDev.getI2cReadCacheLock();
        wLock       = wireDev.getI2cWriteCacheLock();

        dCache      = new byte[CACHE_SIZE];
        dNext       = DATA_OFFSET;
        uCache      = new byte[CACHE_SIZE];
        uMicros     = 0L;
        startTime   = System.nanoTime();
        uNext       = DATA_OFFSET;
        uLimit      = uNext;
        isIdle      = true;

        wireDev.registerForI2cPortReadyCallback(this);
    }

    public void close() {
        wireDev.deregisterForPortReadyCallback();
        downQueue.close();
        upQueue.close();
        wireDev.close();
    }

    //------------------------------------------------- Public Methods -------------------------
    public void beginWrite(int regNumber) {
        dCache[CACHE_MODE]  = WRITE_MODE;
        dCache[DEV_ADDR]    = wireDevAddr;
        dCache[REG_NUMBER]  = (byte) regNumber;
        dNext               = DATA_OFFSET;
    }

    public void write(int value) {
        if (dNext >= LAST_INDEX) return;    // Max write size has been reached
        dCache[dNext++] = (byte) value;
    }

    public void write(int regNumber, int value) {
        beginWrite(regNumber);
        write(value);
        endWrite();
    }

    public void writeHL(int regNumber, int value) {
        beginWrite(regNumber);
        write((byte) (value >> 8));
        write((byte) (value));
        endWrite();
    }

    public void writeLH(int regNumber, int value) {
        beginWrite(regNumber);
        write((byte) (value));
        write((byte) (value >> 8));
        endWrite();
    }

    public void endWrite() {
        dCache[REG_COUNT]   = (byte) (dNext - DATA_OFFSET);
        addRequest();
    }

    public void requestFrom(int regNumber, int regCount) {
        dCache[CACHE_MODE]  = READ_MODE;
        dCache[DEV_ADDR]    = wireDevAddr;
        dCache[REG_NUMBER]  = (byte) regNumber;
        dCache[REG_COUNT]   = (byte) regCount;
        addRequest();
    }

    public int responseCount() {
        int count = 0;
        try {
            rLock.lock();
            count = upQueue.length();
        } finally {
            rLock.unlock();
        }
        return count;
    }

    public boolean getResponse() {
        boolean responseReceived = false;
        uNext = DATA_OFFSET;
        uLimit = uNext;
        try {
            rLock.lock();                   // Explicit protection with rLock
            if (!upQueue.isEmpty()) {
                responseReceived    = true;
                uMicros             = getFromQueue(uCache, upQueue);
                uLimit              = uNext + uCache[REG_COUNT];
            }
        } finally {
            rLock.unlock();
        }
        return responseReceived;
    }

    public boolean isRead()     {
        return (uCache[CACHE_MODE] == READ_MODE);
    }

    public boolean isWrite()    {
        return (uCache[CACHE_MODE] == WRITE_MODE);
    }

    public int registerNumber() {
        return uCache[REG_NUMBER] & 0xff;
    }

    public int deviceAddress() {
        return uCache[DEV_ADDR] & 0xff;
    }

    public long micros() {
        return uMicros;
    }

    public int available() {
        return uLimit - uNext;
    }

    public int read() {
        if (uNext >= uLimit) return 0;
        return uCache[uNext++] & 0xff;
    }

    public int readHL() {
        int high    = read();
        int low     = read();
        return 256 * high + low;
    }

    public int readLH() {
        int low     = read();
        int high    = read();
        return 256 * high + low;
    }

//------------------------------------------------- Main routine: Device CallBack -------------

    public void portIsReady(int port) {
       // DbgLog.msg(String.format("=====  PortIsReady ====="));
        if (isIdle) return;
        boolean isValidReply = false;
        try {
            rLock.lock();
            if (
                    rCache[CACHE_MODE]  == wCache[CACHE_MODE] &&
                    rCache[DEV_ADDR]    == wCache[DEV_ADDR] &&
                    rCache[REG_NUMBER]  == wCache[REG_NUMBER] &&
                    rCache[REG_COUNT]   == wCache[REG_COUNT]) {
                storeReceivedData();                        // Store read/write data
                isValidReply = true;
            }
        } finally {
            rLock.unlock();
        }
        if (isValidReply) {
            executeCommands();                              // Start next transmission
        } else {
            boolean isPollingRequired = false;
            try {
                wLock.lock();                               // Protect the testing
                isPollingRequired = (wCache[DEV_ADDR] == wireDevAddr);
            } finally {
                wLock.unlock();
            }
            if (isPollingRequired) {
                wireDev.readI2cCacheFromController();       // Keep polling active
            }
        }
    }

// --------------------------------- Commands to DIM -------------------------------------------

    private void executeCommands() {
        try {
            wLock.lock();
            if (downQueue.isEmpty()) {
                isIdle  = true;
            } else {
                getFromQueue(wCache, downQueue); // Ignore timestamp
                wCache[ACTION_FLAG] = -1;
            }
        } finally {
            wLock.unlock();
        }
        if (!isIdle) {
            wireDev.writeI2cCacheToController();
        }
    }

    private void addRequest() {
        boolean isStarting = false;
        try {
            wLock.lock();
            if (isIdle) {
                int length = DATA_OFFSET + dCache[REG_COUNT];
                for (int i = 0; i < length; i++) wCache[i] = dCache[i];
                wCache[ACTION_FLAG] = -1;
                isIdle = false;
                isStarting = true;
            } else {
                addToQueue(0L, dCache, downQueue);
            }
        } finally {
            wLock.unlock();
        }
        if (isStarting) {
            wireDev.writeI2cCacheToController();
        }
    }

// --------------------------------- PROCESSING OF RECEIVED DATA -------------------------------

    private void storeReceivedData() {
                                        // rCache has been locked
        long uMicros = (System.nanoTime() - startTime) / 1000L;
        //DbgLog.msg(String.format("=====  rCache value " + rCache[4] +" ====="));

        addToQueue(uMicros, rCache, upQueue);
    }

//------------------------------------------------- Add and Remove from Queue ------------------

    private void addToQueue(long timeStamp, byte[] cache, ArrayQueue queue) {
        int length          = DATA_OFFSET + cache[REG_COUNT];
        Element element     = new Element();
        element.timeStamp   = timeStamp;
        element.cache       = new byte[length];
        for (int i = 0; i < length; i++) element.cache[i] = cache[i];
        //DbgLog.msg(String.format("=====  In addToQueCache value " + cache[4] +" ====="));
        queue.add(element);
    }

    private long getFromQueue(byte[] cache, ArrayQueue queue) {
        Element element     = (org.usfirst.FTC5866.library.Wire.Element) queue.remove();
        //DbgLog.msg(String.format("=====  uQueue After" + queue.length() +" "+ element.cache[4]+" ====="));
        if (element == null) return 0;
        int length          = element.cache.length;
        long timeStamp      = element.timeStamp;
        for (int i = 0; i < length; i++) cache[i] = element.cache[i];
        //DbgLog.msg(String.format("=====  Cache value " + cache[4] +" ====="));

        return timeStamp;
    }

    class Element {
        public long timeStamp;
        public byte[] cache;
    }
}