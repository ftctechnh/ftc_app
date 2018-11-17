package org.firstinspires.ftc.teamcode.robotutil;

/**
 * Created by antonlin on 4/23/18.
 */

public class Values {

    public static double TICKS_PER_INCH_FORWARD = 60;
    public static double HANG_TICKS_PER_INCH = 1000;
    // 61.3
    public static double TICKS_PER_INCH_STRAFE = 103;

    public static double gyroTurnErrorMargin = 3;
    public static double minRotationPower = 0.07;
    //Driving distance/point
    public static double driveKp = 0.0;
    public static double driveKi = 0.0;
    public static double driveKd = 0.0;

    //Rotating to angle
    public static double rotationKi = 0.0;
    public static double rotationKp = 0.0;
    public static double rotationKd = 0.0;

    //Holding angle position
    public static double angleHoldKi = 0.0;
    public static double angleHoldKp = 0.0;
    public static double angleHoldKd = 0.0;


    //SERVO INIT VALUES
    public static double flipDownPos = 0.56;
    public static int goodAngle = 0;
    public static double flipInterPos = flipDownPos - 0.1;
    public static double flipUpPos = flipDownPos - 0.5;
    public static double cryptoDownPos = 0.56;
    public static double cryptoUpPos = cryptoDownPos - 0.5;
    //AUTONOMOUS DISTANCE VALUES

    public enum RotationMode {
        ABSOLOUTE,RELATIVE
    }

    public enum Alliance{
        BLUE,RED
    }
    public enum JewelColor{
        BLUE,RED
    }
}
