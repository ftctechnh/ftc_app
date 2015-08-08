package org.swerverobotics.library.internal;

import org.swerverobotics.library.exceptions.*;

/**
 * Thunks derived from ThunkForWriting are used for thunking write operations. Note
 * that they queue up their work but do not synchronously wait for that work's execution
  * before returning from dispatch() to the caller.
 */
public abstract class ThunkForWriting extends ThunkBase
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkForWriting() 
        { 
        this.addActionKey
            (
            SynchronousThreadContext.getThreadContext().actionKeyWritesFromThisThread
            );
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public void doWriteOperation()
        {
        this.doWriteOperation(null);
        }

    public void doWriteOperation(IThunkedReadWriteListener writer)
        {
        // Don't bother doing more work if this thread has been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            try
                {
                // Let any writer know we are about to write
                if (writer != null)
                    {
                    this.addActionKey(writer.getListenerWriteThunkKey());
                    writer.enterWriteOperation();
                    }

                this.dispatch();
                }
            catch (InterruptedException e)
                {
                // Same as below
                Thread.currentThread().interrupt();
                throw SwerveRuntimeException.Wrap(e);
                }
            catch (RuntimeInterruptedException e)
                {
                // Tell the current thread that he should shut down soon
                Thread.currentThread().interrupt();

                // Since callers generally do reads as well as writes, and so
                // must deal with the necessity we have in reads of throwing,
                // we may as well throw here as well, as that will help shut
                // things down sooner.
                throw SwerveRuntimeException.Wrap(e);
                }
            }
        }
    }