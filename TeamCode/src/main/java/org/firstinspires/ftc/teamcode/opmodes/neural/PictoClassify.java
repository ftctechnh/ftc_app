package org.firstinspires.ftc.teamcode.opmodes.neural;

/**
 * Created by Noah on 2/9/2018.
 * Simple pictogram classifier based on a neural netowrk build in Keras
 * Designed to be called on asynchronously, since NN calculations are expensive
 * Both OpenCV and Tensorflow must be initialized to use any of these functions!
 */

public class PictoClassify {
    int[] cropSize;
    int[] scaleSize;

    public PictoClassify(String modelFPath, int[] cropSize, int[] scaleSize) {
        this.cropSize = cropSize;
        this.scaleSize = scaleSize;
        //load model file from disk
        
    }

}
