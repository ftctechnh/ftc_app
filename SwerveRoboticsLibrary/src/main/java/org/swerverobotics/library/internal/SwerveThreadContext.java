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

    public final Thread     thread;
    public OpMode           opMode;
    public boolean          isSynchronousThread;

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
        this.thread              = Thread.currentThread();
        this.opMode              = null;
        this.isSynchronousThread = false;
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

    public static OpMode getOpMode()
        {
        return getThreadContext()==null ? null : getThreadContext().opMode;
        }

    public static void assertSynchronousThread()
        {
        junit.framework.Assert.assertTrue(isSynchronousThread());
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
    }