package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.Vec2F;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.opencv.core.Mat;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Noah on 3/25/2018.
 * Attempt at moving the cryptopox position sensor to ultrasonic for speed
 * This opmode will work as fast as possible to get more points with multiple glyphs
 */

@Autonomous(name="Ultra Auto")
public class UltraAuto extends VuforiaBallLib {
    //CONSTANTS
    private static final float PILLAR_SPACING_INCH = 7.63f;
    private static final float ENCODE_PER_CM = 17.1f;
    private static final float DRIVE_DUMP_CM = 16.5f;
    private static final float ONE_SIDE_PUSH_TIME = 0.25f;
    private static final int[] RED_ZERO = new int[] {33 /* the offset from the ultrasonic sensor to the wall when the robot is centered on the balance pad */,
                                                    104 /* the ofsett from the back wall when front of robot is aligned with mat edge off balance pad */};
    private static final int[] BLUE_ZERO = new int[] {20, 80};
    private static final int X_STUPID_MAX = 60;

    private final SensorLib.PID encoderHonePID = new SensorLib.PID(3.88f, 9.00f, 0, 50);
    private final SensorLib.PID gyroDrivePID = new SensorLib.PID(-64.0f, 0, 0, 0);
    private final SensorLib.PID gyroTurnPID = new SensorLib.PID(0.0056f, 0.0063f, 0, 7);

    private enum AutoPath {
        RED_FRONT_RIGHT(true, false, RelicRecoveryVuMark.RIGHT, (float)DistanceUnit.INCH.toCm(0), 125),
        RED_FRONT_CENTER(true, false, RelicRecoveryVuMark.CENTER, (float)DistanceUnit.INCH.toCm(PILLAR_SPACING_INCH), 125),
        RED_FRONT_LEFT(true, false, RelicRecoveryVuMark.LEFT, (float)DistanceUnit.INCH.toCm(2), 55);

        public final RelicRecoveryVuMark vuMark;
        public final boolean red;
        public final boolean rear;
        public final float distanceCM;
        public final float turnAmount;
        AutoPath(boolean red, boolean rear, RelicRecoveryVuMark mark, float travelCM, float turnAmount) {
            this.red = red;
            this.rear = rear;
            this.vuMark = mark;
            this.distanceCM = travelCM;
            this.turnAmount = turnAmount;
        }
    }

    //ADJUSTABLE VARIBLES
    protected boolean red = true;
    protected boolean rear = false;

    //DEVICES
    private final BotHardware bot = new BotHardware(this);

    private enum UltraPos {
        FRONT("ultrafront"),
        BACK("ultraback"),
        LEFT("ultraleft"),
        RIGHT("ultraright");

        public final String name;
        public MatbotixUltra sensor;

        UltraPos(String name) {
            this.name = name;
        }

        public static void init(OpMode mode) {
            for(UltraPos pos : values()) {
                pos.sensor = new MatbotixUltra(mode.hardwareMap.get(I2cDeviceSynch.class, pos.name));
                pos.sensor.initDevice();
                pos.sensor.startDevice();
            }
        }

        public static MatbotixUltra getXSensor() {
            return RIGHT.sensor;
        }

        public static MatbotixUltra getYSensor(boolean red, boolean rear) {
            if(rear || red) return BACK.sensor;
            return FRONT.sensor;
        }
    }

    //MEMBERS
    private AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
    private boolean firstRun = false;
    private int xOffsetStart;
    private int yOffsetStart;

    //CODE
    public void init() {
        bot.init();
        bot.start();
        UltraPos.init(this);
        bot.setDropPos(0.62);
        //TODO: Ball stuff
    }

    public void start() {
        //initalize starting sequence

        mSeq.add(new AutoLib.AzimuthCountedDriveStep(this, 0, bot.getHeadingSensor(), gyroDrivePID, bot.getMotorVelocityShimArray(),
                -550, 500, false, -720, -55));
        mSeq.add(new DriveAndMeasureStep(this, 0, bot.getHeadingSensor(), gyroDrivePID, bot.getMotorVelocityShimArray(),
                -550, 200, true, -720, -55));
        mSeq.add(new WaitMotorVelocityStep(new DcMotorEx[] {BotHardware.Motor.frontLeft.motor, BotHardware.Motor.frontRight.motor}, 10));

    }

