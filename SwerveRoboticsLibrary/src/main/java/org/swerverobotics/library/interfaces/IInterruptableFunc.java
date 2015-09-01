package org.swerverobotics.library.interfaces;

/**
 * @see IFunc
 */
public interface IInterruptableFunc<TResult>
    {
    TResult value() throws InterruptedException;
    }
