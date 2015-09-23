package org.swerverobotics.library.interfaces;

/**
 * Advanced: IThunkDispatcher is an interface through which one can cause work to be thunked to the loop() thread.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Thunk">https://en.wikipedia.org/wiki/Thunk</a>
 */
public interface IThunkDispatcher
    {
    /**
     * Execute the indicated action over on the loop() thread.
     *
     * @param action the action to execute
     */
    void executeOnLoopThread(IAction action);

    /**
     * Execute the indicated action over on the loop() thread, but replace any
     * pending singleton there already there who was queued using the same action key.
     *
     * @param singletonKey  the key that indicates which pending previous action to replace
     * @param action        the action to execute
     *
     * @see #getNewSingletonKey()
     */
    void executeSingletonOnLoopThread(int singletonKey, IAction action);

    /**
     * Return a new singleton key
     * @return the new key
     * @see #executeSingletonOnLoopThread(int, IAction)
     */
    int getNewSingletonKey();
    }
