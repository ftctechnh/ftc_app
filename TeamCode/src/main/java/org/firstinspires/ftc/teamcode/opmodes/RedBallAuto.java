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
                mStart.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 0.5, false));

                AutoLib.Sequence mEnd = new AutoLib.LinearSequence();
                //turn left
                mEnd.add(new AutoLib.GyroTurnStep(this, -90, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 270.0f, 3.0f, true));
                //drive backwards
                int count = 1600;
                if(getLastVuMark() != null){
                    if(getLastVuMark() == RelicRecoveryVuMark.CENTER) count += 300;
                    else if (getLastVuMark() == RelicRecoveryVuMark.LEFT) count += 600;
                }

                mEnd.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 180.0f, count, true));
                //turn back
                mEnd.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 270.0f, 3.0f, true));
                //dump block
                mEnd.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.garyDown, 0.5, false));

                float power = 0.2f;
                if(isRed) power = -power;

                mLeftRedSeq.add(mStart);
                mLeftRedSeq.add(new AutoLib.TurnByTimeStep(bot.getMotorRay(), -power, power, 1.5, true));
                mLeftRedSeq.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.5, false));
                mLeftRedSeq.add(mEnd);

                mLeftBlueSeq.add(mStart);
                mLeftBlueSeq.add(new AutoLib.TurnByTimeStep(bot.getMotorRay(), power, -power, 1.5, true));
                mLeftBlueSeq.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.5, false));
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
