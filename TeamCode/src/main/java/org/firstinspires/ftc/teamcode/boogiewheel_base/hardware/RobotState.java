package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.framework.userHardware.paths.Path;

public final class RobotState {

    public static MatchState currentMatchState = MatchState.UNKNOWN;

    public static DriveDirection currentDriveDirection = DriveDirection.FORWARD;

    public static MineralLiftState currentMineralLiftState = MineralLiftState.COLLECT_POSITION;
    public static MineralGatePosition currentMineralGatePosition = MineralGatePosition.CLOSED;

    public static RobotLiftState currentRobotLiftState = RobotLiftState.RAISED;

    public static IntakeLiftState currentIntakeLiftState = IntakeLiftState.LOWERED;

    public static Path currentPath = null;

    public enum MatchState {
        AUTONOMOUS,
        TELEOP,
        UNKNOWN
    }

    public enum DriveDirection {
        FORWARD,
        REVERSED
    }

    public enum MineralLiftState {
        COLLECT_POSITION,
        DUMP_POSITION,
        IN_MOTION
    }

    public enum MineralGatePosition {
        OPEN,
        CLOSED
    }

    public enum RobotLiftState {
        RAISED,
        LOWERED,
        IN_MOTION
    }

    public enum IntakeLiftState {
        RAISED,
        LOWERED,
        IN_MOTION
    }
}
