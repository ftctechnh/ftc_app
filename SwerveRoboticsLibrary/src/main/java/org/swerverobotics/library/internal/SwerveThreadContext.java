package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * SwerveThreadContext maintains thread-specific context for the Swerve Library
 */
public class SwerveThreadContext
    {
    //----------------------------------------------------------------------------------------------
    // State
    // Only to be accessed directly by internal Swerve Library components. All others to use methods.
    //----------------------------------------------------------------------------------------------

    /**
     * The action key used for write thunks that are issued by this thread
     */
    public int actionKeyWritesFromThisThread = Thunk.getNewActionKey();

    public final Thread     thread;
    public OpMode           opMode;
    public IThunkDispatcher thunker;
    public boolean          isSynchronousThread;
    public SwerveFtcEventLoop swerveFtcEventLoop;

    /**
     * tlsThreadContext is the thread local variable by which a SwerveThreadContext is associated with a thread
     */
    private static final ThreadLocal<SwerveThreadContext> tlsThreadContext = new ThreadLocal<SwerveThreadContext>()
        {
        @Override protected SwerveThreadContext initialValue() { return null; }
        };

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SwerveThreadContext()
        {
        this.thread  = Thread.currentThread();
        this.opMode              = null;
        this.thunker             = null;
        this.isSynchronousThread = false;
        this.swerveFtcEventLoop  = null;
        }

    public static SwerveThreadContext createIfNecessary()
        {
        SwerveThreadContext result = getThreadContext();
        if (null == result)
            {
            result = new SwerveThreadContext();
            tlsThreadContext.set(result);
            }
        return result;
        }

    //----------------------------------------------------------------------------------------------
    // Access
    //----------------------------------------------------------------------------------------------

    public static SwerveThreadContext getThreadContext()
        {
        return tlsThreadContext.get();
        }
    public static IThunkDispatcher getThunker()
        {
        return getThreadContext()==null ? null : getThreadContext().thunker;
        }
    public static OpMode getOpMode()
        {
        return getThreadContext()==null ? null : getThreadContext().opMode;
        }
    public static EventLoopManager getEventLoopManager()
        {
        SwerveThreadContext me = getThreadContext();
        if (me != null)
            {
            if (me.swerveFtcEventLoop != null)
                {
                return me.swerveFtcEventLoop.getEventLoopManager();
                }
            }
        return null;
        }

    public static void assertSynchronousThread()
        {
        junit.framework.Assert.assertTrue(!BuildConfig.DEBUG || isSynchronousThread());
        }
    public static boolean isSynchronousThread()
        {
        SwerveThreadContext context = getThreadContext();
        return context != null && context.thisIsSynchronousThread();
        }

    public boolean thisIsSynchronousThread()
        {
        return this.isSynchronousThread;
        }
    public IThunkDispatcher thisGetThunker()
        {
        return this.thunker;
        }
    }