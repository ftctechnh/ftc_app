package com.qualcomm.customsensors;
//import com.qualcomm.robotcore.hardware.I2cDevice;

import android.util.Log;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.concurrent.locks.Lock;

/**
 * Created by Eric on 10/27/2015.
 */
/* Also included code modified from Alan M. Gilkes: https://github.com/ImperialRobotics/BoschIMU/blob/master/AdafruitIMU.java */
/* For more info on FTC MR I2C see http://olliesworkshops.blogspot.com/2015/10/ftc-i2c-sensor-illustrations.html */

public class IMUSensor implements HardwareDevice, I2cController.I2cPortReadyCallback {

    // Address Variables
    public static final short GYRO_ADDR_I2C = 0xD6;   //Default Device ID 1101 + 011b (Chip Enable)
    public static final short ACCEL_ADDR_I2C = 0x3A;    //Default Device ID 0011101b

    //Gyro Chip Registers
    public static final short CHIP_ENABLE = 0x0000;   //CHIP Enable Address -- 000b for now
    public static final short WHO_AM_I = 0x0F;
    public static final short CTRL_1 = 0x20;
    public static final short CTRL_2 = 0x21;
    public static final short CTRL_3 = 0x22;
    public static final short CTRL_4 = 0x23;
    public static final short CTRL_5 = 0x24;
    public static final short REFERENCE = 0x25;
    public static final short OUT_TEMP = 0x26;
    public static final short STATUS = 0x27;
    public static final short OUT_X = 0x28;
    public static final short OUT_Y = 0x2A;
    public static final short OUT_Z = 0x2C;
    public static final short FIFO_CTRL = 0x2E;
    public static final short FIFO_SRC = 0x2F;
    public static final short IG_CFG = 0x30;

    public static final short LOW_ODR = 0x39;

    //0.007476806640625 degrees/bit
    public static final double degreesPerBit = (2 * 245) / 65536; // 490 degrees / 16 bits

    private double currentPosition = 0;

    //Gyro Chip Settings
    //LOW_ODR is set in register 0x39--see data sheet page 48
    //DR1-DR0 Output Data Rate: 12.5 Hz = 00b
    //BW1-BW0 Bandwidth Not Applicable so 00b
    //PD: 1b = Normal Power Mode
    //Z-Axis Enable = 1b
    //Y-Axis Enable = 0b, disabled
    //X-Axis Enable = 0b, disabled
    public static final short SETTINGS_CTRL1 = 0x0C; //0x0C instead of 0x8C because LOW_ODR is different register
    public static final short SETTINGS_LOW_ODR_RESET = 0x05; //Bit 2 (0-Indexed) applies a software reset to the gyro page 48 of datasheet
    public static final short SETTINGS_LOW_ODR = 0x01;

    // 0x00 = 0b00000000
    // FS = 00 (+/- 250 dps full scale)
    public static final short SETTINGS_CTRL4 = 0x00;

    private final I2cDevice i2cIMU;
    private final int i2cBufferSize = 26; //Size of any extra buffers that will hold any incoming/outgoing cache data

    private final int baseI2Caddress; //The base I2C address used to address all of the IMU's registers
    private int operationalMode;//The operational mode to which the IMU will be set after its initial
    //reset.
    private final byte[] i2cReadCache;//The interface will insert the bytes which have been read into
    // this cache
    private final byte[] i2cWriteCache; //This cache will hold the bytes which are to be written to
    //the interface
    private final Lock i2cReadCacheLock;//A lock on access to the IMU's I2C read cache
    private final Lock i2cWriteCacheLock; //A lock on access to the IMU's I2C write cache

    public long totalI2Creads;//This variable counts the number of "read"s processed by the callback
    public double maxReadInterval;
    public double avgReadInterval;
    private long readStartTime;

    public IMUSensor(HardwareMap currentHWmap, String configuredIMUname,
                     byte baseAddress/*, byte operMode*/)  {
        i2cIMU = currentHWmap.i2cDevice.get(configuredIMUname);

        baseI2Caddress = (int) baseAddress & 0XFF;
        //operationalMode = (int)operMode & 0XFF;
        i2cReadCache = i2cIMU.getI2cReadCache();
        i2cReadCacheLock = i2cIMU.getI2cReadCacheLock();
        i2cWriteCache = i2cIMU.getI2cWriteCache();
        i2cWriteCacheLock = i2cIMU.getI2cWriteCacheLock();
    }

