package org.swerverobotics.library.thunking;

/**
 * IThunker is an interface through which one can cause work to be thunked to the loop() thread.
 */
public interface IThunker
    {
    /**
     * Execute the work contained in the thunk over on the loop thread.
     */
    void executeOnLoopThread(IThunk thunk);
    }
