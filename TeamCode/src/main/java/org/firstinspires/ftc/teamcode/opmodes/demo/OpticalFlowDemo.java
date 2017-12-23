package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.MeccanumVelocityLib;
import org.firstinspires.ftc.teamcode.libraries.OpenCVLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.HeadingSensor;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;
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
@Disabled
public class OpticalFlowDemo extends OpenCVLib {

    private int yVal;
    private Mat lastMat;
    private Mat lastMatGrey;
    //private boolean firstCall = true;

    private double xPos = 0;
    private double yPos = 0;

    boolean bDone = false;

    // parameters of the PID controller for this sequence's first part
    float Kp = 0.035f;        // degree heading proportional term correction per degree of deviation
    float Ki = 0.02f;         // ... integrator term
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    // parameters of the PID controller for the heading of the line following
    float Kp2 = 0.125f;
    float Ki2 = 0.000f;
    float Kd2 = 0;
    float Ki2Cutoff = 0.0f;

    private double headingVelocity = 0;

    private CameraVelocity sense = new CameraVelocity();

    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    private SensorLib.PID mPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);

    private SensorLib.PID vPid = new SensorLib.PID(Kp2, Ki2, Kd2, Ki2Cutoff);

    private BotHardwareOld bot;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame){
        Mat ret = frame.rgba();
        Mat grey = frame.gray();

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
                    headingVelocity = SensorLib.Utils.wrapAngle(Math.toDegrees(Math.atan2(avgy, avgx)) - 90);
                    telemetry.addData("Heading", headingVelocity);
                }
            }
        }


        lastMat = ret.submat(ret.rows() / 4, (ret.rows() / 4) * 3, ret.cols() / 4, (ret.cols() / 4) * 3).clone();
        lastMatGrey = grey.submat(ret.rows() / 4, (ret.rows() / 4) * 3, ret.cols() / 4, (ret.cols() / 4) * 3).clone();

        //if(frame != null) firstCall = false;

        return ret;
    }

    private class CameraVelocity implements HeadingSensor {

        private FilterLib.MovingWindowAngleFilter filter = new FilterLib.MovingWindowAngleFilter();

        public float getHeading(){
            filter.appendValue(headingVelocity);
            return (float)filter.currentValue();
        }

    }

    @Override
    public void init(){
        initOpenCV();

        lastMat = new Mat();
        lastMatGrey = new Mat();

        bot = new BotHardwareOld();
        bot.init(this, false);

        bot.startNavX();

        mSeq.add(new MeccanumVelocityLib.SquirrleyAzimuthTimedAngledDriveStep(this, 45, bot.getNavXHeadingSensor(), sense, mPid, vPid, bot.getMotorArray(), 0.2f, 10.0f, true));
        mSeq.add(new MeccanumVelocityLib.SquirrleyAzimuthTimedAngledDriveStep(this, -135, bot.getNavXHeadingSensor(), sense, mPid, vPid, bot.getMotorArray(), 0.2f, 10.0f, true));
    }

    @Override
    public void start(){
        startCamera();
    }

    @Override
    public void loop(){
        if(!bDone) bDone = mSeq.loop();
        else telemetry.addData("Seq", "Done");
    }

    @Override
    public void stop(){
        stopCamera();
        lastMat.release();
        bot.navX.close();
    }
}