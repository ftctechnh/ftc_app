package org.swerverobotics.library.internal;

/**
 * IThunkedReadWriteListener interface can be implemented by those thunks who need to be made
 * aware of transitions between reading and writing operations.
 *
 * The thunk dispatching logic will call out to provided instances of this 
 * interface as read and write operations are made.
 */
public interface IThunkedReadWriteListener
    {
    void enterReadOperation() throws InterruptedException;
    void enterWriteOperation() throws InterruptedException;

    int getListenerReadThunkKey();
    int getListenerWriteThunkKey();
    }
