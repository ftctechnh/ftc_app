package ftc.vision;


import android.os.Environment;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

/**
 * This file was made by the electronVolts, FTC team 7393
 * Date Created: 8/25/16.
 */
public class ImageUtil {
  public static final Scalar WHITE        = new Scalar(255, 255, 255);
  public static final Scalar GRAY         = new Scalar(128, 128, 128);
  public static final Scalar BLACK        = new Scalar(  0,   0,   0);

  public static final Scalar RED          = HSVtoRGB(  0, 255, 255);
  public static final Scalar ORANGE       = HSVtoRGB( 30, 255, 255);
  public static final Scalar YELLOW       = HSVtoRGB( 60, 255, 255);
  public static final Scalar CHARTREUSE   = HSVtoRGB( 90, 255, 255);
  public static final Scalar GREEN        = HSVtoRGB(120, 255, 255);
  public static final Scalar SPRING_GREEN = HSVtoRGB(150, 255, 255);
  public static final Scalar INDIGO       = HSVtoRGB(180, 255, 255);
  public static final Scalar AZURE        = HSVtoRGB(210, 255, 255);
  public static final Scalar BLUE         = HSVtoRGB(240, 255, 255);
  public static final Scalar PURPLE       = HSVtoRGB(270, 255, 255);
  public static final Scalar MAGENTA      = HSVtoRGB(300, 255, 255);
  public static final Scalar PINK         = HSVtoRGB(330, 255, 255);

  public static final Scalar BROWN        = HSVtoRGB( 30, 255, 150);

  /**
   * Compare 2 Scalars
   * @param s1 the first Scalar
   * @param s2 the second Scalar
   * @return whether or not s1 > s2
   */
  public static boolean isScalarGreater(Scalar s1, Scalar s2){
    for (int i=0; i<s1.val.length; i++) {
      if(s1.val[i] <= s2.val[i]) return false;
    }
    return true;
  }

  /**
   * Compare 2 Scalars
   * @param s1 the first Scalar
   * @param s2 the second Scalar
   * @return whether or not s1 < s2
   */
  public static boolean isScalarLess(Scalar s1, Scalar s2){
    for (int i=0; i<s1.val.length; i++) {
      if(s1.val[i] >= s2.val[i]) return false;
    }
    return true;
  }

  /**
   * Finds the center of an OpenCV Rect
   * @param r the Rect
   * @return an OpenCV Point that is the center
   */
  public static Point centerOfRect(Rect r) {
    return new Point(r.x+r.width/2, r.y+r.height/2);
  }

  /**
   * Finds the center of an OpenCV RotatedRect
   * @param r the RotatedRect
   * @return an OpenCV Point that is the center
   */
  public static Point centerOfRect(RotatedRect r) {
    return centerOfRect(r.boundingRect());
  }

  /**
   * Rescale the hue of an HSV Scalar to be in the range 0 to 179
   * @param hsv the input HSV Scalar
   * @return the rescaled HSV Scalar
   */
  public static double rescaleHsvHue(Scalar hsv) {
    //rescale hsv hue to range 0-179
    double[] vals = hsv.val;
    vals[0] = vals[0] % 180;
    if(vals[0] < 0) vals[0] += 180;
    hsv.set(vals);
    return vals[0];
  }

  /**
   * Applys the Core.inRange function to a Mat after accounting for rollover
   * on the hsv hue channel.
   * @param srcHSV source Mat in HSV format
   * @param min Scalar that defines the min h, s, and v values
   * @param max Scalar that defines the max h, s, and v values
   * @param dst the output binary image
   */
  public static void hsvInRange(Mat srcHSV, Scalar min, Scalar max, Mat dst){
    //if the max hue is greater than the min hue
    if(max.val[0] > min.val[0]) {
      //use inRange once
      Core.inRange(srcHSV, min, max, dst);
    } else {
      //otherwise, compute 2 ranges and bitwise or them
      double[] vals = min.val.clone();
      vals[0] = 0;
      Scalar min2 = new Scalar(vals);
      vals = max.val.clone();
      vals[0] = 179;
      Scalar max2 = new Scalar(vals);

      Mat tmp1 = new Mat(), tmp2 = new Mat();
      Core.inRange(srcHSV, min, max2, tmp1);
      Core.inRange(srcHSV, min2, max, tmp2);
      Core.bitwise_or(tmp1, tmp2, dst);
    }
  }

