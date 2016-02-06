package org.lasarobotics.vision.test.image;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Implements image filtering algorithms
 */
public class Filter {
    public static void blur(Mat img, int amount) {
        Imgproc.GaussianBlur(img, img, new Size(2 * amount + 1, 2 * amount + 1), 0, 0);
    }

    public static void erode(Mat img, int amount) {
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(2 * amount + 1, 2 * amount + 1),
                new Point(amount, amount));
        Imgproc.erode(img, img, kernel);
    }

    public static void dilate(Mat img, int amount) {
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,
                new Size(2 * amount + 1, 2 * amount + 1),
                new Point(amount, amount));
        Imgproc.dilate(img, img, kernel);
    }

    /**
     * Downsample and blur an image (using a Gaussian pyramid kernel)
     *
     * @param img   The image
     * @param scale The scale, a number greater than 1
     */
    public static void downsample(Mat img, double scale) {
        Imgproc.pyrDown(img, img, new Size((double) img.width() / scale, (double) img.height() / scale));
    }

    /**
     * Upsample and blur an image (using a Gaussian pyramid kernel)
     *
     * @param img   The image
     * @param scale The scale, a number greater than 1
     */
    public static void upsample(Mat img, double scale) {
        Imgproc.pyrUp(img, img, new Size((double) img.width() * scale, (double) img.height() * scale));
    }
}
