package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by sathk_000 on 12/3/2015.
 */
public class RedBeaconTMD extends PacmanBotHardwareBase {
    ElapsedTime timer = new ElapsedTime();
    boolean set = false ;
    boolean charge = false;

    public void init() {
        setupHardware();
    }

    public void loop() {
        if(!set){
            set=true;
            timer.reset();
        }

        if (timer.time() < 1.15) drive(1,0); //1.15
        else if (timer.time() < 1.40) drive(0,-1); //1.4
        else if (timer.time() < 3.65) drive(1,0); //3.75, 3.5
        else if (timer.time() < 4.0) drive(0,-1); //4.0, 3.75
        else if (timer.time() < 5.4) drive(0.5,0); //5.2, 5.4
        else {
            setThrower(true);
            if (charge) {
                drive(0,0);
                setArm(Math.sin(timer.time() * 50));
                if (getEyeColor() == ColorDetected.COLOR_RED) { charge = true; timer.reset(); }
            } else {
                if (timer.time()<1) drive(1,0);
                else drive(0,0);
            }
        }
    }
}
