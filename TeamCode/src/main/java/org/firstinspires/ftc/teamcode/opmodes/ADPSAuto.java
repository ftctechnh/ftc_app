package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

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

        config.setPulse(APDS9960.Config.PulseLength.PULSE_16US, (byte)8, APDS9960.Config.LEDStrength.STREN_100MA, APDS9960.Config.LEDBoost.BOOST_1X, APDS9960.Config.DistGain.GAIN_1X);
        dist = new APDS9960(config, hardwareMap.get(I2cDeviceSynch.class, "dist"), false);
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
                final AutoLib.Step whackLeft;
                if(red) whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed - BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
                else whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue - BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
                final AutoLib.Step whackRight;
                if(red) whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed + BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
                else whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue + BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
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

            int skip = 0;

            if(getLastVuMark() != null) {
                RelicRecoveryVuMark thing = getLastVuMark();
                //if we're on red it's the far one, else it's the close one
                //and if its' on the right and we're on blue, go twice
                if((thing == RelicRecoveryVuMark.LEFT && red) || (thing == RelicRecoveryVuMark.RIGHT && !red)) skip = 2;
                //if it's center, always increment
                if(thing == RelicRecoveryVuMark.CENTER) skip = 1;
                //telemetry
                telemetry.addData("Mark", thing.toString());
            }

            AutoLib.Sequence findPilliar = new AutoLib.LinearSequence();

            //TODO: GYRO STEERING WHILE DRIVING OFF PLATFORM
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 105.0f * mul, 700, true));
            findPilliar.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 45.0f, 360.0f, motorPID, 0.5f, 10, true));
            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stickBase.servo, BotHardware.ServoE.stickBaseCenter, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, 0.9, 0.25, false));
            findPilliar.add(new APDSFind(bot.getMotorVelocityShimArray(), BotHardware.ServoE.stick.servo, 0.9, 0.71, dist, config, new SensorLib.PID(0.5f, 0.15f, 0, 10), 35.0f, 250.0f,
                    70, 8, skip, 110, this));

            findPilliar.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            //findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 500, true));
            findPilliar.add(new AutoLib.GyroTurnStep(this, 60f, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 65.0f, 520.0f, motorPID, 2.0f, 10, true));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 225.0f, 425, true));
            findPilliar.add(bot.getDropStep());
            findPilliar.add(new AutoLib.RunUntilStopStep(
                    new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f, 150, true),
                    new AutoLib.LogTimeStep(this, "huh", 2.0)
            ));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -135.0f, 200, true));


            mSeq.add(whack);
            mSeq.add(findPilliar);

            firstLoop = true;
        }

        //logs!
        if(color != null) telemetry.addData("Ball Color", color.toString());
        if(getLastVuMark() != null) telemetry.addData("VuMark", getLastVuMark().toString());

        try {
            if(firstLoop && mSeq.loop()) requestOpModeStop();
        }
        catch (Exception e) {
            bot.stopAll();
            throw e;
        }
    }


    public void stop() {
        bot.stopAll();
        stopVuforia();
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
        private final Servo stick;
        private final int pilliarDist;
        private int[] encoderCache = new int[4];
        private int pilliarCount;
        private boolean stickPulled = false;
        private final FilterLib.MovingWindowFilter movingAvg = new FilterLib.MovingWindowFilter(10, 255);
        private final double stickDown;
        private final double stickUp;

        private double lastTime = 0;
        private int foundCount = 0;

        private static final int APDS_FOUND_COUNT = 10;
        private static final int COUNTS_BETWEEN_PILLIAR = 145;

        APDSFind(DcMotor[] motors, Servo stick, double stickDown, double stickUp, APDS9960 sens, APDS9960.Config config, SensorLib.PID errorPid, float minPower, float maxPower, int dist, int error, int pilliarSkipCount, int skipDist, OpMode mode) {
            this.motorRay = motors;
            this.config = config;
            this.errorPid = errorPid;
            this.sens = sens;
            this.minPower = minPower;
            this.maxPower = maxPower;
            this.mError = error;
            this.mDist = dist;
            this.mode = mode;
            this.stick = stick;
            this.pilliarCount = pilliarSkipCount;
            this.pilliarDist = skipDist;
            this.stickDown = stickDown;
            this.stickUp = stickUp;
        }

        public boolean loop() {
            //get distance
            double dist = this.sens.getLinearizedDistance();
            movingAvg.appendValue(dist);
            double filteredDist = movingAvg.currentValue();
            //if we aren't skipping any more pilliars
            if(pilliarCount == 0) {
                if(lastTime == 0) lastTime = mode.getRuntime();
                //get the distance and error
                float error = (float)(this.mDist - dist);
                //if we found it, stop
                //if the peak is within stopping margin, stop
                if(config.gain == APDS9960.Config.DistGain.GAIN_1X && Math.abs(error) <= mError) {
                    setMotors(0);
                    return ++foundCount >= APDS_FOUND_COUNT;
                }
                else foundCount = 0;
                //PID
                double time = mode.getRuntime();
                float pError = errorPid.loop((float)(this.mDist - filteredDist), (float)(time - lastTime));
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
            else {
                //else skip them pilliars
                //if stick is pulled, keep stick pulled until driving is done, then drop and decrement pilliars
                if(stickPulled && checkEncoders(COUNTS_BETWEEN_PILLIAR)) {
                    stick.setPosition(stickDown);
                    pilliarCount--;
                    stickPulled = false;
                }
                //else if stick isn't pulled, but the sensor found a pilliar
                //lift the stick and mark the encoders to drive
                else if(!stickPulled && filteredDist <= pilliarDist) {
                    stick.setPosition(stickUp);
                    markEncoders();
                    stickPulled = true;
                }
                //set motors to speedy
                setMotors(maxPower);
                //not done
                return false;
            }


        }

        private void setMotors(float power) {
            for(DcMotor motor : motorRay) motor.setPower(power);
        }

        private boolean checkEncoders(int dist) {
            boolean done = true;
            for(int i = 0; i < 4; i++) done &= (Math.abs(motorRay[i].getCurrentPosition() - encoderCache[i]) >= dist);
            return done;
        }

        private void markEncoders() {
            for (int i = 0; i < 4; i++) encoderCache[i] = motorRay[i].getCurrentPosition();
        }
    }
}
