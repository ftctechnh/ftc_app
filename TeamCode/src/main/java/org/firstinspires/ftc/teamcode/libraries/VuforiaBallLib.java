package org.firstinspires.ftc.teamcode.libraries;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.vuforia.CameraCalibration;
import com.vuforia.Image;
import com.vuforia.Matrix34F;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Tool;
import com.vuforia.Vec3F;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.teamcode.opmodes.demo.VumarkOpenCV;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.opencv.core.CvType.CV_8UC3;

/**
 * Created by Noah on 11/3/2017.
 * Library to handle the vumark and projection of points onto camera image
 * Also includes openCV functionality
 */

public abstract class VuforiaBallLib extends OpenCVLoad {
    //define the jewel platform relative to the image with a buncha vectors to add
    //all units in vuforia are mm, so we multiply to inches to make it readable
    protected static final float inToMM = 25.4f;

    //units in inches, then converted to mm
    protected static final float boxLeftFromImage = 3.625f * inToMM;
    protected static final float boxBottomFromImage = 5.125f * inToMM;
    protected static final float boxWidth = 9.75f * inToMM;
    protected static final float boxLength = 3.75f * inToMM;
    protected static final float boxHeight = 3.75f * inToMM;

    protected static final float ballCenterFromBoxEdge = 1.875f * inToMM;

    //identity mats to be constructed later in the project
    protected Vec3F[] point;
    //output bitmap
    protected Bitmap bm;
    private boolean bmChanged = false;
    //output bitmap lock
    private static final Object bmLock = new Object();

    private ImageView mView;

    protected BlockingQueue<VuforiaLocalizer.CloseableFrame> ray;

    //vuforia objects
    protected VuforiaLocalizerShim vuforia;
    protected VuforiaTrackable relicTemplate;
    protected CameraCalibration camCal;

    protected RelicRecoveryVuMark tempMark;

    protected boolean displayData = false;

