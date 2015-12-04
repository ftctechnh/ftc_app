package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by tdoylend on 2015-12-04.
 *
 * Changelog:
 *
 */
public class RedBeaconFSM extends PacmanBotHardwareBase {

    ElapsedTime timer = new ElapsedTime();
    int state = -1;
    int redStart = 0;
    int redEnd = 0;
    int redMid = 0;

    boolean colorArray[];

    public void init() {
        setupHardware();
    }

    public void loop() {
        switch (state) {
            case -1:
                timer.reset();
                state = 0;
                break;
            case 0:
                drive(1,0);
                if (timer.time()>=1.5) {state=1; timer.reset();}
                break;
            case 1:
                drive(0,-1);
                if (timer.time()>=0.29) {state=2; timer.reset();}
                break;
            case 2:
                drive(1,0);
                if (timer.time()>=2.75) {state=3; timer.reset();}
                break;
            case 3:
                drive(0,-1);
                if (timer.time()>=0.28) {state=4; timer.reset();}
                break;
            case 4:
                drive(0.25,0);
                if (timer.time()>=2.85) {state=5; timer.reset();}
                break;
            case 5:
                setThrower(true);
                drive(0,0);
                if (timer.time()>=2) {setThrower(false); state=6; timer.reset();}
                break;
            case 6:
                if (timer.time()>=1) {state=7; timer.reset();}
                break;
            case 7:
                setArm(timer.time()-1);
                colorArray[(int)(timer.time()*50)] = (getEyeColor()==ColorDetected.COLOR_RED);
                if (timer.time()>=1) {state=8; timer.reset();}
                break;
            case 8:
                boolean startDetect=false;
                for (int i=0;i<100;i++) {
                    if (!startDetect && colorArray[i]) {
                        redStart=i;
                        startDetect=true;
                    }
                    if (startDetect && !colorArray[i]) {
                        redEnd = i;
                        break;
                    }
                }
                redMid = (redEnd - redStart)/2  +  redStart;
                state=100; break;
            case 100:
                //This is the finished state.
                drive(0,0);
                setThrower(true);
                telemetry.addData("State","FINISHED");
                telemetry.addData("Red Middle is",redMid);
                telemetry.addData("That's", ((double)redMid)*200+1);
                break;
        }
    }
}
