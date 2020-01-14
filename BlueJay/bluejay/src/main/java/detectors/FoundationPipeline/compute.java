package detectors.FoundationPipeline;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class compute {

	static MatOfPoint2f toDouble(MatOfPoint p) {
		return new MatOfPoint2f(p.toArray());
	}

	/**
	 * draw rectangle border
	 */
	static void rectangle(Mat canvas, boolean fill) {
		Point topleftPoint, bottomrightPoint;

		// new point
		topleftPoint = new Point(0, 0);
		bottomrightPoint = new Point(canvas.width(), canvas.height());

		// rectangle
		Scalar cvRectangleColor = new Scalar(0.0, 0.0, 0.0, 0.0);
		int cvRectangleThickness;
		if (fill)
			cvRectangleThickness = -1;
		else
			cvRectangleThickness = 2;
		int cvRectangleLinetype = Core.BORDER_DEFAULT;
		int cvRectangleShift = 0;
		Imgproc.rectangle(canvas, topleftPoint, bottomrightPoint, cvRectangleColor, cvRectangleThickness,
				cvRectangleLinetype, cvRectangleShift);
	}

	/**
	 * Takes an RGB image and allplies thresholding based on HSV ranges
	 *
	 * @param input    An RGB image
	 * @param colRange Hue range
	 * @param satRange Saturation range
	 * @param valRange Value Range
	 * @return Thresholded image
	 */
	static Mat threshold(Mat input, double[] colRange, double[] satRange, double[] valRange) {
		Mat output = new Mat();

		Imgproc.cvtColor(input.clone(), output, Imgproc.COLOR_BGR2HSV);
		Core.inRange(output, new Scalar(colRange[0], satRange[0], valRange[0]),
				new Scalar(colRange[1], satRange[1], valRange[1]), output);

		return output;
	}

	static ArrayList<MatOfPoint> findHulls(Mat inp) {
		return findHulls(inp, false);
	}

	/**
	 * Returns a matrix of points representing the convex hulls of the blobs in the
	 * input
	 *
	 * @param inp a binary image
	 */
	static ArrayList<MatOfPoint> findHulls(Mat inp, boolean external) {
		// rectangle(inp,false);

		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		ArrayList<MatOfPoint> hullsOutput = new ArrayList<MatOfPoint>();

		findContours(inp, external, contours);

		convexHulls(contours, hullsOutput);

		return hullsOutput;
	}

	static void drawHulls(List<MatOfPoint> hulls, Mat drawOn) {
		Scalar color = new Scalar(0, 255, 0); // Green
		drawHulls(hulls, drawOn, color, 1);
	}

	/**
	 * Takes a point matrix and draws the shape it represents on the Mat input
	 *
	 * @param drawOn thing that gets drawn on
	 */
	static void drawHulls(List<MatOfPoint> hulls, Mat drawOn, Scalar color, int thick) {
		// draw convex hulls
		for (int i = 0; i < hulls.size(); i++) {
			Imgproc.drawContours(drawOn, hulls, i, color, thick);
		}
	}

	/**
	 * Finds the shapes of blobs
	 *
	 * @param externalOnly Whether to ignore shapes inside shapes
	 */
	static void findContours(Mat input, boolean externalOnly, List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		} else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);


	}

	static void floodFill(Mat canvas, Point origin) {
		double[] pixel = canvas.get((int) origin.y, (int) origin.x);
		if (pixel[0] == 0)
			return;

		Imgproc.floodFill(canvas, new Mat(canvas.height() + 2, canvas.width() + 2, canvas.type()), origin,
				new Scalar(0, 0, 0));
	}

	/**
	 * Filters out contours that do not meet certain criteria. Also removes contours
	 * surrounding everything
	 *
	 * @param inputContours is the input list of contours
	 * @return is the the output list of contours
	 * @param minArea is the minimum area of a contour that will be kept
	 */
	static List<MatOfPoint> filterContours(List<MatOfPoint> inputContours, double minArea) {
		List<MatOfPoint> output = new ArrayList<MatOfPoint>();
		// operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final double area = Imgproc.contourArea(contour);
			if (area < minArea)
				continue;
			Point tl = Imgproc.boundingRect(contour).tl();
			Rect b = Imgproc.boundingRect(contour);
			if (tl.x < 5 & tl.y < 5)
				continue;
			if (b.width + b.x > 636 || b.height + b.y > 475)
				continue;

			output.add(contour);
		}

		return output;
	}

	/**
	 * Compute the convex hulls of contours.
	 */
	private static void convexHulls(List<MatOfPoint> inputContours, ArrayList<MatOfPoint> outputContours) {
		final MatOfInt hull = new MatOfInt();
		outputContours.clear();
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final MatOfPoint mopHull = new MatOfPoint();
			Imgproc.convexHull(contour, hull);
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int) hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1] };
				mopHull.put(j, 0, point);
			}
			outputContours.add(mopHull);
		}
	}

	static Mat distanceTransform(Mat inp, int threshold) {
		Mat proc = new Mat();
		/*
		 * Imgproc.distanceTransform(inp,proc, Imgproc.DIST_C,5);
		 * 
		 * Core.inRange(proc,new Scalar(threshold), new Scalar(255), proc);
		 */
		Imgproc.erode(inp, proc, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 40), new Point(0, 39)));
		return proc;
	}

	static Mat fillHoro(Mat inp) {
		Mat proc = new Mat();
		/*
		 * Imgproc.distanceTransform(inp,proc, Imgproc.DIST_C,5);
		 * 
		 * Core.inRange(proc,new Scalar(threshold), new Scalar(255), proc);
		 */
		Imgproc.dilate(inp, proc, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(4, 1), new Point(2, 0)));
		return proc;
	}

	/**
	 * Runs bitwise and on two binary images
	 */
	static Mat combine(Mat one, Mat two) {
		Mat ret = new Mat();

		Core.bitwise_or(one, two, ret);

		return ret;
	}

	static Mat subtract(Mat one, Mat two) {
		Mat ret = new Mat();

		Core.subtract(one, two, ret);

		return ret;
	}

	static Mat add(Mat one, Mat two) {
		Mat ret = new Mat();

		Core.add(one, two, ret);

		return ret;
	}

	static Mat flip(Mat one) {
		Mat ret = new Mat();

		Core.bitwise_not(one, ret);

		return ret;
	}


