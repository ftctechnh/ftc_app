package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.SquirrelyLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by Noah on 11/13/2017.
 * Testing file for cryptobox pillar detection using camera
 */

@Autonomous(name="Red Drive", group="test")
public class RedPilliar extends VuforiaBallLib {
    private static final double SAT_MIN = 0.5;
    private static final int LUM_THRESH = 25;
    private static final int PEAK_WIDTH_MIN = 4;
    private static final int PEAK_HEIGHT_MIN = 110;
    private static final int PEAK_FIND_WINDOW = 40;
    private static final int PEAK_FRAME_COUNT = 3;
    private static final double BALL_WAIT_SEC = 2.0;//10.0;
    private static final int PILLIAR_COUNT_INC = 350;
    private static final int PILLIAR_COUNT_START_BLUE = 40;
    private static final int PILLIAR_COUNT_START_RED = 250;

    protected boolean red = true;
    protected boolean justDrive = false;
    private int[] data;
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

    @Override
    public void init() {
        initVuforia(true);
        bot.init();
    }

    @Override
    public void start() {
        //hmmm
        startTracking();
    }

    @Override
    public void loop() {
        if(startLoop == 0) startLoop = getRuntime();
        if(getRuntime() - startLoop < BALL_WAIT_SEC && (color == BallColor.Indeterminate || color == BallColor.Undefined)) color = getBallColor();
        else if(!firstLoop){
            //init vars
            int count;
            if(red) count = PILLIAR_COUNT_START_RED;
            else count = PILLIAR_COUNT_START_BLUE;
            final int mul = red ? -1 : 1;
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

            if(getLastVuMark() != null) {
                RelicRecoveryVuMark thing = getLastVuMark();
                //if we're on red it's the far one, else it's the close one
                if(thing == RelicRecoveryVuMark.LEFT && red) count += PILLIAR_COUNT_INC * 2;
                //if it's center, always increment
                if(thing == RelicRecoveryVuMark.CENTER) count += PILLIAR_COUNT_INC;
                //if its' on the right and we're on blue, go twice
                if(thing == RelicRecoveryVuMark.RIGHT && !red) count += PILLIAR_COUNT_INC * 2;
                //telemetry
                telemetry.addData("Mark", thing.toString());
            }

            //contruct the block placing seq
            AutoLib.LinearSequence blockSeq = new AutoLib.LinearSequence();

            blockSeq.add(
                    new AutoLib.RunUntilStopStep(
                            new AutoLib.MoveByTimeStep(bot.getMotorVelocityShimArray(), 135.0f * mul, 5.0f, true),
                            new PeakFindStep(PEAK_FIND_WINDOW, PEAK_FRAME_COUNT)
                    )
            );
            blockSeq.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 45.0f, 360.0f, motorPID, 0.5f, 10, true));
            blockSeq.add(new PeakHoneStep(bot.getMotorVelocityShimArray(), !red, new SensorLib.PID(-0.15f, -0.05f, 0, 15), 35.0f, 105.0f, 3, this));

            blockSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f * mul, count, true));
            blockSeq.add(new AutoLib.GyroTurnStep(this, 90, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 65.0f, 520.0f, motorPID, 2.0f, 10, true));
            blockSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f, 100, true));
            blockSeq.add(bot.getDropStep());
            blockSeq.add(new AutoLib.RunUntilStopStep(
                    new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0f, 400, true),
                    new AutoLib.LogTimeStep(this, "huh", 2.0)
            ));
            blockSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -135.0f, 150, true));
            //blockSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), -135.0f * mul, 100, true));
            //blockSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 270.0f, 1600, true));

            AutoLib.LinearSequence driveSeq = new AutoLib.LinearSequence();

            driveSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 135.0 * mul, 1500, true));

            //smash it all together
            mSeq.add(whack);
            if(!justDrive) mSeq.add(blockSeq);
            else mSeq.add(driveSeq);

            firstLoop = true;
        }

        //logs!
        if(color != null) telemetry.addData("Ball Color", color.toString());
        if(getLastVuMark() != null) telemetry.addData("VuMark", getLastVuMark().toString());

        if(firstLoop && mSeq.loop()) requestOpModeStop();
    }

    private class DebugHeading implements HeadingSensor {
        public float getHeading() {
            return 0;
        }
    }

    private class PeakFindStep extends AutoLib.Step {
        private int peakFrameCount = 0;
        private int count;
        private int centerWindow;
        PeakFindStep(int centerWindow, int count) {
            this.count = count;
            this.centerWindow = centerWindow;
        }

        public boolean loop() {
            final int middle = 255 / 2;
            //check peak window for peaks
            ArrayList<Integer> peaks = getPeaks();
            int i = 0;
            for(; i < peaks.size(); i++) {
                if(peaks.get(i) < middle + centerWindow && peaks.get(i) > middle - centerWindow) {
                    peakFrameCount++;
                    break;
                }
            }
            if(i == peaks.size()) peakFrameCount = 0;

            return peakFrameCount > this.count;
        }
   }

    private static class GyroStopStep extends AutoLib.Step {
        private final HeadingSensor gyro;
        private final float heading;
        private final int error;
        GyroStopStep(HeadingSensor gyro, float heading, int error) {
            this.gyro = gyro;
            this.heading = heading;
            this.error = error;
        }

        public boolean loop() {
            return Math.abs(gyro.getHeading() - heading) <= error;
        }
    }


    private class PeakHoneStep extends  AutoLib.Step {
        private final int error;
        private final DcMotor[] motorRay;
        private final OpMode mode;
        SensorLib.PID errorPid;
        float minPower;
        float maxPower;
        double lastTime = 0;

        private int lastPeakPos = -1;
        private int lostPeaksCount = 0;
        private int foundPeakCount = 0;
        private boolean fromLeft;

        private static final int PEAK_LOST_BREAK = 10;
        private static final int PEAK_DIST_THRESH = 15;
        private static final int PEAK_FOUND_COUNT = 10;

        PeakHoneStep(DcMotor[] motors, boolean fromLeft, SensorLib.PID errorPid, float minPower, float maxPower, int error, OpMode mode) {
            this.motorRay = motors;
            this.errorPid = errorPid;
            this.minPower = minPower;
            this.maxPower = maxPower;
            this.error = error;
            this.fromLeft = fromLeft;
            this.mode = mode;
        }

        public boolean loop() {
            mode.telemetry.addData("Home!", true);
            if(lastTime == 0) lastTime = mode.getRuntime();
            //if first run, get the peak we want to center on.
            //in this case, the one farthest left in the frame
            ArrayList<Integer> peaks = getPeaks();
            //if we have no peaks, ty again next time
            if(peaks.size() <= 0) return ++lostPeaksCount >= PEAK_LOST_BREAK;
            //get the peak according to whether it's coming to the left or right
            int peak;
            if(!fromLeft) peak = peaks.get(0);
            else peak = peaks.get(peaks.size() - 1);
            if(lastPeakPos == -1) lastPeakPos = peak;
            //else if the distance of the peaks is greater than reasonable, break
            if(lastPeakPos != -1 && Math.abs(peak - lastPeakPos) >= PEAK_DIST_THRESH) return ++lostPeaksCount >= PEAK_LOST_BREAK;
            //if the peak is within stopping margin, stop
            if(Math.abs(peak - 127) <= error) {
                setMotors(0);
                return ++foundPeakCount >= PEAK_FOUND_COUNT;
            }
            //save the last peak
            lastPeakPos = peak;
            //reset lost peaks counter
            lostPeaksCount = 0;
            foundPeakCount = 0;
            //adjust motors
            int error = 127 - peak;
            double time = getRuntime();
            float pError = errorPid.loop(error, (float)(time - lastTime));
            lastTime = time;
            mode.telemetry.addData("power error", pError);
            //cut out a middle range, but handle positive and negative
            float power;
            if(pError >= 0) power = Range.clip(minPower + pError, minPower, maxPower);
            else power = Range.clip(pError - minPower, -maxPower, -minPower);
            setMotors(-power);
            //telem
            telemetry.addData("Peak dist", error);
            //return
            return false;
        }

        private void setMotors(float power) {
            for(DcMotor motor : motorRay) motor.setPower(power);
        }
   }

    //cached values
    private int[] ray;
    private double histScale;
    private double rowScale;
    private double colScale;

    private ArrayList<Integer> getPeaks() {
        //I should put this somewhere else, but might as well throw it here
        try {
            Mat frame = getFrame();
            //first algorithm: follow tape line
            //step 0: reduce image by downsizing it with a gaussian pyramid
            Imgproc.pyrDown(frame, frame);
            //twice
            Imgproc.pyrDown(frame, frame);
            //intermission: instanciate list
            data = new int[255];
            if (histScale == 0) {
                histScale = (double)data.length / (double)frame.cols();
                rowScale = (double)data.length / (double)frame.rows();
                colScale = (double) frame.cols() / (double) data.length;
            }
            //step 1: separate pixels into six color groups: r, g, b, black, white, grey
            //upsize aray to 32bit signed
            frame.convertTo(frame, CvType.CV_32S);
            //convert to java array
            if (ray == null) ray = new int[(int) (frame.total() * frame.elemSize())];
            frame.get(0, 0, ray);
            double[] thing = frame.get(frame.rows() / 2, frame.cols() / 2);
            telemetry.addData("Center", "r: " + thing[0] + " g: " + thing[1] + " b: " + thing[2]);
            //and posterize!
            for (int i = 0; i < ray.length; i += 3) {
                //get rgb
                final int r = ray[i];
                final int g = ray[i + 1];
                final int b = ray[i + 2];
                //calc luminance and saturation
                final int lum = Math.max(r, Math.max(g, b));
                double sat = 0;
                if (lum != 0) sat = (double) (lum - Math.min(r, Math.min(g, b))) / (double) lum;
                //threshold values for white or grey pixels
                if (lum <= LUM_THRESH) {
                    ray[i] = 0;
                    ray[i + 1] = 0;
                    ray[i + 2] = 0;
                } else if (sat <= SAT_MIN) {
                    final int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                    ray[i] = gray;
                    ray[i + 1] = gray;
                    ray[i + 2] = gray;
                }
                //else it must be a solid color, so group those
                else {
                    //red
                    if (lum == r) {
                        ray[i] = 255;
                        ray[i + 1] = 0;
                        ray[i + 2] = 0;
                        //increment histogram!
                        if(red) data[(int) (((i / 3) % frame.cols()) * histScale)]++;
                    }
                    //green
                    else if (lum == g) {
                        ray[i] = 0;
                        ray[i + 1] = 255;
                        ray[i + 2] = 0;
                    }
                    //blue
                    else if (lum == b) {
                        ray[i] = 0;
                        ray[i + 1] = 0;
                        ray[i + 2] = 255;
                        if(!red) data[(int) (((i / 3) % frame.cols()) * histScale)]++;
                    }
                }
            }
            //reinsert mat
            frame.put(0, 0, ray);
            //draw histogram
            final Scalar color;
            if(red) color = new Scalar(255, 0, 0);
            else color = new Scalar(0, 0, 255);
            for (int i = 0; i < data.length; i++)
                Imgproc.rectangle(frame, new Point(colScale * i, frame.rows() - 1), new Point(colScale * i + colScale, frame.rows() - 1 - data[i] * rowScale), color);
            Imgproc.drawMarker(frame, new Point(frame.cols() / 2, frame.rows() / 2), color);
            //count pillars
            //generate peaks
            ArrayList<Integer> peaks;
            int tempThresh = PEAK_HEIGHT_MIN;
            do {
                peaks = new ArrayList<>();
                int runStart = -1;
                for (int i = 0; i < data.length; i++) {
                    if (runStart == -1) {
                        if (data[i] > tempThresh) runStart = i;
                    } else if (data[i] <= tempThresh) {
                        if (i - runStart >= PEAK_WIDTH_MIN)
                            peaks.add(runStart + (i - runStart) / 2);
                        runStart = -1;
                    }
                }
                tempThresh += 5;
            } while (peaks.size() > 4);

            telemetry.addData("Thresh", tempThresh - 5);
            telemetry.addData("Peaks", peaks.toString());
            for (Integer i : peaks) telemetry.addData("Peak " + i, data[i]);
            //mark peaks with column
            Scalar green = new Scalar(0, 255, 0);
            for (Integer i : peaks)
                Imgproc.rectangle(frame, new Point(i * colScale, 0), new Point(i * colScale + colScale, frame.rows()), green);
            //convert back to 8 bit
            frame.convertTo(frame, CvType.CV_8U);
            //display
            drawFrame(frame);
            return peaks;
        } catch (InterruptedException e) {
            //lol yeah naw
            return new ArrayList<>();
        }
    }

    private class PilliarTurnStep extends AutoLib.Step {
        private DcMotorEx[] motors;
        private float power;
        private boolean turnLeft;
        private int error;
        private boolean red;

        PilliarTurnStep(DcMotorEx[] motors, float power, boolean turnLeft, int error, boolean red) {
            this.motors = motors;
            this.power = power;
            this.turnLeft = turnLeft;
            this.error = error;
            this.red = red;
        }

        public boolean loop() {


            return false;
        }


        private void turnLeft() {
            motors[0].setVelocity(-180, AngleUnit.DEGREES);
            motors[1].setPower(-power);
            motors[2].setVelocity(180, AngleUnit.DEGREES);
            motors[3].setPower(power);
        }
        private void turnRight() {
            motors[0].setVelocity(180, AngleUnit.DEGREES);
            motors[1].setPower(power);
            motors[2].setVelocity(-180, AngleUnit.DEGREES);
            motors[3].setPower(-power);
        }
        private void stopMotors() { for(DcMotorEx motor : motors ) motor.setPower(0); }
    }
}