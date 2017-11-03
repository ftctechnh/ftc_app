package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.CameraCalibration;
import com.vuforia.Vec3F;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.libraries.VuforiaProjectLib;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Noah on 11/3/2017.
 */


@Autonomous(name="Concept: Vuforia Simple Projection", group ="Concept")
public class ProjectOpenCV extends VuforiaProjectLib {

    //define the jewel platform relative to the image with a buncha vectors to add
    //all units in vuforia are mm, so we multiply to inches to make it readable
    private static final float inToMM = 25.4f;

    //units in inches, then converted to mm
    private static final float boxLeftFromImage = 3.625f * inToMM;
    private static final float boxBottomFromImage = 5.125f * inToMM;
    private static final float boxWidth = 9.75f * inToMM;
    private static final float boxLength = 3.75f * inToMM;
    private static final float boxHeight = 3.75f * inToMM;
    private static final float ballCenterFromBoxEdge = 1.875f * inToMM;

    private float size[];
    private Vec3F points[];
    private Point[] out;
    private boolean isCorrected = false;

    private int leftDist;
    private int[] leftBall;
    private int rightDist;
    private int[] rightBall;

    private int leftIsRed;

    @Override
    public void init() {
        size = initVuforia().getSize().getData();

        size[0] /= 2;
        size[1] /= 2;

        //construct after vuforia has loaded it's crap
        points = new Vec3F[] {
                new Vec3F(boxLeftFromImage + ballCenterFromBoxEdge, -boxBottomFromImage + boxHeight, ballCenterFromBoxEdge), //top of left ball
                new Vec3F(boxLeftFromImage + ballCenterFromBoxEdge, -boxBottomFromImage, ballCenterFromBoxEdge), //bottom of left ball
                new Vec3F(boxLeftFromImage + boxWidth - ballCenterFromBoxEdge, -boxBottomFromImage + boxHeight, ballCenterFromBoxEdge), //top of right ball
                new Vec3F(boxLeftFromImage + boxWidth - ballCenterFromBoxEdge, -boxBottomFromImage, ballCenterFromBoxEdge), //bottom of right ball
        };

        startVuforia();
    }

    public void init_loop() {
        if(isTracking() != RelicRecoveryVuMark.UNKNOWN) out = getProjectedPoints(points);
        telemetry.addData("Tracking", isTracking().toString());
    }

    public void start() {
        stopVuforia();
        initOpenCV();
        startCamera();
    }

    public void loop() {
        telemetry.addData("Left is Red", leftIsRed);
    }

    public void stop() {
        stopCamera();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Mat currentFrame = frame.rgba();

        //if we haven't figured out how to scale the size, do that
        if(!isCorrected){
            //find the offset to the sqaure of pixels vuforia looks at
            int xOffset = (int)((currentFrame.cols() - size[0]) / 2.0);
            int yOffset = (int)((currentFrame.rows() - size[1]) / 2.0);

            //add the offset to all points calculated for openCV
            for(Point point : out) {
                point.x += xOffset;
                point.y += yOffset;
            }

            //run calculations based on projected points
            //find the midpoint between the two points
            int leftXPoint = (int)((out[0].x + out[1].x) / 2.0);
            int leftYPoint = (int)((out[0].y + out[1].y) / 2.0);
            //find the y distande between the two
            leftDist = (int)(Math.abs(out[0].y - out[1].y) / 2.0);
            leftBall = new int[] {leftXPoint - (leftDist / 2), leftYPoint - (leftDist / 2)};

            //find the midpoint between the two points
            int rightXPoint = (int)((out[2].x + out[3].x) / 2.0);
            int rightYPoint = (int)((out[2].y + out[3].y) / 2.0);
            //find the y distande between the two
            rightDist = (int)(Math.abs(out[2].y - out[3].y) / 2.0);
            rightBall = new int[] {rightXPoint - (rightDist / 2), rightYPoint - (rightDist / 2)};

            isCorrected = true;
        }

        //operation: subsquare
        //take a square mat we are 100% sure will have a ball in it
        //sum it up and find the average color

        Scalar left = drawSquare(currentFrame, leftBall, leftDist);
        Scalar right = drawSquare(currentFrame, rightBall, rightDist);

        Imgproc.drawMarker(currentFrame, out[0], new Scalar(0, 255, 0));

        if(left != null && right != null) {
            if(left.val[0] < left.val[1] && right.val[0] > right.val[1]) leftIsRed = 0;
            else if (left.val[0] > left.val[1] && right.val[0] < right.val[1]) leftIsRed = 1;
            else leftIsRed = -1;
        }
        else leftIsRed = -1;

        //flip it for display
        Core.flip(currentFrame, currentFrame, -1);

        return currentFrame;
    }

    private static Scalar drawSquare(Mat src, int[] ballPoint, int ballDist) {
        //find average left and right ball square
        //find the average color for all the pixels in that square
        if(ballPoint[0] >= 0 && ballPoint[1] >= 0 && ballPoint[0] + ballDist < src.cols() && ballPoint[1] + ballDist < src.rows()){
            double total[] = new double[3];
            for(int x = 0; x < ballDist; x++)
                for(int y = 0; y < ballDist; y++) {
                    double[] pixel = src.get(y + ballPoint[1], x + ballPoint[0]);
                    total[0] += pixel[0];
                    total[1] += pixel[1];
                    total[2] += pixel[2];
                }

            //make average color
            Scalar color = new Scalar(total[0] / (ballDist * ballDist), total[1] / (ballDist * ballDist), total[2] / (ballDist * ballDist));

            Imgproc.rectangle(src, new Point(ballPoint[0], ballPoint[1]), new Point(ballPoint[0] + ballDist, ballPoint[1] + ballDist), color, -1);

            return color;
        }
        return null;
    }
}
