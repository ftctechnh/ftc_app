package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.sun.tools.javac.util.ByteBuffer;
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

/**
 * Created by Noah on 9/12/2017.
 * getting rid of warning
 */

@Autonomous(name="Concept: VuMark OpenCV", group ="Concept")
//@Disabled
public class VumarkOpenCV extends OpenCVLoad {

    private BlockingQueue<VuforiaLocalizer.CloseableFrame> ray;
    private VuforiaLocalizer vuforia;
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
    private MatOfPoint3f point;
    private Mat tvec;
    private Mat rvec;
    private MatOfDouble distCoff;
    private Mat cameraMatrix;
    //output mat
    private MatOfPoint2f imagePoints;

    //will need to get sensor sizes for other phones if I do desire
    private static final int BOT_LEFT = 0;
    private static final int BOT_RIGHT = 1;
    private static final int BOT_FRONT_LEFT = 2;
    private static final int BOT_FRONT_RIGHT = 3;
    private static final int TOP_LEFT = 4;
    private static final int TOP_RIGHT = 5;
    private static final int TOP_FRONT_LEFT = 6;
    private static final int TOP_FRONT_RIGHT = 7;


    @Override
    public void init() {
        mView = (ImageView) ((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.OpenCVOverlay);
        mView.setAlpha(1.0f);

        initOpenCV();
        //constrt matrixes
        //construct points like a box
        Point3 botLeft = new Point3(5 * inToMM, -5 * inToMM, 0);
        Point3 botRight = new Point3(10 * inToMM, -5 * inToMM, 0);
        Point3 botFrontLeft = new Point3(5 * inToMM, -5 * inToMM, 2 * inToMM);
        Point3 botFrontRight = new Point3(10 * inToMM, -5 * inToMM, 2 * inToMM);

        Point3 topLeft = new Point3(5 * inToMM, -2 * inToMM, 0);
        Point3 topRight = new Point3(10 * inToMM, -2 * inToMM, 0);
        Point3 topFrontLeft = new Point3(5 * inToMM, -2 * inToMM, 2 * inToMM);
        Point3 topFrontRight = new Point3(10 * inToMM, -2 * inToMM, 2 * inToMM);

        point = new MatOfPoint3f(   botLeft, botRight, botFrontLeft, botFrontRight,
                                    topLeft, topRight, topFrontLeft, topFrontRight);

        tvec = new Mat(3, 1, CV_32FC1);
        rvec = new MatOfFloat(0, 0, 0);
        distCoff = new MatOfDouble(0, 0, 0, 0);
        cameraMatrix = new Mat(3, 3, CV_32FC1);
        cameraMatrix.put(0, 0, new float[] {horizConst, 0, centerX});
        cameraMatrix.put(1, 0, new float[] {0, vertConst, centerY});
        cameraMatrix.put(2, 0, new float[] {0, 0, 1});
        imagePoints = new MatOfPoint2f();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AZPbGaP/////AAAAGcIykH/KO0QNvZGSYxc0fDlVytYrk0HHv6OLmjHsswRvi/1l9RZCkepChaAZup3DIJlrjK2BV57DEz/noNO0oqT9iu2moP/svGmJ+pBG7FlfF4RHxu6UhvVLaKUZCsTJ1zTkd7XnMuRw8aSuIxowOiLJQYcgjmddi11LG26lAr6aRmoWJzr2pv6Yui2Gom0wt9J4+1g3kXqjngnH3h6NPA/6aUfpVngFaFPp5knyDJWZT88THttPsqcKW41QC/qgNh3CHIdADu15Rm51JNRlvG+2+sYstiHeHFQqCDwUkTgWor0v/Bk+xXoj3oUCb4REwT9w94E/VEI4qEAFPpmeo6YgxQ4LLFknu6tgNy8xdD6S";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
    }

    @Override
    public void start() {
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
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
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
            telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
            if (pose != null) {
                VectorF trans = pose.getTranslation();

                // Extract the X, Y, and Z components of the offset of the target relative to the robot
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);

                //create translation vector from that data
                tvec.put(0, 0, tX, tY, tZ);

                try {
                    VuforiaLocalizer.CloseableFrame frame = ray.take();
                    Mat temp = getMatFromImage(frame.getImage(1));
                    frame.close();

                    //now we have a mat, lets draw a point on it from conversions in 3d space
                    Calib3d.projectPoints(point, rvec, tvec, cameraMatrix, distCoff, imagePoints);

                    Point[] ray = imagePoints.toArray();

                    //draw a box!
                    //rectangles
                    Scalar color = new Scalar(0, 255, 0);
                    for(int i = 0; i < 2; i++)
                        for(int o = 1; o < 4; o++)
                            Imgproc.line(temp, ray[i * 4 + o - 1], ray[i * 4 + o], color);

                    //connect the rectangles
                    for(int i = 0; i < 4; i++) Imgproc.line(temp, ray[i], ray[i + 4], color);

                    //convert to bitmap
                    final Bitmap bm = Bitmap.createBitmap(temp.cols(), temp.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(temp, bm);

                    //display!
                    mView.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mView.setImageBitmap(bm);
                        }
                    });
                } catch (InterruptedException e) {
                    //oops
                }
            }
        } else {
            telemetry.addData("VuMark", "not visible");
        }
    }

    @Override
    public void stop() {
        mView.setAlpha(0.0f);
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
        Core.transpose(thing, thing);
        Core.flip(thing, thing, 0);

        //fill color space
        Imgproc.cvtColor(thing, thing, Imgproc.COLOR_GRAY2RGB);

        return thing;
    }
}
