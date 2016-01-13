package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;

/**
 * II2cDeviceClient is the public interface to a utility class that makes it easier to
 * use I2cDevice instances.
 *
 * <p>Having created an II2cDeviceClient instance, reads and writes are performed by calling
 * {@link #read8(int) read8()} and {@link #write8(int, int) write8()} or
 * {@link #read(int, int) read()} and {@link #write(int, byte[]) write()} respectively. These
 * calls are synchronous; they block until their action is semantically complete. No attention
 * to 'read mode' or 'write mode' is required. Simply call reads and writes as you need them, and
 * the right thing happens.</p>
 *
 * <p>A word about optimizing reads. In I2cDevice, reads are accomplished by calling
 * {@link I2cDevice#enableI2cReadMode(int, int, int) enableI2cReadMode()} to indicate a set of
 * registers which are to be read from the I2C device. <em>Changing</em> that set of registers
 * is a relatively time consuming operation, on the order of several tens of milliseconds. If your
 * code wishes to read some registers at some times and then others at another, it behooves you to
 * set up a {@link org.swerverobotics.library.interfaces.II2cDeviceClient.ReadWindow ReadWindow} that
 * covers them all (if it can): the read window will be read all at once, then subsequent read()
 * operations will return various parts of that already retrieved data (if still valid) without
 * the need to invoke another enableI2cReadMode() expense. Note that this is purely an optimization:
 * if you don't specify an explicit read window, one will be automatically created for you. But
 * it's usually worth thinking about.</p>
 *
 * <p>Three different flavors of read window are available that differ in whether they
 * read only one time or perform repeated reads, and whether they aggressively return to reading
 * when there's no writing to do or just read when it's opportune to do so but don't on their
 * own cause underlying mode switches.</p>
 *
 * <p>For devices that automatically shutdown if no communication is received in a certain
 * duration, a heartbeat facility is optionally provided.</p>
 *
 * @see ClassFactory#createI2cDeviceClient(OpMode, I2cDevice, int, boolean)
 * @see org.swerverobotics.library.interfaces.II2cDeviceClient.ReadWindow
 * @see #ensureReadWindow(ReadWindow, ReadWindow)
 * @see #setHeartbeatAction(HeartbeatAction)
 */
