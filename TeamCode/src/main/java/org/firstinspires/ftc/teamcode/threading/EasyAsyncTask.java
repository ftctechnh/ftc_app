package org.firstinspires.ftc.teamcode.threading;

import android.os.AsyncTask;

import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class EasyAsyncTask extends AsyncTask<Void, Void, Void>
{
    private String name;
    protected Object[] properties;
    public EasyAsyncTask(String name, Object... properties)
    {
        super();

        this.name = name;

        this.properties = properties;
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

    public void setProperties(Object... properties)
    {
        this.properties = properties;
    }
    public Object[] getProperties ()
    {
        return properties;
    }

    //Each method is handled in an anonymous class.
    protected abstract void taskToAccomplish() throws InterruptedException;
    protected void taskOnCompletion() {}
}
