package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 11/9/2017.
 */
//@Autonomous(name="Red Ball")
public class RedBallAuto extends VuforiaBallLib {
    BotHardware bot = new BotHardware(this);
    AutoLib.Sequence mLeftRedSeq = new AutoLib.LinearSequence();
    AutoLib.Sequence mLeftBlueSeq = new AutoLib.LinearSequence();

    VuforiaBallLib.BallColor ballDetected;

    private boolean en = false;

    protected boolean isRed = true;

    //parameters for gyro PID, but cranked up
    float Kp5 = 0.1f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.05f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 3.0f;    // maximum angle error for which we update integrator

    SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

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
                mStart.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenter, 1.0, false));
                mStart.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 1.0, false));

                AutoLib.Sequence mEnd = new AutoLib.LinearSequence();
                //reset stick
                mEnd.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 1.0, false));
                mEnd.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 1.0, false));
                //drive forward into parking zone
                final int mult = isRed ? -1 : 1;
                mEnd.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 180.0f * mult, 1600, true));
                mEnd.add(new AutoLib.RunUntilStopStep(
                        new AutoLib.TurnByTimeStep(bot.getMotorVelocityShimArray(), 180.0f * mult, -180.0f * mult, 3.0, true),
                        new GyroStopStep(bot.getHeadingSensor(), 90 * mult, 3)
                ));
                mEnd.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -180.0f, 200, true));
                mEnd.add(bot.getDropStep());
                mEnd.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 180.0f, 200, true));

                double mod = 0.2;
                if(!isRed) mod = -mod;

                mLeftRedSeq.add(mStart);
                mLeftRedSeq.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenter + mod, 1.5, true));
                mLeftRedSeq.add(mEnd);

                mLeftBlueSeq.add(mStart);
                mLeftBlueSeq.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenter - mod, 1.5, true));
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


    private static class GyroStopStep extends AutoLib.Step {
        private final HeadingSensor gyro;
        private final float heading;
        private final int error;
        GyroStopStep(HeadingSensor gyro, float heading, int error) {
            this.gyro = gyro;
            this.heading = heading;
            this.error = error;
        }

        public boolean loop() {
            return Math.abs(gyro.getHeading() - heading) <= error;
        }
    }
}
