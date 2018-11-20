package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift;

import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class MineralLiftController extends SubsystemController{

    private MineralLift mineralLift;

    private final int COLLECT_POSITION = 0;
    private final int DUMP_POSITION = 1000;

    public MineralLiftController(){
        init();
    }

    @Override
    public void init() {
        opModeSetup();

        mineralLift = new MineralLift(hardwareMap);
    }

    public void moveToCollectPosition(){
        mineralLift.setPosition(COLLECT_POSITION);
    }

    public void moveToDumpPosition(){
        mineralLift.setPosition(DUMP_POSITION);
    }

    @Override
    public void stop() {
        mineralLift.stop();
    }
}
