package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants;
import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class MineralLiftController extends SubsystemController {

    private MineralLift mineralLift;

    public MineralLiftController() {
        init();
    }

    @Override
    public synchronized void init() {
        opModeSetup();

        mineralLift = new MineralLift(hardwareMap);
    }

    public synchronized void moveToCollectPosition() {
        mineralLift.setPosition(Constants.MINERAL_LIFT_COLLECT_POSITION);
    }

    public synchronized void moveToDumpPosition() {
        mineralLift.setPosition(Constants.MINERAL_LIFT_DUMP_POSITION);
    }

    public synchronized void openGate() {
        mineralLift.setGateServoPosition(0.7);
    }

    public synchronized void closeGate() {
        mineralLift.setGateServoPosition(0);
    }

    @Override
    public synchronized void stop() {
        mineralLift.stop();
    }
}
