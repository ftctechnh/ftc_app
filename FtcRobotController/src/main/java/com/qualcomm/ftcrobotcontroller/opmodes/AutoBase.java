package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

public class AutoBase extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    boolean done=false;
    ElapsedTime timer=new ElapsedTime();

    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());
        setupHardware();
    }
    public void BinaryAuto(int n){
        /*if((n/8)%2)
        {

        }*/
    }

    @Override
    public void loop() {
    }
}

