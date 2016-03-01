package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Public interface to the little OpModeLoopCounter utility
 * @see org.swerverobotics.library.ClassFactory#createLoopCounter(OpMode)
 */
public interface IOpModeLoopCounter
    {
    /**
     * Returns the number of times that loop() has been called in the associated OpMode
     * @return the number of times loop() has been called
     */
    int getLoopCount();

    /** Shuts down the loop counter */
    void close();
    }
