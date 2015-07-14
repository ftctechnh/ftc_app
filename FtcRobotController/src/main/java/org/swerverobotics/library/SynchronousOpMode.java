package org.swerverobotics.library;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * SynchronousOpMode is a base class that can be derived from in order to
 * write op modes that can be coded in a linear, synchronous programming style.
 *
 * Extend this class and implement the main() method to add your own code.
 */
public abstract class SynchronousOpMode extends OpMode implements Runnable
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    Thread loopThread;
    Thread mainThread;
    private static final ThreadLocal<SynchronousOpMode> synchronousOpContext
            = new ThreadLocal<SynchronousOpMode>()
                {
                @Override protected SynchronousOpMode initialValue() { return null; }
                };

    ConcurrentLinkedQueue queue;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousOpMode()
        {
        this.queue = new ConcurrentLinkedQueue();
        }

    /**
     * If we are running on a main() thread, then return the SynchronousOpMode
     * which is managing that thread.
     */
    public static SynchronousOpMode getThreadThunker()
        {
        return synchronousOpContext.get();
        }

    //----------------------------------------------------------------------------------------------
    // Thread communication
    //----------------------------------------------------------------------------------------------

    @Override
    public void loop()
        {
        // Run any actions we've been asked to execute
        for (;;)
            {
            Object o = this.queue.poll();
            if (null == o)
                break;

            IAction action = (IAction)(o);
            if (null != action)
                action.doIt();
            }
        }

    void executeOnLoopThread(IAction action)
        {
        this.queue.add(action);
        }

    //----------------------------------------------------------------------------------------------
    // User code
    //----------------------------------------------------------------------------------------------

    /**
     * Implement main() (in a derived class) to contain your robot logic.
     *
     * Note that ideally your code will be interruption-aware, but that is not
     * strictly necessary.
     *
     * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#interrupt()">Thread.interrupt()</a>
     * @see <a href="http://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html">Interrupts</a>
     */
    public abstract void main() throws InterruptedException;

    //----------------------------------------------------------------------------------------------
    // Startup and shutdown
    //----------------------------------------------------------------------------------------------

    @Override
    public void start()
        {
        this.loopThread = Thread.currentThread();
        //
        this.mainThread = new Thread(this);
        this.mainThread.start();
        }

    /**
     * Shut down this op mode.
     *
     * It will in particular ALWAYS be the case that by the  time the stop() method returns that
     * the thread on which MainThread() is executed will have been terminated.
     */
    @Override
    public final void stop()
        {
        // Notify the MainThread() method that we wish it to stop what it's doing, clean
        // up, and return.
        this.mainThread.interrupt();
        //
        try {
            // Wait, briefly, to give the thread a chance to handle the interruption and complete
            this.mainThread.wait(100);
            }
        catch (InterruptedException e) { }
        finally
            {
            // Under all circumstances, make sure the thread shuts down, even if the
            // programmer hasn't handled interruption (for example, he might have entered
            // an infinite loop, or a very long one at least).
            this.mainThread.stop();
            }
        try {
            // Wait until the thread terminates
            this.mainThread.join();
            }
        catch (InterruptedException e) { }
        }

    /**
     * Our run method here merely calls the synchronous MainThread() method.
     */
    public final void run()
        {
        // Note that this op mode is the thing on this thread that can hop one back to the loop thread
        synchronousOpContext.set(this);

        try
            {
            this.main();
            }
        catch (InterruptedException e)
            {
            return;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    public boolean IsLoopThread()
        {
        return this.loopThread.getId() == Thread.currentThread().getId();
        }
    public boolean IsMainThread()
        {
        return this.mainThread.getId() == Thread.currentThread().getId();
        }

    void Foo()
        {
        ThreadLocal<SynchronousOpMode> local = new ThreadLocal<SynchronousOpMode>();
        }
    }

//==================================================================================================
//
//==================================================================================================

interface IAction
    {
    void doIt();
    }

abstract class WaitableAction implements IAction
    {
    Semaphore semaphore = new Semaphore(1);

    public void doIt()
        {
        this.evaluateOnLoopThread();
        this.semaphore.release();
        }

    protected abstract void evaluateOnLoopThread();

    void dispatch()
        {
        SynchronousOpMode.getThreadThunker().executeOnLoopThread(this);
        try
            {
            this.semaphore.acquire();
            }
        catch (InterruptedException e)
            {
            Thread.currentThread().interrupt();
            }
        }
    }
abstract class ResultableAction<T> extends WaitableAction
    {
    T result;
    }
