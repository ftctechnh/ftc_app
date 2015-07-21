package org.swerverobotics.library.exceptions;

/**
 * An unchecked version of InterruptedException. Thrown by classes that pay attention to if they
 * were interrupted, yet have no other choice but to throw.
 */
public class RuntimeInterruptedException extends SwerveRuntimeException
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public InterruptedException interruptedException = null;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public RuntimeInterruptedException()
        {
        }

    public RuntimeInterruptedException(InterruptedException e)
        {
        this.interruptedException = e;
        }

    }
