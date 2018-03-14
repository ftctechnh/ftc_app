package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import com.vuforia.PIXEL_FORMAT;
import com.android.internal.util.Predicate;
import com.vuforia.ar.pl.ImageTools;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by boswo on 3/12/2018.
 */

public class blueBack_WithVuJewel extends Autonomous_General_George {
    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {


        vuforiaInit(true, true);
        //intiates the vuforia sdk and camera
        telemetry.addData("", "Vuforia Initiated");
        telemetry.update();
        //tell driver that vuforia is ready
        initiate(false);
        //intiate hardware
        sleep(500);
        telemetry.addData("", "GOOD TO GO! :)");
        telemetry.update();
        //tell driver that we are good to go

        waitForStart();
//reseting gyro sensor

        jewelServoRotate.setPosition(0.74);
        sleep(100);
        toggleLight(true);
        //light.setPower(0.5);
        startTracking();
        telemetry.addData("", "READY TO TRACK");
        telemetry.update();

        double begintime = runtime.seconds();
        while (!vuMarkFound() && runtime.seconds() - begintime <= waitTime) {


        }
        toggleLight(false);

        telemetry.addData("Vumark", vuMark);
        telemetry.update();
        sleep(250);

        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator
        sleep(250);
        middleGlyphManipulator();
        sleep(250);
        moveDownGlyph(1.45);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.45);
        sleep(250);

        Bitmap img =getImage();

        Analyze_Color(img,img.getPixel(img.getWidth(), img.getHeight()) );

        sleep(1000);


        jewelServo.setPosition(1);
        sleep(700);
        encoderMecanumDrive(0.4, 50, 50, 5000, 0);
        sleep(100);
        gyroTurnREV(0.4, 0);
        sleep(100);


        if (vuMark == RelicRecoveryVuMark.LEFT) {
            encoderMecanumDrive(0.4, 4.25, 4.25, 5000, 0);
        } else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN) {
            encoderMecanumDrive(0.4, -4, -4, 5000, 0);

        } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
            encoderMecanumDrive(0.4, 9.25, 9.25, 5000, 0);
        }


        sleep(100);

        if (vuMark == RelicRecoveryVuMark.LEFT) {
            gyroTurnREV(0.5, 112);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        } else {
            gyroTurnREV(0.5, 60);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall
        }


        sleep(750);

        moveDownGlyph(1.05);
        sleep(100);
        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);*/
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3, 16, 16, 1000, 0);
        sleep(250);

        if (vuMark == RelicRecoveryVuMark.LEFT) {
            encoderMecanumDrive(0.3, -10, 10, 1000, 0);

        } else {
            encoderMecanumDrive(0.3, 10, -10, 1000, 0);
        }

        sleep(500);
        encoderMecanumDrive(0.3, -10, -10, 1000, 0);
    }


    public Bitmap getImage() throws InterruptedException {
        com.vuforia.Image img;
        img = getImagefromFrame(vuforia.getFrameQueue().take(), PIXEL_FORMAT.RGB565);
        Bitmap bm_img = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
        bm_img.copyPixelsFromBuffer(img.getPixels());

        return bm_img;
    }

    @Nullable
    private com.vuforia.Image getImagefromFrame(VuforiaLocalizer.CloseableFrame frame, int format) {
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

        int width = bm_img.getWidth();
        int height = bm_img.getHeight();

        for (int i = 500; i < height; i += 3) {
            for (int j = pix; j < width; j += 3) {
                cur_color_int = bm_img.getPixel(j, i);
                rgb[0] = cur_color.red(cur_color_int);
                rgb[1] = cur_color.green(cur_color_int);
                rgb[3] = cur_color.blue(cur_color_int);

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
}
