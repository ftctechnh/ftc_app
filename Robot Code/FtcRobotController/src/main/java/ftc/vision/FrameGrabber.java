package ftc.vision;


import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * used to manage the frames that openCV takes
 * This file was made by the electronVolts, FTC team 7393
 * Date Created: 8/24/16.
 */
public class FrameGrabber implements CameraBridgeViewBase.CvCameraViewListener2 {
  public enum FrameGrabberMode {
    SINGLE,
    THROWAWAY,
    STOPPED
  }

  private FrameGrabberMode mode = FrameGrabberMode.STOPPED;

  private boolean saveImages;

  //the frame, the blank frame, and temporary images to flip the frame
  private Mat frame, blank, tmp1, tmp2;

  //logging tag
  private static final String TAG = "FrameGrabber";

  private boolean resultReady = false;

  //objects to run and store the result
  private ImageProcessor imageProcessor = null;
  private ImageProcessorResult result = null;

  //timing variables
  private long totalTime = 0, loopCount = 0, loopTimer = 0;

  public boolean isSaveImages() {
    return saveImages;
  }

  public ImageProcessor getImageProcessor() {
    return imageProcessor;
  }

  public FrameGrabberMode getMode() {
    return mode;
  }

  public void setSaveImages(boolean saveImages) {
    this.saveImages = saveImages;
  }

  public void setImageProcessor(ImageProcessor imageProcessor) {
    this.imageProcessor = imageProcessor;
  }

  public FrameGrabber(CameraBridgeViewBase cameraBridgeViewBase, int frameWidthRequest, int frameHeightRequest) {
    cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);

    cameraBridgeViewBase.setMinimumWidth(frameWidthRequest);
    cameraBridgeViewBase.setMinimumHeight(frameHeightRequest);
    cameraBridgeViewBase.setMaxFrameSize(frameWidthRequest, frameHeightRequest);
    cameraBridgeViewBase.setCvCameraViewListener(this);
  }

  private boolean isImageProcessorNull(){
    if(imageProcessor == null) {
      Log.e(TAG, "imageProcessor is null! Call setImageProcessor() to set it.");
      return true;
    } else {
      return false;
    }
  }

  public void grabSingleFrame(){
    if(isImageProcessorNull()) return;
    mode = FrameGrabberMode.SINGLE;
    resultReady = false;
  }

  public void throwAwayFrames(){
    mode = FrameGrabberMode.THROWAWAY;
    resultReady = false;
  }

  public void stopFrameGrabber(){
    mode = FrameGrabberMode.STOPPED;
    totalTime = 0;
    loopCount = 0;
    loopTimer = 0;
  }

  //getter for the result
  public boolean isResultReady(){
    return resultReady;
  }
  public ImageProcessorResult getResult(){
    return result;
  }

  private void processFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
    if(imageProcessor == null){
      return;
    }
    //start the loop timer
    if(mode == FrameGrabberMode.SINGLE){
      loopTimer = System.nanoTime();
    }
    long frameTime = System.currentTimeMillis();

    //get the rgb alpha image
    tmp1 = inputFrame.rgba();
    ImageUtil.rotate(tmp1, frame, 90);

    //process the image using the provided imageProcessor
    result = imageProcessor.process(frameTime, frame, saveImages); //process
    frame = result.getFrame(); //get the output frame
    Log.i(TAG, "Result: " + result);

    Log.i(TAG, "frame size: " + frame.size());

    Core.transpose(frame, tmp1);
    Imgproc.resize(tmp1, tmp2, tmp2.size(), 0, 0, 0);
    Core.transpose(tmp2, frame);

    //Loop timer
    long now = System.nanoTime();
    long loopTime = now - loopTimer;
    if(loopTimer > 0) {
      loopCount++;
      totalTime += loopTime;
      Log.i(TAG, "LOOP #" + loopCount);
      Log.i(TAG, "LOOP TIME: " + loopTime / 1000000.0 + " ms");
      Log.i(TAG, "AVERAGE LOOP TIME: " + totalTime / 1000000.0 / loopCount + " ms");
      Log.i(TAG, "ESTIMATED AVERAGE FPS: " + 1000.0 * 1000000.0 * loopCount / totalTime);
    }
    loopTimer = now;
  }

  @Override
  public void onCameraViewStarted(int width, int height) {
    //create the frame and tmp images
    frame = new Mat(height, width, CvType.CV_8UC4, new Scalar(0,0,0));
    blank = new Mat(height, width, CvType.CV_8UC4, new Scalar(0,0,0));
    tmp1 = new Mat(height, width, CvType.CV_8UC4);
    tmp2 = new Mat(width, height, CvType.CV_8UC4);
  }

  @Override
  public void onCameraViewStopped() {

  }

  @Override
  public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
    if(mode == FrameGrabberMode.SINGLE){ //if a single frame was requested
      processFrame(inputFrame); //process it
      stopFrameGrabber(); //and stop grabbing
      resultReady = true;
    }
    else if(mode == FrameGrabberMode.THROWAWAY) { //if throwing away frames
      return blank;
    }
    else if(mode == FrameGrabberMode.STOPPED) { //if stopped
      //wait for a frame request from the main program
      //in the meantime hang to avoid grabbing extra frames and wasting battery
      resultReady = true;
      while (mode == FrameGrabberMode.STOPPED) {
        try {
          Thread.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      resultReady = false;
    } else {
      stopFrameGrabber(); //paranoia
    }
    return frame; //this is displayed on the screen
  }
}