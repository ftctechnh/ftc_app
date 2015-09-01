package org.swerverobotics.library.interfaces;

import org.swerverobotics.library.interfaces.IAction;

/**
 * Advanced: IThunker is an interface through which one can cause work to be thunked to the loop() thread.
 */
public interface IThunker
    {
    /**
     * Execute the indicated action over on the loop() thread.
     */
    void executeOnLoopThread(IAction action);

    /**
     * Execute the indicated action over on the loop() thread, but replace any
     * pending singleton there already there who was queued using the same action key.
     */
    void executeSingletonOnLoopThread(int singletonKey, IAction action);

    /**
     * Return a new singleton key
     */
    int getNewSingletonKey();
    }
