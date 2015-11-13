package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import junit.framework.Test;

/**
 * Created by Peter on 10/8/2015.
 */
 abstract public class PushButtonAuto extends OpMode
{

    TestBot testBot;

    public void init()
    {
        testBot = new TestBot(hardwareMap);
    }

    public void start()
    {
       /* testBot.moveStraightEncoders(48.0f, .75f);
        testBot.spinOnCenter(90.0f, .5f, true);
        testBot.moveStraightEncoders(48.0f, .75f);
        testBot.spinOnCenter(90f, .5f, false);
        testBot.moveStraightEncoders(24.0f, .75f);
        testBot.spinOnCenter(90.0f, .5f, true);
        testBot.moveStraightEncoders(24.0f, .75f);
        senseColor();
        if (//parameter for red)
            {
                   pushBotton(true)
            }
        if (//parameter for blue)
                {
                  pushButton(false)
                };
        spinOnCenter(48.0f, .75f, true);
        testBot.moveStraightEncoders(24.0f, .75f);*/
    }

    public void loop()
    {

    }

    public void stop()
    {
        testBot.stop();
    }
}
