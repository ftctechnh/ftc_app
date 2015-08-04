package org.swerverobotics.library.thunking;

import org.swerverobotics.library.exceptions.*;

/**
 * Thunks derived from ResultableThunk have a member variable named 'result
 * which can be set inside of actionOnLoopThread() in order to return data
 * back to the caller of dispatch().
 */
public abstract class ResultableThunk<T> extends WaitingThunk
    {
    public ResultableThunk() { }
    public ResultableThunk(int actionKey) { super(actionKey); }
    
    public T result;

    public T doReadOperation()
        {
        return this.doReadOperation(null);
        }

    public T doReadOperation(IThunkedReadWrite reader)
        {
        // Don't bother doing more work if we've been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            try
                {
                // Let any reader know that we are about to read
                if (reader != null)
                    {
                    this.actionKey = reader.getListenerReadThunkKey();
                    reader.enterReadOperation();
                    }

                this.dispatch();
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
            return this.result;
            }
        else
            {
            // Translate the isInterrupted into an exception, as we have to throw, since
            // we have no value we can possibly return
            throw new RuntimeInterruptedException();
            }
        }
    }