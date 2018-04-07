package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.StupidColor;
import org.firstinspires.ftc.teamcode.opmodes.ADPSAuto;
import org.firstinspires.ftc.teamcode.opmodes.UltraAuto;

/**
 * Created by Noah on 4/6/2018.
 */

@Autonomous(name = "Line Color Stop")
public class LineStopTest extends OpMode {
    private static final float RED_RATIO_MIN = 3f;
    private final SensorLib.PID encoderHonePID = new SensorLib.PID(3.88f, 9.00f, 0, 50);
    private final SensorLib.PID gyroDrivePID = new SensorLib.PID(-64.0f, 0, 0, 0);

    private final BotHardware bot = new BotHardware(this);
    private StupidColor color;
    private boolean lineFound = false;

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    public void init() {
        bot.init();
        bot.start();

        color = new StupidColor(hardwareMap.get(AdafruitI2cColorSensor.class, "fc"));
        color.setGain(AMSColorSensor.Gain.GAIN_64);
    }

    public void start() {
        bot.setRightDrive(0.5f);
        bot.setLeftDrive(0.5f);
    }

    public void loop() {
        if(!lineFound && (double)color.red() / ((double)(color.blue() + color.green()) / 2.0) >= RED_RATIO_MIN) {
            //read position
            final int counts = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
            //stop motors
            bot.setLeftDrive(0);
            bot.setRightDrive(0);
            //wait until velocity is zero
            while (BotHardware.Motor.frontLeft.motor.getVelocity(AngleUnit.DEGREES) > 0);
            //create encoder hone step
            final ADPSAuto.GyroCorrectStep step = makeGyroDriveStep(0, gyroDrivePID, 600, 720, 55);
            mSeq.add(new UltraAuto.EncoderHoneStep(this, counts - BotHardware.Motor.frontLeft.motor.getCurrentPosition(), 5, 5, encoderHonePID, step, new DcMotor[]{BotHardware.Motor.frontLeft.motor}));
            lineFound = true;
        }
        else if(lineFound) mSeq.loop();
    }

    public void stop() {
        bot.stopAll();
    }

    private ADPSAuto.GyroCorrectStep makeGyroDriveStep(float heading, SensorLib.PID pid, float power, float powerMin, float powerMax) {
        return new ADPSAuto.GyroCorrectStep(this, heading, bot.getHeadingSensor(), pid, bot.getMotorVelocityShimArray(), power, powerMin, powerMax);
    }
}
