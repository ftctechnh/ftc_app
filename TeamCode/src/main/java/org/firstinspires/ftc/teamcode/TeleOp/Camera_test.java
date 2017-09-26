package org.firstinspires.ftc.teamcode.TeleOp;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot.TestBotHardware;


/**
 * Created by gallagherb20503 on 9/23/2017.
 */
@TeleOp(name = "Testcamera", group = "TeleOp")
public class Camera_test extends OpMode {
    TestBotHardware testBot = new TestBotHardware();
    HardwareMap hwMap;
    Camera camera;
    SurfaceView preview;
    Context appContext;

    @Override

    public void init() {
        appContext = hwMap.appContext;

        testBot.init(hardwareMap);
        hwMap = hardwareMap;
        preview = new SurfaceView(appContext);
    }

    public Camera openCamera(int cameraInfoType) {
        int cameraId = -1;
        Camera cam = null;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraInfoType) { // Camera.CameraInfo.CAMERA_FACING_FRONT or BACK
                cameraId = i;
                break;
            }
        }
        camera = Camera.open(cameraId);




    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the back facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }



    @Override
    public void loop() {

    }
}
