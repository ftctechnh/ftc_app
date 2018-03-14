package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.vuforia.PIXEL_FORMAT;
import com.android.internal.util.Predicate;
import com.vuforia.ar.pl.ImageTools;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;


import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/**
 * Created by boswo on 3/13/2018.
 */
@Autonomous(name="Test for Phone Jewel")
public class Test_For_Phone_Jewel extends Autonomous_General_George  {
    @Override
    public void runOpMode() throws InterruptedException{

      vuforiaInit1(true,false);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        while(true) {
            Bitmap img = getImage();
            telemetry.addLine("have Bitmap");
            telemetry.update();
            int color = Analyze_Color(img, 0);
            telemetry.addData("Red=0&Blue=1&None=2", color);
            telemetry.update();
            sleep(250);
        }


    }


    public Bitmap getImage() throws InterruptedException {
        vuforiaInit(true,false);
        vuforia.setFrameQueueCapacity(1);
        com.vuforia.Image img;
        telemetry.addLine("Getting Image");
        telemetry.update();
        img = getImagefromFrame(vuforia.getFrameQueue().take(), PIXEL_FORMAT.RGB565);
        Bitmap bm_img = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
        bm_img.copyPixelsFromBuffer(img.getPixels());
        telemetry.addLine("Got Image");
        telemetry.update();
        return bm_img;
    }

    @Nullable
    private com.vuforia.Image getImagefromFrame(VuforiaLocalizer.CloseableFrame frame, int format) {
        telemetry.addLine("In get Image from Frame");
        telemetry.update();
        long numImgs = frame.getNumImages();
        for (int i = 0; i < numImgs; i++) {
            if (frame.getImage(1).getFormat() == format) {
                return frame.getImage(1);
            }
        }

        return null;
    }

    public int Analyze_Color(Bitmap bm_img, int pix) {
        int RED_COUNT = 0, BLUE_COUNT = 0, Xl = 0, X = 0;
        Color cur_color = null;
        int cur_color_int, rgb[] = new int[3];
        float hsv[] = new float[3];
        int hueMax = 0;
telemetry.addLine("Begin analyze");
telemetry.update();
        int width = bm_img.getWidth();
        int height = bm_img.getHeight();

        for (int i = 500; i < height; i += 3) {
            for (int j = pix; j < width; j += 3) {
                cur_color_int = bm_img.getPixel(j, i);
                rgb[0] = cur_color.red(cur_color_int);
                rgb[1] = cur_color.green(cur_color_int);
                rgb[2] = cur_color.blue(cur_color_int);

                Color.RGBToHSV(rgb[0], rgb[1], rgb[2], hsv);

                hueMax = Math.max((int) hsv[0], hueMax);

                if (hsv[0] < 15 && j > pix && hsv[0] > 0) {
                    RED_COUNT++;
                    X += j;
                }
                else if (((hsv[0]>180)&&(hsv[0]<210))   && j>pix){
                    BLUE_COUNT++;
                    Xl += j;
                }
            }
            if (RED_COUNT > 0 && BLUE_COUNT > 0) {
                X /= RED_COUNT;
                Xl /= BLUE_COUNT;
            }
        }
        telemetry.addLine("Analyzed color");
        telemetry.update();
        if (X>Xl){//red
            return 0;
        }
        else if (Xl>X){//blue
            return 1;
        }
        else {
            return 2;
        }
    }
    public void vuforiaInit1(boolean cameraView, boolean rearCamera){

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters;

            parameters = new VuforiaLocalizer.Parameters();


        parameters.vuforiaLicenseKey = "AffveYv/////AAAAGQ5VbB9zQUgjlHWrneVac2MnNgfMDlq6EwI3tyURgRK6CHargOTFidfzKod6GLQwGD4m9MPLkR+0NfUrnY8+o8FqAKc" +
                "QbrAsjk8ONdkWYTPZDfoBRgDLNWRuB7LU1MOp9KqAWpXBJjvH5JCKF/Hxz+beHfVqdWQ0BVZdgGMXG4yEzLN5AI+4NIkQeLvI7Cwz5pIlksoH+rb/e6+YExoWZbQWhDTiR" +
                "iemlWjvDM1z2a0kteGDz0wTyHz48IkV4M0YsSQIFKwu3YB2a1vkB9FiRfMrBI+CyInjgNoO8V0EEOtRc6Vqsf3XbF3fGXricZUhl7RIl5M/IkFOgeAZ4ML+JcrjTqfZb2Yh3JNx1me524cK";


        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
    }
}