//	static void whiteBalance(Mat canvas, double blueC, double redC) {
//		compute.forEach(canvas, (double[] d) -> {
//			// b,g,r
//			d[0] *= blueC;
//			d[2] *= redC;
//
//			return d;
//		});
//	}

	static Mat equalize(Mat inp) {
		Mat out = new Mat();

		List<Mat> channels = new ArrayList<Mat>();
		Core.split(inp, channels);
		for (Mat mat : channels) {
			Imgproc.equalizeHist(mat, mat);
		}
		Core.merge(channels, out);
		return out;
	}

	// ============Histogram================
	// ============Histogram================

	// no visual debug
	/**
	 * Creates a histogram from input 3 channel mat. it will ananlyze white sufaces
	 * and decide the image brightness from them Then it returns a value
	 * proportional to the brightness
	 */
	public static double getHistogramfast(Mat in) {
		int histSize = 255;// # bins
		int histogramHeight = 512;// physcical size

		Mat[] histData = new Mat[] { new Mat(), new Mat(), new Mat() };

		Mat output = new Mat(new Size(512, 512), in.type());

		// memory leaks and phased out data add static to the image
		Imgproc.rectangle(output, new Point(0, 0), new Point(1000, 1000), new Scalar(new double[] { 0, 0, 0 }), -1);

		for (int i = 0; i < 3; i++) {
			getHistdataf(in, i, histData[i], histSize);

			// stretch vertically
			Core.normalize(histData[i], histData[i], histogramHeight, 0, Core.NORM_INF);

		}
		// analysis
		// sum of all white colors
		double whiteSum = 0;
		double whiteTot = 0;
		for (int j = 0; j < histSize; j++) {
			// avg
			double Val = (histData[0].get(j, 0)[0] + histData[1].get(j, 0)[0] + histData[2].get(j, 0)[0]) / 3;
			// min
			double minVal = minf(histData[0].get(j, 0)[0], histData[1].get(j, 0)[0], histData[2].get(j, 0)[0]);

			whiteSum += minVal * j;
			whiteTot += Val * j;

		}
		double whiteSumAvg = whiteSum / whiteTot * 500;// width of the thing
		double blackCutOff = whiteSumAvg * 1 / 5;

		histData[0].release();
		histData[1].release();
		histData[2].release();

		return blackCutOff;

	}

	static void getHistdataf(Mat in, int channel, Mat out, int histsize) {

		Imgproc.calcHist(Arrays.asList(in), new MatOfInt(channel), // color channel
				new Mat(), out, new MatOfInt(histsize), // size
				new MatOfFloat(1, 256));// ranges we do not count zeros because they skew it

		Imgproc.blur(out, out, new Size(90, 90));
	}

	static double minf(double... vals) {
		double min = vals[0];

		for (int i = 1; i < vals.length; i++) {
			if (vals[i] < min)
				min = vals[i];
		}

		return min;
	}

}
