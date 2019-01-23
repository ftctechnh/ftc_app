//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import org.opencv.ml.ANN_MLP;

// C++: class ANN_MLP_ANNEAL
//javadoc: ANN_MLP_ANNEAL

public class ANN_MLP_ANNEAL extends ANN_MLP {

    protected ANN_MLP_ANNEAL(long addr) { super(addr); }

    // internal usage only
    public static ANN_MLP_ANNEAL __fromPtr__(long addr) { return new ANN_MLP_ANNEAL(addr); }

    //
    // C++:  double cv::ml::ANN_MLP_ANNEAL::getAnnealCoolingRatio()
    //

    //javadoc: ANN_MLP_ANNEAL::getAnnealCoolingRatio()
    public  double getAnnealCoolingRatio()
    {
        
        double retVal = getAnnealCoolingRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP_ANNEAL::getAnnealFinalT()
    //

    //javadoc: ANN_MLP_ANNEAL::getAnnealFinalT()
    public  double getAnnealFinalT()
    {
        
        double retVal = getAnnealFinalT_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP_ANNEAL::getAnnealInitialT()
    //

    //javadoc: ANN_MLP_ANNEAL::getAnnealInitialT()
    public  double getAnnealInitialT()
    {
        
        double retVal = getAnnealInitialT_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::ANN_MLP_ANNEAL::getAnnealItePerStep()
    //

    //javadoc: ANN_MLP_ANNEAL::getAnnealItePerStep()
    public  int getAnnealItePerStep()
    {
        
        int retVal = getAnnealItePerStep_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealCoolingRatio(double val)
    //

    //javadoc: ANN_MLP_ANNEAL::setAnnealCoolingRatio(val)
    public  void setAnnealCoolingRatio(double val)
    {
        
        setAnnealCoolingRatio_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealFinalT(double val)
    //

    //javadoc: ANN_MLP_ANNEAL::setAnnealFinalT(val)
    public  void setAnnealFinalT(double val)
    {
        
        setAnnealFinalT_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealInitialT(double val)
    //

    //javadoc: ANN_MLP_ANNEAL::setAnnealInitialT(val)
    public  void setAnnealInitialT(double val)
    {
        
        setAnnealInitialT_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealItePerStep(int val)
    //

    //javadoc: ANN_MLP_ANNEAL::setAnnealItePerStep(val)
    public  void setAnnealItePerStep(int val)
    {
        
        setAnnealItePerStep_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  double cv::ml::ANN_MLP_ANNEAL::getAnnealCoolingRatio()
    private static native double getAnnealCoolingRatio_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP_ANNEAL::getAnnealFinalT()
    private static native double getAnnealFinalT_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP_ANNEAL::getAnnealInitialT()
    private static native double getAnnealInitialT_0(long nativeObj);

    // C++:  int cv::ml::ANN_MLP_ANNEAL::getAnnealItePerStep()
    private static native int getAnnealItePerStep_0(long nativeObj);

    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealCoolingRatio(double val)
    private static native void setAnnealCoolingRatio_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealFinalT(double val)
    private static native void setAnnealFinalT_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealInitialT(double val)
    private static native void setAnnealInitialT_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP_ANNEAL::setAnnealItePerStep(int val)
    private static native void setAnnealItePerStep_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
