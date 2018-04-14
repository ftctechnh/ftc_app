package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.GyroCorrectStep;
import org.firstinspires.ftc.teamcode.libraries.UltraHoneStep;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9930;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.hardware.BlinkyEffect;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;
import org.firstinspires.ftc.teamcode.libraries.hardware.StupidColor;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.opmodes.demo.Color;
import org.firstinspires.ftc.teamcode.opmodes.diagnostic.UltraHoneDebug;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 12/20/2017.
 * Take a guess
 */

@Autonomous(name="Blue Front Auto", group="test")
public class ADPSAuto extends VuforiaBallLib {
    private static final double BALL_WAIT_SEC = 2.0;
    private static final int UNDERGLOW_PULSE_WAIT = 50;
    private static final double UNDERGLOW_INC = 0.01;
    private static final double UNDERGLOW_MAX = 0.5;

    protected APDS9960 backDist; //formerly redDist
    protected final APDS9960.Config backConfig = new APDS9960.Config();
    protected APDS9960 frontDist;
    protected final APDS9960.Config frontConfig = new APDS9960.Config();
    protected MatbotixUltra frontUltra;
    protected MatbotixUltra backUltra;
    protected boolean red = false;
    protected boolean rear = false;
    private BotHardware bot = new BotHardware(this);

    private BallColor color = BallColor.Undefined;
    private BallColor altColor = BallColor.Undefined;
    private double startLoop = 0;
    private boolean firstLoop = false;

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    //parameters for gyro steering
    private static final float Kp4 = -8.0f;        // degree heading proportional term correction per degree of deviation
    private static final float Ki4 = 0.0f;         // ... integrator term
    private static final float Kd4 = 0;             // ... derivative term
    private static final float Ki4Cutoff = 0.0f;    // maximum angle error for which we update integrator

    SensorLib.PID drivePID = new SensorLib.PID(Kp4, Ki4, Kd4, Ki4Cutoff);

    //parameters for gyro turning
    private static final float Kp5 = 10.0f;        // degree heading proportional term correction per degree of deviation
    private static final float Ki5 = 0.0f;         // ... integrator term
    private static final float Kd5 = 0;             // ... derivative term
    private static final float Ki5Cutoff = 0.0f;    // maximum angle error for which we update integrator

    private final SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

    private static final double MM_PER_ENCODE = 13.298;
    private static final int APDS_DIST = 60;

    private boolean glowInc = true;
    private double glowPower = 0.0;
    private long glowLastTime;

    private enum AutoPath {
        RED_FRONT_LEFT(true, false, RelicRecoveryVuMark.LEFT, 1, 125, 720, 32),
        RED_FRONT_CENTER(true, false, RelicRecoveryVuMark.CENTER, 0, 125, 680, 32),
        RED_FRONT_RIGHT(true, false, RelicRecoveryVuMark.RIGHT, 2, 55, 740, 32),
        RED_REAR_LEFT(true, true, RelicRecoveryVuMark.LEFT, 1, 125, 590, 33),
        RED_REAR_CENTER(true, true, RelicRecoveryVuMark.CENTER, 0, 125, 540, 33),
        RED_REAR_RIGHT(true, true, RelicRecoveryVuMark.RIGHT, 2, 50, 640, 33),

        BLUE_FRONT_LEFT(false, false, RelicRecoveryVuMark.LEFT, 3, 130, 700, 32),
        BLUE_FRONT_CENTER(false, false, RelicRecoveryVuMark.CENTER, 1, 55, 600, 32),
        BLUE_FRONT_RIGHT(false, false, RelicRecoveryVuMark.RIGHT, 2, 55, 640, 32),
        BLUE_REAR_LEFT(false, true, RelicRecoveryVuMark.LEFT, 1, 115, 320, 33),
        BLUE_REAR_CENTER(false, true, RelicRecoveryVuMark.CENTER, 0, 55, 520, 33),
        BLUE_REAR_RIGHT(false, true, RelicRecoveryVuMark.RIGHT, 1, 55, 520, 33);

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
        bot.start();
        bot.setDropPos(0.62);

        telemetry.update();

        startTracking();
    }

    public void init_loop() {
        telemetry.addData("Front Ultra", frontUltra.getReading());
        telemetry.addData("Back Ultra", backUltra.getReading());
        telemetry.addData("Front Infrared", frontDist.getDist());
        telemetry.addData("Back Infrared", backDist.getDist());
        telemetry.addData("Ball Color", getBallColor().toString());
        telemetry.addData("IMU", bot.getHeadingSensor().getHeading());
        telemetry.addData("Lift", BotHardware.Motor.lift.motor.getCurrentPosition());
    }

    public void start() {

    }