    public void loop() {
        //ball code goes above here
        final boolean done;
        if((done = mSeq.loop()) && !firstRun) {
            //initialize sequence with found values
            final int yZero = red ? RED_ZERO[1] : BLUE_ZERO[1];
            yOffsetStart = UltraPos.getYSensor(red, rear).getReading() - yZero;

            AutoPath path = AutoPath.RED_FRONT_RIGHT;

            mSeq = new AutoLib.LinearSequence();

            final AutoLib.GyroTurnStep turn = new AutoLib.GyroTurnStep(this, path.turnAmount, bot.getHeadingSensor(), bot.getMotorRay(), 0.04f, 0.7f, gyroTurnPID, 2f, 5, true);
            mSeq.add(makeDriveToBoxSeq(this, bot, encoderHonePID,
                    makeGyroDriveStep(0, gyroDrivePID, 100, 55, 720),
                    turn,
                    makeGyroDriveStep(path.turnAmount, gyroDrivePID, 500, 55, 720),
                    new DcMotor[] {BotHardware.Motor.frontLeft.motor, BotHardware.Motor.frontRight.motor},
                    /*Math.round(path.distanceCM * ENCODE_PER_CM)*/0, Math.round(DRIVE_DUMP_CM * ENCODE_PER_CM),
                    5, 10, xOffsetStart, yOffsetStart));
            mSeq.add(bot.getDropStep());
            final AutoLib.ConcurrentSequence backAndDown = new AutoLib.ConcurrentSequence();
            backAndDown.add(bot.getReverseDropStep());
            backAndDown.add(bot.getLiftLowerStep());
            AutoLib.ConcurrentSequence oneSideSeq = new AutoLib.ConcurrentSequence();
            DcMotor[] temp = bot.getMotorRay();
            if(path.turnAmount > 55) {
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[0], 0.7f, ONE_SIDE_PUSH_TIME, true));
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[1], 0.7f, ONE_SIDE_PUSH_TIME, true));
            }
            else {
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[2], 0.7f, ONE_SIDE_PUSH_TIME, true));
                oneSideSeq.add(new AutoLib.TimedMotorStep(temp[3], 0.7f, ONE_SIDE_PUSH_TIME, true));
            }
            backAndDown.add(oneSideSeq);
            mSeq.add(backAndDown);

            mSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -720, 200, true));
            mSeq.add(new AutoLib.GyroTurnStep(this, red ? 90 : -90, bot.getHeadingSensor(), bot.getMotorRay(), 0.04f, 0.7f, gyroTurnPID, 3f, 5, true));

            firstRun = true;
        }
        telemetry.addData("X Offset", xOffsetStart);
        telemetry.addData("Y Offset", yOffsetStart);
        if(done && firstRun) {
            telemetry.addData("Y Reading", UltraPos.getYSensor(red, rear).getReading());
        }
    }

    public void stop() {

    }

    private ADPSAuto.GyroCorrectStep makeGyroDriveStep(float heading, SensorLib.PID pid, float power, float powerMin, float powerMax) {
        return new ADPSAuto.GyroCorrectStep(this, heading, bot.getHeadingSensor(), pid, bot.getMotorVelocityShimArray(), power, powerMin, powerMax);
    }

    private static AutoLib.Sequence makeDriveToBoxSeq(OpMode mode, BotHardware bot,
                                                      SensorLib.PID errorPid,
                                                      ADPSAuto.GyroCorrectStep firstDriveStep,
                                                      AutoLib.GyroTurnStep turnStep,
                                                      ADPSAuto.GyroCorrectStep secondDriveStep,
                                                      DcMotor[] encoderMotors, int firstLegCounts, int secondLegCounts, int error, int count, int xOffsett, int yOffsett) {
        //step zero: calculate how far the robot must drive according to our x and y offset
        //subtract the extra distance needed to be travelled in the second leg from the first leg
        final double rad = Math.toRadians(90 - turnStep.getTargetHeading());
        firstLegCounts += Math.round(xOffsett * Math.tan(rad) * ENCODE_PER_CM);
        firstLegCounts -= Math.round(yOffsett * ENCODE_PER_CM);
        //calculate the 45degree leg delta
        secondLegCounts += Math.round(xOffsett / Math.cos(rad) * ENCODE_PER_CM);
        //construct sequence
        final AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
        //drive firstlegcounts
        mSeq.add(new EncoderHoneStep(mode, firstLegCounts, error, count, errorPid, firstDriveStep, encoderMotors));
        //turn
        mSeq.add(turnStep);
        //drive secondlegcounts
        AutoLib.ConcurrentSequence liftAndDrive = new AutoLib.ConcurrentSequence();
        liftAndDrive.add(bot.getLiftRaiseStep());
        //liftAndDrive.add(new EncoderHoneStep(mode, -secondLegCounts, error, count, errorPid, secondDriveStep, encoderMotors));
        liftAndDrive.add(new CheapEncoderGyroStep(secondDriveStep, encoderMotors, secondLegCounts - 30));
        mSeq.add(liftAndDrive);
        return mSeq;
    }

    public static class EncoderHoneStep extends AutoLib.Step {
        private final OpMode mode;
        private final DcMotor[] encode;
        private final int dist;
        private final int error;
        private final int count;
        private final SensorLib.PID errorPid;
        private final ADPSAuto.GyroCorrectStep gyroStep;

        private double lastTime = 0;
        private int currentCount = 0;
        private int[] startPos;

        public EncoderHoneStep(OpMode mode, int distCounts, int error, int count, SensorLib.PID errorPid, ADPSAuto.GyroCorrectStep gyroStep, DcMotor[] encoderRay) {
            this.mode = mode;
            this.encode = encoderRay;
            this.dist = distCounts;
            this.error = error;
            this.count = count;
            this.errorPid = errorPid;
            this.gyroStep = gyroStep;
        }

        /*
        EncoderHoneStep(OpMode mode, int distCounts, int error, int count, SensorLib.PID errorPid, ADPSAuto.GyroCorrectStep gyroStep, DcMotor encoderRay) {
            this(mode, distCounts, error, count, errorPid, gyroStep, new DcMotor[] {encoderRay});
        }
        */

        public boolean loop() {
            super.loop();
            if(firstLoopCall()) {
                gyroStep.reset();
                lastTime = mode.getRuntime() - 1;
                startPos = new int[encode.length];
                for (int i = 0; i < encode.length; i++) startPos[i] = encode[i].getCurrentPosition();
            }
            //get the distance delta
            int total = 0;
            for (int i = 0; i < encode.length; i++) {
                total += encode[i].getCurrentPosition() - startPos[i];
                //mode.telemetry.addData("Delta " + i, encode[i].getCurrentPosition() - startPos[i]);
            }

            final float read = -((float)total / encode.length);
            float curError = read - dist;
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

            gyroStep.setPower(power);
            gyroStep.loop();
            //telem
            mode.telemetry.addData("Power", -power);
            mode.telemetry.addData("Encoder error", curError);
            mode.telemetry.addData("Encoder", read);
            mode.telemetry.addData("Dist", dist);
            //return
            return false;
        }

        private void setMotorsWithoutGyro(float power) {
            for (DcMotor motor : gyroStep.getMotors()) motor.setPower(power);
        }
    }

    private static class WaitMotorVelocityStep extends AutoLib.Step {
        private final DcMotorEx[] motors;
        private final float velThresh;

        WaitMotorVelocityStep(DcMotorEx[] motors, float velThresh) {
            this.motors = motors;
            this.velThresh = velThresh;
        }

        WaitMotorVelocityStep(DcMotorEx motor, float velThresh) {
            this.motors = new DcMotorEx[] {motor};
            this.velThresh = velThresh;
        }

        public boolean loop() {
            super.loop();
            boolean done = true;
            for(DcMotorEx motor : motors) done &= Math.abs(motor.getVelocity(AngleUnit.DEGREES)) <= velThresh;
            return done;
        }
    }

    private static class CheapEncoderGyroStep extends AutoLib.Step {
        private final ADPSAuto.GyroCorrectStep gyroStep;
        private final int count;
        private final DcMotor[] encoders;
        private int[] startPos;

        CheapEncoderGyroStep(ADPSAuto.GyroCorrectStep step, DcMotor[] encoders, int counts) {
            this.gyroStep = step;
            this.encoders = encoders;
            this.count = counts;
        }

        public boolean loop() {
            super.loop();
            if(firstLoopCall()) {
                gyroStep.reset();
                startPos = new int[encoders.length];
                for (int i = 0; i < encoders.length; i++) startPos[i] = encoders[i].getCurrentPosition();
            }
            //get the distance delta
            int total = 0;
            for (int i = 0; i < encoders.length; i++) {
                total += encoders[i].getCurrentPosition() - startPos[i];
                //mode.telemetry.addData("Delta " + i, encode[i].getCurrentPosition() - startPos[i]);
            }
            final float read = count - Math.abs((float)total / encoders.length);
            if(read > 0) gyroStep.loop();
            else {
                for(DcMotor motor : gyroStep.getMotors()) motor.setPower(0);
                return true;
            }
            return false;
        }

    }

    private class DriveAndMeasureStep extends AutoLib.AzimuthCountedDriveStep {

        private List<Integer> data = new LinkedList<>();

        DriveAndMeasureStep(OpMode mode, float heading, HeadingSensor gyro, SensorLib.PID pid,
                            DcMotor motors[], float power, int count, boolean stop, float powerMin, float powerMax) {
            super(mode, heading, gyro, pid, motors, power, count, stop, powerMin, powerMax);
        }

        @Override
        public boolean loop() {
            final int reading;
            if(super.loop()) {
                int max = 0;
                for(Integer i : data) if(i < X_STUPID_MAX && i > max) max = i;
                final int xZero = red ? RED_ZERO[0] : BLUE_ZERO[0];
                xOffsetStart = max - xZero;
                return true;
            }
            else if((reading = UltraPos.getXSensor().getReadingNoDelay()) >= 0) data.add(reading);
            return false;
        }
    }
}
