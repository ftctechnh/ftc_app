package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 12/20/2017.
 */

@Autonomous(name="Blue APDS Auto", group="test")
public class ADPSAuto extends VuforiaBallLib {
    private static final double BALL_WAIT_SEC = 2.0;

    APDS9960 dist;
    APDS9960.Config config = new APDS9960.Config();
    protected boolean red = false;
    protected boolean justDrive = false;
    private BotHardware bot = new BotHardware(this);

    private BallColor color = BallColor.Undefined;
    private double startLoop = 0;
    private boolean firstLoop = false;

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    //parameters for gyro PID, but cranked up
    float Kp5 = 3.0f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.0f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 0.0f;    // maximum angle error for which we update integrator

    SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

    public void init() {
        initVuforia(true);

        config.setPulse(APDS9960.Config.PulseLength.PULSE_16US, (byte)8, APDS9960.Config.LEDStrength.STREN_100MA, APDS9960.Config.LEDBoost.BOOST_1X, APDS9960.Config.DistGain.GAIN_8X);
        dist = new APDS9960(config, hardwareMap.get(I2cDeviceSynch.class, "dist"), true);
        dist.initDevice();

        bot.init();
    }

    public void start() {
        dist.startDevice();
        startTracking();
    }

    public void loop() {
        if(startLoop == 0) startLoop = getRuntime();
        if(getRuntime() - startLoop < BALL_WAIT_SEC && (color == BallColor.Indeterminate || color == BallColor.Undefined)) color = getBallColor();
        else if(!firstLoop){
            //init whacky stick code here
            AutoLib.Sequence whack = new AutoLib.LinearSequence();
            //check detection
            if(color != BallColor.Indeterminate && color != BallColor.Undefined) {
                if(red) whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed, 0.25, false));
                else whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue, 0.25, false));
                whack.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 0.5, false));
                //hmmmmm
                final AutoLib.Step whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseSwingLeft, 1.0, false);
                final AutoLib.Step whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseSwingRight, 1.0, false);
                if(color == BallColor.LeftBlue) {
                    if(red) whack.add(whackLeft);
                    else whack.add(whackRight);
                }
                else if(color == BallColor.LeftRed) {
                    if(red) whack.add(whackRight);
                    else whack.add(whackLeft);
                }
                whack.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
                whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            }

            final int mul = red ? -1 : 1;

            AutoLib.Sequence findPilliar = new AutoLib.LinearSequence();

            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 700, true));
            findPilliar.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 45.0f, 360.0f, motorPID, 0.5f, 10, true));
            findPilliar.add(makeFindSequence());

            if(getLastVuMark() != null) {
                RelicRecoveryVuMark thing = getLastVuMark();
                //if we're on red it's the far one, else it's the close one
                //and if its' on the right and we're on blue, go twice
                if((thing == RelicRecoveryVuMark.LEFT && red) || (thing == RelicRecoveryVuMark.RIGHT && !red)) {
                    findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 200, true));
                    findPilliar.add(makeFindSequence());
                    findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 200, true));
                    findPilliar.add(makeFindSequence());
                }
                //if it's center, always increment
                if(thing == RelicRecoveryVuMark.CENTER) {
                    findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 200, true));
                    findPilliar.add(makeFindSequence());
                }
                //telemetry
                telemetry.addData("Mark", thing.toString());
            }

            findPilliar.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 500, true));
            findPilliar.add(new AutoLib.GyroTurnStep(this, 90, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 65.0f, 520.0f, motorPID, 2.0f, 10, true));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f, 100, true));
            findPilliar.add(bot.getDropStep());
            findPilliar.add(new AutoLib.RunUntilStopStep(
                    new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f, 400, true),
                    new AutoLib.LogTimeStep(this, "huh", 2.0)
            ));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -135.0f, 150, true));

            mSeq.add(whack);
            mSeq.add(findPilliar);

            firstLoop = true;
        }

        //logs!
        if(color != null) telemetry.addData("Ball Color", color.toString());
        if(getLastVuMark() != null) telemetry.addData("VuMark", getLastVuMark().toString());

        if(firstLoop && mSeq.loop()) requestOpModeStop();
    }


    public void stop() {
        stopVuforia();
    }

    private AutoLib.Sequence makeFindSequence() {
        AutoLib.Sequence seq = new AutoLib.LinearSequence();
        seq.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stickBase.servo, 0.469, 0.5, false));
        seq.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, BotHardware.ServoE.stickDown - 0.1, 0.5, false));
        seq.add(new APDSFind(bot.getMotorVelocityShimArray(), dist, config, new SensorLib.PID(0.5f, 0.15f, 0, 10), 35.0f, 360.0f, 45, 5, this));
        seq.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, BotHardware.ServoE.stickUp, 0.5, false));
        return seq;
    }

    public static class APDSFind extends AutoLib.Step {
        private final int mError;
        private final int mDist;
        private final DcMotor[] motorRay;
        private final APDS9960 sens;
        private final APDS9960.Config config;
        private final OpMode mode;
        private final SensorLib.PID errorPid;
        private final float minPower;
        private final float maxPower;
        private final FilterLib.MovingWindowFilter movingAvg = new FilterLib.MovingWindowFilter(10);

        private double lastTime = 0;
        private int foundCount = 0;

        private static final int APDS_FOUND_COUNT = 10;

        APDSFind(DcMotor[] motors, APDS9960 sens, APDS9960.Config config, SensorLib.PID errorPid, float minPower, float maxPower, int dist, int error, OpMode mode) {
            this.motorRay = motors;
            this.config = config;
            this.errorPid = errorPid;
            this.sens = sens;
            this.minPower = minPower;
            this.maxPower = maxPower;
            this.mError = error;
            this.mDist = dist;
            this.mode = mode;
        }

        public boolean loop() {
            if(lastTime == 0) lastTime = mode.getRuntime();
            //get the distance and error
            double dist = this.sens.getLinearizedDistance();
            float error = (float)(this.mDist - dist);
            //if we found it, stop
            //if the peak is within stopping margin, stop
            if(config.gain == APDS9960.Config.DistGain.GAIN_1X && Math.abs(error) <= mError) {
                setMotors(0);
                return ++foundCount >= APDS_FOUND_COUNT;
            }
            else foundCount = 0;
            //moving avg
            movingAvg.appendValue(dist);
            //PID
            double time = mode.getRuntime();
            float pError = errorPid.loop((float)(this.mDist - movingAvg.currentValue()), (float)(time - lastTime));
            lastTime = time;
            mode.telemetry.addData("power error", pError);
            //cut out a middle range, but handle positive and negative
            float power;
            if(pError >= 0) power = Range.clip(minPower + pError, minPower, maxPower);
            else power = Range.clip(pError - minPower, -maxPower, -minPower);
            setMotors(-power);
            //telem
            mode.telemetry.addData("APDS dist", error);
            //return
            return false;
        }

        private void setMotors(float power) {
            for(DcMotor motor : motorRay) motor.setPower(power);
        }
    }
}
