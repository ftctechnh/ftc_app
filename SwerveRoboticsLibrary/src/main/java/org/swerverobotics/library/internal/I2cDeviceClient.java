package org.swerverobotics.library.internal;

import android.util.Log;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

import static junit.framework.Assert.*;
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
        IDLE,                 // the read cache is quiescent; it doesn't contain valid data
        SWITCHINGTOREADMODE,  // a request to switch to read mode has been made
        QUEUED,               // an I2C read has been queued, but we've not yet seen valid data
        VALID,                // a transient intermediate state only used in the state machine logic (never set when lock is released)
        VALIDQUEUED;          // read cache has valid data AND a read has been queued

        boolean isValid()
            {
            return this==VALIDQUEUED || this==VALID;
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
            if (this.regWindowRead == null || !this.regWindowRead.equals(window))
                {
                // Remember the new window
                this.regWindowRead = window;
                
                // Any reads we currently have or are perhaps currently in flight are junk.
                this.readCacheStatus = READ_CACHE_STATUS.IDLE;      // TO DO: does this cause problems?
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
                if (this.regWindowRead == null)
                    throw new IllegalArgumentException("read request with no register window set");
                if (!this.regWindowRead.contains(ireg, creg))
                    throw new IllegalArgumentException(String.format("read request (%d,%d) outside of read register window", ireg, creg));

                // Wait until the read cache is valid and the write cache isn't busy.
                // The latter honors the visibility semantic we intend to portray, namely
                // that issuing a read after a write has been issued will see the state AFTER
                // the write has had a chance to take effect.
                while (!this.readCacheStatus.isValid() && this.writeCacheStatus != WRITE_CACHE_STATUS.IDLE)
                    {
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
     * Write data to a set of registers, beginning with the one indicated. The data will be
     * written to the I2C device as expeditiously as possible, but that is not guaranteed
     * to have occurred by the time this method returns.
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
                this.callback.updateStateMachines(UPDATE_STATE_MACHINE.FROM_USER_WRITE);
                // Wait until the write has been issued
                // TODO: We SHOULD be able to do without this
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
    
    /* Disable, temporarily, to allow more thinking as to how best to model heartbeats

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
    */

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
        //------------------------------------------------------------------------------------------
        // State, kept in member variables so we can divy the updateStateMachines() logic
        // across multiple function
        //------------------------------------------------------------------------------------------

        boolean setActionFlag     = false;
        boolean queueFullWrite    = false;
        boolean queueRead         = false;
        boolean heartbeatRequired = false;
        boolean enabledReadMode   = false;
        boolean enabledWriteMode  = false;

        READ_CACHE_STATUS  prevReadCacheStatus  = READ_CACHE_STATUS.IDLE;
        WRITE_CACHE_STATUS prevWriteCacheStatus = WRITE_CACHE_STATUS.IDLE;
        MODE_CACHE_STATUS  prevModeCacheStatus  = MODE_CACHE_STATUS.IDLE;

        //------------------------------------------------------------------------------------------
        // Main entry point
        //------------------------------------------------------------------------------------------

        @Override public void portIsReady(int port)
        // This is the callback from the device module indicating completion of previously requested work.
        // At the moment we are called, we are assured that the read buffer / write buffer for our port in the
        // USB device is not currently busy.
            {
            updateStateMachines(UPDATE_STATE_MACHINE.FROM_CALLBACK);
            }

        //------------------------------------------------------------------------------------------
        // Update logic
        //------------------------------------------------------------------------------------------

        void startSwitchingToReadMode()
            {
            readCacheStatus = READ_CACHE_STATUS.SWITCHINGTOREADMODE;
            i2cDevice.enableI2cReadMode(regWindowRead.getIregFirst(), regWindowRead.getCreg());
            enabledReadMode = true;

            setActionFlag   = true;     // causes an I2C read
            queueFullWrite  = true;     // for the mode bytes

            assertTrue(BuildConfig.DEBUG && modeCacheStatus == MODE_CACHE_STATUS.IDLE);
            modeCacheStatus = MODE_CACHE_STATUS.DIRTY;
            }

        void issueWrite()
            {
            writeCacheStatus = WRITE_CACHE_STATUS.QUEUED;
            i2cDevice.enableI2cWriteMode(iregWriteFirst, cregWrite);
            enabledWriteMode = true;

            setActionFlag  = true;      // causes the I2C write to happen
            queueFullWrite = true;      // for the mode bytes and the payload

            assertTrue(BuildConfig.DEBUG && modeCacheStatus == MODE_CACHE_STATUS.IDLE);
            modeCacheStatus = MODE_CACHE_STATUS.DIRTY;
            }

        void dealWithHeartbeat()
        // This is not yet used; heartbeats are temporariliy disabled
            {
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

        void updateStateMachines(UPDATE_STATE_MACHINE caller)
        // We've got quite the little state machine here!
            {
            synchronized (theLock)
                {
                //----------------------------------------------------------------------------------
                // If we're calling from other than the callback (in which we *know* the port is
                // ready), we need to check whether things are currently busy. We defer until
                // later if they are.
                if (caller==UPDATE_STATE_MACHINE.FROM_USER_WRITE)
                    {
                    if (!i2cDevice.isI2cPortReady() || callbackThread==null)
                        return;

                    // Optimized calling from user mode is not yet implemented
                    return;
                    }

                //----------------------------------------------------------------------------------
                // Some ancillary bookkeeping

                if (caller == UPDATE_STATE_MACHINE.FROM_CALLBACK)
                    {
                    // Capture the current callback thread if we haven't already
                    if (callbackThread == null)
                        callbackThread = Thread.currentThread();
                    else
                        assertTrue(BuildConfig.DEBUG && callbackThread.getId() == Thread.currentThread().getId());

                    // Set the thread name to make the system more debuggable
                    if (0 == hardwareCycleCount)
                        Thread.currentThread().setName(String.format("RWLoop(%s)", i2cDevice.getDeviceName()));

                    // Update cycle statistics
                    hardwareCycleCount++;
                    }

                //----------------------------------------------------------------------------------
                // Initialize state for managing state transition

                setActionFlag     = false;
                queueFullWrite    = false;
                queueRead         = false;
                heartbeatRequired = (msHeartbeatInterval > 0 && timeSinceLastHeartbeat.time()*1000 >= msHeartbeatInterval);
                enabledReadMode   = false;
                enabledWriteMode  = false;
                
                prevReadCacheStatus  = readCacheStatus;
                prevWriteCacheStatus = writeCacheStatus;
                prevModeCacheStatus  = modeCacheStatus;

                //----------------------------------------------------------------------------------
                // Handle the state machine

                if (caller==UPDATE_STATE_MACHINE.FROM_CALLBACK)
                    {
                    //--------------------------------------------------------------------------
                    // Deal with the fact that we've completed any previous queueing operation

                    if (modeCacheStatus == MODE_CACHE_STATUS.QUEUED)
                        modeCacheStatus = MODE_CACHE_STATUS.IDLE;

                    if (readCacheStatus == READ_CACHE_STATUS.QUEUED || readCacheStatus == READ_CACHE_STATUS.VALIDQUEUED)
                        {
                        readCacheStatus = READ_CACHE_STATUS.VALID;
                        nanoTimeReadCacheValid = System.nanoTime();
                        }

                    if (writeCacheStatus == WRITE_CACHE_STATUS.QUEUED)
                        writeCacheStatus = WRITE_CACHE_STATUS.IDLE;

                    //--------------------------------------------------------------------------
                    // That limits the number of states the caches can now be in

                    assertTrue(BuildConfig.DEBUG && (readCacheStatus==READ_CACHE_STATUS.IDLE||readCacheStatus==READ_CACHE_STATUS.SWITCHINGTOREADMODE||readCacheStatus==READ_CACHE_STATUS.VALID));
                    assertTrue(BuildConfig.DEBUG && (writeCacheStatus==WRITE_CACHE_STATUS.IDLE||writeCacheStatus==WRITE_CACHE_STATUS.DIRTY));

                    //--------------------------------------------------------------------------
                    // If there's a write request pending, and it's ok to issue the write, do so

                    if (writeCacheStatus == WRITE_CACHE_STATUS.DIRTY && readCacheStatus != READ_CACHE_STATUS.SWITCHINGTOREADMODE)
                        {
                        issueWrite();

                        // Our ordering rules are that any reads after a write have to wait until
                        // the write is actually sent to the hardware, so anything we've read before is junk.
                        // Note that there's an analogous check in read().
                        readCacheStatus = READ_CACHE_STATUS.IDLE;
                        }

                    //--------------------------------------------------------------------------
                    // Initiate reading if we should

                    else if (readCacheStatus == READ_CACHE_STATUS.IDLE)
                        {
                        if (regWindowRead != null)
                            {
                            startSwitchingToReadMode();
                            }
                        }

                    //--------------------------------------------------------------------------
                    // Complete a read mode switch if there is one

                    else if (readCacheStatus == READ_CACHE_STATUS.SWITCHINGTOREADMODE)
                        {
                        // We're trying to switch into read mode. Are we there yet?
                        if (i2cDevice.isI2cPortInReadMode())
                            {
                            // TODO: Is the data actually valid now? Or do we really have to issue a read as we're doing here?
                            readCacheStatus = READ_CACHE_STATUS.QUEUED;
                            setActionFlag = true;       // actually do an I2C read
                            }
                        }

                    //--------------------------------------------------------------------------
                    // Reissue any previous read

                    else if (readCacheStatus == READ_CACHE_STATUS.VALID)
                        {
                        readCacheStatus = READ_CACHE_STATUS.VALIDQUEUED;
                        setActionFlag = true;           // actually do an I2C read
                        }

                    //--------------------------------------------------------------------------
                    // In all cases, we want to read the latest from the interface module.
                    // That may or may not involve doing an I2C read depending on other settings.

                    queueRead = true;
                    }

                else if (caller==UPDATE_STATE_MACHINE.FROM_USER_WRITE)
                    {
                    // This is not yet implemented
                    }

                //----------------------------------------------------------------------------------
                // Read, set action flag and / or queue to module as requested
                
                if (setActionFlag)
                    i2cDevice.setI2cPortActionFlag();
                else
                    clearActionFlag();

                if (setActionFlag && !queueFullWrite)
                    {
                    i2cDevice.writeI2cPortFlagOnlyToModule();
                    }
                else if (queueFullWrite)
                    {
                    i2cDevice.writeI2cCacheToModule();
                    //
                    if (modeCacheStatus == MODE_CACHE_STATUS.DIRTY)
                        modeCacheStatus =  MODE_CACHE_STATUS.QUEUED;
                    }

                // Queue a read after queuing any write for a bit of paranoia: if we're mode switching
                // to write, we want that write to go out first, THEN read the mode status. It probably
                // would anyway, but why not...
                if (queueRead)
                    {
                    i2cDevice.readI2cCacheFromModule();
                    }

                //----------------------------------------------------------------------------------
                // Do logging

                if (loggingEnabled)
                    {
                    StringBuilder message = new StringBuilder();

                    switch (caller)
                        {
                        case FROM_CALLBACK:     message.append(String.format("cyc %d", hardwareCycleCount)); break;
                        case FROM_USER_WRITE:   message.append(String.format("usr write")); break;
                        }
                    if (setActionFlag)                            message.append("|flag");
                    if (setActionFlag && !queueFullWrite)         message.append("|f");
                    else if (queueFullWrite)                      message.append("|w");
                    if (queueRead)                                message.append("|r");
                    if (readCacheStatus != prevReadCacheStatus)   message.append("| R." + prevReadCacheStatus.toString() + "->" + readCacheStatus.toString());
                    if (writeCacheStatus != prevWriteCacheStatus) message.append("| W." + prevWriteCacheStatus.toString() + "->" + writeCacheStatus.toString());
                    if (modeCacheStatus != prevModeCacheStatus)   message.append("| M." + prevModeCacheStatus.toString() + "->" + modeCacheStatus.toString());
                    if (enabledWriteMode)                         message.append(String.format("| setWrite(0x%02x,%d)", iregWriteFirst, cregWrite));
                    if (enabledReadMode)                          message.append(String.format("| setRead(0x%02x,%d)", regWindowRead.getIregFirst(), regWindowRead.getCreg()));

                    log(Log.DEBUG, message.toString());
                    }

                //----------------------------------------------------------------------------------
                // Notify anyone blocked in read() or write()
                theLock.notifyAll();
                }
            }
        }
    }
