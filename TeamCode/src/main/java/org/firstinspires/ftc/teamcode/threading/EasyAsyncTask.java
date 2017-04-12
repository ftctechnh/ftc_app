package org.firstinspires.ftc.teamcode.threading;

import android.os.AsyncTask;

import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class EasyAsyncTask extends AsyncTask<Void, Void, Void>
{
    private String name;
    public EasyAsyncTask(String name)
    {
        super();

        this.name = name;
    }

    /***** OBJECT SPECIFIC STUFF *****/
    @Override
    protected Void doInBackground (Void... params)
    {
        try
        {
            taskToAccomplish ();
        }
        catch (InterruptedException e)
        {
            stopEasyTask ();
        }

        return null;
    }

    /******** CUSTOM WRITTEN ********/
    public void startEasyTask () throws InterruptedException
    {
        try
        {
            if (getStatus () != Status.RUNNING)
            {
                this.executeOnExecutor (AsyncTask.THREAD_POOL_EXECUTOR);
                ConsoleManager.outputNewSequentialLine ("Started " + name + " AsyncTask!");
            }
        }
        catch (Exception e)
        {
            ConsoleManager.outputNewSequentialLine ("Error while attempting to start " + name + " AsyncTask!  Trying again...");
            ProgramFlow.pauseForMS (200);
            startEasyTask ();
        }
    }
    public void stopEasyTask ()
    {
        try
        {
            if (getStatus () != Status.FINISHED)
            {
                output = 0;
                taskOnCompletion ();
                this.cancel (true);

                ConsoleManager.outputNewSequentialLine ("Stopped " + name + " AsyncTask!");
            }
        }
        catch (Exception e)
        {
            ConsoleManager.outputNewSequentialLine ("Error while attempting to stop " + name + " AsyncTask!  Trying again...");
        }
    }

    //Can't just create a variable that is not final in an anonymous class, so it has to be declared here.
    public double output = 0;

    //Each method is handled in an anonymous class.
    protected abstract void taskToAccomplish() throws InterruptedException;
    protected void taskOnCompletion() {}
}
