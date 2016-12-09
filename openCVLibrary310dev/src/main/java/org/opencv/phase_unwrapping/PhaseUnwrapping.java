
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.phase_unwrapping;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;

// C++: class PhaseUnwrapping
//javadoc: PhaseUnwrapping
public class PhaseUnwrapping extends Algorithm {

    protected PhaseUnwrapping(long addr) { super(addr); }


    //
    // C++:  void unwrapPhaseMap(Mat wrappedPhaseMap, Mat& unwrappedPhaseMap, Mat shadowMask = Mat())
    //

    //javadoc: PhaseUnwrapping::unwrapPhaseMap(wrappedPhaseMap, unwrappedPhaseMap, shadowMask)
    public  void unwrapPhaseMap(Mat wrappedPhaseMap, Mat unwrappedPhaseMap, Mat shadowMask)
    {
        
        unwrapPhaseMap_0(nativeObj, wrappedPhaseMap.nativeObj, unwrappedPhaseMap.nativeObj, shadowMask.nativeObj);
        
        return;
    }

    //javadoc: PhaseUnwrapping::unwrapPhaseMap(wrappedPhaseMap, unwrappedPhaseMap)
    public  void unwrapPhaseMap(Mat wrappedPhaseMap, Mat unwrappedPhaseMap)
    {
        
        unwrapPhaseMap_1(nativeObj, wrappedPhaseMap.nativeObj, unwrappedPhaseMap.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void unwrapPhaseMap(Mat wrappedPhaseMap, Mat& unwrappedPhaseMap, Mat shadowMask = Mat())
    private static native void unwrapPhaseMap_0(long nativeObj, long wrappedPhaseMap_nativeObj, long unwrappedPhaseMap_nativeObj, long shadowMask_nativeObj);
    private static native void unwrapPhaseMap_1(long nativeObj, long wrappedPhaseMap_nativeObj, long unwrappedPhaseMap_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
