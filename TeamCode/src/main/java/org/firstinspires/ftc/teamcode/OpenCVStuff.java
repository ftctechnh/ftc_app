package org.firstinspires.ftc.teamcode;

import android.provider.ContactsContract;

import com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.video.DenseOpticalFlow;
import org.opencv.video.Video;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.flip;
import static org.opencv.core.CvType.*;
import static org.opencv.video.Video.createOptFlow_DualTVL1;
import static org.opencv.video.Video.findTransformECC;

/**
 * Created by Robotics on 10/28/2016.
 */

@Autonomous(name = "OpenCV Testing", group = "Line Follow")
//@Disabled
public class OpenCVStuff extends OpenCVLib {

    private int yVal;
    private Mat lastMat;
    private boolean firstCall = true;

    private BotHardware robot = new BotHardware();

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        Mat thing = frame.gray();


        MatOfPoint prev_corner = new MatOfPoint();
        MatOfPoint2f corner = new MatOfPoint2f();
        MatOfByte status = new MatOfByte();
        MatOfFloat err = new MatOfFloat();

        if(!firstCall) {

            Imgproc.goodFeaturesToTrack(lastMat, prev_corner, 30, 0.25, 30);

            RobotLog.vv(cvTAG, prev_corner.dump());

            //FeatureDetector feat = FeatureDetector.create(FeatureDetector.FAST);

           // MatOfKeyPoint keypoits = new MatOfKeyPoint();

            //feat.detect(lastMat, keypoits);

           // RobotLog.vv(cvTAG, keypoits.dump());

            if(!prev_corner.empty()){
                Video.calcOpticalFlowPyrLK(lastMat, thing, new MatOfPoint2f(prev_corner.toArray()), corner, status, err);

                byte[] statusRay = status.toArray();

                List<Point> prevRay = prev_corner.toList();

                List<Point> ray = corner.toList();

                float avgx = 0;
                float avgy = 0;
                int totalNum = 0;

                for(int i=0; i<ray.size(); i++){
                    if(statusRay[i] == 1){
                        avgx += ray.get(i).x - prevRay.get(i).x;
                        avgy += ray.get(i).y - prevRay.get(i).y;
                        totalNum++;
                    }
                }

                avgx /= (float)totalNum;
                avgy /= (float)totalNum;

                /*
                // weed out bad matches
                for(int i=0; i < statusRay.length; i++) {
                    if(statusRay[i] == 0) {
                        final int offset = statusRay.length - prevRay.size();
                        prevRay.remove(i - offset);
                    }
                }
                */



                //Mat trans = Converters.vector_Point_to_Mat(prevRay);

                //Video.estimateRigidTransform(Converters.vector_Point_to_Mat(prevRay), trans, false);

                //telemetry.addData("Status", status.dump());
                telemetry.addData("Transformation", avgx + ", " + avgy);
                double heading = Math.atan2(avgx, avgy);
                telemetry.addData("Heading", heading);
            }
        }

        lastMat = thing.clone();

        if(frame != null) firstCall = false;

        return thing;
    }

    @Override
    public void init(){
        robot.init(this, true);

        initOpenCV();

        lastMat = new Mat();
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