    public void loop() {
        if(startLoop == 0) startLoop = getRuntime();
        if(getRuntime() - startLoop >= BALL_WAIT_SEC / 2 && !firstLoop) CameraDevice.getInstance().setFlashTorchMode(true);
        if(getRuntime() - startLoop < BALL_WAIT_SEC && (color == BallColor.Indeterminate || color == BallColor.Undefined)) {
            color = getBallColor();
            altColor = getCVBallColor();
        }
        else if(!firstLoop){
            CameraDevice.getInstance().setFlashTorchMode(false);
            //init whacky stick code here
            AutoLib.Sequence whack = new AutoLib.LinearSequence();

            if(red) whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed, 0.25, false));
            else whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue, 0.25, false));
            whack.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickDown, 0.5, false));

            //hmmmmm
            final AutoLib.Step whackLeft;
            if(red) whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed - BotHardware.ServoE.stickBaseSwingSize, 0.5, false);
            else whackLeft = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue - BotHardware.ServoE.stickBaseSwingSize, 0.5, false);
            final AutoLib.Step whackRight;
            if(red) whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterRed + BotHardware.ServoE.stickBaseSwingSize, 0.5, false);
            else whackRight = new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseCenterBlue + BotHardware.ServoE.stickBaseSwingSize, 0.5, false);
            whack.add(new APDSBallFind(red, (color != BallColor.Indeterminate && color != BallColor.Undefined) ? color : altColor, whackLeft, whackRight));

            whack.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
            whack.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));

            final int mul = red ? -1 : 1;

            AutoPath path = AutoPath.getPath(red, rear, getLastVuMark());

            AutoLib.Sequence findPilliar = new AutoLib.LinearSequence();

            GyroCorrectStep step;
            if(!red) step = new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-20, 0, 0, 0), bot.getMotorVelocityShimArray(), -250.0f, 45.0f, 360.0f);
            else step = new GyroCorrectStep(this, 0, bot.getHeadingSensor(), new SensorLib.PID(-20, 0, 0, 0), bot.getMotorVelocityShimArray(), 250.0f, 45.0f, 360.0f);

            int heading = rear ? (red ? 90 : -90) : 0;

            if(!rear) {
                findPilliar.add(new AutoLib.AzimuthCountedDriveStep(this, 0, bot.getHeadingSensor(), drivePID, bot.getMotorVelocityShimArray(), 250.0f * mul, 1200, true, -360.0f, 360.0f));
                findPilliar.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 45.0f, 360.0f, motorPID, 0.5f, 10, true));
            }
            else {
                //TODO: implement encoder count backup automatically
                findPilliar.add(new UltraHoneStep(this, red ? frontUltra : backUltra, path.ultraDist, 0, 5, new SensorLib.PID(11f, 0.15f, 0, 10), step));
                findPilliar.add(new AutoLib.GyroTurnStep(this, heading, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 45.0f, 360.0f, motorPID, 0.5f, 10, true));
                //findPilliar.add(new AutoLib.GyroTurnStep(this, heading, bot.getHeadingSensor(), bot.getMotorRay(), 0.04f, 0.4f, new SensorLib.PID(0.006f, 0, 0, 0), 0.5f, 10, true));
                //if(!red) findPilliar.add(new AutoLib.AzimuthCountedDriveStep(this, heading, bot.getHeadingSensor(), drivePID, bot.getMotorVelocityShimArray(), -360.0f, 100, true, -500.0f, 500.0f));
            }

            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stickBase.servo, BotHardware.ServoE.stickBaseCenter, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(BotHardware.ServoE.stick.servo, 0.85, 0.25, false));

            //reconstruct for saftey
            if(!red) step = new GyroCorrectStep(this, heading, bot.getHeadingSensor(), new SensorLib.PID(-30, 0, 0, 0), bot.getMotorVelocityShimArray(), 250.0f, 25.0f, 150.0f);
            else step = new GyroCorrectStep(this, heading, bot.getHeadingSensor(), new SensorLib.PID(-30, 0, 0, 0), bot.getMotorVelocityShimArray(), -250.0f, 25.0f, 150.0f);
            final APDS9960 dist = red ? frontDist : backDist;
            //TODO: add camera fallback
            findPilliar.add(new APDSFind(BotHardware.ServoE.stick.servo, 0.85, 0.55, dist, new SensorLib.PID(1.0f, 0, 0, 10), step,
                    APDS_DIST, 8, path.skipCount, 100, this, red, rear));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.stickUp, 0.25, false));
            findPilliar.add(new AutoLib.TimedServoStep(bot.getStickBase(), BotHardware.ServoE.stickBaseHidden, 0.25, false));
            AutoLib.ConcurrentSequence raiseWhile = new AutoLib.ConcurrentSequence();
            AutoLib.LinearSequence driveToBox = new AutoLib.LinearSequence();
            //findPilliar.add(new AutoLib.GyroTurnStep(this, path.turnAmount + heading, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 520.0f, motorPID, 2.0f, 5, true));
            driveToBox.add(new AutoLib.GyroTurnStep(this, path.turnAmount + heading, bot.getHeadingSensor(), bot.getMotorRay(), 0.04f, 0.4f, new SensorLib.PID(0.006f, 0, 0, 0), 2f, 5, true));
            driveToBox.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 250.0f, path.driveCounts, true));
            raiseWhile.add(driveToBox);
            int liftPos = BotHardware.Motor.lift.motor.getCurrentPosition();
            raiseWhile.add(new MoveToPositionStep(BotHardware.Motor.lift.motor, liftPos + 400, 10, 1.0f, true));
            findPilliar.add(raiseWhile);
            findPilliar.add(bot.getDropStep());
            findPilliar.add(bot.getReverseDropStep());
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
            oneSideSeq.add(new MoveToPositionStep(BotHardware.Motor.lift.motor, liftPos, 3, 1.0f, true));
            findPilliar.add(oneSideSeq);
            findPilliar.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -400.0f, 600, true));
            findPilliar.add(new AutoLib.GyroTurnStep(this,heading + 180, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 520.0f, motorPID, 10.0f, 10, true));

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
        private final boolean rear;
        private int[] encoderCache = new int[4];
        private int[] startEncoderCache = new int[4];
        private int pilliarCount;
        private boolean stickPulled = false;
        private FilterLib.MovingWindowFilter movingAvg;
        private final double stickDown;
        private final double stickUp;

        private double lastTime = 0;
        private int foundCount = 0;

        private static final int APDS_FOUND_COUNT = 4;
        private static final int COUNTS_BETWEEN_PILLIAR = 250;
        private static final int COUNT_OH_SHIT_REAR = COUNTS_BETWEEN_PILLIAR * 7;
        private static final int COUNT_OH_SHIT_FRONT = COUNT_OH_SHIT_REAR * 9;

        APDSFind(Servo stick, double stickDown, double stickUp, APDS9960 sens, SensorLib.PID errorPid,
                 GyroCorrectStep correctIt, int dist, int error, int pilliarSkipCount, int skipDist, OpMode mode, boolean red, boolean rear) {
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
            this.rear = rear;
        }

        public boolean loop() {
            super.loop();
            //get distance
            double dist = this.sens.getLinearizedDistance(red);
            if(Double.isInfinite(dist)) dist = 150;
            if(movingAvg == null) {
                movingAvg = new FilterLib.MovingWindowFilter(10, dist);
                for (int i = 0; i < 4; i++) startEncoderCache[i] = gyroStep.getMotors()[i].getCurrentPosition();
                //check stick distance when we first drop to see if we need to p[ick up immediately
                if(pilliarCount > 0 && dist <= pilliarDist) {
                    stick.setPosition(stickUp);
                    markEncoders();
                    stickPulled = true;
                    try { TimeUnit.MILLISECONDS.sleep(250); }
                    catch (InterruptedException e) {}
                    return false;
                }
            }
            movingAvg.appendValue(dist);
            double filteredDist = movingAvg.currentValue();
            //stupid check
            //if any of the encoders are past the OH SHIT threshold, abort
            boolean shit = false;
            for(int i = 0; i < 4; i++) {
                shit |= Math.abs(gyroStep.getMotors()[i].getCurrentPosition() - startEncoderCache[i]) >= (rear ? COUNT_OH_SHIT_REAR : COUNT_OH_SHIT_FRONT);
                mode.telemetry.addData("Motor " + i, Math.abs(gyroStep.getMotors()[i].getCurrentPosition() - startEncoderCache[i]));
            }
            if(shit) {
                setMotorsWithoutGyro(0);
                throw new IllegalStateException("Ur bumfucked m8");
            }
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
            }
            return false;
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

    public static class APDSBallFind extends AutoLib.Step {
        private final boolean red;
        private BallColor color;
        private final AutoLib.Step whackLeft;
        private final AutoLib.Step whackRight;

        APDSBallFind(boolean red, BallColor color, AutoLib.Step whackLeft, AutoLib.Step whackRight) {
            this.red = red;
            this.color = color;
            this.whackLeft = whackLeft;
            this.whackRight = whackRight;
        }

        public boolean loop() {
            super.loop();
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

    public static class MoveToPositionStep extends AutoLib.Step {
        private final DcMotorEx motor;
        private final int pos;
        private final int tol;
        private final float power;
        private final boolean restoreMode;
        private DcMotor.RunMode storeMode;
        private DcMotor.ZeroPowerBehavior storeZero;
        private int lastTol;

        MoveToPositionStep(DcMotorEx motor, int pos, int tol, float power, boolean restoreMode) {
            this.motor = motor;
            this.pos = pos;
            this.tol = tol;
            this.power = power;
            this.restoreMode = restoreMode;
        }

        public boolean loop() {
            super.loop();

            if(firstLoopCall()) {
                storeMode = motor.getMode();
                if(storeMode == DcMotor.RunMode.RUN_TO_POSITION) lastTol = motor.getTargetPositionTolerance();
                storeZero = motor.getZeroPowerBehavior();
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.setTargetPositionTolerance(tol);
                motor.setTargetPosition(pos);
                motor.setPower(power);
            }

            if(!motor.isBusy()) {
                motor.setPower(0);
                if(restoreMode) {
                    motor.setZeroPowerBehavior(storeZero);
                    motor.setMode(storeMode);
                    if(storeMode == DcMotor.RunMode.RUN_TO_POSITION) motor.setTargetPositionTolerance(lastTol);
                }
                return true;
            }
            return false;
        }

    }
}
