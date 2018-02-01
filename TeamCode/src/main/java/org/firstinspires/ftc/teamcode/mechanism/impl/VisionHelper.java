package org.firstinspires.ftc.teamcode.mechanism.impl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * A helper class that initializes Vuforia and is a wrapper for {@link VuforiaLocalizer}.
 * Using Vuforia, this class provides a means to read {@link Mat}s acquired from the camera
 * for use with OpenCV.
 */

public class VisionHelper implements IMechanism {
    private static final String LOG_TAG = "VisionHelper";

    private Activity activity;
    private VuforiaLocalizer vuforia;

    /**
     * Construct a new instance of this class with a reference to the robot and the
     * {@link VuforiaLocalizer} currently in use.
     *
     * @param robot the robot utilizing this object
     */
    public VisionHelper(Robot robot) {
        this.activity = (Activity)robot.getCurrentOpMode().hardwareMap.appContext;
    }

    /**
     * Initialize Vuforia with the specified {@code cameraDirection}.
     * On a ZTE Speed phone, this method blocks for a few seconds as determined by minimal testing.
     *
     * @param cameraDirection the desired camera direction to use with Vuforia
     */
    public void initializeVuforia(VuforiaLocalizer.CameraDirection cameraDirection) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey =
                "ATI4rJn/////AAAAGaU649WKjUPNjEAS8RHKRjFL+3htvDbOq/i5kIheS3" +
                "6mg04zehxVYIKQacMQbsHteW8MaFqibM5CTPJkObcAnnIe+Yt8uA2E288cN1g2LRuu6OmJWUgUNrfH9Oe" +
                "p/MDDh8/mOWD2osAziNUsh7xYdb2FH6VmGFomR8Whb1i+3t5ilAGd0mIOd6OFFSA8IcRxcw9EfE0SIFmg" +
                "SXwi05SMU5CkUtnidIBpC+w6wfNp2BLL863XgaZjfpsNz57TaKqgxuy/HBjec0uvS2JQ/vBVeKV72FGVv" +
                "SLJlPh/nQZ964O/5VpNbHUmZYhyKYQgTvP2HuUZ+azjY/WGUDTOCB2ZxZj9wSmX5iCY7k9rI0cMNIri";

        parameters.cameraDirection = cameraDirection;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
    }

    /**
     * Asynchronously initialize OpenCV. If the OpenCV Manager app is not installed, the user will
     * be prompted to install it from the Play Store. If the user does not install the app and
     * returns to the Robot Controller app, this method will silently fail.
     */
    public void initializeOpenCV() {
        final BaseLoaderCallback openCvCallBack = new BaseLoaderCallback(activity) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        Log.d(LOG_TAG, "OpenCV Manager connected");
                        break;
                    default:
                        Log.d(LOG_TAG, "Something went wrong initializing OpenCV");
                        super.onManagerConnected(status);
                }
            }
        };

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, activity, openCvCallBack);

        vuforia.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
     }

    /**
     * Read an OpenCV {@link Mat} using the {@link VuforiaLocalizer} provided to the constructor
     * of this class. If a frame could not be read, for example if an {@link InterruptedException}
     * is thrown when attempting to take a frame from the vuforia frame queue, this method
     * internally handles that exception and returns null. The {@link #initializeOpenCV()} method
     * must be called before calling this method.
     * <p>
     * The OpenCV color-space of the converted frame returned by this method is BGR (blue, green, red).
     * <p>
     * This method has been minimally tested on a ZTE Speed phone to convert frames at a rate of
     * about 10 FPS on average.
     *
     * @see Mat the converted BGR image
     * @return the OpenCV matrix object or null if the frame couldn't be read
     */
     public Mat readOpenCVFrame() {
         VuforiaLocalizer.CloseableFrame frame;
         Image rgb = null;

         try {
             // grab the last frame pushed onto the queue
             frame = vuforia.getFrameQueue().take();
         } catch (InterruptedException e) {
             Log.d(LOG_TAG, "Problem taking frame off Vuforia queue");
             e.printStackTrace();
             return null;
         }

         // basically get the number of formats for this frame
         long numImages = frame.getNumImages();

         // initialize rgb object if one of the image formats is RGB565
         for(int i = 0; i < numImages; i++) {
             if(frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                 rgb = frame.getImage(i);
                 break;
             }
         }

         if(rgb == null) {
             Log.d(LOG_TAG, "Image format not found");
             return null;
         }

         // create a new bitmap and copy the byte buffer returned by rgb.getPixels() to it
         Bitmap bm = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
         bm.copyPixelsFromBuffer(rgb.getPixels());

         // construct an OpenCV mat from the bitmap using Utils.bitmapToMat()
         Mat mat = new Mat(bm.getWidth(), bm.getHeight(), CvType.CV_8UC4);
         Utils.bitmapToMat(bm, mat);

         // convert to BGR before returning
         Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);

         frame.close();

         Log.d(LOG_TAG, "Frame closed");

         return mat;
     }

    /**
     * Get the {@link VuforiaLocalizer} for robot algorithms to use.
     *
     * @return the vuforia localizer instance this class wraps around
     */
    public VuforiaLocalizer getVuforia() {
         return vuforia;
     }
}
