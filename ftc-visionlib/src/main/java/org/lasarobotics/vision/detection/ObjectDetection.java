/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Some code from OpenCV samples, license at http://opencv.org/license.html.
 */
package org.lasarobotics.vision.detection;

import android.util.Log;

import org.lasarobotics.vision.image.Drawing;
import org.lasarobotics.vision.image.Transform;
import org.lasarobotics.vision.util.color.ColorRGBA;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Object Detector - searches a scene for keypoints then can match keypoints in the object
 * to keypoints in the scene, effectively locating an object within a scene
 * <p/>
 * This class is designed to detect a single object at a time in an image
 */
public class ObjectDetection {
    private final FeatureDetector detector;
    private final DescriptorExtractor extractor;
    private final DescriptorMatcher matcher;

    /**
     * Instantiate an object detector based on the FAST, BRIEF, and BRUTEFORCE_HAMMING algorithms
     */
    public ObjectDetection() {
        detector = FeatureDetector.create(FeatureDetectorType.FAST.val());
        extractor = DescriptorExtractor.create(DescriptorExtractorType.BRIEF.val());
        matcher = DescriptorMatcher.create(DescriptorMatcherType.BRUTEFORCE_HAMMING.val());
    }

    /**
     * Instantiate an object detector based on custom algorithms
     *
     * @param detector  Keypoint detection algorithm
     * @param extractor Keypoint descriptor extractor
     * @param matcher   Descriptor matcher
     */
    public ObjectDetection(FeatureDetectorType detector, DescriptorExtractorType extractor, DescriptorMatcherType matcher) {
        this.detector = FeatureDetector.create(detector.val());
        this.extractor = DescriptorExtractor.create(extractor.val());
        this.matcher = DescriptorMatcher.create(matcher.val());
    }

    /**
     * Draw keypoints directly onto an scene image - red circles indicate keypoints
     *
     * @param output        The scene matrix
     * @param sceneAnalysis Analysis of the scene, as given by analyzeScene()
     */
    public static void drawKeypoints(Mat output, SceneAnalysis sceneAnalysis) {
        KeyPoint[] keypoints = sceneAnalysis.keypoints.toArray();
        for (KeyPoint kp : keypoints) {
            Drawing.drawCircle(output, new Point(kp.pt.x, kp.pt.y), 4, new ColorRGBA(255, 0, 0));
        }
    }

    /**
     * Draw the object's location
     *
     * @param output         Image to draw on
     * @param objectAnalysis Object analysis information
     * @param sceneAnalysis  Scene analysis information
     */
    public static void drawObjectLocation(Mat output, ObjectAnalysis objectAnalysis, SceneAnalysis sceneAnalysis) {
        List<Point> ptsObject = new ArrayList<>();
        List<Point> ptsScene = new ArrayList<>();

        KeyPoint[] keypointsObject = objectAnalysis.keypoints.toArray();
        KeyPoint[] keypointsScene = sceneAnalysis.keypoints.toArray();

        DMatch[] matches = sceneAnalysis.matches.toArray();

        for (DMatch matche : matches) {
            //Get the keypoints from these matches
            ptsObject.add(keypointsObject[matche.queryIdx].pt);
            ptsScene.add(keypointsScene[matche.trainIdx].pt);
        }

        MatOfPoint2f matObject = new MatOfPoint2f();
        matObject.fromList(ptsObject);

        MatOfPoint2f matScene = new MatOfPoint2f();
        matScene.fromList(ptsScene);

        //Calculate homography of object in scene
        Mat homography = Calib3d.findHomography(matObject, matScene, Calib3d.RANSAC, 5.0f);

        //Create the unscaled array of corners, representing the object size
        Point cornersObject[] = new Point[4];
        cornersObject[0] = new Point(0, 0);
        cornersObject[1] = new Point(objectAnalysis.object.cols(), 0);
        cornersObject[2] = new Point(objectAnalysis.object.cols(), objectAnalysis.object.rows());
        cornersObject[3] = new Point(0, objectAnalysis.object.rows());

        Point[] cornersSceneTemp = new Point[0];

        MatOfPoint2f cornersSceneMatrix = new MatOfPoint2f(cornersSceneTemp);
        MatOfPoint2f cornersObjectMatrix = new MatOfPoint2f(cornersObject);

        //Transform the object coordinates to the scene coordinates by the homography matrix
        Core.perspectiveTransform(cornersObjectMatrix, cornersSceneMatrix, homography);

        //Mat transform = Imgproc.getAffineTransform(cornersObjectMatrix, cornersSceneMatrix);

        //Draw the lines of the object on the scene
        Point[] cornersScene = cornersSceneMatrix.toArray();
        final ColorRGBA lineColor = new ColorRGBA("#00ff00");
        Drawing.drawLine(output, new Point(cornersScene[0].x + objectAnalysis.object.cols(), cornersScene[0].y),
                new Point(cornersScene[1].x + objectAnalysis.object.cols(), cornersScene[1].y), lineColor, 5);
        Drawing.drawLine(output, new Point(cornersScene[1].x + objectAnalysis.object.cols(), cornersScene[1].y),
                new Point(cornersScene[2].x + objectAnalysis.object.cols(), cornersScene[2].y), lineColor, 5);
        Drawing.drawLine(output, new Point(cornersScene[2].x + objectAnalysis.object.cols(), cornersScene[2].y),
                new Point(cornersScene[3].x + objectAnalysis.object.cols(), cornersScene[3].y), lineColor, 5);
        Drawing.drawLine(output, new Point(cornersScene[3].x + objectAnalysis.object.cols(), cornersScene[3].y),
                new Point(cornersScene[0].x + objectAnalysis.object.cols(), cornersScene[0].y), lineColor, 5);
    }

