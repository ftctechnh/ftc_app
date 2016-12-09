
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.structured_light;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

// C++: class StructuredLightPattern
//javadoc: StructuredLightPattern
public class StructuredLightPattern extends Algorithm {

    protected StructuredLightPattern(long addr) { super(addr); }


    //
    // C++:  bool decode(vector_Mat patternImages, Mat& disparityMap, vector_Mat blackImages = vector_Mat(), vector_Mat whiteImages = vector_Mat(), int flags = DECODE_3D_UNDERWORLD)
    //

    //javadoc: StructuredLightPattern::decode(patternImages, disparityMap, blackImages, whiteImages, flags)
    public  boolean decode(List<Mat> patternImages, Mat disparityMap, List<Mat> blackImages, List<Mat> whiteImages, int flags)
    {
        Mat patternImages_mat = Converters.vector_Mat_to_Mat(patternImages);
        Mat blackImages_mat = Converters.vector_Mat_to_Mat(blackImages);
        Mat whiteImages_mat = Converters.vector_Mat_to_Mat(whiteImages);
        boolean retVal = decode_0(nativeObj, patternImages_mat.nativeObj, disparityMap.nativeObj, blackImages_mat.nativeObj, whiteImages_mat.nativeObj, flags);
        
        return retVal;
    }

    //javadoc: StructuredLightPattern::decode(patternImages, disparityMap)
    public  boolean decode(List<Mat> patternImages, Mat disparityMap)
    {
        Mat patternImages_mat = Converters.vector_Mat_to_Mat(patternImages);
        boolean retVal = decode_1(nativeObj, patternImages_mat.nativeObj, disparityMap.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool generate(vector_Mat& patternImages)
    //

    //javadoc: StructuredLightPattern::generate(patternImages)
    public  boolean generate(List<Mat> patternImages)
    {
        Mat patternImages_mat = new Mat();
        boolean retVal = generate_0(nativeObj, patternImages_mat.nativeObj);
        Converters.Mat_to_vector_Mat(patternImages_mat, patternImages);
        patternImages_mat.release();
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool decode(vector_Mat patternImages, Mat& disparityMap, vector_Mat blackImages = vector_Mat(), vector_Mat whiteImages = vector_Mat(), int flags = DECODE_3D_UNDERWORLD)
    private static native boolean decode_0(long nativeObj, long patternImages_mat_nativeObj, long disparityMap_nativeObj, long blackImages_mat_nativeObj, long whiteImages_mat_nativeObj, int flags);
    private static native boolean decode_1(long nativeObj, long patternImages_mat_nativeObj, long disparityMap_nativeObj);

    // C++:  bool generate(vector_Mat& patternImages)
    private static native boolean generate_0(long nativeObj, long patternImages_mat_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
