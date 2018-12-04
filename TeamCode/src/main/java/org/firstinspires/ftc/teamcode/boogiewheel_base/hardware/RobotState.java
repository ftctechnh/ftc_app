package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

public class RobotState {
    public static MineralLiftState mineralLiftState = MineralLiftState.COLLECT_POSITION;

    public enum MineralLiftState {
        COLLECT_POSITION,
        DUMP_POSITION,
        IN_MOTION
    }




}
