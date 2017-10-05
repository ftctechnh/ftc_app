package ftc.vision;


import org.opencv.core.Mat;

/**
 * takes an image, creates a result, and modifies the image to show the result
 * This file was made by the electronVolts, FTC team 7393
 * Date Created: 8/17/16.
 */
public interface ImageProcessor<ResultType> {
    ImageProcessorResult<ResultType> process(long startTime, Mat rgbaFrame, boolean saveImages);
}
