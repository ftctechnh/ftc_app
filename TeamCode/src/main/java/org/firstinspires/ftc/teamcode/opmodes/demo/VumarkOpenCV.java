package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.sun.tools.javac.util.ByteBuffer;
import com.vuforia.CameraCalibration;
import com.vuforia.Image;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaPoseMatrix;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackableImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackablesImpl;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLoad;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingQueue;

import static org.opencv.core.CvType.CV_32FC1;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.core.CvType.CV_8UC4;

import com.vuforia.Matrix34F;
import com.vuforia.Tool;
import com.vuforia.Trackable;
import com.vuforia.Vec2F;
import com.vuforia.Vec3F;

/**
 * Created by Noah on 9/12/2017.
 * getting rid of warning
 */

@Autonomous(name="Concept: VuMark OpenCV", group ="Concept")
//@Disabled
public class VumarkOpenCV extends OpenCVLoad {

    private BlockingQueue<VuforiaLocalizer.CloseableFrame> ray;
    private VuforiaLocalizerShim vuforia;
    private VuforiaTrackable relicTemplate;

    private ImageView mView;

    //bullshoot factors
    private static final float horizBullFactor = -15.0f;
    private static final float vertBullFactor = -7.0f;

    //viewing angles for nexus 5x (in degreres)
    private static final float horizontalViewAngle = 53.2988f + horizBullFactor;
    private static final float verticalViewAngle = 67.5747f + vertBullFactor;

    //viewport sizes
    private static final int viewWidth = 360;
    private static final int viewHeight = 640;

    //pixel constants
    private static final float horizConst = (float)((viewWidth * 0.5) / Math.tan(Math.toRadians(0.5 * horizontalViewAngle)));
    private static final float vertConst = -(float)((viewHeight * 0.5) / Math.tan(Math.toRadians(0.5 * verticalViewAngle)));

    //center points
    private static final int centerX = viewWidth / 2;
    private static final int centerY = viewHeight / 2;

    //define the jewel platform relative to the image with a buncha vectors to add
    //all units in vuforia are mm, so we multiply to inches to make it readable
    private static final float inToMM = 25.4f;


    //identity mats to be constructed later in the project
    private Vec3F[] point;
    private float[] size;
    //output bitmap
    Bitmap bm;
    //Canvas canvas;
    //Paint p;


    //storage camera calibration
    private CameraCalibration camCal;

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
        camCal = vuforia.getCameraCalibration();

        //constrt matrixes
        //construct points like a box
        Vec3F botLeft = new Vec3F(-5 * inToMM, -5 * inToMM, 0);
        Vec3F botRight = new Vec3F(-10 * inToMM, -5 * inToMM, 0);
        Vec3F botFrontLeft = new Vec3F(-5 * inToMM, -5 * inToMM, 2 * inToMM);
        Vec3F botFrontRight = new Vec3F(-10 * inToMM, -5* inToMM, 2 * inToMM);

        Vec3F topLeft = new Vec3F(-5 * inToMM, -3 * inToMM, 0);
        Vec3F topRight = new Vec3F(-10 * inToMM, -3 * inToMM, 0);
        Vec3F topFrontLeft = new Vec3F(-5 * inToMM, -3 * inToMM, 2 * inToMM);
        Vec3F topFrontRight = new Vec3F(-10 * inToMM, -3 * inToMM, 2 * inToMM);

        point = new Vec3F[] {   botLeft, botRight, botFrontRight, botFrontLeft,
                                topLeft, topRight, topFrontRight, topFrontLeft};

        /*opencv stuff
        tvec = new Mat(3, 1, CV_32FC1);
        rvec = new MatOfFloat(0, 0, 0);
        distCoff = new MatOfDouble(0, 0, 0, 0);
        cameraMatrix = new Mat(3, 3, CV_32FC1);
        cameraMatrix.put(0, 0, new float[] {horizConst, 0, centerX});
        cameraMatrix.put(1, 0, new float[] {0, vertConst, centerY});
        cameraMatrix.put(2, 0, new float[] {0, 0, 1});
        imagePoints = new MatOfPoint2f();
        */

