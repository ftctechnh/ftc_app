package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

public enum GoldPosition {
    LEFT, CENTER, RIGHT;

    public int index;
    public String fileName;

    static {
        LEFT.index = 0;
        CENTER.index = 1;
        RIGHT.index = 2;
        LEFT.fileName = "Left";
        CENTER.fileName = "Center";
        RIGHT.fileName = "Right";
    }
}
