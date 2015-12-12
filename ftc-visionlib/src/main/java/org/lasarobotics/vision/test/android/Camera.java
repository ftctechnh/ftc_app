package org.lasarobotics.vision.test.android;

import android.content.pm.PackageManager;

import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a single Android camera
 */
@SuppressWarnings("deprecation")
public class Camera {
    android.hardware.Camera c;
    int id;

    public Camera(Cameras camera) {
        makeCamera(camera.getID());
    }

    public static boolean isHardwareAvailable() {
        return Util.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static int getCameraCount() {
        return android.hardware.Camera.getNumberOfCameras();
    }

    public android.hardware.Camera getCamera() {
        return c;
    }

    private void makeCamera(int id) {
        this.id = id;
        try {
            this.c = android.hardware.Camera.open(id);
        } catch (Exception e) {
            this.c = null;
        }
    }

    public void unlock() {
        c.unlock();
    }

    public void lock() {
        c.lock();
    }

    public void release() {
        c.release();
    }

    public int getID() {
        return id;
    }

    public boolean doesExist() {
        return (c != null);
    }

    public Size getLargestFrameSize() {
        android.hardware.Camera.Size s = c.getParameters().getPreviewSize();
        return new Size(s.width, s.height);
    }

    public Size getBestFrameSize() {
        android.hardware.Camera.Size s = c.getParameters().getPreferredPreviewSizeForVideo();
        return new Size(s.width, s.height);
    }

    public List<Size> getAllFrameSizes() {
        List<Size> l = new ArrayList<>();
        for (android.hardware.Camera.Size s : c.getParameters().getSupportedPreviewSizes())
            l.add(new Size(s.width, s.height));
        return l;
    }
}
