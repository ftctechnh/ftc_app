package com.disnodeteam.dogecv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Debug;
import android.util.Log;
import android.view.Surface;

import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.qualcomm.robotcore.util.ThreadPool;
import com.vuforia.CameraDevice;
import com.vuforia.Frame;
import com.vuforia.Matrix34F;
import com.vuforia.Matrix44F;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Tool;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCaptureRequest;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraFrame;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCaptureSession;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackableImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackablesImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.VuforiaWebcam;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import static com.vuforia.Vuforia.setFrameFormat;

/**
 * An implementation of Vuforia intended to be cross-compatible with OpenCV (and DogeCV by extension)
 */

public class Dogeforia extends VuforiaLocalizerImpl {
    DogeCVDetector detector;
    DrawViewSource displayView;
    boolean dogeCVEnabled;
    boolean showDebug = false;

    Thread workerThread;
    Bitmap outputImage;
    Bitmap bitmap;
    Mat inputMat;
    Mat outMat;
    BlockingQueue<CloseableFrame> frames;
    public Dogeforia(Parameters parameters) {
        super(parameters);
    }

    public void setDogeCVDetector(DogeCVDetector detector){
        this.detector = detector;
        detector.enable();
        displayView = detector.getRawView();
        setMonitorViewParent(displayView.getId());
        setFrameQueueCapacity(1);
    }

    public void start(){
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

    public void enableDogeCV(){

        dogeCVEnabled = true;
    }

    public void disableDogeCV(){
        dogeCVEnabled = false;
    }
    public void enableTrack(){
        startTracker();
    }

    public void disableTrack() {
        stopTracker();
    }
    public void showDebug(){
        showDebug = true;

    }

    public void processFrame(Frame frame){
        if(frame != null ){

            bitmap = convertFrameToBitmap(frame);

            inputMat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,inputMat);

            outMat = detector.processFrame(inputMat, null);

            if(showDebug){
                VuforiaTrackablesImpl trackables = loadedTrackableSets.get(0);
                int count = 0;
                for(VuforiaTrackable trackable : trackables){
                    if(trackable == null || ((VuforiaTrackableDefaultListener)trackable.getListener()) == null){
                        continue;
                    }
                    if(((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()){
                        Imgproc.putText(outMat,"Vuforia: " + trackable.getName(), new Point(10,50 * count + 50),0,2,new Scalar(0,255,0),3);
                        count++;
                    }

                }
            }


            if(!outMat.empty() ){

                bitmap.setHeight(outMat.height());
                bitmap.setWidth(outMat.width());
                Utils.matToBitmap(outMat, bitmap);



                //height = <user-chosen width> * original height / original width
                double adjustedHieght = displayView.getWidth() * outMat.height()/ outMat.width();
                outputImage =  Bitmap.createScaledBitmap(bitmap,displayView.getWidth(), (int)adjustedHieght, false);

                ((Activity)displayView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayView.onFrame(outputImage);
                        displayView.invalidate();
                    }
                });

            }else{
                Log.w("DogeCV", "MAT BITMAP MISMATCH OR EMPTY ERROR");
            }


            inputMat.release();
            outMat.release();


        }else{
            Log.d("DogeCV", "No Frame!");
        }
    }

    public void render() {
       // Log.d("DogeCV", "Rendering Frame");
       // super.onRenderFrame()

        if(detector != null && dogeCVEnabled){

            if(!getFrameQueue().isEmpty()){
                try {
                    processFrame(getFrameQueue().take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                Log.w("DogeCV", "Frame is empty wtf: " + getFrameQueueCapacity());
            }

            /*
            getFrameOnce(Continuation.create(ThreadPool.getDefault(), new Consumer<Frame>()
            {
                @Override public void accept(Frame frame)
                {
                    processFrame(frame);
                }
            }));
             */
        }

    }

    public void stop(){
        close();
        ((Activity)displayView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                workerThread.interrupt();

                detector.disable();
            }
        });

    }
}
