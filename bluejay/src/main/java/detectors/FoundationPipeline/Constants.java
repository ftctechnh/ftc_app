package detectors.FoundationPipeline;

import org.opencv.core.Mat;

public class Constants {
    public static final double[] blueColor1 =  {170,180};
    public static final double[] blueColor2 =  {0,10};

    public static final double[] redColor =  {110,120};

    public static final double[] yellowColor = {7+67,38+67};
    public static final double[] stressedYellowColor = {-12+67,45+67};

    public static Mat redOutput    = new Mat();
    public static Mat blueOutput   = new Mat();
    public static Mat blackOutput  = new Mat();
    public static Mat yellowOutput = new Mat();
    public static Mat yellowTags   = new Mat();

    public static void updateColors(Mat resizedImage, Mat equalizedImage, double blackCutOff) {
        /*
        //For yellow
        double[] yellowRange = {73,86};

        //For Blue
        double[] blueRange1 = {160,180};
        double[] blueRange2 = {0,20};

        //for Red
        double[] redRange = {40,63};
        */

        double[] satRange = {60, 255};
        double[] valRange = {blackCutOff*1.0, 255};

        redOutput = compute.threshold(resizedImage, redColor, satRange, valRange);

        blueOutput = compute.combine(
                compute.threshold(resizedImage, blueColor1, satRange, valRange),
                compute.threshold(resizedImage, blueColor2, satRange, valRange));

        blackOutput = compute.threshold(
                resizedImage,
                new double[]{0, 255},//hue  0, 180
                new double[]{0, 180},//sat  0, 180
                new double[]{0, blackCutOff*0.8});//val


        //For yellow HUE HUE HUE
        double[] yellowRange = yellowColor;
        double[] stressedYellowRange = stressedYellowColor;

        //STONE STONE STONE
        yellowOutput = compute.threshold( //equalize to spread. yellowRange to be less
                equalizedImage,
                yellowRange,
                new double[]{100, 255},//sat
                new double[]{blackCutOff*1.0, 255}); //val

        //SKYSTONE SKYSTONE
        yellowTags = compute.threshold(  //just want all of it
                resizedImage,
                stressedYellowRange,
                new double[]{90, 255},//sat
                new double[]{blackCutOff*0.8 , 255}); //val
        //        Mat yellowTags = compute.threshold(
//                resizedImage,
//                new double[]{80,105},
//                new double[]{100, 255},//sat
//                new double[]{blackCutOff*1.5, 255}); //val

    }

}
