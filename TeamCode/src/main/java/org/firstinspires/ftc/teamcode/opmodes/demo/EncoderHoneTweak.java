package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.EncoderHoneStep;
import org.firstinspires.ftc.teamcode.libraries.GyroCorrectStep;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.UDPid;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.opmodes.ADPSAuto;
import org.firstinspires.ftc.teamcode.opmodes.UltraAuto;

/**
 * Created by Noah on 3/27/2018.
 * Tweaking encoder pid values using the internet!
 */


@TeleOp(name="Wifi Encoder PID Tweak")
@Disabled
public class EncoderHoneTweak extends OpMode {
    private final BotHardware bot = new BotHardware(this);
    private final UDPid udPid = new UDPid();
    private final SensorLib.PID gyroTurnPID = new SensorLib.PID(-16.0f, 0, 0, 0);
    private static final float POWER = 100;
    private static final float POWER_MIN = 55;
    private static final float POWER_MAX = 720;
    private static final int COUNTS = 500;

    private AutoLib.Sequence step;

    private boolean drivingBackward = false;

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
        step = makeEncoderStep(drivingBackward);
    }

    public void loop() {
        if(step.loop()) step = makeEncoderStep(drivingBackward = !drivingBackward);
        telemetry.addData("P", udPid.getP() * 100);
        telemetry.addData("I", udPid.getI() * 1000);
        telemetry.addData("D", udPid.getD() * 100);
    }

    public void stop() {
        bot.stopAll();
        udPid.shutdown();
    }

    private GyroCorrectStep makeGyroDriveStep(float heading, SensorLib.PID pid, float power, float powerMin, float powerMax) {
        return new GyroCorrectStep(this, heading, bot.getHeadingSensor(), pid, bot.getMotorVelocityShimArray(), power, powerMin, powerMax, 0.5f);
    }

    private AutoLib.Sequence makeEncoderStep(boolean reversed) {
        AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
        mSeq.add(new EncoderHoneStep(this, reversed ? -COUNTS : COUNTS, 5, 25,
                new SensorLib.PID((float)udPid.getP() * 100, (float)udPid.getI() * 1000, (float)udPid.getD() * 100, 50),
                makeGyroDriveStep(0, gyroTurnPID, POWER, POWER_MIN, POWER_MAX),
                new DcMotor[] {BotHardware.Motor.frontLeft.motor, BotHardware.Motor.frontRight.motor}));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
        return mSeq;
    }
}
