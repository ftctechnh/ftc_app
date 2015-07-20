package org.swerverobotics.library.thunking;

import org.swerverobotics.library.exceptions.*;

/**
 * ThunkedBase is the base class for all our several thunked objects
 */
public class ThunkedBase
    {
    //----------------------------------------------------------------------------------------------
    // Dispatching
    //----------------------------------------------------------------------------------------------

    protected void enterReadOperation() throws InterruptedException
        {
        /* subclass hook */
        }

    protected void enterWriteOperation() throws InterruptedException
        {
        /* subclass hook */
        }

    protected <T> T doReadOperation(final ResultableThunk<T> thunk)
        {
        // Don't bother doing more work if we've been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            T result = null;
            try
                {
                this.enterReadOperation();
                thunk.dispatch();
                result = thunk.result;
                }
            catch (InterruptedException e)
                {
                // Tell the current thread that he should shut down soon
                Thread.currentThread().interrupt();

                // Our signature (and that of our caller) doesn't allow us to throw
                // InterruptedException. But we can't actually return a value to our caller,
                // as we have nothing to return. So, we do the best we can, and throw SOMETHING.
                throw SwerveRuntimeException.Wrap(e);
                }
            return result;
            }
        else
            {
            // Translate the isInterrupted into an exception, as we have to throw, since
            // we have no value we can possibly return
            throw new RuntimeInterruptedException();
            }
        }

    protected void doWriteOperation(final NonwaitingThunk thunk)
        {
        // Don't bother doing more work if this thread has been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            try
                {
                this.enterWriteOperation();
                thunk.dispatch();
                }
            catch (InterruptedException e)
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
