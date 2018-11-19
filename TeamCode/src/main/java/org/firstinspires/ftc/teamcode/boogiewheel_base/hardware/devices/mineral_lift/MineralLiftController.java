package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift;

import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class MineralLiftController extends SubsystemController{

    private MineralLift mineralLift;

    public MineralLiftController(){
        init();
    }

    @Override
    public void init() {
        opModeSetup();

        mineralLift = new MineralLift(hardwareMap);
    }

    @Override
    public void stop() {
        mineralLift.stop();
    }
}
