
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class SIFT
//javadoc: SIFT
public class SIFT extends Feature2D {

    protected SIFT(long addr) { super(addr); }


    //
    // C++: static Ptr_SIFT create(int nfeatures = 0, int nOctaveLayers = 3, double contrastThreshold = 0.04, double edgeThreshold = 10, double sigma = 1.6)
    //

    //javadoc: SIFT::create(nfeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma)
    public static SIFT create(int nfeatures, int nOctaveLayers, double contrastThreshold, double edgeThreshold, double sigma)
    {
        
        SIFT retVal = new SIFT(create_0(nfeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma));
        
        return retVal;
    }

    //javadoc: SIFT::create()
    public static SIFT create()
    {
        
        SIFT retVal = new SIFT(create_1());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_SIFT create(int nfeatures = 0, int nOctaveLayers = 3, double contrastThreshold = 0.04, double edgeThreshold = 10, double sigma = 1.6)
    private static native long create_0(int nfeatures, int nOctaveLayers, double contrastThreshold, double edgeThreshold, double sigma);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
