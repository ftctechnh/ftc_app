package org.usfirst.FTC5866.library;

/**
 * Created by Olavi Kamppari on 10/22/2015.
 */

public class I2cTransfer {
    static final byte
        READ_MODE           = 0x00 - 128,   // MSB = 1
        WRITE_MODE          = 0x00;         // MSB = 0

    public byte         mode;
    public byte         regNumber;
    public byte         regCount;
    public long         regValue;       // Write up to 8 bytes
    public boolean      isLowFirst;     // true = Low Byte first, false = Low Byte last

    public I2cTransfer (byte regNumber, byte regCount){
        this.mode           = READ_MODE;
        this.regNumber      = regNumber;
        this.regCount       = regCount;
        this.regValue       = 0;
        this.isLowFirst     = false;
    }

    public I2cTransfer (byte regNumber, byte regCount, long regValue, boolean isLowFirst){
        this.mode           = WRITE_MODE;
        this.regNumber      = regNumber;
        this.regCount       = regCount;
        this.regValue       = regValue;
        this.isLowFirst     = isLowFirst;
    }
}
