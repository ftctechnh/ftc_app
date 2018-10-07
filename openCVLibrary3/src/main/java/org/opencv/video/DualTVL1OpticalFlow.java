//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.DenseOpticalFlow;
import org.opencv.video.DualTVL1OpticalFlow;

// C++: class DualTVL1OpticalFlow
//javadoc: DualTVL1OpticalFlow

public class DualTVL1OpticalFlow extends DenseOpticalFlow {

    protected DualTVL1OpticalFlow(long addr) { super(addr); }

    // internal usage only
    public static DualTVL1OpticalFlow __fromPtr__(long addr) { return new DualTVL1OpticalFlow(addr); }

    //
    // C++: static Ptr_DualTVL1OpticalFlow cv::DualTVL1OpticalFlow::create(double tau = 0.25, double lambda = 0.15, double theta = 0.3, int nscales = 5, int warps = 5, double epsilon = 0.01, int innnerIterations = 30, int outerIterations = 10, double scaleStep = 0.8, double gamma = 0.0, int medianFiltering = 5, bool useInitialFlow = false)
    //

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep, gamma, medianFiltering, useInitialFlow)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep, double gamma, int medianFiltering, boolean useInitialFlow)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_0(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep, gamma, medianFiltering, useInitialFlow));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep, gamma, medianFiltering)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep, double gamma, int medianFiltering)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_1(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep, gamma, medianFiltering));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep, gamma)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep, double gamma)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_2(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep, gamma));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_3(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations, scaleStep));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_4(tau, lambda, theta, nscales, warps, epsilon, innnerIterations, outerIterations));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon, innnerIterations)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_5(tau, lambda, theta, nscales, warps, epsilon, innnerIterations));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps, epsilon)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps, double epsilon)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_6(tau, lambda, theta, nscales, warps, epsilon));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales, warps)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales, int warps)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_7(tau, lambda, theta, nscales, warps));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta, nscales)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta, int nscales)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_8(tau, lambda, theta, nscales));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda, theta)
    public static DualTVL1OpticalFlow create(double tau, double lambda, double theta)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_9(tau, lambda, theta));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau, lambda)
    public static DualTVL1OpticalFlow create(double tau, double lambda)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_10(tau, lambda));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create(tau)
    public static DualTVL1OpticalFlow create(double tau)
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_11(tau));
        
        return retVal;
    }

    //javadoc: DualTVL1OpticalFlow::create()
    public static DualTVL1OpticalFlow create()
    {
        
        DualTVL1OpticalFlow retVal = DualTVL1OpticalFlow.__fromPtr__(create_12());
        
        return retVal;
    }


    //
    // C++:  bool cv::DualTVL1OpticalFlow::getUseInitialFlow()
    //

    //javadoc: DualTVL1OpticalFlow::getUseInitialFlow()
    public  boolean getUseInitialFlow()
    {
        
        boolean retVal = getUseInitialFlow_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::DualTVL1OpticalFlow::getEpsilon()
    //

    //javadoc: DualTVL1OpticalFlow::getEpsilon()
    public  double getEpsilon()
    {
        
        double retVal = getEpsilon_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::DualTVL1OpticalFlow::getGamma()
    //

    //javadoc: DualTVL1OpticalFlow::getGamma()
    public  double getGamma()
    {
        
        double retVal = getGamma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::DualTVL1OpticalFlow::getLambda()
    //

    //javadoc: DualTVL1OpticalFlow::getLambda()
    public  double getLambda()
    {
        
        double retVal = getLambda_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::DualTVL1OpticalFlow::getScaleStep()
    //

    //javadoc: DualTVL1OpticalFlow::getScaleStep()
    public  double getScaleStep()
    {
        
        double retVal = getScaleStep_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::DualTVL1OpticalFlow::getTau()
    //

    //javadoc: DualTVL1OpticalFlow::getTau()
    public  double getTau()
    {
        
        double retVal = getTau_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::DualTVL1OpticalFlow::getTheta()
    //

    //javadoc: DualTVL1OpticalFlow::getTheta()
    public  double getTheta()
    {
        
        double retVal = getTheta_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DualTVL1OpticalFlow::getInnerIterations()
    //

    //javadoc: DualTVL1OpticalFlow::getInnerIterations()
    public  int getInnerIterations()
    {
        
        int retVal = getInnerIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DualTVL1OpticalFlow::getMedianFiltering()
    //

    //javadoc: DualTVL1OpticalFlow::getMedianFiltering()
    public  int getMedianFiltering()
    {
        
        int retVal = getMedianFiltering_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DualTVL1OpticalFlow::getOuterIterations()
    //

    //javadoc: DualTVL1OpticalFlow::getOuterIterations()
    public  int getOuterIterations()
    {
        
        int retVal = getOuterIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DualTVL1OpticalFlow::getScalesNumber()
    //

    //javadoc: DualTVL1OpticalFlow::getScalesNumber()
    public  int getScalesNumber()
    {
        
        int retVal = getScalesNumber_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DualTVL1OpticalFlow::getWarpingsNumber()
    //

    //javadoc: DualTVL1OpticalFlow::getWarpingsNumber()
    public  int getWarpingsNumber()
    {
        
        int retVal = getWarpingsNumber_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setEpsilon(double val)
    //

    //javadoc: DualTVL1OpticalFlow::setEpsilon(val)
    public  void setEpsilon(double val)
    {
        
        setEpsilon_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setGamma(double val)
    //

    //javadoc: DualTVL1OpticalFlow::setGamma(val)
    public  void setGamma(double val)
    {
        
        setGamma_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setInnerIterations(int val)
    //

    //javadoc: DualTVL1OpticalFlow::setInnerIterations(val)
    public  void setInnerIterations(int val)
    {
        
        setInnerIterations_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setLambda(double val)
    //

    //javadoc: DualTVL1OpticalFlow::setLambda(val)
    public  void setLambda(double val)
    {
        
        setLambda_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setMedianFiltering(int val)
    //

    //javadoc: DualTVL1OpticalFlow::setMedianFiltering(val)
    public  void setMedianFiltering(int val)
    {
        
        setMedianFiltering_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setOuterIterations(int val)
    //

    //javadoc: DualTVL1OpticalFlow::setOuterIterations(val)
    public  void setOuterIterations(int val)
    {
        
        setOuterIterations_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setScaleStep(double val)
    //

    //javadoc: DualTVL1OpticalFlow::setScaleStep(val)
    public  void setScaleStep(double val)
    {
        
        setScaleStep_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setScalesNumber(int val)
    //

    //javadoc: DualTVL1OpticalFlow::setScalesNumber(val)
    public  void setScalesNumber(int val)
    {
        
        setScalesNumber_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setTau(double val)
    //

    //javadoc: DualTVL1OpticalFlow::setTau(val)
    public  void setTau(double val)
    {
        
        setTau_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setTheta(double val)
    //

    //javadoc: DualTVL1OpticalFlow::setTheta(val)
    public  void setTheta(double val)
    {
        
        setTheta_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setUseInitialFlow(bool val)
    //

    //javadoc: DualTVL1OpticalFlow::setUseInitialFlow(val)
    public  void setUseInitialFlow(boolean val)
    {
        
        setUseInitialFlow_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DualTVL1OpticalFlow::setWarpingsNumber(int val)
    //

    //javadoc: DualTVL1OpticalFlow::setWarpingsNumber(val)
    public  void setWarpingsNumber(int val)
    {
        
        setWarpingsNumber_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_DualTVL1OpticalFlow cv::DualTVL1OpticalFlow::create(double tau = 0.25, double lambda = 0.15, double theta = 0.3, int nscales = 5, int warps = 5, double epsilon = 0.01, int innnerIterations = 30, int outerIterations = 10, double scaleStep = 0.8, double gamma = 0.0, int medianFiltering = 5, bool useInitialFlow = false)
    private static native long create_0(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep, double gamma, int medianFiltering, boolean useInitialFlow);
    private static native long create_1(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep, double gamma, int medianFiltering);
    private static native long create_2(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep, double gamma);
    private static native long create_3(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations, double scaleStep);
    private static native long create_4(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations, int outerIterations);
    private static native long create_5(double tau, double lambda, double theta, int nscales, int warps, double epsilon, int innnerIterations);
    private static native long create_6(double tau, double lambda, double theta, int nscales, int warps, double epsilon);
    private static native long create_7(double tau, double lambda, double theta, int nscales, int warps);
    private static native long create_8(double tau, double lambda, double theta, int nscales);
    private static native long create_9(double tau, double lambda, double theta);
    private static native long create_10(double tau, double lambda);
    private static native long create_11(double tau);
    private static native long create_12();

    // C++:  bool cv::DualTVL1OpticalFlow::getUseInitialFlow()
    private static native boolean getUseInitialFlow_0(long nativeObj);

    // C++:  double cv::DualTVL1OpticalFlow::getEpsilon()
    private static native double getEpsilon_0(long nativeObj);

    // C++:  double cv::DualTVL1OpticalFlow::getGamma()
    private static native double getGamma_0(long nativeObj);

    // C++:  double cv::DualTVL1OpticalFlow::getLambda()
    private static native double getLambda_0(long nativeObj);

    // C++:  double cv::DualTVL1OpticalFlow::getScaleStep()
    private static native double getScaleStep_0(long nativeObj);

    // C++:  double cv::DualTVL1OpticalFlow::getTau()
    private static native double getTau_0(long nativeObj);

    // C++:  double cv::DualTVL1OpticalFlow::getTheta()
    private static native double getTheta_0(long nativeObj);

    // C++:  int cv::DualTVL1OpticalFlow::getInnerIterations()
    private static native int getInnerIterations_0(long nativeObj);

    // C++:  int cv::DualTVL1OpticalFlow::getMedianFiltering()
    private static native int getMedianFiltering_0(long nativeObj);

    // C++:  int cv::DualTVL1OpticalFlow::getOuterIterations()
    private static native int getOuterIterations_0(long nativeObj);

    // C++:  int cv::DualTVL1OpticalFlow::getScalesNumber()
    private static native int getScalesNumber_0(long nativeObj);

    // C++:  int cv::DualTVL1OpticalFlow::getWarpingsNumber()
    private static native int getWarpingsNumber_0(long nativeObj);

    // C++:  void cv::DualTVL1OpticalFlow::setEpsilon(double val)
    private static native void setEpsilon_0(long nativeObj, double val);

    // C++:  void cv::DualTVL1OpticalFlow::setGamma(double val)
    private static native void setGamma_0(long nativeObj, double val);

    // C++:  void cv::DualTVL1OpticalFlow::setInnerIterations(int val)
    private static native void setInnerIterations_0(long nativeObj, int val);

    // C++:  void cv::DualTVL1OpticalFlow::setLambda(double val)
    private static native void setLambda_0(long nativeObj, double val);

    // C++:  void cv::DualTVL1OpticalFlow::setMedianFiltering(int val)
    private static native void setMedianFiltering_0(long nativeObj, int val);

    // C++:  void cv::DualTVL1OpticalFlow::setOuterIterations(int val)
    private static native void setOuterIterations_0(long nativeObj, int val);

    // C++:  void cv::DualTVL1OpticalFlow::setScaleStep(double val)
    private static native void setScaleStep_0(long nativeObj, double val);

    // C++:  void cv::DualTVL1OpticalFlow::setScalesNumber(int val)
    private static native void setScalesNumber_0(long nativeObj, int val);

    // C++:  void cv::DualTVL1OpticalFlow::setTau(double val)
    private static native void setTau_0(long nativeObj, double val);

    // C++:  void cv::DualTVL1OpticalFlow::setTheta(double val)
    private static native void setTheta_0(long nativeObj, double val);

    // C++:  void cv::DualTVL1OpticalFlow::setUseInitialFlow(bool val)
    private static native void setUseInitialFlow_0(long nativeObj, boolean val);

    // C++:  void cv::DualTVL1OpticalFlow::setWarpingsNumber(int val)
    private static native void setWarpingsNumber_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
