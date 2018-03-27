package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
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
public class EncoderHoneTweak extends OpMode {
    private final BotHardware bot = new BotHardware(this);
    private final UDPid udPid = new UDPid();
    private final SensorLib.PID gyroTurnPID = new SensorLib.PID(10.0f, 0, 0, 0);
    private static final float POWER = 360;
    private static final float POWER_MIN = 55;
    private static final float POWER_MAX = 550;
    private static final int COUNTS = 1000;

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
        telemetry.addData("I", udPid.getI() * 100);
        telemetry.addData("D", udPid.getD() * 100);
    }

    public void stop() {
        bot.stopAll();
        udPid.shutdown();
    }

    private ADPSAuto.GyroCorrectStep makeGyroDriveStep(float heading, SensorLib.PID pid, float power, float powerMin, float powerMax) {
        return new ADPSAuto.GyroCorrectStep(this, heading, bot.getHeadingSensor(), pid, bot.getMotorRay(), power, powerMin, powerMax);
    }

    private AutoLib.Sequence makeEncoderStep(boolean reversed) {
        AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
        mSeq.add(new UltraAuto.EncoderHoneStep(this, reversed ? -COUNTS : COUNTS, 10, 10,
                new SensorLib.PID((float)udPid.getP() * 100, (float)udPid.getI() * 100, (float)udPid.getD() * 100, 0),
                makeGyroDriveStep(0, gyroTurnPID, reversed ? -POWER : POWER, POWER_MIN, POWER_MAX),
                new DcMotor[] {BotHardware.Motor.frontLeft.motor, BotHardware.Motor.frontRight.motor}));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
        return mSeq;
    }
}
