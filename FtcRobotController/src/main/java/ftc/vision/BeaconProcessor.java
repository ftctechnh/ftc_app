package ftc.vision;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vandejd1 on 8/29/16.
 * FTC Team EV 7393
 */
public class BeaconProcessor implements ftc.vision.ImageProcessor<ftc.vision.BeaconColorResult> {
    private static final String TAG = "BeaconProcessor";
    private static final double MIN_MASS = 1.0;

    @Override
    public ftc.vision.ImageProcessorResult<ftc.vision.BeaconColorResult> process(long startTime, Mat rgbaFrame, boolean saveImages) {
        //save the image in the Pictures directory
        if (saveImages) {
            ftc.vision.ImageUtil.saveImage(TAG, rgbaFrame, Imgproc.COLOR_RGBA2BGR, "0_camera", startTime);
        }
        //convert image to hsv
        Mat hsv = new Mat();
        Imgproc.cvtColor(rgbaFrame, hsv, Imgproc.COLOR_RGB2HSV);
        // rgbaFrame is untouched; hsv now contains the same image but using HSV colors

        // Note that in OpenCV, the HSV values have these valid ranges:
        //the h range is 0 to 179
        //the s range is 0 to 255
        //the v range is 0 to 255

        // This class will create filters to find red and find blue and find green.
        // This is done by creating a range of H and S and V that selects each color.
        // The HSV thresholds for each color (red, green, blue) are stored as a list of min HSV
        // and a list of max HSV
        List<Scalar> hsvMin = new ArrayList<>();
        List<Scalar> hsvMax = new ArrayList<>();

        //hsvMin.add(new Scalar(  H,   S,   V  ));
        hsvMin.add(new Scalar(  // yellow minimum
                (int) (37 / 360.0 * 179.0),     // Reddest allowed yellow. Was 46.
                (int) (0.60 * 255.0),           // Allow some pastel
                (int) (0.50 * 255.0)));         // Allow some dimming. Was 0.50.
        hsvMax.add(new Scalar(  // yellow max
                (int) (66 / 360.0 * 179.0),     // Greenest allowed yellow
                (int) (1.00 * 255.0),           // Pure color
                (int) (1.00 * 255.0)));         // Fully illuminated

        hsvMin.add(new Scalar( 45,  50, 150));  // green min
        hsvMax.add(new Scalar(75, 255, 255));   // green max

        hsvMin.add(new Scalar( //white min
                0,                              // Allow any color, as long as ...
                (int) (0.00 * 255.0),           // ... it's really gray or white (unsaturated).
                (int) (0.80 * 255.0)));         // Allow some dimming.
        hsvMax.add(new Scalar( //white max
                179,                            // Allow any color, as long as ...
                (int) (0.20 * 255.0),           // ... it's close to white (pastel), and ...
                (int) (1.00 * 255.0)));         // ... really bright.

        // make a list of channels that are blank (used for combining binary images)
        List<Mat> rgbaChannels = new ArrayList<>();

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
            ftc.vision.ImageUtil.hsvInRange(hsv, hsvMin.get(i), hsvMax.get(i), maskedImage);

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
                if(mass >= MIN_MASS && mass > maxMass[j]){
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
        Core.merge(rgbaChannels, rgbaFrame);

        //use the maxIndex array to get the left and right colors
        ftc.vision.BeaconColorResult.BeaconColor[] beaconColors = ftc.vision.BeaconColorResult.BeaconColor.values();
        ftc.vision.BeaconColorResult.BeaconColor left = beaconColors[maxMassIndex[0]];
        ftc.vision.BeaconColorResult.BeaconColor right = beaconColors[maxMassIndex[1]];

        //draw the color result bars
        int barHeight = hsv.height()/30;
        Imgproc.rectangle(rgbaFrame, new Point(0, 0), new Point(hsv.width()/2, barHeight), left.color, barHeight);
        Imgproc.rectangle(rgbaFrame, new Point(hsv.width()/2, 0), new Point(hsv.width(), barHeight), right.color, barHeight);

        if (saveImages) {
            ftc.vision.ImageUtil.saveImage(TAG, rgbaFrame, Imgproc.COLOR_RGBA2BGR, "1_binary", startTime);
        }

        //construct and return the result
        return new ftc.vision.ImageProcessorResult<>(startTime, rgbaFrame,
                new ftc.vision.BeaconColorResult(left, right)
        );
    }
}
