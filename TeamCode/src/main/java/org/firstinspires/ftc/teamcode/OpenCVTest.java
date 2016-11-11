package org.firstinspires.ftc.teamcode;

import android.os.Environment;
import android.view.SurfaceView;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.core.CvType.*;

/**
 * Created by Robotics on 10/28/2016.
 */

@TeleOp(name = "OpenCVTest", group = "Test")
//@Disabled
public class OpenCVTest extends OpenCVLib {
    //edge kernel (stolen from http://roboreal.com)
    private static final float[][] kernel = {{-1, -1, -1, -1, -1},
            {-1,  0,  0,  0, -1},
            {-1,  0, 16,  0, -1},
            {-1,  0,  0,  0, -1},
            {-1, -1, -1, -1, -1}};

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        //here we gooooooooo!
        //my not very simple edge detection algorithm!
        Mat src = frame.gray();

        src.convertTo(src, CV_32F);

        //Mat rotation = Imgproc.getRotationMatrix2D(new Point(src.cols()/2, src.rows()/2), 90, 1);

        //Imgproc.warpAffine(src, src, rotation, new Size(src.cols(), src.rows()));

        Imgproc.GaussianBlur(src, src, new Size(7,7), 0);

        Mat edgeKernel = new Mat(5, 5, CV_32F);
        for (int i=0; i<kernel.length; i++)
            edgeKernel.put(i,0, kernel[i]);

        Imgproc.filter2D(src, src, -1, edgeKernel);

        int size = (int) (src.total() * src.channels());

        float[] buff = new float[size];
        int[][] ray = new int[src.rows()][src.cols()];

        int imgRow = src.rows();
        int imgCol = src.cols();

        src.get(0, 0, buff);

        int thresh = 100;

        for(int i = 0; i < imgRow; i++){
            for(int o = 0; o < imgCol; o++){
                if(buff[i * imgCol + o] > thresh){
                    ray[i][o] = 1;
                }
                else{
                    ray[i][o] = 0;
                }
            }
        }

        ThinningService thinThing = new ThinningService();

        thinThing.doZhangSuenThinning(ray, true);

        float totalRow = 0;
        float totalCol = 0;
        long totalPix = 0;

        for(int i = 0; i < imgRow; i++) {
            for (int o = 0; o < imgCol; o++) {
                if(ray[i][o] == 0 || (o < 50 || o > imgCol - 50)) buff[i * imgCol + o] = 0;
                else{
                    buff[i * imgCol + o] = 165.0f;
                    totalRow += i;
                    totalCol += o;
                    totalPix++;
                }
            }
        }

        if(totalPix != 0){
            totalCol /= totalPix;
            totalRow /= totalPix;
        }
        else{
            totalCol = imgCol/2.0f;
            totalRow = imgRow/2.0f;
        }

        src.put(0, 0, buff);

        src.convertTo(src, CvType.CV_8U);

        Imgproc.circle(src, new Point((int)totalCol, (int)totalRow), 4, new Scalar(255,0,0));

        Imgproc.circle(src, new Point(imgCol/2, imgRow/2), 10, new Scalar(255,0,0));

        return src;
    }

    @Override
    public void init(){
        initOpenCV();
    }

    @Override
    public void start(){
        startCamera();
    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){
        stopCamera();
    }

}

class ThinningService {
    /**
     * @param givenImage
     * @param changeGivenImage decides whether the givenArray should be modified or a clone should be used
     * @return a 2D array of binary image after thinning using zhang-suen thinning algo.
     */
    public int[][] doZhangSuenThinning(final int[][] givenImage, boolean changeGivenImage) {
        int[][] binaryImage;
        if (changeGivenImage) {
            binaryImage = givenImage;
        } else {
            binaryImage = givenImage.clone();
        }
        int a, b;
        List<mPoint> pointsToChange = new LinkedList();
        boolean hasChange;
        do {
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new mPoint(x, y));
//binaryImage[y][x] = 0;
                        hasChange = true;
                    }
                }
            }
            for (mPoint point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new mPoint(x, y));
                        hasChange = true;
                    }
                }
            }
            for (mPoint point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
        } while (hasChange);
        return binaryImage;
    }

    private int getA(int[][] binaryImage, int y, int x) {
        int count = 0;
//p2 p3
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
            count++;
        }
//p3 p4
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
            count++;
        }
//p4 p5
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
            count++;
        }
//p5 p6
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
            count++;
        }
//p6 p7
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
            count++;
        }
//p7 p8
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
            count++;
        }
//p8 p9
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
            count++;
        }
//p9 p2
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
            count++;
        }
        return count;
    }

    private int getB(int[][] binaryImage, int y, int x) {
        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
                + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
    }
}

/**
 * Created by nayef on 1/27/15.
 */
class mPoint {
    private int x;
    private int y;

    public mPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
