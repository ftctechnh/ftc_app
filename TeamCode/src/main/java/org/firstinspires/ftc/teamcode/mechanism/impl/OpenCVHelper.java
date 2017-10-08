package org.firstinspires.ftc.teamcode.mechanism.impl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.IMechanism;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * A helper class that initializes OpenCV and allows for reading {@link Mat}s
 * while Vuforia has acquired the camera.
 */

public class OpenCVHelper implements IMechanism {
    private static final String LOG_TAG = "OpenCVHelper";

    private Activity activity;
    private VuforiaLocalizer vuforia;

    /**
     * Construct a new instance of this class with a reference to the robot and the
     * {@link VuforiaLocalizer} currently in use.
     *
     * @param robot the robot utilizing this object
     * @param vuforia the vuforia localizer currently in use
     */
    public OpenCVHelper(Robot robot, VuforiaLocalizer vuforia) {
        this.activity = (Activity)robot.getCurrentOpMode().hardwareMap.appContext;

        this.vuforia = vuforia;
        vuforia.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
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
                    default:
                        Log.d(LOG_TAG, "Something went wrong initializing OpenCV");
                        super.onManagerConnected(status);
                }
            }
        };

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, activity, openCvCallBack);
     }

    /**
     * Read an OpenCV {@link Mat} using the {@link VuforiaLocalizer} provided to the constructor
     * of this class. If a frame could not be read, for example if an {@link InterruptedException}
     * is thrown when attempting to take a frame from the vuforia frame queue, this method
     * internally handles that exception and returns null. The {@link #initializeOpenCV()} method
     * must be called before calling this method.
     *
     * @see Mat Mat data structure
     * @return the OpenCV matrix object or null if the frame couldn't be read
     */
     public Mat readFrame() {
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

         // set rgb object if one of the formats is RGB565
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

         frame.close();

         Log.d(LOG_TAG, "Frame closed");

         return mat;
     }
}
