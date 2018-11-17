package org.firstinspires.ftc.teamcode.robotutil;

public enum GoldPosition {
    LEFT(-35), CENTER(0), RIGHT(35);
    private final int dir;
    GoldPosition(int dir) { this.dir = dir; }
    public int getValue() { return dir; }
}