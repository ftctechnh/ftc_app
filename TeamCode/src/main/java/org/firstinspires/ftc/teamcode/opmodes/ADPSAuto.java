package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9930;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;

/**
 * Created by Noah on 12/20/2017.
 */

@Autonomous(name="Blue APDS Auto", group="test")
public class ADPSAuto extends VuforiaBallLib {
    private static final double BALL_WAIT_SEC = 2.0;

    APDS9930 dist;
    protected boolean red = false;
    protected boolean justDrive = false;
    private BotHardware bot = new BotHardware(this);

    private BallColor color = BallColor.Undefined;
    private double startLoop = 0;
    private boolean firstLoop = false;

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    //parameters for gyro steering
    float Kp4 = -8.0f;        // degree heading proportional term correction per degree of deviation
    float Ki4 = 0.0f;         // ... integrator term
    float Kd4 = 0;             // ... derivative term
    float Ki4Cutoff = 0.0f;    // maximum angle error for which we update integrator

    SensorLib.PID drivePID = new SensorLib.PID(Kp4, Ki4, Kd4, Ki4Cutoff);

    //parameters for gyro turning
    float Kp5 = 10.0f;        // degree heading proportional term correction per degree of deviation
    float Ki5 = 0.0f;         // ... integrator term
    float Kd5 = 0;             // ... derivative term
    float Ki5Cutoff = 0.0f;    // maximum angle error for which we update integrator

    SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

    private static final double MM_PER_ENCODE = 13.298;

