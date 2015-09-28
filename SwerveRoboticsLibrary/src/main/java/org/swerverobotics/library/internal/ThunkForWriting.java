package org.swerverobotics.library.internal;

import org.swerverobotics.library.exceptions.*;
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
        SynchronousThreadContext.assertSynchronousThread();
        this.addActionKey
            (
            SynchronousThreadContext.getThreadContext().actionKeyWritesFromThisThread
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

    protected void doUntrackedWriteOperation(IInterruptableAction actionBeforeDispatch)
        {
        // Don't bother doing more work if this thread has been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            try
                {
                if (actionBeforeDispatch != null)
                    actionBeforeDispatch.doAction();

                this.dispatch();
                }
            catch (InterruptedException | RuntimeInterruptedException e)
                {
                // Tell the current thread that he should shut down soon
                Util.handleCapturedInterrupt(e);

                // Since callers generally do reads as well as writes, and so
                // must deal with the necessity we have in reads of throwing,
                // we may as well throw here as well, as that will help shut
                // things down sooner.
                throw SwerveRuntimeException.wrap(e);
                }
            }
        }
    
    public void doWriteOperation()
        {
        this.doWriteOperation(null);
        }

    public void doWriteOperation(final IThunkedReadWriteListener writer)
        {
        this.doUntrackedWriteOperation(new IInterruptableAction()
        {
        @Override public void doAction() throws InterruptedException
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