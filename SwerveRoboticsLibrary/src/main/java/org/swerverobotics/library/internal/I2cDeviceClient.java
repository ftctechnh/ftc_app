package org.swerverobotics.library.internal;

import android.os.Build;
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

    private final Object        theLock = new Object();        // the lock we use to synchronize concurrent callers as well as the portIsReady() callback

    private RegWindow           regWindowRead;              // the set of registers to look at when we are in read mode. May be null, indicating no read needed
    private long                nanoTimeReadCacheValid;     // the time on the System.nanoTime() clock at which the read cache was last set as valid
    private READ_CACHE_STATUS   readCacheStatus;            // what we know about the contents of readCache
    private WRITE_CACHE_STATUS  writeCacheStatus;           // what we know about the (payload) contents of writeCache
    private MODE_CACHE_STATUS   modeCacheStatus;            // what we know about the first four bytes of writeCache (mostly a debugging aid)
    private int                 iregWriteFirst;             // when writeCacheStatus is DIRTY, this is where we want to write
    private int                 cregWrite;

    /** Keeps track of what we know about about the state of 'readCache' */
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

    /** Keeps track about what we know about the state of 'writeCache' */
    private enum WRITE_CACHE_STATUS
        {
        IDLE,               // write cache is quiescent
        DIRTY,              // write cache has changed bits that need to be pushed to module
        QUEUED,             // write cache is currently being written to module, not yet returned
        }

    /** Keeps track about what we know about the state of the first four bytes of the
     * write cache, which are used for requesting a mode switch.
     */
    private enum MODE_CACHE_STATUS
        {
        IDLE,               // mode byte are quiesent
        DIRTY,              // mode bytes have changed, and need to be pushed to the module
        QUEUED,             // mode bytes have been queued to the module, but not yet returned.
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
        this.modeCacheStatus  = MODE_CACHE_STATUS.IDLE;
        
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
        synchronized (this.theLock)
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
        synchronized (this.theLock)
            {
            return this.regWindowRead;
            }
        }

    /**
     * Ensure that the current register window covers the indicated set of registers.
     */
    public void ensureReadWindow(RegWindow windowNeeded, RegWindow windowToSet)
        {
        synchronized (this.theLock)
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
            synchronized (this.theLock)
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
                    this.theLock.wait();
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
     * Write data to a set of registers, beginning with the one indicated. This method blocks
     * until the write has actually been issued to the device.
     */
    public void write(int ireg, byte[] data)
        {
        try
            {
            synchronized (this.theLock)
                {
                // Wait until we can write to the write cache
                while (this.writeCacheStatus != WRITE_CACHE_STATUS.IDLE)
                    {
                    this.theLock.wait();
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

                // Send out the data proactively (this is optional, but more efficient).
                // Not yet working; commented out for now
                // this.callback.updateStateMachines(UPDATE_STATE_MACHINE.FROM_USER_WRITE);

                // Wait until the write has been issued
                while (this.writeCacheStatus != WRITE_CACHE_STATUS.IDLE)
                    {
                    this.theLock.wait();
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
        synchronized (this.theLock)
            {
            return this.callbackThread;
            }
        }
    
    public int getI2cCycleCount()
        {
        synchronized (this.theLock)
            {
            return this.hardwareCycleCount;
            }
        }
    
    public void setLogging(boolean enabled)
        {
        synchronized (this.theLock)
            {
            this.loggingEnabled = enabled;
            }
        }

    public void setLoggingTag(String loggingTag)
        {
        synchronized (this.theLock)
            {
            this.loggingTag = loggingTag;
            }
        }
    
    public int getHeartbeatInterval()
        {
        synchronized (this.theLock)
            {
            return this.msHeartbeatInterval;
            }
        }
    
    public void setHeartbeatRead(int ms)
        {
        ms = Math.max(0, ms);
        synchronized (this.theLock)
            {
            this.msHeartbeatInterval = ms;
            this.heartBeatUsingRead  = true;
            }
        }
    
    public void setHeartbeatWrite(int ms)
        {
        ms = Math.max(0, ms);
        synchronized (this.theLock)
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

    /** Flag to distinguish state machine updates that are caused by the callback vs state  
      * machine updates that are due to application-initiated writes */
    private enum UPDATE_STATE_MACHINE
        {
        FROM_CALLBACK,
        FROM_USER_WRITE
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
        MODE_CACHE_STATUS  nextModeCacheStatus;
        
        @Override public void portIsReady(int port)
        // This is the callback from the device module indicating completion of previously requested work.
        // At the moment we are called, we are assured that the read buffer / write buffer for our port in the
        // USB device is not currently busy.
            {
            updateStateMachines(UPDATE_STATE_MACHINE.FROM_CALLBACK);
            }

        void updateStateMachines(UPDATE_STATE_MACHINE caller)
        // We've got quite the little state machine here!
            {
            synchronized (theLock)
                {
                //----------------------------------------------------------------------------------
                // If we're calling from other than the callback (in which we *know* the port is
                // ready), we need to check whether things are currently busy. We defer until
                // later if they are.
                if (caller==UPDATE_STATE_MACHINE.FROM_USER_WRITE && !i2cDevice.isI2cPortReady())
                    return;

                //----------------------------------------------------------------------------------
                // Some preliminaries and bookkeeping
                if (caller == UPDATE_STATE_MACHINE.FROM_CALLBACK)
                    {
                    if (callbackThread == null)
                        callbackThread = Thread.currentThread();
                    else if (BuildConfig.DEBUG)
                        Assert.assertEquals(callbackThread.getId(), Thread.currentThread().getId());

                    if (0 == hardwareCycleCount)
                        Thread.currentThread().setName(String.format("rw loop(%s)", i2cDevice.getDeviceName()));
                    hardwareCycleCount++;
                    }

                setActionFlag     = false;
                queueFullWrite    = false;
                queueRead         = false;
                heartbeatRequired = (msHeartbeatInterval > 0 && timeSinceLastHeartbeat.time()*1000 >= msHeartbeatInterval);
                enabledReadMode   = false;
                enabledWriteMode  = false;
                
                nextReadCacheStatus  = readCacheStatus;
                nextWriteCacheStatus = writeCacheStatus;
                nextModeCacheStatus  = modeCacheStatus;

                //----------------------------------------------------------------------------------
                // Handle the state machine 
                
                if ((writeCacheStatus == WRITE_CACHE_STATUS.IDLE
                        || writeCacheStatus == WRITE_CACHE_STATUS.QUEUED
                        || readCacheStatus == READ_CACHE_STATUS.AWAITINGREADMODE)

                    && caller==UPDATE_STATE_MACHINE.FROM_CALLBACK)
                    {
                    // We don't have anything fresh to actually write. So we try to do reads
                    // if we can, generally.
                    
                    // If we just finished a write cycle, then our write cache is now idle
                    if (writeCacheStatus == WRITE_CACHE_STATUS.QUEUED) nextWriteCacheStatus = WRITE_CACHE_STATUS.IDLE;
                    if (modeCacheStatus  == MODE_CACHE_STATUS.QUEUED)  modeCacheStatus      = MODE_CACHE_STATUS.IDLE;
    
                    // Do state transitions on the read cache
                    switch (readCacheStatus)
                        {
                    case IDLE:
                        {
                        // Read something if we have something we've been asked to read
                        if (regWindowRead != null)
                            {
                            // Initiate switch to read mode.
                            // TODO: We might not actually need the AWAITINGREADMODE state
                            nextReadCacheStatus = READ_CACHE_STATUS.AWAITINGREADMODE;
                            i2cDevice.enableI2cReadMode(regWindowRead.getIregFirst(), regWindowRead.getCreg());
                            enabledReadMode = true;
                            queueFullWrite = true;
                            setActionFlag = true;
    
                            if (BuildConfig.DEBUG) Assert.assertTrue(modeCacheStatus == MODE_CACHE_STATUS.IDLE);
                            nextModeCacheStatus = MODE_CACHE_STATUS.DIRTY;

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
                            // Keep reading the read vs write mode status
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
                        // We were valid last cycle and we also queued a new read request,
                        // which has now arrived.  We'll do so again, so we stay in the same state.
                        nextReadCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        nanoTimeReadCacheValid = System.nanoTime();

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
    
                // We can't start writing if we have an outstanding read transition.
                // We repeat that test here as it covers the FROM_USER_WRITE case.
                else if (writeCacheStatus==WRITE_CACHE_STATUS.DIRTY
                         && readCacheStatus != READ_CACHE_STATUS.AWAITINGREADMODE
                        /* && caller is either case */)
                    {
                    // We've got something fresh to write. We have to here do the enable,
                    // as we must defer until now in order that we don't stomp on other uses of
                    // those first four bytes in the write cache (a READ_CACHE_STATUS.AWAITINGREADMODE
                    // might be in flight with its attendant enableI2cReadMode()). But the payload 
                    // data has already been written to the write cache as those latter
                    // bytes are only used for writing the data; they are not shared with other uses.
                    i2cDevice.enableI2cWriteMode(iregWriteFirst, cregWrite);
                    enabledWriteMode = true;

                    if (BuildConfig.DEBUG) Assert.assertTrue(modeCacheStatus == MODE_CACHE_STATUS.IDLE);
                    nextModeCacheStatus = MODE_CACHE_STATUS.DIRTY;

                    // Queue the write cache for writing to the module
                    // Don't actually issue the write action, though
                    nextWriteCacheStatus = WRITE_CACHE_STATUS.QUEUED;
                    setActionFlag = true;
                    queueFullWrite = true;
                    queueRead = true;       // for mode status

                    if (caller==UPDATE_STATE_MACHINE.FROM_CALLBACK)
                        {
                        // What's the impact on the read cache? If we're demand-writing, not
                        // from the callback, then the answer is 'nothing'.
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
                        // TODO: is this really the best idea?
                        setActionFlag  = true;
                        queueFullWrite = true;
                        }
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
                    if (caller==UPDATE_STATE_MACHINE.FROM_CALLBACK)
                        log(moreVerbose, "hardware cycle: %d | setActionFlag: %s", hardwareCycleCount, setActionFlag ? "true" : "false");
                    else
                        log(moreVerbose, "user write | setActionFlag: %s", setActionFlag ? "true" : "false");
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
                
                if (setActionFlag)
                    i2cDevice.setI2cPortActionFlag();
                else
                    clearActionFlag();

                if (setActionFlag && !queueFullWrite)
                    i2cDevice.writeI2cPortFlagOnlyToModule();
                else if (queueFullWrite)
                    {
                    i2cDevice.writeI2cCacheToModule();
                    if (nextModeCacheStatus==MODE_CACHE_STATUS.DIRTY)
                        nextModeCacheStatus=MODE_CACHE_STATUS.QUEUED;
                    }

                // Do read after write for a bit of paranoia: if we're mode switching
                // to write, we want that write to go out first, THEN read the mode status.
                if (queueRead)
                    i2cDevice.readI2cCacheFromModule();;

                //----------------------------------------------------------------------------------
                // Update our state machine
                
                readCacheStatus  = nextReadCacheStatus;
                writeCacheStatus = nextWriteCacheStatus;
                modeCacheStatus  = nextModeCacheStatus;

                theLock.notifyAll();
                }
            }
        }
    }
