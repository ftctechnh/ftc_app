package org.swerverobotics.library.internal;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * SynchronousThreadContext maintains the internal context for a synchronous thread.
 */
public class SynchronousThreadContext
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    /**
     * getThread() returns the Thread for which we are the internal context.
     */
    public Thread getThread() { return this.thread; }

    /**
     * getThunker() returns the channel by which we can thunk from a synchronous
     * thread to the loop() thread.
     */
    public IThunkDispatcher getThunker() { return this.thunker; }

    /**
     * The action key used for write thunks that are issued by this thread
     */
    public int actionKeyWritesFromThisThread = Thunk.getNewActionKey();

    private final Thread   thread;
    private final IThunkDispatcher thunker;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousThreadContext(IThunkDispatcher thunker)
        {
        this.thread = Thread.currentThread();
        this.thunker = thunker;
        }

    //----------------------------------------------------------------------------------------------
    // Lookup
    //----------------------------------------------------------------------------------------------

    public static void setThreadThunker(IThunkDispatcher thunker)
        {
        tlsThunker.set(new SynchronousThreadContext(thunker));
        }

    public static SynchronousThreadContext getThreadContext()
        {
        return tlsThunker.get();
        }
    
    public static boolean isSynchronousThread()
        {
        return getThreadContext() != null;
        }
    
    public static void assertSynchronousThread()
        {
        if (BuildConfig.DEBUG) 
            {
            junit.framework.Assert.assertEquals(true, isSynchronousThread());
            }
        }

    /**
     * tlsThunker is the thread local variable by which a SynchronousThreadContext is associated with a thread
     */
    private static final ThreadLocal<SynchronousThreadContext> tlsThunker = new ThreadLocal<SynchronousThreadContext>()
        {
        @Override protected SynchronousThreadContext initialValue() { return null; }
        };

    }