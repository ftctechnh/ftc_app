package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "CalibrateNew pivs")
public class ParadeBotBasicBotTest extends LinearOpMode
{
    ParadeBotUsingBasicBot bot;

    public void runOpMode() throws InterruptedException
    {
        bot = new ParadeBotUsingBasicBot(hardwareMap, this);
        boolean buttonPressed = false;
        waitForStart();
        while (opModeIsActive())
        {
            if (gamepad1.a)
            {
                bot.pivot(120, .8);
            }
            else if (gamepad1.b)
            {
                bot.pivot( -120, .8);
            }
        }
    }
}