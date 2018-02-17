package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9930;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.opmodes.diagnostic.UltraHoneDebug;

import java.util.Arrays;

/**
 * Created by Noah on 12/20/2017.
 */

@Autonomous(name="Blue Front Auto", group="test")
public class ADPSAuto extends VuforiaBallLib {
    private static final double BALL_WAIT_SEC = 2.0;

    protected APDS9960 backDist; //formerly redDist
    protected final APDS9960.Config backConfig = new APDS9960.Config();
    protected APDS9960 frontDist;
    protected final APDS9960.Config frontConfig = new APDS9960.Config();
    protected MatbotixUltra frontUltra;
    protected MatbotixUltra backUltra;
    protected boolean red = false;
    protected boolean rear = false;
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
    private static final int APDS_DIST = 60;

    private enum AutoPath {
        RED_FRONT_LEFT(true, false, RelicRecoveryVuMark.LEFT, 1, 125, 370, 32),
        RED_FRONT_CENTER(true, false, RelicRecoveryVuMark.CENTER, 0, 125, 350, 32),
        RED_FRONT_RIGHT(true, false, RelicRecoveryVuMark.RIGHT, 2, 55, 370, 32),
        RED_REAR_LEFT(true, true, RelicRecoveryVuMark.LEFT, 1, 125, 325, 32),
        RED_REAR_CENTER(true, true, RelicRecoveryVuMark.CENTER, 0, 125, 300, 32),
        RED_REAR_RIGHT(true, true, RelicRecoveryVuMark.RIGHT, 2, 55, 325, 32),

        BLUE_FRONT_LEFT(false, false, RelicRecoveryVuMark.LEFT, 3, 125, 370, 32),
        BLUE_FRONT_CENTER(false, false, RelicRecoveryVuMark.CENTER, 1, 55, 350, 32),
        BLUE_FRONT_RIGHT(false, false, RelicRecoveryVuMark.RIGHT, 2, 55, 340, 32),
        BLUE_REAR_LEFT(false, true, RelicRecoveryVuMark.LEFT, 2, 125, 320, 32),
        BLUE_REAR_CENTER(false, true, RelicRecoveryVuMark.CENTER, 1, 55, 300, 32),
        BLUE_REAR_RIGHT(false, true, RelicRecoveryVuMark.RIGHT, 2, 55, 325, 32);

        //robot and field constants (in cm)
        private final double REAR_PILLAR_WALL_DIST = DistanceUnit.INCH.toCm(24);
        private final double FRONT_PILLAR_WALL_DIST = DistanceUnit.INCH.toCm(48);
        private final double PILLAR_COL_DIST = DistanceUnit.INCH.toCm(7.63);
        private final double RED_WHACKY_TO_ULTRA = 12.265;
        private final double BLUE_WHACKY_TO_ULTRA = 30.591;

        public final RelicRecoveryVuMark vuMark;
        public final boolean red;
        public final boolean rear;
        public final int skipCount;
        public final float turnAmount;
        public final int driveCounts;
        public final int ultraDist;
        public final int apdsFailUltra;
        AutoPath(boolean red, boolean rear, RelicRecoveryVuMark mark, int skipCount, float turnAmount, int driveCounts, int ultraDist) {
            this.red = red;
            this.rear = rear;
            this.vuMark = mark;
            this.skipCount = skipCount;
            this.turnAmount = turnAmount;
            this.driveCounts = driveCounts;
            this.ultraDist = ultraDist;
            //calculate the fallback ultrasonic distance based on known values on the robot and field
            double fieldDist = this.rear ? REAR_PILLAR_WALL_DIST : FRONT_PILLAR_WALL_DIST;
            double whackyUltra = this.red ? RED_WHACKY_TO_ULTRA : BLUE_WHACKY_TO_ULTRA;
            double skipDist = this.skipCount * PILLAR_COL_DIST;
            this.apdsFailUltra = (int)Math.round(fieldDist + skipDist*skipCount - DistanceUnit.MM.toCm(APDS_DIST) - whackyUltra);
        }

