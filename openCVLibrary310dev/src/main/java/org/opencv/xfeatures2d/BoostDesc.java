
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class BoostDesc
//javadoc: BoostDesc
public class BoostDesc extends Feature2D {

    protected BoostDesc(long addr) { super(addr); }


    //
    // C++: static Ptr_BoostDesc create(int desc = BoostDesc::BINBOOST_256, bool use_scale_orientation = true, float scale_factor = 6.25f)
    //

    //javadoc: BoostDesc::create(desc, use_scale_orientation, scale_factor)
    public static BoostDesc create(int desc, boolean use_scale_orientation, float scale_factor)
    {
        
        BoostDesc retVal = new BoostDesc(create_0(desc, use_scale_orientation, scale_factor));
        
        return retVal;
    }

    //javadoc: BoostDesc::create()
    public static BoostDesc create()
    {
        
        BoostDesc retVal = new BoostDesc(create_1());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_BoostDesc create(int desc = BoostDesc::BINBOOST_256, bool use_scale_orientation = true, float scale_factor = 6.25f)
    private static native long create_0(int desc, boolean use_scale_orientation, float scale_factor);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
