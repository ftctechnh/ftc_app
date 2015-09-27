package org.swerverobotics.library.interfaces;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * Advanced: IThunkDispatcher is an interface through which one can cause work to be thunked to the loop() thread.
 *
 * @see SynchronousOpMode#getThreadThunker()
 * @see <a href="https://en.wikipedia.org/wiki/Thunk">https://en.wikipedia.org/wiki/Thunk</a>
 */
public interface IThunkDispatcher
    {
    /**
     * Executes the indicated action over on the loop() thread.
     *
     * @param action the action to execute
     * @see SynchronousOpMode#getThreadThunker()
     */
    void executeOnLoopThread(IAction action);

    /**
     * Executes the indicated action over on the loop() thread, but replace any
     * pending singleton already there who was queued using the same singleton key.
     *
     * @param singletonKey  the key that indicates which pending previous action to replace
     * @param action        the action to execute
     *
     * @see #getNewSingletonKey()
     * @see SynchronousOpMode#getThreadThunker()
     */
    void executeSingletonOnLoopThread(int singletonKey, IAction action);

    /**
     * Returns a new singleton key
     * @return the new key
     * @see #executeSingletonOnLoopThread(int, IAction)
     * @see SynchronousOpMode#getThreadThunker()
     */
    int getNewSingletonKey();
    }