    public void initIMU() throws RobotCoreException
    {
        boolean okSoFar = true;
        long calibrationStartTime = 0L;
        byte[] outboundBytes = new byte[i2cBufferSize];

        //TODO: If we have troubles with the reset, perhaps we can get away without it?
        Log.i("FtcRobotController", "Preparing to software reset IMU (LOW_ODR) ......");
        outboundBytes[0] = LOW_ODR;//Sets the LOW_ODR Register and applies software reset
        okSoFar &= i2cWriteImmediately(outboundBytes, 1, SETTINGS_LOW_ODR_RESET);
        if (!okSoFar) {
            throw new RobotCoreException("IMU LOW_ODR RESET setting interrupted or I2C bus \"stuck busy\".");
        }

        //TODO: Do we need a delay to wait for the reset?

        Log.i("FtcRobotController", "Preparing to setup IMU CTRL1 register......");
        outboundBytes[0] = SETTINGS_CTRL1;//Sets the CTRL1 Registers
        okSoFar &= i2cWriteImmediately(outboundBytes, 1, CTRL_1);
        if (!okSoFar) {
            throw new RobotCoreException("IMU CTRL_1 setting interrupted or I2C bus \"stuck busy\".");
        }

        // 0x00 = 0b00000000
        // FS = 00 (+/- 250 dps full scale)
        Log.i("FtcRobotController", "Preparing to setup IMU CTRL4 register......");
        outboundBytes[0] = SETTINGS_CTRL1;//Sets the CTRL1 Registers
        okSoFar &= i2cWriteImmediately(outboundBytes, 1, CTRL_4);
        if (!okSoFar) {
            throw new RobotCoreException("IMU CTRL_4 setting interrupted or I2C bus \"stuck busy\".");
        }
    }

//    public IMUSensor(I2cDevice SensorInput){
//        IMUsensor = SensorInput;
//    }

    private void snooze(long milliSecs) {//Simple utility for sleeping (thereby releasing the CPU to
        // threads other than this one)
        try {
            Thread.sleep(milliSecs);
        } catch (InterruptedException e) {
        }
    }

    private boolean waitForI2cPortIsReady(int timeOutSeconds) {
        long rightNow = System.nanoTime();
        long loopStart = System.nanoTime();

        try {
            loopStart = System.nanoTime();
            while ((!i2cIMU.isI2cPortReady())
                    && (((rightNow = System.nanoTime()) - loopStart) < (timeOutSeconds * 1000000000L))) {
                Thread.sleep(250);//"Snooze" right here, until the port is ready (a good thing) OR n billion
                //nanoseconds pass with the port "stuck busy" (a VERY bad thing)
            }
        } catch (InterruptedException e) {
            Log.i("FtcRobotController", "Unexpected interrupt while \"sleeping\" in autoCalibrationOK.");
            return false;
        }
        if ((rightNow - loopStart) >= (timeOutSeconds * 1000000000L)) {
            Log.i("FtcRobotController", "IMU I2C port \"stuck busy\" for "
                    + (rightNow - loopStart) + " ns.");
            return false;//Signals the "stuck busy" condition
        }
        return true;
    }

    //returns a byte array with 1 or more register values
    private byte[] imuSensorRead(short address, short reg, int length) {
        byte[] retVal = new byte[length];

        waitForI2cPortIsReady(2); //Be sure the i2c port is ready with a 2 second timeout

        i2cIMU.enableI2cReadMode(address, reg, length);
        i2cIMU.setI2cPortActionFlag();

        i2cIMU.writeI2cCacheToController();
        snooze(250);
        i2cIMU.readI2cCacheFromController();//Read in the most recent data from the device
        snooze(500);//Give the data time to come into the Interface Module from the IMU hardware
        try {
            i2cReadCacheLock.lock();

            for (int i = 0; i < length; i++) {
                retVal[i] = i2cReadCache[I2cController.I2C_BUFFER_START_ADDRESS + i];
            }
        } finally {
            i2cReadCacheLock.unlock();
        }
        return retVal;
    }

    public void startIMU() {
  /*
   * The IMU datasheet lists the following actions as necessary to initialize and set up the IMU,
   * asssuming that all operations of the Constructor completed successfully:
   * 1. Register the callback method which will keep the reading of IMU registers going
   * 2. Enable I2C read mode and start the self-perpetuating sequence of register readings
   *
  */
        i2cIMU.registerForI2cPortReadyCallback(this);
        //offsetsInitialized = false;
        i2cIMU.enableI2cReadMode(baseI2Caddress, OUT_Z, 2);
        maxReadInterval = 0.0;
        avgReadInterval = 0.0;
        totalI2Creads = 0L;
        i2cIMU.setI2cPortActionFlag();//Set this flag to do the next read
        i2cIMU.writeI2cCacheToController();
        readStartTime = System.nanoTime();
    }

//TODO: Delete me if not needed
//    //reg should contain 1 of the gyro register values
//    //byte[] buffer should contain 1 or more bytes to be written to the gyro register
//    //length is the number of bytes that you want to write to the register
//    public void IMUGyroSensorWrite(short reg, byte[] buffer, int length )
//    {
//        //TODO: Possibly check isI2cPortReady() before performing action?
//        i2cIMU.copyBufferIntoWriteBuffer(buffer);
//        i2cIMU.enableI2cWriteMode(GYRO_ADDR_I2C, reg, length);
//        i2cIMU.setI2cPortActionFlag();
//    }

