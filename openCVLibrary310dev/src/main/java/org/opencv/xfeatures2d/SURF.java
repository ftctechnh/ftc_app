
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class SURF
//javadoc: SURF
public class SURF extends Feature2D {

    protected SURF(long addr) { super(addr); }


    //
    // C++: static Ptr_SURF create(double hessianThreshold = 100, int nOctaves = 4, int nOctaveLayers = 3, bool extended = false, bool upright = false)
    //

    //javadoc: SURF::create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright)
    public static SURF create(double hessianThreshold, int nOctaves, int nOctaveLayers, boolean extended, boolean upright)
    {
        
        SURF retVal = new SURF(create_0(hessianThreshold, nOctaves, nOctaveLayers, extended, upright));
        
        return retVal;
    }

    //javadoc: SURF::create()
    public static SURF create()
    {
        
        SURF retVal = new SURF(create_1());
        
        return retVal;
    }


    //
    // C++:  bool getExtended()
    //

    //javadoc: SURF::getExtended()
    public  boolean getExtended()
    {
        
        boolean retVal = getExtended_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool getUpright()
    //

    //javadoc: SURF::getUpright()
    public  boolean getUpright()
    {
        
        boolean retVal = getUpright_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double getHessianThreshold()
    //

    //javadoc: SURF::getHessianThreshold()
    public  double getHessianThreshold()
    {
        
        double retVal = getHessianThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getNOctaveLayers()
    //

    //javadoc: SURF::getNOctaveLayers()
    public  int getNOctaveLayers()
    {
        
        int retVal = getNOctaveLayers_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getNOctaves()
    //

    //javadoc: SURF::getNOctaves()
    public  int getNOctaves()
    {
        
        int retVal = getNOctaves_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setExtended(bool extended)
    //

    //javadoc: SURF::setExtended(extended)
    public  void setExtended(boolean extended)
    {
        
        setExtended_0(nativeObj, extended);
        
        return;
    }


    //
    // C++:  void setHessianThreshold(double hessianThreshold)
    //

    //javadoc: SURF::setHessianThreshold(hessianThreshold)
    public  void setHessianThreshold(double hessianThreshold)
    {
        
        setHessianThreshold_0(nativeObj, hessianThreshold);
        
        return;
    }


    //
    // C++:  void setNOctaveLayers(int nOctaveLayers)
    //

    //javadoc: SURF::setNOctaveLayers(nOctaveLayers)
    public  void setNOctaveLayers(int nOctaveLayers)
    {
        
        setNOctaveLayers_0(nativeObj, nOctaveLayers);
        
        return;
    }


    //
    // C++:  void setNOctaves(int nOctaves)
    //

    //javadoc: SURF::setNOctaves(nOctaves)
    public  void setNOctaves(int nOctaves)
    {
        
        setNOctaves_0(nativeObj, nOctaves);
        
        return;
    }


    //
    // C++:  void setUpright(bool upright)
    //

    //javadoc: SURF::setUpright(upright)
    public  void setUpright(boolean upright)
    {
        
        setUpright_0(nativeObj, upright);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_SURF create(double hessianThreshold = 100, int nOctaves = 4, int nOctaveLayers = 3, bool extended = false, bool upright = false)
    private static native long create_0(double hessianThreshold, int nOctaves, int nOctaveLayers, boolean extended, boolean upright);
    private static native long create_1();

    // C++:  bool getExtended()
    private static native boolean getExtended_0(long nativeObj);

    // C++:  bool getUpright()
    private static native boolean getUpright_0(long nativeObj);

    // C++:  double getHessianThreshold()
    private static native double getHessianThreshold_0(long nativeObj);

    // C++:  int getNOctaveLayers()
    private static native int getNOctaveLayers_0(long nativeObj);

    // C++:  int getNOctaves()
    private static native int getNOctaves_0(long nativeObj);

    // C++:  void setExtended(bool extended)
    private static native void setExtended_0(long nativeObj, boolean extended);

    // C++:  void setHessianThreshold(double hessianThreshold)
    private static native void setHessianThreshold_0(long nativeObj, double hessianThreshold);

    // C++:  void setNOctaveLayers(int nOctaveLayers)
    private static native void setNOctaveLayers_0(long nativeObj, int nOctaveLayers);

    // C++:  void setNOctaves(int nOctaves)
    private static native void setNOctaves_0(long nativeObj, int nOctaves);

    // C++:  void setUpright(bool upright)
    private static native void setUpright_0(long nativeObj, boolean upright);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
