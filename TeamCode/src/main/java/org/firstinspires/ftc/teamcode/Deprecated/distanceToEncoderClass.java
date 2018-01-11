package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by BeehiveRobotics-3648 on 9/7/2017.
 */

public class distanceToEncoderClass {
     distanceToEncoderClass(double Diameter){
    C = Diameter * Math.PI;
    }
   double C = 1;
    int CPR = 1120;
    double encodertoDistance(int encoderClicks){

        double distance = ((encoderClicks / CPR) * C);

        return distance;

    }

    double distancetoEncoder(double distanceInInches){

        double clicks = (distanceInInches * CPR)/C;

        return clicks;
    }}
