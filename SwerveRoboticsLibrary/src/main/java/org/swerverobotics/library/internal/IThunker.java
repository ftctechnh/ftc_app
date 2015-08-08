package org.swerverobotics.library.internal;

import org.swerverobotics.library.interfaces.IAction;

/**
 * IThunker is an interface through which one can cause work to be thunked to the loop() thread.
 */
public interface IThunker
    {
    /**
     * Execute the work contained in the thunk over on the loop thread.
     */
    void executeOnLoopThread(IAction thunk);

    /**
     * Execute the work on the loop thread as soon as we can (which may be on the current thread).
     */
    void executeSingletonOnLoopThread(int singletonKey, IAction action);

    /**
     * Return a new singleton key
     */
    int getNewSingletonKey();
    }