public interface II2cDeviceClient extends HardwareDevice
    {
    //----------------------------------------------------------------------------------------------
    // ReadWindow management
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we will read and read and read again on every hardware cycle
     * 
     * @param window    the register window to read. May be null, indicating that no reads are to occur.
     * @see #getReadWindow() 
     */
    void setReadWindow(ReadWindow window);

    /**
     * Returns the current register window used for reading.
     * @return the current read window
     * @see #setReadWindow(ReadWindow)
     */
    ReadWindow getReadWindow();

    /**
     * Ensure that the current register window covers the indicated set of registers.
     *
     * If there is currently a non-null register window, and windowNeeded is non-null,
     * and the current register window entirely contains windowNeeded, then do nothing.
     * Otherwise, set the current register window to windowToSet.
     * 
     * @param windowNeeded Test the current register window, if any, against this window 
     *                     to see if an update to the current register window is needed in
     *                     order to cover it. May be null, indicating that an update to the
     *                     current register window is <I>always</I> needed
     * @param windowToSet  If an update to the current register window is needed, then this
     *                     is the window to which it will be set. May be null.
     *
     * @see #setReadWindow(ReadWindow)
     * @see #read8(int)
     * @see #executeFunctionWhileLocked(IFunc)
     */
    void ensureReadWindow(ReadWindow windowNeeded, ReadWindow windowToSet);

    //----------------------------------------------------------------------------------------------
    // Reading
    //----------------------------------------------------------------------------------------------

    /**
     * Read the byte at the indicated register. See {@link #readTimeStamped(int, int)} for a
     * complete description.
     *
     * @param ireg  the register number to read
     * @return      the byte that was read
     *
     * @see #read(int, int)
     * @see #readTimeStamped(int, int)
     * @see #ensureReadWindow(ReadWindow, ReadWindow)
     */
    byte read8(int ireg);

    /**
     * Read a contiguous set of device I2C registers. See {@link #readTimeStamped(int, int)} for a
     * complete description.
     *
     * @param ireg  the register number of the first byte register to read
     * @param creg  the number of bytes / registers to read
     * @return      the data which was read
     *
     * @see #read8(int)
     * @see #readTimeStamped(int, int)
     * @see #ensureReadWindow(ReadWindow, ReadWindow)
     */
    byte[] read(int ireg, int creg);

    /**
     * Reads and returns a contiguous set of device I2C registers, together with a best-available
     * timestamp of when the actual I2C read occurred. Note that this can take many tens of
     * milliseconds to execute, and thus should not be called from the loop() thread.
     *
     * <p>You can always just call this method without worrying at all about
     * {@link org.swerverobotics.library.interfaces.II2cDeviceClient.ReadWindow read windows},
     * that will work, but usually it is more efficient to take some thought and care as to what set
     * of registers the I2C device controller is being set up to read, as adjusting that window
     * of registers incurs significant extra time.</p>
     *
     * <p>If the current read window can't be used to read the requested registers, then
     * a new read window will automatically be created as follows. If the current read window is non
     * null and wholly contains the registers to read but can't be read because it is a used-up
     * {@link org.swerverobotics.library.interfaces.II2cDeviceClient.READ_MODE#ONLY_ONCE} window,
     * a new read fresh window will be created with the same set of registers. Otherwise, a
     * window that exactly covers the requested set of registers will be created.</p>
     *
     * <p>If one is trying to optimize the the register window by calling
     * {@link #ensureReadWindow(ReadWindow, ReadWindow) ensureReadWindow()}, this auto-window
     * creation can cause difficulties if any concurrent access is present. In such situations,
     * {@link #executeFunctionWhileLocked(IFunc)} can be used to allow you to atomically both
     * set the read window and execute a read without the possibility of the read window being
     * re-adjusted in the middle.
     * </p>
     *
     * @param ireg  the register number of the first byte register to read
     * @param creg  the number of bytes / registers to read
     * @return      the data which was read, together with the timestamp
     *
     * @see #read(int, int)
     * @see #read8(int)
     * @see #ensureReadWindow(ReadWindow, ReadWindow)
     * @see #executeFunctionWhileLocked(IFunc)
     */
    TimestampedData readTimeStamped(int ireg, int creg);
    
    /** TimestampedData pairs together data which has been read with the timestamp at which
     * the read occurred, as best that can be determined */
    class TimestampedData
        {
        /** the data in question */
        public byte[]   data;
        /** the timestamp on the System.nanoTime() clock associated with that data */
        public long     nanoTime;
        }

    /**
     * Advanced: Atomically calls ensureReadWindow() with the last two parameters and then
     * readTimeStamped() with the first two without the possibility of a concurrent client
     * interrupting in the middle.
     *
     * @param ireg              the register number of the first byte register to read
     * @param creg              the number of bytes / registers to read
     * @param readWindowNeeded  the read window we require
     * @param readWindowSet     the read window to set if the required read window is not current
     * @return                  the data that was read, together with the timestamp
     *
     * @see #ensureReadWindow(ReadWindow, ReadWindow)
     * @see #readTimeStamped(int, int)
     * @see #executeFunctionWhileLocked(IFunc)
     */
    TimestampedData readTimeStamped(int ireg, int creg, ReadWindow readWindowNeeded, ReadWindow readWindowSet);

    //----------------------------------------------------------------------------------------------
    // Writing
    //----------------------------------------------------------------------------------------------

    /**
     * Writes a byte to the indicated register. The call does not return until the write has
     * been queued to the device controller.
     * 
     * @param ireg      the register number that is to be written
     * @param bVal      the byte which is to be written to that register
     *
     * @see #write(int, byte[])
     */
    void write8(int ireg, int bVal);

    /**
     * Writes data to a set of registers, beginning with the one indicated. The data will be
     * written to the I2C device as expeditiously as possible. This method will not return until
     * the data has been written to the device controller; however, that does not necessarily
     * indicate that the data has been issued in an I2C write transaction, though that ought
     * to happen a short deterministic time later.
     * 
     * @param ireg      the first of the registers which is to be written
     * @param data      the data which is to be written to the registers
     *
     * @see #write8(int, int)
     * @see #write(int, byte[], boolean)
     */
    void write(int ireg, byte[] data);

    /**
     * Writes a byte to the indicated register. The call may or may block until the write
     * has been issued to the device controller
     *
     * @param ireg                  the register number that is to be written
     * @param bVal                  the byte which is to be written to that register
     * @param waitForCompletion     whether or not to wait until the write has been sent to the controller
     *
     * @see #write(int, byte[], boolean)
     */
    void write8(int ireg, int bVal, boolean waitForCompletion);

    /**
     * Writes data to a set of registers, beginning with the one indicated. The data will be
     * written to the I2C device as expeditiously as possible. The call may or may block until the write
     * has been issued to the device controller.
     *
     * @param ireg                  the first of the registers which is to be written
     * @param data                  the data which is to be written to the registers
     * @param waitForCompletion     whether or not to wait until the write has been sent to the controller
     *
     * @see #write8(int, int, boolean)
     */
    void write(int ireg, byte[] data, boolean waitForCompletion);

    /**
     * Waits for any previously issued writes to complete.
     * @throws InterruptedException
     */
    void waitForWriteCompletions();

    //----------------------------------------------------------------------------------------------
    // Concurrency management
    //----------------------------------------------------------------------------------------------

    /**
     * Executes the indicated action while holding the concurrency lock on the object
     * so as to prevent other threads from interleaving.
     *
     * @param action the action to execute
     * @see #executeFunctionWhileLocked(IFunc)
     */
    void executeActionWhileLocked(Runnable action);

    /**
     * Executes the indicated function while holding the concurrency lock on the object
     * so as to prevent other threads from interleaving. Returns the value of the function.
     *
     * @param function      the function to execute
     * @param <T>           the type of the data returned from the function
     * @return              the datum value returned from the function
     * @see #executeActionWhileLocked(Runnable)
     */
    <T> T executeFunctionWhileLocked(IFunc<T> function);

    //----------------------------------------------------------------------------------------------
    // Heartbeats
    //----------------------------------------------------------------------------------------------

    /**
     * Sets the interval within which communication must be received by the I2C device list
     * a timeout may occur. The default heartbeat interval is zero, signifying that no heartbeat
     * is maintained.
     *
     * @param ms the new hearbeat interval, in milliseconds
     * @see #getHeartbeatInterval()
     */
    void setHeartbeatInterval(int ms);

    /**
     * Returns the interval within which communication must be received by the I2C device lest
     * a timeout occur.
     * 
     * @return  the current heartbeat interval, in milliseconds
     * @see #setHeartbeatInterval(int)
     */
    int getHeartbeatInterval();

    /**
     * Sets the action to take when the current heartbeat interval expires.
     * The default action is null; thus, to be useful, an action must always
     * be explicitly specified.
     *
     * @param action the action to take at each heartbeat.
     * @see #getHeartbeatAction()
     * @see #setHeartbeatInterval(int)
     */
    void setHeartbeatAction(HeartbeatAction action);

    /**
     * Returns the current action, if any, to take upon expiration of the heartbeat interval.
     * @return the current heartbeat action. May be null
     * @see #setHeartbeatAction(HeartbeatAction)
     */
    HeartbeatAction getHeartbeatAction();

    /**
     * Instances of HeartBeatAction indicate what action to carry out to perform
     * a heartbeat should that become necessary. The actual action to take is indicated
     * by one of several prioritized possibilities. When a heartbeat is needed, these
     * are considered in order, and the first one applicable given the state of the
     * I2C device at the time will be applied.
     */
    class HeartbeatAction
        {
        /** Priority #1: re-issue the last I2C read operation, if possible. */
        public boolean      rereadLastRead      = false;

        /** Priority #2: re-issue the last I2C write operation, if possible. */
        public boolean      rewriteLastWritten  = false;

        /** Priority #3: explicitly read a given register window. Note that using
         * this form of heartbeat may cause the I2C device to experience concurrency it
         * otherwise might not support for this heartbeat form may make use of
         * worker threads.
         *
         * @see #executeFunctionWhileLocked(IFunc)
         */
        public ReadWindow   heartbeatReadWindow = null;
        }

    //----------------------------------------------------------------------------------------------
    // Monitoring, debugging, and life cycle management
    //----------------------------------------------------------------------------------------------

    /** Returns the thread on which it is observed that portIsReady callbacks occur 
     * @return the thread on which callbacks occur. If null, then no callback has yet been made
     *         so the identity of this thread is not yet known.
     */
    Thread getCallbackThread();

    /**
     * Sets the boost in thread priority we use for data acquisition. Judiciously applied,
     * this boost can help reduce jitter in data timestamps.
     * @param boost the boost in thread priority to apply
     * @see #getThreadPriorityBoost()
     */
    void setThreadPriorityBoost(int boost);

    /**
     * Retrieves the current boost in thread priority used for data acquisition.
     * @return the current boost in priority
     */
    int getThreadPriorityBoost();

    /**
     * Returns the number of I2C cycles that we've seen for this device. This at times
     * can be a useful debugging aid, but probably isn't useful for much more.
     *
     * @return the current I2C cycle count
     */
    int getI2cCycleCount();

    /**
     * Turn logging on or off. Logging output can be viewed using the Android Logcat tools.
     * @param enabled     whether to enable logging or not
     */
    void setLogging(boolean enabled);
    /**
     * Set the tag to use when logging is on.
     * @param loggingTag    the logging tag to sue
     */
    void setLoggingTag(String loggingTag);

    /**
     * Arms the client for operation. This involves registering for callbacks with
     * the underlying I2cDevice. Only one client of an I2cDevice may register for callbacks
     * at any given time; if multiple clients exist, they must be coordinated so as to use
     * the I2cDevice sequentially. This method is idempotent.
     * @see #disarm()
     * @see #isArmed()
     */
    void arm();

    /**
     * Answers as to whether this I2cDeviceClient is currently armed.
     * @return whether the client is currently armed
     * @see #arm()
     */
    boolean isArmed();

    /**
     * Disarms the client if it is currently armed. This method is idempotent.
     * @see #arm()
     */
    void disarm();

    /**
     * Close down and disable this device. Once this is done, the object instance cannot
     * support further read() or write() calls. Note that calling close() here does NOT
     * also close() the underlying I2cDevice: we here are a *client* of the I2cDevice, not
     * its owner. If your I2cDevice has a non-trivial close() semantic, you are yourself
     * responsible for calling that method at an appropriate time.
     */
    void close();

    /**
     * Sets the I2C address of the underlying client. If necessary, the client is briefly
     * disarmed and automatically rearmed in the process.
     * @param i2cAddr8Bit the new I2C address
     */
    void setI2cAddr(int i2cAddr8Bit);

    /**
     * Returns the I2C address currently being used.
     * @return the current I2C address
     */
    int getI2cAddr();

    //----------------------------------------------------------------------------------------------
    // RegWindow
    //----------------------------------------------------------------------------------------------

    /**
     * READ_MODE controls whether when asked to read we read only once or read multiple times.
     *
     * In all modes, it is guaranteed that a read() which follows a write() operation will
     * see the state of the device <em>after</em> the write has had effect.
     */
    enum READ_MODE
        {
        /**
         * Continuously issue I2C reads whenever there's nothing else needing to be done.
         * In this mode, {@link #read(int, int) read()} will not necessarily execute an I2C transaction
         * for every call but might instead return data previously read from the I2C device.
         * This mode is most useful in a device that spends most of its time doing read operations
         * and only very infrequently writes, if ever.
         *
         * @see #read(int, int)
         */
        REPEAT,

        /**
         * Continuously issue I2C reads as in REPEAT when we can, but do <em>not</em> automatically
         * transition back to read-mode following a write operation in order to do so. This mode is
         * most useful in a device which has a balanced mix of read() and write() operations, such
         * as a motor controller. Like {@link #REPEAT}, this mode might return data that was
         * previously read a short while ago.
         */
        BALANCED,

        /**
         * Only issue a single I2C read, then set the read window to null to disable further reads.
         * Executing a {@link #read(int, int) read()} in this mode will always get fresh data
         * from the I2C device.
         */
        ONLY_ONCE
        };


    /**
     * RegWindow is a utility class for managing the window of I2C register bytes that
     * are read from our I2C device on every hardware cycle
     */
    class ReadWindow
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        /**
         * enableI2cReadMode and enableI2cWriteMode both impose a maximum length
         * on the size of data that can be read or written at one time. cregReadMax
         * and cregWriteMax indicate those maximum sizes.
         * @see #cregWriteMax
         */
        public static final int cregReadMax = 26;   // No, not 27: the CDIM can't handle 27
        /** @see #cregReadMax */
        public static final int cregWriteMax = 26;  // the CDIM might be able to do 27, not just 26, but we're paranoid

        /**
         * The first register in the window
         */
        private final int iregFirst;
        /**
         * The number of registers in the window
         */
        private final int creg;
        /**
         * The mode of the window
         */
        private final READ_MODE readMode;
        /**
         * Whether a read has been issued for this window or not
         */
        private boolean readIssued;


        /**
         * Returns the first register in the window
         * @return the first register in the window
         */
        public int getIregFirst() { return this.iregFirst; }
        /**
         * Returns the first register NOT in the window
         * @return the first register NOT in the window
         */
        public int getIregMax()   { return this.iregFirst + this.creg; }
        /**
         * Returns the number of registers in the window
         * @return the number of registers in the window
         */
        public int getCreg()      { return this.creg; }
        /**
         * Returns the mode of the window
         * @return the mode of the window
         */
        public READ_MODE getReadMode() { return this.readMode; }
        /**
         * Returns whether a read has ever been issued for this window or not
         * @return whether a read has ever been issued for this window or not
         */
        public boolean getReadIssued() { return this.readIssued; }
        /**
         * Sets that a read has in fact been issued for this window
         */
        public void setReadIssued() { this.readIssued = true; }

        /**
         * Answers as to whether we're allowed to read using this window. This will return
         * false for ONLY_ONCE windows after {@link #setReadIssued()} has been called on them.
         * @return whether it is permitted to perform a read for this window.
         */
        public boolean isOkToRead()
            {
            return !this.readIssued || this.readMode != READ_MODE.ONLY_ONCE;
            }

        /**
         * Answers as to whether this window in its present state ought to cause a transition
         * to read-mode when there's nothing else for the device to be doing.
         * @return whether this device should cause a read mode transition
         */
        public boolean maySwitchToReadMode()
            {
            return !this.readIssued || this.readMode == READ_MODE.REPEAT;
            }

        //------------------------------------------------------------------------------------------
        // Construction
        //------------------------------------------------------------------------------------------

        /**
         * Create a new register window with the indicated starting register and register count
         *
         * @param iregFirst the index of the first register to read
         * @param creg      the number of registers to read
         * @param readMode  whether to repeat-read or read only once
         */
        public ReadWindow(int iregFirst, int creg, READ_MODE readMode)
            {
            this.readMode   = readMode;
            this.readIssued = false;
            this.iregFirst  = iregFirst;
            this.creg       = creg;
            if (creg < 0 || creg > cregReadMax)
                throw new IllegalArgumentException(String.format("buffer length %d invalid; max is %d", creg, cregReadMax));
            }

        /**
         * Returns a copy of this window but with the {@link #readIssued} flag clear
         * @return a fresh readable copy of the window
         */
        public ReadWindow freshCopy()
            {
            return new ReadWindow(this.iregFirst, this.creg, this.readMode);
            }

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        /**
         * Do the receiver and the indicated register window cover exactly the
         * same set of registers and have the same modality?
         * @param him   the other window to compare to
         * @return the result of the comparison
         */
        public boolean sameAsIncludingMode(ReadWindow him)
            {
            if (him == null)
                return false;
            
            return this.getIregFirst() == him.getIregFirst() 
                    && this.getCreg() == him.getCreg()
                    && this.getReadMode() == him.getReadMode();
            }
        
        /**
         * Answers as to whether the receiver wholly contains the indicated window.
         *
         * @param him   the window we wish to see whether we contain
         * @return      whether or not we contain the window
         * @see #contains(int, int)
         */
        public boolean contains(ReadWindow him)
            {
            if (him==null)
                return false;

            return this.getIregFirst() <= him.getIregFirst() && him.getIregMax() <= this.getIregMax();
            }

        /**
         * Answers as to whether the receiver wholly contains the indicated window
         * and also has the same modality.
         *
         * @param him   the window we wish to see whether we contain
         * @return      whether or not we contain the window
         */
        public boolean containsWithSameMode(ReadWindow him)
            {
            return contains(him) && (this.getReadMode() == him.getReadMode());
            }

        /**
         * Answers as to whether the receiver wholly contains the indicated set of registers.
         *
         * @param ireg  the first register of interest
         * @param creg  the number of registers of interest
         * @return whether or not the receiver contains this set of registers
         * @see #contains(ReadWindow)
         */
        public boolean contains(int ireg, int creg)
            {
            return this.containsWithSameMode(new ReadWindow(ireg, creg, this.getReadMode()));
            }
        }
    }