        static AutoPath getPath(boolean red, boolean rear, RelicRecoveryVuMark mark) {
            //return match
            if(mark != null && mark != RelicRecoveryVuMark.UNKNOWN){
                for(AutoPath path : values()) if(path.red == red && path.rear == rear && path.vuMark == mark) return path;
                throw new IllegalArgumentException("WTF Why");
            }
            //else return default
            if(red && rear) return RED_REAR_RIGHT;
            if(red) return RED_FRONT_CENTER;
            if(rear) return BLUE_REAR_CENTER;
            return BLUE_FRONT_CENTER;
        }
    }

    public void init() {
        initVuforia(true);

        backDist = new APDS9960(backConfig, hardwareMap.get(I2cDeviceSynch.class, "reddist"));
        frontDist = new APDS9960(frontConfig, hardwareMap.get(I2cDeviceSynch.class, "bluedist"));
        frontUltra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultrafront"), 100);
        backUltra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraback"), 100);

        backDist.initDevice();
        frontDist.initDevice();
        frontUltra.initDevice();
        backUltra.initDevice();

        backDist.startDevice();
        frontDist.startDevice();
        frontUltra.startDevice();
        backUltra.startDevice();

        bot.init();

        bot.setDropPos(0.7);
    }

    public void init_loop() {
        telemetry.addData("Front Ultra", frontUltra.getReading());
        telemetry.addData("Back Ultra", backUltra.getReading());
        telemetry.addData("Front Infrared", frontDist.getDist());
        telemetry.addData("Back Infrared", backDist.getDist());
    }

    public void start() {
        startTracking();
    }

