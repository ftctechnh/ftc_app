package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/**
 * Created by Noah on 4/18/2018.
 */

public abstract class OpenCVLoadLinear extends LinearOpMode {
    public static final String cvTAG = "OpenCVInit";
    //load opencv
    public void initOpenCV() {
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(hardwareMap.appContext) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }
        };

        if (!OpenCVLoader.initDebug()) {
            RobotLog.vv(cvTAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, hardwareMap.appContext, mLoaderCallback);
        } else {
            RobotLog.vv(cvTAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void stopOpenCV() {

    }
}
