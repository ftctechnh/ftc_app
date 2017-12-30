package org.firstinspires.ftc.teamcode.libraries.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Noah on 12/28/2017.
 * Library which takes the exact degrees of the whacky stick and
 * converts it to an extension distance in MM
 */

public final class WhackyDistLib {
    //arbitrary unexplained constants
    private static final double a = 1.1711272;
    private static final double b = 0.4534887;
    private static final double c = 1.0799188;
    private static final double d = 1.1065445;
    private static final double e = 10.370;
    private static final double g  = 9.370;
    private static final double h = 2.000;
    private static final double i = 9.688;

    private static final double u = g - e;
    private static final double v = d / c;
    private static final double w = (d * a) / c - b;

   public static double getWhackyDistance(double degrees, DistanceUnit unit) {
       final double theta = Math.toRadians(degrees);
       final double sintheta = Math.sin(theta);
       final double costheta = Math.cos(theta);
       final double Gx = g*costheta;
       final double Gy = g*sintheta;
       final double Dx = (-Math.sqrt(Math.pow(u*v*sintheta - u*costheta + v*w, 2) - (Math.pow(v, 2) + 1)*(Math.pow(u, 2) + Math.pow(w, 2) - Math.pow(h, 2) + 2*u*w*sintheta)) + u*costheta - u*v*sintheta - v*w) / (Math.pow(v, 2) + 1);
       final double Fx = Dx + e*costheta;
       final double Fy = (-d/c)*(Dx + a) + b + e*sintheta;
       final double m = (Gy - Fy)/(Gx - Fx);
       final double phi = Math.atan(m);
       final double Ix = Gx + (i-h)*Math.cos(phi);

       if(unit == DistanceUnit.MM) return Ix * DistanceUnit.mmPerInch;
       if(unit == DistanceUnit.CM) return (Ix * DistanceUnit.mmPerInch) / 10.0;
       if(unit == DistanceUnit.METER) return (Ix * DistanceUnit.mmPerInch) / 1000.0;
       else return Ix;
   }

   public static double getWhackyDistance(double degrees) {
       return getWhackyDistance(degrees, DistanceUnit.MM);
   }

    public static double getWhackyPosFromDegrees(double degrees) {
        return BotHardware.ServoE.stick0 - (degrees / 90.0) * (BotHardware.ServoE.stick0 - BotHardware.ServoE.stick90);
    }
}
