package org.swerverobotics.library.internal;

import android.util.Log;

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

    private final Callback      callback;                   // the callback object on which we actually receive callbacks
    private       Thread        callbackThread;             // the thread on which we observe our callbacks to be made
    private       int           hardwareCycleCount;         // number of callbacks that we've received
    private       boolean       loggingEnabled;             // whether we are to log to Logcat or not
    private       String        loggingTag;                 // what we annotate our logging with
    private final ElapsedTime   timeSinceLastHeartbeat;     // keeps track of our need for doing heartbeats
    private       int           msHeartbeatInterval;        // time between heartbeats; zero is none necessary
    private       boolean       heartBeatUsingRead;         // true if we are to read for heartbeats, false if we are to write
    
    private final byte[]        readCache;                  // the buffer into which reads are retrieved
    private final byte[]        writeCache;                 // the buffer that we write from 
    private static final int    dibCacheOverhead = 4;       // this many bytes at start of writeCache are system overhead
    private static final int    ibActionFlag = 31;          // index of the action flag in our write cache
    private final Lock          readCacheLock;              // lock we must hold to look at readCache
    private final Lock          writeCacheLock;             // lock we must old to look at writeCache

    private final Object        lock = new Object();        // the lock we use to synchronize concurrent callers as well as the portIsReady() callback

    private RegWindow           regWindowRead;              // the set of registers to look at when we are in read mode. May be null, indicating no read needed
    private long                nanoTimeReadCacheValid;     // the time on the System.nanoTime() clock at which the read cache was last set as valid
    private READ_CACHE_STATUS   readCacheStatus;            // what we know about the contents of readCache
    private WRITE_CACHE_STATUS  writeCacheStatus;           // what we know about the contents of writeCache
    private int                 iregWriteFirst;             // when writeCacheStatus is DIRTY, this is where we want to write
    private int                 cregWrite;

    private enum READ_CACHE_STATUS
        {
        IDLE,               // the read cache is quiescent; it doesn't contain valid data
        AWAITINGREADMODE,   // a request to switch to read mode has been made
        QUEUED,             // an I2C read has been queued, but we've not yet seen valid data
        VALIDQUEUED,        // read cache has valid data AND a read has been queued 
        VALIDWRITING;       // read cache has valid data but the device is currently in write mode
        
        boolean isValid()
            {
            return this==VALIDQUEUED || this==VALIDWRITING;
            }
        boolean isQueued()
            {
            return this==QUEUED || this==VALIDQUEUED;
            }
        }

    private enum WRITE_CACHE_STATUS 
        {
        IDLE,               // write cache is quiescent
        DIRTY,              // write cache has changed bits that need to be pushed to module
        QUEUED,             // write cache is currently being written to module, not yet returned
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
    public I2cDeviceClient(II2cDevice i2cDevice, int i2cAddr8Bit, RegWindow initialRegisterWindow)
        {
        this.i2cDevice              = i2cDevice;
        this.callback               = new Callback();
        this.callbackThread         = null;
        this.hardwareCycleCount     = 0;
        this.loggingEnabled         = false;
        this.loggingTag             = null;
        this.timeSinceLastHeartbeat = new ElapsedTime();
        this.timeSinceLastHeartbeat.reset();
        this.msHeartbeatInterval    = 0;
        this.heartBeatUsingRead     = true;

        this.readCache      = this.i2cDevice.getI2cReadCache();
        this.readCacheLock  = this.i2cDevice.getI2cReadCacheLock();
        this.writeCache     = this.i2cDevice.getI2cWriteCache();
        this.writeCacheLock = this.i2cDevice.getI2cWriteCacheLock();
        
        this.regWindowRead  = initialRegisterWindow;

        this.nanoTimeReadCacheValid = 0;
        this.readCacheStatus  = READ_CACHE_STATUS.IDLE;
        this.writeCacheStatus = WRITE_CACHE_STATUS.IDLE;
        
        this.i2cDevice.setI2cAddr(i2cAddr8Bit);
        
        this.i2cDevice.registerForI2cPortReadyCallback(this.callback);
        }
    
    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------
    
    public String getDeviceName()
        {
        return this.i2cDevice.getDeviceName();  
        }
    
    public String getConnectionInfo()
        {
        return this.i2cDevice.getConnectionInfo();
        }
    
    public int getVersion()
        {
        return this.i2cDevice.getVersion();
        }

    public void close()
        {
        this.i2cDevice.close();
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we will read and read and read again on every hardware cycle 
     */
    public void setReadWindow(RegWindow window)
        {
        synchronized (this.lock)
            {
            if (this.regWindowRead ==null || !this.regWindowRead.equals(window))
                {
                // Remember the new window
                this.regWindowRead = window;
                
                // Any reads we currently have or are perhaps currently 
                // in flight are junk.
                this.readCacheStatus = READ_CACHE_STATUS.IDLE;
                }
            }
        }

    /**
     * Return the current register window.
     */
    public RegWindow getReadWindow()
        {
        synchronized (this.lock)
            {
            return this.regWindowRead;
            }
        }

    /**
     * Ensure that the current register window covers the indicated set of registers.
     */
    public void ensureReadWindow(RegWindow windowNeeded, RegWindow windowToSet)
        {
        synchronized (this.lock)
            {
            if (this.regWindowRead == null || !this.regWindowRead.contains(windowNeeded))
                {
                setReadWindow(windowToSet);
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
                if (!this.regWindowRead.contains(ireg, creg))
                    {
                    throw new IllegalArgumentException(String.format("read request (%d,%d) outside of read register window", ireg, creg));
                    }

                // Wait until we fill up with data
                for (;;)
                    {
                    if (this.readCacheStatus.isValid())
                        break;
                    //
                    this.lock.wait();
                    }

                // Extract the data and return!
                this.readCacheLock.lockInterruptibly();
                try
                    {
                    int ibFirst = ireg - this.regWindowRead.getIregFirst() + dibCacheOverhead;
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
            Util.handleCapturedInterrupt();

            // Can't return (no data to return!) so we must throw
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
                this.iregWriteFirst = ireg;
                this.cregWrite      = data.length;
                
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
            Util.handleCapturedInterrupt();
            }
        }
    
    public Thread getCallbackThread()
        {
        synchronized (this.lock)
            {
            return this.callbackThread;
            }
        }
    
    public int getI2cCycleCount()
        {
        synchronized (this.lock)
            {
            return this.hardwareCycleCount;
            }
        }
    
    public void setLoggingEnabled(boolean enabled)
        {
        synchronized (this.lock)
            {
            this.loggingEnabled = enabled;
            }
        }
    
    public int getHeartbeatInterval()
        {
        synchronized (this.lock)
            {
            return this.msHeartbeatInterval;
            }
        }
    
    public void setHeartbeatRead(int ms)
        {
        ms = Math.max(0, ms);
        synchronized (this.lock)
            {
            this.msHeartbeatInterval = ms;
            this.heartBeatUsingRead  = true;
            }
        }
    
    public void setHeartbeatWrite(int ms)
        {
        ms = Math.max(0, ms);
        synchronized (this.lock)
            {
            this.msHeartbeatInterval = ms;
            this.heartBeatUsingRead  = false;
            }
        }
    
    private void log(int verbosity, String message)
        {
        synchronized (this)
            {
            if (this.loggingTag == null)
                this.loggingTag = String.format("I2cDeviceClient(%s)", i2cDevice.getDeviceName());
            }
        
        switch (verbosity)
            {
        case Log.VERBOSE:   Log.v(loggingTag, message); break;
        case Log.DEBUG:     Log.d(loggingTag, message); break;
        case Log.INFO:      Log.i(loggingTag, message); break;
        case Log.WARN:      Log.w(loggingTag, message); break;
        case Log.ERROR:     Log.e(loggingTag, message); break;
        case Log.ASSERT:    Log.wtf(loggingTag, message); break;
            }
        }
    private void log(int verbosity, String format, Object... args)
        {
        log(verbosity, String.format(format, args));
        }
    
    private void clearActionFlag()
        {
        try {
            this.writeCacheLock.lock();
            this.writeCache[ibActionFlag] = 0;
            }
        finally
            {
            this.writeCacheLock.unlock();
            }
        }
    
    private class Callback implements I2cController.I2cPortReadyCallback
        {
        boolean setActionFlag     = false;
        boolean queueFullWrite    = false;
        boolean queueRead         = false;
        boolean heartbeatRequired = false;
        boolean enabledReadMode   = false;
        boolean enabledWriteMode  = false;

        READ_CACHE_STATUS  nextReadCacheStatus;
        WRITE_CACHE_STATUS nextWriteCacheStatus;
        
        void setEnableReadMode()
            {
            i2cDevice.enableI2cReadMode(regWindowRead.getIregFirst(), regWindowRead.getCreg());
            enabledReadMode = true;
            queueFullWrite = true;
            }

        @Override public void portIsReady(int port)
        // This is the callback from the device module indicating completion of previously requested work.
        // At the moment we are called, we are assured that the read buffer / write buffer for our port in the
        // USB device is not currently busy.
        //
        // We've got quite the little state machine here!
            {
            synchronized (lock)
                {
                //----------------------------------------------------------------------------------
                // Some preliminaries and bookkeeping
                
                if (callbackThread == null)
                    callbackThread = Thread.currentThread();
                else if (BuildConfig.DEBUG)
                    Assert.assertEquals(callbackThread.getId(), Thread.currentThread().getId());
                
                if (0 == hardwareCycleCount)
                    Thread.currentThread().setName(String.format("rw loop(%s)", i2cDevice.getDeviceName()));

                hardwareCycleCount++;

                setActionFlag     = false;
                queueFullWrite    = false;
                queueRead         = false;
                heartbeatRequired = (msHeartbeatInterval > 0 && timeSinceLastHeartbeat.time()*1000 >= msHeartbeatInterval);
                enabledReadMode   = false;
                enabledWriteMode  = false;
                
                nextReadCacheStatus  = readCacheStatus;
                nextWriteCacheStatus = writeCacheStatus;

                //----------------------------------------------------------------------------------
                // Handle the state machine 
                
                if (writeCacheStatus == WRITE_CACHE_STATUS.IDLE
                        || writeCacheStatus == WRITE_CACHE_STATUS.QUEUED
                        || readCacheStatus == READ_CACHE_STATUS.AWAITINGREADMODE)
                    {
                    // We don't have anything fresh to actually write. So we try to do reads
                    // if we can, generally.
                    
                    // If we just finished a write cycle, then our write cache is now idle
                    if (writeCacheStatus == WRITE_CACHE_STATUS.QUEUED)
                        nextWriteCacheStatus = WRITE_CACHE_STATUS.IDLE;
    
                    // Do state transitions on the read cache
                    switch (readCacheStatus)
                        {
                    case IDLE:
                        {
                        // Read something if we have something we've been asked to read
                        if (regWindowRead != null)
                            {
                            // Initiate switch to read mode.
                            // TODO: We might not actually need the SWITCHING state 
                            nextReadCacheStatus = READ_CACHE_STATUS.AWAITINGREADMODE;
                            setEnableReadMode();
                            setActionFlag = true;
    
                            // Read the read vs write mode back again asap.
                            queueRead = true;
                            }
                        break;
                        }
                    case AWAITINGREADMODE:
                        {
                        // We're trying to switch into read mode. Are we there yet?
                        if (i2cDevice.isI2cPortInReadMode())
                            {
                            // The port has switched to read mode. Start reading from it
                            nextReadCacheStatus = READ_CACHE_STATUS.QUEUED;
                            setActionFlag = true;
                            queueRead = true;
                            }
                        else
                            {
                            // Keep reading the read vs write mode back again
                            queueRead = true;
                            }
                        break;
                        }
                    case QUEUED:
                        {
                        // We queued a read last time, now it's here, so we're valid. We'll
                        // also queue another one
                        nextReadCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        nanoTimeReadCacheValid = System.nanoTime();

                        // Re-read the next time around
                        setActionFlag = true;
                        queueRead = true;
                        break;
                        }
                    case VALIDQUEUED:
                        {
                        // We were valid last cycle and we also queued a new read request.
                        // We'll do so again, so we stay in the same state.
                        nextReadCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;

                        // Queue another read
                        setActionFlag = true;
                        queueRead = true;
                        break;
                        }
                    case VALIDWRITING:
                        {
                        // We were valid last time but that cycle issued a write so we're no longer valid
                        nextReadCacheStatus = READ_CACHE_STATUS.IDLE;
                        break;    
                        }
                    /* end switch */
                        }
                    }
    
                else switch (writeCacheStatus)
                    {
                case DIRTY:
                    {
                    // We've got something fresh to write. We have to here do the enable,
                    // as we must defer until now in order that we don't stomp on other uses of
                    // those first four bytes in the write cache (a READ_CACHE_STATUS.SWITCHING 
                    // might be in flight with its attendant enableI2cReadMode()). But the payload 
                    // data has already been written to the write cache as those latter
                    // bytes are only used for writing the data; they are not shared with other uses.
                    i2cDevice.enableI2cWriteMode(iregWriteFirst, cregWrite);
                    enabledWriteMode = true;

                    // Queue the write cache for writing to the module
                    // Don't actually issue the write action, though
                    nextWriteCacheStatus = WRITE_CACHE_STATUS.QUEUED;
                    setActionFlag = true;
                    queueFullWrite = true;
                    queueRead = true;       // for mode status

                    // What's the impact on the read cache? 
                    switch (readCacheStatus)
                        {
                    case IDLE:
                    case AWAITINGREADMODE:
                        break;
                    case QUEUED:
                    case VALIDQUEUED:
                        {
                        // We just completed a queued read cycle. Data is valid, for the moment.
                        // But don't re-issue the reread request
                        nextReadCacheStatus = READ_CACHE_STATUS.VALIDWRITING;
                        nanoTimeReadCacheValid = System.nanoTime();
                        break;
                        }
                    case VALIDWRITING:
                        {
                        // We've gone a cycle w/o queueing a read. What's there is out of date.
                        nextReadCacheStatus = READ_CACHE_STATUS.IDLE;
                        break;
                        }
                    /* end switch */
                        }

                    if (setActionFlag)
                        {
                        // We're about to communicate right now, so reset the heart beat.
                        // Note that we reset() *before* we talk to the device so as to do 
                        // conservative timing accounting
                        timeSinceLastHeartbeat.reset();
                        }

                    if (!setActionFlag && heartbeatRequired && !heartBeatUsingRead)
                        {
                        // Rewrite what we previously wrote
                        setActionFlag  = true;
                        queueFullWrite = true;
                        }

                    break;
                    }
                /* end switch */
                    }

                //----------------------------------------------------------------------------------
                // Do logging 

                final int moreVerbose = Log.DEBUG;
                final int moreQuiet   = Log.INFO;

                if (loggingEnabled)
                    {
                    boolean anyQuietLoggingToDo = (readCacheStatus != nextReadCacheStatus
                            || writeCacheStatus != nextWriteCacheStatus
                            || enabledReadMode || enabledWriteMode);
    
                    log(anyQuietLoggingToDo ? moreQuiet : moreVerbose, "-----");
                    log(moreVerbose, "hardware cycle: %d | setActionFlag: %s", hardwareCycleCount, setActionFlag ? "true" : "false");
                    log(moreVerbose, "mode: %s", i2cDevice.isI2cPortInReadMode() ? "read" : "write");
                    
                    if (readCacheStatus != nextReadCacheStatus)
                        log(moreQuiet, "READ." + readCacheStatus.toString() + "->" + nextReadCacheStatus.toString());
                    if (enabledReadMode)
                        log(moreQuiet, "setReadMode(0x%02x,%d)", regWindowRead.getIregFirst(), regWindowRead.getCreg());
                    if (writeCacheStatus != nextWriteCacheStatus)
                        log(moreQuiet, "WRITE." + writeCacheStatus.toString() + "->" + nextWriteCacheStatus.toString());
                    if (enabledWriteMode)
                        log(moreQuiet, "setWriteMode(0x%02x,%d)", iregWriteFirst, cregWrite);
                    }

                //----------------------------------------------------------------------------------
                // Read, set action flag and / or queue to module as requested
                
                if (queueRead)
                    i2cDevice.readI2cCacheFromModule();;

                if (setActionFlag)
                    i2cDevice.setI2cPortActionFlag();
                else
                    clearActionFlag();

                if (setActionFlag && !queueFullWrite)
                    i2cDevice.writeI2cPortFlagOnlyToModule();
                else if (queueFullWrite)
                    i2cDevice.writeI2cCacheToModule();

                //----------------------------------------------------------------------------------
                // Update our state machine
                
                readCacheStatus  = nextReadCacheStatus;
                writeCacheStatus = nextWriteCacheStatus;

                lock.notifyAll();
                }
            }
        }
    }
