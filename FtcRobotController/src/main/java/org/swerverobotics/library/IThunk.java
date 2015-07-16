package org.swerverobotics.library;

/**
 * IThunk is an interface for encapsulating work that must be thunked from
 * a synchronous thread to the loop() thread.
 */
public interface IThunk
    {
    void doThunk();
    }

