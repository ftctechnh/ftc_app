package org.firstinspires.ftc.teamcode.threading;

import android.os.AsyncTask;

import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class EasyAsyncTask extends AsyncTask<String, Void, String>
{
    /***** OBJECT SPECIFIC STUFF *****/

    public EasyAsyncTask ()
    {
        this.execute ("");
    }

    //OH MY GOD YOU CAN JUST DO String a, String b, String c, etc. rather than String[]?????  HOW DID I NOT KNOW THIS
    @Override
    protected String doInBackground (String... params)
    {
        String output = "";
        try
        {
            output = taskToAccomplish ();
        }
        catch (InterruptedException e)
        {
            ConsoleManager.outputNewLineToDrivers ("Ended early!");
        }

        return output;
    }

    @Override
    protected void onPostExecute(String result) {}

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    //Changed by other classes.
    protected abstract String taskToAccomplish() throws InterruptedException;
}
