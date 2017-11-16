package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 11/9/2017.
 */
@Autonomous(name="Stick Test")
public class RedBallAuto extends VuforiaBallLib {
    BotHardware bot = new BotHardware(this);
    AutoLib.Sequence mLeftRedSeq = new AutoLib.LinearSequence();
    AutoLib.Sequence mLeftBlueSeq = new AutoLib.LinearSequence();

    VuforiaBallLib.BallColor ballDetected;

    @Override
    public void init() {
        bot.init();
        initVuforia(true);

        AutoLib.Sequence mStart = new AutoLib.LinearSequence();
        //half lower
        mStart.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown - 0.1, 0.5, false));
        //drive forward
        mStart.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), 0.2, 0.2, true));
        //full lower
        mStart.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 0.5, false));

        AutoLib.Sequence mEnd = new AutoLib.LinearSequence();
        //back up
        mEnd.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), -0.2, 0.4, true));

        mLeftRedSeq.add(mStart);
        mLeftRedSeq.add(new AutoLib.TurnByTimeStep(bot.getMotorRay(), -0.2, 0.2, 1.0, true));
        mLeftRedSeq.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.5, false));
        mLeftRedSeq.add(new AutoLib.TurnByTimeStep(bot.getMotorRay(), 0.2, -0.2, 1.0, true));
        mLeftRedSeq.add(mEnd);

        mLeftBlueSeq.add(mStart);
        mLeftBlueSeq.add(new AutoLib.TurnByTimeStep(bot.getMotorRay(), 0.2, -0.2, 1.0, true));
        mLeftBlueSeq.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.5, false));
        mLeftBlueSeq.add(new AutoLib.TurnByTimeStep(bot.getMotorRay(), -0.2, 0.2, 1.0, true));
        mLeftBlueSeq.add(mEnd);
    }

    @Override
    public void start() {
        startTracking();
    }

    @Override
    public void loop() {
        if (ballDetected == null || ballDetected == BallColor.Indeterminate || ballDetected == BallColor.Undefined) ballDetected = getBallColor();
        else if(ballDetected == BallColor.LeftRed && mLeftRedSeq.loop()) requestOpModeStop();
        else if(ballDetected == BallColor.LeftBlue && mLeftBlueSeq.loop()) requestOpModeStop();
    }

    @Override
    public void stop() {

    }
}
