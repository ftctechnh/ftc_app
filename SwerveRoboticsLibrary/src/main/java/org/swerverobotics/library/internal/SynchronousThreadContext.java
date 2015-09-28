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
     * The action key used for write thunks that are issued by this thread
     */
    public int actionKeyWritesFromThisThread = Thunk.getNewActionKey();

    private final Thread           thread;
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
    // Access
    //----------------------------------------------------------------------------------------------

    /**
     * Retrieves the thread context the current thread
     * @return the context of the current thread
     */
    public static SynchronousThreadContext getThreadContext()
        {
        return tlsThunker.get();
        }

    /**
     * Returns the Thread for which the receiver is the context.
     *
     * @return the thread for which the receiver is the context
     */
    public Thread getThread() { return this.thread; }

    /**
     * Returns an object that can assist in thunking work from a synchronous thread
     * to the loop() thread.
     *
     * @return the object that can help with thunking
     */
    public IThunkDispatcher getThunker() { return this.thunker; }

    /**
     * Returns an object that one can use to register an action when a synchronous opmode stops
     * @return
     */
    public IStopActionRegistrar getStopActionRegistrar()
        {
        return (IStopActionRegistrar)(this.getThunker());
        }

    //----------------------------------------------------------------------------------------------
    // Lookup
    //----------------------------------------------------------------------------------------------

    public static void setThreadThunker(IThunkDispatcher thunker)
        {
        tlsThunker.set(new SynchronousThreadContext(thunker));
        }

    public static boolean isSynchronousThread()
        {
        return getThreadContext() != null;
        }
    
    public static void assertSynchronousThread()
        {
        junit.framework.Assert.assertTrue(!BuildConfig.DEBUG || isSynchronousThread());
        }

    /**
     * tlsThunker is the thread local variable by which a SynchronousThreadContext is associated with a thread
     */
    private static final ThreadLocal<SynchronousThreadContext> tlsThunker = new ThreadLocal<SynchronousThreadContext>()
        {
        @Override protected SynchronousThreadContext initialValue() { return null; }
        };
    }