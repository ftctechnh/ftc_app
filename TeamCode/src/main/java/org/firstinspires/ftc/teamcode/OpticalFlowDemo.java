package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.util.List;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "Optical Flow 1", group = "Line Follow")
//@Disabled
public class OpticalFlowDemo extends OpenCVLib {

    private int yVal;
    private Mat lastMat;
    private Mat lastMatGrey;
    private boolean firstCall = true;

    private double xPos = 0;
    private double yPos = 0;

    FilterLib.MovingWindowAngleFilter filter = new FilterLib.MovingWindowAngleFilter();

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        Mat ret = frame.rgba();
        Mat grey = frame.gray();

        if(!firstCall) {
            //submat the middle quarter to save proc power
            Mat thing = ret.submat(ret.rows() / 4, (ret.rows() / 4) * 3, ret.cols() / 4, (ret.cols() / 4) * 3).clone();

            MatOfPoint prev_corner = new MatOfPoint();
            MatOfPoint2f corner = new MatOfPoint2f();
            MatOfByte status = new MatOfByte();
            MatOfFloat err = new MatOfFloat();

            Imgproc.goodFeaturesToTrack(lastMatGrey, prev_corner, 30, 0.25, 30);

            //RobotLog.vv(cvTAG, prev_corner.dump());

            if(!prev_corner.empty()){
                Video.calcOpticalFlowPyrLK(lastMat, thing, new MatOfPoint2f(prev_corner.toArray()), corner, status, err);

                byte[] statusRay = status.toArray();

                List<Point> prevRay = prev_corner.toList();

                List<Point> ray = corner.toList();

                double avgx = 0;
                double avgy = 0;
                int totalNum = 0;

                for(int i=0; i<ray.size(); i++){
                    if(statusRay[i] == 1){
                        avgx += ray.get(i).x - prevRay.get(i).x;
                        avgy += ray.get(i).y - prevRay.get(i).y;
                        totalNum++;
                    }
                }

                if(totalNum > 0){
                    avgx /= (double) totalNum;
                    avgy /= (double) totalNum;

                    xPos += avgx;
                    yPos += avgy;

                    telemetry.addData("Position", xPos + ", " + yPos);
                    if(Math.abs(avgx) > 1.2 || Math.abs(avgy) > 1.2){
                        double heading = SensorLib.Utils.wrapAngle(Math.toDegrees(Math.atan2(avgy, avgx)));
                        filter.appendValue(heading);
                        telemetry.addData("Heading", filter.currentValue());
                    }
                }
            }
        }

        lastMat = ret.submat(ret.rows() / 4, (ret.rows() / 4) * 3, ret.cols() / 4, (ret.cols() / 4) * 3).clone();
        lastMatGrey = grey.submat(ret.rows() / 4, (ret.rows() / 4) * 3, ret.cols() / 4, (ret.cols() / 4) * 3).clone();

        if(frame != null) firstCall = false;

        return ret;
    }

    @Override
    public void init(){
        initOpenCV();

        lastMat = new Mat();
        lastMatGrey = new Mat();
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
        lastMat.release();
    }
}