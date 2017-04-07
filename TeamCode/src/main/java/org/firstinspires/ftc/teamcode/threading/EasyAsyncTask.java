package org.firstinspires.ftc.teamcode.threading;

import android.os.AsyncTask;

import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class EasyAsyncTask extends AsyncTask<String, Void, String>
{
    /***** OBJECT SPECIFIC STUFF *****/
    //OH MY GOD YOU CAN JUST DO String a, String b, String c, etc. rather than String[]?????  HOW DID I NOT KNOW THIS
    @Override
    protected String doInBackground (String... params)
    {
        try
        {
            taskToAccomplish ();
        }
        catch (InterruptedException e)
        {
            taskOnCompletion ();
            return "Ended early!";
        }

        return "Success!";
    }

    @Override
    protected void onPostExecute(String result) {}

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    /******** CUSTOM WRITTEN ********/

    //Runs and stops the program.
    public void run()
    {
        if (getStatus () != Status.RUNNING)
            this.execute ("");
    }
    public void stop()
    {
        taskOnCompletion ();

        if (getStatus () != Status.FINISHED)
            this.cancel (true);
    }

    //Can't just create a variable that is not final in an anonymous class, so it has to be declared here.
    public Object output = 0;

    //Each method is handled in an anonymous class.
    protected abstract void taskToAccomplish() throws InterruptedException;
    protected void taskOnCompletion() {}
}
