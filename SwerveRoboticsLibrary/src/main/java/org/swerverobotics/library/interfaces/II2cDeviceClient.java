package org.swerverobotics.library.interfaces;

/**
 * Created by bob on 8/29/15.
 */
public interface II2cDeviceClient
    {
    //----------------------------------------------------------------------------------------------
    // RegWindow management
    //----------------------------------------------------------------------------------------------

    /**
     * Set the set of registers that we will read and read and read again on every hardware cycle 
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
     */
    void ensureRegisterWindow(RegWindow windowNeeded, RegWindow windowToSet);

    //----------------------------------------------------------------------------------------------
    // Reading
    //----------------------------------------------------------------------------------------------

    /**
     * Read the byte at the indicated register.
     *
     * The register must lie within the current register window.
     */
    byte read8(int ireg);

    /**
     * Read a (little-endian) integer starting at the indicated register.
     *
     * The two registers comprising the integer must lie within the current register window.
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
     */
    void write8(int ireg, int bVal);

    /**
     * Write an little endian integer to an adjacent pair of registers
     */
    void write16(int ireg, int iVal);

    /**
     * Write data to a set of registers, beginning with the one indicated
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
