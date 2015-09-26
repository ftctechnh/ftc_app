package org.swerverobotics.library.internal;

import org.swerverobotics.library.exceptions.*;
import org.swerverobotics.library.interfaces.*;

/**
 * Thunks derived from ResultableThunk have a member variable named 'result
 * which can be set inside of actionOnLoopThread() in order to return data
 * back to the caller of dispatch().
 */
public abstract class ThunkForReading<T> extends Thunk
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public T result;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkForReading() 
        {
        SynchronousThreadContext.assertSynchronousThread();
        }
    public ThunkForReading(int actionKey) 
        { 
        this();
        this.addActionKey(actionKey); 
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------
    
    @Override protected void dispatch() throws InterruptedException
    // Once dispatched, we wait for our own completion, as that's when the
    // the data to be read will be available
        {
        super.dispatch();
        waitForCompletion();
        }
   
    public T doUntrackedReadOperation()
        {
        return this.doUntrackedReadOperation(null);
        }

    public T doUntrackedReadOperation(IInterruptableAction actionBeforeDispatch)
        {
        // Don't bother doing more work if we've been interrupted
        if (!Thread.currentThread().isInterrupted())
            {
            try
                {
                if (actionBeforeDispatch != null)
                    actionBeforeDispatch.doAction();

                this.dispatch();
                }
            catch (InterruptedException|RuntimeInterruptedException e)
                {
                // (Re)tell the current thread that he should shut down soon
                Util.handleCapturedInterrupt(e);

                // Our signature (and that of our caller) doesn't allow us to throw
                // InterruptedException. But we can't actually return a value to our caller,
                // as we have nothing to return. So, we do the best we can, and throw SOMETHING.
                throw SwerveRuntimeException.wrap(e);
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

    public T doReadOperation()
        {
        return this.doReadOperation(null);
        }

    /**
     * Do a tracking read. Tracking reads are most commonly used in classes that
     * do both reading and writing to a LegacyModule-hosted device, where they have
     * to keep track of mode switching.
     */
    public T doReadOperation(final IThunkedReadWriteListener reader)
        {
        return this.doUntrackedReadOperation(new IInterruptableAction()
            {
            @Override public void doAction() throws InterruptedException
                {
                // Let any reader know that we are about to read
                if (reader != null)
                    {
                    ThunkForReading.this.addActionKey(reader.getListenerReadThunkKey());
                    reader.enterReadOperation();
                    }
                }
            });
        }
    }