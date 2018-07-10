package com.disnodeteam.dogecv.detectors;

import android.util.Log;


import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;
import com.disnodeteam.dogecv.math.Line;
import com.disnodeteam.dogecv.math.Lines;
import com.disnodeteam.dogecv.math.MathFTC;
import com.disnodeteam.dogecv.math.Points;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CryptoboxDetector extends OpenCVPipeline {

    public enum CryptoboxDetectionMode {
        RED, BLUE
    }

    public enum CryptoboxSpeed {
        VERY_FAST, FAST, BALANCED, SLOW, VERY_SLOW
    }

    public CryptoboxDetectionMode detectionMode      = CryptoboxDetectionMode.RED;
    public double                 downScaleFactor    = 0.5;
    public boolean                rotateMat          = false;
    public CryptoboxSpeed         speed              = CryptoboxSpeed.BALANCED;
    public int                    centerOffset       = 0;
    public boolean                debugShowMask      = true;
    public int                    trackableMemory    = 5;
    public DogeCVColorFilter      colorFilterRed     = new LeviColorFilter(LeviColorFilter.ColorPreset.RED);
    public DogeCVColorFilter      colorFilterBlue    = new LeviColorFilter(LeviColorFilter.ColorPreset.BLUE);


    private boolean CryptoBoxDetected = false;
    private boolean ColumnDetected = false;
    private int[] CryptoBoxPositions = new int[3];

    private Mat workingMat = new Mat();
    private Mat mask = new Mat();
    private Size newSize = new Size();


    private List<List<Point>> trackables = new ArrayList<>(3);
    List<Point> avgPoints = new ArrayList<>();
    Point fullAvgPoint = new Point();

    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        downScaleFactor    = 0.5;
        Size initSize= rgba.size();
        newSize  = new Size(initSize.width * downScaleFactor, initSize.height * downScaleFactor);
        rgba.copyTo(workingMat);

        avgPoints = new ArrayList<>();

        Imgproc.resize(workingMat, workingMat,newSize);
        if(rotateMat) {
            Mat tempBefore = workingMat.t();
            Core.flip(tempBefore, workingMat, 1); //mRgba.t() is the transpose
            tempBefore.release();
        }

        switch(detectionMode){
            case RED:
                Mat redMask = workingMat.clone();
                colorFilterRed.process(redMask, mask);
                redMask.release();
                break;
            case BLUE:
                Mat blueMask = workingMat.clone();
                colorFilterBlue.process(blueMask, mask);
                blueMask.release();
                break;
        }

        //display = new Mat(mask.height(), mask.width(), CvType.CV_8UC1);
        ArrayList<Line> lines = (ArrayList<Line>) Lines.getOpenCvLines(mask, 1, 55);
        lines = (ArrayList<Line>) Lines.linearExtend(lines, 4, newSize);
        //lines = Lines.mergeLines(lines, 13, 300, 6);
        //lines = Lines.mergeLines(lines, 6, 2000, 4);
        List<Line> linesVertical = new ArrayList<Line>();
        for (Line line : lines) {
            if(Lines.getAngularDistance(line, new Line(new Point(0,0), new Point(100,0))) > 45) {
                linesVertical.add(line);
            }
        }

        Collections.sort(linesVertical, new Comparator<Line>() {
            @Override
            public int compare(Line line1, Line line2) {
                if(line1.center().x > line2.center().x){
                    return 1;
                }else if(line1.center().x < line2.center().x){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        if(linesVertical.size() == 0){

            CryptoBoxDetected = false;
            ColumnDetected = false;

            return rgba;
        }

        Line left = linesVertical.get(0);
        Line right = linesVertical.get(linesVertical.size()-1);

        double perpDistance = Lines.getPerpindicularDistance(left, right);
        double collumnLength =Lines.getPerpindicularDistance(left, right)/6;

        List<List<Line>> groupings = new ArrayList<List<Line>>();
        int j = 0;
        while (j < linesVertical.size()) {
            List<Line> group = new ArrayList<Line>();
            group.add(linesVertical.get(j));
            int i = j+1;
            while ( i < linesVertical.size() && Lines.getPerpindicularDistance(linesVertical.get(j), linesVertical.get(i)) < collumnLength) {
                group.add(linesVertical.get(i));
                i++;
            }
            groupings.add(group);
            j = i;
        }

        for (int i = 0; i < groupings.size()-1; i++) {
            Point center = new Line(Lines.getMeanPoint(groupings.get(i)), Lines.getMeanPoint(groupings.get(i+1))).center();
            int y = (int) MathFTC.clip(0.6*center.y, 0, mask.height());
            double max = 1.4*center.y;
            if (center.y < 125) {
                y = 1;
                max = 250;
            }
            int count = 0;
            while (y < mask.height() && y < max && count < 10) {
                if(mask.get(y, (int) center.x)[0] > 0) {
                    count++;
                    //Imgproc.circle(original, new Point(2*center.x, 2*y), 10, new Scalar(255,255,255), 6);
                } else {
                    //Imgproc.circle(original, new Point(2*center.x, 2*y), 10, new Scalar(30,30,200), 6);
                }
                y += 10;
            }
            if(count >= 10) {
                List<Line> appendee = groupings.get(i);
                appendee.addAll(groupings.get(i+1));
                groupings.set(i, appendee);
                groupings.remove(i+1);
                i -= 1;
            }
        }

        for (int i = 0; i < groupings.size(); i++) {
            Point center = Lines.getMeanPoint(groupings.get(i));
            int y = (int) MathFTC.clip(0.2*center.y, 0, mask.height());
            double max = 1.8*center.y;
            if (center.y < 50) {
                y = 1;
                max = (int) 0.8*mask.height();
            }
            int minX = (int) MathFTC.clip(center.x-5, 0, mask.width());
            int maxX = (int) MathFTC.clip(center.x+5, 0, mask.width());
            int count = 0;
            while (y < mask.height() && y < max && count < 10) {
                if(mask.get(y, (int) center.x)[0] > 0 || mask.get(y, minX)[0] > 0 || mask.get(y, maxX)[0] > 0) {
                    count++;
                    // Imgproc.circle(rgba, new Point(2*center.x, 2*y), 10, new Scalar(255,255,255), 6);
                } else {
                    //Imgproc.circle(rgba, new Point(2*center.x, 2*y), 10, new Scalar(30,30,200), 6);
                }
                y += 4;
            }
            if(count <= 9) {
                groupings.remove(i);
                i -= 1;
            }
        }

        if(groupings.size() > 4) {
            Collections.sort(groupings, new Comparator<List<Line>>() {
                @Override
                public int compare(List<Line> g1, List<Line> g2) {
                    if(Lines.stdDevX(g1) > Lines.stdDevX(g2)){
                        return 1;
                    }else if(Lines.stdDevX(g1) < Lines.stdDevX(g2)){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });
            groupings = groupings.subList(0, 4);
        }
        List<Line> columns = new ArrayList<Line>();
        for (int i = 0; i < groupings.size(); i++) {
            Point center =  Lines.getMeanPoint(groupings.get(i));
            double angle = Lines.getMeanAngle(groupings.get(i));
            columns.add(Lines.constructLine(Lines.getMeanPoint(groupings.get(i)), Lines.getMeanAngle(groupings.get(i)), 400));
        }

        for(int i = 0; i < groupings.size(); i++) {
            groupings.set(i, Lines.resize(groupings.get(i), 1/downScaleFactor));
        }

        for(int i = 0; i < groupings.size(); i++) {
            //Imgproc.circle(original, Lines.getMeanPoint(groupings.get(i)), 50, new Scalar(40,200,70), 4);
            for (Line line : groupings.get(i)) {
                //Imgproc.line(rgba, line.point1, line.point2, new Scalar(50,200,55), 4);
                //Imgproc.circle(rgba, line.center(), 20, new Scalar(80,60,190),4);
                //Imgproc.putText(rgba, Integer.toString(i), line.center(), Core.FONT_HERSHEY_PLAIN, 7, new Scalar(10,240,230),3);
            }
        }

        for (Line line : columns) {
            line.resize(1/downScaleFactor);
            Imgproc.line(rgba, line.point1, line.point2, new Scalar(20,165,240), 20);
        }
        if(columns.size() < 3){
            trackables = new ArrayList<>();
            CryptoBoxDetected = false;
            ColumnDetected = false;

            return rgba;
        }



        for(int i=0;i<columns.size() - 1; i++) {

            Line conec = Lines.getPerpindicularConnector(columns.get(i), columns.get(i+1), rgba.size());
            //Imgproc.line(rgba, conec.point1, conec.point2, new Scalar(210, 30, 40), 7);

            Point centerPoint = conec.center();
            //Imgproc.circle(rgba,centerPoint,5, new Scalar(0,255,255),5);

            if(i<3){
                if(trackables.size() == 0){
                    for(int l=0;l<trackableMemory;l++){
                        trackables.add(new ArrayList<Point>());
                    }
                }
                if(trackables.size() <= i ){
                    trackables.add(new ArrayList<Point>());
                }

                if(trackables.get(i).size() < trackableMemory){
                    trackables.get(i).add(centerPoint);
                }else{
                    Collections.rotate(trackables.get(i), -1);
                    trackables.get(i).set(trackableMemory -1 ,centerPoint);
                }

                for(int k =0;k<  trackables.get(i).size();k++){
                    //Imgproc.circle(rgba, trackables.get(i).get(k),4,new Scalar(255,255,255),3);
                }
            }

            Point avgPoint = Points.getMeanPoint(trackables.get(i));
            Imgproc.putText(rgba,"Col #" + i, new Point(avgPoint.x, avgPoint.y - 15), 0, 1.5, new Scalar(0,255,255), 2);
            //DogeLogger.LogVar("Col-"+i, avgPoint.toString());
            Imgproc.circle(rgba,avgPoint, 15,new Scalar(0,255,0),6);
            avgPoints.add(avgPoint);

            CryptoBoxPositions[i] = (int)avgPoint.x;

        }
        if(avgPoints.size() == 3){
            CryptoBoxDetected = true;
        }

        ColumnDetected = true;
        Point newFull =  Points.getMeanPoint(avgPoints);
        Line newFullLine = new Line(newFull, fullAvgPoint);
        if(newFullLine.length() > 75){
            trackables = new ArrayList<>();
            Log.d("DogeCV", "RESETTING TRACKABLE!");
        }
        fullAvgPoint = newFull;
        //  Imgproc.cvtColor(white, white, Imgproc.COLOR_RGB2HSV);


        Imgproc.putText(rgba,"DogeCV 1.1 Crypto: " + newSize.toString() + " - " + speed.toString() + " - " + detectionMode.toString() ,new Point(5,30),0,1.2,new Scalar(0,255,255),2);



        return rgba;



    }



    public Point drawSlot(int slot, List<Rect> boxes){
        Rect leftColumn = boxes.get(slot); //Get the pillar to the left
        Rect rightColumn = boxes.get(slot + 1); //Get the pillar to the right

        int leftX = leftColumn.x; //Get the X Coord
        int rightX = rightColumn.x; //Get the X Coord

        int drawX = ((rightX - leftX) / 2) + leftX; //Calculate the point between the two
        int drawY = leftColumn.height + leftColumn.y; //Calculate Y Coord. We wont use this in our bot's opetation, buts its nice for drawing

        return new Point(drawX, drawY);
    }


    public static double[] getPosition(Line left, Line right, Size size, double f, double x0, double y0) {
        Line connector = Lines.getPerpindicularConnector(left, right, size);
        double u1 = connector.x1-x0;
        double u2 = connector.x2-x0;
        double v1 = y0-connector.y1;
        double v2 = y0-connector.y2;

        double y1 = 7.63*f*Math.sqrt(1/( (Math.pow(u1, 2)+1) - (2*v1*(u1*u2+1)/v2) + (Math.pow(v1/v2, 2)*(Math.pow(u2, 2)+1)) ));
        double y2 = 7.63*f*Math.sqrt(1/( (Math.pow(u2, 2)+1) - (2*v2*(u1*u2+1)/v1) + (Math.pow(v2/v1, 2)*(Math.pow(u1, 2)+1)) ));

        double x1 = y1*u1/f;
        double x2 = y2*u2/f;

        return new double[] {x1,y1,x2,y2};
    }

    public int[] getCryptoBoxPositions() {
        return CryptoBoxPositions;
    }

    public int getCryptoBoxLeftPosition() {
        return CryptoBoxPositions[0];
    }

    public int getCryptoBoxCenterPosition() {
        return CryptoBoxPositions[1];
    }

    public int getCryptoBoxRightPosition() {
        return CryptoBoxPositions[2];
    }

    public boolean isCryptoBoxDetected() {
        return CryptoBoxDetected;
    }

    public boolean isColumnDetected() {
        return ColumnDetected;
    }


    public Size getFrameSize() {
        return newSize;
    }


}
