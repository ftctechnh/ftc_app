package org.swerverobotics.library.exceptions;

/**
 * An unchecked version of InterruptedException. Thrown by classes that pay attention to if they
 * were interrupted, yet have no other choice but to throw.
 */
public class RuntimeInterruptedException extends SwerveRuntimeException
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public RuntimeInterruptedException()
        {
        super(String.format("thread '%s' was interrupted", Thread.currentThread().getName()));
        }

    public RuntimeInterruptedException(InterruptedException e)
        {
        super(String.format("thread '%s' was interrupted", Thread.currentThread().getName()), e);
        }

    }
