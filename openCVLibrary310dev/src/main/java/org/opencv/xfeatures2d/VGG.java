
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Feature2D;

// C++: class VGG
//javadoc: VGG
public class VGG extends Feature2D {

    protected VGG(long addr) { super(addr); }


    //
    // C++: static Ptr_VGG create(int desc = VGG::VGG_120, float isigma = 1.4f, bool img_normalize = true, bool use_scale_orientation = true, float scale_factor = 6.25f, bool dsc_normalize = false)
    //

    //javadoc: VGG::create(desc, isigma, img_normalize, use_scale_orientation, scale_factor, dsc_normalize)
    public static VGG create(int desc, float isigma, boolean img_normalize, boolean use_scale_orientation, float scale_factor, boolean dsc_normalize)
    {
        
        VGG retVal = new VGG(create_0(desc, isigma, img_normalize, use_scale_orientation, scale_factor, dsc_normalize));
        
        return retVal;
    }

    //javadoc: VGG::create()
    public static VGG create()
    {
        
        VGG retVal = new VGG(create_1());
        
        return retVal;
    }


    //
    // C++:  void compute(Mat image, vector_KeyPoint keypoints, Mat& descriptors)
    //

    //javadoc: VGG::compute(image, keypoints, descriptors)
    public  void compute(Mat image, MatOfKeyPoint keypoints, Mat descriptors)
    {
        Mat keypoints_mat = keypoints;
        compute_0(nativeObj, image.nativeObj, keypoints_mat.nativeObj, descriptors.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_VGG create(int desc = VGG::VGG_120, float isigma = 1.4f, bool img_normalize = true, bool use_scale_orientation = true, float scale_factor = 6.25f, bool dsc_normalize = false)
    private static native long create_0(int desc, float isigma, boolean img_normalize, boolean use_scale_orientation, float scale_factor, boolean dsc_normalize);
    private static native long create_1();

    // C++:  void compute(Mat image, vector_KeyPoint keypoints, Mat& descriptors)
    private static native void compute_0(long nativeObj, long image_nativeObj, long keypoints_mat_nativeObj, long descriptors_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
