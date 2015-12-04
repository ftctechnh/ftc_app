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

    public void init() {
        setupHardware();
    }

    public void loop() {
        switch (state) {
            case -1:
                timer.reset();
                state = 1;
                break;
            case 1:
                //This is the finished state.
                telemetry.addData("State","FINISHED");
                break;
        }
    }
}
