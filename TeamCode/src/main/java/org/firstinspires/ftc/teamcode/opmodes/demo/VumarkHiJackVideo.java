package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.vuforia.CameraCalibration;
import com.vuforia.Image;
import com.vuforia.Matrix34F;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Tool;
import com.vuforia.Vec3F;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLoad;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.opencv.core.CvType.CV_8U;
import static org.opencv.core.CvType.CV_8UC3;

/**
 * Created by Noah on 9/12/2017.
 * getting rid of warning
 */

@Autonomous(name="Red Auto", group ="Auto")
@Disabled
public class VumarkHiJackVideo extends OpenCVLoad {

    private BlockingQueue<VuforiaLocalizer.CloseableFrame> ray;
    private VuforiaLocalizerShim vuforia;
    private VuforiaTrackable relicTemplate;

    private ImageView mView;

    //define the jewel platform relative to the image with a buncha vectors to add
    //all units in vuforia are mm, so we multiply to inches to make it readable
    private static final float inToMM = 25.4f;

    //units in inches, then converted to mm
    private static final float boxLeftFromImage = 3.625f * inToMM;
    private static final float boxBottomFromImage = 5.125f * inToMM;
    private static final float boxWidth = 9.75f * inToMM;
    private static final float boxLength = 3.75f * inToMM;
    private static final float boxHeight = 3.75f * inToMM;

    private static final float ballCenterFromBoxEdge = 1.875f * inToMM;

    //identity mats to be constructed later in the project
    private Vec3F[] point;
    private float[] size;
    //output bitmap
    private Bitmap bm;
    //Canvas canvas;
    //Paint p;

    private BlockingDeque<Mat> matQueue = new LinkedBlockingDeque<>();

    //storage camera calibration
    private CameraCalibration camCal;

    private Point[] imagePoints;
    private int[] leftBall = new int[2];
    private int leftDist;
    private int[] rightBall = new int[2];
    private int rightDist;

    private Scalar leftColor;
    private Scalar rightColor;

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    private BotHardware bot = new BotHardware(this);

    protected boolean RED = true;

