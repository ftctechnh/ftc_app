package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;

/**
 * II2cDeviceClient is the public interface to a utility class that makes it easier to
 * use I2cDevice instances.
 * 
 * @see ClassFactory#createI2cDeviceClient(I2cDevice, II2cDeviceClient.RegWindow, int)
 */
public interface II2cDeviceClient
    {
    //----------------------------------------------------------------------------------------------
    // RegWindow management
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we will read and read and read again on every hardware cycle
     * 
     * @param window    the register window to read. May be null, indicating that no reads are to occur.
     */
    void setRegisterWindow(RegWindow window);

    /**
     * Return the current register window.
     */
    RegWindow getRegisterWindow();

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
     * @see #setRegisterWindow
     */
    void ensureRegisterWindow(RegWindow windowNeeded, RegWindow windowToSet);

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
     * Read a (little-endian) integer starting at the indicated register.
     *
     * The two registers comprising the integer must lie within the current register window.
     * 
     * @param ireg  the register number of the first of the two registers to read
     * @return      the integer that was read
     */
    int read16(int ireg);

    /**
     * Read a contiguous set of registers.
     *
     * All the registers must lie within the current register window. Note that this 
     * method can take several tens of milliseconds to execute.
     */
    byte[] read(int ireg, int creg);

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
     * Write an little endian 16-bit integer to an adjacent pair of registers
     * 
     * @param ireg      the first of the registers which is to be written
     * @param iVal      the 16 bit integer which is to be written there
     */
    void write16(int ireg, int iVal);

    /**
     * Write data to a set of registers, beginning with the one indicated
     * 
     * @param ireg      the first of the registers which is to be written
     * @param data      the data which is to be written to the registers
     */
    void write(int ireg, byte[] data);
    
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