    /**
     * Draw debug info onto screen
     *
     * @param output        Image to draw on
     * @param sceneAnalysis Scene analysis object
     */
    public static void drawDebugInfo(Mat output, SceneAnalysis sceneAnalysis) {
        Transform.flip(output, Transform.FlipType.FLIP_ACROSS_Y);
        Drawing.drawText(output, "Keypoints: " + sceneAnalysis.keypoints.rows(), new Point(0, 8), 1.0f, new ColorRGBA(255, 255, 255), Drawing.Anchor.BOTTOMLEFT_UNFLIPPED_Y);
        Transform.flip(output, Transform.FlipType.FLIP_ACROSS_Y);
    }

    /**
     * Analyzes an object in preparation to search for the object in a frame.
     * <p/>
     * This method should be called in an initialize() method.
     * Calling the analyzeObject method twice will overwrite the previous objectAnalysis.
     * <p/>
     * It is recommended to use a GFTT (Good Features To Track) detector for this phase.
     *
     * @param object Object image
     * @return The object descriptor matrix to be piped into locateObject() later
     */
    public ObjectAnalysis analyzeObject(Mat object) throws IllegalArgumentException {
        Mat descriptors = new Mat();
        MatOfKeyPoint keypoints = new MatOfKeyPoint();

        Log.d("FTCVision", "Analyzing object...");

        if (object == null || object.empty()) {
            throw new IllegalArgumentException("Object image cannot be empty!");
        }

        //Detect object keypoints
        detector.detect(object, keypoints);

        //Extract object keypoints
        extractor.compute(object, keypoints, descriptors);

        return new ObjectAnalysis(keypoints, descriptors, object);
    }

    /**
     * Analyzes a scene for a target object.
     *
     * @param scene    The scene to be analyzed as a GRAYSCALE matrix
     * @param analysis The target object's analysis from analyzeObject
     * @return A complete scene analysis
     */
    public SceneAnalysis analyzeScene(Mat scene, ObjectAnalysis analysis) throws IllegalArgumentException {
        MatOfKeyPoint keypointsScene = new MatOfKeyPoint();

        //DETECT KEYPOINTS in scene
        detector.detect(scene, keypointsScene);

        //EXTRACT KEYPOINT INFO from scene
        Mat descriptorsScene = new Mat();
        extractor.compute(scene, keypointsScene, descriptorsScene);

        if (analysis == null) {
            throw new IllegalArgumentException("Analysis must not be null!");
        }

        if (analysis.descriptors.cols() != descriptorsScene.cols() || analysis.descriptors.type() != descriptorsScene.type()) {
            throw new IllegalArgumentException("Object and scene descriptors do not match in cols() or type().");
        }

        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(analysis.descriptors, descriptorsScene, matches);

        //FILTER KEYPOINTS
         /*double max_dist = 0, min_dist = 100;

        for(int i = 0; i < objectAnalysis.descriptors.rows(); i++) {
            double dist = matches.get;
            if(dist < )
        }*/

        //STORE SCENE ANALYSIS
        return new SceneAnalysis(keypointsScene, descriptorsScene, matches, scene);
    }

