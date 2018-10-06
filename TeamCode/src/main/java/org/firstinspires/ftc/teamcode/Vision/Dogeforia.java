package org.firstinspires.ftc.teamcode.Vision;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.disnodeteam.dogecv.DrawViewSource;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.vuforia.Frame;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackablesImpl;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Victo on 9/12/2018.
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

            if(!frameQueue.isEmpty()){
                try {
                    processFrame(frameQueue.take());
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

        ((Activity)displayView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                workerThread.interrupt();
                stopCamera();
                stopTracker();
                detector.disable();
            }
        });

    }
}