    @Override
    public void init() {
        mView = (ImageView)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.OpenCVOverlay);
        mView.setAlpha(1.0f);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AZPbGaP/////AAAAGcIykH/KO0QNvZGSYxc0fDlVytYrk0HHv6OLmjHsswRvi/1l9RZCkepChaAZup3DIJlrjK2BV57DEz/noNO0oqT9iu2moP/svGmJ+pBG7FlfF4RHxu6UhvVLaKUZCsTJ1zTkd7XnMuRw8aSuIxowOiLJQYcgjmddi11LG26lAr6aRmoWJzr2pv6Yui2Gom0wt9J4+1g3kXqjngnH3h6NPA/6aUfpVngFaFPp5knyDJWZT88THttPsqcKW41QC/qgNh3CHIdADu15Rm51JNRlvG+2+sYstiHeHFQqCDwUkTgWor0v/Bk+xXoj3oUCb4REwT9w94E/VEI4qEAFPpmeo6YgxQ4LLFknu6tgNy8xdD6S";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE;
        //bwahaha java shim
        this.vuforia = new VuforiaLocalizerShim(parameters);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB888, true); //enables RGB565 format for the image
        camCal = vuforia.getCameraCalibration();

        //constrt matrixes
        //contruct top and bottom of both balls
        final Vec3F ballLeftTop = new Vec3F(boxLeftFromImage + ballCenterFromBoxEdge, -boxBottomFromImage + boxHeight, ballCenterFromBoxEdge);
        final Vec3F ballLeftBottom = new Vec3F(boxLeftFromImage + ballCenterFromBoxEdge, -boxBottomFromImage, ballCenterFromBoxEdge);

        final Vec3F ballRightTop = new Vec3F(boxLeftFromImage + boxWidth - ballCenterFromBoxEdge, -boxBottomFromImage + boxHeight, ballCenterFromBoxEdge);
        final Vec3F ballRightBottom = new Vec3F(boxLeftFromImage + boxWidth - ballCenterFromBoxEdge, -boxBottomFromImage, ballCenterFromBoxEdge);

        //construct points like a box
        final Vec3F botLeft = new Vec3F(boxLeftFromImage, -boxBottomFromImage, 0);
        final Vec3F botRight = new Vec3F(boxLeftFromImage + boxWidth, -boxBottomFromImage, 0);
        final Vec3F botFrontLeft = new Vec3F(boxLeftFromImage, -boxBottomFromImage, boxLength);
        final Vec3F botFrontRight = new Vec3F(boxLeftFromImage + boxWidth, -boxBottomFromImage, boxLength);

        final Vec3F topLeft = new Vec3F(boxLeftFromImage, -boxBottomFromImage + boxHeight, 0);
        final Vec3F topRight = new Vec3F(boxLeftFromImage + boxWidth, -boxBottomFromImage + boxHeight, 0);
        final Vec3F topFrontLeft = new Vec3F(boxLeftFromImage, -boxBottomFromImage + boxHeight, boxLength);
        final Vec3F topFrontRight = new Vec3F(boxLeftFromImage + boxWidth, -boxBottomFromImage + boxHeight, boxLength);

        point = new Vec3F[] { botLeft, botRight, botFrontRight, botFrontLeft, topLeft, topRight, topFrontRight, topFrontLeft,
                ballLeftTop, ballLeftBottom, ballRightTop, ballRightBottom };

        imagePoints = new Point[point.length];

        size = camCal.getSize().getData();

        bm = Bitmap.createBitmap((int)size[0], (int)size[1], Bitmap.Config.ARGB_8888);

        //setup view
        mView.post(new Runnable() {
            @Override
            public void run() {
                mView.setImageBitmap(bm);
            }
        });

        telemetry.update();

        initOpenCV();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        bot.init();

        VuforiaTrackables relicTrackables = this.vuforia.loadShimTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();

        this.vuforia.setFrameQueueCapacity(1);
        this.ray = this.vuforia.getFrameQueue();
    }

    @Override
    public void init_loop() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
            telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
            //qualcomm is stupid and didn't let me see the real vuforia matrix, so I shimmed it
            //heres where is gets good
            VuforiaDefaultListenerShim writeGoodCode = (VuforiaDefaultListenerShim) relicTemplate.getListener();

            OpenGLMatrix pose = writeGoodCode.getRawPose();
            telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
            if (pose != null) {
                //alternate projection!
                //get vuforia's real position matrix
                Matrix34F goodCodeWritten = writeGoodCode.getRealPose();

                //reset imagePoints
                final float[][] vufPoints = new float[point.length][2];

                //use vuforias projectPoints method to project all those box points
                for(int i = 0; i < point.length; i++){
                    //project
                    vufPoints[i] = Tool.projectPoint(camCal, goodCodeWritten, point[i]).getData();
                    //convert to opencv language

                    //telemetry.addData("point", "num: %d, x: %f.2, y: %f.2", i, vufPoints[i][0], vufPoints[i][1]);
                }

                //telemetry.addData("Camera Size", "w: %f.2, h: %f.2", camCal.getSize().getData()[0], camCal.getSize().getData()[1]);

                //get frame from vuforia
                try{
                    VuforiaLocalizer.CloseableFrame frame = ray.take();

                    int img = 0;
                    for(; img < frame.getNumImages(); img++){
                        //telemetry.addData("Image format " + img, frame.getImage(img).getFormat());
                        if(frame.getImage(img).getFormat() == PIXEL_FORMAT.RGB888) break;
                    }

                    Image mImage = frame.getImage(img);

                    //stick it in a Mat for "display purposes"
                    Mat out = new Mat(mImage.getHeight(), mImage.getWidth(), CV_8UC3);

                    java.nio.ByteBuffer color = mImage.getPixels();
                    byte[] ray = new byte[color.limit()];
                    color.rewind();
                    color.get(ray);
                    out.put(0, 0, ray);

                    frame.close();

                    //convert points, halfing distances b/c vuforia does that internally so we gotta fix it
                    for(int i = 0; i < vufPoints.length; i++) imagePoints[i] = new Point((int)vufPoints[i][0], (int)vufPoints[i][1]);

                    Scalar green = new Scalar(0, 255, 0);
                    for(int i = 0; i < 2; i++)
                        for(int o = 0; o < 4; o++)
                            Imgproc.line(out, imagePoints[o == 0 ? 3 + i * 4 : i * 4 + o - 1], imagePoints[i * 4 + o], green);

                    //connect the rectangles
                    for(int i = 0; i < 4; i++) Imgproc.line(out, imagePoints[i], imagePoints[i + 4], green);

                    for(int i = 8; i < imagePoints.length; i++) Imgproc.drawMarker(out, imagePoints[i], green);

                    //calculate points from projection
                    //find the midpoint between the two points
                    int leftXPoint = (int)((imagePoints[8].x + imagePoints[9].x) / 2.0);
                    int leftYPoint = (int)((imagePoints[8].y + imagePoints[9].y) / 2.0);
                    //find the y distande between the two
                    leftDist = (int)(Math.abs(imagePoints[8].y - imagePoints[9].y) / 2.0);
                    leftBall = new int[] {leftXPoint - (leftDist / 2), leftYPoint - (leftDist / 2)};

                    //find the midpoint between the two points
                    int rightXPoint = (int)((imagePoints[10].x + imagePoints[11].x) / 2.0);
                    int rightYPoint = (int)((imagePoints[10].y + imagePoints[11].y) / 2.0);
                    //find the y distande between the two
                    rightDist = (int)(Math.abs(imagePoints[10].y - imagePoints[11].y) / 2.0);
                    rightBall = new int[] {rightXPoint - (rightDist / 2), rightYPoint - (rightDist / 2)};

                    //operation: subsquare
                    //take a square mat we are 100% sure will have a ball in it
                    //sum it up and find the average color

                    leftColor = drawSquare(out, leftBall, leftDist);
                    rightColor = drawSquare(out, rightBall, rightDist);

                    //flip it for display
                    Core.flip(out, out, -1);

                    matQueue.add(out);

                    //display!
                    mView.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Mat frame = matQueue.take();
                                //convert to bitmap
                                Utils.matToBitmap(frame, bm);
                                frame.release();
                                mView.invalidate();
                            }
                            catch (InterruptedException e) {
                                //huh
                            }
                        }
                    });
                }
                catch (Exception e){
                    Log.e("OPENCV", e.getLocalizedMessage());
                }
            }
        } else {
            telemetry.addData("VuMark", "not visible");
        }
    }

    @Override
    public void start() {
        if(leftColor != null && rightColor != null) {
            if (leftColor.val[0] < leftColor.val[1] && rightColor.val[0] > rightColor.val[1]){
                telemetry.addData("Left", "Blue");

                //construct sequence
                mSeq.add(new AutoLib.TimedMotorStep(bot.getMotor("stick"), 0.2f, 0.7, true));
                if(RED) mSeq.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), -0.5f, 0.5, true));
                else mSeq.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), 0.5f, 0.5, true));
                mSeq.add(new AutoLib.TimedMotorStep(bot.getMotor("stick"), -0.2f, 0.7, true));
            }
            else if (leftColor.val[0] > leftColor.val[1] && rightColor.val[0] < rightColor.val[1]) {
                telemetry.addData("Left", "Red");

                //construct sequence
                mSeq.add(new AutoLib.TimedMotorStep(bot.getMotor("stick"), 0.2f, 0.7, true));
                if(RED) mSeq.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), 0.5f, 0.5, true));
                else mSeq.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), -0.5f, 0.5, true));
                mSeq.add(new AutoLib.TimedMotorStep(bot.getMotor("stick"), -0.2f, 0.7, true));
            }
            else telemetry.addData("Left", "Indeterminate");
        }
        else telemetry.addData("Left", "Undefined");
    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
    }

    private static Scalar drawSquare(Mat src, int[] ballPoint, int ballDist) {
        //find average left and right ball square
        //find the average color for all the pixels in that square
        if(ballPoint[0] >= 0 && ballPoint[1] >= 0 && ballPoint[0] + ballDist < src.cols() && ballPoint[1] + ballDist < src.rows()){
            double total[] = new double[3];
            for(int x = 0; x < ballDist; x++)
                for(int y = 0; y < ballDist; y++) {
                    double[] pixel = src.get(y + ballPoint[1], x + ballPoint[0]);
                    total[0] += pixel[0];
                    total[1] += pixel[1];
                    total[2] += pixel[2];
                }

            //make average color
            Scalar color = new Scalar(total[0] / (ballDist * ballDist), total[1] / (ballDist * ballDist), total[2] / (ballDist * ballDist));

            Imgproc.rectangle(src, new Point(ballPoint[0], ballPoint[1]), new Point(ballPoint[0] + ballDist, ballPoint[1] + ballDist), color, -1);

            return color;
        }
        else return null;
    }

    @Override
    public void stop() {
        mView.setAlpha(0.0f);
        mView.post(new Runnable() {
            @Override
            public void run() {
                mView.setImageDrawable(null);
                mView.invalidate();
                if(bm != null) bm.recycle();
                if(matQueue != null) matQueue.clear();
            }
        });
        this.ray.clear();
    }

    private String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    private Mat getMatFromImage(Image img) {
        java.nio.ByteBuffer grey = img.getPixels();
        Mat thing = new Mat(img.getHeight(), img.getWidth(), CV_8U);
        byte[] ray = new byte[grey.limit()];
        grey.rewind();

        grey.get(ray);
        thing.put(0, 0, ray);
        //rotate -90
        //Core.transpose(thing, thing);
        //Core.flip(thing, thing, 0);

        //fill color space
        Imgproc.cvtColor(thing, thing, Imgproc.COLOR_GRAY2RGB);

        return thing;
    }

    /**
     * I'm so sorry
     * Shims created so I can access the pose in vuforia matrix form
     * they are protected variables, so I need to java fu my way into them
     */
    //start from the very begining...
    //vuforia localizer shim
    private static class VuforiaLocalizerShim extends VuforiaLocalizerImpl {
        //constructor
        VuforiaLocalizerShim(Parameters params){
            super(params);
        }
        //the shim part
        public VuforiaTrackables loadShimTrackablesFromAsset(String assetName){
            return loadTrackablesFromAsset(assetName, VuforiaDefaultListenerShim.class);
        }

        public void stop() {
            //god damn quallcomm
            super.close();
        }
    }
    //vuforia default listener shim
    public static class VuforiaDefaultListenerShim extends VuforiaTrackableDefaultListener {
        //constructor shim
        public VuforiaDefaultListenerShim(VuforiaTrackable trackable) {
            super(trackable);
        }
        //and the jesus data gettums
        @Nullable
        public synchronized Matrix34F getRealPose() {
            return this.currentPose;
        }
    }
}
