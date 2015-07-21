package org.swerverobotics.library.thunking;

/**
 * IThunker is an interface through which one can cause work to be thunked to the loop() thread.
 */
public interface IThunker
    {
    /**
     * Execute the work contained in the thunk over on the loop thread.
     */
    void thunkFromSynchronousThreadToLoopThread(IThunk thunk);

    /**
     * Execute the work on the loop thread as soon as we can (which may be on the current thread).
     */
    void executeOnLoopThreadASAP(IThunk thunk);

    /**
     * Is the current thread the loop thread?
     */
    boolean isLoopThread();
    }
