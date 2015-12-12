
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgproc;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.Size;

// C++: class CLAHE
//javadoc: CLAHE
public class CLAHE extends Algorithm {

    protected CLAHE(long addr) { super(addr); }


    //
    // C++:  void setClipLimit(double clipLimit)
    //

    //javadoc: CLAHE::setClipLimit(clipLimit)
    public  void setClipLimit(double clipLimit)
    {
        
        setClipLimit_0(nativeObj, clipLimit);
        
        return;
    }


    //
    // C++:  void apply(Mat src, Mat& dst)
    //

    //javadoc: CLAHE::apply(src, dst)
    public  void apply(Mat src, Mat dst)
    {
        
        apply_0(nativeObj, src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  double getClipLimit()
    //

    //javadoc: CLAHE::getClipLimit()
    public  double getClipLimit()
    {
        
        double retVal = getClipLimit_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setTilesGridSize(Size tileGridSize)
    //

    //javadoc: CLAHE::setTilesGridSize(tileGridSize)
    public  void setTilesGridSize(Size tileGridSize)
    {
        
        setTilesGridSize_0(nativeObj, tileGridSize.width, tileGridSize.height);
        
        return;
    }


    //
    // C++:  void collectGarbage()
    //

    //javadoc: CLAHE::collectGarbage()
    public  void collectGarbage()
    {
        
        collectGarbage_0(nativeObj);
        
        return;
    }


    //
    // C++:  Size getTilesGridSize()
    //

    //javadoc: CLAHE::getTilesGridSize()
    public  Size getTilesGridSize()
    {
        
        Size retVal = new Size(getTilesGridSize_0(nativeObj));
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void setClipLimit(double clipLimit)
    private static native void setClipLimit_0(long nativeObj, double clipLimit);

    // C++:  void apply(Mat src, Mat& dst)
    private static native void apply_0(long nativeObj, long src_nativeObj, long dst_nativeObj);

    // C++:  double getClipLimit()
    private static native double getClipLimit_0(long nativeObj);

    // C++:  void setTilesGridSize(Size tileGridSize)
    private static native void setTilesGridSize_0(long nativeObj, double tileGridSize_width, double tileGridSize_height);

    // C++:  void collectGarbage()
    private static native void collectGarbage_0(long nativeObj);

    // C++:  Size getTilesGridSize()
    private static native double[] getTilesGridSize_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
