
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class LUCID
//javadoc: LUCID
public class LUCID extends Feature2D {

    protected LUCID(long addr) { super(addr); }


    //
    // C++: static Ptr_LUCID create(int lucid_kernel, int blur_kernel)
    //

    //javadoc: LUCID::create(lucid_kernel, blur_kernel)
    public static LUCID create(int lucid_kernel, int blur_kernel)
    {
        
        LUCID retVal = new LUCID(create_0(lucid_kernel, blur_kernel));
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_LUCID create(int lucid_kernel, int blur_kernel)
    private static native long create_0(int lucid_kernel, int blur_kernel);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
