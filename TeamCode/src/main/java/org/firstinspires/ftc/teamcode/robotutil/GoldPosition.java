package org.firstinspires.ftc.teamcode.robotutil;

public enum GoldPosition {
    LEFT(-35), CENTER(0), RIGHT(35);
    private final double dir;
    GoldPosition(double dir) { this.dir = dir; }
    public double getValue() { return dir; }
}