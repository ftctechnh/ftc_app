package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.Vec2F;

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
    private static final float ENCODE_PER_CM = 17.3f;
    private static final float DRIVE_DUMP_CM = 15.0f;
    private static final int[] RED_ZERO = new int[] {32 /* the offset from the ultrasonic sensor to the wall when the robot is centered on the balance pad */,
                                                    78 /* the ofsett from the back wall when front of robot is aligned with mat edge off balance pad */};
    private static final int[] BLUE_ZERO = new int[] {20, 80};
    private static final int X_STUPID_MAX = 60;

    private final SensorLib.PID encoderHonePID = new SensorLib.PID(3.88f, 15.87f, 0, 50);
    private final SensorLib.PID gyroDrivePID = new SensorLib.PID(-16.0f, 0, 0, 0);
    private final SensorLib.PID gyroTurnPID = new SensorLib.PID(10.0f, 0, 0, 0);

    private enum AutoPath {
        RED_FRONT_RIGHT(true, false, RelicRecoveryVuMark.RIGHT, (float)DistanceUnit.INCH.toCm(3), 45),
        RED_FRONT_CENTER(true, false, RelicRecoveryVuMark.CENTER, (float)DistanceUnit.INCH.toCm(3 - PILLAR_SPACING_INCH), 45),
        RED_FRONT_LEFT(true, false, RelicRecoveryVuMark.LEFT, (float)DistanceUnit.INCH.toCm(5), 135);

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
    private final AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
    private boolean firstRun = false;
    private int xOffsetStart;
    private int yOffsetStart;

    //CODE
    public void init() {
        bot.init();
        bot.start();
        UltraPos.init(this);
        //TODO: Ball stuff
    }

    public void start() {
        //initalize starting sequence
        mSeq.add(new AutoLib.AzimuthCountedDriveStep(this, 0, bot.getHeadingSensor(), gyroDrivePID, bot.getMotorVelocityShimArray(),
                -550, 500, false, -720, -55));
        mSeq.add(new DriveAndMeasureStep(this, 0, bot.getHeadingSensor(), gyroDrivePID, bot.getMotorVelocityShimArray(),
                -550, 200, true, -720, -55));
    }

    public void loop() {
        //ball code goes above here
        if(!firstRun && mSeq.loop()) {
            //initialize sequence with found values
            final int yZero = red ? RED_ZERO[1] : BLUE_ZERO[1];
            yOffsetStart = UltraPos.getYSensor(red, rear).getReading() - yZero;

            telemetry.addData("X Offset", xOffsetStart);
            telemetry.addData("Y Offset", yOffsetStart);
            firstRun = true;
        }


    }

    public void stop() {

    }

    private ADPSAuto.GyroCorrectStep makeGyroDriveStep(float heading, SensorLib.PID pid, float power, float powerMin, float powerMax) {
        return new ADPSAuto.GyroCorrectStep(this, heading, bot.getHeadingSensor(), pid, bot.getMotorRay(), power, powerMin, powerMax);
    }

    private static AutoLib.Sequence makeDriveToBoxSeq(OpMode mode, SensorLib.PID errorPid,
                                                      ADPSAuto.GyroCorrectStep firstDriveStep,
                                                      AutoLib.GyroTurnStep turnStep,
                                                      ADPSAuto.GyroCorrectStep secondDriveStep,
                                                      DcMotor[] encoderMotors, int firstLegCounts, int secondLegCounts, int error, int count, int xOffsett, int yOffsett) {
        //step zero: calculate how far the robot must drive according to our x and y offset
        //subtract the extra distance needed to be travelled in the second leg from the first leg
        final double rad = Math.toRadians(90 - turnStep.getTargetHeading());
        firstLegCounts -= xOffsett * Math.tan(rad) * ENCODE_PER_CM;
        firstLegCounts -= yOffsett * ENCODE_PER_CM;
        //calculate the 45degree leg delta
        secondLegCounts -= xOffsett / Math.cos(rad) * ENCODE_PER_CM;
        //construct sequence
        final AutoLib.LinearSequence mSeq = new AutoLib.LinearSequence();
        //drive firstlegcounts
        mSeq.add(new EncoderHoneStep(mode, firstLegCounts, error, count, errorPid, firstDriveStep, encoderMotors));
        //turn
        mSeq.add(turnStep);
        //drive secondlegcounts
        mSeq.add(new EncoderHoneStep(mode, secondLegCounts, error, count, errorPid, secondDriveStep, encoderMotors));
        return mSeq;
    }

    public static class EncoderHoneStep extends AutoLib.Step {
        private final OpMode mode;
        private final DcMotor[] encoders;
        private final int dist;
        private final int error;
        private final int count;
        private final SensorLib.PID errorPid;
        private final ADPSAuto.GyroCorrectStep gyroStep;

        private double lastTime = 0;
        private int currentCount = 0;
        private final int[] startPos;

        public EncoderHoneStep(OpMode mode, int distCounts, int error, int count, SensorLib.PID errorPid, ADPSAuto.GyroCorrectStep gyroStep, DcMotor[] encoderRay) {
            this.mode = mode;
            this.encoders = encoderRay;
            this.dist = distCounts;
            this.error = error;
            this.count = count;
            this.errorPid = errorPid;
            this.gyroStep = gyroStep;
            this.startPos = new int[encoderRay.length];
        }

        /*
        EncoderHoneStep(OpMode mode, int distCounts, int error, int count, SensorLib.PID errorPid, ADPSAuto.GyroCorrectStep gyroStep, DcMotor encoderRay) {
            this(mode, distCounts, error, count, errorPid, gyroStep, new DcMotor[] {encoderRay});
        }
        */

        public boolean loop() {
            super.loop();
            if(firstLoopCall()) {
                lastTime = mode.getRuntime() - 1;
                for (int i = 0; i < encoders.length; i++) startPos[i] = encoders[i].getCurrentPosition();
            }
            //get the distance and error
            final float read = readEncoderDeltaAvg();
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
            //return
            return false;
        }

        private float readEncoderDeltaAvg() {
            int total = 0;
            for (int i = 0; i < encoders.length; i++) total =+ encoders[i].getCurrentPosition() - startPos[i];
            return (float)-total / (float)encoders.length;

        }

        private void setMotorsWithoutGyro(float power) {
            for (DcMotor motor : gyroStep.getMotors()) motor.setPower(power);
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
