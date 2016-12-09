
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.structured_light;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.utils.Converters;

// C++: class SinusoidalPattern
//javadoc: SinusoidalPattern
public class SinusoidalPattern extends StructuredLightPattern {

    protected SinusoidalPattern(long addr) { super(addr); }


    //
    // C++:  void computeDataModulationTerm(vector_Mat patternImages, Mat& dataModulationTerm, Mat shadowMask)
    //

    //javadoc: SinusoidalPattern::computeDataModulationTerm(patternImages, dataModulationTerm, shadowMask)
    public  void computeDataModulationTerm(List<Mat> patternImages, Mat dataModulationTerm, Mat shadowMask)
    {
        Mat patternImages_mat = Converters.vector_Mat_to_Mat(patternImages);
        computeDataModulationTerm_0(nativeObj, patternImages_mat.nativeObj, dataModulationTerm.nativeObj, shadowMask.nativeObj);
        
        return;
    }


    //
    // C++:  void computePhaseMap(vector_Mat patternImages, Mat& wrappedPhaseMap, Mat& shadowMask = Mat(), Mat fundamental = Mat())
    //

    //javadoc: SinusoidalPattern::computePhaseMap(patternImages, wrappedPhaseMap, shadowMask, fundamental)
    public  void computePhaseMap(List<Mat> patternImages, Mat wrappedPhaseMap, Mat shadowMask, Mat fundamental)
    {
        Mat patternImages_mat = Converters.vector_Mat_to_Mat(patternImages);
        computePhaseMap_0(nativeObj, patternImages_mat.nativeObj, wrappedPhaseMap.nativeObj, shadowMask.nativeObj, fundamental.nativeObj);
        
        return;
    }

    //javadoc: SinusoidalPattern::computePhaseMap(patternImages, wrappedPhaseMap)
    public  void computePhaseMap(List<Mat> patternImages, Mat wrappedPhaseMap)
    {
        Mat patternImages_mat = Converters.vector_Mat_to_Mat(patternImages);
        computePhaseMap_1(nativeObj, patternImages_mat.nativeObj, wrappedPhaseMap.nativeObj);
        
        return;
    }


    //
    // C++:  void findProCamMatches(Mat projUnwrappedPhaseMap, Mat camUnwrappedPhaseMap, vector_Mat& matches)
    //

    //javadoc: SinusoidalPattern::findProCamMatches(projUnwrappedPhaseMap, camUnwrappedPhaseMap, matches)
    public  void findProCamMatches(Mat projUnwrappedPhaseMap, Mat camUnwrappedPhaseMap, List<Mat> matches)
    {
        Mat matches_mat = new Mat();
        findProCamMatches_0(nativeObj, projUnwrappedPhaseMap.nativeObj, camUnwrappedPhaseMap.nativeObj, matches_mat.nativeObj);
        Converters.Mat_to_vector_Mat(matches_mat, matches);
        matches_mat.release();
        return;
    }


    //
    // C++:  void unwrapPhaseMap(vector_Mat wrappedPhaseMap, Mat& unwrappedPhaseMap, Size camSize, Mat shadowMask = Mat())
    //

    //javadoc: SinusoidalPattern::unwrapPhaseMap(wrappedPhaseMap, unwrappedPhaseMap, camSize, shadowMask)
    public  void unwrapPhaseMap(List<Mat> wrappedPhaseMap, Mat unwrappedPhaseMap, Size camSize, Mat shadowMask)
    {
        Mat wrappedPhaseMap_mat = Converters.vector_Mat_to_Mat(wrappedPhaseMap);
        unwrapPhaseMap_0(nativeObj, wrappedPhaseMap_mat.nativeObj, unwrappedPhaseMap.nativeObj, camSize.width, camSize.height, shadowMask.nativeObj);
        
        return;
    }

    //javadoc: SinusoidalPattern::unwrapPhaseMap(wrappedPhaseMap, unwrappedPhaseMap, camSize)
    public  void unwrapPhaseMap(List<Mat> wrappedPhaseMap, Mat unwrappedPhaseMap, Size camSize)
    {
        Mat wrappedPhaseMap_mat = Converters.vector_Mat_to_Mat(wrappedPhaseMap);
        unwrapPhaseMap_1(nativeObj, wrappedPhaseMap_mat.nativeObj, unwrappedPhaseMap.nativeObj, camSize.width, camSize.height);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  void computeDataModulationTerm(vector_Mat patternImages, Mat& dataModulationTerm, Mat shadowMask)
    private static native void computeDataModulationTerm_0(long nativeObj, long patternImages_mat_nativeObj, long dataModulationTerm_nativeObj, long shadowMask_nativeObj);

    // C++:  void computePhaseMap(vector_Mat patternImages, Mat& wrappedPhaseMap, Mat& shadowMask = Mat(), Mat fundamental = Mat())
    private static native void computePhaseMap_0(long nativeObj, long patternImages_mat_nativeObj, long wrappedPhaseMap_nativeObj, long shadowMask_nativeObj, long fundamental_nativeObj);
    private static native void computePhaseMap_1(long nativeObj, long patternImages_mat_nativeObj, long wrappedPhaseMap_nativeObj);

    // C++:  void findProCamMatches(Mat projUnwrappedPhaseMap, Mat camUnwrappedPhaseMap, vector_Mat& matches)
    private static native void findProCamMatches_0(long nativeObj, long projUnwrappedPhaseMap_nativeObj, long camUnwrappedPhaseMap_nativeObj, long matches_mat_nativeObj);

    // C++:  void unwrapPhaseMap(vector_Mat wrappedPhaseMap, Mat& unwrappedPhaseMap, Size camSize, Mat shadowMask = Mat())
    private static native void unwrapPhaseMap_0(long nativeObj, long wrappedPhaseMap_mat_nativeObj, long unwrappedPhaseMap_nativeObj, double camSize_width, double camSize_height, long shadowMask_nativeObj);
    private static native void unwrapPhaseMap_1(long nativeObj, long wrappedPhaseMap_mat_nativeObj, long unwrappedPhaseMap_nativeObj, double camSize_width, double camSize_height);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
