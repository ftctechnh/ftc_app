package org.swerverobotics.library.internal;

import java.util.concurrent.Semaphore;

/**
 * A class that helps us start a thread and interlock with its actual starting up
 */
public class HandshakeThreadStarter
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------
    
    public String  getName()    { return this.name;  }
    public Thread  getThread()  { return this.thread; }

    private String          name           = null;
    private IHandshakeable  shakeable      = null;
    private Semaphore       semaphore      = null;
    private Thread          thread         = null;
    private boolean         started        = false;
    private boolean         stopRequested  = false;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public HandshakeThreadStarter(String name, IHandshakeable shakeable)
        {
        this.shakeable    = shakeable;
        this.name         = name;
        this.thread       = null;
        this.semaphore    = new Semaphore(0);
        }
    public HandshakeThreadStarter(IHandshakeable shakeable)
        {
        this(null, shakeable);
        }

    //----------------------------------------------------------------------------------------------
    // Public methods
    //----------------------------------------------------------------------------------------------

    /** Get everything ready for starting */
    public synchronized Thread prepareStart()
        {
        stop();

        resetEvent();
        this.stopRequested = false;
        this.thread = new Thread(new Run());
        if (this.name != null)
            this.thread.setName(this.name);

        return this.thread;
        }

    /** Starts the thread going. Blocks until the thread actually runs and calls starter.handshake(). */
    public synchronized void start() throws InterruptedException
        {
        try {
            if (this.thread == null)
                prepareStart();

            this.thread.start();
            waitEvent();

            this.started = true;
            }
        catch (InterruptedException ex)
            {
            // Clean up if we were interrupted while waiting
            this.started = true;    // so stop() will do the work
            stop();
            throw ex;               // pass it on
            }
        }

    /** Returns whether the thread is, for the moment, started */
    public synchronized boolean isStarted()
        {
        return this.started;
        }

    /** Requests that the thread stop at its earliest available opportunity */
    public synchronized void requestStop()
        {
        this.stopRequested = true;
        if (this.thread != null)
            this.thread.interrupt();
        }

    /** Stops the thread, if currently running. Blocks until thread terminates. */
    public synchronized void stop()
        {
        if (this.started)
            {
            try {
                this.stopRequested = true;
                this.thread.interrupt();
                this.thread.join();
                }
            catch (InterruptedException ignored)
                {
                }
            finally
                {
                this.thread  = null;
                this.started = false;
                }
            }
        }

    /** block until the thread terminates. Note that this does not itself stop the thread */
    public void join() throws InterruptedException
        {
        Thread thread;
        synchronized (this)
            {
            thread = this.thread;
            }
        if (thread != null)
            thread.join();
        }

    public void join(int ms) throws InterruptedException
        {
        Thread thread;
        synchronized (this)
            {
            thread = this.thread;
            }
        if (thread != null)
            thread.join(ms);
        }
    //----------------------------------------------------------------------------------------------
    // For use by thread
    //----------------------------------------------------------------------------------------------

    /** called by the thread to indicate that he's alive and well */
    public void handshake()
        {
        this.setEvent();
        }

    /** Returns whether the thread has been asked to stop */
    public synchronized boolean stopRequested()
        {
        return this.stopRequested || (this.thread != null && this.thread.isInterrupted());
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    class Run implements Runnable
        {
        @Override public void run()
            {
            shakeable.run(HandshakeThreadStarter.this);
            }
        }

    void resetEvent()
        {
        synchronized (this)
            {
            // Make the semaphore have zero permits. Thus, subsequent
            // acquirers will have to wait
            this.semaphore.drainPermits();
            }
        }

    void waitEvent() throws InterruptedException
        {
        synchronized (this)
            {
            this.semaphore.acquire();       // get a permit
            this.semaphore.release();       // give it back
            }
        }

    void setEvent()
        {
        synchronized (this)
            {
            // Make the semaphore have one permit
            this.semaphore.drainPermits();
            this.semaphore.release();
            }
        }
    }

