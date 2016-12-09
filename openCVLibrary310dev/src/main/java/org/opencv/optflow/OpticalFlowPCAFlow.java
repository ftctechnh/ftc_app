
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;


import org.opencv.video.DenseOpticalFlow;

// C++: class OpticalFlowPCAFlow
//javadoc: OpticalFlowPCAFlow
public class OpticalFlowPCAFlow extends DenseOpticalFlow {

    protected OpticalFlowPCAFlow(long addr) { super(addr); }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // native support for java finalize()
    private static native void delete(long nativeObj);

}
