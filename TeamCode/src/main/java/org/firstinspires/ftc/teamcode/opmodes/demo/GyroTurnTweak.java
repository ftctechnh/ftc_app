package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.UDPid;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 4/4/2018.
 */

@Autonomous(name="Gyro PID Tweak")
@Disabled
public class GyroTurnTweak extends OpMode {
    private final BotHardware bot = new BotHardware(this);
    private final UDPid udPid = new UDPid();

    private AutoLib.Sequence step;
    private boolean reversed = false;

    public void init() {
        udPid.beginListening();
        bot.init();
        bot.start();
    }

    public void init_loop() {
        telemetry.addData("P", udPid.getP());
        telemetry.addData("I", udPid.getI());
        telemetry.addData("D", udPid.getD());
    }

    public void start() {
        step = makeTurnStep(reversed);
    }

    public void loop() {
        if(step.loop()) step = makeTurnStep(reversed = !reversed);
        telemetry.addData("P", udPid.getP());
        telemetry.addData("I", udPid.getI());
        telemetry.addData("D", udPid.getD());
    }

    public void stop() {
        bot.stopAll();
        udPid.shutdown();
    }

    private AutoLib.Sequence makeTurnStep(boolean reversed) {
        AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
        mSeq.add(new AutoLib.GyroTurnStep(this, reversed ? 0 : 180, bot.getHeadingSensor(), bot.getMotorRay(), 0.045f, 0.7f, new SensorLib.PID((float)udPid.getP(), (float)udPid.getI(), (float)udPid.getD(), 7.0f), 2f, 5, true));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
        return mSeq;
    }
}
