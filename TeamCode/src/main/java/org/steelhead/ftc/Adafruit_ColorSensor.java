package org.steelhead.ftc;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.concurrent.locks.Lock;

/**
 * Created by alecmatthews on 10/18/16.
 */

public class Adafruit_ColorSensor implements I2cController.I2cPortReadyCallback {
    String LogId = "AdafrutCS:";

    static final byte
        ADDRESS         = 0x29, //Device Address

        ENABLE          = 0x00 - 128, //Enable Register
        ENABLE_AEIN     = 0x10,
        ENABLE_WEN      = 0x08,
        ENABLE_AEN      = 0x02,
        ENABLE_PON      = 0x01,

        ATIME           = 0x01 - 128,

        WTIME           = 0x03 - 128,

        AILTL           = 0x04 - 128,
        AILTH           = 0x05 - 128,
        AIHTL           = 0x06 - 128,
        AIHTH           = 0x07 - 128,

        PERS            = 0x0C - 128,

        CONFIG          = 0x0D - 128,
        CONFIG_WLONG    = 0x02,

        CONTROL         = 0x0F - 128,
        CONTROL_AGAIN1  = 0x00,
        CONTROL_AGAIN4  = 0x01,
        CONTROL_AGAIN16 = 0x10,
        CONTROL_AGAIN60 = 0x11,

        ID              = 0x12 - 128,
        STATUS          = 0x13 - 128,
        STATUS_AINT     = 0x10,
        STATUS_AVALID   = 0x01,

        DATA_START      = 0x14 - 128,   //Color Data Start
        DATA_LENGTH     = 0x08,         //Color Data Length

        CDATA           = 0x14 - 128,
        CDATAH          = 0x15 - 128,
        RDATA           = 0x16 - 128,
        RDATAH          = 0x17 - 128,
        GDATA           = 0x18 - 128,
        GDATAH          = 0x19 - 128,
        BDATA           = 0x1A - 128,
        BDATAH          = 0x1B - 128;

    static final byte
        READ_MODE = 0x00 - 128,
        RIGHT_MODE = 0x00;

    static final int
        CACHE_MODE = 0,
        DEV_ADDR = 1,
        REG_NUMBER = 2,
        REG_COUNT = 3,
        DATA_OFFSET = 4,
        ACTION_FLAG = 31;   //0 = idle, -1 = transfer in progress

    private ArrayQueue<I2cTransfer> transferQueue;
    private I2cDevice           csDev;
    private byte                csDevAddr;
    private byte[]              rCache;
    private byte[]              wCache;
    private Lock                rLock;
    private Lock                wLock;

    private int clear           = 0;
    private int red             = 0;
    private int green           = 0;
    private int blue            = 0;
    private int aTimeValue      = 0;
    private int controlValue    = 0;
    private int idValue         = 0;

    public Adafruit_ColorSensor(HardwareMap hardwareMap, String deviceName) {
        transferQueue = new ArrayQueue<I2cTransfer>();
        csDev = hardwareMap.i2cDevice.get(deviceName);
        csDevAddr = 2*ADDRESS;

        rCache = csDev.getI2cReadCache();
        wCache = csDev.getI2cWriteCache();
        rLock = csDev.getI2cReadCacheLock();
        wLock = csDev.getI2cWriteCacheLock();

        addWriteRequest(ENABLE, (Byte) (ENABLE_PON | ENABLE_AEN));
        addReadRquest(ID, (byte) 1);
        executeComands();
        csDev.registerForI2cPortReadyCallback(this);
        setGain(16);
        setIntegrationTime(50);
    }

    @Override
    public void portIsReady(int port) {

    }
}
