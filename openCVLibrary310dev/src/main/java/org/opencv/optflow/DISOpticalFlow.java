
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;


import org.opencv.video.DenseOpticalFlow;

// C++: class DISOpticalFlow
//javadoc: DISOpticalFlow
public class DISOpticalFlow extends DenseOpticalFlow {

    protected DISOpticalFlow(long addr) { super(addr); }


    public static final int
            PRESET_ULTRAFAST = 0,
            PRESET_FAST = 1,
            PRESET_MEDIUM = 2;


    //
    // C++:  bool getUseMeanNormalization()
    //

    //javadoc: DISOpticalFlow::getUseMeanNormalization()
    public  boolean getUseMeanNormalization()
    {
        
        boolean retVal = getUseMeanNormalization_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool getUseSpatialPropagation()
    //

    //javadoc: DISOpticalFlow::getUseSpatialPropagation()
    public  boolean getUseSpatialPropagation()
    {
        
        boolean retVal = getUseSpatialPropagation_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float getVariationalRefinementAlpha()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementAlpha()
    public  float getVariationalRefinementAlpha()
    {
        
        float retVal = getVariationalRefinementAlpha_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float getVariationalRefinementDelta()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementDelta()
    public  float getVariationalRefinementDelta()
    {
        
        float retVal = getVariationalRefinementDelta_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float getVariationalRefinementGamma()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementGamma()
    public  float getVariationalRefinementGamma()
    {
        
        float retVal = getVariationalRefinementGamma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getFinestScale()
    //

    //javadoc: DISOpticalFlow::getFinestScale()
    public  int getFinestScale()
    {
        
        int retVal = getFinestScale_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getGradientDescentIterations()
    //

    //javadoc: DISOpticalFlow::getGradientDescentIterations()
    public  int getGradientDescentIterations()
    {
        
        int retVal = getGradientDescentIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getPatchSize()
    //

    //javadoc: DISOpticalFlow::getPatchSize()
    public  int getPatchSize()
    {
        
        int retVal = getPatchSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getPatchStride()
    //

    //javadoc: DISOpticalFlow::getPatchStride()
    public  int getPatchStride()
    {
        
        int retVal = getPatchStride_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int getVariationalRefinementIterations()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementIterations()
    public  int getVariationalRefinementIterations()
    {
        
        int retVal = getVariationalRefinementIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void setFinestScale(int val)
    //

    //javadoc: DISOpticalFlow::setFinestScale(val)
    public  void setFinestScale(int val)
    {
        
        setFinestScale_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setGradientDescentIterations(int val)
    //

    //javadoc: DISOpticalFlow::setGradientDescentIterations(val)
    public  void setGradientDescentIterations(int val)
    {
        
        setGradientDescentIterations_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setPatchSize(int val)
    //

    //javadoc: DISOpticalFlow::setPatchSize(val)
    public  void setPatchSize(int val)
    {
        
        setPatchSize_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setPatchStride(int val)
    //

    //javadoc: DISOpticalFlow::setPatchStride(val)
    public  void setPatchStride(int val)
    {
        
        setPatchStride_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setUseMeanNormalization(bool val)
    //

    //javadoc: DISOpticalFlow::setUseMeanNormalization(val)
    public  void setUseMeanNormalization(boolean val)
    {
        
        setUseMeanNormalization_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setUseSpatialPropagation(bool val)
    //

    //javadoc: DISOpticalFlow::setUseSpatialPropagation(val)
    public  void setUseSpatialPropagation(boolean val)
    {
        
        setUseSpatialPropagation_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setVariationalRefinementAlpha(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementAlpha(val)
    public  void setVariationalRefinementAlpha(float val)
    {
        
        setVariationalRefinementAlpha_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setVariationalRefinementDelta(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementDelta(val)
    public  void setVariationalRefinementDelta(float val)
    {
        
        setVariationalRefinementDelta_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setVariationalRefinementGamma(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementGamma(val)
    public  void setVariationalRefinementGamma(float val)
    {
        
        setVariationalRefinementGamma_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void setVariationalRefinementIterations(int val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementIterations(val)
    public  void setVariationalRefinementIterations(int val)
    {
        
        setVariationalRefinementIterations_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool getUseMeanNormalization()
    private static native boolean getUseMeanNormalization_0(long nativeObj);

    // C++:  bool getUseSpatialPropagation()
    private static native boolean getUseSpatialPropagation_0(long nativeObj);

    // C++:  float getVariationalRefinementAlpha()
    private static native float getVariationalRefinementAlpha_0(long nativeObj);

    // C++:  float getVariationalRefinementDelta()
    private static native float getVariationalRefinementDelta_0(long nativeObj);

    // C++:  float getVariationalRefinementGamma()
    private static native float getVariationalRefinementGamma_0(long nativeObj);

    // C++:  int getFinestScale()
    private static native int getFinestScale_0(long nativeObj);

    // C++:  int getGradientDescentIterations()
    private static native int getGradientDescentIterations_0(long nativeObj);

    // C++:  int getPatchSize()
    private static native int getPatchSize_0(long nativeObj);

    // C++:  int getPatchStride()
    private static native int getPatchStride_0(long nativeObj);

    // C++:  int getVariationalRefinementIterations()
    private static native int getVariationalRefinementIterations_0(long nativeObj);

    // C++:  void setFinestScale(int val)
    private static native void setFinestScale_0(long nativeObj, int val);

    // C++:  void setGradientDescentIterations(int val)
    private static native void setGradientDescentIterations_0(long nativeObj, int val);

    // C++:  void setPatchSize(int val)
    private static native void setPatchSize_0(long nativeObj, int val);

    // C++:  void setPatchStride(int val)
    private static native void setPatchStride_0(long nativeObj, int val);

    // C++:  void setUseMeanNormalization(bool val)
    private static native void setUseMeanNormalization_0(long nativeObj, boolean val);

    // C++:  void setUseSpatialPropagation(bool val)
    private static native void setUseSpatialPropagation_0(long nativeObj, boolean val);

    // C++:  void setVariationalRefinementAlpha(float val)
    private static native void setVariationalRefinementAlpha_0(long nativeObj, float val);

    // C++:  void setVariationalRefinementDelta(float val)
    private static native void setVariationalRefinementDelta_0(long nativeObj, float val);

    // C++:  void setVariationalRefinementGamma(float val)
    private static native void setVariationalRefinementGamma_0(long nativeObj, float val);

    // C++:  void setVariationalRefinementIterations(int val)
    private static native void setVariationalRefinementIterations_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
