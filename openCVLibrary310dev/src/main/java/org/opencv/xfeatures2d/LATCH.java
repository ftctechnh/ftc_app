
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class LATCH
//javadoc: LATCH
public class LATCH extends Feature2D {

    protected LATCH(long addr) { super(addr); }


    //
    // C++: static Ptr_LATCH create(int bytes = 32, bool rotationInvariance = true, int half_ssd_size = 3)
    //

    //javadoc: LATCH::create(bytes, rotationInvariance, half_ssd_size)
    public static LATCH create(int bytes, boolean rotationInvariance, int half_ssd_size)
    {
        
        LATCH retVal = new LATCH(create_0(bytes, rotationInvariance, half_ssd_size));
        
        return retVal;
    }

    //javadoc: LATCH::create()
    public static LATCH create()
    {
        
        LATCH retVal = new LATCH(create_1());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_LATCH create(int bytes = 32, bool rotationInvariance = true, int half_ssd_size = 3)
    private static native long create_0(int bytes, boolean rotationInvariance, int half_ssd_size);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