        size = camCal.getSize().getData();

        //half size b/c vuforia does that internally
        size[0] /= 2;
        size[1] /= 2;

        Log.i("OPENCV", "width " + size[0]);
        Log.i("OPENCV", "height " + size[1]);

        bm = Bitmap.createBitmap((int)size[0], (int)size[1], Bitmap.Config.ARGB_8888);
        //canvas = new Canvas(bm);
        //p = new Paint();
        //p.setColor(Color.GREEN);
        //p.setStrokeWidth(1);

        //setup view
        mView.post(new Runnable() {
            @Override
            public void run() {
                mView.setImageBitmap(bm);
            }
        });

        initOpenCV();

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
    }

    @Override
    public void start() {
        VuforiaTrackables relicTrackables = this.vuforia.loadShimTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();

        this.vuforia.setFrameQueueCapacity(1);
        this.ray = this.vuforia.getFrameQueue();
    }

    @Override
    public void loop() {
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
                final float[][] vufPoints = new float[8][2];

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
                        //Log.i("OPENCV", "Image " + img + " " + frame.getImage(img).getWidth());
                        if(frame.getImage(img).getWidth() == size[0]) break;
                    }

                    final Mat out;
                    if(img < frame.getNumImages()) out = getMatFromImage(frame.getImage(img));
                    else throw new IllegalArgumentException("No frame with matching width!");

                    frame.close();

                    //create an array of points fron the float
                    Point imagePoints[] = new Point[vufPoints.length];

                    //convert points, halfing distances b/c vuforia does that internally so we gotta fix it
                    for(int i = 0; i < vufPoints.length; i++) imagePoints[i] = new Point((int)vufPoints[i][0] / 2, (int)vufPoints[i][1] / 2);

                    Scalar color = new Scalar(0, 255, 0);
                    for(int i = 0; i < 2; i++)
                        for(int o = 0; o < 4; o++)
                            Imgproc.line(out, imagePoints[o == 0 ? 3 + i * 4 : i * 4 + o - 1], imagePoints[i * 4 + o], color);

                    //connect the rectangles
                    for(int i = 0; i < 4; i++) Imgproc.line(out, imagePoints[i], imagePoints[i + 4], color);

                    //flip it for display
                    Core.flip(out, out, -1);

                    //display!
                    mView.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            //convert to bitmap
                            Utils.matToBitmap(out, bm);
                            mView.invalidate();
                            //old vuforia stuff
                            /*
                            //clear canvas
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            for(int i = 0; i < 2; i++)
                                for(int o = 0; o < 4; o++)
                                    canvas.drawLine(imagePoints[o == 0 ? 3 + i * 4 : i * 4 + o - 1][0], imagePoints[o == 0 ? 3 + i * 4 : i * 4 + o - 1][1], imagePoints[i * 4 + o][0], imagePoints[i * 4 + o][1], p);

                            //connect the rectangles
                            for(int i = 0; i < 4; i++) canvas.drawLine(imagePoints[i][0], imagePoints[i][1], imagePoints[i + 4][0], imagePoints[i + 4][1], p);
                            */
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
    public void stop() {
        mView.setAlpha(0.0f);
        mView.post(new Runnable() {
            @Override
            public void run() {
                mView.setImageDrawable(null);
                mView.invalidate();
                if(bm != null) bm.recycle();
            }
        });
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
        VuforiaLocalizerShim(VuforiaLocalizer.Parameters params){
            super(params);
        }
        //the shim part
        public VuforiaTrackables loadShimTrackablesFromAsset(String assetName){
            return loadTrackablesFromAsset(assetName, VuforiaDefaultListenerShim.class);
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
