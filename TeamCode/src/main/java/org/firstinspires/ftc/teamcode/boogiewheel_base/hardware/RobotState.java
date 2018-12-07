package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

public class RobotState {

    public static MatchState currentMatchState = MatchState.UNKNOWN;

    public static DriveDirection currentDriveDirection = DriveDirection.FORWARD;

    public static MineralLiftState currentMineralLiftState = MineralLiftState.COLLECT_POSITION;
    public static MineralGatePosition currentMineralGatePosition = MineralGatePosition.CLOSED;

    public static RobotLiftState currentRobotLiftState = RobotLiftState.RAISED;

    public enum MatchState{
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
}
