package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;
/**
 * Created by sathk_000 on 11/21/2015.
 */
public class RedMountainAutonomous extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    boolean set=false;
    ElapsedTime timer=new ElapsedTime();

    @Override
    public void init() {
        telemetry.addData("Program","Red Mountain");
        telemetry.addData("Version",version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());

        setupHardware();
    }

    @Override
    public void loop() {
        if (!set) {
            set = true;
            timer.reset();
        }
        setThrower(false);
        if (timer.time() < 1.15) drive(1, 0);

        else if (timer.time() < 1.45) drive(0, -1);

        else if (timer.time() < 3.35) drive(0.5, 0);

        else if (timer.time() < 4.0) drive(0, -0.5);

        else if (timer.time() < 7.0) drive(0.25, 0);

        else drive(0, 0);
    }
}
