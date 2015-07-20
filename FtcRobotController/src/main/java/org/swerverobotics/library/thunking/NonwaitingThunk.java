package org.swerverobotics.library.thunking;

/**
 * Thunks derived from NonwaitingThunk queue up their work but do not synchronously
 * wait for that work's execution before returning from dispatch() to the caller.
 */
public abstract class NonwaitingThunk extends ThunkBase
    {
    }