package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.vuforia;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;

public class VuforiaTest {
    Vuforia vuforia;

    public VuforiaTest() {
        vuforia = new Vuforia(true);
    }

    public void getPixel() {
        Bitmap image = vuforia.getImage();
        if (image != null) {
            int[][][] pixels = new int[image.getWidth()][image.getHeight()][4];
            /*for (int w = 0; w < image.getWidth(); w++) {
                for (int h = 0; h < image.getHeight(); h++) {
                    pixels[w][h][0] = Color.red(image.getPixel(w, h));
                    pixels[w][h][1] = Color.green(image.getPixel(w, h));
                    pixels[w][h][2] = Color.blue(image.getPixel(w, h));
                    pixels[w][h][3] = Color.alpha(image.getPixel(w, h));
                }
            }*/
            AbstractOpMode.getTelemetry().addData(Color.red(image.getPixel(100, 100)));
        } else {
            AbstractOpMode.getTelemetry().addData("Image is null");
        }
    }
}
