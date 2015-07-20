package org.swerverobotics.library;

/**
 * An unchecked version of InterruptedException. Thrown by classes that pay attention to if they
 * were interrupted, yet have no other choice but to throw.
 */
public class RuntimeInterruptedException extends SwerveRuntimeException
    {
    public InterruptedException interruptedException = null;

    public RuntimeInterruptedException()
        {
        }

    public RuntimeInterruptedException(InterruptedException e)
        {
        this.interruptedException = e;
        }

    }
