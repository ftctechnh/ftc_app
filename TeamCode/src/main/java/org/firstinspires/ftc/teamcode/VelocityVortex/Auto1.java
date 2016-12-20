package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Matthew Fan on 10/3/2016.
 */

@Autonomous(name="" , group="")

public class Auto1 extends AutonomousBase
{
    private Auto1Thread t;

    private boolean threadStarted = false;

    @Override
    public void init()
    {
        super.init();
        t = new Auto1Thread();
    }

    @Override
    public void loop()
    {
        if(!threadStarted)
        {
            t.start();
            threadStarted = true;
        }
    }

    @Override
    public void stop()
    {

    }
}
