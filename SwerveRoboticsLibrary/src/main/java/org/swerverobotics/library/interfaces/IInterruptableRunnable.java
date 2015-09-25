package org.swerverobotics.library.interfaces;

/**
 * A variation on Runnable that allows InterruptedExceptions to be thrown
 */
public interface IInterruptableRunnable
    {
    void run() throws InterruptedException;
    }
