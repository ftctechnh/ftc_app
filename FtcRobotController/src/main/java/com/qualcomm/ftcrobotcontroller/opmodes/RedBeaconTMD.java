package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by sathk_000 on 12/3/2015.
 */
public class RedBeaconTMD extends PacmanBotHardwareBase {
    ElapsedTime timer = new ElapsedTime();
    boolean set = false ;
    //boolean charge = false;

    public void init() {
        setupHardware();
    }

    public void loop() {
        if(!set){
            set=true;
            timer.reset();
        }

        if (timer.time() < 1.25) drive(1,0); //1.15
        else if (timer.time() < 1.5) drive(0,-1); //1.4
        else if (timer.time() < 4.35) drive(1,0); //3.75, 3.5
        else if (timer.time() < 4.65) drive(0,-1); //4.0, 3.75
        else if (timer.time() < 6.3) drive(0.5,0); //5.2, 5.4
        else {
            drive(0, 0);
            timer.reset();
            setThrower(true);
            if (getEyeColor() == ColorDetected.COLOR_RED){
                telemetry.addData("RED", 0);
                if (timer.time() <0.2) drive(0,0);
                if (timer.time() < 0.35) drive(0.5,0);
                setArm(0.2);
                setArm(-0.2);
            }
            else if (getEyeColor() == ColorDetected.COLOR_BLUE) {
                telemetry.addData("BLUE", 0);
                if (timer.time() < 0.75) drive(-0.5,0);
                if (timer.time() < 0.95) drive(0,-0.5);
                if (timer.time() < 1.05) drive(0.5,0);
                if (timer.time() < 1.15) drive(0,0.5);
                if (timer.time() < 1.45) drive(0/5,0);
                setArm(0.2);
                setArm(-0.2);
            }
            /*if (charge) {
                drive(0,0);
                setArm(Math.sin(timer.time() * 50));
                if (getEyeColor() == ColorDetected.COLOR_RED) { charge = true;}
            } else {
                drive(.1,0);
            }
            */
        }
    }
}
