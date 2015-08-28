package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;
import org.swerverobotics.library.internal.*;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 */
public class I2cDeviceClient implements I2cController.I2cPortReadyCallback
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    I2cDevice           i2cDevice;                  // the device we are talking to

    byte[]              readCache;                  // the buffer into which reads are retrieved
    byte[]              writeCache;                 // the buffer that we write from 
    static final int    dibCacheOverhead = 4;       // this many bytes at start of writeCache are system overhead
    Lock                readCacheLock;              // lock we must hold to look at readCache
    Lock                writeCacheLock;             // lock we must old to look at writeCache
    
    Object              lock = new Object();
    RegisterWindow      registerWindow;             // the set of registers to look at when we are in read mode

    READ_CACHE_STATUS   readCacheStatus;            // what we know about the contents of readCache
    WRITE_CACHE_STATUS  writeCacheStatus;           // what we know about the contents of writeCache

    enum READ_CACHE_STATUS
        {
        IDLE,           // the read cache is quiescent; it doesn't contain valid data
        SWITCHING,      // a request to switch to read mode has been made
        QUEUED,         // an I2C read has been queued, but we've not yet seen valid data
        VALID,          // the read cache contains valid data
        VALIDQUEUED,    // read cache has valid data AND a read has been queued 
        }

    enum WRITE_CACHE_STATUS 
        {
        IDLE,       // write cache is quiescent
        DIRTY,      // write cache has changed bits that need to be pushed to module
        QUEUED,     // write cache is currently being written to module, not yet returned
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public I2cDeviceClient(I2cDevice i2cDevice, int i2cAddr, RegisterWindow window)
        {
        this.i2cDevice = i2cDevice;

        this.readCache      = this.i2cDevice.getI2cReadCache();
        this.readCacheLock  = this.i2cDevice.getI2cReadCacheLock();
        this.writeCache     = this.i2cDevice.getI2cWriteCache();
        this.writeCacheLock = this.i2cDevice.getI2cWriteCacheLock();
        
        this.registerWindow = window;

        this.readCacheStatus  = READ_CACHE_STATUS.IDLE;
        this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
        
        Util.setPrivateIntField(this.i2cDevice, 2, i2cAddr);
        
        this.i2cDevice.registerForI2cPortReadyCallback(this);
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we pretty much continuously read 
     */
    public void setRegisterWindow(RegisterWindow window)
        {
        synchronized (this.lock)
            {
            if (this.registerWindow==null || window==null || !this.registerWindow.equals(window))
                {
                // Remember the new window
                this.registerWindow = window;
                
                // Any reads we currently have or are perhaps currently 
                // in flight are junk.
                this.readCacheStatus = READ_CACHE_STATUS.IDLE;
                }
            }
        }

    /**
     * Ensure that the current register window covers the indicated set of registers
     */
    public void ensureRegisterWindow(RegisterWindow windowNeeded, RegisterWindow windowToSet)
        {
        synchronized (this.lock)
            {
            if (this.registerWindow == null || !this.registerWindow.contains(windowNeeded))
                {
                setRegisterWindow(windowToSet);
                }
            }
        }

    /**
     * Read the indicated register
     */
    public byte read8(int ireg)
        {
        return this.read(ireg, 1)[0];
        }
    
    public byte[] read(int ireg, int creg)
        {
        try
            {
            synchronized (this.lock)
                {
                // We can only fetch registers that lie within the register window
                if (!this.registerWindow.contains(ireg, creg))
                    {
                    throw new IllegalArgumentException(String.format("read request (%d,%d) outside of read register window", ireg, creg));
                    }

                // Wait until we fill up with data
                for (; ; )
                    {
                    if (this.readCacheStatus == READ_CACHE_STATUS.VALID)
                        break;
                    if (this.readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                        break;
                    //
                    this.lock.wait();
                    }

                // Extract the data and return!
                this.readCacheLock.lockInterruptibly();
                try
                    {
                    int ibFirst = ireg - this.registerWindow.getIregFirst() + dibCacheOverhead;
                    return Arrays.copyOfRange(this.readCache, ibFirst, ibFirst + creg);
                    }
                finally
                    {
                    this.readCacheLock.unlock();
                    }
                }
            }
        catch (InterruptedException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }

    /**
     * Write a byte to the indicated register
     */
    public void write8(int ireg, int data)
        {
        this.write(ireg, new byte[] {(byte) data});
        }

    /**
     * Write data to a set of registers, begining with the one indicated
     */
    public void write(int ireg, byte[] data)
        {
        try
            {
            synchronized (this.lock)
                {
                // Wait until we can write to the write cache
                while (this.writeCacheStatus != WRITE_CACHE_STATUS.IDLE)
                    {
                    this.lock.wait();
                    }

                // Indicate where we want to write
                this.i2cDevice.enableI2cWriteMode(ireg, data.length);
                this.writeCacheStatus = WRITE_CACHE_STATUS.DIRTY;

                // Provide the data we want to write
                this.writeCacheLock.lockInterruptibly();
                try
                    {
                    System.arraycopy(data, 0, this.writeCache, dibCacheOverhead, data.length);
                    }
                finally
                    {
                    this.writeCacheLock.unlock();
                    }
                }
            }
        catch (InterruptedException e)
            {
            throw SwerveRuntimeException.wrap(e);
            }
        }
    
    @Override public void portIsReady(int port)
    // This is the callback from the device module indicating completion of previously requested work
        {
        synchronized (this.lock)
            {
            boolean setI2CActionFlag = false;
            boolean writeFullCache = false;
            
            if (this.writeCacheStatus == WRITE_CACHE_STATUS.IDLE 
                    || this.writeCacheStatus == WRITE_CACHE_STATUS.QUEUED
                    || this.readCacheStatus == READ_CACHE_STATUS.SWITCHING)
                {
                // If we just finished a write cycle, then our write cache is idle
                if (this.writeCacheStatus == WRITE_CACHE_STATUS.QUEUED)
                    this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
                
                // Deal with read issues
                if (this.readCacheStatus == READ_CACHE_STATUS.IDLE)
                    {
                    if (this.registerWindow != null)
                        {
                        // Initiate switch to read mode
                        this.readCacheStatus = READ_CACHE_STATUS.SWITCHING;
                        this.i2cDevice.enableI2cReadMode(this.registerWindow.getIregFirst(), this.registerWindow.getCreg());
                        setI2CActionFlag = true;
                        writeFullCache = true;
                        }
                    }
                else if (this.readCacheStatus == READ_CACHE_STATUS.SWITCHING)
                    {
                    // The port is in read mode. Start reading from it
                    this.readCacheStatus = READ_CACHE_STATUS.QUEUED;
                    this.i2cDevice.readI2cCacheFromModule();
                    setI2CActionFlag = true;
                    }
                else if (this.readCacheStatus == READ_CACHE_STATUS.QUEUED)
                    {
                    this.readCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                    // Re-read the next time around
                    this.i2cDevice.readI2cCacheFromModule();
                    setI2CActionFlag = true;
                    }
                else if (this.readCacheStatus == READ_CACHE_STATUS.VALID || this.readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                    {
                    this.readCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                    // Re-read the next time around
                    this.i2cDevice.readI2cCacheFromModule();
                    setI2CActionFlag = true;
                    }
                }
            
            else if (this.writeCacheStatus == WRITE_CACHE_STATUS.DIRTY)
                {
                // Queue the write cache for writing to the module
                this.writeCacheStatus = WRITE_CACHE_STATUS.QUEUED;
                setI2CActionFlag = true;      // request an I2C write
                writeFullCache = true;
                
                // What's the impact on the read cache? 
                if (this.readCacheStatus == READ_CACHE_STATUS.QUEUED || this.readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                    {
                    // We just completed a read cycle. Data is valid, for the moment.
                    // But don't re-issue the reread request
                    this.readCacheStatus = READ_CACHE_STATUS.VALID;
                    }
                else if (this.readCacheStatus == READ_CACHE_STATUS.VALID)
                    {
                    // We've gone a cycle w/o reading. What's there is out of date.
                    this.readCacheStatus = READ_CACHE_STATUS.IDLE;
                    }
                }
            
            // Set action flag and queue to module as requested
            if (setI2CActionFlag)
                this.i2cDevice.setI2cPortActionFlag();

            if (setI2CActionFlag && !writeFullCache)
                this.i2cDevice.writeI2cPortFlagOnlyToModule();
            
            if (writeFullCache)
                this.i2cDevice.writeI2cCacheToModule();
            
            // Tell any readers or writers that statuses have changed
            this.lock.notifyAll();
            }
        }
    
    //----------------------------------------------------------------------------------------------
    // RegisterWindow
    //----------------------------------------------------------------------------------------------

    public static class RegisterWindow
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        // enableI2cReadMode and enableI2cWriteMode both impose a maximum length
        // on the size of data that can be read or written at one time
        public static final int cregMax = 27;

        private final int iregFirst;
        private final int creg;

        public int getIregFirst() { return this.iregFirst; }
        public int getIregMax()   { return this.iregFirst + this.creg; }
        public int getCreg()      { return this.creg; }

        //------------------------------------------------------------------------------------------
        // Construction
        //------------------------------------------------------------------------------------------

        public RegisterWindow(int ib)              { this(ib, 1); }
        public RegisterWindow(int iregFirst, int creg)
            {
            this.iregFirst = iregFirst;
            this.creg = creg;
            if (creg < 0 || creg > cregMax)
                throw new IllegalArgumentException(String.format("buffer length %d invalid; max is %d", creg, cregMax));
            }

        public static RegisterWindow createDefault()
            {
            // Most of the legacy sensors, at least, start their registers at 0x41, so
            // that seems a reasonable thing to start our default window at
            return new RegisterWindow(0x41, cregMax);
            }

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        public boolean equals(RegisterWindow him)
            {
            return this.getIregFirst() == him.getIregFirst() 
                    && this.getCreg() == him.getCreg();
            }
        
        /**
         * Is this register window wholly contained within the receiver
         */
        public boolean contains(RegisterWindow him)
            {
            return this.getIregFirst() <= him.getIregFirst() && him.getIregMax() <= this.getIregMax();
            }
        
        public boolean contains(int ireg, int creg)
            {
            return this.getIregFirst() <= ireg && ireg + creg <= this.getIregMax();
            }
        }
    }
