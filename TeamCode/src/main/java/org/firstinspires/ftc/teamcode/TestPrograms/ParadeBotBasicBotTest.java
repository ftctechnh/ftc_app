package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.TestPrograms.ParadeBotUsingBasicBot;

@Autonomous(name = "CalibrateNew pivs")
@Disabled
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