
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.utils.Converters;

// C++: class PCTSignaturesSQFD
//javadoc: PCTSignaturesSQFD
public class PCTSignaturesSQFD extends Algorithm {

    protected PCTSignaturesSQFD(long addr) { super(addr); }


    //
    // C++: static Ptr_PCTSignaturesSQFD create(int distanceFunction = 3, int similarityFunction = 2, float similarityParameter = 1.0f)
    //

    //javadoc: PCTSignaturesSQFD::create(distanceFunction, similarityFunction, similarityParameter)
    public static PCTSignaturesSQFD create(int distanceFunction, int similarityFunction, float similarityParameter)
    {
        
        PCTSignaturesSQFD retVal = new PCTSignaturesSQFD(create_0(distanceFunction, similarityFunction, similarityParameter));
        
        return retVal;
    }

    //javadoc: PCTSignaturesSQFD::create()
    public static PCTSignaturesSQFD create()
    {
        
        PCTSignaturesSQFD retVal = new PCTSignaturesSQFD(create_1());
        
        return retVal;
    }


    //
    // C++:  float computeQuadraticFormDistance(Mat _signature0, Mat _signature1)
    //

    //javadoc: PCTSignaturesSQFD::computeQuadraticFormDistance(_signature0, _signature1)
    public  float computeQuadraticFormDistance(Mat _signature0, Mat _signature1)
    {
        
        float retVal = computeQuadraticFormDistance_0(nativeObj, _signature0.nativeObj, _signature1.nativeObj);
        
        return retVal;
    }


    //
    // C++:  void computeQuadraticFormDistances(Mat sourceSignature, vector_Mat imageSignatures, vector_float distances)
    //

    //javadoc: PCTSignaturesSQFD::computeQuadraticFormDistances(sourceSignature, imageSignatures, distances)
    public  void computeQuadraticFormDistances(Mat sourceSignature, List<Mat> imageSignatures, MatOfFloat distances)
    {
        Mat imageSignatures_mat = Converters.vector_Mat_to_Mat(imageSignatures);
        Mat distances_mat = distances;
        computeQuadraticFormDistances_0(nativeObj, sourceSignature.nativeObj, imageSignatures_mat.nativeObj, distances_mat.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_PCTSignaturesSQFD create(int distanceFunction = 3, int similarityFunction = 2, float similarityParameter = 1.0f)
    private static native long create_0(int distanceFunction, int similarityFunction, float similarityParameter);
    private static native long create_1();

    // C++:  float computeQuadraticFormDistance(Mat _signature0, Mat _signature1)
    private static native float computeQuadraticFormDistance_0(long nativeObj, long _signature0_nativeObj, long _signature1_nativeObj);

    // C++:  void computeQuadraticFormDistances(Mat sourceSignature, vector_Mat imageSignatures, vector_float distances)
    private static native void computeQuadraticFormDistances_0(long nativeObj, long sourceSignature_nativeObj, long imageSignatures_mat_nativeObj, long distances_mat_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
