/**
 * CameraTestOp - a simple operating mode that tries to acquire an image from the
 * phone camera and get some data from the image
 * Created by phanau on 12/9/15.
 */

package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.*;

import android.hardware.Camera;

import org.firstinspires.ftc.teamcode.libraries.CameraLib;


@Autonomous(name="Test: CameraLib Test 1", group ="Test")
@Disabled
public class CameraTestOp extends OpMode {

    int mLoopCount;
    CameraLib.CameraAcquireFrames mCamAcqFr;


    // Constructor
    public CameraTestOp() {
        mCamAcqFr = new CameraLib.CameraAcquireFrames();
    }

    @Override
    public void init() {
        mLoopCount = 0;

        if (mCamAcqFr.init(2) == false)     // init camera at 2nd smallest size
            telemetry.addData("error: ", "cannot initialize camera");

    }

    public void loop() {
        // post some debug data
        telemetry.addData("loop count:", mLoopCount++);
        telemetry.addData("version: ", "1.3");

        int[] topScan;
        int[] middleScan;
        int[] bottomScan;

        // get most recent frame from camera (may be same as last time or null)
        CameraLib.CameraImage frame = mCamAcqFr.loop();

        // log debug info ...
        if (frame != null) {

            // process the current frame
            // ... "move toward the light..."

            // log data about the most current image to driver station every loop so it stays up long enough to read
            Camera.Size camSize = frame.cameraSize();
            telemetry.addData("preview camera size: ", String.valueOf(camSize.width) + "x" + String.valueOf(camSize.height));
            telemetry.addData("preview data size:", frame.dataSize());
            telemetry.addData("preview rgb(center):", String.format("%08X", frame.getPixel(camSize.width / 2, camSize.height / 2)));
            telemetry.addData("frame number: ", mCamAcqFr.frameCount());

            // log text representations of several significant scanlines

            topScan = frame.scanlineValue(camSize.height / 3);
            middleScan = frame.scanlineValue(camSize.height / 2);
            bottomScan = frame.scanlineValue(camSize.height * 2 / 3);

            //telemetry.addData("value a(1/3): ", topScan);
            //telemetry.addData("value b(1/2): ", middleScan);
            //telemetry.addData("value c(2/3): ", bottomScan);

            final int bandSize = 10;
            telemetry.addData("hue a(1/3): ", frame.scanlineHue(camSize.height / 3, bandSize));
            telemetry.addData("hue b(1/2): ", frame.scanlineHue(camSize.height / 2, bandSize));
            telemetry.addData("hue c(2/3): ", frame.scanlineHue(2*camSize.height / 3, bandSize));

            int topThresh = threshFind(topScan);
            int middleThresh = threshFind(middleScan);
            int bottomThresh = threshFind(bottomScan);

            telemetry.addData("Top Thresh", topThresh);
            telemetry.addData("Middle Thresh", middleThresh);
            telemetry.addData("Bottom Thresh", bottomThresh);

            int topPos = findAvgOverThresh(topScan, topThresh);
            int middlePos = findAvgOverThresh(middleScan, middleThresh);
            int bottomPos = findAvgOverThresh(bottomScan, bottomThresh);

            telemetry.addData("Top Pos", topPos);
            telemetry.addData("Middle Pos", middlePos);
            telemetry.addData("Bottom Pos", bottomPos);
        }
    }

    public void stop() {
        mCamAcqFr.stop();
    }

    //takes a n array of bytes, and returns (min + max)/2 for a threshold
    private int threshFind(int[] ray){
        int min = 0;
        int max = 0;
        for(int i = 0; i < ray.length; i++){
            if(min > ray[i]) min = ray[i];
            else if(max < ray[i]) max = ray[i];
        }

        return (min + max) / 2;
    }

    private int findAvgOverThresh(int[] ray, int thresh){
        int totalPos = 0;
        int totalNum = 0;
        for(int i = 0; i < ray.length; i++){
            if(ray[i] >= thresh){
                totalPos += i;
                totalNum++;
            }
        }

        return totalPos/totalNum;
    }


}