    /**
     * Feature detector types
     * <p/>
     * Feature detectors search the images for features - typically corners - that are then
     * extracted and processed to locate an object in a scene.
     */
    public enum FeatureDetectorType {
        FAST(1),
        STAR(2),
        //SIFT(3),
        //SURF(4),
        ORB(5),
        MSER(6),
        GFTT(7),
        HARRIS(8),
        SIMPLEBLOB(9),
        DENSE(10),
        BRISK(11),
        FAST_DYNAMIC(1, true),
        STAR_DYNAMIC(2, true),
        //SIFT_DYNAMIC(3, true),
        //SURF_DYNAMIC(4, true),
        ORB_DYNAMIC(5, true),
        MSER_DYNAMIC(6, true),
        GFTT_DYNAMIC(7, true),
        HARRIS_DYNAMIC(8, true),
        SIMPLEBLOB_DYNAMIC(9, true),
        DENSE_DYNAMIC(10, true),
        BRISK_DYNAMIC(11, true);

        private final int m;

        FeatureDetectorType(int type) {
            m = type;
        }

        FeatureDetectorType(int type, boolean dynamic) {
            m = type + (dynamic ? 3000 : 0);
        }

        public int val() {
            return m;
        }
    }

    /**
     * Descriptor extractor types
     * <p/>
     * Descriptor extractors get information from the feature detector and analyze
     * it in various ways - these descriptors are then stored in the object analysis
     * to later search for the same descriptors in the scene.
     */
    public enum DescriptorExtractorType {
        //SIFT(1),
        //SURF(2),
        ORB(3),
        BRIEF(4),
        BRISK(5),
        FREAK(6),
        //SIFT_OPPONENT(1, true),
        //SURF_OPPONENT(2, true),
        ORB_OPPONENT(3, true),
        BRIEF_OPPONENT(4, true),
        BRISK_OPPONENT(5, true),
        FREAK_OPPONENT(6, true);

        private final int m;

        DescriptorExtractorType(int type) {
            m = type;
        }

        DescriptorExtractorType(int type, boolean opponent) {
            m = type + (opponent ? 1000 : 0);
        }

        public int val() {
            return m;
        }
    }

    /**
     * Descriptor matcher types
     * <p/>
     * Descriptor matchers match descriptors found from the object
     * to descriptors in an image - they effectively locate the object in the scene.
     */
    public enum DescriptorMatcherType {
        FLANN(1),
        BRUTEFORCE(2),
        BRUTEFORCE_L1(3),
        BRUTEFORCE_HAMMING(4),
        BRUTEFORCE_HAMMINGLUT(5),
        BRUTEFORCE_SL2(6);

        private final int m;

        DescriptorMatcherType(int type) {
            m = type;
        }

        public int val() {
            return m;
        }
    }

    /**
     * Object analysis class returned after analyzing an object
     */
    public final class ObjectAnalysis {
        final MatOfKeyPoint keypoints;
        final Mat descriptors;
        final Mat object;

        ObjectAnalysis(MatOfKeyPoint keypoints, Mat descriptors, Mat object) {
            this.keypoints = keypoints;
            this.descriptors = descriptors;
            this.object = object;
        }
    }

    /**
     * Scene analysis class returned after analyzing a scene
     */
    public final class SceneAnalysis {
        final MatOfKeyPoint keypoints;
        final Mat descriptors;
        final MatOfDMatch matches;
        final Mat scene;

        SceneAnalysis(MatOfKeyPoint keypoints, Mat descriptors, MatOfDMatch matches, Mat scene) {
            this.keypoints = keypoints;
            this.descriptors = descriptors;
            this.matches = matches;
            this.scene = scene;
        }
    }


}
