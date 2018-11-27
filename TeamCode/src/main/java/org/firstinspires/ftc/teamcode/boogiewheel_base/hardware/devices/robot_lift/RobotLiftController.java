package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift;

import org.firstinspires.ftc.teamcode.framework.SubsystemController;

public class RobotLiftController extends SubsystemController{

    private RobotLift robotLift;

    public RobotLiftController(){
        init();
    }

    @Override
    public synchronized void init() {
        opModeSetup();

        robotLift = new RobotLift(hardwareMap);
    }

    public void robotLiftUp(){robotLift.setLiftPower(0.5);}
    public void robotLiftStop(){robotLift.setLiftPower(0);}
    public void robotLiftDown(){robotLift.setLiftPower(-0.5);}

    @Override
    public synchronized void stop() {
        robotLift.stop();
    }
}
