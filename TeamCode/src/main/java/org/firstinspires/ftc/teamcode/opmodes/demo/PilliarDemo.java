package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.graphics.Bitmap;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.libraries.PilliarLib;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Noah on 12/20/2017.
 */

@Autonomous(name="Pilliar Demo", group="test")
public class PilliarDemo extends PilliarLib {
    private boolean red = true;
    private boolean lastA = false;

    public void init(){
        initVuforia(true);
    }

    public void init_loop() {
        if(lastA && !gamepad1.a) red = !red;
        lastA = gamepad1.a;
        telemetry.addData("Red", red);

        getPeaks(red);
    }

    public void start() {
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/pil" + getRuntime() + ".png");
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);

        }
        catch(Exception e) {
            //oops
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loop() {
        getPeaks(red);
    }

}
