
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class StarDetector
//javadoc: StarDetector
public class StarDetector extends Feature2D {

    protected StarDetector(long addr) { super(addr); }


    //
    // C++: static Ptr_StarDetector create(int maxSize = 45, int responseThreshold = 30, int lineThresholdProjected = 10, int lineThresholdBinarized = 8, int suppressNonmaxSize = 5)
    //

    //javadoc: StarDetector::create(maxSize, responseThreshold, lineThresholdProjected, lineThresholdBinarized, suppressNonmaxSize)
    public static StarDetector create(int maxSize, int responseThreshold, int lineThresholdProjected, int lineThresholdBinarized, int suppressNonmaxSize)
    {
        
        StarDetector retVal = new StarDetector(create_0(maxSize, responseThreshold, lineThresholdProjected, lineThresholdBinarized, suppressNonmaxSize));
        
        return retVal;
    }

    //javadoc: StarDetector::create()
    public static StarDetector create()
    {
        
        StarDetector retVal = new StarDetector(create_1());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_StarDetector create(int maxSize = 45, int responseThreshold = 30, int lineThresholdProjected = 10, int lineThresholdBinarized = 8, int suppressNonmaxSize = 5)
    private static native long create_0(int maxSize, int responseThreshold, int lineThresholdProjected, int lineThresholdBinarized, int suppressNonmaxSize);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
