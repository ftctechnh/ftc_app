package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.ByteArrayOutputStream;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class OpModeCamera extends OpMode {

  public Camera camera;
  public CameraPreview preview;

  public int width;
  public int height;
  public YuvImage yuvImage = null;

  volatile private boolean imageReady = false;

  private int looped = 0;
  private String data;
  private int ds = 1; // downsampling parameter

  /*
 * Code to run when the op mode is first enabled goes here
 */
  @Override
  public void init() {
    startCamera();
  }

  /*
   * This method will be called repeatedly in a loop
   * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
   */
  @Override
  public void loop() {

  }

  @Override
  public void stop() {
    stopCamera();
  }

  public Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
    public void onPreviewFrame(byte[] data, Camera camera) {
      try {
        Camera.Parameters parameters = camera.getParameters();
        width = parameters.getPreviewSize().width;
        height = parameters.getPreviewSize().height;
        yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        imageReady = true;
        looped += 1;
      } catch (Exception e) {

      }
    }
  };

  public void setCameraDownsampling(int downSampling) {
    ds = downSampling;
  }

  public boolean imageReady() {
    return imageReady;
  }

  public boolean isCameraAvailable() {
    int cameraId = -1;
    Camera cam = null;
    int numberOfCameras = Camera.getNumberOfCameras();
    for (int i = 0; i < numberOfCameras; i++) {
      Camera.CameraInfo info = new Camera.CameraInfo();
      Camera.getCameraInfo(i, info);
      if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) { // Camera.CameraInfo.CAMERA_FACING_FRONT or BACK
        cameraId = i;
        break;
      }
    }
    try {
      cam = Camera.open(cameraId);
    } catch (Exception e) {
      Log.e("Error", "Camera Not Available!");
      return false;
    }
    if (cam != null) {
      cam.release();
    }
    cam = null;
    return true;
  }

  public Camera openCamera(int cameraInfoType) {
    int cameraId = -1;
    Camera cam = null;
    int numberOfCameras = Camera.getNumberOfCameras();
    for (int i = 0; i < numberOfCameras; i++) {
      Camera.CameraInfo info = new Camera.CameraInfo();
      Camera.getCameraInfo(i, info);
      if (info.facing == cameraInfoType) { // Camera.CameraInfo.CAMERA_FACING_FRONT or BACK
        cameraId = i;
        break;
      }
    }
    try {
      cam = Camera.open(cameraId);
    } catch (Exception e) {
      Log.e("Error", "Can't Open Camera");
    }
    return cam;
  }

  public void startCamera() {
    camera = openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

    camera.setPreviewCallback(previewCallback);

    Camera.Parameters parameters = camera.getParameters();

    width = parameters.getPreviewSize().width / ds;
    height = parameters.getPreviewSize().height / ds;
    parameters.setPreviewSize(width, height);

    camera.setParameters(parameters);

    data = parameters.flatten();

    if (preview == null) {
      ((FtcRobotControllerActivity) hardwareMap.appContext).initPreview(camera, this, previewCallback);
    }
  }

  public void stopCamera() {
    if (camera != null) {
      if (preview != null) {
        ((FtcRobotControllerActivity) hardwareMap.appContext).removePreview(this);
        preview = null;
      }
      camera.stopPreview();
      camera.setPreviewCallback(null);
      if (camera != null) {
        camera.release();
      }
      camera = null;
    }
  }

  static public int red(int pixel) {
    return (pixel >> 16) & 0xff;
  }

  static public int green(int pixel) {
    return (pixel >> 8) & 0xff;
  }

  static public int blue(int pixel) {
    return pixel & 0xff;
  }

  static public int gray(int pixel) {
    return (red(pixel) + green(pixel) + blue(pixel));
  }

  static public int highestColor(int red, int green, int blue) {
    int[] color = {red, green, blue};
    int value = 0;
    for (int i = 1; i < 3; i++) {
      if (color[value] < color[i]) {
        value = i;
      }
    }
    return value;
  }

  // returns ROTATED image, to match preview window
  static public Bitmap convertYuvImageToRgb(YuvImage yuvImage, int width, int height, int downSample) {
    Bitmap rgbImage;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, out);
    byte[] imageBytes = out.toByteArray();

    BitmapFactory.Options opt;
    opt = new BitmapFactory.Options();
    opt.inSampleSize = downSample;

    // get image and rotate it so (0,0) is in the bottom left
    Bitmap tmpImage;
    Matrix matrix = new Matrix();
    matrix.postRotate(90); // to rotate the camera images so (0,0) is in the bottom left
    tmpImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, opt);
    rgbImage=Bitmap.createBitmap(tmpImage , 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), matrix, true);

    return rgbImage;
  }


}
