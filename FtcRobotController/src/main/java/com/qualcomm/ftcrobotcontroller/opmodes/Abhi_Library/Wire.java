package com.qualcomm.ftcrobotcontroller.opmodes.Abhi_Library;


import android.util.Log;

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
    private ArrayQueue<Element> downQueue;  // Down stream buffer
    private ArrayQueue<Element> upQueue;    // Up stream buffer
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
        downQueue   = new ArrayQueue<Element>();
        upQueue     = new ArrayQueue<Element>();
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

    public void write(int regNumber, Object x) {
        if (x == null) {
            beginWrite(regNumber);
            endWrite();
        }
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

    public int requestCount() {
        int count = 0;
        try {
            wLock.lock();
            count = downQueue.length();
        } finally {
            wLock.unlock();
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
        boolean isValidReply = false;
        try {
            rLock.lock();
           if (
                    rCache[CACHE_MODE]  == wCache[CACHE_MODE] &&
                    rCache[DEV_ADDR]    == wCache[DEV_ADDR] &&
                    rCache[REG_NUMBER]  == wCache[REG_NUMBER] &&
                    rCache[REG_COUNT]   == wCache[REG_COUNT]) {
                storeReceivedData();                        // Store read/write data
                rCache[REG_COUNT]   = -1;                   // Mark the reply used
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
                wCache[DEV_ADDR]    = -1;           // No further polling is required
            } else {
                getFromQueue(wCache, downQueue);    // Ignore timestamp
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
//        logCache('d',"addRequest");
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
        addToQueue(uMicros, rCache, upQueue);
//        logCache('r',"storeReceivedData");
    }

//------------------------------------------------- Add and Remove from Queue ------------------

    private void addToQueue(long timeStamp, byte[] cache, ArrayQueue<Element> queue) {
        int length          = DATA_OFFSET + cache[REG_COUNT];
        Element element     = new Element();
        element.timeStamp   = timeStamp;
        element.cache       = new byte[length];
        for (int i = 0; i < length; i++) element.cache[i] = cache[i];
        queue.add(element);
    }

    private long getFromQueue(byte[] cache, ArrayQueue<Element> queue) {
        Element element     = queue.remove();
        if (element == null) return 0;
        int length          = element.cache.length;
        long timeStamp      = element.timeStamp;
        for (int i = 0; i < length; i++) cache[i] = element.cache[i];
        return timeStamp;
    }

    class Element {
        public long timeStamp;
        public byte[] cache;
    }


    private void logCache(char cacheLetter, String function) {
        switch (cacheLetter){
            case 'd':   logCache(dCache,function,cacheLetter); break;
            case 'w':   logCache(wCache,function,cacheLetter); break;
            case 'r':   logCache(rCache,function,cacheLetter); break;
            case 'u':   logCache(uCache,function,cacheLetter); break;
        }
    }

    private void logCache(byte[] cache, String function, char cacheLetter) {
        int     showCount = cache[3];
        boolean isWrite = (cache[0] == WRITE_MODE);
        switch (cacheLetter){
            case 'd':
            case 'w':
                if (cache[0] != WRITE_MODE) showCount = 0;
                break;
            case 'r':
            case 'u':
                if (cache[0] == WRITE_MODE) showCount = 0;
                break;
        }
        if (showCount > 6)          showCount   = 6;
        String msg = String.format(
            "%17s %c[%d][%d], %cCache: mod=0x%02X, dev=0x%02X, reg=0x%02X, cnt=%2d  ",
            function,isIdle?'T':'F',downQueue.length(),upQueue.length(),
            cacheLetter,cache[0],cache[1],cache[2],cache[3]);
        for (int i=0;i<showCount;i++) {
            msg += String.format(" 0x%02X",cache[4+i]);
        }
        Log.i("Wire",msg);
    }
}

