package org.firstinspires.ftc.teamcode;


public class TurnCalc {
    private static final float WheelSpace = 14.375f;
    private static final float Pi = 3.1415926f;
    private static final float Equation = ((WheelSpace)/2) * (2*Pi);

        public static float Turn(float val) {
            return val/360f * Equation;

        }


}
