package org.firstinspires.ftc.teamcode;

import android.os.Environment;
import android.view.SurfaceView;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;

/**
 * Created by Robotics on 10/28/2016.
 */

@TeleOp(name = "OpenCVTest", group = "Test")
//@Disabled
public class OpenCVTest extends OpMode {


    @Override
    public void init() {
        FtcRobotControllerActivity.startCamera();
        telemetry.addData("Camera", "Started");
    }

    @Override
    public void start() {
        Mat temp = FtcRobotControllerActivity.getCameraMat();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(path, "save.png");

        Imgcodecs.imwrite(image.toString(), temp);
        telemetry.addData("Camera grab", "Worked!");
    }

    @Override
    public void loop() {
    }

    @Override
    public void stop(){
        FtcRobotControllerActivity.stopCamera();
    }

}
