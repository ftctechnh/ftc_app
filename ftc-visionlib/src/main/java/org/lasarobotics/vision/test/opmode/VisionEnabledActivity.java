package org.lasarobotics.vision.test.opmode;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.jetbrains.annotations.Nullable;
import org.lasarobotics.vision.test.android.Camera;
import org.lasarobotics.vision.test.android.Cameras;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Size;

/**
 * Initiates a VisionEnabledActivity
 */
public abstract class VisionEnabledActivity extends Activity {
    public static CameraBridgeViewBase openCVCamera;

    protected BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Woohoo!

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    /**
     * Initialize vision
     * Call this method from the onCreate() method after the super()
     *
     * @param layoutID  The ID of the primary layout (e.g. R.id.layout_robotcontroller)
     * @param camera    The camera to use in vision processing
     * @param frameSize The target frame size. If null, it will get the optimal size.
     */
    protected final void initializeVision(int layoutID, Cameras camera, @Nullable Size frameSize) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        if (frameSize == null) {
            Camera c = camera.createCamera();
            if (c.doesExist()) {
                frameSize = c.getBestFrameSize();
                c.unlock();
                c.release();
            } else {
                //get some kind of default
                frameSize = new Size(900, 900);
            }
        }

        layout.setLayoutParams(new LinearLayout.LayoutParams(
                (int) frameSize.width, (int) frameSize.height));

        JavaCameraView cameraView = new JavaCameraView(this, camera.getID());
        cameraView.setVisibility(View.VISIBLE);

        layout.addView(cameraView);
        layout.setVisibility(View.VISIBLE);

        openCVCamera = cameraView;

        LinearLayout primaryLayout = (LinearLayout) this.findViewById(layoutID);
        primaryLayout.addView(layout);
    }

    /**
     * Initialize vision
     * Call this method from the onCreate() method after the super()
     *
     * @param layoutID The ID of the primary layout (e.g. R.id.layout_robotcontroller)
     * @param camera   The camera to use in vision processing
     */
    protected final void initializeVision(int layoutID, Cameras camera) {
        initializeVision(layoutID, camera, null);
    }

    /**
     * Initialize vision
     * Call this method from the onCreate() method after the super()
     *
     * @param layoutID The ID of the primary layout (e.g. R.id.layout_robotcontroller)
     */
    protected final void initializeVision(int layoutID) {
        initializeVision(layoutID, Cameras.PRIMARY);
    }

    /*protected final void initializeVision(int framePreview) {
        openCVCamera = (CameraBridgeViewBase) findViewById(framePreview);
        openCVCamera.setVisibility(SurfaceView.VISIBLE);
    }*/

    public void onDestroy() {
        super.onDestroy();
        if (openCVCamera != null)
            openCVCamera.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}
