package com.fellowshipoftheloosescrews.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Thomas on 7/20/2015.
 *
 * This lets us use the camera in the background for the robot
 */
public class ThomasCamera {
    private Camera camera;
    private SurfaceTexture surfaceTexture;

    private jpegCallback currentCallback = null;

    private Bitmap bitmap;

    private static String logTag = "ThomasCamera";

    private ArrayList<CameraListener> listenerArrayList;

    public enum CameraStates
    {
        CAMERA_NOT_READY,
        CAMERA_READY,
        CAMERA_CAPTURING
    }

    private CameraStates state;

    public ThomasCamera() {
        state = CameraStates.CAMERA_NOT_READY;
        listenerArrayList = new ArrayList();
    }

    public void init()
    {
        if(state != CameraStates.CAMERA_NOT_READY)
        {
            Log.e(logTag, "ERROR: Camera in not in expected state");
            return;
        }

        camera = getCameraInstance(0);

        Camera.Parameters parameters = camera.getParameters();

        ArrayList<Camera.Size> sizes = (ArrayList<Camera.Size>)parameters.getSupportedPictureSizes();
        for(Camera.Size size : sizes)
        {
            Log.d(logTag, "index: " + sizes.indexOf(size) + " width " + size.width + " height " + size.height);
        }

        int sizeIndex = 0;
        parameters.setPictureSize(sizes.get(sizeIndex).width, sizes.get(sizeIndex).height);

        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);
        //TODO: add auto focus if not implemented
        camera.setParameters(parameters);

        surfaceTexture = new SurfaceTexture(10);
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            Log.e(logTag, "ERROR: Couldn't bind preview texture");
            e.printStackTrace();
        }
        camera.startPreview();
        state = CameraStates.CAMERA_READY;
    }

    public void capture()
    {
        if(state != CameraStates.CAMERA_READY)
        {
            Log.e(logTag, "ERROR: Camera was not ready to capture");
            return;
        }
        //Log.d("Camera", "Capture call");
        state = CameraStates.CAMERA_CAPTURING;
        currentCallback = new jpegCallback();
        camera.takePicture(null, null, currentCallback);
        //Log.d(logTag, "Trace");
        //while(state != CameraStates.CAMERA_READY){Log.d(logTag, String.valueOf(state));}
    }

    public void release()
    {
        camera.stopPreview();
        camera.release();
        state = CameraStates.CAMERA_NOT_READY;
    }

    private static Camera getCameraInstance(int cameraNumber)
    {
        Camera c = Camera.open(cameraNumber);
        if (c == null)
        {
            Log.e(logTag, "ERROR: Camera is null!");
        }
        return c;
    }

    private class jpegCallback implements Camera.PictureCallback
    {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //Log.d(logTag, "Picture captured");

            if(data == null) {
                Log.e(logTag, "data is null");
                return;
            }

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            Image image = new Image(bitmap);

            camera.startPreview();

            notifyListeners(image);
            state = CameraStates.CAMERA_READY;
        }
    }

    public void addListener(CameraListener listener)
    {
        listenerArrayList.add(listener);
    }

    private void notifyListeners(Image image)
    {
        for(CameraListener listener : listenerArrayList)
        {
            listener.imageCallback(image);
        }
    }

    public CameraStates getState()
    {
        return state;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
}