    //load vuforia libraries and configure
    protected void initVuforia(boolean displayData) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AZPbGaP/////AAAAGcIykH/KO0QNvZGSYxc0fDlVytYrk0HHv6OLmjHsswRvi/1l9RZCkepChaAZup3DIJlrjK2BV57DEz/noNO0oqT9iu2moP/svGmJ+pBG7FlfF4RHxu6UhvVLaKUZCsTJ1zTkd7XnMuRw8aSuIxowOiLJQYcgjmddi11LG26lAr6aRmoWJzr2pv6Yui2Gom0wt9J4+1g3kXqjngnH3h6NPA/6aUfpVngFaFPp5knyDJWZT88THttPsqcKW41QC/qgNh3CHIdADu15Rm51JNRlvG+2+sYstiHeHFQqCDwUkTgWor0v/Bk+xXoj3oUCb4REwT9w94E/VEI4qEAFPpmeo6YgxQ4LLFknu6tgNy8xdD6S";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE;
        //bwahaha java shim
        this.vuforia = new VuforiaLocalizerShim(parameters);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB888, true); //enables RGB888 format for the image
        VuforiaTrackables relicTrackables = this.vuforia.loadShimTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        vuforia.setFrameQueueCapacity(1);
        ray = vuforia.getFrameQueue();

        this.camCal = vuforia.getCameraCalibration();

        camCal = vuforia.getCameraCalibration();
        this.displayData = displayData;

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

        initOpenCV();

        //setup view
        if(displayData) {
            mView = (ImageView)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.OpenCVOverlay);
            mView.post(new Runnable() {
                @Override
                public void run() {
                    mView.setAlpha(1.0f);
                    mView.setImageBitmap(bm);
                }
            });
        }
    }

    //start vuforia tracking
    protected void startTracking() {
        relicTemplate.getTrackables().activate();
    }

    protected RelicRecoveryVuMark isTracking() {
        return RelicRecoveryVuMark.from(relicTemplate);
    }

    protected OpenGLMatrix getPose() {
        return ((VuforiaDefaultListenerShim) relicTemplate.getListener()).getPose();
    }

    protected BallColor getBallColor() {
        //get frame from vuforia
        try{
            Mat out = getFrame();

            //get vuforia's real position matrix
            Matrix34F goodCodeWritten = ((VuforiaDefaultListenerShim) relicTemplate.getListener()).getRealPose();


            if(goodCodeWritten == null) return BallColor.Undefined;

            this.tempMark = RelicRecoveryVuMark.from(relicTemplate);

            final float[][] ballPoints = new float[4][2];
            //the actual color determination
            for(int i = 8; i < point.length; i++) ballPoints[i - 8] = Tool.projectPoint(camCal, goodCodeWritten, point[i]).getData();

            Point imagePoints[] = new Point[ballPoints.length];
            //convert points to opencv language
            for(int i = 0; i < ballPoints.length; i++) imagePoints[i] = new Point((int)ballPoints[i][0], (int)ballPoints[i][1]);

            //calculate points from projection
            //find the midpoint between the two points
            int leftXPoint = (int)((imagePoints[0].x + imagePoints[1].x) / 2.0);
            int leftYPoint = (int)((imagePoints[0].y + imagePoints[1].y) / 2.0);
            //find the y distande between the two
            int leftDist = (int)(Math.abs(imagePoints[0].y - imagePoints[1].y) / 2.0);
            int[] leftBall = new int[] {leftXPoint - (leftDist / 2), leftYPoint - (leftDist / 2)};

            //find the midpoint between the two points
            int rightXPoint = (int)((imagePoints[2].x + imagePoints[3].x) / 2.0);
            int rightYPoint = (int)((imagePoints[2].y + imagePoints[3].y) / 2.0);
            //find the y distande between the two
            int rightDist = (int)(Math.abs(imagePoints[2].y - imagePoints[3].y) / 2.0);
            int[] rightBall = new int[] {rightXPoint - (rightDist / 2), rightYPoint - (rightDist / 2)};
            //operation: subsquare
            //take a square mat we are 100% sure will have a ball in it
            //sum it up and find the average color

            Scalar leftColor = drawSquare(out, leftBall, leftDist);
            Scalar rightColor = drawSquare(out, rightBall, rightDist);

            if(displayData) {
                float[][] squarePoints = new float[8][2];
                for(int i = 0; i < 8; i++) squarePoints[i] = Tool.projectPoint(camCal, goodCodeWritten, point[i]).getData();
                Point[] squarePointRay = new Point[squarePoints.length];
                for(int i = 0; i < squarePoints.length; i++) squarePointRay[i] = new Point((int)squarePoints[i][0], (int)squarePoints[i][1]);

                Scalar green = new Scalar(0, 255, 0);
                for(int i = 0; i < 2; i++)
                    for(int o = 0; o < 4; o++)
                        Imgproc.line(out, squarePointRay[o == 0 ? 3 + i * 4 : i * 4 + o - 1], squarePointRay[i * 4 + o], green);

                //connect the rectangles
                for(int i = 0; i < 4; i++) Imgproc.line(out, squarePointRay[i], squarePointRay[i + 4], green);

                for(int i = 0; i < imagePoints.length; i++) Imgproc.drawMarker(out, imagePoints[i], green);

                //flip it for display
                Core.flip(out, out, -1);

                drawFrame(out);
            }

            if(leftColor != null && rightColor != null) {
                if (leftColor.val[0] < leftColor.val[1] && rightColor.val[0] > rightColor.val[1]) return BallColor.LeftBlue;
                else if (leftColor.val[0] > leftColor.val[1] && rightColor.val[0] < rightColor.val[1]) return BallColor.LeftRed;
                else return BallColor.Indeterminate;
            }
            else return BallColor.Undefined;
        }
        catch (Exception e){
            Log.e("OPENCV", e.getMessage());
            return BallColor.Undefined;
        }
    }

    protected RelicRecoveryVuMark getLastVuMark() {
        return this.tempMark;
    }

    protected void stopTracking() {
        relicTemplate.getTrackables().deactivate();
    }

    protected void stopVuforia() {
        this.vuforia.stop();
        if(displayData) {
            mView.setAlpha(0.0f);
            mView.post(new Runnable() {
                @Override
                public void run() {
                    synchronized (bmLock) {
                        mView.setImageDrawable(null);
                        mView.invalidate();
                        if(bm != null) bm.recycle();
                    }
                }
            });
            this.ray.clear();
        }
    }

    protected static Scalar drawSquare(Mat src, int[] ballPoint, int ballDist) {
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

    protected void drawFrame(Mat frame) {
        //convert to bitmap, synchronized
        synchronized (bmLock) {
            if(bm == null || frame.rows() != bm.getHeight() && frame.cols() != bm.getWidth()){
                if(bm != null) bm.recycle();
                bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                bmChanged = true;
            }
            Utils.matToBitmap(frame, bm);
        }

        //display!
        mView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (bmLock) {
                    if(bmChanged) {
                        mView.setImageBitmap(bm);
                        bmChanged = false;
                    }
                    mView.invalidate();
                }
            }
        });
    }

    protected Mat getFrame() throws InterruptedException {
        VuforiaLocalizer.CloseableFrame frame = ray.take();

        int img = 0;
        for(; img < frame.getNumImages(); img++){
            //telemetry.addData("Image format " + img, frame.getImage(img).getFormat());
            if(frame.getImage(img).getFormat() == PIXEL_FORMAT.RGB888) break;
        }

        if(img == frame.getNumImages()) throw new IllegalArgumentException("Incorrect format");
        Image mImage = frame.getImage(img);

        //stick it in a Mat for "display purposes"
        Mat out = new Mat(mImage.getHeight(), mImage.getWidth(), CV_8UC3);

        java.nio.ByteBuffer color = mImage.getPixels();
        byte[] ray = new byte[color.limit()];
        color.rewind();
        color.get(ray);
        out.put(0, 0, ray);

        frame.close();

        return out;
    }

    public enum BallColor {
        LeftRed,
        LeftBlue,
        Indeterminate,
        Undefined;
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
        VuforiaLocalizerShim(VuforiaLocalizer.Parameters params){
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
