package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.*;
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
    private final OpMode           opMode;
    private final IThunkDispatcher thunker;

    /**
     * tlsThunker is the thread local variable by which a SynchronousThreadContext is associated with a thread
     */
    private static final ThreadLocal<SynchronousThreadContext> tlsThunker = new ThreadLocal<SynchronousThreadContext>()
        {
        @Override protected SynchronousThreadContext initialValue() { return null; }
        };

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousThreadContext(OpMode opMode, IThunkDispatcher thunker)
        {
        this.opMode  = opMode;
        this.thread  = Thread.currentThread();
        this.thunker = thunker;
        }

    public static void create(OpMode opMode, IThunkDispatcher thunker)
        {
        tlsThunker.set(new SynchronousThreadContext(opMode, thunker));
        }

    //----------------------------------------------------------------------------------------------
    // Access
    //----------------------------------------------------------------------------------------------

    public static SynchronousThreadContext getThreadContext()
        {
        return tlsThunker.get();
        }
    public static IThunkDispatcher getContextualThunker()
        {
        return getThreadContext()==null ? null : getThreadContext().getThunker();
        }
    public static boolean isSynchronousThread()
        {
        return getThreadContext() != null;
        }
    public static void assertSynchronousThread()
        {
        junit.framework.Assert.assertTrue(!BuildConfig.DEBUG || isSynchronousThread());
        }

    public static OpMode getContextualOpMode()
        {
        return getThreadContext()==null ? null : getThreadContext().getOpMode();
        }
    public IThunkDispatcher getThunker()
        {
        return this.thunker;
        }
    public OpMode getOpMode()
        {
        return this.opMode;
        }
    }