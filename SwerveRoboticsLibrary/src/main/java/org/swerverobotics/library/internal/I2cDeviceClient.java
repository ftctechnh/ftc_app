package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import junit.framework.Assert;
import org.swerverobotics.library.BuildConfig;
import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * I2cDeviceClient is a utility class that makes it easy to read or write data to 
 * an instance of I2cDevice.
 */
public final class I2cDeviceClient implements II2cDeviceClient
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /**
     * The I2cDevice of which are are a client.
     */
    public final II2cDevice     i2cDevice;                  // the device we are talking to

    private final Callback      callback;
    private       Thread        callbackThread;             // the thread on which we observe our callbacks to be made
    private final ElapsedTime   timeSinceLastHeartbeat;
    private       int           msHeartbeatInterval;
    
    private final byte[]        readCache;                  // the buffer into which reads are retrieved
    private final byte[]        writeCache;                 // the buffer that we write from 
    private static final int    dibCacheOverhead = 4;       // this many bytes at start of writeCache are system overhead
    private final Lock          readCacheLock;              // lock we must hold to look at readCache
    private final Lock          writeCacheLock;             // lock we must old to look at writeCache

    private final Object        lock = new Object();        // the lock we use to synchronize concurrent callers as well as the portIsReady() callback

    private RegWindow           registerWindow;             // the set of registers to look at when we are in read mode. May be null, indicating no read needed
    private long                nanoTimeReadCacheValid;     // the time on the System.nanoTime() clock at which the read cache was last set as valid
    private READ_CACHE_STATUS   readCacheStatus;            // what we know about the contents of readCache
    private WRITE_CACHE_STATUS  writeCacheStatus;           // what we know about the contents of writeCache

    private enum READ_CACHE_STATUS
        {
        IDLE,           // the read cache is quiescent; it doesn't contain valid data
        SWITCHING,      // a request to switch to read mode has been made
        QUEUED,         // an I2C read has been queued, but we've not yet seen valid data
        VALID,          // the read cache contains valid data
        VALIDQUEUED,    // read cache has valid data AND a read has been queued 
        }

    private enum WRITE_CACHE_STATUS 
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
     */
    public I2cDeviceClient(II2cDevice i2cDevice, int i2cAddr, RegWindow initialRegisterWindow)
        {
        this.i2cDevice = i2cDevice;
        this.callback = new Callback();
        this.callbackThread = null;
        this.timeSinceLastHeartbeat = new ElapsedTime();
        this.timeSinceLastHeartbeat.reset();
        this.msHeartbeatInterval = 0;

        this.readCache      = this.i2cDevice.getI2cReadCache();
        this.readCacheLock  = this.i2cDevice.getI2cReadCacheLock();
        this.writeCache     = this.i2cDevice.getI2cWriteCache();
        this.writeCacheLock = this.i2cDevice.getI2cWriteCacheLock();
        
        this.registerWindow = initialRegisterWindow;

        this.nanoTimeReadCacheValid = 0;
        this.readCacheStatus  = READ_CACHE_STATUS.IDLE;
        this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
        
        this.i2cDevice.setI2cAddr(i2cAddr);
        
        this.i2cDevice.registerForI2cPortReadyCallback(this.callback);
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we will read and read and read again on every hardware cycle 
     */
    public void setRegisterWindow(RegWindow window)
        {
        synchronized (this.lock)
            {
            if (this.registerWindow==null || !this.registerWindow.equals(window))
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
     */
    public void ensureRegisterWindow(RegWindow windowNeeded, RegWindow windowToSet)
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
     * Read the byte at the indicated register.
     */
    public byte read8(int ireg)
        {
        return this.read(ireg, 1)[0];
        }

    /**
     * Read a (little-endian) integer starting at the indicated register.
     */
    public int read16(int ireg)
        {
        byte[] data = read(ireg, 2);
        return Util.makeInt(data[0], data[1]);
        }

    /**
     * Read a contiguous set of registers
     */
    public byte[] read(int ireg, int creg)
        {
        return this.readTimeStamped(ireg, creg).data;
        }
    
    /**
     * Read a contiguous set of registers.
     */
    public TimestampedData readTimeStamped(int ireg, int creg)
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
                    TimestampedData result = new TimestampedData();
                    result.data     = Arrays.copyOfRange(this.readCache, ibFirst, ibFirst + creg);
                    result.nanoTime = this.nanoTimeReadCacheValid;
                    return result;
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
     * Write data to a set of registers, beginning with the one indicated
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
                
                // Indicate we are dirty so the callback will write us out
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
    
    public Thread getCallbackThread()
        {
        synchronized (this.lock)
            {
            return this.callbackThread;
            }
        }
    
    public int getHeartbeatInterval()
        {
        synchronized (this.lock)
            {
            return this.msHeartbeatInterval;
            }
        }
    
    public void setHeartbeatInterval(int ms)
        {
        ms = Math.max(0, ms);
        synchronized (this.lock)
            {
            this.msHeartbeatInterval = ms;
            }
        }
    
    private class Callback implements I2cController.I2cPortReadyCallback
        {
        @Override public void portIsReady(int port)
        // This is the callback from the device module indicating completion of previously requested work.
        // At the moment we are called, we are assured that the read buffer / write buffer for our port in the
        // USB device is not currently busy.
        //
        // We've got quite the little state machine here!
            {
            synchronized (lock)
                {
                if (callbackThread == null)
                    callbackThread = Thread.currentThread();
                else if (BuildConfig.DEBUG)
                    Assert.assertEquals(callbackThread.getId(), Thread.currentThread().getId());
                
                boolean setActionFlag  = false;
                boolean queueFullWrite = false;
                boolean queueRead      = false;
                
                READ_CACHE_STATUS  nextReadCacheStatus  = readCacheStatus;
                WRITE_CACHE_STATUS nextWriteCacheStatus = writeCacheStatus;
    
                if (writeCacheStatus == WRITE_CACHE_STATUS.IDLE
                        || writeCacheStatus == WRITE_CACHE_STATUS.QUEUED
                        || readCacheStatus == READ_CACHE_STATUS.SWITCHING)
                    {
                    // If we just finished a write queuing cycle, then our write cache is idle
                    if (writeCacheStatus == WRITE_CACHE_STATUS.QUEUED)
                        nextWriteCacheStatus = WRITE_CACHE_STATUS.IDLE;
    
                    // Deal with read issues
                    if (readCacheStatus == READ_CACHE_STATUS.IDLE)
                        {
                        if (registerWindow != null)
                            {
                            // Initiate switch to read mode
                            nextReadCacheStatus = READ_CACHE_STATUS.SWITCHING;
                            i2cDevice.enableI2cReadMode(registerWindow.getIregFirst(), registerWindow.getCreg());
                            setActionFlag = true;
                            queueFullWrite = true;
                            }
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.SWITCHING)
                        {
                        if (i2cDevice.isI2cPortInReadMode())
                            {
                            // The port has switched to read mode. Start reading from it
                            nextReadCacheStatus = READ_CACHE_STATUS.QUEUED;
                            queueRead = true;
                            setActionFlag = true;
                            }
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.QUEUED)
                        {
                        nextReadCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        nanoTimeReadCacheValid = System.nanoTime();
                        // Re-read the next time around
                        queueRead = true;
                        setActionFlag = true;
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.VALID || readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                        {
                        nextReadCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        nanoTimeReadCacheValid = System.nanoTime();
                        // Re-read the next time around
                        queueRead = true;
                        setActionFlag = true;
                        }
                    }
    
                else if (writeCacheStatus == WRITE_CACHE_STATUS.DIRTY)
                    {
                    // Queue the write cache for writing to the module
                    nextWriteCacheStatus = WRITE_CACHE_STATUS.QUEUED;
                    setActionFlag = true;      // request an I2C write
                    queueFullWrite = true;
    
                    // What's the impact on the read cache? 
                    if (readCacheStatus == READ_CACHE_STATUS.QUEUED || readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                        {
                        // We just completed a read cycle. Data is valid, for the moment.
                        // But don't re-issue the reread request
                        nextReadCacheStatus = READ_CACHE_STATUS.VALID;
                        nanoTimeReadCacheValid = System.nanoTime();
                        }
                    else if (readCacheStatus == READ_CACHE_STATUS.VALID)
                        {
                        // We've gone a cycle w/o reading. What's there is out of date.
                        nextReadCacheStatus = READ_CACHE_STATUS.IDLE;
                        }
                    }
    
                // Deal with any heartbeats which might be necessary to keep the I2C
                // device alive and operational.
                if (setActionFlag || queueFullWrite || queueRead)
                    {
                    // We're about to communicate right now, so reset the heart beat.
                    // Note that we reset() *before* we talk to the device so as to do 
                    // conservative timing accounting
                    timeSinceLastHeartbeat.reset();
                    }
                else
                    {
                    // We're not otherwise communicating during this callback.
                    // Do we need to do a heartbeat?
                    if (msHeartbeatInterval > 0 && timeSinceLastHeartbeat.time()*1000 >= msHeartbeatInterval)
                        {
                        // Yes, we need to send a heartbeat. What can we do?
                        }
                    }

                // Read, set action flag and / or queue to module as requested
                if (queueRead)
                    i2cDevice.readI2cCacheFromModule();;

                if (setActionFlag)
                    i2cDevice.setI2cPortActionFlag();

                if (setActionFlag && !queueFullWrite)
                    i2cDevice.writeI2cPortFlagOnlyToModule();
    
                if (queueFullWrite)
                    i2cDevice.writeI2cCacheToModule();
                
                // Update our state machine
                readCacheStatus  = nextReadCacheStatus;
                writeCacheStatus = nextWriteCacheStatus;
                
                // Tell any readers or writers that statuses have changed
                lock.notifyAll();
                }
            }
        }
    }