    public void init() {
        initVuforia(true);

        dist = new APDS9930(hardwareMap.get(I2cDeviceSynch.class, red ? "reddist" : "bluedist"), true);
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

            int skip = 1;
            boolean closest = false;

            if(getLastVuMark() != null) {
                RelicRecoveryVuMark thing = getLastVuMark();
                //if we're on red it's the far one, else it's the close one
                //and if its' on the right and we're on blue, go twice
                if((thing == RelicRecoveryVuMark.LEFT && red) || (thing == RelicRecoveryVuMark.RIGHT && !red)) skip = 2;
                //if it's center, always increment
                else if(thing == RelicRecoveryVuMark.CENTER) skip = 1;
                else {
                    skip = 3;
                    closest = true;
                }
                //telemetry
                telemetry.addData("Mark", thing.toString());
            }

            AutoLib.Sequence findPilliar = new AutoLib.LinearSequence();

            findPilliar.add(new AutoLib.AzimuthCountedDriveStep(this, 0, bot.getHeadingSensor(), drivePID, bot.getMotorVelocityShimArray(), 155.0f * mul, 600, true, -360.0f, 360.0f));
            findPilliar.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 45.0f, 360.0f, motorPID, 0.5f, 10, true));
            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stickBase.servo, BotHardware.ServoE.stickBaseCenter, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, 0.9, 0.25, false));
            GyroCorrectStep step;
            if(!red) step = new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-16, 0, 0, 0), bot.getMotorVelocityShimArray(), 250.0f, 35.0f, 360.0f);
            else step = new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-16, 0, 0, 0), bot.getMotorVelocityShimArray(), -250.0f, 35.0f, 360.0f);
            if(red) skip--;
            findPilliar.add(new APDSFind(BotHardware.ServoE.stick.servo, 0.87, 0.7, dist, new SensorLib.PID(0.5f, 0.15f, 0, 10), step,
                    70, 8, skip, 70, this, red));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            float heading = 55;
            if(closest) heading = 180 - 55;
            if(red) heading = 180 - heading;
            findPilliar.add(new AutoLib.GyroTurnStep(this, heading, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 65.0f, 520.0f, motorPID, 2.0f, 10, true));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 225.0f, 410, true));
            findPilliar.add(bot.getDropStep());
            AutoLib.ConcurrentSequence oneSideSeq = new AutoLib.ConcurrentSequence();
            DcMotor[] temp = bot.getMotorRay();
            if(closest != red) {
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[0], 0.2f, 2.0, true));
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[1], 0.2f, 2.0, true));
            }
            else {
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[2], 0.2f, 2.0, true));
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[3], 0.2f, 2.0, true));
            }
            findPilliar.add(oneSideSeq);
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
            dist.stopDevice();
            bot.stopAll();
            throw e;
        }
    }


    public void stop() {
        dist.stopDevice();
        bot.stopAll();
        stopVuforia();
    }

    public static class APDSFind extends AutoLib.Step {
        private final int mError;
        private final int mDist;
        private final APDS9930 sens;
        private final OpMode mode;
        private final SensorLib.PID errorPid;
        private final GyroCorrectStep gyroStep;
        private final Servo stick;
        private final int pilliarDist;
        private final boolean red;
        private int[] encoderCache = new int[4];
        private int pilliarCount;
        private boolean stickPulled = false;
        private FilterLib.MovingWindowFilter movingAvg;
        private final double stickDown;
        private final double stickUp;

        private double lastTime = 0;
        private int foundCount = 0;

        private static final int APDS_FOUND_COUNT = 10;
        private static final int COUNTS_BETWEEN_PILLIAR = 155;

        APDSFind(Servo stick, double stickDown, double stickUp, APDS9930 sens, SensorLib.PID errorPid,
                 GyroCorrectStep correctIt, int dist, int error, int pilliarSkipCount, int skipDist, OpMode mode, boolean red) {
            this.errorPid = errorPid;
            this.sens = sens;
            this.gyroStep = correctIt;
            this.mError = error;
            this.mDist = dist;
            this.mode = mode;
            this.stick = stick;
            this.pilliarCount = pilliarSkipCount;
            this.pilliarDist = skipDist;
            this.stickDown = stickDown;
            this.stickUp = stickUp;
            this.red = red;
        }

        public boolean loop() {
            //get distance
            double dist = this.sens.getLinearizedDistance(red);
            if(movingAvg == null) movingAvg = new FilterLib.MovingWindowFilter(10, dist);
            movingAvg.appendValue(dist);
            double filteredDist = movingAvg.currentValue();
            //if we aren't skipping any more pilliars
            if(pilliarCount == 0) {
                if(lastTime == 0) lastTime = mode.getRuntime() - 1;
                //get the distance and error
                float error = (float)(this.mDist - dist);
                //if we found it, stop
                //if the peak is within stopping margin, stop
                if(sens.getPGAIN() == APDS9930.ProxGain.GAIN_1X && Math.abs(error) <= mError) {
                    setMotorsWithoutGyro(0);
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
                if(red) pError = -pError;
                if(pError >= 0) power = Range.clip(gyroStep.getMinPower() + pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
                else power = Range.clip(pError - gyroStep.getMinPower(), -gyroStep.getMaxPower(), -gyroStep.getMinPower());
                setMotorsWithoutGyro(-power);
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
                else if(!stickPulled && dist <= pilliarDist) {
                    stick.setPosition(stickUp);
                    markEncoders();
                    stickPulled = true;
                }
                //set motors to corrected
                gyroStep.loop();
                //not done
                return false;
            }


        }

        private void setMotorsWithoutGyro(float power) {
            for(DcMotor motor : gyroStep.getMotors()) motor.setPower(power);
        }

        private boolean checkEncoders(int dist) {
            boolean done = true;
            DcMotor[] motorRay = gyroStep.getMotors();
            for(int i = 0; i < 4; i++) done &= (Math.abs(motorRay[i].getCurrentPosition() - encoderCache[i]) >= dist);
            return done;
        }

        private void markEncoders() {
            DcMotor[] motorRay = gyroStep.getMotors();
            for (int i = 0; i < 4; i++) encoderCache[i] = motorRay[i].getCurrentPosition();
        }
    }

    // a Step that provides gyro-based guidance to motors controlled by other concurrent Steps (e.g. encoder or time-based)
    // assumes an even number of concurrent drive motor steps in order right ..., left ...
    // this step tries to keep the robot on the given course by adjusting the left vs. right motors to change the robot's heading.
    static public class GyroCorrectStep extends AutoLib.Step {
        private final float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private final float mHeading;                             // compass heading to steer for (-180 .. +180 degrees)
        private final OpMode mOpMode;                             // needed so we can log output (may be null)
        private final HeadingSensor mGyro;                        // sensor to use for heading information (e.g. Gyro or Vuforia)
        private final SensorLib.PID mPid;                         // proportional–integral–derivative controller (PID controller)
        private double mPrevTime;                           // time of previous loop() call
        private final DcMotor[] mMotorSteps;            // the motor steps we're guiding - assumed order is right ... left ...
        private final float powerMin;
        private final float powerMax;

        public GyroCorrectStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                             DcMotor[] motorsteps, float power)
        {
            this(mode, heading, gyro, pid, motorsteps, power, -1, 1);
        }

        public GyroCorrectStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                             DcMotor[] motorsteps, float power, float powerMin, float powerMax)
        {
            mOpMode = mode;
            mHeading = heading;
            mGyro = gyro;
            mPid = pid;
            mMotorSteps = motorsteps;
            mPower = power;
            this.powerMin = powerMin;
            this.powerMax = powerMax;
        }

        public boolean loop()
        {
            super.loop();
            // initialize previous-time on our first call -> dt will be zero on first call
            if (firstLoopCall()) {
                mPrevTime = mOpMode.getRuntime();           // use timer provided by OpMode
            }

            float heading = mGyro.getHeading();     // get latest reading from direction sensor
            // convention is positive angles CCW, wrapping from 359-0

            float error = SensorLib.Utils.wrapAngle(heading-mHeading);   // deviation from desired heading
            // deviations to left are positive, to right are negative

            // compute delta time since last call -- used for integration time of PID step
            double time = mOpMode.getRuntime();
            double dt = time - mPrevTime;
            mPrevTime = time;

            // feed error through PID to get motor power correction value
            float correction = -mPid.loop(error, (float)dt);

            mOpMode.telemetry.addData("Correction", correction);

            // compute new right/left motor powers
            float rightPower;
            float leftPower;
            if(mPower >= 0) {
                rightPower = Range.clip(mPower + correction, this.powerMin, this.powerMax);
                leftPower = Range.clip(mPower - correction, this.powerMin, this.powerMax);
            }
            else {
                rightPower = Range.clip(mPower + correction, -this.powerMax, -this.powerMin);
                leftPower = Range.clip(mPower - correction, -this.powerMax, -this.powerMin);
            }

            // set the motor powers -- handle both time-based and encoder-based motor Steps
            // assumed order is right motors followed by an equal number of left motors
            int i = 0;
            for (DcMotor ms : mMotorSteps) {
                ms.setPower((i++ < mMotorSteps.length/2) ? rightPower : leftPower);
            }

            // log some data
            if (mOpMode != null) {
                mOpMode.telemetry.addData("heading ", heading);
                mOpMode.telemetry.addData("left power ", leftPower);
                mOpMode.telemetry.addData("right power ", rightPower);
            }

            // guidance step always returns "done" so the CS in which it is embedded completes when
            // all the motors it's controlling are done
            return true;
        }

        public float getPower() {
            return this.mPower;
        }

        public DcMotor[] getMotors() {
            return this.mMotorSteps;
        }

        public float getMinPower() {
            return this.powerMin;
        }

        public float getMaxPower() {
            return this.powerMax;
        }
    }
}
