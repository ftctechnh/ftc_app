package org.firstinspires.ftc.teamcode;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Noah on 11/24/2016.
 */

public final class LineFollowLib {
    public static final int ERROR_TOO_NOISY = -255;

    //get the angle of the phone relative to robot through line following
    public static double getAngle(Mat src, int topY, int bottomY){
        int[] linePos = new int[2];

        linePos[0] = scanlineAvg(src, topY);
        linePos[1] = scanlineAvg(src, bottomY);

        if(linePos[0] == ERROR_TOO_NOISY || linePos[1] == ERROR_TOO_NOISY) return ERROR_TOO_NOISY;

        final int yDist = linePos[1] - linePos[0];
        final int xDist = bottomY - topY;

        double headingToTarget = Math.atan2(yDist, xDist);
        headingToTarget = Math.toDegrees(headingToTarget); //to degrees

        return headingToTarget;
    }

    //gets the line relative to the camera view on the horizontal axis, from 1 to -1
    public static double getDisplacment(Mat src, int middleY){
        int linePos;

        linePos = scanlineAvg(src, middleY);

        if(linePos == ERROR_TOO_NOISY) return ERROR_TOO_NOISY;

        //convert to value from -1 to 1
        return  (double)(linePos - (src.cols() / 2))  / (double)(src.cols() / 2);
    }

    public static int[] scanlineAvg(Mat src, int[] scanlineYs) {
        //here we gooooooooo!

        byte[][]ray = new byte[scanlineYs.length][src.cols()];

        //apply adaptive threshold to image
        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        //copy scan lines into arrays
        for (int i = 0; i < scanlineYs.length; i++) {
            src.row(scanlineYs[i]).get(0, 0, ray[i]);
        }

        //calculate average position for each
        int[] ret = new int[scanlineYs.length];
        for (int i = 0; i < scanlineYs.length; i++) {
            final int avg = findAvgOverZero(ray[i]);
            if(avg == -400) ret[i] = ERROR_TOO_NOISY;
            else ret[i] = avg;
        }

        return ret;
    }

    public static int scanlineAvg(Mat src, int scanlineY){
        byte[] ray = new byte[src.cols()];

        Imgproc.threshold(src, src, 0, 200, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);

        src.row(scanlineY).get(0, 0, ray);

        final int avg = findAvgOverZero(ray);
        if(avg == -400) return ERROR_TOO_NOISY;
        else return avg;
    }


    //takes a n array of bytes, and returns (min + max)/2 for a threshold
    private static int threshFind(int[] ray){
        int min = 0;
        int max = 0;
        for(int i = 0; i < ray.length; i++){
            if(min > ray[i]) min = ray[i];
            else if(max < ray[i]) max = ray[i];
        }

        return (min + max) / 2;
    }

    //find the average position of all numbers > 0 in an array
    private static int findAvgOverZero(byte[] ray){
        int totalPos = 0;
        int totalNum = 0;
        for(int i = 0; i < ray.length; i++){
            if(ray[i] != 0){
                totalPos += i;
                totalNum++;
            }
        }

        //and return it in a value between -1 and 1, or and error if there is too much noise
        if(totalNum > ray.length * 2 / 3 || totalNum < 25) return -400;
        else if(totalNum == 0) return 0;
        else return totalPos/totalNum;
    }
}