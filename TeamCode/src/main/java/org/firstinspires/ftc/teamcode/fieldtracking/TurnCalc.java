package org.firstinspires.ftc.teamcode.fieldtracking;


public class TurnCalc {
    private static final double WheelSpace = 14.5;
    private static final float Pi = 3.1415926f;
    private static final double Equation = ((WheelSpace)/2) * (2*Pi);
    private static final double Closex1 = 2;
    private static final double Farx2 = 3.5;
    private static final double Y1= .2; //.25 is max speed before it starts to go haywire
    private static final double Y2= 0;  //.05 is max speed before it starts to go haywire
    private static final double m1 = (Y2 - Y1)/(Farx2-Closex1);
    private static final double m2 = (Y1 - Y2)/(Farx2-Closex1);
    private static final double b1 = Y1 - (m1*Closex1);
    private static final double b2 = Y2 - (m2*Closex1);
    private static final double Degoff1 = -1;
    private static final double Degoff2 = 1;
    private static final double Y1deg= .2;
    private static final double Y2deg= 0;
    private static final double m1deg = (Y2deg - Y1deg)/(Degoff2-Degoff1);
    private static final double m2deg = (Y1deg - Y2deg)/(Degoff2-Degoff1);
    private static final double b1deg = Y1deg - (m1deg*Degoff1);
    private static final double b2deg = Y2deg - (m2deg*Degoff2);
        public static double Turn(double val) {
            return val/360 * Equation;

        }
        public static double WallDistR(double odsval){
            return (m1 * odsval) + b1;


        }
        public static double WallDistL(double odsval){
            return (m2 * odsval) + b2;

        }
        public static double OffL(double Gyroval,double angle){
            return (m1deg * (Math.abs(Gyroval) - angle)) + b1deg;
        }
        public static  double OffR(double Gyroval,double angle){
        return  (m2deg * (Math.abs(Gyroval - angle))) + b2deg;

        }


    }
