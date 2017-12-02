package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 11/9/2017.
 */
@Autonomous(name="Red Ball")
public class RedBallAuto extends VuforiaBallLib {
    BotHardware bot = new BotHardware(this);
    AutoLib.Sequence mLeftRedSeq = new AutoLib.LinearSequence();
    AutoLib.Sequence mLeftBlueSeq = new AutoLib.LinearSequence();

    VuforiaBallLib.BallColor ballDetected;

    private boolean en = false;

    protected boolean isRed = true;

    @Override
    public void init() {
        bot.init();
        initVuforia(true);
    }

    @Override
    public void start() {
        startTracking();
    }

    @Override
    public void loop() {
        if (ballDetected == null || ballDetected == BallColor.Indeterminate || ballDetected == BallColor.Undefined) ballDetected = getBallColor();
        else{
            if(!en){
                AutoLib.Sequence mStart = new AutoLib.LinearSequence();
                //full lower
                mStart.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenter, 0.5, false));
                mStart.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 0.5, false));

                AutoLib.Sequence mEnd = new AutoLib.LinearSequence();
                mEnd.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.5, false));
                mEnd.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.5, false));

                double mod = 0.2;
                if(isRed) mod = -mod;

                mLeftRedSeq.add(mStart);
                mLeftRedSeq.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenter + mod, 0.8, true));
                mLeftRedSeq.add(mEnd);

                mLeftBlueSeq.add(mStart);
                mLeftBlueSeq.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenter - mod, 0.8, true));
                mLeftBlueSeq.add(mEnd);
                en = true;
            }

            if(ballDetected == BallColor.LeftRed && mLeftRedSeq.loop()) requestOpModeStop();
            else if(ballDetected == BallColor.LeftBlue && mLeftBlueSeq.loop()) requestOpModeStop();
        }
    }

    @Override
    public void stop() {

    }
}
