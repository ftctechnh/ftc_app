package org.swerverobotics.library;

/**
 * Thunks derived from ResultableThunk have a member variable named 'result
 * which can be set inside of actionOnLoopThread() in order to return data
 * back to the caller of dispatch().
 */
public abstract class ResultableThunk<T> extends WaitingThunk
    {
    T result;
    }