    public double getIMUGyroYawRate() {
//        double tempYaw = 0.0;
//        try {
//            i2cReadCacheLock.lock();
//
//            tempYaw = ((short) (((i2cReadCache[I2cController.I2C_BUFFER_START_ADDRESS] & 0XFF) << 8)
//                    | (i2cReadCache[I2cController.I2C_BUFFER_START_ADDRESS] & 0XFF)) * degreesPerBit);
//
//        } finally {
//            i2cReadCacheLock.unlock();
//

//       return tempYaw;

        return currentPosition;
    }

    public boolean testRead_WHO_AM_I()
    {
        waitForI2cPortIsReady(2); //Be sure the i2c port is ready, 2 second timeout
        byte[] test = imuSensorRead(GYRO_ADDR_I2C, WHO_AM_I, 1);

        if (test[0] == 0xD7)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean i2cWriteImmediately(byte[]outboundBytes, int byteCount, int registerAddress){
        long rightNow = System.nanoTime(), startTime = System.nanoTime(), timeOut = 3000000000L;
        int indx;

        try {
            while ((!i2cIMU.isI2cPortReady())
                    && (((rightNow = System.nanoTime()) - startTime) < timeOut)){
                Thread.sleep(250);//"Snooze" right here, until the port is ready (a good thing) OR n billion
                //nanoseconds pass with the port "stuck busy" (a VERY bad thing)
            }
        } catch (InterruptedException e) {
            Log.i("FtcRobotController", "Unexpected interrupt while \"sleeping\" in i2cWriteImmediately.");
            return false;
        }
        if ((rightNow - startTime) >= timeOut) return false;//Signals the "stuck busy" condition
        try {
            i2cWriteCacheLock.lock();
            for (indx =0; indx < byteCount; indx++) {
                i2cWriteCache[I2cController.I2C_BUFFER_START_ADDRESS + indx] = outboundBytes[indx];
                //Both the read and write caches start with 5 bytes of prefix data.
            }
        } finally {
            i2cWriteCacheLock.unlock();
        }
        i2cIMU.enableI2cWriteMode(baseI2Caddress, registerAddress, byteCount);
        i2cIMU.setI2cPortActionFlag();  //Set the "go do it" flag
        i2cIMU.writeI2cCacheToController(); //Then write it and the cache out
        snooze(250);//Give the data time to go from the Interface Module to the IMU hardware
        return true;
    }

    /*
 * Use of the following callback assumes that I2C reading has been enabled for a particular I2C
 * register address (as the starting address) and a particular byte count. Registration of this
 * callback should only take place when that reading enablement is done.
*/
    public void portIsReady(int port) { //Implementation of I2cController.I2cPortReadyCallback
        double latestInterval;
        if ((latestInterval = ((System.nanoTime() - readStartTime) / 1000000.0)) > maxReadInterval)
            maxReadInterval = latestInterval;
        avgReadInterval = ((avgReadInterval * 511.0) + latestInterval)/512.0;
        i2cIMU.readI2cCacheFromController(); //Read in the most recent data from the device
        totalI2Creads++;

        //Add to current position
        try {
            i2cReadCacheLock.lock();
            currentPosition = ((short) (((i2cReadCache[I2cController.I2C_BUFFER_START_ADDRESS] & 0XFF) << 8)
                    | (i2cReadCache[I2cController.I2C_BUFFER_START_ADDRESS] & 0XFF)));
            currentPosition *= latestInterval;
        } finally {
            i2cReadCacheLock.unlock();
        }

        i2cIMU.setI2cPortActionFlag();   //Set this flag to do the next read
        i2cIMU.writeI2cPortFlagOnlyToController();
        readStartTime = System.nanoTime();
        totalI2Creads++;
        //At this point, the port becomes busy (not ready) doing the next read
    }

    //All of the following implement the HardwareDevice Interface

    public String getDeviceName() {
        return "MinIMU-9 v3";
    }

    public String getConnectionInfo() {
        //return this.b.getConnectionInfo() + "; I2C port " + this.c;
        //Temporarily:
        return ("IMU connection info??");
    }

    public int getVersion() {
        return GYRO_ADDR_I2C;
    } //Temporarily

    public void close() {
    }
}
