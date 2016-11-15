/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.image;

import org.lasarobotics.vision.detection.objects.Contour;
import org.lasarobotics.vision.detection.objects.Ellipse;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.util.color.Color;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Methods for drawing shapes onto images
 */
public class Drawing {
    public static void drawCircle(Mat img, Point center, int diameter, Color color) {
        drawCircle(img, center, diameter, color, 2);
    }

    private static void drawCircle(Mat img, Point center, int diameter, Color color, int thickness) {
        Imgproc.circle(img, center, diameter, color.getScalarRGBA(), thickness);
    }

    public static void drawEllipse(Mat img, Ellipse ellipse, Color color) {
        drawEllipse(img, ellipse, color, 2);
    }

    private static void drawEllipse(Mat img, Ellipse ellipse, Color color, int thickness) {
        Imgproc.ellipse(img, ellipse.center(), ellipse.size(), ellipse.angle(), 0, 360, color.getScalarRGBA(), thickness);
    }

    public static void drawEllipses(Mat img, List<Ellipse> ellipses, Color color) {
        drawEllipses(img, ellipses, color, 2);
    }

    public static void drawEllipses(Mat img, List<Ellipse> ellipses, Color color, int thickness) {
        for (Ellipse ellipse : ellipses)
            drawEllipse(img, ellipse, color, thickness);
    }

    public static void drawArc(Mat img, Ellipse ellipse, Color color, double angleDegrees) {
        drawArc(img, ellipse, color, angleDegrees, 2);
    }

    private static void drawArc(Mat img, Ellipse ellipse, Color color, double angleDegrees, int thickness) {
        Imgproc.ellipse(img, ellipse.center(), ellipse.size(), ellipse.angle(), 0, angleDegrees, color.getScalarRGBA(), thickness);
    }

    public static void drawText(Mat img, String text, Point origin, float scale, Color color) {
        drawText(img, text, origin, scale, color, Anchor.TOPLEFT);
    }

    public static void drawText(Mat img, String text, Point origin, float scale, Color color, Anchor locationOnImage) {
        if (locationOnImage == Anchor.BOTTOMLEFT)
            Transform.flip(img, Transform.FlipType.FLIP_ACROSS_Y);
        Imgproc.putText(img, text, origin, Core.FONT_HERSHEY_SIMPLEX, scale, color.getScalarRGBA(), 2, Core.LINE_8,
                (locationOnImage == Anchor.BOTTOMLEFT || locationOnImage == Anchor.BOTTOMLEFT_UNFLIPPED_Y));
        if (locationOnImage == Anchor.BOTTOMLEFT)
            Transform.flip(img, Transform.FlipType.FLIP_ACROSS_Y);
    }

    public static void drawLine(Mat img, Point point1, Point point2, Color color) {
        drawLine(img, point1, point2, color, 2);
    }

    public static void drawLine(Mat img, Point point1, Point point2, Color color, int thickness) {
        Imgproc.line(img, point1, point2, color.getScalarRGBA(), thickness);
    }

    public static void drawCross(Mat img, Point center, Color color) {
        drawCross(img, center, color, 8, 2);
    }

    public static void drawCross(Mat img, Point center, Color color, int radius, int thickness) {
        drawLine(img, new Point(center.x - radius, center.y),
                new Point(center.x + radius, center.y), color, thickness);
        drawLine(img, new Point(center.x, center.y - radius),
                new Point(center.x, center.y + radius), color, thickness);
    }

    public static void drawContour(Mat img, Contour contour, Color color) {
        drawContour(img, contour, color, 2);
    }

    private static void drawContour(Mat img, Contour contour, Color color, int thickness) {
        List<MatOfPoint> contoursOut = new ArrayList<>();
        contoursOut.add(contour.getData());
        Imgproc.drawContours(img, contoursOut, -1, color.getScalarRGBA(), thickness);
    }

    public static void drawContours(Mat img, List<Contour> contours, Color color) {
        drawContours(img, contours, color, 2);
    }

    public static void drawContours(Mat img, List<Contour> contours, Color color, int thickness) {
        List<MatOfPoint> contoursOut = new ArrayList<>();
        for (Contour contour : contours)
            contoursOut.add(contour.getData());
        Imgproc.drawContours(img, contoursOut, -1, color.getScalarRGBA(), thickness);
    }

    public static void drawRectangles(Mat img, List<Rectangle> rects, Color color) {
        for (Rectangle r : rects)
            drawRectangle(img, r.topLeft(), r.bottomRight(), color, 2);
    }

    public static void drawRectangles(Mat img, List<Rectangle> rects, Color color, int thickness) {
        for (Rectangle r : rects)
            drawRectangle(img, r.topLeft(), r.bottomRight(), color, thickness);
    }

    public static void drawRectangle(Mat img, Rectangle rect, Color color) {
        drawRectangle(img, rect.topLeft(), rect.bottomRight(), color, 2);
    }

    public static void drawRectangle(Mat img, Rectangle rect, Color color, int thickness) {
        drawRectangle(img, rect.topLeft(), rect.bottomRight(), color, thickness);
    }

    public static void drawRectangle(Mat img, Point topLeft, Point bottomRight, Color color) {
        drawRectangle(img, topLeft, bottomRight, color, 2);
    }

    public static void drawRectangle(Mat img, Point topLeft, Point bottomRight, Color color, int thickness) {
        Imgproc.rectangle(img, topLeft, bottomRight, color.getScalarRGBA(), thickness);
    }

    public enum Anchor {
        TOPLEFT,
        BOTTOMLEFT,
        BOTTOMLEFT_UNFLIPPED_Y
    }
}
