package org.firstinspires.ftc.teamcode.TestCode.ParallelCommandTest;

import android.util.Log;

import org.firstinspires.ftc.teamcode.Core.RobotCommand;


public class CountCommand extends RobotCommand
{
    private boolean _running = false;
    private boolean _stop = false;

    @Override
    public void runSequentially()
    {
        for(int i = 0; i < 5; i ++)
        {
            Log.i("Command" , Integer.toString(i));
        }
    }


    @Override
    public void runParallel()
    {
        if(t == null)
        {
            _running  = true;
            _stop = false;

            t = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    for(int i = 0; !_stop; i++)
                    {
                        Log.i("Command" , Integer.toString(i));

                        try
                        {
                            t.sleep(1_000);
                        }
                        catch(Exception e){}
                    }
                }
            });

            t.start();
        }
    }


    @Override public void stop()
    {
        _stop = true;
        _running = false;
        t = null;
    }


    public boolean isRunning()
    {
        return _running;
    }
}
