package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;
/**
 * Created by sathk_000 on 12/12/2015.
 */
public class RedBeaconRoute2 extends PacmanBotHardwareBase {
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

            brush.setPower(-1);
        }
        else {

            brush.setPower(0);
        }
        if (!set) {
            set = true;
            timer.reset();
        }
        if (timer.time() < 2.8) {
            drive(1, 0); //1.15
        }
        else if (timer.time() < 3.35) {
            drive(0, -1); //1.4
        }
        else if (timer.time() < 4.75) {
            drive(1, 0); //3.75, 3.5
        }
        else if (timer.time() < 5.0) {
            drive(0, -1); //4.0, 3.75
        }
        else if (timer.time() < 6) {//5.4
            drive(0.5, 0); //5.2, 5.4
        }
        else {
            drive(0, 0);
            moveameoba = false;
        }
    }
}
