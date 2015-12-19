package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;
/**
 * Created by sathk_000 on 12/12/2015.
 */
public class RedBeaconRoute2 extends PacmanBotHWB2 {
    final static VersionNumber version = new VersionNumber(1,0,0);
    boolean set=false;
    boolean moveameoba=true;
    ElapsedTime timer=new ElapsedTime();
    boolean drive=false;

    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());
        setupHardware();
        timer.reset();
    }

    @Override
    public void loop() {
        if (moveameoba) {
            collector.setPower(-1);
        }
        else {
            collector.setPower(0);
        }

        if (!set) {
            set = true;
            timer.reset();
        }

        if (timer.time() < 4.5) {
            drive(1, 0); //1.15
        }
        else if (timer.time() < 4.8) {
            drive(0, -1); //1.4
        }
        else {
            drive(0, 0);
            moveameoba = false;
        }
    }
}
