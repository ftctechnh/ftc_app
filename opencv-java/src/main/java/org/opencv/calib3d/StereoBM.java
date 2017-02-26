
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.calib3d;

import org.opencv.core.Rect;

// C++: class StereoBM
//javadoc: StereoBM
public class StereoBM extends StereoMatcher {

    protected StereoBM(long addr) { super(addr); }


    public static final int
            PREFILTER_NORMALIZED_RESPONSE = 0,
            PREFILTER_XSOBEL = 1;


    //
    // C++:  int getPreFilterType()
    //

    //javadoc: StereoBM::getPreFilterType()
    public  int getPreFilterType()
    {
        
        int retVal = getPreFilterType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setPreFilterType(int preFilterType)
    //

    //javadoc: StereoBM::setPreFilterType(preFilterType)
    public  void setPreFilterType(int preFilterType)
    {
        
        setPreFilterType_0(nativeObj, preFilterType);
        
        return;
    }


    //
    // C++:  int getPreFilterSize()
    //

    //javadoc: StereoBM::getPreFilterSize()
    public  int getPreFilterSize()
    {
        
        int retVal = getPreFilterSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setPreFilterSize(int preFilterSize)
    //

    //javadoc: StereoBM::setPreFilterSize(preFilterSize)
    public  void setPreFilterSize(int preFilterSize)
    {
        
        setPreFilterSize_0(nativeObj, preFilterSize);
        
        return;
    }


    //
    // C++:  int getPreFilterCap()
    //

    //javadoc: StereoBM::getPreFilterCap()
    public  int getPreFilterCap()
    {
        
        int retVal = getPreFilterCap_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setPreFilterCap(int preFilterCap)
    //

    //javadoc: StereoBM::setPreFilterCap(preFilterCap)
    public  void setPreFilterCap(int preFilterCap)
    {
        
        setPreFilterCap_0(nativeObj, preFilterCap);
        
        return;
    }


    //
    // C++:  int getTextureThreshold()
    //

    //javadoc: StereoBM::getTextureThreshold()
    public  int getTextureThreshold()
    {
        
        int retVal = getTextureThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setTextureThreshold(int textureThreshold)
    //

    //javadoc: StereoBM::setTextureThreshold(textureThreshold)
    public  void setTextureThreshold(int textureThreshold)
    {
        
        setTextureThreshold_0(nativeObj, textureThreshold);
        
        return;
    }


    //
    // C++:  int getUniquenessRatio()
    //

    //javadoc: StereoBM::getUniquenessRatio()
    public  int getUniquenessRatio()
    {
        
        int retVal = getUniquenessRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setUniquenessRatio(int uniquenessRatio)
    //

    //javadoc: StereoBM::setUniquenessRatio(uniquenessRatio)
    public  void setUniquenessRatio(int uniquenessRatio)
    {
        
        setUniquenessRatio_0(nativeObj, uniquenessRatio);
        
        return;
    }


    //
    // C++:  int getSmallerBlockSize()
    //

    //javadoc: StereoBM::getSmallerBlockSize()
    public  int getSmallerBlockSize()
    {
        
        int retVal = getSmallerBlockSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setSmallerBlockSize(int blockSize)
    //

    //javadoc: StereoBM::setSmallerBlockSize(blockSize)
    public  void setSmallerBlockSize(int blockSize)
    {
        
        setSmallerBlockSize_0(nativeObj, blockSize);
        
        return;
    }


    //
    // C++:  Rect getROI1()
    //

    //javadoc: StereoBM::getROI1()
    public  Rect getROI1()
    {
        
        Rect retVal = new Rect(getROI1_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  void setROI1(Rect roi1)
    //

    //javadoc: StereoBM::setROI1(roi1)
    public  void setROI1(Rect roi1)
    {
        
        setROI1_0(nativeObj, roi1.x, roi1.y, roi1.width, roi1.height);
        
        return;
    }


    //
    // C++:  Rect getROI2()
    //

    //javadoc: StereoBM::getROI2()
    public  Rect getROI2()
    {
        
        Rect retVal = new Rect(getROI2_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  void setROI2(Rect roi2)
    //

    //javadoc: StereoBM::setROI2(roi2)
    public  void setROI2(Rect roi2)
    {
        
        setROI2_0(nativeObj, roi2.x, roi2.y, roi2.width, roi2.height);
        
        return;
    }


    //
    // C++: static Ptr_StereoBM create(int numDisparities = 0, int blockSize = 21)
    //

    //javadoc: StereoBM::create(numDisparities, blockSize)
    public static StereoBM create(int numDisparities, int blockSize)
    {
        
        StereoBM retVal = new StereoBM(create_0(numDisparities, blockSize));
        
        return retVal;
    }

    //javadoc: StereoBM::create()
    public static StereoBM create()
    {
        
        StereoBM retVal = new StereoBM(create_1());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  int getPreFilterType()
    private static native int getPreFilterType_0(long nativeObj);

    // C++:  void setPreFilterType(int preFilterType)
    private static native void setPreFilterType_0(long nativeObj, int preFilterType);

    // C++:  int getPreFilterSize()
    private static native int getPreFilterSize_0(long nativeObj);

    // C++:  void setPreFilterSize(int preFilterSize)
    private static native void setPreFilterSize_0(long nativeObj, int preFilterSize);

    // C++:  int getPreFilterCap()
    private static native int getPreFilterCap_0(long nativeObj);

    // C++:  void setPreFilterCap(int preFilterCap)
    private static native void setPreFilterCap_0(long nativeObj, int preFilterCap);

    // C++:  int getTextureThreshold()
    private static native int getTextureThreshold_0(long nativeObj);

    // C++:  void setTextureThreshold(int textureThreshold)
    private static native void setTextureThreshold_0(long nativeObj, int textureThreshold);

    // C++:  int getUniquenessRatio()
    private static native int getUniquenessRatio_0(long nativeObj);

    // C++:  void setUniquenessRatio(int uniquenessRatio)
    private static native void setUniquenessRatio_0(long nativeObj, int uniquenessRatio);

    // C++:  int getSmallerBlockSize()
    private static native int getSmallerBlockSize_0(long nativeObj);

    // C++:  void setSmallerBlockSize(int blockSize)
    private static native void setSmallerBlockSize_0(long nativeObj, int blockSize);

    // C++:  Rect getROI1()
    private static native double[] getROI1_0(long nativeObj);

    // C++:  void setROI1(Rect roi1)
    private static native void setROI1_0(long nativeObj, int roi1_x, int roi1_y, int roi1_width, int roi1_height);

    // C++:  Rect getROI2()
    private static native double[] getROI2_0(long nativeObj);

    // C++:  void setROI2(Rect roi2)
    private static native void setROI2_0(long nativeObj, int roi2_x, int roi2_y, int roi2_width, int roi2_height);

    // C++: static Ptr_StereoBM create(int numDisparities = 0, int blockSize = 21)
    private static native long create_0(int numDisparities, int blockSize);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
