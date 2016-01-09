package org.swerverobotics.library.internal;

import org.swerverobotics.library.interfaces.*;

/**
 * Thunks derived from ThunkForWriting are used for thunking write operations. Note
 * that they queue up their work but do not synchronously wait for that work's execution
  * before returning from dispatch() to the caller.
 */
public abstract class ThunkForWriting extends Thunk
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkForWriting() 
        {
        SwerveThreadContext.assertSynchronousThread();
        this.addActionKey
            (
            SwerveThreadContext.getThreadContext().actionKeyWritesFromThisThread
            );
        }
    
    public ThunkForWriting(int actionKey)
        {
        this();
        this.addActionKey(actionKey);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public void doUntrackedWriteOperation()
        {
        this.doUntrackedWriteOperation(null);
        }

    protected void doUntrackedWriteOperation(IInterruptableRunnable actionBeforeDispatch)
        {
        // Don't bother doing more work if this thread has been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            try
                {
                if (actionBeforeDispatch != null)
                    actionBeforeDispatch.run();

                this.dispatch();
                }
            catch (Exception e)
                {
                Util.handleCapturedException(e);
                }
            }
        }
    
    public void doWriteOperation()
        {
        this.doWriteOperation(null);
        }

    public void doWriteOperation(final IThunkedReadWriteListener writer)
        {
        this.doUntrackedWriteOperation(new IInterruptableRunnable()
            {
            @Override public void run() throws InterruptedException
                {
                // Let any writer know we are about to write
                if (writer != null)
                    {
                    ThunkForWriting.this.addActionKey(writer.getListenerWriteThunkKey());
                    writer.enterWriteOperation();
                    }
                }
            });
        }
    }