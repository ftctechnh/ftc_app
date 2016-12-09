
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.optflow;

import java.lang.String;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public class Optflow {

    public static final int
            GPC_DESCRIPTOR_DCT = 0,
            GPC_DESCRIPTOR_WHT = 0+1;


    //
    // C++:  Mat readOpticalFlow(String path)
    //

    //javadoc: readOpticalFlow(path)
    public static Mat readOpticalFlow(String path)
    {
        
        Mat retVal = new Mat(readOpticalFlow_0(path));
        
        return retVal;
    }


    //
    // C++:  Ptr_DISOpticalFlow createOptFlow_DIS(int preset = DISOpticalFlow::PRESET_FAST)
    //

    //javadoc: createOptFlow_DIS(preset)
    public static DISOpticalFlow createOptFlow_DIS(int preset)
    {
        
        DISOpticalFlow retVal = new DISOpticalFlow(createOptFlow_DIS_0(preset));
        
        return retVal;
    }

    //javadoc: createOptFlow_DIS()
    public static DISOpticalFlow createOptFlow_DIS()
    {
        
        DISOpticalFlow retVal = new DISOpticalFlow(createOptFlow_DIS_1());
        
        return retVal;
    }


    //
    // C++:  Ptr_DenseOpticalFlow createOptFlow_DeepFlow()
    //

    // Return type 'Ptr_DenseOpticalFlow' is not supported, skipping the function


    //
    // C++:  Ptr_DenseOpticalFlow createOptFlow_Farneback()
    //

    // Return type 'Ptr_DenseOpticalFlow' is not supported, skipping the function


    //
    // C++:  Ptr_DenseOpticalFlow createOptFlow_PCAFlow()
    //

    // Return type 'Ptr_DenseOpticalFlow' is not supported, skipping the function


    //
    // C++:  Ptr_DenseOpticalFlow createOptFlow_SimpleFlow()
    //

    // Return type 'Ptr_DenseOpticalFlow' is not supported, skipping the function


    //
    // C++:  Ptr_DenseOpticalFlow createOptFlow_SparseToDense()
    //

    // Return type 'Ptr_DenseOpticalFlow' is not supported, skipping the function


    //
    // C++:  Ptr_VariationalRefinement createVariationalFlowRefinement()
    //

    //javadoc: createVariationalFlowRefinement()
    public static VariationalRefinement createVariationalFlowRefinement()
    {
        
        VariationalRefinement retVal = new VariationalRefinement(createVariationalFlowRefinement_0());
        
        return retVal;
    }


    //
    // C++:  bool writeOpticalFlow(String path, Mat flow)
    //

    //javadoc: writeOpticalFlow(path, flow)
    public static boolean writeOpticalFlow(String path, Mat flow)
    {
        
        boolean retVal = writeOpticalFlow_0(path, flow.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double calcGlobalOrientation(Mat orientation, Mat mask, Mat mhi, double timestamp, double duration)
    //

    //javadoc: calcGlobalOrientation(orientation, mask, mhi, timestamp, duration)
    public static double calcGlobalOrientation(Mat orientation, Mat mask, Mat mhi, double timestamp, double duration)
    {
        
        double retVal = calcGlobalOrientation_0(orientation.nativeObj, mask.nativeObj, mhi.nativeObj, timestamp, duration);
        
        return retVal;
    }


    //
    // C++:  void calcMotionGradient(Mat mhi, Mat& mask, Mat& orientation, double delta1, double delta2, int apertureSize = 3)
    //

    //javadoc: calcMotionGradient(mhi, mask, orientation, delta1, delta2, apertureSize)
    public static void calcMotionGradient(Mat mhi, Mat mask, Mat orientation, double delta1, double delta2, int apertureSize)
    {
        
        calcMotionGradient_0(mhi.nativeObj, mask.nativeObj, orientation.nativeObj, delta1, delta2, apertureSize);
        
        return;
    }

    //javadoc: calcMotionGradient(mhi, mask, orientation, delta1, delta2)
    public static void calcMotionGradient(Mat mhi, Mat mask, Mat orientation, double delta1, double delta2)
    {
        
        calcMotionGradient_1(mhi.nativeObj, mask.nativeObj, orientation.nativeObj, delta1, delta2);
        
        return;
    }


    //
    // C++:  void segmentMotion(Mat mhi, Mat& segmask, vector_Rect& boundingRects, double timestamp, double segThresh)
    //

    //javadoc: segmentMotion(mhi, segmask, boundingRects, timestamp, segThresh)
    public static void segmentMotion(Mat mhi, Mat segmask, MatOfRect boundingRects, double timestamp, double segThresh)
    {
        Mat boundingRects_mat = boundingRects;
        segmentMotion_0(mhi.nativeObj, segmask.nativeObj, boundingRects_mat.nativeObj, timestamp, segThresh);
        
        return;
    }


    //
    // C++:  void updateMotionHistory(Mat silhouette, Mat& mhi, double timestamp, double duration)
    //

    //javadoc: updateMotionHistory(silhouette, mhi, timestamp, duration)
    public static void updateMotionHistory(Mat silhouette, Mat mhi, double timestamp, double duration)
    {
        
        updateMotionHistory_0(silhouette.nativeObj, mhi.nativeObj, timestamp, duration);
        
        return;
    }


    //
    // C++:  void calcOpticalFlowSF(Mat from, Mat to, Mat& flow, int layers, int averaging_block_size, int max_flow, double sigma_dist, double sigma_color, int postprocess_window, double sigma_dist_fix, double sigma_color_fix, double occ_thr, int upscale_averaging_radius, double upscale_sigma_dist, double upscale_sigma_color, double speed_up_thr)
    //

    //javadoc: calcOpticalFlowSF(from, to, flow, layers, averaging_block_size, max_flow, sigma_dist, sigma_color, postprocess_window, sigma_dist_fix, sigma_color_fix, occ_thr, upscale_averaging_radius, upscale_sigma_dist, upscale_sigma_color, speed_up_thr)
    public static void calcOpticalFlowSF(Mat from, Mat to, Mat flow, int layers, int averaging_block_size, int max_flow, double sigma_dist, double sigma_color, int postprocess_window, double sigma_dist_fix, double sigma_color_fix, double occ_thr, int upscale_averaging_radius, double upscale_sigma_dist, double upscale_sigma_color, double speed_up_thr)
    {
        
        calcOpticalFlowSF_0(from.nativeObj, to.nativeObj, flow.nativeObj, layers, averaging_block_size, max_flow, sigma_dist, sigma_color, postprocess_window, sigma_dist_fix, sigma_color_fix, occ_thr, upscale_averaging_radius, upscale_sigma_dist, upscale_sigma_color, speed_up_thr);
        
        return;
    }


    //
    // C++:  void calcOpticalFlowSF(Mat from, Mat to, Mat& flow, int layers, int averaging_block_size, int max_flow)
    //

    //javadoc: calcOpticalFlowSF(from, to, flow, layers, averaging_block_size, max_flow)
    public static void calcOpticalFlowSF(Mat from, Mat to, Mat flow, int layers, int averaging_block_size, int max_flow)
    {
        
        calcOpticalFlowSF_1(from.nativeObj, to.nativeObj, flow.nativeObj, layers, averaging_block_size, max_flow);
        
        return;
    }


    //
    // C++:  void calcOpticalFlowSparseToDense(Mat from, Mat to, Mat& flow, int grid_step = 8, int k = 128, float sigma = 0.05f, bool use_post_proc = true, float fgs_lambda = 500.0f, float fgs_sigma = 1.5f)
    //

    //javadoc: calcOpticalFlowSparseToDense(from, to, flow, grid_step, k, sigma, use_post_proc, fgs_lambda, fgs_sigma)
    public static void calcOpticalFlowSparseToDense(Mat from, Mat to, Mat flow, int grid_step, int k, float sigma, boolean use_post_proc, float fgs_lambda, float fgs_sigma)
    {
        
        calcOpticalFlowSparseToDense_0(from.nativeObj, to.nativeObj, flow.nativeObj, grid_step, k, sigma, use_post_proc, fgs_lambda, fgs_sigma);
        
        return;
    }

    //javadoc: calcOpticalFlowSparseToDense(from, to, flow)
    public static void calcOpticalFlowSparseToDense(Mat from, Mat to, Mat flow)
    {
        
        calcOpticalFlowSparseToDense_1(from.nativeObj, to.nativeObj, flow.nativeObj);
        
        return;
    }




    // C++:  Mat readOpticalFlow(String path)
    private static native long readOpticalFlow_0(String path);

    // C++:  Ptr_DISOpticalFlow createOptFlow_DIS(int preset = DISOpticalFlow::PRESET_FAST)
    private static native long createOptFlow_DIS_0(int preset);
    private static native long createOptFlow_DIS_1();

    // C++:  Ptr_VariationalRefinement createVariationalFlowRefinement()
    private static native long createVariationalFlowRefinement_0();

    // C++:  bool writeOpticalFlow(String path, Mat flow)
    private static native boolean writeOpticalFlow_0(String path, long flow_nativeObj);

    // C++:  double calcGlobalOrientation(Mat orientation, Mat mask, Mat mhi, double timestamp, double duration)
    private static native double calcGlobalOrientation_0(long orientation_nativeObj, long mask_nativeObj, long mhi_nativeObj, double timestamp, double duration);

    // C++:  void calcMotionGradient(Mat mhi, Mat& mask, Mat& orientation, double delta1, double delta2, int apertureSize = 3)
    private static native void calcMotionGradient_0(long mhi_nativeObj, long mask_nativeObj, long orientation_nativeObj, double delta1, double delta2, int apertureSize);
    private static native void calcMotionGradient_1(long mhi_nativeObj, long mask_nativeObj, long orientation_nativeObj, double delta1, double delta2);

    // C++:  void segmentMotion(Mat mhi, Mat& segmask, vector_Rect& boundingRects, double timestamp, double segThresh)
    private static native void segmentMotion_0(long mhi_nativeObj, long segmask_nativeObj, long boundingRects_mat_nativeObj, double timestamp, double segThresh);

    // C++:  void updateMotionHistory(Mat silhouette, Mat& mhi, double timestamp, double duration)
    private static native void updateMotionHistory_0(long silhouette_nativeObj, long mhi_nativeObj, double timestamp, double duration);

    // C++:  void calcOpticalFlowSF(Mat from, Mat to, Mat& flow, int layers, int averaging_block_size, int max_flow, double sigma_dist, double sigma_color, int postprocess_window, double sigma_dist_fix, double sigma_color_fix, double occ_thr, int upscale_averaging_radius, double upscale_sigma_dist, double upscale_sigma_color, double speed_up_thr)
    private static native void calcOpticalFlowSF_0(long from_nativeObj, long to_nativeObj, long flow_nativeObj, int layers, int averaging_block_size, int max_flow, double sigma_dist, double sigma_color, int postprocess_window, double sigma_dist_fix, double sigma_color_fix, double occ_thr, int upscale_averaging_radius, double upscale_sigma_dist, double upscale_sigma_color, double speed_up_thr);

    // C++:  void calcOpticalFlowSF(Mat from, Mat to, Mat& flow, int layers, int averaging_block_size, int max_flow)
    private static native void calcOpticalFlowSF_1(long from_nativeObj, long to_nativeObj, long flow_nativeObj, int layers, int averaging_block_size, int max_flow);

    // C++:  void calcOpticalFlowSparseToDense(Mat from, Mat to, Mat& flow, int grid_step = 8, int k = 128, float sigma = 0.05f, bool use_post_proc = true, float fgs_lambda = 500.0f, float fgs_sigma = 1.5f)
    private static native void calcOpticalFlowSparseToDense_0(long from_nativeObj, long to_nativeObj, long flow_nativeObj, int grid_step, int k, float sigma, boolean use_post_proc, float fgs_lambda, float fgs_sigma);
    private static native void calcOpticalFlowSparseToDense_1(long from_nativeObj, long to_nativeObj, long flow_nativeObj);

}
