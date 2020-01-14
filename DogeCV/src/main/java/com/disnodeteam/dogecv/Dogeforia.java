package com.disnodeteam.dogecv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.math.MathFTC;
import com.vuforia.Frame;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackablesImpl;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingQueue;

/**
 * An implementation of Vuforia intended to be cross-compatible with DogeCV
 */

public abstract class Dogeforia extends VuforiaLocalizerImpl {

    DrawViewSource rawView;

    Thread workerThread;
    Bitmap outputImage;
    Bitmap rotatedImage;
    Bitmap bitmap;
    Mat inputMat;
    Mat outMat;
    Mat rotatedMat;
    Mat displayMat;
    public Dogeforia(Parameters parameters) {
        super(parameters);
    }

    /**
     * Sets the raw view. Make sure to run this before start()!
     * @param rawView The raw view to which we should display the camera frame
     */
    public void setRawView(DrawViewSource rawView){
        this.rawView = rawView;
        setFrameQueueCapacity(1);
    }

    /**
     * Starts Dogeforia
     */
    public synchronized void start(){

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!workerThread.isInterrupted()){
                    render();
                }
            }
        });
        workerThread.setName("Dogeforia Thread");
        workerThread.start();
        Log.d("DogeCV", workerThread.getState().toString());
    }

    public abstract Mat analyzeFrame(Mat rgba, Mat gray);

    /**
     * Analyzes the frame passed to this class using DogeCV
     * @param frame The processed frame to be displayed
     */
    public void anaylzeFrame(Frame frame){
        if(frame != null ){
            bitmap = convertFrameToBitmap(frame);
            inputMat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,inputMat);
            rotatedMat = new Mat();
            Core.flip(inputMat.t(), rotatedMat, 1); //Adjust this line to change the image rotation
            outMat = analyzeFrame(rotatedMat, null);

            if(!outMat.empty() ){

                displayMat = new Mat();
                outMat.copyTo(displayMat);
                rotatedImage = Bitmap.createBitmap(displayMat.width(), displayMat.height(), bitmap.getConfig());
                Utils.matToBitmap(displayMat, rotatedImage);

                //height = <user-chosen width> * original height / original width
                Size newSize = MathFTC.fullscreen(displayMat.size(), new Size(rawView.getWidth(), rawView.getHeight()));
                outputImage =  Bitmap.createScaledBitmap(rotatedImage, (int) newSize.width, (int) newSize.height, false);

                ((Activity) rawView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rawView.onFrame(outputImage);
                        rawView.invalidate();
                    }
                });
            }else{
                Log.w("DogeCV", "MAT BITMAP MISMATCH OR EMPTY ERROR");
            }
            inputMat.release();
            rotatedMat.release();
            outMat.release();
            displayMat.release();
        } else{
            Log.d("DogeCV", "No Frame!");
        }
    }

    /**
     * Renders the frame passed to this class through Vuforia
     */
    public void render() {
        if(!getFrameQueue().isEmpty()){
            try {
                anaylzeFrame(getFrameQueue().take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else{
            Log.v("DogeCV", "Frame is empty. Que Size: " + getFrameQueueCapacity());
        }

    }

    /**
     * Terminates Dogeforia
     */
    public synchronized void stop(){
        close();
        ((Activity) rawView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                workerThread.interrupt();
            }
        });
    }
}
