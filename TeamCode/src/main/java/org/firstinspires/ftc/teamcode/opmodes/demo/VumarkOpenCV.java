package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingQueue;

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

    //sensor sizes for my nexus 5x
    private static final float sensroWidthMM = 10.2f;
    private static final float sensorHeightMM = 8.0f;

    //viewing angles for nexus 5x (in degreres)
    private static final float horizontalViewAngle = 67.5747f;
    private static final float verticalViewAngle = 53.2988f;

    //will need to get sensor sizes for other phones if I do desire

    //the final focal for openCV calculation

    private static double calcFocalPixel(int length, float angle) {
        return (length * 0.5f) / Math.tan(Math.toRadians(0.5f * angle));
    }

    @Override
    public void init() {
        mView = (ImageView) ((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.OpenCVOverlay);
        mView.setAlpha(1.0f);

        initOpenCV();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AZPbGaP/////AAAAGcIykH/KO0QNvZGSYxc0fDlVytYrk0HHv6OLmjHsswRvi/1l9RZCkepChaAZup3DIJlrjK2BV57DEz/noNO0oqT9iu2moP/svGmJ+pBG7FlfF4RHxu6UhvVLaKUZCsTJ1zTkd7XnMuRw8aSuIxowOiLJQYcgjmddi11LG26lAr6aRmoWJzr2pv6Yui2Gom0wt9J4+1g3kXqjngnH3h6NPA/6aUfpVngFaFPp5knyDJWZT88THttPsqcKW41QC/qgNh3CHIdADu15Rm51JNRlvG+2+sYstiHeHFQqCDwUkTgWor0v/Bk+xXoj3oUCb4REwT9w94E/VEI4qEAFPpmeo6YgxQ4LLFknu6tgNy8xdD6S";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
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
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
            telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
            if (pose != null) {
                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                // Extract the X, Y, and Z components of the offset of the target relative to the robot
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);

                // Extract the rotational components of the target relative to the robot
                double rX = rot.firstAngle;
                double rY = rot.secondAngle;
                double rZ = rot.thirdAngle;
            }
        }
        else {
            telemetry.addData("VuMark", "not visible");
        }

        try {
            VuforiaLocalizer.CloseableFrame frame = ray.take();
            telemetry.addData("frame", frame.getNumImages() + " images");
            for (int i = 0; i < frame.getNumImages(); i++) {
                Image temp = frame.getImage(i);
                telemetry.addData("image #" + i, "type: %d width: %d", temp.getFormat(), temp.getBufferWidth());
            }

            Mat temp = getMatFromImage(frame.getImage(3));
            frame.close();
            //now we have a mat, lets draw a box on it
            Imgproc.rectangle(temp, new Point(10, 10), new Point(50, 50), new Scalar(0, 255, 0));

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
        }
        catch (InterruptedException e) {
            //oops
        }

        telemetry.update();
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
        Mat dst = new Mat(thing.cols(), thing.rows(), CV_8U);
        Core.transpose(thing, thing);
        Core.flip(thing, thing, 1);

        //fill color space
        Imgproc.cvtColor(thing, thing, Imgproc.COLOR_GRAY2RGB);

        return thing;
    }
}
