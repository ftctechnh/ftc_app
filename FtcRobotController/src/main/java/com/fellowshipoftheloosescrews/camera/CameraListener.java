package com.fellowshipoftheloosescrews.camera;

/**
 * Created by Thomas on 7/20/2015.
 *
 * A basic interface for receiving the images from the camera
 */
public interface CameraListener {
    void imageCallback(Image image);
}
