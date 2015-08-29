package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.exceptions.SwerveRuntimeException;
import org.swerverobotics.library.internal.*;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * I2cDeviceClient is a utility class that makes it easy to read or write data to 
 * an instance of I2cDevice.
 */
public final class I2cDeviceClient 
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    final I2cDevice     i2cDevice;                  // the device we are talking to
    final Callback      callback;

    final byte[]        readCache;                  // the buffer into which reads are retrieved
    final byte[]        writeCache;                 // the buffer that we write from 
    static final int    dibCacheOverhead = 4;       // this many bytes at start of writeCache are system overhead
    final Lock          readCacheLock;              // lock we must hold to look at readCache
    final Lock          writeCacheLock;             // lock we must old to look at writeCache
    
    final Object        lock = new Object();        // the lock we use to synchronize concurrent callers as well as the portIsReady() callback
    
    RegWindow           registerWindow;             // the set of registers to look at when we are in read mode. May be null, indicating no read needed
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
        IDLE,           // write cache is quiescent
        DIRTY,          // write cache has changed bits that need to be pushed to module
        QUEUED,         // write cache is currently being written to module, not yet returned
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Instantiate an I2cDeviceClient instance in the indicated device with the indicated
     * initial window of registers being read.
     * 
     * initialRegisterWindow may be null if no registers are initially to be read.
     * 
     * If i2cAddr is >= 0, then the address of the i2cDevice is changed to be the one indicated.
     */
    public I2cDeviceClient(I2cDevice i2cDevice, RegWindow initialRegisterWindow, int i2cAddr)
        {
        this.i2cDevice = i2cDevice;
        this.callback = new Callback();

        this.readCache      = this.i2cDevice.getI2cReadCache();
        this.readCacheLock  = this.i2cDevice.getI2cReadCacheLock();
        this.writeCache     = this.i2cDevice.getI2cWriteCache();
        this.writeCacheLock = this.i2cDevice.getI2cWriteCacheLock();
        
        this.registerWindow = initialRegisterWindow;

        this.readCacheStatus  = READ_CACHE_STATUS.IDLE;
        this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
        
        if (i2cAddr >= 0)
            Util.setPrivateIntField(this.i2cDevice, 2, i2cAddr);
        
        this.i2cDevice.registerForI2cPortReadyCallback(this.callback);
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we pretty much continuously read 
     */
    public void setRegisterWindow(RegWindow window)
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
     * Return the current register window.
     */
    public RegWindow getRegisterWindow()
        {
        synchronized (this.lock)
            {
            return this.registerWindow;
            }
        }

    /**
     * Ensure that the current register window covers the indicated set of registers.
     * 
     * If there is currently a non-null register window, and windowNeeded is non-null,
     * and the curret register window entirely contains windowNeeded, then do nothing.
     * Otherwise, set the current register window to windowToSet.
     */
    public void ensureRegisterWindow(RegWindow windowNeeded, RegWindow windowToSet)
        {
        synchronized (this.lock)
            {
            if (this.registerWindow == null 
                    || windowNeeded == null
                    || !this.registerWindow.contains(windowNeeded))
                {
                setRegisterWindow(windowToSet);
                }
            }
        }

    /**
     * Read the byte at the indicated register.
     * 
     * The register must lie within the current register window.
     */
    public byte read8(int ireg)
        {
        return this.read(ireg, 1)[0];
        }

    /**
     * Read a (little-endian) integer starting at the indicated register.
     * 
     * The integer must lie within the current register window.
     */
    public int read16(int ireg)
        {
        byte[] data = read(ireg, 2);
        return Util.makeInt(data[0], data[1]);
        }

    /**
     * Read a contiguous set of registers.
     * 
     * All the registers must lie within the current register window
     */
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
                for (;;)
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
     * Write an little endian int to an adjacent pair of registers
     */
    public void write16(int ireg, int data)
        {
        this.write(ireg, new byte[] { (byte)(data&0xFF), (byte)((data>>8)&0xFF)} );
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
    
    class Callback implements I2cController.I2cPortReadyCallback
        {
        @Override public void portIsReady(int port)
            // This is the callback from the device module indicating completion of previously requested work
            {
            synchronized (lock)
                {
                boolean setI2CActionFlag = false;
                boolean writeFullCache = false;
    
                if (writeCacheStatus == WRITE_CACHE_STATUS.IDLE
                        || writeCacheStatus == WRITE_CACHE_STATUS.QUEUED
                        || readCacheStatus == READ_CACHE_STATUS.SWITCHING)
                    {
                    // If we just finished a write queuing cycle, then our write cache is idle
                    if (writeCacheStatus == WRITE_CACHE_STATUS.QUEUED)
                        writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
    
                    // Deal with read issues
                    if (readCacheStatus == READ_CACHE_STATUS.IDLE)
                        {
                        if (registerWindow != null)
                            {
                            // Initiate switch to read mode
                            readCacheStatus = READ_CACHE_STATUS.SWITCHING;
                            i2cDevice.enableI2cReadMode(registerWindow.getIregFirst(), registerWindow.getCreg());
                            setI2CActionFlag = true;
                            writeFullCache = true;
                            }
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.SWITCHING)
                        {
                        if (i2cDevice.isI2cPortInReadMode())
                            {
                            // The port is in read mode. Start reading from it
                            readCacheStatus = READ_CACHE_STATUS.QUEUED;
                            i2cDevice.readI2cCacheFromModule();
                            setI2CActionFlag = true;
                            }
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.QUEUED)
                        {
                        readCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        // Re-read the next time around
                        i2cDevice.readI2cCacheFromModule();
                        setI2CActionFlag = true;
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.VALID || readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                        {
                        readCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        // Re-read the next time around
                        i2cDevice.readI2cCacheFromModule();
                        setI2CActionFlag = true;
                        }
                    }
    
                else if (writeCacheStatus == WRITE_CACHE_STATUS.DIRTY)
                    {
                    // Queue the write cache for writing to the module
                    writeCacheStatus = WRITE_CACHE_STATUS.QUEUED;
                    setI2CActionFlag = true;      // request an I2C write
                    writeFullCache = true;
    
                    // What's the impact on the read cache? 
                    if (readCacheStatus == READ_CACHE_STATUS.QUEUED || readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                        {
                        // We just completed a read cycle. Data is valid, for the moment.
                        // But don't re-issue the reread request
                        readCacheStatus = READ_CACHE_STATUS.VALID;
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.VALID)
                        {
                        // We've gone a cycle w/o reading. What's there is out of date.
                        readCacheStatus = READ_CACHE_STATUS.IDLE;
                        }
                    }
    
                // Set action flag and queue to module as requested
                if (setI2CActionFlag)
                    i2cDevice.setI2cPortActionFlag();
    
                if (setI2CActionFlag && !writeFullCache)
                    i2cDevice.writeI2cPortFlagOnlyToModule();
    
                if (writeFullCache)
                    i2cDevice.writeI2cCacheToModule();
    
                // Tell any readers or writers that statuses have changed
                lock.notifyAll();
                }
            }
        }
    //----------------------------------------------------------------------------------------------
    // RegisterWindow
    //----------------------------------------------------------------------------------------------

    /**
     * RegWindow is a utility class for managing the window of I2C register bytes that
     * are read from our I2C device on every hardware cycle
     */
    public static class RegWindow
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        /**
         * enableI2cReadMode and enableI2cWriteMode both impose a maximum length
         * on the size of data that can be read or written at one time. cregMax
         * indicates that maximum size.
         */
        public static final int cregMax = 27;

        /**
         * The first register in the window
         */
        private final int iregFirst;
        /**
         * The number of registers in the window
         */
        private final int creg;
        /**
         * Return the first register in the window
         */
        public int getIregFirst() { return this.iregFirst; }
        /**
         * Return the first register NOT in the window
         */
        public int getIregMax()   { return this.iregFirst + this.creg; }
        /**
         * Return the number of registers in the window
         */
        public int getCreg()      { return this.creg; }

        //------------------------------------------------------------------------------------------
        // Construction
        //------------------------------------------------------------------------------------------

        /**
         * Create a new register window with the indicated starting register and register count
         */
        public RegWindow(int iregFirst, int creg)
            {
            this.iregFirst = iregFirst;
            this.creg = creg;
            if (creg < 0 || creg > cregMax)
                throw new IllegalArgumentException(String.format("buffer length %d invalid; max is %d", creg, cregMax));
            }

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        /**
         * Do the recevier and the indicated register window cover exactly the 
         * same set of registers?
         */
        public boolean equals(RegWindow him)
            {
            return this.getIregFirst() == him.getIregFirst() 
                    && this.getCreg() == him.getCreg();
            }
        
        /**
         * Does the receiver wholly contain the indicated window?
         */
        public boolean contains(RegWindow him)
            {
            if (him==null)
                return false;
            
            return this.getIregFirst() <= him.getIregFirst() && him.getIregMax() <= this.getIregMax();
            }

        /**
         * @see #contains(RegWindow) 
         */
        public boolean contains(int ireg, int creg)
            {
            return this.contains(new RegWindow(ireg, creg));
            }
        }
    }
