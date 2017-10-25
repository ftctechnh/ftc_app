package org.firstinspires.ftc.robotcontroller.internal.Core.OpenCV;


import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilColor;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class JewelFinder
{
    // Colors for the left and right jewels
    private UtilColor.Color _leftColor;
    private UtilColor.Color _rightColor;


    public void process(long startTime , Mat rbga , boolean save)
    {
        if(save)
        {
            CVUtil.saveImg(rbga , Imgproc.COLOR_RGB2BGR , "0_camera" , startTime);
        }

        Mat hsv = new Mat();
        Imgproc.cvtColor(rbga , hsv , Imgproc.COLOR_RGB2HSV);

        ArrayList<Scalar> hsvMin = new ArrayList<>();
        ArrayList<Scalar> hsvMax = new ArrayList<>();

        //hsvMin.add(new Scalar(  H,   S,   V  ));
        hsvMin.add(new Scalar(300/2,  50, 150)); //red min
        hsvMax.add(new Scalar( 60/2, 255, 255)); //red max

        hsvMin.add(new Scalar( 60/2,  50, 150)); //green min
        hsvMax.add(new Scalar(180/2, 255, 255)); //green max

        hsvMin.add(new Scalar(180/2,  50, 150)); //blue min
        hsvMax.add(new Scalar(300/2, 255, 255)); //blue max

        // make a list of channels that are blank (used for combining binary images)
        ArrayList<Mat> rgbaChannels = new ArrayList<>();

        // For each side of the image, a "color mass" will be computed. This mass is just how
        // much of a color is present on that side (in units of scaled pixels that pass the
        // color filter). This variable keeps track of the mass
        // of the color that ended up having the most "color mass" on each side.
        double [] maxMass = { Double.MIN_VALUE, Double.MIN_VALUE }; //max mass for left and right
        // This next variable keeps track of the color on each side that had the max "color mass"
        // with  0=red  1=green  2=blue  3=UNKNOWN
        // So both sides start as unknown:
        int[] maxMassIndex = { 3, 3}; // index of the max mass

        // We are about to loop over the filters and compute the "color mass" for each color
        // on each side of the image.

        // These variables are used inside the loop:
        Mat maskedImage;
        Mat colSum = new Mat();
        double mass;
        int[] data = new int[3]; //used to read the colSum

        //loop through the filters
        for(int i=0; i<3; i++) {
            //apply HSV thresholds
            maskedImage = new Mat();
            CVUtil.hsvInRange(hsv, hsvMin.get(i), hsvMax.get(i), maskedImage);

            //copy the binary image to a channel of rgbaChannels
            rgbaChannels.add(maskedImage);

            //apply a column sum to the (unscaled) binary image
            Core.reduce(maskedImage, colSum, 0, Core.REDUCE_SUM, 4);

            //loop through left and right to calculate mass
            int start = 0;
            int end = hsv.width()/2;
            for(int j=0; j<2; j++){
                //calculate the mass
                mass = 0;
                for(int x=start; x<end; x++){
                    colSum.get(0, x, data);
                    mass += data[0];
                }
                mass /= hsv.size().area(); //scale the mass by the image size

                //if the mass found is greater than the max for this side
                if(mass >= 6 && mass > maxMass[j]){
                    //this mass is the new max for this side
                    maxMass[j] = mass;
                    //and this index is the new maxIndex for this side
                    maxMassIndex[j] = i;
                }

                start = end;
                end = hsv.width();
            }

        }
        //add empty alpha channel
        rgbaChannels.add(Mat.zeros(hsv.size(), CvType.CV_8UC1));
        //merge the 3 binary images and 1 alpha channel into one image
        Core.merge(rgbaChannels, rbga);

        UtilColor.Color colors[] = {UtilColor.Color.RED , UtilColor.Color.GREEN , UtilColor.Color.BLUE ,
                UtilColor.Color.UNKNOWN};

        _leftColor = colors[maxMassIndex[0]];
        _rightColor = colors[maxMassIndex[1]];

        if(save)
        {
            CVUtil.saveImg(rbga , Imgproc.COLOR_RGBA2BGR , "1_binary" , startTime);
        }
    }


    /**
     * @return Result of jewel finding as a String
     */
    String resultStr()
    {
        return _leftColor.asString() + " , " + _rightColor.asString();
    }
}
