package org.firstinspires.ftc.robotcontroller.internal.Core.OpenCV;


import android.os.Environment;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class CVUtil
{
    /**
     * Saves an image to the default photos directory
     *
     * @param mat Mat image to save
     * @param conversionCode OpenCV code for converting to bgr
     * @param suffix End-of-file name (not extension!)
     * @param time Current time, goes at the beginning of the file name
     *
     * @return True if writing successful, false otherwise
     */
    public static boolean saveImg(Mat mat , int conversionCode , String suffix , long time)
    {
        Mat bgr = new Mat();
        Imgproc.cvtColor(mat , bgr , conversionCode);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path , time + "_" + suffix + ".png");

        if(Imgcodecs.imwrite(file.toString() , bgr))
        {
            return true;
        }
        return false;
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
}
