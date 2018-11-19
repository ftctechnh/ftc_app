package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift;

import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class RobotLiftController extends SubsystemController{

    private RobotLift robotLift;

    public RobotLiftController(){
        init();
    }

    @Override
    public void init() {
        opModeSetup();

        robotLift = new RobotLift(hardwareMap);
    }

    @Override
    public void stop() {
        robotLift.stop();
    }
}
