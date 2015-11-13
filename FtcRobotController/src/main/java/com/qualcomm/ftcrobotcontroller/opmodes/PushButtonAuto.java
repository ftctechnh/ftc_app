package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import junit.framework.Test;

/**
 * Created by Peter on 10/8/2015.
 */
 abstract public class PushButtonAuto extends OpMode
{
<<<<<<< HEAD

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
=======
    /*public void runOpMode()
    {
        moveStraightLineDist(48.0f, .75f,  true);
        spinOnCenter(90.0f, .5f,  true);
        moveStraightLineDist(48.0f, .75f,   true);
        spinOnCenter(90f, .5f,  false);
        moveStraightLineDist(24.0f, .75f,true);
        spinOnCenter(90.0f, .5f,  true);
        moveStraightLineDist(24.0f, .75f, true);
        senseColor();
        if (false)
>>>>>>> 7cb1cc7b1cb19b48d62a6258617511c8711114e7
            {
                   pushBotton(true);
            }
        if (false)
                {
<<<<<<< HEAD
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
=======
                  pushButton(false);
                }
        spinOnCenter(48.0f, .75f, true);
        moveStraightLineDist(24.0f, .75f, true);
    }*/
>>>>>>> 7cb1cc7b1cb19b48d62a6258617511c8711114e7
}
