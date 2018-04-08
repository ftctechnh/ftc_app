package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.GyroCorrectStep;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;
import org.firstinspires.ftc.teamcode.opmodes.ADPSAuto;

/**
 * Created by Noah on 2/14/2018.
 */

@Autonomous(name="Ultra Calibrate")
@Disabled
public class UltraHoneDebug extends OpMode {
    private static final int BACKUP_COUNTS = 980;

    AutoLib.Sequence mSeq = new AutoLib.LinearSequence();
    BotHardware bot = new BotHardware(this);
    protected MatbotixUltra frontUltra;
    protected MatbotixUltra backUltra;

    private int counts = 32;
    private boolean lastDPad = false;
    private boolean done = false;

    private int startCounts = 0;
    private int endCounts = 0;

    //parameters for gyro turning
    float Kp5 = 15.0f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.0f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 0.0f;    // maximum angle error for which we update integrator

    SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

    public void init() {
        frontUltra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultrafront"), 100);
        backUltra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraback"), 100);
        frontUltra.initDevice();
        backUltra.initDevice();
        frontUltra.startDevice();
        backUltra.startDevice();
        bot.init();
    }

    public void init_loop() {
        if(!lastDPad) {
            if(gamepad1.dpad_up) counts++;
            if(gamepad1.dpad_down) counts--;
        }
        lastDPad = gamepad1.dpad_up || gamepad1.dpad_down;
    }

    public void start() {
        //initialize sequence
        mSeq.add(new UltraHoneStep(this, frontUltra, counts, 0, 5,
                new SensorLib.PID(15f, 0.15f, 0, 10),
                new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-16, 0, 0, 0), bot.getMotorVelocityShimArray(), 0.0f, 55.0f, 400.0f)));
        mSeq.add(new AutoLib.GyroTurnStep(this, 90, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 55.0f, 650.0f, motorPID, 0.5f, 10, true));
        mSeq.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stickBase.servo, BotHardware.ServoE.stickBaseCenter, 0.25, false));
        mSeq.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, 0.85, 0.25, false));
    }

    public void loop() {
        if(!done) done = mSeq.loop();
        telemetry.addData("Start Counts", startCounts);
        telemetry.addData("End Counts", endCounts);
        telemetry.addData("Total Distance", startCounts - endCounts);
    }

    private class UltraHoneStep extends AutoLib.Step {
        private final OpMode mode;
        private final MatbotixUltra ultra;
        private final int dist;
        private final int error;
        private final int count;
        private final SensorLib.PID errorPid;
        private final GyroCorrectStep gyroStep;

        private double lastTime = 0;
        private int currentCount = 0;

        UltraHoneStep(OpMode mode, MatbotixUltra ultra, int dist, int error, int count, SensorLib.PID errorPid, GyroCorrectStep gyroStep) {
            this.mode = mode;
            this.ultra = ultra;
            this.dist = dist;
            this.error = error;
            this.count = count;
            this.errorPid = errorPid;
            this.gyroStep = gyroStep;
        }

        public boolean loop() {
            super.loop();
            if(firstLoopCall()) {
                lastTime = mode.getRuntime() - 1;
                startCounts = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
            }
            //get the distance and error
            final int read = ultra.getReading();
            final int curError = dist - read;
            //if we found it, stop
            //if the peak is within stopping margin, stop
            if(Math.abs(curError) <= error) {
                setMotorsWithoutGyro(0);
                if (++currentCount >= count) {
                    endCounts = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
                    return true;
                }
                else return false;
            }
            else currentCount = 0;
            //PID
            final double time = mode.getRuntime();
            final float pError = errorPid.loop(curError, (float)(time - lastTime));
            lastTime = time;
            mode.telemetry.addData("power error", pError);
            //cut out a middle range, but handle positive and negative
            final float power;
            if(pError >= 0) power = Range.clip(pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
            else power = Range.clip(pError, -gyroStep.getMaxPower(), -gyroStep.getMinPower());
            /*
            if(gyroStep.getStartPower() >= 0){
                if(pError >= 0) power = Range.clip(gyroStep.getStartPower() + pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
                else power = Range.clip(pError - gyroStep.getStartPower(), -gyroStep.getMaxPower(), -gyroStep.getMinPower());
            }
            else {
                if(pError >= 0) power = Range.clip(gyroStep.getStartPower() - pError, -gyroStep.getMaxPower(), -gyroStep.getMinPower());
                else power = Range.clip(Math.abs(gyroStep.getStartPower() + pError), gyroStep.getMinPower(), gyroStep.getMaxPower());
            }
            */
            gyroStep.setPower(power);
            gyroStep.loop();
            //telem
            mode.telemetry.addData("Power", -power);
            mode.telemetry.addData("Ultra error", curError);
            mode.telemetry.addData("Ultra", read);
            //return
            return false;
        }

        private void setMotorsWithoutGyro(float power) {
            for(DcMotor motor : gyroStep.getMotors()) motor.setPower(power);
        }
    }
}