    public void loop() {
        if(startLoop == 0) startLoop = getRuntime();
        if(getRuntime() - startLoop >= BALL_WAIT_SEC / 2 && !firstLoop) CameraDevice.getInstance().setFlashTorchMode(true);
        if(getRuntime() - startLoop < BALL_WAIT_SEC && (color == BallColor.Indeterminate || color == BallColor.Undefined)) color = getBallColor();
        else if(!firstLoop){
            CameraDevice.getInstance().setFlashTorchMode(false);
            //init whacky stick code here
            AutoLib.Sequence whack = new AutoLib.LinearSequence();

            if(color == BallColor.LeftBlue || color == BallColor.LeftRed) {
                if(red) whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed, 0.25, false));
                else whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue, 0.25, false));
                whack.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 0.5, false));
            }
            //hmmmmm
            final AutoLib.Step whackLeft;
            if(red) whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed - BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
            else whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue - BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
            final AutoLib.Step whackRight;
            if(red) whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed + BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
            else whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue + BotHardware.ServoE.stickBaseSwingSize, 1.0, false);
            //whack.add(new APDSBallFind(red, frontDist, backDist, color, whackLeft, whackRight, this));
            if(red) {
                if(color == BallColor.LeftBlue) whack.add(whackLeft);
                else if(color == BallColor.LeftRed) whack.add(whackRight);
            }
            else {
                if(color == BallColor.LeftBlue) whack.add(whackRight);
                else if(color == BallColor.LeftRed) whack.add(whackLeft);
            }

            if(color == BallColor.LeftRed || color == BallColor.LeftBlue) {
                whack.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
                whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            }

            final int mul = red ? -1 : 1;

            AutoPath path = AutoPath.getPath(red, rear, getLastVuMark());

            AutoLib.Sequence findPilliar = new AutoLib.LinearSequence();

            GyroCorrectStep step;
            if(!red) step = new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-16, 0, 0, 0), bot.getMotorVelocityShimArray(), -250.0f, 55.0f, 400.0f);
            else step = new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-16, 0, 0, 0), bot.getMotorVelocityShimArray(), 250.0f, 55.0f, 400.0f);

            int heading = rear ? (red ? 90 : -90) : 0;

            if(!rear) {
                findPilliar.add(new AutoLib.AzimuthCountedDriveStep(this, 0, bot.getHeadingSensor(), drivePID, bot.getMotorVelocityShimArray(), 155.0f * mul, 600, true, -360.0f, 360.0f));
                findPilliar.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 360.0f, motorPID, 0.5f, 10, true));
            }
            else {
                //TODO: implement encoder count backup automatically
                findPilliar.add(new UltraHoneStep(this, red ? frontUltra : backUltra, path.ultraDist, 0, 5, new SensorLib.PID(15f, 0.15f, 0, 10), step));
                //findPilliar.add(new AutoLib.GyroTurnStep(this, heading, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 520.0f, motorPID, 0.5f, 10, true));
                findPilliar.add(new AutoLib.GyroTurnStep(this, heading, bot.getHeadingSensor(), bot.getMotorRay(), 0.05f, 0.4f, new SensorLib.PID(0.005f, 0, 0, 0), 0.5f, 10, true));
                if(!red) findPilliar.add(new AutoLib.AzimuthCountedDriveStep(this, heading, bot.getHeadingSensor(), drivePID, bot.getMotorVelocityShimArray(), -360.0f, 100, true, -500.0f, 500.0f));
            }

            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stickBase.servo, BotHardware.ServoE.stickBaseCenter, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, 0.85, 0.25, false));

            //reconstruct for saftey
            if(!red) step = new GyroCorrectStep(this, heading, bot.getHeadingSensor(), new SensorLib.PID(-30, 0, 0, 0), bot.getMotorVelocityShimArray(), 250.0f, 25.0f, 150.0f);
            else step = new GyroCorrectStep(this, heading, bot.getHeadingSensor(), new SensorLib.PID(-30, 0, 0, 0), bot.getMotorVelocityShimArray(), -250.0f, 25.0f, 150.0f);
            final APDS9960 dist = red ? backDist : frontDist;
            //TODO: add camera fallback
            findPilliar.add(new APDSFind(BotHardware.ServoE.stick.servo, 0.85, 0.62, dist, new SensorLib.PID(1.0f, 0.15f, 0, 10), step,
                    APDS_DIST, 8, path.skipCount, 100, this, red));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            findPilliar.add(new AutoLib.GyroTurnStep(this, path.turnAmount + heading, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 520.0f, motorPID, 2.0f, 10, true));
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 195.0f, path.driveCounts, true));
            findPilliar.add(bot.getDropStep());
            AutoLib.ConcurrentSequence oneSideSeq = new AutoLib.ConcurrentSequence();
            DcMotor[] temp = bot.getMotorRay();
            if(path.turnAmount > 55) {
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[0], 0.5f, 1.0, true));
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[1], 0.5f, 1.0, true));
            }
            else {
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[2], 0.5f, 1.0, true));
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[3], 0.5f, 1.0, true));
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
            backDist.stopDevice();
            frontDist.stopDevice();
            bot.stopAll();
            throw e;
        }
    }


    public void stop() {
        backDist.stopDevice();
        frontDist.stopDevice();
        backUltra.stopDevice();
        frontUltra.stopDevice();
        bot.stopAll();
        stopVuforia();
    }

    public static class APDSFind extends AutoLib.Step {
        private final int mError;
        private final int mDist;
        private final APDS9960 sens;
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

        APDSFind(Servo stick, double stickDown, double stickUp, APDS9960 sens, SensorLib.PID errorPid,
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
            super.loop();
            //get distance
            double dist = this.sens.getLinearizedDistance(red);
            if(Double.isInfinite(dist)) dist = 150;
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
                if(Math.abs(error) <= mError) {
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
                //if(pError >= 0) power = Range.clip(pError, 0, gyroStep.getMaxPower());
                //else power = Range.clip(pError, -gyroStep.getMaxPower(), 0);
                gyroStep.setPower(-power);
                gyroStep.loop();
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
        private float mPower;                               // basic power setting of all 4 motors -- adjusted for steering along path
        private final float startPower;
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
            startPower = power;
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

        public float getStartPower() {
            return this.startPower;
        }

        public void setPower(float power) {
            this.mPower = power;
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

    public static class APDSBallFind extends AutoLib.Step {
        private final boolean red;
        private final APDS9960 frontDist;
        private final APDS9960 backDist;
        private BallColor color;
        private final AutoLib.Step whackLeft;
        private final AutoLib.Step whackRight;
        private final OpMode mode;

        APDSBallFind(boolean red, APDS9960 frontDist, APDS9960 backDist, BallColor color, AutoLib.Step whackLeft, AutoLib.Step whackRight, OpMode mode) {
            this.red = red;
            this.frontDist = frontDist;
            this.backDist = backDist;
            this.color = color;
            this.whackLeft = whackLeft;
            this.whackRight = whackRight;
            this.mode = mode;
        }

        public boolean loop() {
            super.loop();
            //check detection
            /*
            if(color == BallColor.Indeterminate || color == BallColor.Undefined) {
                //get colors
                int[] backColor;
                int[] frontColor;
                int count = 0;
                do {
                    backColor = backDist.getColor();
                    frontColor = frontDist.getColor();
                    count++;
                } while ((backColor[1] == 0 || backColor[3] == 0 || frontColor[1] == 0 || frontColor[3] == 0) && count < 5);
                if(backColor[1] != 0 && backColor[3] != 0 && frontColor[1] != 0 && frontColor[3] != 0) {
                    if(backColor[1] < backColor[3] && frontColor[1] > frontColor[3]) color = BallColor.LeftRed;
                    else if(backColor[1] > backColor[3] && frontColor[1] < frontColor[3]) color = BallColor.LeftBlue;
                    else {
                        mode.telemetry.addData("MUY SKETCH", Arrays.toString(frontColor) + ' ' + Arrays.toString(backColor));
                        if(backColor[1] > frontColor[1]) color = BallColor.LeftBlue;
                        else if(backColor[1] < frontColor[1]) color = BallColor.LeftRed;
                        else color = BallColor.Indeterminate;
                    }
                }
                else color = BallColor.Undefined;
            }
            */
            //run appropriete sewunce
            if(color == BallColor.LeftBlue) {
                if(red) return whackLeft.loop();
                else return whackRight.loop();
            }
            else if(color == BallColor.LeftRed) {
                if(red) return whackRight.loop();
                else return whackLeft.loop();
            }
            //or finish immedietly
            return true;
        }
    }

    public static class UltraHoneStep extends AutoLib.Step {
        private final OpMode mode;
        private final MatbotixUltra ultra;
        private final int dist;
        private final int error;
        private final int count;
        private final SensorLib.PID errorPid;
        private final ADPSAuto.GyroCorrectStep gyroStep;

        private double lastTime = 0;
        private int currentCount = 0;

        UltraHoneStep(OpMode mode, MatbotixUltra ultra, int dist, int error, int count, SensorLib.PID errorPid, ADPSAuto.GyroCorrectStep gyroStep) {
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
            if(firstLoopCall()) lastTime = mode.getRuntime() - 1;
            //get the distance and error
            final int read = ultra.getReading();
            final int curError = dist - read;
            //if we found it, stop
            //if the peak is within stopping margin, stop
            if(Math.abs(curError) <= error) {
                setMotorsWithoutGyro(0);
                return ++currentCount >= count;
            }
            else currentCount = 0;
            //PID
            final double time = mode.getRuntime();
            final float pError = errorPid.loop(curError, (float)(time - lastTime));
            lastTime = time;
            mode.telemetry.addData("power error", pError);
            //cut out a middle range, but handle positive and negative
            float power;
            if(pError >= 0) power = Range.clip(pError, gyroStep.getMinPower(), gyroStep.getMaxPower());
            else power = Range.clip(pError, -gyroStep.getMaxPower(), -gyroStep.getMinPower());
            //reverse if necessary
            if(gyroStep.getStartPower() < 0) power = -power;
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
