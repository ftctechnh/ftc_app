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
        telemetry.addData("initBegin", -2);
        compBot = new CompBotOpMode(hardwareMap);
        step = 1;
        telemetry.addData("initEnd", -1);
    }

    @Override
    public void loop()
    {
        telemetry.addData("begin", 0);
        telemetry.addData("encoderValue = ", compBot.getRightMotor().getCurrentPosition());
        if(compBot.doneWithPrev())
        {
            telemetry.addData("firstStep ", 1);
            switch(step)
            {
                case 1:
                    compBot.moveStraightEncoders(5, (float).9);
                    telemetry.addData("firstCase ", 2);
                    break;

                case 2:
                    compBot.moveStraightEncoders(-5,(float).9);
                    telemetry.addData("secondCase ", 3);
                    break;
                case 3:
                    compBot.moveStraightEncoders(5,(float).9);
                    telemetry.addData("thirdCase ", 4);
                    break;
                default:
                    compBot.stop();
                    telemetry.addData("fourthCase ", 5);
                    break;
            }
            step++;
            telemetry.addData("stepped ", 6);
        }
    }

    @Override
    public void moveStraightEncoders(float inches, float speed) { }

    @Override
    public void spinOnCenter(float degrees, float speed) { }
}