  /**
   * rotates a Mat by any angle. If the angle is 90n, use transpose and/or flip.
   * Otherwise, use warpAffine
   * @param src Mat to be rotated
   * @param dst output Mat
   * @param angle angle to rotate by
   */
  public static void rotate(Mat src, Mat dst, int angle) {
    angle = angle % 360;
    if (angle == 270 || angle == -90) {
      // Rotate clockwise 270 degrees
      Core.transpose(src, dst);
      Core.flip(dst, dst, 0);
    }
    else if (angle == 180 || angle == -180) {
      // Rotate clockwise 180 degrees
      Core.flip(src, dst, -1);
    }
    else if (angle == 90 || angle == -270) {
      // Rotate clockwise 90 degrees
      Core.transpose(src, dst);
      Core.flip(dst, dst, 1);
    }
    else if (angle == 360 || angle == 0 || angle == -360) {
      if (src.dataAddr() != dst.dataAddr()) {
        src.copyTo(dst);
      }
    }
    else {
      Point srcCenter = new Point(src.width()/2, src.height()/2);
      Size size = new RotatedRect(srcCenter, src.size(), angle).boundingRect().size();
      Point center = new Point(size.width/2, size.height/2);

      Mat rotationMatrix2D = Imgproc.getRotationMatrix2D(center, angle, 1);
      Imgproc.warpAffine(src, dst, rotationMatrix2D, size);
    }
  }

  /**
   * Save an image to a file
   * @param tag logging tag
   * @param mat image to save
   * @param conversionToBGR openCV code to convert to bgr
   * @param fileSuffix end of file name
   * @param time start of file name
   * @return whether or not the save was successful
   */
  public static boolean saveImage(String tag, Mat mat, int conversionToBGR, String fileSuffix, long time) {
    Mat bgrMat = new Mat();
    Imgproc.cvtColor(mat, bgrMat, conversionToBGR);

    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File file = new File(path, time + "_" + fileSuffix + ".png");

    if (Imgcodecs.imwrite(file.toString(), bgrMat)){
      return true;
    } else {
      Log.e(tag, "FAILED writing image to external storage");
      return false;
    }
  }

  /**
   * Rotates Point p by angleRadians about Point ref.
   * @param ref center of rotation
   * @param p point to be rotated
   * @param angleRadians angle to rotate in radians
   * @return a new point which is the rotated p
   */
  public static Point rotatePoint(Point ref, Point p, double angleRadians){
    return new Point(
            ((p.x - ref.x) * Math.cos(angleRadians)) - ((p.y - ref.y) * Math.sin(angleRadians)) + ref.x,
            ((p.x - ref.x) * Math.sin(angleRadians)) + ((p.y - ref.y) * Math.cos(angleRadians)) + ref.y
    );
  }

  public static Mat graphColSum(Mat colSum, int height) {
    int[] white = {128};
    int[] data = new int[3];
    int max = 0;
    for(int x=0; x<colSum.width(); x++){
      colSum.get(0, x, data);
      if (data[0] > max) {
        max = data[0];
      }
    }
    Mat graph = new Mat(colSum.rows(), height, CvType.CV_8UC1);
    for(int x=0; x<colSum.width(); x++){
      colSum.get(0, x, data);
      int y = 0;
      if (max > 0) {
        y = data[0] * height / max;
      }
      graph.put(y,
              x,
              white);
      if (data[0] > max) {
        max = data[0];
      }
    }
    return graph;
  }

  /**
   * Converts colorspace from HSV to RGB
   * @param hsv OpenCV HSV Scalar (Note: H goes from 0 to 179)
   * @return OpenCV RGB Scalar
   */
  public static Scalar HSVtoRGB(Scalar hsv) {
    return HSVtoRGB(hsv.val[0] * 2, hsv.val[1], hsv.val[2]);
  }

  /**
   * Converts colorspace from HSV to RGB
   * @param h HSV hue (0 to 360)
   * @param s HSV saturation (0 to 255)
   * @param v HSV value (0 to 255)
   * @return OpenCV RGB Scalar
   */
  public static Scalar HSVtoRGB(double h, double s, double v) {
    double r, g, b;
    int i;
    double f, p, q, t;
    h /= 60;			// sector 0 to 5
    i = (int) h;
    f = h - i;			// factorial part of h
    p = v * ( 1 - s );
    q = v * ( 1 - s * f );
    t = v * ( 1 - s * ( 1 - f ) );
    switch( i ) {
      case 0:
        r = v;
        g = t;
        b = p;
        break;
      case 1:
        r = q;
        g = v;
        b = p;
        break;
      case 2:
        r = p;
        g = v;
        b = t;
        break;
      case 3:
        r = p;
        g = q;
        b = v;
        break;
      case 4:
        r = t;
        g = p;
        b = v;
        break;
      default:		// case 5:
        r = v;
        g = p;
        b = q;
        break;
    }
    return new Scalar(r, g, b);
  }
}
