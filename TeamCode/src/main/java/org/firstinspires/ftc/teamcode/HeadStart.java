package org.firstinspires.ftc.teamcode;

import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * Created by djfigs1 on 2/7/16.
 */
public class HeadStart extends RobotHardware {

    private long startTime, endTime;
    private boolean firstTime = true, done = false, waiting = true, waitingFirstTime = true;

    private int runTime = 5000;

    @Override
    public void start(){
        /*
        arm_1_position(0.26D);
        arm_2_position(0.54D);
        arm_3_position(0.40D);
        */
    }

    @Override
    public void loop() {
        telemetry.addData("CTM", System.currentTimeMillis());
        telemetry.addData("Done", done);
        if (!done){
            if (waiting){
                if (waitingFirstTime){
                    startTime = System.currentTimeMillis();
                    endTime = startTime + 10000;
                    waitingFirstTime = false;
                }
                waiting = (System.currentTimeMillis() < endTime);
            }else{
                if (firstTime){
                    startTime = System.currentTimeMillis();
                    endTime = startTime + runTime;
                    firstTime = false;
                }
                if (System.currentTimeMillis() < endTime){
                    set_drive_power(0.25f, 0.25f);
                }else{
                    done = true;
                }
            }
        }else{
            set_drive_power(0f,0f);
        }
    }

}
