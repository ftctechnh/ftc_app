package RicksCode.Bill_VV;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by ftc8045 on 10/22/2017.
 */

public class BeaconColorDetector {

    public void init(HardwareMap hardwareMap) {
    }

    static public int red(int pixel) {
        return (pixel >> 16) & 0xff;
    }

    static public int green(int pixel) {
        return (pixel >> 8) & 0xff;
    }

    static public int blue(int pixel) {
        return pixel & 0xff;
    }

    public boolean blueIsOnLeft(Bitmap rgbImage) {
        int leftXStart = (int) ((double) rgbImage.getWidth() * 0.05);
        int rightXStart = (int) ((double) rgbImage.getWidth() * 0.75);
        int leftXEnd = (int) ((double) rgbImage.getWidth() * 0.25);
        int rightXEnd = (int) ((double) rgbImage.getWidth() * 0.95);

        int yStart = (int) ((double) rgbImage.getHeight() * 0.4);
        int yEnd = (int) ((double) rgbImage.getHeight() * 0.6);

        int rightRedValue = 0;
        int rightBlueValue = 0;
        int leftRedValue = 0;
        int leftBlueValue = 0;

        for (int x = leftXStart; x < leftXEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                leftRedValue += red(pixel);
                leftBlueValue += blue(pixel);
            }
        }

        for (int x = rightXStart; x < rightXEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                rightRedValue += red(pixel);
                rightBlueValue += blue(pixel);
            }
        }

        if (leftRedValue - leftBlueValue > rightRedValue - rightBlueValue)
            return true;
        else
            return false;
    }

    public boolean leftSideBlue(Bitmap rgbImage) {
        int leftXStart = (int) ((double) rgbImage.getWidth() * 0.05);
        int rightXStart = (int) ((double) rgbImage.getWidth() * 0.75);
        int leftXEnd = (int) ((double) rgbImage.getWidth() * 0.25);
        int rightXEnd = (int) ((double) rgbImage.getWidth() * 0.95);

        int yStart = (int) ((double) rgbImage.getHeight() * 0.4);
        int yEnd = (int) ((double) rgbImage.getHeight() * 0.6);

        int rightRedValue = 0;
        int rightBlueValue = 0;
        int leftRedValue = 0;
        int leftBlueValue = 0;

        for (int x = leftXStart; x < leftXEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                leftRedValue += red(pixel);
                leftBlueValue += blue(pixel);
            }
        }

        for (int x = rightXStart; x < rightXEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                rightRedValue += red(pixel);
                rightBlueValue += blue(pixel);
            }
        }

        if (leftRedValue - leftBlueValue > 0)
            return false;
        else
            return true;
    }

    public boolean rightSideBlue(Bitmap rgbImage) {
        int leftXStart = (int) ((double) rgbImage.getWidth() * 0.05);
        int rightXStart = (int) ((double) rgbImage.getWidth() * 0.75);
        int leftXEnd = (int) ((double) rgbImage.getWidth() * 0.25);
        int rightXEnd = (int) ((double) rgbImage.getWidth() * 0.95);

        int yStart = (int) ((double) rgbImage.getHeight() * 0.4);
        int yEnd = (int) ((double) rgbImage.getHeight() * 0.6);

        int rightRedValue = 0;
        int rightBlueValue = 0;
        int leftRedValue = 0;
        int leftBlueValue = 0;

        for (int x = leftXStart; x < leftXEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                leftRedValue += red(pixel);
                leftBlueValue += blue(pixel);
            }
        }

        for (int x = rightXStart; x < rightXEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                rightRedValue += red(pixel);
                rightBlueValue += blue(pixel);
            }
        }

        if (rightRedValue - rightBlueValue > 0)
            return false;
        else
            return true;
    }


    public boolean wholeBeaconBlue(Bitmap rgbImage) {
        if(leftSideBlue(rgbImage) && rightSideBlue(rgbImage))
            return true;
        else
            return false;
    }
    public boolean wholeBeaconRed(Bitmap rgbImage) {
        if(!leftSideBlue(rgbImage) && !rightSideBlue(rgbImage))
            return true;
        else
            return false;
    }

}

