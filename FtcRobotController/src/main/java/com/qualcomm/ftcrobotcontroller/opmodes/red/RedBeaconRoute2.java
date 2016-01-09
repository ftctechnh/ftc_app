package com.qualcomm.ftcrobotcontroller.opmodes.red;

import com.qualcomm.ftcrobotcontroller.opmodes.PacmanBotHardwareBase3;
import com.qualcomm.robotcore.util.ElapsedTime;
/**
 * Created by sathk_000 on 12/12/2015.
 */
public class RedBeaconRoute2 extends PacmanBotHardwareBase3 {
    final static String version = "1.0.0";
    boolean set=false;
    boolean moveameoba=true;
    ElapsedTime timer=new ElapsedTime();
    boolean driveEnable=false;

    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version);
        telemetry.addData("Hardware Base Version", hwbVersion);
        side=false;
        setupHardware();
        timer.reset();
    }
    double turnTime = 0.5;
    double forwardTime = 0.55;
    @Override
    public void loop() {
        if (moveameoba) {
            //collector.setPower(-1);
        }
        else {
            //collector.setPower(0);
        }

        if (!set) {
            set = true;
            timer.reset();
        }

        if (timer.time() < forwardTime){
            drive.driveStd(0.25, 0.0);
        }
        else if (timer.time() < forwardTime+turnTime){
            drive.driveStd(0, -0.25);
        }
        else if (timer.time() < forwardTime+turnTime+5.95) {
            drive.driveStd(0.25, 0); //1.15
        }
        /*
        else if (timer.time() < forwardTime+turnTime+5.35) {
            drive.driveStd(0, 0.25);
        }
        */
        else if (timer.time() < forwardTime+turnTime+6.45) {
            drive.driveStd(0, -0.25);
        }
        else if (timer.time() < forwardTime+turnTime+7.45   ) {
            drive.driveStd(0.25,0);
        }
        /*
        else if (timer.time()<forwardTime+turnTime+8.27) {
            drive.driveStd(0, -0.25);
        }
        else if (timer.time()<forwardTime+turnTime+13.75) {
            drive.driveStd(0.25, 0);
        }
        else if (timer.time()<forwardTime+turnTime+13.95) {
            drive.driveStd(0, -0.25);
        }
        */
        else {
            drive.driveStd(0, 0);
            //setDumper (true);
            moveameoba = false;
        }

    }
}
