/**
 * This code enables the creation of AsyncTasks easily, which is required for advanced robot functionality.
 */

package org.firstinspires.ftc.teamcode.threads;

import android.os.AsyncTask;

import org.firstinspires.ftc.teamcode.console.NiFTConsole;

public abstract class NiFTAsyncTask extends AsyncTask <Void, Void, Void>
{
    //Creates a task with the given NAME, and creates a new process console for the task.
    private final String taskName;
    protected final NiFTConsole.ProcessConsole processConsole;
    //Default constructor, initializes with default NAME.
    public NiFTAsyncTask()
    {
        this("Unnamed NiFT Task");
    }
    public NiFTAsyncTask (String taskName)
    {
        this.taskName = taskName;
        processConsole = new NiFTConsole.ProcessConsole (taskName);
    }

    /**
     * Runs the onBeginTask() method while catching potential InterruptedExceptions, which indicate that the user has requested a stop which was thrown in NiFTFlow.
     *
     * Runs the onQuitAndDestroyConsole() method on catching an InterruptedException, which destroys the process console and ends the program.
     */
    @Override
    protected Void doInBackground (Void... params)
    {
        try
        {
            onBeginTask ();
        }
        catch (InterruptedException e) //Upon stop requested by NiFTFlow
        {
            onQuitAndDestroyConsole ();
        }

        return null;
    }

    @Override
    protected void onCancelled ()
    {
        onQuitAndDestroyConsole ();
    }

    //Must be overriden.
    protected abstract void onBeginTask () throws InterruptedException;

    /**
     * Used solely in this class, used to destroy the created process console and THEN
     * run the desired onCompletion method.
     */
    private void onQuitAndDestroyConsole ()
    {
        onQuitTask ();
        processConsole.destroy ();
    }
    //You can optionally choose whether to implement this method in your derived classes.
    protected void onQuitTask () {}

    /**
     * run() attempts to run the program in a try-catch block, and in the event of an
     * error, stops the attempt and returns an error to the user.
     *
     * stop() attempts to stop the program in a similar manner.
     *
     * Example scenarios of error could be when the user has already run a task instance
     * and has to create a new one, or cancelling a cancelled task.
     */
    public void run()
    {
        try
        {
            this.executeOnExecutor (AsyncTask.THREAD_POOL_EXECUTOR);
        }
        catch (Exception e)
        {
            NiFTConsole.outputNewSequentialLine ("Uh oh! " + taskName + " can't run!");
            NiFTConsole.outputNewSequentialLine (e.getMessage ());
            NiFTConsole.outputNewSequentialLine ("Proceeding normally.");
        }
    }
    public void stop()
    {
        try
        {
            this.cancel (true);
        }
        catch (Exception e)
        {
            NiFTConsole.outputNewSequentialLine ("Uh oh! " + taskName + " couldn't be stopped!");
            NiFTConsole.outputNewSequentialLine (e.getMessage ());
            NiFTConsole.outputNewSequentialLine ("Proceeding normally.");
        }
    }
}
