package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.robot_lift;

import org.firstinspires.ftc.teamcode.framework.util.SubsystemController;

import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants.*;
import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState.*;

public class RobotLiftController extends SubsystemController {

    private RobotLift robotLift;

    public RobotLiftController() {
        init();
    }

    public synchronized void init() {
        opModeSetup();

        robotLift = new RobotLift(hardwareMap);
    }

    public synchronized void update(){

    }

    public synchronized void stop() {
        robotLift.stop();
    }

    public synchronized void robotLiftUp() {
        robotLift.setLiftPower(1);
    }

    public synchronized void robotLiftStop() {
        robotLift.setLiftPower(0);
    }

    public synchronized void robotLiftDown() {
        robotLift.setLiftPower(-0.5);
    }

    public synchronized void raiseLift() {
        robotLift.setPosition((ROBOT_LIFT_RAISED_POSITION));
        currentRobotLiftState = RobotLiftState.RAISED;
    }

    public synchronized void lowerLift() {
        robotLift.setPosition((ROBOT_LIFT_LOWERED_POSITION));
        currentRobotLiftState = RobotLiftState.LOWERED;
    }
}
