package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.devices.mineral_lift;

import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;
import org.firstinspires.ftc.teamcode.framework.util.SubsystemController;

import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants.*;
import static org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.RobotState.*;

public class MineralLiftController extends SubsystemController {

    private MineralLift mineralLift;
    private boolean isMovingDown = false;
    private int[] liftValues = {-1, -1, -1};

    public MineralLiftController() {
        init();
    }

    public synchronized void init() {
        opModeSetup();

        mineralLift = new MineralLift(hardwareMap);
    }

    public synchronized void update() {
        if (isMovingDown) {
            int currentValue = mineralLift.getCurrentPosition();
            if (liftValues[0] == -1) {
                liftValues[0] = currentValue;
                liftValues[1] = currentValue;
                liftValues[2] = currentValue;
                return;
            }
            liftValues[2] = liftValues[1];
            liftValues[1] = liftValues[0];
            liftValues[0] = currentValue;
            if (atPosition(liftValues[0], liftValues[1], 2) && atPosition(liftValues[1], liftValues[2], 2) && mineralLift.getCurrentPosition() < 500) {
                mineralLift.resetPosition();
                liftValues[0] = -1;
                liftValues[1] = -1;
                liftValues[2] = -1;
                isMovingDown = false;
                return;
            }
        }
    }

    public synchronized void stop() {
        mineralLift.stop();
    }

    public synchronized void autonLowerLiftSequence() {
        mineralLift.setTargetPosition(300);
        delay(2000);
        mineralLift.setCurrentPosition(600);
        mineralLift.setTargetPosition(MINERAL_LIFT_COLLECT_POSITION);
        while (AbstractOpMode.isOpModeActive()) {
            int currentValue = mineralLift.getCurrentPosition();
            if (liftValues[0] == -1) {
                liftValues[0] = currentValue;
                liftValues[1] = currentValue;
                liftValues[2] = currentValue;
                continue;
            }
            liftValues[2] = liftValues[1];
            liftValues[1] = liftValues[0];
            liftValues[0] = currentValue;
            if (atPosition(liftValues[0], liftValues[1], 1) && atPosition(liftValues[1], liftValues[2], 1) && mineralLift.getCurrentPosition() < 100) {
                mineralLift.resetPosition();
                liftValues[0] = -1;
                liftValues[1] = -1;
                liftValues[2] = -1;
                isMovingDown = false;
                return;
            }
        }
    }

    public synchronized void moveToCollectPosition() {

        if (mineralLift.getDistance() < 20) return;
        currentMineralLiftState = MineralLiftState.IN_MOTION;
        mineralLift.setTargetPosition(MINERAL_LIFT_COLLECT_POSITION);
        isMovingDown = true;
        //while (mineralLift.isLiftInProgress());
        currentMineralLiftState = MineralLiftState.COLLECT_POSITION;
    }

    public synchronized void moveToDumpPosition() {
        currentMineralLiftState = MineralLiftState.IN_MOTION;
        mineralLift.setTargetPosition(MINERAL_LIFT_DUMP_POSITION);
        //while (mineralLift.isLiftInProgress());
        currentMineralLiftState = MineralLiftState.DUMP_POSITION;
    }

    public synchronized void openGate() {
        mineralLift.setGateServoPosition(MINERAL_GATE_OPEN_POSITION);
        currentMineralGatePosition = MineralGatePosition.OPEN;
    }

    public synchronized void closeGate() {
        mineralLift.setGateServoPosition(MINERAL_GATE_CLOSED_POSITION);
        currentMineralGatePosition = MineralGatePosition.CLOSED;
    }

    public synchronized void toggleGate() {
        if (currentMineralGatePosition == MineralGatePosition.OPEN) closeGate();
        else openGate();
    }

    private boolean atPosition(double x, double y, double error) {
        double upperRange = x + error;
        double lowerRange = x - error;

        return y >= lowerRange && y <= upperRange;
    }
}
