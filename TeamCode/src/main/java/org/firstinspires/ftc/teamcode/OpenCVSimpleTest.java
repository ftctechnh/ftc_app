package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8U;

/**
 * Created by Robotics on 10/28/2016.
 */

@TeleOp(name = "OpenCVSimpleTest", group = "Test")
//@Disabled
public class OpenCVSimpleTest extends OpenCVLib {
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

        float[] ray = new float[(int)src.total()];

        src.get(0, 0, ray);

        int imgRow = src.rows();
        int imgCol = src.cols();

        src.get(0, 0, ray);

        int thresh = 200;

        float totalRow = 0;
        float totalCol = 0;
        long totalPix = 0;

        for(int i = 0; i < imgRow; i++){
            for(int o = 0; o < imgCol; o++){
                if(ray[i * imgCol + o] > thresh){
                    ray[i * imgCol] = 165;
                    totalRow += i;
                    totalCol += o;
                    totalPix++;
                }
                else{
                    ray[i * imgCol + o] = 0;
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

        src.put(0, 0, ray);

        src.convertTo(src, CV_8U);

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

