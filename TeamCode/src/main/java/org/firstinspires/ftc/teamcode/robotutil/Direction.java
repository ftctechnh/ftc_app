package org.firstinspires.ftc.teamcode.robotutil;

/**
 * Created by
 * Anton on 1/10/17.
 */

public enum Direction {
    FORWARD(1),BACK(-1),CW(1),CCW(-1),RIGHT(1),LEFT(-1),UP(1),DOWN(-1);

    private final int dir;
    Direction(int dir) { this.dir = dir; }
    public int getValue() { return dir; }
}
