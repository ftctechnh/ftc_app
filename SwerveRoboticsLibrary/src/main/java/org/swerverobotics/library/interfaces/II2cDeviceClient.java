package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;

/**
 * II2cDeviceClient is the public interface to a utility class that makes it easier to
 * use I2cDevice instances.
 * 
 * @see ClassFactory#createI2cDeviceClient(I2cDevice, int, II2cDeviceClient.RegWindow)
 */
public interface II2cDeviceClient extends HardwareDevice
    {
    //----------------------------------------------------------------------------------------------
    // RegWindow management
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we will read and read and read again on every hardware cycle
     * 
     * @param window    the register window to read. May be null, indicating that no reads are to occur.
     * @see #getReadWindow() 
     */
    void setReadWindow(RegWindow window);

    /**
     * Return the current register window.
     * @see #setReadWindow(RegWindow)  
     */
    RegWindow getReadWindow();

    /**
     * Ensure that the current register window covers the indicated set of registers.
     *
     * If there is currently a non-null register window, and windowNeeded is non-null,
     * and the curret register window entirely contains windowNeeded, then do nothing.
     * Otherwise, set the current register window to windowToSet.
     * 
     * @param windowNeeded Test the current register window, if any, against this window 
     *                     to see if an update to the current register window is needed in
     *                     order to cover it. May be null, indicating that an update to the
     *                     current register window is <I>always</I> needed
     * @param windowToSet  If an update to the current register window is needed, then this
     *                     is the window to which it will be set. May be null.
     *
     * @see #setReadWindow(RegWindow) 
     */
    void ensureReadWindow(RegWindow windowNeeded, RegWindow windowToSet);

    //----------------------------------------------------------------------------------------------
    // Reading
    //----------------------------------------------------------------------------------------------

    /**
     * Read the byte at the indicated register.
     *
     * The register must lie within the current register window.
     * 
     * @param ireg  the register number to read
     * @return      the byte that was read
     */
    byte read8(int ireg);

    /**
     * Read a contiguous set of device I2C registers.
     *
     * All the registers must lie within the current register window. Note that this 
     * method can take several tens of milliseconds to execute.
     * 
     * @param ireg  the register number of the first byte register to read
     * @param creg  the number of bytes / registers to read
     * @return      the data which was read
     */
    byte[] read(int ireg, int creg);

    /**
     * Reads and returns a contiguous set of device I2C registers, together with a timestamp
     * of when the read occurred.
     *
     * @param ireg  the register number of the first byte register to read
     * @param creg  the number of bytes / registers to read
     * @return      the data which was read, together with the timestamp
     */
    TimestampedData readTimeStamped(int ireg, int creg);
    
    /** TimestampedData pairs together data which has been read with the timestamp at which the read occurred */
    class TimestampedData
        {
        /** the data in question */
        public byte[]   data;
        /** the timestamp on the System.nanoTime() clock associated with that data */
        public long     nanoTime;
        }

    //----------------------------------------------------------------------------------------------
    // Writing
    //----------------------------------------------------------------------------------------------

    /**
     * Write a byte to the indicated register
     * 
     * @param ireg      the register number that is to be written
     * @param bVal      the byte which is to be written to that register
     */
    void write8(int ireg, int bVal);

    /**
     * Write data to a set of registers, beginning with the one indicated
     * 
     * @param ireg      the first of the registers which is to be written
     * @param data      the data which is to be written to the registers
     */
    void write(int ireg, byte[] data);

    //----------------------------------------------------------------------------------------------
    // Heartbeats
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the interval within which communication must be received by the I2C device lest
     * a timeout occur. The default heartbeat interval is zero.
     * 
     * @return  the current heartbeat interval, in milliseconds
     * @see #setHeartbeatRead(int) 
     * @see #setHeartbeatWrite(int) 
     */
    int getHeartbeatInterval();

    /**
     * Sets the interval within which communication must be received by the I2C device lest
     * a timeout occur. If a heartbeat must be sent, read the current read window registers
     * from the device.
     * 
     * In effect, this sets a minimum frequency with which the read window registers are read.
     * Note, though, that they may be read much more often than this, at the discretion of the
     * implementation.
     * 
     * For read-heartbeats to be useful, the current read window must be non-null.
     * @param ms            the timeout interval, in milliseconds. If ms is less than or equal to
     *                      zero, then no heartbeat messages are sent
     * @see #setReadWindow(RegWindow) 
     */
    void setHeartbeatRead(int ms);

    /**
     * Sets the interval within which communication must be received by the I2C device lest
     * a timeout occur. If a heartbeat must be sent, the previous write operation on the 
     * device is reissued.
     *
     * @param ms            the timeout interval, in milliseconds. If ms is less than or equal to
     *                      zero, then no heartbeat messages are sent
     * @see #setHeartbeatRead(int) 
     */
    void setHeartbeatWrite(int ms);

    //----------------------------------------------------------------------------------------------
    // Monitoring & debugging
    //----------------------------------------------------------------------------------------------

    /** Returns the thread on which it is observed that portIsReady callbacks occur 
     * @return the thread on which callbacks occur. If null, then no callback has yet been made
     *         so the identity of this thread is not yet known.
     */
    Thread getCallbackThread();

    /**
     * Returns the number of I2C cycles that we've seen for this device. This at times
     * can be a useful debugging aid, but probably isn't useful for much more.
     */
    int getI2cCycleCount();

    /**
     * Turn logging on or off. Logging output can be viewed using the Android Logcat tools.
     * @param enabled     whether to enable logging or not
     */
    void setLoggingEnabled(boolean enabled);

    //----------------------------------------------------------------------------------------------
    // RegWindow
    //----------------------------------------------------------------------------------------------

    /**
     * RegWindow is a utility class for managing the window of I2C register bytes that
     * are read from our I2C device on every hardware cycle
     */
    class RegWindow
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
            if (him == null)
                return false;
            
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
