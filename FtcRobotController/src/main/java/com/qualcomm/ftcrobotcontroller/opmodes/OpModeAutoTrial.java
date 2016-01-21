package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaitlin on 1/15/16.
 */
public class OpModeAutoTrial extends OpMode implements DriverInterface
{
    CompBotOpMode compBot;
    int step;

    @Override
    public void init()
    {
        int step = 1;
    }

    @Override
    public void loop()
    {
        if(compBot.doneWithPrev())
        {
            switch(step)
            {
                case 1:
                    compBot.moveStraightEncoders(5, (float).9);
                    break;

                case 2:
                    compBot.moveStraightEncoders(-5,(float).9);
                    break;
                case 3:
                    compBot.moveStraightEncoders(5,(float).9);
                    break;
                default:
                    compBot.stop();
                    break;
            }
            step++;
        }
    }

    @Override
    public void moveStraightEncoders(float inches, float speed) { }

    @Override
    public void spinOnCenter(float degrees, float speed) { }
}
