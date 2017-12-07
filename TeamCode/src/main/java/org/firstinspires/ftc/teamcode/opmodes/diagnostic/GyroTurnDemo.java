package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 12/1/2017.
 */

@Autonomous(name="Turn Testing", group="test")
public class GyroTurnDemo extends OpMode {
    BotHardware bot = new BotHardware(this);

    AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    //parameters for gyro PID, but cranked up
    float Kp5 = 0.1f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.05f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 3.0f;    // maximum angle error for which we update integrator

    SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

    public void init() {
        mSeq.add(new AutoLib.RunUntilStopStep(
                new AutoLib.AzimuthTimedDriveStep(this, 90, bot.getHeadingSensor(), motorPID, bot.getMotorVelocityShimArray(), 270.0f, 10, true, -450.0f, 450.0f),
                new GyroStopStep(bot.getHeadingSensor(), 90, 3)
        ));

        mSeq.add(new AutoLib.AzimuthTimedDriveStep(this, 90, bot.getHeadingSensor(), motorPID, bot.getMotorVelocityShimArray(), 270.0f, 2.0f, true, -450.0f, 450.0f));

        bot.init();

    }

    public void start() {

    }

    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
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
