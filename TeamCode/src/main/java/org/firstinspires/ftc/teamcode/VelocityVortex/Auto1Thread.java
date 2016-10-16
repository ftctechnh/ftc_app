package org.firstinspires.ftc.teamcode.VelocityVortex;

/**
 * Created by dogea on 10/3/2016.
 */

public class Auto1Thread extends AutonomousBase implements Runnable
{
    @Override public void init(){/*Guess what? Nothing!*/}

    @Override public void loop(){/*Hey, hey, what's here? Nothing!*/}

    @Override public void stop(){/*Now's your chance! Guess! That's right, nothing!*/}

    private Thread t;

    public Auto1Thread()
    {

    }

    public void start()
    {
        if(t == null)
        {
            t = new Thread(this);
            t.start();
        }
    }

    public void run()
    {
        driveTo(12f , 75f , 0.5f);
        turn(135f , 0.5f);
    }
}
