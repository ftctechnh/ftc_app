package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
        compBot = new CompBotOpMode(hardwareMap);
        compBot.stop();
        step = 1;
        telemetry.addData("init",0);
    }

    @Override
    public void loop()
    {
        telemetry.addData("start loop",1);

        if(compBot.doneWithPrev())
        {
            telemetry.addData("done with prevjherwhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhgre",2);

            switch(step)
            {
                case 1:
                    telemetry.addData("move straight",1);
                    compBot.moveStraightEncoders(5, (float) .9);
                    //compBot.stop();
                    break;

                case 2:
                    telemetry.addData("move back", 2);
                    //compBot.moveStraightEncoders(-5, (float) .9);
                    //compBot.stop();
                    break;
                case 3:
                    telemetry.addData("move forward 2", 3);
                    //compBot.moveStraightEncoders(5, (float) .9);
                    //compBot.stop();
                    break;
                default:
                    telemetry.addData("default", step);
                    //compBot.stop();
                    break;
            }
            step++;
        }
        else
        {
            telemetry.addData("running step",step);
            telemetry.addData("Current is", compBot.returnCurrent());
            telemetry.addData("Target is", compBot.returnTarget());
        }

    }

    @Override
    public void moveStraightEncoders(float inches, float speed) { }


    public void spinOnCenter(float degrees, float speed) { }
}
