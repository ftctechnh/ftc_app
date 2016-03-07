package org.swerverobotics.library.interfaces;

/**
 * A variation of IFunc that permits the throwing of an InterruptedException
 * @see IFunc
 */
public interface IInterruptableFunc<TResult>
    {
    TResult value() throws InterruptedException;
    }
